package domain.combat.ai.declarative;

/** Relación causal/física remota expuesta a MDPAR sin valoración. */
public record RemoteRelationFact(String relation, String detail) {
    public RemoteRelationFact {
        if (relation==null||relation.isBlank()||detail==null||detail.isBlank()) throw new IllegalArgumentException("Relación remota incompleta.");
    }
}
