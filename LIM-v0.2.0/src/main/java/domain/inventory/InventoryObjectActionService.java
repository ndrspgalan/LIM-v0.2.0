package domain.inventory;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.LogisticsState;
import domain.worldmemory.WorldMemory;
import domain.worldmemory.spatial.WorldCoordinate;
import domain.inventory.catalog.PhysicalObjectCatalog;
import java.util.*;

/** transiciones seguras de las acciones de inventario que alteran asignación o propiedad. */
public final class InventoryObjectActionService {
    private static final InventoryAutoPlacementService AUTO_PLACEMENT = new InventoryAutoPlacementService();
    private InventoryObjectActionService() {}

    public static InventoryState equipQuickAccess(InventoryState inventory, InventoryEntry item, int slot) {
        var a=InventoryObjectActionPolicy.evaluate(item,inventory);
        if(!a.eligibleQuickSlots().contains(slot)) throw new IllegalStateException("El objeto no puede asignarse al acceso rápido "+slot+".");
        return new InventoryState(inventory.equipment(), inventory.quickAccessBar().assign(slot,item), inventory.logistics(),inventory.armorLayout());
    }

    public static InventoryState unequipQuickAccess(InventoryState inventory, InventoryEntry item) {
        Objects.requireNonNull(inventory); Objects.requireNonNull(item);
        InventoryState cleared = new InventoryState(inventory.equipment(),inventory.quickAccessBar().clearItem(item),inventory.logistics(),inventory.armorLayout());
        // Quick es normalmente una referencia a un objeto ya almacenado. Si un estado legado no conserva origen físico,
        // el objeto vuelve a ingresar por la misma política universal Quick 1→4.
        if (InventoryObjectActionPolicy.storedCompartment(item,cleared.logistics()).isPresent()) return cleared;
        InventoryAdmissionResult admitted=AUTO_PLACEMENT.admit(cleared,item,InventoryAdmissionSource.QUICK_UNEQUIP);
        if(!admitted.accepted()) throw new IllegalStateException(admitted.message());
        return admitted.inventory();
    }

    public static InventoryState equipActive(InventoryState inventory, InventoryEntry item, EquipmentSlot slot) {
        var a=InventoryObjectActionPolicy.evaluate(item,inventory);
        if(!a.eligibleEquipmentSlots().contains(slot)) throw new IllegalStateException("El objeto no puede equiparse en "+slot.label()+".");
        InventoryCompartmentType source=InventoryObjectActionPolicy.storedCompartment(item,inventory.logistics()).orElseThrow();
        LogisticsState logistics=removeFrom(inventory.logistics(),source,item);
        EquipmentState equipment=inventory.equipment().withItem(slot,item);
        logistics=logistics.synchronizeGarmentStorage(equipment);
        return new InventoryState(equipment,inventory.quickAccessBar().clearItem(item),logistics,inventory.armorLayout());
    }

    public static InventoryState unequipActive(InventoryState inventory, InventoryEntry item) {
        Objects.requireNonNull(inventory); Objects.requireNonNull(item);
        EquipmentSlot slot=Arrays.stream(EquipmentSlot.values()).filter(s -> inventory.equipment().itemAt(s).filter(i -> i==item).isPresent()).findFirst()
                .orElseThrow(() -> new IllegalStateException("El objeto no está en equipamiento activo."));
        EquipmentState equipment=inventory.equipment().withoutItem(slot);
        LogisticsState reduced=inventory.logistics().synchronizeGarmentStorage(equipment);
        InventoryState base=new InventoryState(equipment,inventory.quickAccessBar().clearItem(item),reduced,inventory.armorLayout());
        InventoryAdmissionResult admitted=AUTO_PLACEMENT.admit(base,item,InventoryAdmissionSource.ACTIVE_UNEQUIP);
        if(!admitted.accepted()) throw new IllegalStateException(admitted.message());
        return admitted.inventory();
    }

    /**
     * Variante contextual : antes de retirar un objeto único registra su posición en la
     * Memoria del Mundo. El drop ordinario se conserva para objetos sin identidad persistente.
     */
    public static InventoryState drop(InventoryState inventory, InventoryEntry item,
                                      WorldMemory worldMemory, WorldCoordinate worldPosition) {
        Objects.requireNonNull(worldMemory);
        Objects.requireNonNull(worldPosition);
        if (PersistentDropPolicy.requiresWorldMemoryTracking(item)) {
            worldMemory.knowledge().rememberPersistentDroppedObject(
                    PhysicalObjectCatalog.typeIdOf(item), worldPosition);
        }
        return drop(inventory,item);
    }

    public static InventoryState drop(InventoryState inventory, InventoryEntry item) {
        Objects.requireNonNull(inventory); Objects.requireNonNull(item);
        Optional<InventoryCompartmentType> source=InventoryObjectActionPolicy.storedCompartment(item,inventory.logistics());
        if(source.isPresent()) return new InventoryState(inventory.equipment(),inventory.quickAccessBar().clearItem(item),removeFrom(inventory.logistics(),source.get(),item),inventory.armorLayout());
        for(EquipmentSlot slot:EquipmentSlot.values()) if(inventory.equipment().itemAt(slot).filter(i -> i==item).isPresent()) {
            EquipmentState equipment=inventory.equipment().withoutItem(slot);
            LogisticsState logistics=inventory.logistics().synchronizeGarmentStorage(equipment); // rechaza quitar una prenda si deja objetos sin soporte
            return new InventoryState(equipment,inventory.quickAccessBar().clearItem(item),logistics,inventory.armorLayout());
        }
        throw new IllegalStateException("El objeto no pertenece al inventario.");
    }

    public static InventoryState rotate90(InventoryState inventory, InventoryEntry item) {
        Objects.requireNonNull(inventory); Objects.requireNonNull(item);
        InventoryCompartmentType source=InventoryObjectActionPolicy.storedCompartment(item,inventory.logistics())
                .orElseThrow(() -> new IllegalStateException("El objeto no está almacenado en el inventario."));
        InventoryOrientation original=item.inventoryOrientation();
        item.rotate90();
        try {
            InventoryCompartment c=inventory.logistics().compartment(source);
            LogisticsState next=inventory.logistics().withCompartment(c.withEntries(c.entries()));
            return new InventoryState(inventory.equipment(),inventory.quickAccessBar(),next,inventory.armorLayout());
        } catch (IllegalArgumentException ex) {
            item.setInventoryOrientation(original);
            throw new IllegalStateException(InventoryAutoPlacementService.NO_SPACE_MESSAGE,ex);
        }
    }

    public static QuickAccessUseResult authorizeUse(InventoryState inventory, InventoryEntry item) {
        var a=InventoryObjectActionPolicy.evaluate(item,inventory);
        return a.allows(InventoryObjectAction.USE) ? QuickAccessUseResult.permitted()
                : QuickAccessUseResult.rejected("El objeto no puede usarse desde su estado actual en el inventario.");
    }

    private static LogisticsState removeFrom(LogisticsState logistics, InventoryCompartmentType type, InventoryEntry item){
        InventoryCompartment c=logistics.compartment(type); ArrayList<InventoryEntry> entries=new ArrayList<>(c.entries());
        if(!entries.removeIf(e -> e==item)) throw new IllegalStateException("El objeto no está almacenado en "+type.label()+".");
        return logistics.withCompartment(c.withEntries(entries));
    }


}