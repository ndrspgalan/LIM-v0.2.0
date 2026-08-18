package domain.combat;

public record StaggerResult(double knockbackDistanceMeters, double staggerDurationSeconds) {
    public StaggerResult {
        if (knockbackDistanceMeters < 0 || staggerDurationSeconds < 0) {
            throw new IllegalArgumentException("El retroceso y el aturdimiento no pueden ser negativos.");
        }
    }
    public boolean staggered() { return staggerDurationSeconds > 0; }
    public ImpactRecoveryState resultingState() { return staggered() ? ImpactRecoveryState.STAGGERED : ImpactRecoveryState.NORMAL; }
}
