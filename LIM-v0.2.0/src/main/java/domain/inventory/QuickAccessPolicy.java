package domain.inventory;

import domain.inventory.equipment.EquipmentState;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.LogisticsState;

import java.util.Objects;
import java.util.Optional;

/** los cuatro accesos rápidos existen estructuralmente, pero se desbloquean sólo por equipo físico. */
public final class QuickAccessPolicy {
    private QuickAccessPolicy() {}

    public static boolean isSlotAvailable(int slotNumber, EquipmentState equipment, LogisticsState logistics) {
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Objects.requireNonNull(logistics, "La logística no puede ser nula.");
        return switch (slotNumber) {
            case 1 -> logistics.compartment(InventoryCompartmentType.LEGGINGS_STORAGE).available();
            case 2 -> logistics.compartment(InventoryCompartmentType.CHEST_STORAGE).available();
            case 3 -> logistics.compartment(InventoryCompartmentType.LEG_POUCH).available();
            case 4 -> logistics.compartment(InventoryCompartmentType.BANDOLIER).available();
            default -> throw new IllegalArgumentException("El acceso rápido debe estar entre 1 y 4.");
        };
    }

    public static InventoryCompartmentType sourceCompartment(int slotNumber) {
        return switch (slotNumber) {
            case 1 -> InventoryCompartmentType.LEGGINGS_STORAGE;
            case 2 -> InventoryCompartmentType.CHEST_STORAGE;
            case 3 -> InventoryCompartmentType.LEG_POUCH;
            case 4 -> InventoryCompartmentType.BANDOLIER;
            default -> throw new IllegalArgumentException("El acceso rápido debe estar entre 1 y 4.");
        };
    }

    public static void validate(QuickAccessBar bar, EquipmentState equipment, LogisticsState logistics) {
        Objects.requireNonNull(bar, "Los accesos rápidos no pueden ser nulos.");
        for (int index = 0; index < QuickAccessBar.SLOT_COUNT; index++) {
            int slotNumber = index + 1;
            Optional<InventoryEntry> assigned = bar.slots().get(index);
            boolean available = isSlotAvailable(slotNumber, equipment, logistics);
            if (!available && assigned.isPresent()) throw new IllegalArgumentException("El acceso rápido " + slotNumber + " no está disponible y no puede contener objetos.");
            if (assigned.isEmpty()) continue;
            if (assigned.get() instanceof domain.inventory.item.misc.MechanicalLampItem && slotNumber != 2) {
                throw new IllegalArgumentException("Las linternas mecánicas de pecho sólo pueden asignarse a Quick CHEST (2).");
            }
            InventoryCompartment source = logistics.compartment(sourceCompartment(slotNumber));
            boolean belongsToSource = source.entries().stream().anyMatch(entry -> entry == assigned.get());
            if (!belongsToSource) throw new IllegalArgumentException("El objeto de acceso rápido " + slotNumber + " debe encontrarse en " + source.type().label() + ".");
        }
    }
}
