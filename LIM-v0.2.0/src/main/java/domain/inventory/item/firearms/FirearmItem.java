package domain.inventory.item.firearms;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.InventoryState;
import domain.inventory.item.ammunition.AmmunitionInventoryPolicy;
import domain.inventory.item.ammunition.AmmunitionDescriptor;
import domain.inventory.item.ammunition.AmmunitionLoadResult;
import domain.inventory.item.AttributeRequirement;
import domain.inventory.item.GripMode;
import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponRequirementPolicy;
import domain.inventory.item.WeaponGripEligibility;
import domain.inventory.item.WeaponGripEligibilityPolicy;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.firearmAccessories.FirearmAccessoryMount;
import domain.inventory.item.firearmAccessories.FirearmAccessoryItem;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Base de armas de fuego V881. No se desgastan.
 * Mantiene separados munición, estado de disparo, cadencia, retroceso y accesorios desmontables.
 */
public class FirearmItem extends InventoryEntry {
    public static final double IDEAL_ONE_HANDED_MAX_WEIGHT_KG = 1.0;
    public static final double IDEAL_ONE_HANDED_MAX_LENGTH_M = 0.50;
    private final double baseWeightKg;
    private final double lengthMeters;
    private final double widthMeters;
    private final double effectiveRangeMeters;
    private final String caliber;
    private final FirearmCartridge cartridgeDefinition; // nulo solo en alimentación unitaria sin cartucho físico
    private final FirearmLoadDefinition loadDefinition;
    private final LethalityProfile lethalityProfile;
    private final double recoilVelocityPerShotMps;
    private final List<FireMode> fireModes;
    private FireMode activeFireMode;
    private final boolean supportsOneHanded;
    private final boolean supportsTwoHanded;
    private final Set<WeaponTrait> traits;
    private List<ItemProperty> itemProperties = List.of();
    private Set<FirearmAccessoryMount> supportedFirearmAccessoryMounts = Set.of();
    private final EnumMap<FirearmAccessoryMount, FirearmAccessoryItem> mountedAttachments = new EnumMap<>(FirearmAccessoryMount.class);

    private int ammunitionRemaining;
    private FirearmHandlingState handlingState = FirearmHandlingState.NORMAL;
    private final FirearmRecoilState recoilState = new FirearmRecoilState();
    private final FirearmTriggerState triggerState = new FirearmTriggerState();

