package domain.ability;

import java.util.Objects;

/** Puerto común: jugador e IA resuelven la misma MasteryMechanic. */
public final class MasteryUseService {
    public MasteryExecutionResult executeActive(MasteryActor actor) {
        Objects.requireNonNull(actor);
        return actor.masteries().executeSelectedActive(actor.executionContext());
    }
    public MasteryExecutionResult toggleSustained(MasteryActor actor) {
        Objects.requireNonNull(actor);
        return actor.masteries().toggleSelectedSustained(actor.executionContext());
    }
    public java.util.List<MasteryManifestation> available(MasteryActor actor, MasteryType type) {
        Objects.requireNonNull(actor); Objects.requireNonNull(type);
        return actor.masteries().selectableManifestations(type, actor.sheet());
    }
}
