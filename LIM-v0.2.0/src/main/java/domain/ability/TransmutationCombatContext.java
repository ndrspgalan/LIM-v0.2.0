package domain.ability;

public record TransmutationCombatContext(
        boolean inCombat,
        double currentHealth,
        double maximumHealth,
        boolean nextActionIsFeint,
        boolean committedSequenceContinues,
        double currentStamina,
        double nextImmediateActionStaminaCost
) {
    public TransmutationCombatContext {
        if (!Double.isFinite(currentHealth) || !Double.isFinite(maximumHealth)
                || maximumHealth <= 0 || currentHealth < 0 || currentHealth > maximumHealth) {
            throw new IllegalArgumentException("Los PV no son válidos.");
        }
        if (!Double.isFinite(currentStamina) || !Double.isFinite(nextImmediateActionStaminaCost)
                || currentStamina < 0 || nextImmediateActionStaminaCost < 0) {
            throw new IllegalArgumentException("Los PA no son válidos.");
        }
    }

    public boolean healthIsIncomplete() {
        return currentHealth < maximumHealth;
    }

    public boolean needsOverdriveForNextImmediateAction() {
        return committedSequenceContinues && nextImmediateActionStaminaCost > currentStamina;
    }
}
