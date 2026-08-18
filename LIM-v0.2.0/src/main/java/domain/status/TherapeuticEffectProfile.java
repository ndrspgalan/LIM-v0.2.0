package domain.status;

import java.util.Objects;

/** Contrato mecánico tipado de un consumible curativo o estimulante. */
public record TherapeuticEffectProfile(
        HealingKind healingKind,
        double instantHealthFraction,
        double healthRegenerationMultiplier,
        double minimumHealth,
        double staminaRegenerationMultiplier,
        double carryingCapacityMultiplier,
        int physicalStabilityModifier,
        int sanityModifier,
        double feintReachMultiplier,
        double mirageInvulnerabilityMultiplier,
        boolean frenzyImmunity,
        TimedEffect duration
) {
    public TherapeuticEffectProfile {
        Objects.requireNonNull(healingKind, "El tipo de curación no puede ser nulo.");
        if (instantHealthFraction < 0 || instantHealthFraction > 1) throw new IllegalArgumentException("Fracción de PV no válida.");
        if (healthRegenerationMultiplier <= 0 || staminaRegenerationMultiplier <= 0
                || carryingCapacityMultiplier <= 0 || feintReachMultiplier <= 0
                || mirageInvulnerabilityMultiplier <= 0) {
            throw new IllegalArgumentException("Los multiplicadores terapéuticos deben ser positivos.");
        }
        if (minimumHealth < 0) throw new IllegalArgumentException("El mínimo de PV no puede ser negativo.");
    }

    public static TherapeuticEffectProfile none() {
        return new TherapeuticEffectProfile(HealingKind.NONE, 0, 1, 0, 1, 1,
                0, 0, 1, 1, false, null);
    }

    public boolean hasTimedEffects() { return duration != null; }

    public enum HealingKind { NONE, YARROW, BOG_MOSS }
}
