package domain.worldmemory.relation;

import domain.worldmemory.entry.WorldMemoryEntryId;

import java.util.Objects;

/** Una arista adquirida del grafo de conocimiento. La dirección forma parte de su significado. */
public record WorldMemoryRelation(
        WorldMemoryEntryId source,
        WorldMemoryRelationType type,
        WorldMemoryEntryId target,
        String note
) {
    public WorldMemoryRelation {
        Objects.requireNonNull(source, "El origen de la relación no puede ser nulo.");
        Objects.requireNonNull(type, "El tipo de relación no puede ser nulo.");
        Objects.requireNonNull(target, "El destino de la relación no puede ser nulo.");
        if (source.equals(target)) {
            throw new IllegalArgumentException("Una entrada no puede relacionarse consigo misma.");
        }
        note = note == null ? "" : note.trim();
    }

    public WorldMemoryRelation(WorldMemoryEntryId source, WorldMemoryRelationType type,
                               WorldMemoryEntryId target) {
        this(source, type, target, "");
    }
}
