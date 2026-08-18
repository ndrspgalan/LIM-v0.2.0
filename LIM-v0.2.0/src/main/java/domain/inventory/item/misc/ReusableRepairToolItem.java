package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.util.List;

/** Herramienta persistente de mantenimiento: no consume usos tras su adquisición. */
public final class ReusableRepairToolItem extends InventoryEntry {
    public enum Kind { ARTISAN_BOX, TOOLBOX }
    private final Kind kind;

    public ReusableRepairToolItem(Kind kind, String name, String description, double weightKg,
                                  InventoryFootprint footprint, List<String> statistics) {
        super(name, description, weightKg, footprint, statistics);
        this.kind = kind;
    }

    public Kind kind() { return kind; }
    public boolean hasInfiniteUses() { return true; }
}
