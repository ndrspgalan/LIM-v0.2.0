package domain.inventory;

public record InventoryFootprint(int verticalSlots, int horizontalSlots) {
    private static final double METERS_PER_SLOT = 0.10;
    public InventoryFootprint {
        boolean equipmentOnly = verticalSlots == 0 && horizontalSlots == 0;
        boolean gridItem = verticalSlots > 0 && horizontalSlots > 0;
        if (!equipmentOnly && !gridItem) {
            throw new IllegalArgumentException(
                    "Las dimensiones deben ser positivas o 0 x 0 para un objeto sin tamaño de inventario definido."
            );
        }
    }

    public static InventoryFootprint fromMetricDimensions(double verticalMeters, double horizontalMeters) {
        if (!Double.isFinite(verticalMeters) || !Double.isFinite(horizontalMeters)
                || verticalMeters <= 0 || horizontalMeters <= 0) {
            throw new IllegalArgumentException("Las dimensiones métricas deben ser finitas y mayores que cero.");
        }
        return new InventoryFootprint(
                slotsFor(verticalMeters),
                slotsFor(horizontalMeters)
        );
    }

    private static int slotsFor(double meters) {
        return (int) Math.ceil((meters / METERS_PER_SLOT) - 1e-9);
    }

    public static InventoryFootprint equipmentOnly() {
        return new InventoryFootprint(0, 0);
    }

    public boolean hasGridDimensions() {
        return verticalSlots > 0;
    }

    public int occupiedSlots() {
        return verticalSlots * horizontalSlots;
    }


    public InventoryFootprint rotated90() {
        return new InventoryFootprint(horizontalSlots, verticalSlots);
    }

    public boolean isSquare() {
        return verticalSlots == horizontalSlots;
    }

    public boolean fitsInside(InventoryGridDefinition grid) {
        return hasGridDimensions()
                && verticalSlots <= grid.verticalSlots()
                && horizontalSlots <= grid.horizontalSlots();
    }
}
