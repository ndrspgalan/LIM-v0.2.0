package domain.combat.ai.declarative;

import domain.bestiarium.physical_plane.ferae.FeraeProfile;
import domain.combat.ai.inventory.external.ExternalInventoryOwnerState;
import domain.inventory.InventoryEntry;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Estado autoritativo ya filtrado a recursos externos conocidos por el actor. */
public record ExternalResourceSourceState(
        String sourceId,
        Optional<String> ownerActorId,
        ExternalInventoryOwnerState ownerState,
        double distanceMeters,
        boolean perceived,
        boolean reachable,
        boolean invisibilityActive,
        boolean hostileEncounter,
        boolean sessionOpen,
        boolean ownershipKnown,
        List<InventoryEntry> knownItems,
        Optional<FeraeProfile> ferae
) {
    public ExternalResourceSourceState {
        Objects.requireNonNull(sourceId); Objects.requireNonNull(ownerActorId); Objects.requireNonNull(ownerState);
        Objects.requireNonNull(knownItems); Objects.requireNonNull(ferae);
        if(sourceId.isBlank() || distanceMeters < 0) throw new IllegalArgumentException("Fuente externa no válida.");
        knownItems=List.copyOf(knownItems);
    }
}
