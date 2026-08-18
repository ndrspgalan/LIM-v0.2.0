package domain.inventory;

import domain.inventory.catalog.PhysicalStorageSemantics;
import domain.inventory.item.misc.StackableMiscellaneousItem;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.LogisticsState;

import java.util.*;

/**
 * : Quick Access enlaza tipo canónico + expansor origen + instancia actual.
 * Al desaparecer una unidad INDIVIDUAL, se enlaza otra del mismo tipo sólo dentro
 * del mismo expansor. Mochila, alforjas y otros compartimentos nunca se rastrean.
 */
public final class QuickAccessConsumptionPolicy {
    @FunctionalInterface
    public interface ItemUseOperation { boolean use(StackableMiscellaneousItem item); }

    public InventoryState consume(InventoryState inventory,int slotNumber){
        return use(inventory,slotNumber,StackableMiscellaneousItem::consumeOne);
    }

    public InventoryState use(InventoryState inventory,int slotNumber,ItemUseOperation operation){
        Objects.requireNonNull(inventory); Objects.requireNonNull(operation);
        if(slotNumber<1||slotNumber>QuickAccessBar.SLOT_COUNT) throw new IllegalArgumentException("Ranura inválida.");

        QuickAccessBinding binding=inventory.quickAccessBar().binding(slotNumber).orElse(null);
        if(binding==null || !(binding.currentInstance() instanceof StackableMiscellaneousItem item)) return inventory;
        if(!operation.use(item)) return inventory;

        PhysicalStorageSemantics semantics=domain.inventory.catalog.PhysicalObjectCatalog.definitionFor(item).storageSemantics();
        if(!item.isDepleted() || semantics!=PhysicalStorageSemantics.INDIVIDUAL) return inventory;

        InventoryCompartmentType sourceType=binding.sourceCompartment();
        InventoryCompartment source=inventory.logistics().compartment(sourceType);
        ArrayList<InventoryEntry> remaining=new ArrayList<>(source.entries());
        remaining.removeIf(e -> e==item);

        InventoryEntry replacement=remaining.stream()
                .filter(e -> domain.inventory.catalog.PhysicalObjectCatalog.containsName(e.name()))
                .filter(e -> e.canonicalTypeId().equals(binding.typeId()))
                .findFirst().orElse(null);

        LogisticsState nextLogistics=inventory.logistics().withCompartment(source.withEntries(remaining));
        QuickAccessBar nextBar=replacement==null
                ? inventory.quickAccessBar().clear(slotNumber)
                : inventory.quickAccessBar().assign(slotNumber,replacement);
        return new InventoryState(inventory.equipment(),nextBar,nextLogistics,inventory.armorLayout());
    }
}
