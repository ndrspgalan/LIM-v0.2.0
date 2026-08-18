package domain.inventory.logistics;

import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.armor.ArmorInventoryCategory;
import domain.inventory.item.armor.ArmorPiece;
import java.util.*;

/** traduce las prendas actualmente vestidas a módulos CHEST/LEGGINGS. */
public final class GarmentStoragePolicy {
    private GarmentStoragePolicy() {}

    public static List<InventoryStorageModule> modulesFor(ArmorEquipmentLayout layout, InventoryCompartmentType type) {
        Objects.requireNonNull(layout); Objects.requireNonNull(type);
        if (type != InventoryCompartmentType.CHEST_STORAGE && type != InventoryCompartmentType.LEGGINGS_STORAGE)
            throw new IllegalArgumentException("Sólo CHEST_STORAGE y LEGGINGS_STORAGE derivan de prendas.");
        EquipmentSlot target = type == InventoryCompartmentType.CHEST_STORAGE ? EquipmentSlot.CHEST : EquipmentSlot.LEGGINGS;
        ArrayList<InventoryStorageModule> result = new ArrayList<>();
        for (ArmorPiece piece : layout.piecesAt(target)) addProfileModules(piece,type,result);
        // Un traje integral se equipa en CHEST, pero sus bolsillos canónicos pertenecen al proveedor CHEST.
        if (type == InventoryCompartmentType.CHEST_STORAGE) {
            layout.piecesAt(EquipmentSlot.CHEST).stream()
                    .filter(p -> p.inventoryCategory().orElse(null) == ArmorInventoryCategory.INTEGRAL_SUIT)
                    .forEach(p -> {}); // ya incluido por piecesAt(CHEST); comentario explicita el contrato.
        }
        return List.copyOf(result);
    }


    public static List<InventoryStorageModule> modulesFor(domain.inventory.equipment.EquipmentState equipment, InventoryCompartmentType type) {
        Objects.requireNonNull(equipment); Objects.requireNonNull(type);
        if (type != InventoryCompartmentType.CHEST_STORAGE && type != InventoryCompartmentType.LEGGINGS_STORAGE)
            throw new IllegalArgumentException("Sólo CHEST_STORAGE y LEGGINGS_STORAGE derivan de prendas.");
        EquipmentSlot target = type == InventoryCompartmentType.CHEST_STORAGE ? EquipmentSlot.CHEST : EquipmentSlot.LEGGINGS;
        ArrayList<InventoryStorageModule> result = new ArrayList<>();
        equipment.armorAt(target).ifPresent(piece -> addProfileModules(piece,type,result));
        return List.copyOf(result);
    }

    private static void addProfileModules(ArmorPiece piece, InventoryCompartmentType type, List<InventoryStorageModule> out) {
        GarmentStorageCatalog.profileFor(piece).ifPresent(profile -> {
            boolean chest = type == InventoryCompartmentType.CHEST_STORAGE
                    && (profile.category() == ArmorInventoryCategory.CHEST || profile.category() == ArmorInventoryCategory.INTEGRAL_SUIT);
            boolean legs = type == InventoryCompartmentType.LEGGINGS_STORAGE && profile.category() == ArmorInventoryCategory.LEGGINGS;
            if (chest || legs) {
                int i=1;
                for (InventoryStorageModule module : profile.modules()) {
                    out.add(new InventoryStorageModule(piece.name()+" · "+i+" · "+module.label(), module.physicalDimensions(), module.grid()));
                    i++;
                }
            }
        });
    }
}
