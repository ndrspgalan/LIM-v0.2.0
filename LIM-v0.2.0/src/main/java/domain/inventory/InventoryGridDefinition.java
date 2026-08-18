package domain.inventory;

/** Rejilla bidimensional visible del inventario.  admite 0x0 para proveedores físicos todavía inactivos. */
public record InventoryGridDefinition(int verticalSlots, int horizontalSlots) {
    public InventoryGridDefinition {
        boolean empty = verticalSlots == 0 && horizontalSlots == 0;
        boolean positive = verticalSlots > 0 && horizontalSlots > 0;
        if (!empty && !positive) {
            throw new IllegalArgumentException("Las dimensiones del inventario deben ser positivas o 0x0 para capacidad inactiva.");
        }
    }

    public static InventoryGridDefinition empty() { return new InventoryGridDefinition(0, 0); }
    public boolean isEmpty() { return verticalSlots == 0; }
    public int capacity() { return Math.multiplyExact(verticalSlots, horizontalSlots); }
}
