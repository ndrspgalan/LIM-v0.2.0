package domain.movement;

import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ItemPropertyId;
import java.util.Objects;

/** restricciones biomecánicas procedentes de la armadura equipada. */
public final class ArmorMobilityRestrictionPolicy {
    public boolean allows(EquipmentState equipment, LocomotionMode mode) {
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Objects.requireNonNull(mode, "El modo de locomoción no puede ser nulo.");
        if (!equipment.hasArmorProperty(ItemPropertyId.BIOMECHANICAL_RIGIDITY)) return true;
        return mode == LocomotionMode.WALKING
                || mode == LocomotionMode.TROTTING
                || mode == LocomotionMode.RUNNING;
    }

    public boolean allowsSwimming(EquipmentState equipment) {
        Objects.requireNonNull(equipment);
        return !equipment.hasArmorProperty(ItemPropertyId.BIOMECHANICAL_RIGIDITY);
    }

    public boolean allowsSliding(EquipmentState equipment) {
        Objects.requireNonNull(equipment);
        return !equipment.hasArmorProperty(ItemPropertyId.BIOMECHANICAL_RIGIDITY);
    }
}
