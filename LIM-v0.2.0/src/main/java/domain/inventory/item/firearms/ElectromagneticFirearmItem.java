package domain.inventory.item.firearms;


import domain.inventory.item.misc.ElectromagneticPortableBatteryItem;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.ammunition.AmmunitionDescriptor;
import domain.inventory.item.ammunition.AmmunitionFamily;
import domain.inventory.item.WeaponTrait;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/** Fusil bifilar : cinco estados P, batería automática y respaldo dinamoeléctrico. */
public final class ElectromagneticFirearmItem extends FirearmItem {
    public static final double MAX_STORED_ELECTRICAL_ENERGY_J = 1650.0;
    public static final double CRANK_ARM_LENGTH_M = 0.15;
    public static final double CRANK_REVOLUTIONS_PER_SECOND = 1.0;
    private final FirearmProjectileDefinition projectile;
    private final ElectromagneticThermalLock thermalLock = new ElectromagneticThermalLock();
    private double storedElectricalEnergyJ;
    private ElectromagneticChargeSetting selectedSetting = ElectromagneticChargeSetting.P50;
    private ElectromagneticPortableBatteryItem installedBattery;
    private ElectromagneticShotProfile lastFiredShotProfile;

    public ElectromagneticFirearmItem(String name, String narrativeDescription, double weightKg, double lengthMeters,
            double widthMeters, String caliber, FirearmCartridge cartridgeDefinition,
            FirearmProjectileDefinition projectile, List<FireMode> fireModes,
            boolean supportsOneHanded, boolean supportsTwoHanded, Set<WeaponTrait> traits) {
        super(name, narrativeDescription, weightKg, lengthMeters, widthMeters, 420.0, caliber, cartridgeDefinition,
                new LethalityProfile(90, 0, 0), ElectromagneticChargePolicy.resolve(35, weightKg + 0.6).recoilVelocityMps(),
                fireModes, supportsOneHanded, supportsTwoHanded, traits);
        this.projectile = projectile;
        if (!caliber.equals(projectile.caliber())) throw new IllegalArgumentException("El calibre del proyectil no coincide con el arma.");
    }

    @Override public AmmunitionDescriptor ammunitionRequirement() {
        return new AmmunitionDescriptor(AmmunitionFamily.CARTRIDGE, projectile.caliber(),
                "Núcleo de tungsteno, armadura conductora y sabot separable", "Cartucho bifilar unitario", false);
    }
    public FirearmProjectileDefinition projectile() { return projectile; }
    public String railArchitecture() { return "Dos raíles longitudinales de CuCrZr con recubrimiento CuCrZr-W."; }
    public String energyStorage() { return "Banco de condensadores de mica y papel parafinado V881; 1.650 J máximos."; }
    public String primaryPowerSource() { return "Batería Portátil Electromagnética V881 recargable de dos celdas 21700."; }
    public String secondaryPowerSource() { return "Dinamo automática con manivela de 0,15 m a una revolución por segundo."; }
    public String operationalRole() { return "Fusil pesado de posición y penetración discrecional P50-P90."; }
    public boolean fulminatingPropertyPresent() { return true; }
    public boolean electricChargeGaugePresent() { return true; }
    /** Alias históricos redirigidos a los tres umbrales superiores . */
    public double softThresholdIPiercing() { return ElectromagneticChargeSetting.P70.piercing(); }
    public double softThresholdIIPiercing() { return ElectromagneticChargeSetting.P80.piercing(); }
    public double hardThresholdPiercing() { return ElectromagneticChargeSetting.P90.piercing(); }
    public double thermalLockRemainingSeconds() { return thermalLock.remainingSeconds(); }
    public boolean triggerThermallyLocked() { return thermalLock.locked(); }
    public double storedElectricalEnergyJ() { return storedElectricalEnergyJ; }
    public double crankTurns() { return storedElectricalEnergyJ / ElectromagneticChargePolicy.JOULES_PER_EQUIVALENT_TURN; }
    public ElectromagneticChargeSetting selectedSetting() { return selectedSetting; }
    public Optional<ElectromagneticPortableBatteryItem> installedBattery() { return Optional.ofNullable(installedBattery); }
    public boolean installBattery(ElectromagneticPortableBatteryItem battery) { if (battery == null || installedBattery != null) return false; installedBattery = battery; return true; }
    public Optional<ElectromagneticPortableBatteryItem> removeBattery() { ElectromagneticPortableBatteryItem old=installedBattery; installedBattery=null; return Optional.ofNullable(old); }
    public boolean hasElectricalCharge() { return storedElectricalEnergyJ + 0.000001 >= selectedSetting.storedEnergyJ(); }
    public boolean isChargeFull() { return storedElectricalEnergyJ + 0.000001 >= MAX_STORED_ELECTRICAL_ENERGY_J; }

    public ElectromagneticShotProfile currentShotProfile() { return ElectromagneticChargePolicy.resolve(crankTurns(), operationalMassKg()); }
    public ElectromagneticShotProfile selectedShotProfile() { return ElectromagneticChargePolicy.resolve(selectedSetting, operationalMassKg()); }
    public ElectromagneticShotProfile maximumShotProfile() { return ElectromagneticChargePolicy.resolve(ElectromagneticChargeSetting.P90, operationalMassKg()); }
    public Optional<ElectromagneticShotProfile> lastFiredShotProfile() { return Optional.ofNullable(lastFiredShotProfile); }

