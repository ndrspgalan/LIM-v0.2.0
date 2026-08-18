package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.character.progression.AttributeActorCapPolicy;
import domain.character.progression.AttributeActorScope;

import java.util.Objects;
import java.util.Set;

/** Perfil demográfico canónico: el nivel siempre deriva de los nueve atributos. */
public record CanonicalSubprofessionProfile(
        Subprofession subprofession,
        CharacterClass characterClass,
        Set<Gender> genders,
        CharacterSheet attributes,
        String narrativeRationale
) {
    public CanonicalSubprofessionProfile {
        Objects.requireNonNull(subprofession, "La subprofesión no puede ser nula.");
        Objects.requireNonNull(characterClass, "La clase no puede ser nula.");
        genders = Set.copyOf(Objects.requireNonNull(genders, "Los sexos compatibles no pueden ser nulos."));
        if (genders.isEmpty()) throw new IllegalArgumentException("El perfil necesita al menos un sexo compatible.");
        Objects.requireNonNull(attributes, "Los atributos no pueden ser nulos.");
        AttributeActorCapPolicy.requireValid(AttributeActorScope.PROCEDURAL_SUBPROFESSION_NPC, attributes);
        narrativeRationale = Objects.requireNonNull(narrativeRationale, "La justificación narrativa no puede ser nula.").strip();
        if (narrativeRationale.isBlank()) throw new IllegalArgumentException("La justificación narrativa no puede estar vacía.");
    }

    public int canonicalLevel(){ return attributes.totalAttributeLevel(); }
}
