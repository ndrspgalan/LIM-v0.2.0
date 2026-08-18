package application.mdpar.representation.v1;

import java.util.List;
import java.util.Objects;

/** Vista epistemológicamente válida de un actor. */
public record ActorKnowledgeV1(
        String actorId,
        ActorOriginV1 origin,
        String originKey,
        EpistemicStateV1 presenceState,
        List<KnowledgeFactV1> facts
) {
    public ActorKnowledgeV1 {
        if(actorId==null||actorId.isBlank())throw new IllegalArgumentException("actorId obligatorio.");
        Objects.requireNonNull(origin); originKey=Objects.requireNonNull(originKey); Objects.requireNonNull(presenceState);
        facts=List.copyOf(Objects.requireNonNull(facts));
    }
}
