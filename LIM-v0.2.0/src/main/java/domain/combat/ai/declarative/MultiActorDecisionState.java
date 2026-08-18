package domain.combat.ai.declarative;

import domain.combat.ai.encounter.AffectedActorConsequence;
import domain.combat.ai.encounter.CombatIntentBroadcast;
import java.util.List;
import java.util.Objects;

/** Estado multi-actor actor-específico. No contiene blanco seleccionado ni valoración táctica. */
public record MultiActorDecisionState(
        List<MultiActorTargetState> actors,
        List<CombatIntentBroadcast> intentEvents,
        List<AffectedActorConsequence> knownAreaConsequences,
        List<KnownActorRelationshipFact> knownRelationships,
        boolean observerCanSeeIntent,
        boolean observerCanHearIntent,
        double combatTimeSeconds,
        double maximumIntentAgeSeconds
) {
    public MultiActorDecisionState {
        actors=List.copyOf(Objects.requireNonNull(actors));
        intentEvents=List.copyOf(Objects.requireNonNull(intentEvents));
        knownAreaConsequences=List.copyOf(Objects.requireNonNull(knownAreaConsequences));
        knownRelationships=List.copyOf(Objects.requireNonNull(knownRelationships));
        if (!Double.isFinite(combatTimeSeconds)||combatTimeSeconds<0||!Double.isFinite(maximumIntentAgeSeconds)||maximumIntentAgeSeconds<0) {
            throw new IllegalArgumentException("Ventana temporal multi-actor inválida.");
        }
    }
    public static MultiActorDecisionState empty(){return new MultiActorDecisionState(List.of(),List.of(),List.of(),List.of(),false,false,0,0);}
}
