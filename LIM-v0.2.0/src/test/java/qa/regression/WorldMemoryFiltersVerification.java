package qa.regression;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.KnowledgeStatus;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.filter.SpatialMemoryRequirement;
import domain.worldmemory.filter.WorldMemoryFilter;
import domain.worldmemory.filter.WorldMemoryFilterEngine;
import domain.worldmemory.query.WorldMemoryQuery;
import domain.worldmemory.search.WorldMemorySearch;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.WorldCoordinate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class WorldMemoryFiltersVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        remember(memory, "kenan", WorldMemoryCategory.PEOPLE, "Kenan",
                source(KnowledgeSourceType.OBSERVATION, KnowledgeReliability.VERIFIED), Optional.empty());
        remember(memory, "jacob", WorldMemoryCategory.PEOPLE, "Jacob",
                source(KnowledgeSourceType.CONVERSATION, KnowledgeReliability.PLAUSIBLE), Optional.empty());
        remember(memory, "santuario", WorldMemoryCategory.PLACES, "Santuario Blanco",
                source(KnowledgeSourceType.DIRECT_EXPLORATION, KnowledgeReliability.OBSERVED),
                Optional.of(RememberedPosition.verified(new WorldCoordinate(10, 20, 3))));

        WorldMemoryFilterEngine filters = new WorldMemoryFilterEngine(memory.knowledge());
        var people = new WorldMemoryFilter(Optional.of(WorldMemoryCategory.PEOPLE), Optional.empty(),
                Optional.empty(), Optional.empty(), SpatialMemoryRequirement.ANY);
        org.junit.jupiter.api.Assertions.assertTrue(filters.filter(people).size() == 2, "El filtro por categoría debe devolver solo personas adquiridas.");

        var confirmedSpatial = new WorldMemoryFilter(Optional.empty(), Optional.of(KnowledgeStatus.CONFIRMED),
                Optional.empty(), Optional.empty(), SpatialMemoryRequirement.WITH_REMEMBERED_POSITION);
        var result = filters.filter(confirmedSpatial);
        org.junit.jupiter.api.Assertions.assertTrue(result.size() == 1 && result.get(0).title().equals("Santuario Blanco"),
                "Los criterios combinados deben aplicarse conjuntamente.");

        var conversation = new WorldMemoryFilter(Optional.empty(), Optional.empty(),
                Optional.of(KnowledgeReliability.PLAUSIBLE), Optional.of(KnowledgeSourceType.CONVERSATION),
                SpatialMemoryRequirement.WITHOUT_REMEMBERED_POSITION);
        org.junit.jupiter.api.Assertions.assertTrue(filters.filter(conversation).size() == 1 && filters.filter(conversation).get(0).title().equals("Jacob"),
                "Debe filtrarse por fuente, fiabilidad y ausencia de posición.");

        var searched = new WorldMemorySearch(memory.knowledge()).search(new WorldMemoryQuery("jac"), people);
        org.junit.jupiter.api.Assertions.assertTrue(searched.size() == 1 && searched.get(0).title().equals("Jacob"),
                "La búsqueda debe poder reutilizar los mismos filtros.");
    }

    private static WorldKnowledgeSource source(KnowledgeSourceType type, KnowledgeReliability reliability) {
        return new WorldKnowledgeSource(type, "Prueba", Instant.parse("2026-07-28T00:00:00Z"), reliability);
    }

    private static void remember(WorldMemory memory, String id, WorldMemoryCategory category, String title,
                                 WorldKnowledgeSource source, Optional<RememberedPosition> position) {
        memory.knowledge().rememberEntry(new WorldMemoryEntry(new WorldMemoryEntryId(id), category,
                title, "", List.of(source), position));
    }

    
}
