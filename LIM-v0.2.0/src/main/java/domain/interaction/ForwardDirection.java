package domain.interaction;

public record ForwardDirection(double x, double y, double z) {
    public ForwardDirection {
        double length = Math.sqrt(x*x + y*y + z*z);
        if (length == 0) throw new IllegalArgumentException("La dirección frontal no puede ser nula.");
        x /= length; y /= length; z /= length;
    }
}
