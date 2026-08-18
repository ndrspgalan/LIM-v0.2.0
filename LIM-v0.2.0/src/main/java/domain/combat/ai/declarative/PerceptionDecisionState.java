package domain.combat.ai.declarative;

import domain.combat.ai.execution.CombatAction;
import domain.combat.ai.memory.CombatOutcomeMemory;
import domain.combat.ai.perception.CombatPerceptionSnapshot;
import java.util.Objects;
import java.util.Optional;

/** Entrada actor-específica . La memoria sigue siendo propiedad de LIM. */
public record PerceptionDecisionState(String targetActorId, CombatPerceptionSnapshot current,
                                      CombatOutcomeMemory memory, Optional<CombatAction> visibleCurrentAction) {
    public PerceptionDecisionState {
        targetActorId=Objects.requireNonNull(targetActorId); current=Objects.requireNonNull(current);
        memory=Objects.requireNonNull(memory); visibleCurrentAction=Objects.requireNonNull(visibleCurrentAction);
    }
}
