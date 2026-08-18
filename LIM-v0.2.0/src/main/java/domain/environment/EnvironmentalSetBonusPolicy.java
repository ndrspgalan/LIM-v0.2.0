package domain.environment;

import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorForm;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;

import java.util.List;
import java.util.Objects;

/** Bonificaciones físicas derivadas de la composición material y del abrigo exterior equipado. */
public final class EnvironmentalSetBonusPolicy {
    public static final String CLOTH_SET_PROPERTY = "INMUNIDAD | Frío Escarchante";

    /** La propiedad física de la tela es siempre visible y no depende de FE. */
    public boolean clothSetVisible(EquipmentState equipment) {
        return hasThreeBodyPiecesOfCloth(equipment);
    }

    public boolean immuneTo(CharacterSheet sheet, EquipmentState equipment, EnvironmentalAdversity adversity) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Objects.requireNonNull(adversity, "La adversidad no puede ser nula.");
        return adversity == EnvironmentalAdversity.BITING_FROST && hasThreeBodyPiecesOfCloth(equipment);
    }

    public boolean hasThreeBodyPiecesOfCloth(EquipmentState equipment) {
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        var integral = equipment.armorAt(EquipmentSlot.CHEST)
                .filter(piece -> piece.form() == ArmorForm.INTEGRAL_SUIT)
                .filter(piece -> !piece.isDepleted())
                .filter(piece -> piece.containsMaterial(ArmorMaterial.CLOTH));
        if (integral.isPresent()) return true;

        return List.of(EquipmentSlot.CHEST, EquipmentSlot.BRACERS, EquipmentSlot.LEGGINGS).stream()
                .allMatch(slot -> equipment.armorAt(slot)
                        .filter(piece -> !piece.isDepleted())
                        .filter(piece -> piece.containsMaterial(ArmorMaterial.CLOTH))
                        .isPresent());
    }


}
