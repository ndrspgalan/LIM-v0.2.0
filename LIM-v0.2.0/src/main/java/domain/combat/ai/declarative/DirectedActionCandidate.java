package domain.combat.ai.declarative;

import domain.social.RelationshipType;
import java.util.List;
import java.util.Objects;

/** Proyección target-específica de una acción ya materializada por LIM. No contiene score. */
public record DirectedActionCandidate(
        DirectedActionDomain domain,
        String candidateId,
        String targetActorId,
        RelationshipType relationship,
        double observedDistanceMeters,
        boolean distanceCompatible,
        DirectedTargetEligibility targetEligibility,
        boolean targetConscious,
        boolean canInterruptVisibleAction,
        List<String> materialRelations
) {
    public DirectedActionCandidate {
        Objects.requireNonNull(domain); candidateId=req(candidateId); targetActorId=req(targetActorId); Objects.requireNonNull(relationship); Objects.requireNonNull(targetEligibility);
        materialRelations=List.copyOf(Objects.requireNonNull(materialRelations));
        if(!Double.isFinite(observedDistanceMeters)||observedDistanceMeters<0) throw new IllegalArgumentException("Distancia dirigida inválida.");
    }
    private static String req(String s){Objects.requireNonNull(s);s=s.trim();if(s.isEmpty())throw new IllegalArgumentException("Identidad vacía.");return s;}
}
