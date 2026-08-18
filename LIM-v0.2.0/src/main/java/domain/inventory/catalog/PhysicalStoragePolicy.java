package domain.inventory.catalog;

import domain.inventory.InventoryEntry;
import java.util.Objects;

public final class PhysicalStoragePolicy {
    private PhysicalStoragePolicy(){}

    public static PhysicalStorageSemantics semanticsOf(InventoryEntry entry){
        return PhysicalObjectCatalog.definitionFor(Objects.requireNonNull(entry)).storageSemantics();
    }

    public static boolean occupiesOwnInventoryRectangle(InventoryEntry entry){
        return semanticsOf(entry)==PhysicalStorageSemantics.INDIVIDUAL;
    }

    public static boolean mayAggregateInternally(InventoryEntry entry){
        PhysicalStorageSemantics s=semanticsOf(entry);
        return s==PhysicalStorageSemantics.PERSISTENT_CONTAINER
                || s==PhysicalStorageSemantics.SPECIALIZED_CONTAINER
                || s==PhysicalStorageSemantics.CURRENCY_STACK;
    }
}
