package domain.environment;

public record EnvironmentalTickResult(
        EnvironmentalAdversity adversity,
        double exposureSeconds,
        double requiredExposureSeconds,
        boolean active,
        boolean recovering,
        double recoverySecondsRemaining,
        double rawHealthDamage,
        boolean naturalConductor
) {
    public EnvironmentalTickResult {
        if (adversity == null) throw new NullPointerException("La adversidad no puede ser nula.");
        if (exposureSeconds < 0 || requiredExposureSeconds < 0 || recoverySecondsRemaining < 0 || rawHealthDamage < 0) {
            throw new IllegalArgumentException("Los valores temporales y de daño no pueden ser negativos.");
        }
    }
}
