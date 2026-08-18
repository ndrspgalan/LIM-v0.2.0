package domain.ability;

import domain.character.CharacterClass;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;

import java.util.Objects;

/**
 * Maestría pasiva común a todas las clases. Su existencia revela que VITALIDAD
 * y ADAPTABILIDAD conservan una región evolutiva humana entre 76 y 120.
 */
public record EvolutiveMastery(
        MasteryId id,
        String name,
        Attribute progressionAttribute,
        String narrativeDescription,
        String mechanicalDescription
) implements Mastery {
    public static final int EVOLUTION_THRESHOLD = 76;

    public EvolutiveMastery {
        Objects.requireNonNull(id, "El identificador no puede ser nulo.");
        Objects.requireNonNull(progressionAttribute, "El atributo evolutivo no puede ser nulo.");
        if (progressionAttribute != Attribute.VITALIDAD && progressionAttribute != Attribute.ADAPTABILIDAD) {
            throw new IllegalArgumentException("Una maestría evolutiva solo puede depender de VITALIDAD o ADAPTABILIDAD.");
        }
        name = requireText(name);
        narrativeDescription = requireText(narrativeDescription);
        mechanicalDescription = requireText(mechanicalDescription);
    }

    @Override public MasteryStructure structure() { return MasteryStructure.UNITARY; }

    /** No tiene resonancia exclusiva: es común a las siete clases. */
    @Override public CharacterClass resonanceClass() { return null; }

    @Override public MasteryCategory category() { return MasteryCategory.EVOLUTIVE; }

    public boolean isVisibleTo(CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
        return true;
    }

    public boolean isActiveFor(CharacterSheet sheet) {
        return isVisibleTo(sheet) && sheet.valueOf(progressionAttribute) >= EVOLUTION_THRESHOLD;
    }

    private static String requireText(String value) {
        Objects.requireNonNull(value, "El texto no puede ser nulo.");
        String normalized = value.trim();
        if (normalized.isEmpty()) throw new IllegalArgumentException("El texto no puede estar vacío.");
        return normalized;
    }
}
