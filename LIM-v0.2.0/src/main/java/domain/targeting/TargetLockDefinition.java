package domain.targeting;

public record TargetLockDefinition(TargetLockPoint point, double heightRatio) {
    public TargetLockDefinition {
        if (point == null) {
            throw new IllegalArgumentException("El punto de fijación no puede ser nulo.");
        }
        if (heightRatio <= 0 || heightRatio >= 1) {
            throw new IllegalArgumentException("La proporción de altura debe estar entre 0 y 1.");
        }
    }

    public static TargetLockDefinition stomachHeight() {
        return new TargetLockDefinition(TargetLockPoint.STOMACH, 0.55);
    }
}
