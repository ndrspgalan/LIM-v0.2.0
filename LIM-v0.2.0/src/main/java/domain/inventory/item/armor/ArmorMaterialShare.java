package domain.inventory.item.armor;

import java.util.Objects;

/** Participación defensiva proporcional de un material dentro de una pieza. */
public record ArmorMaterialShare(ArmorMaterial material, double ratio) {
    public ArmorMaterialShare {
        Objects.requireNonNull(material, "El material no puede ser nulo.");
        if (!Double.isFinite(ratio) || ratio <= 0 || ratio > 1) {
            throw new IllegalArgumentException("La proporción debe estar en (0,1].");
        }
    }
}