    public FirearmItem(
            String name,
            String narrativeDescription,
            double weightKg,
            double lengthMeters,
            double widthMeters,
            double effectiveRangeMeters,
            String caliber,
            FirearmCartridge cartridgeDefinition,
            LethalityProfile lethalityProfile,
            double recoilVelocityPerShotMps,
            List<FireMode> fireModes,
            boolean supportsOneHanded,
            boolean supportsTwoHanded,
            Set<WeaponTrait> traits
    ) {
        super(name, narrativeDescription, weightKg,
                domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintForOrMetricFallback(name, widthMeters, lengthMeters),
                statistics(effectiveRangeMeters, caliber, cartridgeDefinition, lethalityProfile,
                        recoilVelocityPerShotMps, fireModes, supportsOneHanded, supportsTwoHanded));
        if (!Double.isFinite(weightKg) || weightKg < 0) {
            throw new IllegalArgumentException("El peso base debe ser finito y no negativo.");
        }
        if (!Double.isFinite(lengthMeters) || lengthMeters <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva y finita.");
        }
        if (!Double.isFinite(widthMeters) || widthMeters <= 0) {
            throw new IllegalArgumentException("La anchura debe ser positiva y finita.");
        }
        if (!Double.isFinite(effectiveRangeMeters) || effectiveRangeMeters <= 0) {
            throw new IllegalArgumentException("El alcance debe ser positivo y finito.");
        }
        if (!Double.isFinite(recoilVelocityPerShotMps) || recoilVelocityPerShotMps < 0) {
            throw new IllegalArgumentException("El retroceso debe ser finito y no negativo.");
        }
        if (!supportsOneHanded && !supportsTwoHanded) {
            throw new IllegalArgumentException("Un arma de fuego debe admitir al menos un tipo de agarre.");
        }
        this.baseWeightKg = weightKg;
        this.lengthMeters = lengthMeters;
        this.widthMeters = widthMeters;
        this.effectiveRangeMeters = effectiveRangeMeters;
        this.caliber = requireText(caliber, "El calibre no puede estar vacío.");
        this.cartridgeDefinition = Objects.requireNonNull(cartridgeDefinition, "El cartucho no puede ser nulo.");
        this.loadDefinition = FirearmLoadDefinition.fromCartridge(cartridgeDefinition);
        if (!this.caliber.equals(cartridgeDefinition.caliber())) {
            throw new IllegalArgumentException("El calibre del cartucho no coincide con el del arma.");
        }
        this.lethalityProfile = Objects.requireNonNull(lethalityProfile, "La letalidad no puede ser nula.");
        this.recoilVelocityPerShotMps = recoilVelocityPerShotMps;
        this.fireModes = List.copyOf(Objects.requireNonNull(fireModes, "Las cadencias no pueden ser nulas."));
        if (this.fireModes.isEmpty() || this.fireModes.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("El arma debe disponer de al menos una cadencia válida.");
        }
        this.activeFireMode = this.fireModes.getFirst();
        this.supportsOneHanded = supportsOneHanded;
        this.supportsTwoHanded = supportsTwoHanded;
        this.traits = Set.copyOf(Objects.requireNonNull(traits, "Las propiedades no pueden ser nulas."));
        this.ammunitionRemaining = loadDefinition.capacity();
    }

