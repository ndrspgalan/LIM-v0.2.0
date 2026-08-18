package domain.movement;

/** Deslizamiento contextual al pulsar C mientras el personaje está corriendo. */
public final class SlidingPolicy {
    public static final double HEIGHT_MULTIPLIER = 1.5;

    public double distanceMeters(double heightMeters, boolean running) {
        if (!Double.isFinite(heightMeters) || heightMeters <= 0) throw new IllegalArgumentException("La altura debe ser positiva y finita.");
        return running ? heightMeters * HEIGHT_MULTIPLIER : 0.0;
    }
}
