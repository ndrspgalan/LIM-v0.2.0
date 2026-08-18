package domain.combat.ai.declarative;

import domain.combat.ai.encounter.EncounterActorState;
import domain.combat.ai.execution.CombatAction;
import domain.social.RelationshipType;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/** Vista actor-específica de otro participante. Sólo contiene hechos observados/recordados legítimos. */
public record PerceivedActorFact(
        String actorId,
        RelationshipType relationship,
        EncounterActorState lifeState,
        boolean perceptibleNow,
        boolean mounted,
        boolean forcedTargetByEffect,
        OptionalDouble observedDistanceMeters,
        OptionalDouble observedMissingHealth,
        OptionalDouble observedStaminaDepletionIntensity,
        OptionalDouble observedStaggerSeconds,
        Optional<CombatAction> visibleCurrentAction,
        boolean visibleActionInterruptible,
        PerceivedCombatState perception
) {
    public PerceivedActorFact {
        actorId=Objects.requireNonNull(actorId); Objects.requireNonNull(relationship); Objects.requireNonNull(lifeState);
        observedDistanceMeters=Objects.requireNonNull(observedDistanceMeters); observedMissingHealth=Objects.requireNonNull(observedMissingHealth);
        observedStaminaDepletionIntensity=Objects.requireNonNull(observedStaminaDepletionIntensity); observedStaggerSeconds=Objects.requireNonNull(observedStaggerSeconds);
        visibleCurrentAction=Objects.requireNonNull(visibleCurrentAction); Objects.requireNonNull(perception);
    }
}
