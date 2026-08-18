package domain.character.sheet;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record CharacterSheet(Map<Attribute, Integer> attributeValues) {
    public static final int MINIMUM_ATTRIBUTE_VALUE = 1;
    public static final int ORDINARY_MAXIMUM_ATTRIBUTE_VALUE = 75;
    public static final int ABSOLUTE_MAXIMUM_ATTRIBUTE_VALUE = 120;

    public CharacterSheet {
        Objects.requireNonNull(attributeValues, "Los atributos no pueden ser nulos.");
        EnumMap<Attribute, Integer> copy = new EnumMap<>(Attribute.class);

        for (Attribute attribute : Attribute.values()) {
            Integer value = attributeValues.get(attribute);
            if (value == null) {
                throw new IllegalArgumentException("Falta el valor del atributo " + attribute.label() + ".");
            }
            int maximum = structuralMaximum(attribute);
            if (value < MINIMUM_ATTRIBUTE_VALUE || value > maximum) {
                throw new IllegalArgumentException(
                        attribute.label() + " debe estar entre " + MINIMUM_ATTRIBUTE_VALUE
                                + " y " + maximum + "."
                );
            }
            copy.put(attribute, value);
        }
        attributeValues = Map.copyOf(copy);
    }

    public int valueOf(Attribute attribute) {
        Objects.requireNonNull(attribute, "El atributo no puede ser nulo.");
        return attributeValues.get(attribute);
    }

    public CharacterSheet increase(Attribute attribute) {
        int current = valueOf(attribute);
        if (current >= structuralMaximum(attribute)) {
            throw new IllegalStateException(attribute.label() + " ya ha alcanzado su máximo estructural.");
        }
        EnumMap<Attribute, Integer> updated = new EnumMap<>(attributeValues);
        updated.put(attribute, current + 1);
        return new CharacterSheet(updated);
    }

    /** Límite estructural: sólo VITALIDAD y ADAPTABILIDAD poseen espacio 76..120.
     * La autorización del actor para ocupar ese espacio se resuelve en CharacterSheetLimitPolicy. */
    public static int structuralMaximum(Attribute attribute) {
        Objects.requireNonNull(attribute);
        return attribute == Attribute.VITALIDAD || attribute == Attribute.ADAPTABILIDAD
                ? ABSOLUTE_MAXIMUM_ATTRIBUTE_VALUE : ORDINARY_MAXIMUM_ATTRIBUTE_VALUE;
    }

    public static CharacterSheet kenanCanonical() { return domain.character.KenanCanonicalProfile.initialSheet(); }

    public static CharacterSheet kiaraCanonical() {
        return of(25, 30, 12, 30, 20, 30, 3, 25, 11);
    }

    public int totalAttributeLevel() {
        return attributeValues.values().stream().mapToInt(Integer::intValue).sum();
    }

    public static CharacterSheet of(
            int vitality,
            int endurance,
            int adaptability,
            int strength,
            int dexterity,
            int intelligence,
            int faith,
            int charisma,
            int clairvoyance
    ) {
        EnumMap<Attribute, Integer> values = new EnumMap<>(Attribute.class);
        values.put(Attribute.VITALIDAD, vitality);
        values.put(Attribute.AGUANTE, endurance);
        values.put(Attribute.ADAPTABILIDAD, adaptability);
        values.put(Attribute.FUERZA, strength);
        values.put(Attribute.DESTREZA, dexterity);
        values.put(Attribute.INTELIGENCIA, intelligence);
        values.put(Attribute.FE, faith);
        values.put(Attribute.CARISMA, charisma);
        values.put(Attribute.CLARIVIDENCIA, clairvoyance);
        return new CharacterSheet(values);
    }
}
