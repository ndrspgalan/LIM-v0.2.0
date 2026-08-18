package domain.inventory.logistics;

/** Dimensiones físicas del contenedor en slots métricos de 0,1 m: X(ancho), Y(alto), Z(profundidad). */
public record InventoryPhysicalDimensions(int xSlots, int ySlots, int zSlots) {
    private static final double METERS_PER_SLOT = 0.10;
    public InventoryPhysicalDimensions {
        if (xSlots <= 0 || ySlots <= 0 || zSlots <= 0) {
            throw new IllegalArgumentException("X, Y y Z deben ser mayores que cero.");
        }
    }

    public static InventoryPhysicalDimensions fromMetricDimensions(double xMeters, double yMeters, double zMeters) {
        if (!Double.isFinite(xMeters) || !Double.isFinite(yMeters) || !Double.isFinite(zMeters)
                || xMeters <= 0 || yMeters <= 0 || zMeters <= 0) {
            throw new IllegalArgumentException("X, Y y Z métricos deben ser finitos y mayores que cero.");
        }
        return new InventoryPhysicalDimensions(slotsFor(xMeters), slotsFor(yMeters), slotsFor(zMeters));
    }

    private static int slotsFor(double meters) {
        return (int) Math.ceil((meters / METERS_PER_SLOT) - 1e-9);
    }
}
