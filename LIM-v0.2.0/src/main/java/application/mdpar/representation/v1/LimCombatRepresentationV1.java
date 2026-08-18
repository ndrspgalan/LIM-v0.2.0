package application.mdpar.representation.v1;

import java.util.List;
import java.util.Objects;

/**
 *  — contrato puro previo a JSON/HTTP. La simplificación es estructural, nunca por omisión táctica.
 */
public record LimCombatRepresentationV1(
        String schemaVersion,
        ActorKnowledgeV1 self,
        BattlespaceRepresentationV1 battlespace,
        List<ActorKnowledgeV1> knownActors,
        List<ActionRepresentationV1> actions,
        List<KnowledgeFactV1> projectedConsequences,
        List<KnowledgeFactV1> exhaustiveSelfAndKnownState,
        TargetingDoctrineV1 targetingDoctrine
) {
    public static final String VERSION="lim-combat-state/v1";
    public LimCombatRepresentationV1 {
        if(!VERSION.equals(schemaVersion))throw new IllegalArgumentException("Versión no soportada: "+schemaVersion);
        Objects.requireNonNull(self);Objects.requireNonNull(battlespace);knownActors=List.copyOf(Objects.requireNonNull(knownActors));
        actions=List.copyOf(Objects.requireNonNull(actions));projectedConsequences=List.copyOf(Objects.requireNonNull(projectedConsequences));
        exhaustiveSelfAndKnownState=List.copyOf(Objects.requireNonNull(exhaustiveSelfAndKnownState));Objects.requireNonNull(targetingDoctrine);
    }
}
