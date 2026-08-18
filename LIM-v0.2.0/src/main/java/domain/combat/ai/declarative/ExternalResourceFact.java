package domain.combat.ai.declarative;

import domain.combat.ai.inventory.external.ExternalInventoryOwnerState;
import java.util.Objects;
import java.util.Optional;

/** Hecho sobre una fuente de recursos ajena; no contiene valoración táctica. */
public record ExternalResourceFact(
        String sourceId,
        Optional<String> ownerActorId,
        ExternalInventoryOwnerState ownerState,
        double distanceMeters,
        boolean reachable,
        boolean inventoryInspectable,
        boolean sessionOpen,
        boolean ownershipKnown,
        int knownItemCount
) {
    public ExternalResourceFact {
        Objects.requireNonNull(sourceId); Objects.requireNonNull(ownerActorId); Objects.requireNonNull(ownerState);
        if(distanceMeters<0 || knownItemCount<0) throw new IllegalArgumentException("Hecho externo no válido.");
    }
}
