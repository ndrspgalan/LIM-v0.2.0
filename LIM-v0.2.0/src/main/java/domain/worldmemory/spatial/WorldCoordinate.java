package domain.worldmemory.spatial;

public record WorldCoordinate(double x, double y, double elevation) {
    public WorldCoordinate {
        requireFinite(x, "x");
        requireFinite(y, "y");
        requireFinite(elevation, "elevation");
    }

    private static void requireFinite(double value, String coordinate) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("La coordenada " + coordinate + " debe ser finita.");
        }
    }
}
