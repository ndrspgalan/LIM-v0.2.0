package domain.character.sheet;

public record StaminaRecovery(double pointsPerSecond, double fullRecoverySeconds, boolean immobilized) {
    public StaminaRecovery {
        if (pointsPerSecond < 0 || fullRecoverySeconds < 0) {
            throw new IllegalArgumentException("La recuperación de PA no puede ser negativa.");
        }
    }
}
