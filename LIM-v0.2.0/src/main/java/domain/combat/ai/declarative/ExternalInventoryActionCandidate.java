package domain.combat.ai.declarative;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Operación material de adquisición externa. No contiene score ni preferencia. */
public record ExternalInventoryActionCandidate(
        ExternalInventoryActionType actionType,
        String sourceId,
        Optional<InventoryItemFact> item,
        Optional<String> feraeTrophy,
        List<String> preconditions,
        List<String> consequences
) {
    public ExternalInventoryActionCandidate {
        Objects.requireNonNull(actionType); Objects.requireNonNull(sourceId); Objects.requireNonNull(item); Objects.requireNonNull(feraeTrophy);
        preconditions=List.copyOf(Objects.requireNonNull(preconditions));
        consequences=List.copyOf(Objects.requireNonNull(consequences));
    }
}
