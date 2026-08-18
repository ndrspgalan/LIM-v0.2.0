package domain.inventory.item.ammunition;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.util.List;
import java.util.Objects;

/** Munición unitaria no recuperable con footprint propio (p. ej. cohetes pesados). */
public final class UnitaryAmmunitionItem extends InventoryEntry implements AmmunitionSource {
    private final AmmunitionDescriptor descriptor;
    private boolean available = true;

    public UnitaryAmmunitionItem(String name, String narrativeDescription, double weightKg,
            InventoryFootprint footprint, AmmunitionDescriptor descriptor) {
        super(name, narrativeDescription, weightKg, footprint,
                List.of("Munición unitaria | " + descriptor.caliber(),
                        "Material / carga | " + descriptor.material(),
                        "Variante | " + descriptor.variant(),
                        "Stack máximo | 1"));
        this.descriptor = Objects.requireNonNull(descriptor);
        if (descriptor.recoverable()) throw new IllegalArgumentException("Esta munición unitaria pesada no es recuperable.");
    }

    @Override public AmmunitionDescriptor ammunitionDescriptor() { return descriptor; }
    @Override public int remainingUnits() { return available ? 1 : 0; }
    @Override public int maxUnits() { return 1; }
    @Override public int shotsLoadedPerConsumedUnit() { return 1; }
    @Override public boolean consumeOneUnit() { if (!available) return false; available = false; return true; }
}
