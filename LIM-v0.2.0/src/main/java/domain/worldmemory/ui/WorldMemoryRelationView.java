package domain.worldmemory.ui;

import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.relation.WorldMemoryRelationDirection;
import domain.worldmemory.relation.WorldMemoryRelationType;

import java.util.Objects;

/** Proyección legible de una arista desde la perspectiva de la entrada consultada. */
public record WorldMemoryRelationView(
        WorldMemoryEntryId relatedEntryId,
        String relatedEntryTitle,
        WorldMemoryCategory relatedEntryCategory,
        WorldMemoryRelationType type,
        WorldMemoryRelationDirection direction,
        String label,
        String note
) {
    public WorldMemoryRelationView {
        Objects.requireNonNull(relatedEntryId);
        if (relatedEntryTitle == null || relatedEntryTitle.isBlank()) {
            throw new IllegalArgumentException("El título relacionado no puede estar vacío.");
        }
        relatedEntryTitle = relatedEntryTitle.trim();
        Objects.requireNonNull(relatedEntryCategory);
        Objects.requireNonNull(type);
        Objects.requireNonNull(direction);
        if (label == null || label.isBlank()) throw new IllegalArgumentException("La etiqueta no puede estar vacía.");
        label = label.trim();
        note = note == null ? "" : note.trim();
    }
}
