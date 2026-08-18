package domain.targeting;

import domain.environment.time.DayPhase;
/** Distancia universal de fijación: 30 m; de noche 6 m. Independiente del Tipo de Relación. */
public final class TargetLockRangePolicy {
    public static final double DAY_RANGE_METERS = 30.0;
    public static final double NIGHT_RANGE_METERS = 6.0;
    public double maximumRange(DayPhase phase) { return phase == DayPhase.NIGHT ? NIGHT_RANGE_METERS : DAY_RANGE_METERS; }
    public boolean canLock(double distanceMeters, DayPhase phase, boolean otherwiseAllowed) {
        if (!Double.isFinite(distanceMeters) || distanceMeters < 0) throw new IllegalArgumentException("Distancia inválida.");
        return otherwiseAllowed && distanceMeters <= maximumRange(phase);
    }
}
