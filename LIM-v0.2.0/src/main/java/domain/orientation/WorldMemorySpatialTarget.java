package domain.orientation;

import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.spatial.RememberedPosition;

import java.util.Objects;

/** Proyección espacial neutral que consume el Astrolabio. */
public record WorldMemorySpatialTarget(
        WorldMemoryEntryId entryId,
        String title,
        RememberedPosition position,
        KnowledgeReliability reliability
) {
    public WorldMemorySpatialTarget {
        Objects.requireNonNull(entryId, "La entrada no puede ser nula.");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío.");
        title = title.trim();
        Objects.requireNonNull(position, "La posición recordada no puede ser nula.");
        Objects.requireNonNull(reliability, "La fiabilidad no puede ser nula.");
    }
}
