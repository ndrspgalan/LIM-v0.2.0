package domain.worldmemory.navigation;

import domain.worldmemory.WorldMemoryKnowledge;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.filter.WorldMemoryFilter;
import domain.worldmemory.filter.WorldMemoryFilterEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Construye la navegación categorial sin exponer conocimiento no adquirido. */
public final class WorldMemoryCategoryNavigator {
    private final WorldMemoryKnowledge knowledge;
    private final WorldMemoryFilterEngine filters;

    public WorldMemoryCategoryNavigator(WorldMemoryKnowledge knowledge) {
        this.knowledge = Objects.requireNonNull(knowledge);
        this.filters = new WorldMemoryFilterEngine(knowledge);
    }

    public List<WorldMemoryCategorySummary> categories() {
        return Arrays.stream(WorldMemoryCategory.values())
                .map(category -> new WorldMemoryCategorySummary(category, knowledge.knownCount(category)))
                .toList();
    }

    public List<WorldMemoryEntry> entries(WorldMemoryCategory category) {
        return knowledge.entries(Objects.requireNonNull(category));
    }

    public List<WorldMemoryEntry> entries(WorldMemoryCategory category, WorldMemoryFilter filter) {
        Objects.requireNonNull(category);
        Objects.requireNonNull(filter);
        return filters.filter(knowledge.entries(category), filter);
    }
}
