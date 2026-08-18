package domain.inventory.equipment;

import domain.inventory.item.armor.ArmorLayerPosition;
import domain.inventory.item.armor.ArmorPiece;

import java.util.Objects;

/** Una pieza colocada físicamente en una ranura y, cuando procede, en una capa concreta. */
public record EquippedArmorLayer(EquipmentSlot slot, ArmorLayerPosition position, ArmorPiece piece) {
    public EquippedArmorLayer {
        Objects.requireNonNull(slot, "La ranura no puede ser nula.");
        Objects.requireNonNull(position, "La posición de capa no puede ser nula.");
        Objects.requireNonNull(piece, "La pieza no puede ser nula.");
    }
}