    public boolean beginChargeSelection() { enterElectromagneticManualChargeState(); return true; }
    public ElectromagneticChargeSetting cycleChargeSetting() {
        ElectromagneticChargeSetting next = selectedSetting.next();
        if (next.storedEnergyJ() + 0.000001 < storedElectricalEnergyJ) return selectedSetting;
        selectedSetting = next;
        return selectedSetting;
    }
    public boolean exitChargeSelection() {
        if (handlingState() != FirearmHandlingState.ELECTROMAGNETIC_CHARGE_SELECTION) return false;
        leaveElectromagneticManualChargeState(); return true;
    }

    /** Acción preferente automática: batería y manivela avanzan en paralelo mientras no se interrumpa. */
    public double advanceAutomaticCharge(double elapsedSeconds, boolean actionAvailable) {
        if (!Double.isFinite(elapsedSeconds) || elapsedSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        if (!actionAvailable || elapsedSeconds == 0 || hasElectricalCharge()) return 0.0;
        double target = selectedSetting.storedEnergyJ();
        double missing = target - storedElectricalEnergyJ;
        double manual = Math.min(missing, elapsedSeconds * CRANK_REVOLUTIONS_PER_SECOND * ElectromagneticChargePolicy.JOULES_PER_EQUIVALENT_TURN);
        storedElectricalEnergyJ += manual;
        missing = target - storedElectricalEnergyJ;
        double battery = 0;
        if (missing > 0 && installedBattery != null && !installedBattery.depleted()) {
            double totalWindow = selectedSetting.thermalLockSeconds();
            double manualExpected = totalWindow * CRANK_REVOLUTIONS_PER_SECOND * ElectromagneticChargePolicy.JOULES_PER_EQUIVALENT_TURN;
            double requiredBatteryPower = Math.max(0, target - manualExpected) / totalWindow;
            battery = installedBattery.draw(Math.min(missing, requiredBatteryPower * elapsedSeconds));
            storedElectricalEnergyJ += battery;
        }
        storedElectricalEnergyJ = Math.min(target, storedElectricalEnergyJ);
        return manual + battery;
    }

    public ElectromagneticChargeGaugeReading chargeGaugeReading() {
        ElectromagneticShotProfile profile=currentShotProfile();
        return new ElectromagneticChargeGaugeReading(crankTurns(), storedElectricalEnergyJ,
                profile.lethality().piercing(), profile.effectiveRangeMeters(), profile.thermalLockSeconds(),
                selectedSetting, Math.max(0, selectedSetting.equivalentTurns()-crankTurns()),
                installedBattery == null ? 0 : installedBattery.chargeRatio());
    }
    public void advanceThermalTime(double elapsedSeconds) { thermalLock.advance(elapsedSeconds); }
    @Override protected boolean canConsumeShot() { return super.canConsumeShot() && hasElectricalCharge() && !triggerThermallyLocked(); }
    @Override protected void consumeShot() {
        ElectromagneticShotProfile shot=selectedShotProfile();
        if (!hasElectricalCharge()) throw new IllegalStateException("El banco no ha alcanzado " + selectedSetting + ".");
        if (triggerThermallyLocked()) throw new IllegalStateException("El gatillo permanece bloqueado por temperatura.");
        consumeAmmunitionAndRegisterShot(shot.recoilVelocityMps());
        lastFiredShotProfile=shot; thermalLock.engage(shot.thermalLockSeconds()); storedElectricalEnergyJ=0;
    }
    @Override public String destabilizingTechniqueDescription() { return "Golpe desestabilizador mediante patada frontal."; }
    public boolean isCurrentChargeFulminatingImpact(domain.combat.ArmorCombatHitbox hitbox,double coveragePercent,double piercingProtection){return FulminatingPolicy.isFulminatingImpact(hitbox,coveragePercent,piercingProtection,selectedShotProfile().lethality().piercing());}
    public boolean isCurrentChargeFulminatingHeadImpact(double c,double p){return isCurrentChargeFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET,c,p);}
    public boolean isLastShotFulminatingImpact(domain.combat.ArmorCombatHitbox hitbox,double c,double p){var s=lastFiredShotProfile().orElseThrow();return FulminatingPolicy.isFulminatingImpact(hitbox,c,p,s.lethality().piercing());}
    public boolean isLastShotFulminatingHeadImpact(double c,double p){return isLastShotFulminatingImpact(domain.combat.ArmorCombatHitbox.HELMET,c,p);}
    public ElectromagneticShotSignature currentShotSignature(){double r=crankTurns()/35.0;if(r<=0)return new ElectromagneticShotSignature("Sin descarga.","Sin destello.");if(r<14.0/35.0)return new ElectromagneticShotSignature("Golpe eléctrico grave y contenido.","Destello azul-blanquecino tenue.");if(r<23.0/35.0)return new ElectromagneticShotSignature("Descarga electromecánica grave.","Destello azul-blanquecino definido.");return new ElectromagneticShotSignature("Descarga electromecánica grave de máxima intensidad.","Destello azul-blanquecino intenso.");}
    private double operationalMassKg(){return weightKg() + (installedBattery==null?0:installedBattery.weightKg()) + cartridgeDefinition().weightKg();}
}
