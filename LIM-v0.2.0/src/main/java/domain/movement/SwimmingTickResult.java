package domain.movement;

public record SwimmingTickResult(SwimmingState state, double staminaBefore,
                                 double staminaAfter, double staminaConsumed) {
    public SwimmingTickResult {
        if (state == null) throw new IllegalArgumentException("El estado no puede ser nulo.");
        if (staminaBefore < 0.0 || staminaAfter < 0.0 || staminaConsumed < 0.0) {
            throw new IllegalArgumentException("Los valores de PA no pueden ser negativos.");
        }
    }

    public boolean dead() { return state == SwimmingState.DEAD_BY_DROWNING; }
}
