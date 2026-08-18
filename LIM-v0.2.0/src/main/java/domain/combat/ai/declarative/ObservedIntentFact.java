package domain.combat.ai.declarative;

import domain.combat.ai.execution.CombatAction;
import java.util.Objects;

/** Intención ajena realmente perceptible; no implica coordinación ni comunicación telepática. */
public record ObservedIntentFact(
        String actorId,
        String targetActorId,
        CombatAction action,
        boolean perceivedVisually,
        boolean perceivedAudibly,
        double ageSeconds
) {
    public ObservedIntentFact {
        actorId=Objects.requireNonNull(actorId); targetActorId=targetActorId==null?"":targetActorId; Objects.requireNonNull(action);
        if(!Double.isFinite(ageSeconds)||ageSeconds<0) throw new IllegalArgumentException("Edad de intención inválida.");
    }
}
