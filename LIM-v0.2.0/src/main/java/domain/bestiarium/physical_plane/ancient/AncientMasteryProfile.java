package domain.bestiarium.physical_plane.ancient;

import domain.ability.MasteryId;
import domain.ability.TransmutationNodeId;
import java.util.List;
import java.util.Set;

/** Maestrías realmente disponibles para un ANCIENT y manifestaciones atributivas concretas. */
public record AncientMasteryProfile(
        Set<MasteryId> masteryIds,
        Set<String> unlockedStages,
        Set<TransmutationNodeId> transmutationNodes,
        List<String> doctrine
) {
    public AncientMasteryProfile {
        masteryIds=Set.copyOf(masteryIds); unlockedStages=Set.copyOf(unlockedStages);
        transmutationNodes=Set.copyOf(transmutationNodes); doctrine=List.copyOf(doctrine);
    }
}
