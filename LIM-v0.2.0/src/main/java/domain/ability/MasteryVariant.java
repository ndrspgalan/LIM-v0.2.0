package domain.ability;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Objects;

public record MasteryVariant(
        String name,
        boolean refined,
        MasteryType type,
        Attribute scalingAttribute,
        int scalingStart,
        int accessibilityThreshold,
        String narrativeDescription,
        String mechanicalDescription
) {
    public MasteryVariant {
        name = requireText(name);
        Objects.requireNonNull(type, "El tipo no puede ser nulo.");
        Objects.requireNonNull(scalingAttribute, "El atributo no puede ser nulo.");
        narrativeDescription = requireText(narrativeDescription);
        mechanicalDescription = requireText(mechanicalDescription);
        if (scalingStart < 1 || accessibilityThreshold < scalingStart || accessibilityThreshold > 120) {
            throw new IllegalArgumentException("Intervalo de progresión de maestría inválido.");
        }
    }

    public boolean isVisibleTo(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return true;
    }

    public boolean isAccessibleTo(CharacterSheet sheet) {
        int requirement = refined ? accessibilityThreshold : scalingStart;
        return isVisibleTo(sheet) && sheet.valueOf(scalingAttribute) >= requirement;
    }

    private static String requireText(String value) {
        Objects.requireNonNull(value, "El texto no puede ser nulo.");
        String normalized = value.trim();
        if (normalized.isEmpty()) throw new IllegalArgumentException("El texto no puede estar vacío.");
        return normalized;
    }
}
