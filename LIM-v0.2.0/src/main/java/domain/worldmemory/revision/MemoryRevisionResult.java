package domain.worldmemory.revision;

import domain.worldmemory.entry.RememberedIndicator;

public record MemoryRevisionResult(
        RememberedIndicator indicator,
        boolean created,
        boolean positionUpdated,
        boolean descriptionUpdated,
        boolean precisionImproved,
        boolean reliabilityImproved,
        boolean sourceAdded
) {
    public boolean changed() {
        return created || positionUpdated || descriptionUpdated || precisionImproved
                || reliabilityImproved || sourceAdded;
    }
}
