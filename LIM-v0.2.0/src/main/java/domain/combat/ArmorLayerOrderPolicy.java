package domain.combat;

import domain.inventory.equipment.EquippedArmorLayer;
import domain.inventory.item.armor.ArmorLayerPosition;
import domain.inventory.item.armor.HeadLayer;

import java.util.Comparator;

/** orden físico de impacto, de la superficie exterior hacia el cuerpo. */
public final class ArmorLayerOrderPolicy {
    private ArmorLayerOrderPolicy() {}

    public static Comparator<EquippedArmorLayer> outerToInner() {
        return Comparator.comparingInt(ArmorLayerOrderPolicy::depth).reversed();
    }

    private static int depth(EquippedArmorLayer layer) {
        if (layer.slot() == domain.inventory.equipment.EquipmentSlot.HEAD) {
            HeadLayer head = layer.piece().headLayer().orElse(HeadLayer.TACTICAL);
            return switch (head) {
                case UPPER_ACCESSORY -> 30;
                case TACTICAL -> 20;
                case LOWER_ACCESSORY -> 10;
            };
        }
        return switch (layer.position()) {
            case OUTER -> 30;
            case MIDDLE, UNSPECIFIED -> 20;
            case INNER -> 10;
        };
    }
}
