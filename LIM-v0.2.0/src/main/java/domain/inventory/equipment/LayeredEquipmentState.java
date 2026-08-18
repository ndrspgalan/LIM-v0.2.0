package domain.inventory.equipment;

import domain.inventory.InventoryEntry;
import domain.inventory.item.armor.ArmorLayerPosition;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.logistics.LogisticsState;
import java.util.*;

/**
 * : estado canónico sincronizado. Las ranuras no estratificadas viven en EquipmentState;
 * las ranuras de armadura son una proyección directa de ArmorEquipmentLayout.
 */
public record LayeredEquipmentState(EquipmentState activeEquipment, ArmorEquipmentLayout armorLayout) {
    public LayeredEquipmentState {
        Objects.requireNonNull(activeEquipment); Objects.requireNonNull(armorLayout);
        ArmorActiveEquipmentPolicy.requireSynchronized(activeEquipment, armorLayout);
    }
    public static LayeredEquipmentState empty(){ return new LayeredEquipmentState(EquipmentState.empty(),ArmorEquipmentLayout.empty()); }
    public List<InventoryEntry> activeItems(EquipmentSlot slot){
        if(ArmorActiveEquipmentPolicy.isArmorSlot(slot)) return List.copyOf(ArmorActiveEquipmentPolicy.activeArmor(armorLayout,slot));
        return activeEquipment.itemAt(slot).<List<InventoryEntry>>map(List::of).orElseGet(List::of);
    }
    public LayeredEquipmentState equipArmor(EquipmentSlot slot, ArmorLayerPosition position, ArmorPiece piece){
        if(!ArmorActiveEquipmentPolicy.isArmorSlot(slot)) throw new IllegalArgumentException("La ranura no es de armadura.");
        return new LayeredEquipmentState(activeEquipment,armorLayout.equip(slot,position,piece));
    }
    public LayeredEquipmentState unequipArmor(ArmorPiece piece){ return new LayeredEquipmentState(activeEquipment,armorLayout.unequip(piece)); }
    public LogisticsState synchronizeGarmentStorage(LogisticsState logistics){ return logistics.synchronizeGarmentStorage(armorLayout); }
}
