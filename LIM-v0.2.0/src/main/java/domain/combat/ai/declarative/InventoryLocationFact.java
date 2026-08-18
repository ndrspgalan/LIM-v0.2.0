package domain.combat.ai.declarative;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.logistics.InventoryCompartmentType;
import java.util.Optional;

/** Ubicación física/referencial actual de una instancia de inventario. */
public record InventoryLocationFact(
        Kind kind,
        Optional<InventoryCompartmentType> compartment,
        Optional<EquipmentSlot> equipmentSlot,
        Optional<Integer> quickAccessSlot
) {
    public InventoryLocationFact {
        compartment = compartment == null ? Optional.empty() : compartment;
        equipmentSlot = equipmentSlot == null ? Optional.empty() : equipmentSlot;
        quickAccessSlot = quickAccessSlot == null ? Optional.empty() : quickAccessSlot;
    }
    public enum Kind { STORED, EQUIPPED, QUICK_ACCESS, GROUND }
    public static InventoryLocationFact stored(InventoryCompartmentType c){ return new InventoryLocationFact(Kind.STORED,Optional.of(c),Optional.empty(),Optional.empty()); }
    public static InventoryLocationFact equipped(EquipmentSlot s){ return new InventoryLocationFact(Kind.EQUIPPED,Optional.empty(),Optional.of(s),Optional.empty()); }
    public static InventoryLocationFact quick(int slot){ return new InventoryLocationFact(Kind.QUICK_ACCESS,Optional.empty(),Optional.empty(),Optional.of(slot)); }
    public static InventoryLocationFact ground(){ return new InventoryLocationFact(Kind.GROUND,Optional.empty(),Optional.empty(),Optional.empty()); }
}
