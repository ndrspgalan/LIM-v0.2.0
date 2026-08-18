package domain.worldmemory.entry;

import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.spatial.RememberedPosition;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Raíz neutral preparada para que el conocimiento deje de equivaler a un marcador espacial. */
public record WorldMemoryEntry(WorldMemoryEntryId id, WorldMemoryCategory category, String title,
                               String description, List<WorldKnowledgeSource> sources,
                               Optional<RememberedPosition> spatialMemory) {
    public WorldMemoryEntry {
        Objects.requireNonNull(id); Objects.requireNonNull(category);
        if (title == null || title.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío.");
        title = title.trim(); description = description == null ? "" : description.trim();
        sources = List.copyOf(Objects.requireNonNull(sources));
        if (sources.isEmpty()) throw new IllegalArgumentException("Una entrada debe conservar al menos una fuente.");
        spatialMemory = Objects.requireNonNull(spatialMemory);
    }
}
