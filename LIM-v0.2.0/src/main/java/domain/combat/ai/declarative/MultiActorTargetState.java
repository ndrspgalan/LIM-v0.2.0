package domain.combat.ai.declarative;

import domain.combat.ai.encounter.CombatParticipantSnapshot;
import domain.combat.ai.observation.CombatObservation;
import java.util.Objects;

/** Entrada  de un actor potencialmente observable. CombatObservation permanece interna: nunca se exporta. */
public record MultiActorTargetState(
        CombatParticipantSnapshot participant,
        CombatObservation observation,
        PerceptionDecisionState perception
) {
    public MultiActorTargetState {
        Objects.requireNonNull(participant); Objects.requireNonNull(observation); Objects.requireNonNull(perception);
        if (!participant.actorId().equals(perception.targetActorId())) {
            throw new IllegalArgumentException("La identidad perceptiva debe coincidir con el participante.");
        }
    }
}
