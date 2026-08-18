package domain.inventory.item;

import domain.character.sheet.Attribute;

import java.util.Objects;

public record AttributeRequirement(Attribute attribute, int minimumValue) {
    public AttributeRequirement {
        Objects.requireNonNull(attribute, "El atributo requerido no puede ser nulo.");
        if (minimumValue < 1 || minimumValue > 120) {
            throw new IllegalArgumentException("El requisito debe estar entre 1 y 120.");
        }
    }
}
