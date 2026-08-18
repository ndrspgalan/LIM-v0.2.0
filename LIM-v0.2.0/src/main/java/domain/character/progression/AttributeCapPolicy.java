package domain.character.progression;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Map;
import java.util.Objects;

public final class AttributeCapPolicy {
    private final GenderSoftcapProfile softcaps;
    private final Map<CharacterClass, CharacterClassDefinition> classDefinitions;

    public AttributeCapPolicy(
            GenderSoftcapProfile softcaps,
            Map<CharacterClass, CharacterClassDefinition> classDefinitions
    ) {
        this.softcaps = Objects.requireNonNull(softcaps, "Los softcaps no pueden ser nulos.");
        this.classDefinitions = Map.copyOf(Objects.requireNonNull(classDefinitions, "Las clases no pueden ser nulas."));
    }

    public int maximumFor(
            Gender gender,
            CharacterClass characterClass,
            CharacterSheet sheet,
            Attribute attribute
    ) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        CharacterClassDefinition definition = Objects.requireNonNull(
                classDefinitions.get(characterClass), "No existe definición para " + characterClass + "."
        );

        if (!definition.isAvailableFor(gender)) {
            throw new IllegalStateException(
                    characterClass.label() + " no está disponible para el género " + gender.label() + "."
            );
        }

        if (attribute == Attribute.VITALIDAD || attribute == Attribute.ADAPTABILIDAD) {
            return AttributeActorCapPolicy.absoluteMaximum(AttributeActorScope.KENAN, attribute);
        }
        int ordinaryCap = softcaps.ordinaryCap(gender, attribute);
        if (definition.requirementsSatisfied(sheet) && definition.extendsAttribute(attribute)) {
            return Math.max(ordinaryCap, definition.extendedCap());
        }
        return ordinaryCap;
    }

    public int maximumFor(AttributeActorScope actorScope, Gender gender, CharacterClass characterClass, CharacterSheet sheet, Attribute attribute) {
        Objects.requireNonNull(actorScope, "El tipo de actor no puede ser nulo.");
        int actorAbsolute=AttributeActorCapPolicy.absoluteMaximum(actorScope,attribute);
        if(attribute==Attribute.VITALIDAD || attribute==Attribute.ADAPTABILIDAD) return actorAbsolute;
        return Math.min(actorAbsolute, maximumFor(gender,characterClass,sheet,attribute));
    }

    public boolean canIncrease(
            Gender gender,
            CharacterClass characterClass,
            CharacterSheet sheet,
            Attribute attribute
    ) {
        return sheet.valueOf(attribute) < maximumFor(gender, characterClass, sheet, attribute);
    }
}
