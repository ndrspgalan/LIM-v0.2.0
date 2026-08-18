package domain.worldmemory.navigation;

import domain.worldmemory.category.WorldMemoryCategory;
import java.util.Objects;

/** Read model mínimo de una familia en la pantalla principal. */
public record WorldMemoryCategorySummary(WorldMemoryCategory category, int knownEntries) {
    public WorldMemoryCategorySummary {
        Objects.requireNonNull(category);
        if (knownEntries < 0) throw new IllegalArgumentException("El número de entradas no puede ser negativo.");
    }
}
