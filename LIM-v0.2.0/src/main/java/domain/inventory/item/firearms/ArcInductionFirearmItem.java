package domain.inventory.item.firearms;


import domain.inventory.item.misc.ElectromagneticPortableBatteryItem;
import domain.inventory.InventoryState;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.ammunition.AmmunitionDescriptor;
import domain.inventory.item.ammunition.AmmunitionFamily;
import domain.inventory.item.ammunition.AmmunitionLoadResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/** Lanza-Arcos Electrodinámico V881: tres bobinas, condensadores y batería portátil obligatoria. */
public final class ArcInductionFirearmItem extends FirearmItem {
    public static final double RANGE_METERS = 3.0;
    public static final double BASE_WEIGHT_WITHOUT_SLING_KG = 2.62;
    public static final double LENGTH_METERS = 0.88;
    public static final double WIDTH_METERS = 0.22;
private double storedElectricalEnergyJ;
    private double thermalLockRemainingSeconds;
    private ArcDischargeProfile lastDischarge;
    private ElectromagneticPortableBatteryItem installedBattery;

    public ArcInductionFirearmItem(String narrativeDescription) {
        super(
                "Lanza-Arcos Electrodinámico V881", narrativeDescription, BASE_WEIGHT_WITHOUT_SLING_KG,
                LENGTH_METERS, WIDTH_METERS, RANGE_METERS, "Sin munición",
                new FirearmCartridge("Reserva eléctrica interna", "Sin munición", "Ninguno", "Sin proyectil", 1, 0.0),
                new LethalityProfile(0, 0, 0), 0.0, List.of(FireMode.ONE_A), false, true, Set.<WeaponTrait>of()
        );
    }

    @Override public boolean supportsAiming() { return false; }
    @Override public AmmunitionDescriptor ammunitionRequirement() {
        return new AmmunitionDescriptor(AmmunitionFamily.BULLET, "Sin munición", "Ninguno", "Sin proyectil", false);
    }
    @Override public AmmunitionLoadResult reloadFromInventory(InventoryState inventory) {
        return AmmunitionLoadResult.rejected("El Lanza-Arcos no utiliza munición ni cargador; requiere Batería Portátil Electromagnética V881.");
    }
    @Override public void reloadFullCartridge() { triggerState().release(); }

    public String energyStorage() { return "Banco de condensadores V881 de 1.650 J repartido entre tres bobinas de inducción."; }
    public String primaryPowerSource() { return "Batería Portátil Electromagnética V881 acoplada como asistencia estable del banco trifásico; su autonomía no limita el uso ordinario del Lanza-Arcos."; }
    public String secondaryPowerSource() { return "Manivela preferente automática; el personaje la acciona mientras ninguna acción de mayor prioridad lo interrumpa."; }
    public Optional<ElectromagneticPortableBatteryItem> installedBattery() { return Optional.ofNullable(installedBattery); }
    public boolean installBattery(ElectromagneticPortableBatteryItem battery) { if (battery == null || installedBattery != null) return false; installedBattery = battery; return true; }
    public Optional<ElectromagneticPortableBatteryItem> removeBattery() { ElectromagneticPortableBatteryItem old = installedBattery; installedBattery = null; return Optional.ofNullable(old); }
    public boolean operationalBatteryInstalled() { return installedBattery != null; }

    public double storedElectricalEnergyJ() { return storedElectricalEnergyJ; }
    public double crankTurns() { return storedElectricalEnergyJ / ArcChargePolicy.JOULES_PER_EQUIVALENT_TURN; }
    public double batteryChargeRatio() { return installedBattery == null ? 0.0 : installedBattery.chargeRatio(); }
    public boolean hasElectricalCharge() { return storedElectricalEnergyJ > 0.000001; }
    public boolean chargeFull() { return storedElectricalEnergyJ + 0.000001 >= ArcChargePolicy.MAX_STORED_ELECTRICAL_ENERGY_J; }
    public boolean triggerThermallyLocked() { return thermalLockRemainingSeconds > 0.0; }
    public double thermalLockRemainingSeconds() { return thermalLockRemainingSeconds; }
    public ArcDischargeProfile currentDischargeProfile() { return ArcChargePolicy.resolve(crankTurns()); }
    public ArcDischargeProfile lastDischargeProfile() { return lastDischarge; }
    public int maxPrimaryChannels() { return Integer.MAX_VALUE; }
    public boolean supportsUnboundedArcDistribution() { return true; }
    public String confidentialElectricalParameters() {
        return "CONFIDENCIAL — tensión, corriente, duración de pulso, geometría de electrodos, devanados y dieléctricos.";
    }

