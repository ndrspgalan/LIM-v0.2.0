package domain.inventory.equipment;

import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;

import java.util.Objects;

/**
 * : la clase LIGHT/MEDIUM/HEAVY no decide electricidad.
 * Se resuelve la ruta suelo -> suela -> pie por construcción/materiales.
 */
public final class FeetElectricalContactPolicy {
    private FeetElectricalContactPolicy() {}

    public static FeetElectricalContact resolve(EquipmentState equipment) {
        Objects.requireNonNull(equipment);

        var feet = equipment.armorAt(EquipmentSlot.FEET);
        if (feet.isEmpty()) {
            var integrated = equipment.equippedArmor().stream()
                    .filter(a -> a.hasActiveProperty(ItemPropertyId.INTEGRATED_FOOTWEAR))
                    .findFirst();
            if (integrated.isEmpty()) return FeetElectricalContact.EARTH_COUPLED;

            ArmorPiece piece = integrated.get();
            if (piece.hasActiveProperty(ItemPropertyId.INSULATING)
                    || piece.hasActiveProperty(ItemPropertyId.INTEGRAL_SEAL)) {
                return FeetElectricalContact.INTEGRATED_ISOLATED;
            }
            boolean conductor = piece.hasActiveProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR)
                    || piece.materials().contains(ArmorMaterial.STEEL)
                    || piece.materials().contains(ArmorMaterial.BRONZE)
                    || piece.materials().contains(ArmorMaterial.TUNGSTEN)
                    || piece.materials().contains(ArmorMaterial.TUNGSTEN_PLATES_2_5_MM);
            return conductor ? FeetElectricalContact.INTEGRATED_CONDUCTIVE
                    : FeetElectricalContact.INTEGRATED_ISOLATED;
        }

        ArmorPiece piece = feet.get();

        // Una propiedad tecnológica explícita prevalece sobre la composición genérica.
        if (piece.hasActiveProperty(ItemPropertyId.INSULATING)) return FeetElectricalContact.INSULATED;
        if (piece.hasActiveProperty(ItemPropertyId.GROUNDING)) return FeetElectricalContact.EARTH_COUPLED;

        // Una suela declarada de caucho vulcanizado interrumpe la ruta al terreno,
        // aunque existan elementos metálicos localizados como una puntera.
        if (piece.materials().contains(ArmorMaterial.VULCANIZED_RUBBER)) {
            return FeetElectricalContact.INSULATED;
        }

        // Textil/cuero sin suela dieléctrica dedicada conserva acoplamiento físico al suelo.
        if (piece.materials().contains(ArmorMaterial.CLOTH)
                || piece.materials().contains(ArmorMaterial.HARDENED_LEATHER)
                || piece.materials().contains(ArmorMaterial.DIELECTRIC_CLOTH)) {
            return FeetElectricalContact.EARTH_COUPLED;
        }

        return FeetElectricalContact.INSULATED;
    }
}
