package domain.combat.ai.declarative;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.Objects;

/** Hechos autoritativos del actor que LIM entrega al razonador; no contiene valoración táctica. */
public record CombatActorDecisionState(
        String actorId,
        Gender gender,
        CharacterSheet sheet,
        double heightMeters,
        double currentStamina,
        double maximumStamina,
        CombatActorOriginFact origin
) {
    public CombatActorDecisionState {
        if (actorId == null || actorId.isBlank()) throw new IllegalArgumentException("actorId obligatorio.");
        Objects.requireNonNull(gender); Objects.requireNonNull(sheet); Objects.requireNonNull(origin);
        if (!Double.isFinite(heightMeters) || heightMeters <= 0) throw new IllegalArgumentException("Altura inválida.");
        if (!Double.isFinite(maximumStamina) || maximumStamina <= 0 || !Double.isFinite(currentStamina)
                || currentStamina < 0 || currentStamina > maximumStamina) throw new IllegalArgumentException("PA inválidos.");
    }
    public CombatActorDecisionState(String actorId, Gender gender, CharacterSheet sheet, double heightMeters, double currentStamina, double maximumStamina) {
        this(actorId,gender,sheet,heightMeters,currentStamina,maximumStamina,CombatActorOriginFact.unspecified());
    }
}