    @Override public List<String> statistics() {
        List<String> stats = new ArrayList<>();
        stats.add("Alcance | 3.0 m");
        stats.add("Munición | Ninguna");
        stats.add("Alimentación | Batería Portátil Electromagnética V881 acoplada + manivela preferente automática");
        stats.add("Autonomía | La batería no exige ciclos de cargador durante el uso ordinario del Lanza-Arcos");
        stats.add("Condensadores | 1.650 J máximos");
        stats.add("Bobinas | 3; carga continua; umbrales 3 / 5 / 6 vueltas equivalentes");
        stats.add("Carga preferente | 1,2 / 2,1 / 3,0 s; se puede disparar desde 1,2 s o seguir cargando");
        stats.add("Letalidad especial | ELÉCTRICO 0–300; la reserva se reparte entre todos los objetivos válidos");
        stats.add("SACUDIDA | 25 / 50 / 75");
        stats.add("AIMING | No utiliza");
        stats.add("Desgaste | No");
        if (!supportedFirearmAccessoryMounts().isEmpty()) stats.add("ADMITE ACCESORIOS | " + supportedFirearmAccessoryMounts().stream().map(Enum::name).sorted().toList());
        return List.copyOf(stats);
    }

    public boolean beginManualCharge() {
        if (!operationalBatteryInstalled()) return false;
        if (chargeFull()) { leaveArcManualChargeState(); return false; }
        enterArcManualChargeState();
        return true;
    }

    public boolean turnCrankOneRevolution() { return advanceManualCharge(1.0); }

    public boolean advanceManualCharge(double revolutions) {
        if (handlingState() != FirearmHandlingState.ARC_MANUAL_CHARGE) throw new IllegalStateException("El personaje no está sujetando la manivela del Lanza-Arcos.");
        if (!operationalBatteryInstalled()) throw new IllegalStateException("El Lanza-Arcos requiere una Batería Portátil Electromagnética V881 acoplada.");
        if (!Double.isFinite(revolutions) || revolutions <= 0) throw new IllegalArgumentException("El avance debe ser positivo y finito.");
        if (chargeFull()) { leaveArcManualChargeState(); return false; }
        addStoredEnergy(revolutions * ArcChargePolicy.JOULES_PER_EQUIVALENT_TURN);
        if (chargeFull()) leaveArcManualChargeState();
        return true;
    }

    /**
     * Acción de carga manual preferente, equivalente al patrón operativo del
     * Bifilar pero continua: el personaje acciona la manivela automáticamente
     * mientras no haya una acción de mayor prioridad. La batería estabiliza y
     * asiste el banco sin consumir una reserva relevante para gameplay.
     */
    public double advancePreferredManualCharge(double elapsedSeconds, boolean actionAvailable) {
        if (!Double.isFinite(elapsedSeconds) || elapsedSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        if (!actionAvailable || elapsedSeconds == 0 || chargeFull() || !operationalBatteryInstalled()) return 0.0;
        double beforeTurns = crankTurns();
        double beforeSeconds = ArcChargePolicy.preferredChargeSecondsForTurns(beforeTurns);
        double afterTurns = ArcChargePolicy.turnsAfterPreferredChargeSeconds(beforeSeconds + elapsedSeconds);
        addStoredEnergy((afterTurns - beforeTurns) * ArcChargePolicy.JOULES_PER_EQUIVALENT_TURN);
        return Math.max(0.0, storedElectricalEnergyJ - beforeTurns * ArcChargePolicy.JOULES_PER_EQUIVALENT_TURN);
    }

    /** Alias de compatibilidad con . */
    public double advanceAutomaticCharge(double elapsedSeconds, boolean actionAvailable) {
        return advancePreferredManualCharge(elapsedSeconds, actionAvailable);
    }

    public boolean cancelManualCharge() {
        if (handlingState() != FirearmHandlingState.ARC_MANUAL_CHARGE) return false;
        leaveArcManualChargeState();
        return true;
    }

    public void advanceThermalTime(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("El tiempo debe ser finito y no negativo.");
        thermalLockRemainingSeconds = Math.max(0.0, thermalLockRemainingSeconds - seconds);
    }

    public double electricalIntensityPerTarget(int validTargets) { return currentDischargeProfile().electricalIntensityPerTarget(validTargets); }

    @Override protected boolean canConsumeShot() { return operationalBatteryInstalled() && hasElectricalCharge() && !triggerThermallyLocked(); }

    @Override protected void consumeShot() {
        ArcDischargeProfile shot = currentDischargeProfile();
        if (!operationalBatteryInstalled()) throw new IllegalStateException("El Lanza-Arcos requiere una Batería Portátil Electromagnética V881 acoplada.");
        if (shot.offensiveReserve() <= 0) throw new IllegalStateException("Carga eléctrica 0: el Lanza-Arcos no descarga.");
        if (triggerThermallyLocked()) throw new IllegalStateException("El Lanza-Arcos permanece bloqueado por temperatura.");
        recoilState().registerShot(0.0);
        triggerState().registerShot();
        lastDischarge = shot;
        thermalLockRemainingSeconds = shot.thermalLockSeconds();
        storedElectricalEnergyJ = 0.0;
    }

    @Override public String destabilizingTechniqueDescription() { return "Golpe desestabilizador mediante patada frontal."; }

    private void addStoredEnergy(double joules) {
        storedElectricalEnergyJ = Math.min(ArcChargePolicy.MAX_STORED_ELECTRICAL_ENERGY_J, storedElectricalEnergyJ + Math.max(0.0, joules));
    }
}
