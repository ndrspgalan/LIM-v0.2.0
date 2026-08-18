package domain.character.progression;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record CharacterClassDefinition(
        CharacterClass characterClass,
        Set<Attribute> extendedAttributes,
        int extendedCap,
        int requiredClairvoyance,
        Set<Gender> affinities,
        boolean replacesGenderMastery,
        boolean unlocksTransmutation
) {
    public CharacterClassDefinition {
        Objects.requireNonNull(characterClass, "La clase no puede ser nula.");
        extendedAttributes = Set.copyOf(Objects.requireNonNull(extendedAttributes, "Los atributos extendidos no pueden ser nulos."));
        affinities = Set.copyOf(Objects.requireNonNull(affinities, "Las afinidades no pueden ser nulas."));
        if (extendedCap != 75 && extendedCap != 120) {
            throw new IllegalArgumentException("El límite de clase debe ser 75 o 120.");
        }
        if (requiredClairvoyance < 1 || requiredClairvoyance > CharacterSheet.structuralMaximum(Attribute.CLARIVIDENCIA)) {
            throw new IllegalArgumentException("El requisito de Clarividencia debe estar entre 1 y 75.");
        }
    }

    public boolean isAvailableFor(Gender gender) {
        return affinities.contains(Objects.requireNonNull(gender, "El género no puede ser nulo."));
    }

    public boolean requirementsSatisfied(CharacterSheet sheet) {
        return sheet.valueOf(Attribute.CLARIVIDENCIA) >= requiredClairvoyance;
    }

    public boolean extendsAttribute(Attribute attribute) {
        return extendedAttributes.contains(attribute);
    }

    public static Map<CharacterClass, CharacterClassDefinition> canonicalDefinitions() {
        EnumMap<CharacterClass, CharacterClassDefinition> definitions = new EnumMap<>(CharacterClass.class);
        definitions.put(CharacterClass.LUCHADOR, definition(CharacterClass.LUCHADOR, 75, 1,
                EnumSet.of(Gender.HOMBRE), Attribute.FUERZA));
        definitions.put(CharacterClass.INTELECTUAL, definition(CharacterClass.INTELECTUAL, 75, 1,
                EnumSet.of(Gender.HOMBRE), Attribute.INTELIGENCIA));
        definitions.put(CharacterClass.INDOMITO, definition(CharacterClass.INDOMITO, 75, 1,
                EnumSet.of(Gender.HOMBRE), Attribute.AGUANTE));
        definitions.put(CharacterClass.ESPECIALISTA, definition(CharacterClass.ESPECIALISTA, 75, 1,
                EnumSet.of(Gender.MUJER), Attribute.DESTREZA));
        definitions.put(CharacterClass.APODERADO, definition(CharacterClass.APODERADO, 75, 1,
                EnumSet.of(Gender.MUJER), Attribute.FE));
        definitions.put(CharacterClass.HERALDO, definition(CharacterClass.HERALDO, 75, 1,
                EnumSet.of(Gender.MUJER), Attribute.CARISMA));
        definitions.put(CharacterClass.MAESTRO, new CharacterClassDefinition(
                CharacterClass.MAESTRO,
                EnumSet.of(Attribute.CLARIVIDENCIA),
                75,
                1,
                EnumSet.of(Gender.HOMBRE, Gender.MUJER),
                false,
                true
        ));
        return Map.copyOf(definitions);
    }

    private static CharacterClassDefinition definition(
            CharacterClass characterClass,
            int cap,
            int clairvoyance,
            Set<Gender> affinities,
            Attribute... attributes
    ) {
        EnumSet<Attribute> extended = EnumSet.noneOf(Attribute.class);
        for (Attribute attribute : attributes) {
            extended.add(attribute);
        }
        return new CharacterClassDefinition(
                characterClass, extended, cap, clairvoyance, affinities, false, false
        );
    }
}
