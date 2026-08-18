package domain.interaction;

/**
 * Alcance funcional frontal proporcional a la talla corporal.
 * Se adopta 44 % de la altura como longitud hombro-mano operativa.
 */
public final class AnthropometricInteractionReachPolicy {
    public static final double HEIGHT_TO_REACH_RATIO = 0.44;
    public static final double HALF_FIELD_OF_VIEW_DEGREES = 55.0;

    public double reachMeters(double heightMeters) {
        if (heightMeters <= 0.5 || heightMeters > 3.0) throw new IllegalArgumentException("Altura corporal no válida.");
        return heightMeters * HEIGHT_TO_REACH_RATIO;
    }

    public boolean canReach(double heightMeters, SpatialPoint actor, ForwardDirection facing, SpatialPoint target) {
        double distance = actor.distanceTo(target);
        if (distance > reachMeters(heightMeters)) return false;
        if (distance == 0) return true;
        double vx = (target.x() - actor.x()) / distance;
        double vy = (target.y() - actor.y()) / distance;
        double vz = (target.z() - actor.z()) / distance;
        double dot = Math.max(-1, Math.min(1, vx*facing.x() + vy*facing.y() + vz*facing.z()));
        double angle = Math.toDegrees(Math.acos(dot));
        return angle <= HALF_FIELD_OF_VIEW_DEGREES;
    }
}
