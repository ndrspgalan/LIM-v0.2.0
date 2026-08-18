package domain.maintenance;

import domain.combat.HostileEncounterState;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.QuickAccessUsePolicy;
import domain.inventory.equipment.EquipmentSlot;
import java.util.Objects;

/** el combate no invalida por sí mismo el uso de objetos; manda la política específica del objeto. */
public final class MaintenanceAvailabilityPolicy {
    public boolean canBegin(InventoryEntry target, InventoryEntry repairItem, InventoryState inventory,
                            HostileEncounterState hostileEncounterState) {
        Objects.requireNonNull(target); Objects.requireNonNull(repairItem); Objects.requireNonNull(inventory);
        Objects.requireNonNull(hostileEncounterState);
        if (!QuickAccessUsePolicy.authorize(repairItem, inventory).allowed()) return false;
        if (!QuickAccessUsePolicy.requiresQuickAccess(repairItem)) {
            boolean carried = java.util.Arrays.stream(domain.inventory.logistics.InventoryCompartmentType.values())
                    .map(inventory.logistics()::compartment)
                    .filter(java.util.Objects::nonNull)
                    .filter(domain.inventory.logistics.InventoryCompartment::available)
                    .flatMap(c -> c.entries().stream())
                    .anyMatch(entry -> entry == repairItem);
            if (!carried) return false;
        }
        return inventory.equipment().itemAt(EquipmentSlot.RIGHT_HAND).filter(i -> i == target).isPresent()
                || inventory.equipment().itemAt(EquipmentSlot.LEFT_HAND).filter(i -> i == target).isPresent()
                || inventory.equipment().equippedArmor().stream().anyMatch(i -> i == target);
    }
}
