package domain.environment;

import domain.ability.HomeostasisThermalPolicy;
import domain.animation.CharacterAnimationState;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.StaminaRecovery;
import domain.inventory.equipment.EquipmentState;
import domain.runic.EffectImmunity;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Estado temporal independiente de todas las adversidades ambientales.
 * Las cuatro pueden coexistir; cada una conserva build-up, activación y recovery propios.
 */
public final class EnvironmentalExposure {
    public static final double ACTIVE_DAMAGE_PER_SECOND = 1.0;

    private static final class State {
        double exposureSeconds;
        boolean active;
        boolean insideSource;
        double recoverySecondsRemaining;
    }

    private final EnumMap<EnvironmentalAdversity, State> states = new EnumMap<>(EnvironmentalAdversity.class);
    private boolean custodyActive;
    private boolean thermalAdaptationActive;

    public EnvironmentalExposure() {
        for (EnvironmentalAdversity adversity : EnvironmentalAdversity.values()) {
            if (adversity != EnvironmentalAdversity.NORMAL) states.put(adversity, new State());
        }
    }

    public void setThermalAdaptationActive(boolean active) { this.thermalAdaptationActive = active; }

    /** Al activar CUSTODIA, cualquier build-up o adversidad activa entra inmediatamente en recovery. */
    public void setCustodyActive(boolean active, StaminaRecovery staminaRecovery) {
        Objects.requireNonNull(staminaRecovery, "La recuperación de PA no puede ser nula.");
        boolean justActivated = active && !custodyActive;
        custodyActive = active;
        if (justActivated) states.values().forEach(state -> beginRecovery(state, staminaRecovery));
    }

    public EnvironmentalTickResult enter(EnvironmentalAdversity adversity, double elapsedSeconds,
                                           CharacterSheet sheet, EquipmentState equipment,
                                           StaminaRecovery staminaRecovery) {
        return enter(adversity, elapsedSeconds, sheet, equipment, staminaRecovery, null);
    }

    /** La variante con animación ejecuta automáticamente ENMASCARAR al entrar con un cubrecuellos operativo. */
    public EnvironmentalTickResult enter(EnvironmentalAdversity adversity, double elapsedSeconds,
                                           CharacterSheet sheet, EquipmentState equipment,
                                           StaminaRecovery staminaRecovery,
                                           CharacterAnimationState animationState) {
        validate(elapsedSeconds, sheet, equipment, staminaRecovery);
        Objects.requireNonNull(adversity, "La adversidad no puede ser nula.");
        if (adversity == EnvironmentalAdversity.NORMAL) {
            throw new IllegalArgumentException("NORMAL no representa una fuente ambiental concreta.");
        }
        State state = states.get(adversity);
        state.insideSource = true;

        if (isImmune(adversity, sheet, equipment) || custodyActive) {
            beginRecovery(state, staminaRecovery);
            return snapshot(adversity, 0, sheet);
        }

        state.recoverySecondsRemaining = 0;
        double required = requiredExposureSeconds(adversity, sheet);
        double previousExposure = state.exposureSeconds;
        double incoming = elapsedSeconds;
        if (adversity == EnvironmentalAdversity.BITING_FROST) {
            double resolved = HomeostasisThermalPolicy.frostBuildUpAfterThermalAdaptationTick(
                    previousExposure, incoming, thermalAdaptationActive);
            state.exposureSeconds = Math.min(required, resolved);
        } else {
            state.exposureSeconds = Math.min(required, previousExposure + incoming);
        }
        double secondsAfterActivation = Math.max(0.0, previousExposure + incoming - required);
        if (state.exposureSeconds >= required) state.active = true;
        double damage = state.active && adversity.drainsHealth()
                ? secondsAfterActivation * ACTIVE_DAMAGE_PER_SECOND : 0;
        return snapshot(adversity, damage, sheet);
    }

    public EnvironmentalTickResult leave(EnvironmentalAdversity adversity, double elapsedSeconds,
                                           CharacterSheet sheet, EquipmentState equipment,
                                           StaminaRecovery staminaRecovery) {
        validate(elapsedSeconds, sheet, equipment, staminaRecovery);
        Objects.requireNonNull(adversity, "La adversidad no puede ser nula.");
        if (adversity == EnvironmentalAdversity.NORMAL) return normalSnapshot(sheet);
        State state = states.get(adversity);
        state.insideSource = false;
        return recover(adversity, state, elapsedSeconds, sheet, equipment, staminaRecovery);
    }

    /** Forma compacta: abandona la única adversidad presente; si hay varias, exige especificarla. */
    public EnvironmentalTickResult leave(double elapsedSeconds, CharacterSheet sheet, EquipmentState equipment,
                                           StaminaRecovery staminaRecovery) {
        EnvironmentalAdversity only = states.entrySet().stream()
                .filter(e -> present(e.getValue())).map(Map.Entry::getKey).findFirst().orElse(EnvironmentalAdversity.NORMAL);
        return only == EnvironmentalAdversity.NORMAL ? normalSnapshot(sheet)
                : leave(only, elapsedSeconds, sheet, equipment, staminaRecovery);
    }

    public EnvironmentalTickResult recover(EnvironmentalAdversity adversity, double elapsedSeconds,
                                             CharacterSheet sheet, EquipmentState equipment,
                                             StaminaRecovery staminaRecovery) {
        validate(elapsedSeconds, sheet, equipment, staminaRecovery);
        return recover(adversity, states.get(adversity), elapsedSeconds, sheet, equipment, staminaRecovery);
    }

