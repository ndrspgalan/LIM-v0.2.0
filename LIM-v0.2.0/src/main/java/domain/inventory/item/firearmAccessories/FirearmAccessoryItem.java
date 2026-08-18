package domain.inventory.item.firearmAccessories;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;

import java.util.List;
import java.util.Objects;

/** Accesorio desmontable que se monta sobre un arma de fuego compatible. */
public final class FirearmAccessoryItem extends InventoryEntry {
    private final FirearmAccessoryMount mount;
    private final double minMagnification;
    private final double maxMagnification;
    private boolean deployed;

    public FirearmAccessoryItem(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            FirearmAccessoryMount mount,
            double minMagnification,
            double maxMagnification,
            List<String> statistics,
            List<ItemProperty> properties
    ) {
        super(name, narrativeDescription, weightKg, footprint, statistics, properties);
        this.mount = Objects.requireNonNull(mount, "El punto de montaje no puede ser nulo.");
        if (!Double.isFinite(minMagnification) || !Double.isFinite(maxMagnification)
                || minMagnification < 0 || maxMagnification < minMagnification) {
            throw new IllegalArgumentException("La magnificación del accesorio no es válida.");
        }
        this.minMagnification = minMagnification;
        this.maxMagnification = maxMagnification;
    }

    public FirearmAccessoryMount mount() { return mount; }
    public double minMagnification() { return minMagnification; }
    public double maxMagnification() { return maxMagnification; }
    public boolean isOptic() { return mount == FirearmAccessoryMount.OPTIC; }
    public boolean deployed() { return deployed; }
    public boolean detachable() { return hasProperty(ItemPropertyId.DETACHABLE); }
    public boolean grantsAssistedOneHanded() { return hasProperty(ItemPropertyId.ASSISTED_ONE_HANDED); }
    public boolean grantsBetterErgonomics() { return hasProperty(ItemPropertyId.BETTER_ERGONOMICS); }
    public boolean grantsAssistedStabilizer() { return hasProperty(ItemPropertyId.ASSISTED_STABILIZER); }
    public boolean grantsPrecisionAssistance() { return hasProperty(ItemPropertyId.PRECISION_ASSISTANCE); }
    /** Magnificación óptica; desde  no multiplica el alcance efectivo del arma. */
    public double effectiveRangeMultiplier() { return 1.0; }

    public boolean deploy() {
        if (mount != FirearmAccessoryMount.BIPOD) return false;
        deployed = true;
        return true;
    }

    public boolean fold() {
        if (mount != FirearmAccessoryMount.BIPOD) return false;
        deployed = false;
        return true;
    }

    private boolean hasProperty(ItemPropertyId id) {
        return properties().stream().anyMatch(property -> property.id() == id);
    }
}
