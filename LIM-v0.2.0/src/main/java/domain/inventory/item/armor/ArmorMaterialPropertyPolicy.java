package domain.inventory.item.armor;

import domain.inventory.item.ItemPropertyId;
import java.util.EnumSet;
import java.util.Set;

/** propiedades intrínsecas que nacen del material, no del conjunto que lo utiliza. */
public final class ArmorMaterialPropertyPolicy {
    private ArmorMaterialPropertyPolicy() {}

    public static Set<ItemPropertyId> intrinsicProperties(ArmorMaterial material) {
        return switch (material) {
            case PAPER -> EnumSet.of(ItemPropertyId.FRAGILE, ItemPropertyId.FLAMMABLE,
                    ItemPropertyId.ANTI_CORROSIVE, ItemPropertyId.INSULATING);
            case BRONZE -> EnumSet.of(ItemPropertyId.ANTI_CORROSIVE);
            case WOOD, EBONY_WOOD, MINERALIZED_EBONY -> EnumSet.of(ItemPropertyId.FLAMMABLE);
            case ELECTROMECHANICAL_COMPOSITE -> EnumSet.of(ItemPropertyId.INTRICATE_MANUFACTURE);
            default -> EnumSet.noneOf(ItemPropertyId.class);
        };
    }

    public static boolean has(ArmorMaterial material, ItemPropertyId property) {
        return intrinsicProperties(material).contains(property);
    }
}
