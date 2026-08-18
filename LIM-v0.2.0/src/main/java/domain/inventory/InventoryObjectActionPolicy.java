package domain.inventory;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import java.util.*;

/** menú contextual canónico de cualquier objeto perteneciente al inventario. */
public final class InventoryObjectActionPolicy {
    private InventoryObjectActionPolicy() {}

    public static InventoryObjectActionAvailability evaluate(InventoryEntry item, InventoryState inventory) {
        return evaluate(item,inventory,inventory.armorLayout());
    }

    public static InventoryObjectActionAvailability evaluate(InventoryEntry item, InventoryState inventory, domain.inventory.equipment.ArmorEquipmentLayout armorLayout) {
        Objects.requireNonNull(item); Objects.requireNonNull(inventory); Objects.requireNonNull(armorLayout);
        boolean stored = storedCompartment(item, inventory.logistics()).isPresent();
        boolean equipped = Arrays.stream(EquipmentSlot.values()).anyMatch(s -> inventory.equipment().itemAt(s).filter(i -> i == item).isPresent())
                || armorLayout.layers().stream().anyMatch(l->l.piece()==item);
        boolean quick = inventory.quickAccessBar().slots().stream().flatMap(Optional::stream).anyMatch(i -> i == item);
        boolean owned = stored || equipped || quick;
        List<EquipmentSlot> equipmentSlots = stored && !(item instanceof domain.inventory.item.armor.ArmorPiece) ? eligibleEquipmentSlots(item, inventory.equipment()) : List.of();
        List<domain.inventory.equipment.ArmorEquipDestination> armorDestinations = stored && item instanceof domain.inventory.item.armor.ArmorPiece armor
                ? eligibleArmorDestinations(armor,armorLayout) : List.of();
        List<Integer> quickSlots = stored ? eligibleQuickSlots(item, inventory) : List.of();
        boolean usable = owned && QuickAccessUsePolicy.requiresQuickAccess(item) && QuickAccessUsePolicy.authorize(item, inventory).allowed();
        EnumMap<InventoryObjectAction,Boolean> allowed = new EnumMap<>(InventoryObjectAction.class);
        allowed.put(InventoryObjectAction.DROP, owned);
        allowed.put(InventoryObjectAction.EQUIP_ACTIVE, !equipmentSlots.isEmpty() || !armorDestinations.isEmpty());
        allowed.put(InventoryObjectAction.EQUIP_QUICK_ACCESS, !quickSlots.isEmpty());
        allowed.put(InventoryObjectAction.UNEQUIP, equipped || quick);
        allowed.put(InventoryObjectAction.USE, usable); allowed.put(InventoryObjectAction.INSPECT, owned);
        allowed.put(InventoryObjectAction.ROTATE_90, stored && item.canonicalFootprint().hasGridDimensions());
        return new InventoryObjectActionAvailability(allowed,equipmentSlots,armorDestinations,quickSlots);
    }

    private static List<domain.inventory.equipment.ArmorEquipDestination> eligibleArmorDestinations(domain.inventory.item.armor.ArmorPiece armor, domain.inventory.equipment.ArmorEquipmentLayout layout){
        ArrayList<domain.inventory.equipment.ArmorEquipDestination> out=new ArrayList<>();
        for(EquipmentSlot slot:List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.BRACERS,EquipmentSlot.LEGGINGS,EquipmentSlot.FEET)){
            for(var position:domain.inventory.item.armor.ArmorLayerPosition.values()){
                try{ layout.equip(slot,position,armor); out.add(new domain.inventory.equipment.ArmorEquipDestination(slot,position,destinationLabel(slot,position,armor))); }
                catch(IllegalArgumentException ignored){}
            }
        }
        return List.copyOf(out);
    }
    private static String destinationLabel(EquipmentSlot slot,domain.inventory.item.armor.ArmorLayerPosition p,domain.inventory.item.armor.ArmorPiece armor){
        if(slot==EquipmentSlot.HEAD) return "HEAD / "+armor.headLayer().orElse(domain.inventory.item.armor.HeadLayer.TACTICAL);
        if(slot==EquipmentSlot.BRACERS) return "BRACERS";
        String sub="";
        if(slot==EquipmentSlot.CHEST && p==domain.inventory.item.armor.ArmorLayerPosition.INNER) sub=" / "+armor.innerChestLayer().orElse(domain.inventory.item.armor.InnerChestLayer.BASE);
        if(slot==EquipmentSlot.LEGGINGS && p==domain.inventory.item.armor.ArmorLayerPosition.INNER) sub=" / "+armor.innerLeggingsLayer().orElse(domain.inventory.item.armor.InnerLeggingsLayer.BASE);
        if(slot==EquipmentSlot.FEET) sub=" / "+armor.feetLayer().orElse(domain.inventory.item.armor.FeetLayer.OUTER);
        return slot.label()+" / "+p+sub;
    }
    public static Optional<InventoryCompartmentType> storedCompartment(InventoryEntry item, domain.inventory.logistics.LogisticsState logistics) {
        for (InventoryCompartmentType type : InventoryCompartmentType.values()) {
            InventoryCompartment c = logistics.compartment(type);
            if (c.available() && c.entries().stream().anyMatch(e -> e == item)) return Optional.of(type);
        }
        return Optional.empty();
    }


    private static List<Integer> eligibleQuickSlots(InventoryEntry item, InventoryState inventory) {
        Optional<InventoryCompartmentType> source = storedCompartment(item, inventory.logistics());
        if (source.isEmpty()) return List.of();
        ArrayList<Integer> result = new ArrayList<>();
        for (int slot=1; slot<=QuickAccessBar.SLOT_COUNT; slot++) {
            if (!QuickAccessPolicy.isSlotAvailable(slot, inventory.equipment(), inventory.logistics())) continue;
            if (QuickAccessPolicy.sourceCompartment(slot) != source.get()) continue;
            Optional<InventoryEntry> assigned = inventory.quickAccessBar().slots().get(slot-1);
            if (assigned.isEmpty() || assigned.filter(i -> i == item).isPresent()) result.add(slot);
        }
        return List.copyOf(result);
    }

    private static List<EquipmentSlot> eligibleEquipmentSlots(InventoryEntry item, EquipmentState equipment) {
        ArrayList<EquipmentSlot> result = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (equipment.itemAt(slot).isPresent()) continue; //  no hace swaps implícitos.
            EnumMap<EquipmentSlot,InventoryEntry> proposed = new EnumMap<>(EquipmentSlot.class);
            for (EquipmentSlot existing : EquipmentSlot.values()) equipment.itemAt(existing).ifPresent(i -> proposed.put(existing,i));
            proposed.put(slot,item);
            try { new EquipmentState(proposed); result.add(slot); } catch (IllegalArgumentException ignored) { }
        }
        return List.copyOf(result);
    }
}
