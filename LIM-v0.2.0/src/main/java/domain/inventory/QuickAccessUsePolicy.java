package domain.inventory;

import domain.inventory.item.misc.MiscellaneousCategory;
import domain.inventory.item.misc.StackableMiscellaneousItem;
import domain.inventory.item.misc.RepairResourceContainer;
import domain.inventory.item.misc.PortableLaboratoryItem;
import domain.inventory.item.misc.ReconnaissanceMonocularItem;

import java.util.Objects;

/**
 * : toda acción inmediata sobre un objeto exige que esa misma instancia
 * esté asignada a un acceso rápido disponible.
 */
public final class QuickAccessUsePolicy {
    private QuickAccessUsePolicy() {}

    public static boolean requiresQuickAccess(InventoryEntry item) {
        Objects.requireNonNull(item, "El objeto no puede ser nulo.");
        if (item instanceof domain.inventory.item.ammunition.AmmunitionSource) return true;
        if (item instanceof RepairResourceContainer || item instanceof ReconnaissanceMonocularItem || item instanceof domain.inventory.item.misc.MechanicalLampItem) return true;
        if (!(item instanceof StackableMiscellaneousItem miscellaneous)) return false;
        return switch (miscellaneous.category()) {
            case FOOD, BEVERAGE, HEALING, STIMULANT, MONEY, OBJECT -> true;
        };
    }

    public static boolean isAssignedAndAvailable(
            InventoryEntry item,
            QuickAccessBar bar,
            domain.inventory.equipment.EquipmentState equipment,
            domain.inventory.logistics.LogisticsState logistics
    ) {
        Objects.requireNonNull(item, "El objeto no puede ser nulo.");
        Objects.requireNonNull(bar, "Los accesos rápidos no pueden ser nulos.");
        for (int index = 0; index < QuickAccessBar.SLOT_COUNT; index++) {
            int slotNumber = index + 1;
            if (!QuickAccessPolicy.isSlotAvailable(slotNumber, equipment, logistics)) continue;
            if (bar.slots().get(index).filter(assigned -> assigned == item).isPresent()) return true;
        }
        return false;
    }

    public static QuickAccessUseResult authorize(
            InventoryEntry item,
            InventoryState inventory
    ) {
        Objects.requireNonNull(inventory, "El inventario no puede ser nulo.");
        if (!requiresQuickAccess(item)) return QuickAccessUseResult.permitted();
        if (item instanceof domain.inventory.item.misc.MechanicalLampItem) {
            boolean chestQuick = QuickAccessPolicy.isSlotAvailable(2, inventory.equipment(), inventory.logistics())
                    && inventory.quickAccessBar().slots().get(1).filter(assigned -> assigned == item).isPresent();
            return chestQuick ? QuickAccessUseResult.permitted()
                    : QuickAccessUseResult.rejected("La linterna mecánica debe ir enganchada a la coraza y asignada a Quick CHEST.");
        }
        return isAssignedAndAvailable(item, inventory.quickAccessBar(), inventory.equipment(), inventory.logistics())
                ? QuickAccessUseResult.permitted()
                : QuickAccessUseResult.rejected("El objeto debe equiparse en un acceso rápido disponible antes de usarlo.");
    }

    public static boolean isActiveEquipment(
            InventoryEntry item,
            domain.inventory.equipment.EquipmentState equipment
    ) {
        Objects.requireNonNull(item, "El objeto no puede ser nulo.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        return java.util.Arrays.stream(domain.inventory.equipment.EquipmentSlot.values())
                .anyMatch(slot -> equipment.itemAt(slot).filter(equipped -> equipped == item).isPresent());
    }

    /**
     * Excepción : un recurso solicitado por equipamiento activo puede usarse desde el inventario
     * sin ocupar acceso rápido. No autoriza el uso directo del recurso por parte del jugador.
     */
    public static QuickAccessUseResult authorizeRequestedByActiveEquipment(
            InventoryEntry requester,
            InventoryEntry resource,
            InventoryState inventory
    ) {
        Objects.requireNonNull(requester, "El solicitante no puede ser nulo.");
        Objects.requireNonNull(resource, "El recurso no puede ser nulo.");
        Objects.requireNonNull(inventory, "El inventario no puede ser nulo.");
        if (!isActiveEquipment(requester, inventory.equipment())) {
            return QuickAccessUseResult.rejected("El recurso solo puede ser llamado por equipamiento activo.");
        }
        boolean stored = java.util.Arrays.stream(domain.inventory.logistics.InventoryCompartmentType.values())
                .map(inventory.logistics()::compartment)
                .filter(domain.inventory.logistics.InventoryCompartment::available)
                .flatMap(compartment -> compartment.entries().stream())
                .anyMatch(entry -> entry == resource);
        return stored
                ? QuickAccessUseResult.permitted()
                : QuickAccessUseResult.rejected("El recurso solicitado no está disponible en el inventario.");
    }

}
