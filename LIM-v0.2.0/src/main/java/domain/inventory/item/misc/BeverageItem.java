package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import java.util.List;

public final class BeverageItem extends StackableMiscellaneousItem {
    public BeverageItem(String name, String description, int currentUses, int maximumUses,
                        double structuralWeightKg, double waterWeightPerUseKg,
                        InventoryFootprint footprint, double useSeconds) {
        super(name, description, MiscellaneousCategory.BEVERAGE, currentUses, maximumUses,
                structuralWeightKg, waterWeightPerUseKg, UseResourceKind.CHARGES, footprint,
                new UseAnimation(useSeconds, List.of("Extraer el recipiente.", "Beber una carga de agua.", "Cerrar y guardar el recipiente.")),
                List.of("SED | -1 nivel", "USOS | " + currentUses + " / " + maximumUses), List.of());
    }
}