    /** Constructor para plataformas de alimentación unitaria sin cartucho/cargador físico. */
    protected FirearmItem(
            String name, String narrativeDescription, double weightKg, double lengthMeters, double widthMeters,
            double effectiveRangeMeters, String caliber, FirearmLoadDefinition loadDefinition,
            LethalityProfile lethalityProfile, double recoilVelocityPerShotMps, List<FireMode> fireModes,
            boolean supportsOneHanded, boolean supportsTwoHanded, Set<WeaponTrait> traits
    ) {
        super(name, narrativeDescription, weightKg, domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintForOrMetricFallback(name, widthMeters, lengthMeters),
                statistics(effectiveRangeMeters, caliber, loadDefinition, lethalityProfile, recoilVelocityPerShotMps,
                        fireModes, supportsOneHanded, supportsTwoHanded));
        if (!Double.isFinite(weightKg) || weightKg < 0) throw new IllegalArgumentException("El peso base debe ser finito y no negativo.");
        if (!Double.isFinite(lengthMeters) || lengthMeters <= 0) throw new IllegalArgumentException("La longitud debe ser positiva y finita.");
        if (!Double.isFinite(widthMeters) || widthMeters <= 0) throw new IllegalArgumentException("La anchura debe ser positiva y finita.");
        if (!Double.isFinite(effectiveRangeMeters) || effectiveRangeMeters <= 0) throw new IllegalArgumentException("El alcance debe ser positivo y finito.");
        if (!Double.isFinite(recoilVelocityPerShotMps) || recoilVelocityPerShotMps < 0) throw new IllegalArgumentException("El retroceso debe ser finito y no negativo.");
        if (!supportsOneHanded && !supportsTwoHanded) throw new IllegalArgumentException("Un arma de fuego debe admitir al menos un tipo de agarre.");
        this.baseWeightKg = weightKg;
        this.lengthMeters = lengthMeters;
        this.widthMeters = widthMeters;
        this.effectiveRangeMeters = effectiveRangeMeters;
        this.caliber = requireText(caliber, "El calibre no puede estar vacío.");
        this.cartridgeDefinition = null;
        this.loadDefinition = Objects.requireNonNull(loadDefinition, "La alimentación no puede ser nula.");
        if (!this.caliber.equals(loadDefinition.descriptor().caliber())) throw new IllegalArgumentException("El calibre de la alimentación no coincide con el del arma.");
        this.lethalityProfile = Objects.requireNonNull(lethalityProfile);
        this.recoilVelocityPerShotMps = recoilVelocityPerShotMps;
        this.fireModes = List.copyOf(Objects.requireNonNull(fireModes));
        if (this.fireModes.isEmpty() || this.fireModes.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("El arma debe disponer de al menos una cadencia válida.");
        this.activeFireMode = this.fireModes.getFirst();
        this.supportsOneHanded = supportsOneHanded;
        this.supportsTwoHanded = supportsTwoHanded;
        this.traits = Set.copyOf(Objects.requireNonNull(traits));
        this.ammunitionRemaining = loadDefinition.capacity();
    }


    /** Propiedades de uso transversales declaradas por el catálogo canónico. */
    void declareItemProperties(List<ItemProperty> properties) {
        Objects.requireNonNull(properties, "Las propiedades no pueden ser nulas.");
        if (properties.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Propiedad nula.");
        if (!itemProperties.isEmpty()) throw new IllegalStateException("Las propiedades ya fueron declaradas.");
        itemProperties = List.copyOf(properties);
    }

    @Override
    public List<ItemProperty> properties() { return itemProperties; }

    /** Declaración de interfaces realizada por el catálogo canónico. No equipa accesorios. */
    void declareFirearmAccessoryMounts(Set<FirearmAccessoryMount> mounts) {
        Objects.requireNonNull(mounts, "Los puntos de montaje no pueden ser nulos.");
        if (mounts.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Punto de montaje nulo.");
        if (!supportedFirearmAccessoryMounts.isEmpty()) throw new IllegalStateException("Los puntos de montaje ya fueron declarados.");
        supportedFirearmAccessoryMounts = Set.copyOf(mounts);
    }

    public double baseWeightKg() { return baseWeightKg; }

    /** Masa del arma más accesorios físicamente montados. La munición interna la resuelven las subclases que la modelan. */
    @Override
    public double weightKg() {
        return baseWeightKg + mountedAttachments.values().stream().mapToDouble(FirearmAccessoryItem::weightKg).sum();
    }

    public double lengthMeters() { return lengthMeters; }
    public double widthMeters() { return widthMeters; }
    public double effectiveRangeMeters() { return effectiveRangeMeters; }
    /** la óptica mejora adquisición/precisión, nunca amplía el alcance efectivo propio del arma. */
    public double effectiveRangeWithAttachmentsMeters() { return effectiveRangeMeters; }
    public double effectiveDirectRangeMeters() { return effectiveRangeMeters; }
    /** El alcance efectivo directo no destruye el proyectil: una trayectoria física o parabólica puede continuar más lejos. */
    public boolean trajectoryMayContinueBeyondEffectiveDirectRange() { return true; }
    public String caliber() { return caliber; }
    public FirearmCartridge cartridgeDefinition() {
        if (cartridgeDefinition == null) throw new IllegalStateException("Esta plataforma usa alimentación unitaria sin cartucho físico.");
        return cartridgeDefinition;
    }
    public FirearmLoadDefinition loadDefinition() { return loadDefinition; }
    public boolean usesPhysicalCartridgeContainer() { return cartridgeDefinition != null; }
    public AmmunitionDescriptor ammunitionRequirement() { return loadDefinition.descriptor(); }
    public int ammunitionCapacity() { return loadDefinition.capacity(); }
    public LethalityProfile lethalityProfile() { return lethalityProfile; }
    public double recoilVelocityPerShotMps() { return recoilVelocityPerShotMps; }
    public List<FireMode> fireModes() { return fireModes; }
    public FireMode activeFireMode() { return activeFireMode; }
    public boolean supportsOneHanded() { return supportsOneHanded; }
    public boolean supportsTwoHanded() { return supportsTwoHanded; }
    public boolean intrinsicOneHandedConditionsMet() {
        return baseWeightKg <= IDEAL_ONE_HANDED_MAX_WEIGHT_KG && lengthMeters <= IDEAL_ONE_HANDED_MAX_LENGTH_M;
    }
    public boolean oneHandedOperationAvailable() {
        return supportsOneHanded && (intrinsicOneHandedConditionsMet() || assistedOneHandedActive());
    }

    /** Masa física permanece inalterada; MEJOR ERGONOMÍA solo modifica el peso efectivo de manejo. */
    public boolean betterErgonomicsActive() {
        return mountedAttachments.values().stream().anyMatch(FirearmAccessoryItem::grantsBetterErgonomics);
    }

    public double effectiveHandlingWeightKg() {
        return betterErgonomicsActive() ? weightKg() * 0.75 : weightKg();
    }

    public WeaponGripEligibility gripEligibilityForStrength(int availableStrength) {
        boolean effectiveOneHanded = oneHandedOperationAvailable();
        if (!effectiveOneHanded && !supportsTwoHanded) return WeaponGripEligibility.CANNOT_WIELD;
        return WeaponGripEligibilityPolicy.resolve(effectiveHandlingWeightKg(), effectiveOneHanded, supportsTwoHanded,
                availableStrength, traits);
    }

    public List<AttributeRequirement> oneHandedRequirements() {
        return supportsOneHanded
                ? WeaponRequirementPolicy.calculate(lengthMeters, effectiveHandlingWeightKg(), GripMode.ONE_HANDED, traits)
                : List.of();
    }

    public List<AttributeRequirement> twoHandedRequirements() {
        return supportsTwoHanded
                ? WeaponRequirementPolicy.calculate(lengthMeters, effectiveHandlingWeightKg(), GripMode.TWO_HANDED, traits)
                : List.of();
    }

    @Override
    public List<String> statistics() {
        List<String> base = new ArrayList<>(super.statistics());
        if (!supportedFirearmAccessoryMounts.isEmpty()) {
            base.add("ADMITE ACCESORIOS | " + supportedFirearmAccessoryMounts.stream().map(Enum::name).sorted().toList());
        }
        return List.copyOf(base);
    }

    public Set<FirearmAccessoryMount> supportedFirearmAccessoryMounts() { return supportedFirearmAccessoryMounts; }
    public boolean admitsAttachment(FirearmAccessoryMount mount) { return supportedFirearmAccessoryMounts.contains(Objects.requireNonNull(mount)); }
    public Map<FirearmAccessoryMount, FirearmAccessoryItem> mountedAttachments() { return Map.copyOf(mountedAttachments); }
    public Optional<FirearmAccessoryItem> mountedAttachment(FirearmAccessoryMount mount) { return Optional.ofNullable(mountedAttachments.get(mount)); }

    public boolean mountAttachment(FirearmAccessoryItem attachment) {
        Objects.requireNonNull(attachment, "El accesorio no puede ser nulo.");
        if (!attachment.detachable()) throw new IllegalArgumentException("El accesorio debe poseer DESMONTABLE.");
        if (!admitsAttachment(attachment.mount())) return false;
        if (mountedAttachments.containsKey(attachment.mount())) return false;
        mountedAttachments.put(attachment.mount(), attachment);
        return true;
    }

    public Optional<FirearmAccessoryItem> unmountAttachment(FirearmAccessoryMount mount) {
        Objects.requireNonNull(mount, "El punto de montaje no puede ser nulo.");
        FirearmAccessoryItem removed = mountedAttachments.remove(mount);
        if (removed != null && removed.deployed()) removed.fold();
        return Optional.ofNullable(removed);
    }

    public boolean assistedOneHandedActive() {
        return mountedAttachments.values().stream().anyMatch(FirearmAccessoryItem::grantsAssistedOneHanded);
    }

    public boolean coupDeGracePropertyPresent() { return false; }
    public boolean fulminatingPropertyPresent() { return false; }

    public boolean isCoupDeGraceHeadImpact(double headCoveragePercent, double headPiercingProtection) {
        return coupDeGracePropertyPresent() && CoupDeGracePolicy.isCoupDeGrace(true, headCoveragePercent, headPiercingProtection, lethalityProfile.piercing());
    }

    public boolean assistedStabilizerActive() {
        return mountedAttachments.values().stream()
                .anyMatch(a -> a.grantsAssistedStabilizer() && a.deployed());
    }

    public double effectiveRecoilVelocityPerShotMps() {
        return assistedStabilizerActive() ? 0.0 : recoilVelocityPerShotMps;
    }

    public int ammunitionRemaining() { return ammunitionRemaining; }
    public FirearmHandlingState handlingState() { return handlingState; }
    public FirearmRecoilState recoilState() { return recoilState; }
    public FirearmTriggerState triggerState() { return triggerState; }
    public boolean wearsOut() { return false; }

    /** temporalidad física canónica; no incluye carga energética/thermal lock. */
    public FirearmTimingProfile timingProfile() { return FirearmTimingPolicy.profile(this); }
    public double reloadDurationSeconds() { return timingProfile().reloadDurationSeconds(); }
    public double shotIntervalSeconds() { return timingProfile().shotIntervalSeconds(); }

    /** Las armas de rociado/arco y otros sistemas sin puntería convencional pueden anularlo. */
    public boolean supportsAiming() { return true; }

    public void toggleAim() {
        if (!supportsAiming()) {
            throw new IllegalStateException("Esta plataforma no utiliza AIMING.");
        }
        if (handlingState == FirearmHandlingState.PNEUMATIC_PRESSURIZATION
                || handlingState == FirearmHandlingState.ELECTROMAGNETIC_CHARGE_SELECTION
                || handlingState == FirearmHandlingState.ARC_MANUAL_CHARGE
                || handlingState == FirearmHandlingState.CLUSTER_TIMER_CONFIGURATION) {
            throw new IllegalStateException("No se puede apuntar mientras se manipula el sistema energético del arma.");
        }
        handlingState = handlingState == FirearmHandlingState.AIMING
                ? FirearmHandlingState.NORMAL
                : FirearmHandlingState.AIMING;
    }

    protected void enterPneumaticPressurizationState() {
        handlingState = FirearmHandlingState.PNEUMATIC_PRESSURIZATION;
        triggerState.release();
    }

    protected void leavePneumaticPressurizationState() { handlingState = FirearmHandlingState.NORMAL; }

    protected void enterElectromagneticManualChargeState() {
        handlingState = FirearmHandlingState.ELECTROMAGNETIC_CHARGE_SELECTION;
        triggerState.release();
    }

    protected void leaveElectromagneticManualChargeState() { handlingState = FirearmHandlingState.NORMAL; }

    protected void enterArcManualChargeState() {
        handlingState = FirearmHandlingState.ARC_MANUAL_CHARGE;
        triggerState.release();
    }

    protected void leaveArcManualChargeState() { handlingState = FirearmHandlingState.NORMAL; }

    protected void enterClusterTimerConfigurationState() {
        handlingState = FirearmHandlingState.CLUSTER_TIMER_CONFIGURATION;
        triggerState.release();
    }
    protected void leaveClusterTimerConfigurationState() { handlingState = FirearmHandlingState.NORMAL; }

    public void reloadFullCartridge() {
        ammunitionRemaining = loadDefinition.capacity();
        triggerState.release();
    }

    public void restoreAmmunitionRemaining(int units) { setAmmunitionRemaining(units); }

    protected void setAmmunitionRemaining(int units) {
        if (units < 0 || units > loadDefinition.capacity()) throw new IllegalArgumentException("Cantidad de munición inválida.");
        ammunitionRemaining = units;
    }

    public AmmunitionLoadResult reloadFromInventory(InventoryState inventory) {
        AmmunitionLoadResult result = new AmmunitionInventoryPolicy().consumeForEquippedWeapon(
                this, ammunitionRequirement(), inventory);
        if (!result.loaded()) return result;
        ammunitionRemaining = Math.min(loadDefinition.capacity(), result.shotsLoaded());
        triggerState.release();
        return result;
    }

    public FireMode cycleFireMode() {
        int index = fireModes.indexOf(activeFireMode);
        activeFireMode = fireModes.get((index + 1) % fireModes.size());
        triggerState.release();
        return activeFireMode;
    }

    protected boolean canConsumeShot() { return ammunitionRemaining > 0; }

    protected void consumeShot() { consumeAmmunitionAndRegisterShot(recoilVelocityPerShotMps); }

    protected final void consumeAmmunitionAndRegisterShot(double recoilVelocityMps) {
        if (!canConsumeShot()) throw new IllegalStateException("El arma no puede efectuar el disparo solicitado.");
        ammunitionRemaining--;
        recoilState.registerShot(assistedStabilizerActive() ? 0.0 : recoilVelocityMps);
        triggerState.registerShot();
    }

    public String destabilizingTechniqueDescription() {
        return "Golpe desestabilizador con la culata del arma de fuego.";
    }

    private static List<String> statistics(
            double range, String caliber, FirearmLoadDefinition loadDefinition, LethalityProfile lethality,
            double recoil, List<FireMode> modes, boolean oneHanded, boolean twoHanded
    ) {
        Objects.requireNonNull(loadDefinition);
        Objects.requireNonNull(lethality);
        Objects.requireNonNull(modes);
        List<String> stats = new ArrayList<>();
        stats.add("Alcance efectivo directo | " + range + " m");
        stats.add("Calibre | " + caliber);
        stats.add("Alimentación | " + loadDefinition.name() + " · " + loadDefinition.capacity() + " disparos");
        stats.add("Letalidad | " + lethality.piercing() + " perforante / " + lethality.slashing() + " cortante / " + lethality.blunt() + " contundente");
        stats.add("Retroceso | " + recoil + " m/s por disparo");
        stats.add("Cadencia | " + modes.stream().map(FireMode::code).toList());
        stats.add("Agarre | " + (oneHanded && twoHanded ? "Una o dos manos" : twoHanded ? "Dos manos" : "Una mano"));
        stats.add("Desgaste | No");
        return List.copyOf(stats);
    }

    private static List<String> statistics(
            double range,
            String caliber,
            FirearmCartridge cartridge,
            LethalityProfile lethality,
            double recoil,
            List<FireMode> modes,
            boolean oneHanded,
            boolean twoHanded
    ) {
        Objects.requireNonNull(cartridge, "El cartucho no puede ser nulo.");
        Objects.requireNonNull(lethality, "La letalidad no puede ser nula.");
        Objects.requireNonNull(modes, "Las cadencias no pueden ser nulas.");
        List<String> stats = new ArrayList<>();
        stats.add("Alcance efectivo directo | " + range + " m");
        stats.add("Calibre | " + caliber);
        stats.add("Cartucho | " + cartridge.capacity() + " disparos");
        stats.add("Letalidad | " + lethality.piercing() + " perforante / "
                + lethality.slashing() + " cortante / " + lethality.blunt() + " contundente");
        stats.add("Retroceso | " + recoil + " m/s por disparo");
        stats.add("Cadencia | " + modes.stream().map(FireMode::code).toList());
        stats.add("Agarre | " + (oneHanded && twoHanded ? "Una o dos manos" : twoHanded ? "Dos manos" : "Una mano"));
        stats.add("Desgaste | No");
        return List.copyOf(stats);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        String normalized = value.trim();
        if (normalized.isEmpty()) throw new IllegalArgumentException(message);
        return normalized;
    }
}
