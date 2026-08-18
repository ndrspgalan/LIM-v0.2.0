package domain.worldmemory.search;

import domain.worldmemory.WorldMemoryKnowledge;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.filter.WorldMemoryFilter;
import domain.worldmemory.filter.WorldMemoryFilterEngine;
import domain.worldmemory.query.WorldMemoryQuery;

import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Búsqueda nominal determinista, tolerante a mayúsculas y diacríticos. */
public final class WorldMemorySearch {
    private static final Comparator<WorldMemorySearchResult> RESULT_ORDER =
            Comparator.comparing(WorldMemorySearchResult::matchQuality)
                    .thenComparing(result -> normalize(result.title()))
                    .thenComparing(result -> result.entryId().value());

    private final WorldMemoryKnowledge knowledge;
    private final WorldMemoryFilterEngine filters;

    public WorldMemorySearch(WorldMemoryKnowledge knowledge) {
        this.knowledge = Objects.requireNonNull(knowledge);
        this.filters = new WorldMemoryFilterEngine(knowledge);
    }

    public List<WorldMemorySearchResult> search(WorldMemoryQuery query) {
        return search(query, WorldMemoryFilter.none());
    }

    public List<WorldMemorySearchResult> search(WorldMemoryQuery query, WorldMemoryFilter filter) {
        Objects.requireNonNull(query);
        Objects.requireNonNull(filter);
        String needle = normalize(query.text());
        if (needle.isEmpty()) return List.of();

        return filters.filter(filter).stream()
                .map(entry -> result(entry, needle))
                .flatMap(java.util.Optional::stream)
                .sorted(RESULT_ORDER)
                .toList();
    }

    private java.util.Optional<WorldMemorySearchResult> result(WorldMemoryEntry entry, String needle) {
        String title = normalize(entry.title());
        WorldMemorySearchResult.MatchQuality quality;
        if (title.equals(needle)) quality = WorldMemorySearchResult.MatchQuality.EXACT;
        else if (title.startsWith(needle)) quality = WorldMemorySearchResult.MatchQuality.PREFIX;
        else if (title.contains(needle)) quality = WorldMemorySearchResult.MatchQuality.CONTAINS;
        else return java.util.Optional.empty();

        return java.util.Optional.of(new WorldMemorySearchResult(
                entry.id(), entry.title(), entry.category(), quality));
    }

    static String normalize(String value) {
        String decomposed = Normalizer.normalize(Objects.requireNonNullElse(value, ""), Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll("\\s+", " ");
    }
}
