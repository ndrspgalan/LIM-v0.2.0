package domain.inventory.logistics;

import domain.inventory.InventoryGridDefinition;
import java.util.Objects;

/** bolsillo/compartimento físico independiente dentro de una prenda o contenedor. */
public record InventoryStorageModule(String label, InventoryPhysicalDimensions physicalDimensions, InventoryGridDefinition grid) {
    public InventoryStorageModule {
        if (label == null || label.isBlank()) throw new IllegalArgumentException("El módulo debe tener nombre.");
        Objects.requireNonNull(grid, "La rejilla del módulo no puede ser nula.");
        if (physicalDimensions == null && grid.isEmpty()) throw new IllegalArgumentException("Un módulo debe aportar capacidad.");
    }

    public InventoryStorageModule(String label, InventoryPhysicalDimensions physicalDimensions) {
        this(label, Objects.requireNonNull(physicalDimensions), InventoryVolumeProjectionPolicy.project(physicalDimensions));
    }

    public static InventoryStorageModule fromGrid(String label, InventoryGridDefinition grid) {
        return new InventoryStorageModule(label, null, grid);
    }

    public int capacity() { return grid.capacity(); }
}
