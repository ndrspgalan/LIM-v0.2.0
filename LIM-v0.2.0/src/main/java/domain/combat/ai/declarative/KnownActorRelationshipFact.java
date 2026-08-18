package domain.combat.ai.declarative;

import domain.social.RelationshipType;
import java.util.Objects;

/** Relación social entre dos actores que el observador conoce legítimamente. */
public record KnownActorRelationshipFact(
        String sourceActorId,
        String targetActorId,
        RelationshipType relationship,
        KnowledgeTemporalState temporalState
) {
    public KnownActorRelationshipFact {
        sourceActorId=req(sourceActorId); targetActorId=req(targetActorId); Objects.requireNonNull(relationship); Objects.requireNonNull(temporalState);
    }
    private static String req(String s){Objects.requireNonNull(s);s=s.trim();if(s.isEmpty())throw new IllegalArgumentException("Actor vacío.");return s;}
}
