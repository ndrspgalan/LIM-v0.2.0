package domain.inventory.logistics;

import domain.inventory.InventoryGridDefinition;
import domain.inventory.InventoryFootprint;
import java.util.Objects;

/**
 * : proyecta profundidad física sobre la interfaz 2D sin fingir que la rejilla es la silueta exterior.
 * Canon V881: X×Y×Z -> (X·Z)×(Y·Z).
 */
public final class InventoryVolumeProjectionPolicy {
    private InventoryVolumeProjectionPolicy() {}
    public static InventoryFootprint footprint(InventoryPhysicalDimensions dimensions) {
        InventoryGridDefinition grid = project(dimensions);
        return new InventoryFootprint(grid.verticalSlots(), grid.horizontalSlots());
    }

    public static InventoryGridDefinition project(InventoryPhysicalDimensions dimensions) {
        Objects.requireNonNull(dimensions, "Las dimensiones físicas no pueden ser nulas.");
        return new InventoryGridDefinition(
                Math.multiplyExact(dimensions.xSlots(), dimensions.zSlots()),
                Math.multiplyExact(dimensions.ySlots(), dimensions.zSlots())
        );
    }
}
