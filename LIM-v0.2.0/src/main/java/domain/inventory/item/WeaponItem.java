package domain.inventory.item;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.combat.coating.WeaponCoating;
import domain.combat.moveset.MeleeMovesetProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Optional;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.EnumMap;

public final class WeaponItem extends InventoryEntry {
    private final double reachMeters;
    private final List<WeaponMode> modes;
    private final List<AttributeRequirement> requirements;
    private final List<HiddenItemPropertyDefinition> clairvoyanceUnlocks;
    private final OptionalDouble sheathedWeightKg;
    private java.util.Optional<InventoryFootprint> sheathedFootprint;
    private final int sheathingClairvoyanceRequirement;
    private boolean sheathed;
    private final WeaponConfigurationPolicy configurationPolicy;
    private WeaponConfiguration currentConfiguration;
    private final Map<String, LethalityProfile> currentLethalityByMode;
    private boolean leftHandLimitException;
    private final Set<WeaponTrait> traits;
    private WeaponCombatPolicy combatPolicy;
    private final Map<WeaponActionMode, Set<WeaponCombatAction>> combatActionsByMode;
    private final Map<WeaponActionMode, LightAttackComboProfile> lightAttackCombosByMode;
    private WeaponCoating coating;
    private final Map<WeaponActionMode, MeleeMovesetProfile> offensiveMovesetsByMode;
    private domain.combat.moveset.CrossModeTransitionProfile crossModeTransitionProfile;
    private final List<ItemProperty> additionalProperties = new ArrayList<>();
    private domain.combat.ShieldGuardPosition shieldGuardPosition = domain.combat.ShieldGuardPosition.HEAD;

