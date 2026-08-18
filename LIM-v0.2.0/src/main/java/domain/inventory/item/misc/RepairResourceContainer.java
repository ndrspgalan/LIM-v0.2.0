package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import java.util.List;

/** Recipiente persistente y recargable para recursos de mantenimiento. */
public abstract class RepairResourceContainer extends StackableMiscellaneousItem {
    protected RepairResourceContainer(String name, String description, int currentUses, int maximumUses,
                                      double structuralWeightKg, double contentWeightPerUseKg,
                                      InventoryFootprint footprint, UseAnimation animation, List<String> statistics) {
        super(name, description, MiscellaneousCategory.OBJECT, currentUses, maximumUses,
                structuralWeightKg, contentWeightPerUseKg, UseResourceKind.QUANTITY,
                footprint, animation, statistics, List.of());
    }
}
