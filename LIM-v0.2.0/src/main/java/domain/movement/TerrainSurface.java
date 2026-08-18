package domain.movement;

/** Superficie expresada mediante ángulo de pendiente en grados. */
public record TerrainSurface(double slopeDegrees, boolean climbable) {
    public TerrainSurface {
        if (!Double.isFinite(slopeDegrees) || slopeDegrees < 0.0) {
            throw new IllegalArgumentException("La pendiente debe ser un ángulo finito no negativo.");
        }
    }
}
