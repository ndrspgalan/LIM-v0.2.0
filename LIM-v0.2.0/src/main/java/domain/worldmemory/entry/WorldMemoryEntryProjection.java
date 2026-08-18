package domain.worldmemory.entry;

import domain.worldmemory.category.WorldMemoryCategoryProjection;

import java.util.Optional;

/** Adaptador de compatibilidad entre el marcador espacial heredado y la entrada neutral. */
public final class WorldMemoryEntryProjection {
    private WorldMemoryEntryProjection() {}

    public static WorldMemoryEntry from(RememberedIndicator indicator) {
        return new WorldMemoryEntry(
                new WorldMemoryEntryId(indicator.id().value()),
                WorldMemoryCategoryProjection.from(indicator.type()),
                indicator.title(),
                indicator.description(),
                indicator.sources(),
                Optional.of(indicator.position())
        );
    }
}
