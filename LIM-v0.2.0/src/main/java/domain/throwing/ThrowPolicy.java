package domain.throwing;

/**
 * Balística de lanzamiento manual .
 *
 * Invariantes de gameplay:
 * - a 35 grados, el alcance horizontal máximo es exactamente DESTREZA metros;
 * - se admite lanzar desde -90 hasta +90 grados;
 * - a +/-90 grados no existe recorrido horizontal;
 * - para cualquier otro ángulo se integra una trayectoria parabólica ideal desde la altura de liberación;
 * - la velocidad de salida se calibra para que la trayectoria de referencia de 35 grados alcance DESTREZA metros.
 *
 * Los ángulos que físicamente superarían ligeramente el alcance de referencia quedan limitados por DESTREZA:
 * 35 grados es el óptimo canónico del gesto de lanzamiento y ningún otro ángulo concede alcance gratuito.
 */
public final class ThrowPolicy {
    public static final double GRAVITY_METERS_PER_SECOND_SQUARED = 9.80665;
    public static final double RELEASE_HEIGHT_FACTOR = 0.95;
    public static final double OPTIMAL_RELEASE_ANGLE_DEGREES = 35.0;

    private final ThrownImpactPolicy impactPolicy;

    public ThrowPolicy() {
        this(new ThrownImpactPolicy());
    }

    public ThrowPolicy(ThrownImpactPolicy impactPolicy) {
        if (impactPolicy == null) throw new IllegalArgumentException("La política de impacto no puede ser nula.");
        this.impactPolicy = impactPolicy;
    }

    public ThrowResult resolve(ThrowRequest request, ThrownPayload payload) {
        if (request == null) throw new IllegalArgumentException("La solicitud no puede ser nula.");
        if (payload == null) throw new IllegalArgumentException("La unidad lanzada no puede ser nula.");

        double releaseHeight = request.characterHeightMeters() * RELEASE_HEIGHT_FACTOR;
        double optimalDistance = request.dexterity();
        double releaseVelocity = calibratedVelocityForOptimalDistance(optimalDistance, releaseHeight);
        double distance = horizontalRange(releaseVelocity, releaseHeight, request.releaseAngleDegrees());
        distance = Math.min(optimalDistance, Math.max(0.0, distance));
        if (Math.abs(request.releaseAngleDegrees() - OPTIMAL_RELEASE_ANGLE_DEGREES) < 1.0e-9) {
            distance = optimalDistance;
        }

        double impactVelocity = Math.sqrt(releaseVelocity * releaseVelocity
                + 2.0 * GRAVITY_METERS_PER_SECOND_SQUARED * releaseHeight);
        double impactEnergy = 0.5 * payload.profile().massKg() * impactVelocity * impactVelocity;

        return new ThrowResult(
                payload,
                releaseHeight,
                releaseVelocity,
                distance,
                impactVelocity,
                impactEnergy,
                impactPolicy.resolve(request.strength(), payload.profile())
        );
    }

    private static double calibratedVelocityForOptimalDistance(double distance, double releaseHeight) {
        double theta = Math.toRadians(OPTIMAL_RELEASE_ANGLE_DEGREES);
        double denominator = 2.0 * Math.pow(Math.cos(theta), 2.0)
                * (releaseHeight + distance * Math.tan(theta));
        return Math.sqrt(GRAVITY_METERS_PER_SECOND_SQUARED * distance * distance / denominator);
    }

    private static double horizontalRange(double velocity, double releaseHeight, double angleDegrees) {
        if (Math.abs(Math.abs(angleDegrees) - 90.0) < 1.0e-9) return 0.0;
        double theta = Math.toRadians(angleDegrees);
        double vx = velocity * Math.cos(theta);
        double vy = velocity * Math.sin(theta);
        double discriminant = vy * vy + 2.0 * GRAVITY_METERS_PER_SECOND_SQUARED * releaseHeight;
        double flightSeconds = (vy + Math.sqrt(discriminant)) / GRAVITY_METERS_PER_SECOND_SQUARED;
        return Math.max(0.0, vx * flightSeconds);
    }
}
