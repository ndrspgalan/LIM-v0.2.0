package domain.combat.ai.remote;

/**
 * Clasificación remota deliberadamente simple. Los umbrales son datos de la
 * opción/heurística que los consume; esta política no inventa una distancia
 * óptima universal para todas las tecnologías.
 */
public final class RangedDistancePolicy {
    private RangedDistancePolicy() {}

    public static RangedDistanceState classify(
            double currentDistanceMeters,
            double minimumAdequateMeters,
            double maximumEffectiveMeters
    ) {
        if (!Double.isFinite(currentDistanceMeters) || currentDistanceMeters < 0) {
            throw new IllegalArgumentException("La distancia actual debe ser finita y no negativa.");
        }
        if (!Double.isFinite(minimumAdequateMeters) || minimumAdequateMeters < 0) {
            throw new IllegalArgumentException("La distancia mínima adecuada debe ser finita y no negativa.");
        }
        if (!Double.isFinite(maximumEffectiveMeters) || maximumEffectiveMeters <= minimumAdequateMeters) {
            throw new IllegalArgumentException("El alcance efectivo debe superar la distancia mínima adecuada.");
        }
        if (currentDistanceMeters < minimumAdequateMeters) return RangedDistanceState.TOO_CLOSE;
        if (currentDistanceMeters <= maximumEffectiveMeters) return RangedDistanceState.ADEQUATE;
        return RangedDistanceState.TOO_FAR;
    }
}