    public WeaponItem(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            double reachMeters,
            List<WeaponMode> modes,
            List<AttributeRequirement> requirements,
            List<HiddenItemPropertyDefinition> clairvoyanceUnlocks,

            List<String> statistics
    ) {
        this(name, narrativeDescription, weightKg, footprint, reachMeters, modes, requirements,
                clairvoyanceUnlocks, statistics, OptionalDouble.empty(), 0, false,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), Set.of());
    }

    public WeaponItem(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            double reachMeters,
            List<WeaponMode> modes,
            List<AttributeRequirement> requirements,
            List<HiddenItemPropertyDefinition> clairvoyanceUnlocks,

            List<String> statistics,
            OptionalDouble sheathedWeightKg,
            int sheathingClairvoyanceRequirement,
            boolean sheathed
    ) {
        this(name, narrativeDescription, weightKg, footprint, reachMeters, modes, requirements,
                clairvoyanceUnlocks, statistics, sheathedWeightKg,
                sheathingClairvoyanceRequirement, sheathed,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), Set.of());
    }

    public WeaponItem(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            double reachMeters,
            List<WeaponMode> modes,
            List<AttributeRequirement> requirements,
            List<HiddenItemPropertyDefinition> clairvoyanceUnlocks,

            List<String> statistics,
            OptionalDouble sheathedWeightKg,
            int sheathingClairvoyanceRequirement,
            boolean sheathed,
            WeaponConfigurationPolicy configurationPolicy
    ) {
        this(name, narrativeDescription, weightKg, footprint, reachMeters, modes, requirements,
                clairvoyanceUnlocks, statistics, sheathedWeightKg,
                sheathingClairvoyanceRequirement, sheathed, configurationPolicy, Set.of());
    }

    public WeaponItem(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            double reachMeters,
            List<WeaponMode> modes,
            List<AttributeRequirement> requirements,
            List<HiddenItemPropertyDefinition> clairvoyanceUnlocks,

            List<String> statistics,
            OptionalDouble sheathedWeightKg,
            int sheathingClairvoyanceRequirement,
            boolean sheathed,
            WeaponConfigurationPolicy configurationPolicy,
            Set<WeaponTrait> traits
    ) {
        super(name, narrativeDescription, weightKg, footprint, statistics,
                unlockProperties(clairvoyanceUnlocks));
        if (reachMeters < 0) {
            throw new IllegalArgumentException("El alcance no puede ser negativo.");
        }
        this.reachMeters = reachMeters;
        this.modes = List.copyOf(Objects.requireNonNull(modes, "Los modos no pueden ser nulos."));
        this.requirements = List.copyOf(Objects.requireNonNull(requirements, "Los requisitos no pueden ser nulos."));
        this.clairvoyanceUnlocks = List.copyOf(Objects.requireNonNull(
                clairvoyanceUnlocks, "Los desbloqueos no pueden ser nulos."
        ));
        this.sheathedWeightKg = Objects.requireNonNull(sheathedWeightKg, "El peso envainado no puede ser nulo.");
        this.sheathedFootprint = java.util.Optional.empty();
        if (sheathedWeightKg.isPresent() && (sheathedWeightKg.getAsDouble() < 0 || sheathedWeightKg.getAsDouble() > weightKg)) {
            throw new IllegalArgumentException("El peso envainado debe estar entre cero y el peso máximo del arma.");
        }
        if (sheathedWeightKg.isPresent() && (sheathingClairvoyanceRequirement < 1 || sheathingClairvoyanceRequirement > 120)) {
            throw new IllegalArgumentException("El requisito de envainado debe estar entre 1 y 120.");
        }
        if (sheathedWeightKg.isEmpty() && sheathingClairvoyanceRequirement != 0) {
            throw new IllegalArgumentException("Un arma sin peso envainado debe usar requisito cero.");
        }
        this.sheathingClairvoyanceRequirement = sheathingClairvoyanceRequirement;
        this.sheathed = sheathed && sheathedWeightKg.isPresent();
        this.configurationPolicy = Objects.requireNonNull(configurationPolicy, "La política de configuración no puede ser nula.");
        this.traits = Set.copyOf(Objects.requireNonNull(traits, "Las propiedades del arma no pueden ser nulas."));
        this.leftHandLimitException = false;
        this.combatPolicy = WeaponCombatPolicy.unrestricted();
        this.combatActionsByMode = new EnumMap<>(WeaponActionMode.class);
        this.combatActionsByMode.put(WeaponActionMode.PRIMARY, this.combatPolicy.allowedActions());
        this.combatActionsByMode.put(WeaponActionMode.ALTERNATIVE, this.combatPolicy.allowedActions());
        this.lightAttackCombosByMode = new EnumMap<>(WeaponActionMode.class);
        this.lightAttackCombosByMode.put(WeaponActionMode.PRIMARY, LightAttackComboProfile.standard());
        this.lightAttackCombosByMode.put(WeaponActionMode.ALTERNATIVE, LightAttackComboProfile.standard());
        this.offensiveMovesetsByMode = new EnumMap<>(WeaponActionMode.class);
        this.crossModeTransitionProfile = new domain.combat.moveset.CrossModeTransitionProfile(List.of());
        this.currentConfiguration = configurationPolicy.configurations().getFirst();
        this.currentLethalityByMode = new LinkedHashMap<>();
        for (WeaponMode mode : this.modes) {
            this.currentLethalityByMode.put(mode.name(), mode.lethality());
        }
        if (this.modes.isEmpty()) {
            throw new IllegalArgumentException("Un arma debe tener al menos un modo de ataque.");
        }
    }

    public WeaponItem(
            String name, String narrativeDescription, double weightKg, InventoryFootprint footprint,
            double reachMeters, List<WeaponMode> modes, List<AttributeRequirement> requirements,
            List<HiddenItemPropertyDefinition> clairvoyanceUnlocks,
            List<String> statistics, OptionalDouble sheathedWeightKg,
            java.util.Optional<InventoryFootprint> sheathedFootprint,
            int sheathingClairvoyanceRequirement, boolean sheathed,
            WeaponConfigurationPolicy configurationPolicy, Set<WeaponTrait> traits
    ) {
        this(name, narrativeDescription, weightKg, footprint, reachMeters, modes, requirements,
                clairvoyanceUnlocks, statistics, sheathedWeightKg,
                sheathingClairvoyanceRequirement, sheathed, configurationPolicy, traits);
        java.util.Optional<InventoryFootprint> normalized = Objects.requireNonNull(
                sheathedFootprint, "El tamaño envainado no puede ser nulo.");
        if (normalized.isPresent() && sheathedWeightKg.isEmpty()) {
            throw new IllegalArgumentException("Un arma sin peso envainado no puede tener tamaño envainado.");
        }
        this.sheathedFootprint = normalized;
    }

    private static List<ItemProperty> unlockProperties(List<HiddenItemPropertyDefinition> unlocks) {
        Objects.requireNonNull(unlocks, "Los desbloqueos no pueden ser nulos.");
        List<ItemProperty> result = new ArrayList<>();
        for (HiddenItemPropertyDefinition unlock : unlocks) {
            result.add(Objects.requireNonNull(unlock, "Un desbloqueo no puede ser nulo.").asHiddenProperty());
        }
        return result;
    }

    public double reachMeters() { return reachMeters; }
    public List<WeaponMode> modes() { return modes; }
    public List<AttributeRequirement> requirements() { return requirements; }
    public List<HiddenItemPropertyDefinition> clairvoyanceUnlocks() { return clairvoyanceUnlocks; }
    public boolean supportsSheathing() { return sheathedWeightKg.isPresent(); }
    public boolean isSheathed() { return sheathed; }
    /** una entrada ofensiva no inicia ataques mientras el arma permanezca envainada. */
    public boolean canInitiateCommittedAttack() { return !sheathed; }
    public void restoreSheathed(boolean value){if(value&&!supportsSheathing())throw new IllegalArgumentException("El arma no admite envainado.");sheathed=value;}
    public int sheathingClairvoyanceRequirement() { return sheathingClairvoyanceRequirement; }
    public boolean leftHandLimitException() { return leftHandLimitException; }
    public Set<WeaponTrait> traits() { return traits; }
    public WeaponCombatPolicy combatPolicy() { return combatPolicy; }
    public boolean hasTrait(WeaponTrait trait) { return traits.contains(Objects.requireNonNull(trait)); }

    public boolean isExclusivelyTwoHanded() {
        return configurationPolicy.configurations().stream()
                .allMatch(configuration -> configuration.gripMode() == GripMode.TWO_HANDED);
    }

    public boolean supportsOneHandedUse() {
        return configurationPolicy.configurations().stream()
                .anyMatch(configuration -> configuration.gripMode() == GripMode.ONE_HANDED);
    }

    public boolean supportsTwoHandedUse() {
        return configurationPolicy.configurations().stream()
                .anyMatch(configuration -> configuration.gripMode() == GripMode.TWO_HANDED);
    }

    public WeaponGripEligibility gripEligibilityForStrength(int availableStrength) {
        return WeaponGripEligibilityPolicy.resolve(
                weightKg(), supportsOneHandedUse(), supportsTwoHandedUse(), availableStrength, traits);
    }

    public WeaponItem withProperties(List<ItemProperty> properties) {
        Objects.requireNonNull(properties, "Las propiedades adicionales no pueden ser nulas.");
        if (properties.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Las propiedades adicionales no pueden contener nulos.");
        }
        this.additionalProperties.addAll(properties);
        return this;
    }

    @Override
    public List<ItemProperty> properties() {
        if (additionalProperties.isEmpty()) return super.properties();
        List<ItemProperty> all = new ArrayList<>(super.properties());
        all.addAll(additionalProperties);
        return List.copyOf(all);
    }

    public WeaponItem withCombatPolicy(WeaponCombatPolicy combatPolicy) {
        this.combatPolicy = Objects.requireNonNull(combatPolicy, "La política de combate no puede ser nula.");
        if (hasTrait(WeaponTrait.SHIELD)) {
            this.combatActionsByMode.put(WeaponActionMode.PRIMARY, Set.of(WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.DESTABILIZE));
            this.combatActionsByMode.put(WeaponActionMode.ALTERNATIVE, Set.of(WeaponCombatAction.BLOCK));
        } else {
            this.combatActionsByMode.put(WeaponActionMode.PRIMARY, combatPolicy.allowedActions());
            this.combatActionsByMode.put(WeaponActionMode.ALTERNATIVE, combatPolicy.allowedActions());
        }
        return this;
    }

    public boolean allowsCombatAction(WeaponCombatAction action) {
        return combatPolicy.allows(action);
    }

    public WeaponItem withCombatActionsFor(WeaponActionMode mode, Set<WeaponCombatAction> actions) {
        Objects.requireNonNull(mode, "El modo de acción no puede ser nulo.");
        Objects.requireNonNull(actions, "Las acciones no pueden ser nulas.");
        if (actions.isEmpty()) throw new IllegalArgumentException("Un modo debe permitir al menos una acción.");
        combatActionsByMode.put(mode, Set.copyOf(actions));
        return this;
    }

    public Set<WeaponCombatAction> combatActionsFor(WeaponActionMode mode) {
        return combatActionsByMode.getOrDefault(Objects.requireNonNull(mode), combatPolicy.allowedActions());
    }

    public WeaponItem withLightAttackComboFor(WeaponActionMode mode, int attackCount) {
        lightAttackCombosByMode.put(Objects.requireNonNull(mode), new LightAttackComboProfile(attackCount));
        return this;
    }

    public LightAttackComboProfile lightAttackComboFor(WeaponActionMode mode) {
        return lightAttackCombosByMode.getOrDefault(Objects.requireNonNull(mode), LightAttackComboProfile.standard());
    }

    /** semántica ofensiva interna; no se proyecta a la narrativa del objeto. */
    public WeaponItem withOffensiveMoveset(MeleeMovesetProfile moveset) {
        return withOffensiveMovesetFor(WeaponActionMode.PRIMARY, moveset);
    }

    /** cada modo de acción puede poseer una gramática ofensiva distinta. */
    public WeaponItem withOffensiveMovesetFor(WeaponActionMode mode, MeleeMovesetProfile moveset) {
        Objects.requireNonNull(mode, "El modo de acción no puede ser nulo.");
        Objects.requireNonNull(moveset, "El moveset no puede ser nulo.");
        offensiveMovesetsByMode.put(mode, moveset);
        withLightAttackComboFor(mode, moveset.lightAttackCount());
        return this;
    }


    public WeaponItem withCrossModeTransitionProfile(domain.combat.moveset.CrossModeTransitionProfile profile) {
        this.crossModeTransitionProfile = Objects.requireNonNull(profile); return this;
    }
    public domain.combat.moveset.CrossModeTransitionProfile crossModeTransitionProfile() { return crossModeTransitionProfile; }

    /** Compatibilidad: el moveset ofensivo sin modo significa PRIMARY. */
    public Optional<MeleeMovesetProfile> offensiveMoveset() {
        return offensiveMovesetFor(WeaponActionMode.PRIMARY);
    }

    public Optional<MeleeMovesetProfile> offensiveMovesetFor(WeaponActionMode mode) {
        return Optional.ofNullable(offensiveMovesetsByMode.get(Objects.requireNonNull(mode)));
    }

    /** La inmunidad al desvío pertenece a la geometría DE ROTOR, no al agarre bimanual en general. */
    public boolean canCurrentAttackBeParried() {
        return !hasTrait(WeaponTrait.DE_ROTOR);
    }

    public WeaponItem allowLeftHandLimitException() {
        this.leftHandLimitException = true;
        return this;
    }

    public boolean canToggleSheathing(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        return supportsSheathing()
                && !hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)
                && sheet.valueOf(Attribute.CLARIVIDENCIA) >= sheathingClairvoyanceRequirement;
    }

    public boolean toggleSheathing(CharacterSheet sheet) {
        if (!canToggleSheathing(sheet)) {
            return false;
        }
        sheathed = !sheathed;
        return true;
    }

    /** Transición interna de manejo: guarda el objeto sin reinterpretar requisitos de conocimiento. */
    public void stowForHandlingTransition() {
        if (hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
            throw new IllegalStateException("El Espadón de Rotor no puede envainarse: sólo puede retraerse en el Sistema de Transporte Dorsal del Rotor V881.");
        }
        if (!supportsSheathing()) {
            throw new IllegalStateException("El objeto no admite envainado o colocación en tahalí.");
        }
        sheathed = true;
    }

    /** Transición interna de manejo: vuelve a activar un objeto previamente guardado. */
    public void drawForHandlingTransition() {
        if (hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
            throw new IllegalStateException("El Espadón de Rotor sólo puede desplegarse desde su retracción dorsal mediante la política DE ROTOR.");
        }
        if (!supportsSheathing()) {
            throw new IllegalStateException("El objeto no admite desenvainado.");
        }
        sheathed = false;
    }

    /** transición exclusiva del sistema dorsal; no equivale a envainar el Espadón. */
    public void retractIntoDorsalForHandlingTransition() {
        if (!hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
            throw new IllegalStateException("Sólo el arma compatible con el sistema dorsal puede retraerse dentro del sistema dorsal.");
        }
        if (!supportsSheathing()) {
            throw new IllegalStateException("El Espadón de Rotor no dispone de geometría retraíble.");
        }
        sheathed = true;
    }

    /** despliegue exclusivo desde BACK_HAND; no equivale a desenvainar un arma convencional. */
    public void deployFromDorsalForHandlingTransition() {
        if (!hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
            throw new IllegalStateException("Sólo el arma compatible con el sistema dorsal puede desplegarse desde el sistema dorsal.");
        }
        if (!supportsSheathing()) {
            throw new IllegalStateException("El Espadón de Rotor no dispone de geometría retraíble.");
        }
        sheathed = false;
    }

    public WeaponConfiguration currentConfiguration() { return currentConfiguration; }
    WeaponConfigurationPolicy configurationPolicyView() { return configurationPolicy; }
    public List<WeaponConfiguration> availableConfigurations() { return configurationPolicy.configurations(); }

    public boolean supportsConfiguration(WeaponConfiguration configuration) {
        return configurationPolicy.supports(Objects.requireNonNull(configuration, "La configuración no puede ser nula."));
    }

    public boolean supportsActionMode(WeaponActionMode actionMode) {
        Objects.requireNonNull(actionMode, "El modo de acción no puede ser nulo.");
        return configurationPolicy.configurations().stream()
                .anyMatch(configuration -> configuration.actionMode() == actionMode);
    }

    public WeaponConfiguration selectConfiguration(WeaponConfiguration configuration) {
        Objects.requireNonNull(configuration, "La configuración no puede ser nula.");
        if (!configurationPolicy.supports(configuration)) {
            throw new IllegalArgumentException("El arma no admite la configuración solicitada: " + configuration + ".");
        }
        currentConfiguration = configuration;
        return currentConfiguration;
    }

    public WeaponConfiguration selectActionMode(WeaponActionMode actionMode) {
        Objects.requireNonNull(actionMode, "El modo de acción no puede ser nulo.");
        WeaponConfiguration selected = configurationPolicy.preferredFor(actionMode, currentConfiguration.gripMode())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El arma no admite el modo " + actionMode + "."));
        currentConfiguration = selected;
        return currentConfiguration;
    }

    public WeaponConfiguration cycleConfiguration() {
        currentConfiguration = configurationPolicy.nextAfter(currentConfiguration);
        return currentConfiguration;
    }

    public domain.combat.ShieldGuardPosition shieldGuardPosition() { return shieldGuardPosition; }
    public domain.combat.ShieldGuardPosition toggleShieldGuardPosition() {
        if (!hasTrait(WeaponTrait.SHIELD)) throw new IllegalStateException("Solo un escudo dedicado tiene postura de guardia.");
        shieldGuardPosition = shieldGuardPosition.toggle();
        return shieldGuardPosition;
    }

    public LethalityProfile currentLethality(WeaponMode mode) {
        Objects.requireNonNull(mode, "El modo de arma no puede ser nulo.");
        LethalityProfile current = currentLethalityByMode.get(mode.name());
        if (current == null) throw new IllegalArgumentException("El modo no pertenece a esta arma.");
        return current;
    }

    public int currentPiercingLethality(WeaponMode mode) { return (int) Math.round(currentLethality(mode).piercing()); }
    public int currentSlashingLethality(WeaponMode mode) { return (int) Math.round(currentLethality(mode).slashing()); }
    public int currentBluntLethality(WeaponMode mode) { return (int) Math.round(currentLethality(mode).blunt()); }

    /**
     * : un arma cuerpo a cuerpo sólo se desgasta contra HEAVY. Cada canal pierde
     * un punto si la capa HEAVY conserva protección positiva en ese mismo canal.
     */
    public domain.combat.WeaponProfileWearResult applyHeavyArmorWear(
            WeaponMode mode, domain.inventory.item.armor.ArmorProtectionProfile heavyProtection) {
        Objects.requireNonNull(mode, "El modo de arma no puede ser nulo.");
        Objects.requireNonNull(heavyProtection, "La protección HEAVY no puede ser nula.");
        if (hasTrait(WeaponTrait.NON_DEGRADING)) return new domain.combat.WeaponProfileWearResult(0, 0, 0);
        LethalityProfile current = currentLethality(mode);
        double pLoss = heavyProtection.piercing() > 0 && current.piercing() > 0 ? Math.min(1.0, current.piercing()) : 0.0;
        double cLoss = heavyProtection.slashing() > 0 && current.slashing() > 0 ? Math.min(1.0, current.slashing()) : 0.0;
        double bLoss = heavyProtection.blunt() > 0 && current.blunt() > 0 ? Math.min(1.0, current.blunt()) : 0.0;
        currentLethalityByMode.put(mode.name(), new LethalityProfile(
                current.piercing() - pLoss,
                current.slashing() - cLoss,
                current.blunt() - bLoss));
        if (allModesFullyWorn()) clearCoating();
        return new domain.combat.WeaponProfileWearResult(pLoss, cLoss, bLoss);
    }

    public Optional<WeaponCoating> coating() { return Optional.ofNullable(coating); }

    public void applyCoating(WeaponCoating coating) {
        this.coating = Objects.requireNonNull(coating, "El recubrimiento no puede ser nulo.");
    }

    public boolean clearCoating() {
        boolean present = coating != null;
        coating = null;
        return present;
    }

    private boolean allModesFullyWorn() {
        // Los recubrimientos históricos de Transposición siguen anclados al canal contundente.
        return currentLethalityByMode.values().stream().allMatch(value -> value.blunt() == 0);
    }

    public boolean canBeCoatedWithMucusTear() {
        return modes.stream().anyMatch(mode -> mode.lethality().slashing() > 0);
    }

    public boolean canBeSharpened() {
        return canBeCoatedWithMucusTear();
    }

    public boolean restoreAllLethality() {
        if (!canBeSharpened()) return false;
        for (WeaponMode mode : modes) currentLethalityByMode.put(mode.name(), mode.lethality());
        clearCoating();
        return true;
    }

    /** Compatibilidad con piedra/resina hasta . */
    public boolean restoreAllBluntLethality() { return restoreAllLethality(); }

    @Override
    public InventoryFootprint footprint() {
        return sheathed && sheathedFootprint.isPresent() ? sheathedFootprint.get() : super.footprint();
    }

    public InventoryFootprint deployedFootprint() {
        return super.footprint();
    }

    public java.util.Optional<InventoryFootprint> sheathedFootprint() {
        return sheathedFootprint;
    }

    public double effectiveWeightKg() {
        return sheathed && sheathedWeightKg.isPresent() ? sheathedWeightKg.getAsDouble() : weightKg();
    }
}
