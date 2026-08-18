package domain.inventory.item.armor;

import domain.inventory.InventoryFootprint;

public enum ArmorInventoryCategory {
    HEAD(3, 2),
    CHEST(5, 4),
    BRACERS(3, 2),
    LEGGINGS(4, 2),
    FEET(3, 2),
    INTEGRAL_SUIT(14, 7);

    private final InventoryFootprint footprint;

    ArmorInventoryCategory(int verticalSlots, int horizontalSlots) {
        this.footprint = new InventoryFootprint(verticalSlots, horizontalSlots);
    }

    public InventoryFootprint footprint() {
        return footprint;
    }
}
