package domain.worldmemory.search;

import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntryId;

import java.util.Objects;

/** Resultado seguro: solo identifica una entrada que ya forma parte de la memoria adquirida. */
public record WorldMemorySearchResult(WorldMemoryEntryId entryId, String title,
                                      WorldMemoryCategory category, MatchQuality matchQuality) {
    public WorldMemorySearchResult {
        Objects.requireNonNull(entryId);
        if (title == null || title.isBlank()) throw new IllegalArgumentException("El título no puede estar vacío.");
        title = title.trim();
        Objects.requireNonNull(category);
        Objects.requireNonNull(matchQuality);
    }

    public enum MatchQuality {
        EXACT,
        PREFIX,
        CONTAINS
    }
}
