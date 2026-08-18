package domain.ability;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.combat.HostileEncounterState;
import domain.status.VitalResourceState;

import java.util.Objects;

/** Contexto mínimo y explícito que permite ejecutar una maestría desde gameplay. */
public record MasteryRuntimeContext(
        CharacterSheet sheet,
        Gender gender,
        VitalResourceState resources,
        HostileEncounterState hostileEncounter,
        boolean stationary
) {
    public MasteryRuntimeContext {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        Objects.requireNonNull(resources, "Los recursos no pueden ser nulos.");
        Objects.requireNonNull(hostileEncounter, "El encuentro hostil no puede ser nulo.");
    }
}
