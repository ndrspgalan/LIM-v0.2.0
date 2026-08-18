package domain.inventory.equipment;

import domain.inventory.InventoryEntry;
import domain.inventory.item.armor.ArmorPiece;
import domain.combat.ArmorLayerOrderPolicy;
import java.util.*;

/**
 * : ArmorEquipmentLayout es la autoridad de las ranuras de armadura estratificada.
 * El equipamiento activo consulta/proyecta esas mismas capas en vez de mantener una segunda
 * lista independiente capaz de divergir.
 */
public final class ArmorActiveEquipmentPolicy {
    private static final Set<EquipmentSlot> ARMOR_SLOTS=EnumSet.of(
            EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.BRACERS,EquipmentSlot.LEGGINGS,EquipmentSlot.FEET);
    private ArmorActiveEquipmentPolicy(){}

    public static boolean isArmorSlot(EquipmentSlot slot){ return ARMOR_SLOTS.contains(Objects.requireNonNull(slot)); }

    /** Todas las piezas activas de la ranura en orden exterior -> interior. */
    public static List<ArmorPiece> activeArmor(ArmorEquipmentLayout layout, EquipmentSlot slot){
        Objects.requireNonNull(layout); Objects.requireNonNull(slot);
        if(!isArmorSlot(slot)) return List.of();
        return layout.layers().stream().filter(l->l.slot()==slot).sorted(ArmorLayerOrderPolicy.outerToInner())
                .map(EquippedArmorLayer::piece).toList();
    }

    /** Vista canónica de equipamiento: armas/abalorio/runa desde EquipmentState y armadura desde layout. */
    public static Map<EquipmentSlot,List<InventoryEntry>> synchronizedView(EquipmentState equipment, ArmorEquipmentLayout layout){
        Objects.requireNonNull(equipment); Objects.requireNonNull(layout);
        EnumMap<EquipmentSlot,List<InventoryEntry>> result=new EnumMap<>(EquipmentSlot.class);
        for(EquipmentSlot slot:EquipmentSlot.values()){
            if(isArmorSlot(slot)) result.put(slot,List.copyOf(activeArmor(layout,slot)));
            else result.put(slot,equipment.itemAt(slot).<List<InventoryEntry>>map(List::of).orElseGet(List::of));
        }
        return Collections.unmodifiableMap(result);
    }

    /** Detecta el estado obsoleto peligroso: una armadura declarada en EquipmentState que no está en el layout. */
    public static void requireSynchronized(EquipmentState equipment, ArmorEquipmentLayout layout){
        Map<EquipmentSlot,List<InventoryEntry>> view=synchronizedView(equipment,layout);
        for(EquipmentSlot slot:ARMOR_SLOTS){
            var declaredArmor=equipment.armorAt(slot);
            if(declaredArmor.isPresent() && view.get(slot).stream().noneMatch(i->i==declaredArmor.get()))
                throw new IllegalStateException("La ranura "+slot+" no está sincronizada con ArmorEquipmentLayout.");
        }
    }
}
