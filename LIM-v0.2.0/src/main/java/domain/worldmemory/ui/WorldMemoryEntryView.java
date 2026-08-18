package domain.worldmemory.ui;

import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.KnowledgeStatus;
import domain.worldmemory.spatial.RememberedPosition;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

/** Modelo de lectura estable para la ficha consultable de una entrada adquirida. */
public record WorldMemoryEntryView(
        WorldMemoryEntryId id,
        String title,
        WorldMemoryCategory category,
        KnowledgeStatus status,
        KnowledgeSourceType primarySourceType,
        String primarySourceReference,
        Instant acquiredAt,
        KnowledgeReliability reliability,
        String description,
        Optional<RememberedPosition> spatialMemory,
        List<WorldMemoryRelationView> relations,
        boolean selectableAsReference
) {
    public WorldMemoryEntryView {
        Objects.requireNonNull(id);
        if (title == null || title.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío.");
        title = title.trim();
        Objects.requireNonNull(category);
        Objects.requireNonNull(status);
        Objects.requireNonNull(primarySourceType);
        if (primarySourceReference == null || primarySourceReference.isBlank()) {
            throw new IllegalArgumentException("La referencia principal no puede estar vacía.");
        }
        primarySourceReference = primarySourceReference.trim();
        Objects.requireNonNull(acquiredAt);
        Objects.requireNonNull(reliability);
        description = description == null ? "" : description.trim();
        spatialMemory = Objects.requireNonNull(spatialMemory);
        relations = List.copyOf(Objects.requireNonNull(relations));
        if (selectableAsReference && spatialMemory.isEmpty()) {
            throw new IllegalArgumentException("Solo una entrada espacial puede seleccionarse como referencia.");
        }
    }
}