    private EnvironmentalTickResult recover(EnvironmentalAdversity adversity, State state, double elapsedSeconds,
                                              CharacterSheet sheet, EquipmentState equipment,
                                              StaminaRecovery staminaRecovery) {
        if (!present(state)) return snapshot(adversity, 0, sheet);
        if (state.recoverySecondsRemaining <= 0) state.recoverySecondsRemaining = staminaRecovery.fullRecoverySeconds();
        if (staminaRecovery.immobilized()) {
            return snapshot(adversity, state.active && adversity.drainsHealth()
                    ? elapsedSeconds * ACTIVE_DAMAGE_PER_SECOND : 0, sheet);
        }
        double window = Math.max(staminaRecovery.fullRecoverySeconds(), 1.0e-9);
        state.exposureSeconds = Math.max(0, state.exposureSeconds * (1.0 - Math.min(1.0, elapsedSeconds / window)));
        double previous = state.recoverySecondsRemaining;
        state.recoverySecondsRemaining = Math.max(0, previous - elapsedSeconds);
        double damage = state.active && adversity.drainsHealth()
                ? Math.min(elapsedSeconds, previous) * ACTIVE_DAMAGE_PER_SECOND : 0;
        if (state.recoverySecondsRemaining <= 1.0e-9 || state.exposureSeconds <= 1.0e-9) {
            clear(state);
            return new EnvironmentalTickResult(EnvironmentalAdversity.NORMAL, 0, 0, false, false, 0, damage, naturalConductor());
        }
        return snapshot(adversity, damage, sheet);
    }

    public EnvironmentalTickResult snapshot(EnvironmentalAdversity adversity, CharacterSheet sheet) {
        return adversity == EnvironmentalAdversity.NORMAL ? normalSnapshot(sheet) : snapshot(adversity, 0, sheet);
    }

    public Map<EnvironmentalAdversity, EnvironmentalTickResult> snapshots(CharacterSheet sheet) {
        EnumMap<EnvironmentalAdversity, EnvironmentalTickResult> result = new EnumMap<>(EnvironmentalAdversity.class);
        states.keySet().forEach(a -> result.put(a, snapshot(a, 0, sheet)));
        return Map.copyOf(result);
    }

    public boolean naturalConductor() { return active(EnvironmentalAdversity.SOAKED); }
    public boolean active(EnvironmentalAdversity adversity) { return adversity != EnvironmentalAdversity.NORMAL && states.get(adversity).active; }
    public boolean insideSource(EnvironmentalAdversity adversity) { return adversity != EnvironmentalAdversity.NORMAL && states.get(adversity).insideSource; }

    /** Forma compacta: devuelve una adversidad presente, sin implicar exclusividad. */
    public EnvironmentalAdversity adversity() {
        return states.entrySet().stream().filter(e -> present(e.getValue())).map(Map.Entry::getKey)
                .findFirst().orElse(EnvironmentalAdversity.NORMAL);
    }
    public boolean active() { return states.values().stream().anyMatch(s -> s.active); }
    public boolean insideSource() { return states.values().stream().anyMatch(s -> s.insideSource); }
    public EnvironmentalTickResult snapshot(CharacterSheet sheet) { return snapshot(adversity(), sheet); }

    private boolean isImmune(EnvironmentalAdversity adversity, CharacterSheet sheet, EquipmentState equipment) {
        if (new EnvironmentalSetBonusPolicy().immuneTo(sheet, equipment, adversity)) return true;
        if (custodyActive) return true;
        
        var immunities = equipment.effectImmunities(sheet);
        return (adversity == EnvironmentalAdversity.VIRULENT_TOXICITY && immunities.contains(EffectImmunity.VIRULENT_TOXICITY))
                || (adversity == EnvironmentalAdversity.SUFFOCATING_HEAT && immunities.contains(EffectImmunity.SUFFOCATING_HEAT));
    }

    private static void beginRecovery(State state, StaminaRecovery staminaRecovery) {
        if (present(state)) state.recoverySecondsRemaining = staminaRecovery.fullRecoverySeconds();
    }
    private static boolean present(State state) { return state.exposureSeconds > 0 || state.active || state.recoverySecondsRemaining > 0; }
    private static void clear(State state) { state.exposureSeconds = 0; state.active = false; state.recoverySecondsRemaining = 0; }
    private static double requiredExposureSeconds(EnvironmentalAdversity adversity, CharacterSheet sheet) { double a=sheet.valueOf(Attribute.ADAPTABILIDAD); return adversity==EnvironmentalAdversity.BITING_FROST?a:a*0.1; }

    private EnvironmentalTickResult snapshot(EnvironmentalAdversity adversity, double damage, CharacterSheet sheet) {
        State state = states.get(adversity);
        return new EnvironmentalTickResult(adversity, state.exposureSeconds, requiredExposureSeconds(adversity, sheet), state.active,
                state.recoverySecondsRemaining > 0, state.recoverySecondsRemaining, damage,
                adversity == EnvironmentalAdversity.SOAKED && state.active);
    }
    private EnvironmentalTickResult normalSnapshot(CharacterSheet sheet) {
        return new EnvironmentalTickResult(EnvironmentalAdversity.NORMAL, 0, 0, false, false, 0, 0, naturalConductor());
    }
    private static void validate(double elapsedSeconds, CharacterSheet sheet, EquipmentState equipment, StaminaRecovery staminaRecovery) {
        if (elapsedSeconds < 0) throw new IllegalArgumentException("El tiempo transcurrido no puede ser negativo.");
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Objects.requireNonNull(staminaRecovery, "La recuperación de PA no puede ser nula.");
    }
}
