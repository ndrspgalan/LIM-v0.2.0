package domain.status;

/** Modificadores terapéuticos activos ya agregados para su consumo por políticas existentes. */
public record ActiveTherapeuticEffects(
        double healthRegenerationMultiplier,
        double minimumHealth,
        double staminaRegenerationMultiplier,
        double carryingCapacityMultiplier,
        int physicalStabilityModifier,
        int sanityModifier,
        double feintReachMultiplier,
        double mirageInvulnerabilityMultiplier,
        boolean frenzyImmune
) {
    public ActiveTherapeuticEffects {
        if (healthRegenerationMultiplier <= 0 || staminaRegenerationMultiplier <= 0
                || carryingCapacityMultiplier <= 0 || feintReachMultiplier <= 0
                || mirageInvulnerabilityMultiplier <= 0 || minimumHealth < 0) {
            throw new IllegalArgumentException("Modificadores terapéuticos no válidos.");
        }
    }

    public static ActiveTherapeuticEffects none() {
        return new ActiveTherapeuticEffects(1, 0, 1, 1, 0, 0, 1, 1, false);
    }

    public ActiveTherapeuticEffects apply(TherapeuticEffectProfile effect) {
        if (effect == null) return this;
        return new ActiveTherapeuticEffects(
                healthRegenerationMultiplier * effect.healthRegenerationMultiplier(),
                Math.max(minimumHealth, effect.minimumHealth()),
                staminaRegenerationMultiplier * effect.staminaRegenerationMultiplier(),
                carryingCapacityMultiplier * effect.carryingCapacityMultiplier(),
                physicalStabilityModifier + effect.physicalStabilityModifier(),
                sanityModifier + effect.sanityModifier(),
                feintReachMultiplier * effect.feintReachMultiplier(),
                mirageInvulnerabilityMultiplier * effect.mirageInvulnerabilityMultiplier(),
                frenzyImmune || effect.frenzyImmunity()
        );
    }
}
