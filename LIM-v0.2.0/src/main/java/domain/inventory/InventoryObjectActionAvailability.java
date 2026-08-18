package domain.inventory;

import domain.inventory.equipment.ArmorEquipDestination;
import domain.inventory.equipment.EquipmentSlot;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record InventoryObjectActionAvailability(
        Map<InventoryObjectAction, Boolean> allowed,
        List<EquipmentSlot> eligibleEquipmentSlots,
        List<ArmorEquipDestination> eligibleArmorDestinations,
        List<Integer> eligibleQuickSlots
) {
    public InventoryObjectActionAvailability {
        Objects.requireNonNull(allowed); Objects.requireNonNull(eligibleEquipmentSlots);
        Objects.requireNonNull(eligibleArmorDestinations); Objects.requireNonNull(eligibleQuickSlots);
        allowed = Map.copyOf(allowed);
        eligibleEquipmentSlots = List.copyOf(eligibleEquipmentSlots);
        eligibleArmorDestinations = List.copyOf(eligibleArmorDestinations);
        eligibleQuickSlots = List.copyOf(eligibleQuickSlots);
    }
    public InventoryObjectActionAvailability(Map<InventoryObjectAction, Boolean> allowed,
                                             List<EquipmentSlot> eligibleEquipmentSlots,
                                             List<Integer> eligibleQuickSlots) {
        this(allowed, eligibleEquipmentSlots, List.of(), eligibleQuickSlots);
    }
    public boolean allows(InventoryObjectAction action){ return allowed.getOrDefault(action,false); }
}
