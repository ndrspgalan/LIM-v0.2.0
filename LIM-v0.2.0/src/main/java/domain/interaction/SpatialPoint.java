package domain.interaction;

public record SpatialPoint(double x, double y, double z) {
    public double distanceTo(SpatialPoint other) {
        double dx = other.x - x, dy = other.y - y, dz = other.z - z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
