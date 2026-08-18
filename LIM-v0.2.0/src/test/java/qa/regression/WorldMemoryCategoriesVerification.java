package qa.regression;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.navigation.WorldMemoryCategoryNavigator;
import domain.worldmemory.spatial.TerrainObservation;
import domain.worldmemory.spatial.TerrainSurface;
import domain.worldmemory.spatial.WorldCoordinate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class WorldMemoryCategoriesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        var source = new WorldKnowledgeSource(KnowledgeSourceType.CONVERSATION,
                "Jacob", Instant.parse("2026-01-01T00:00:00Z"), KnowledgeReliability.PLAUSIBLE);
        memory.knowledge().rememberEntry(new WorldMemoryEntry(
                new WorldMemoryEntryId("jacob"), WorldMemoryCategory.PEOPLE,
                "Jacob, el mercenario", "Mercenario conocido por conversación.", List.of(source), Optional.empty()));
        memory.knowledge().recordTerrain(new TerrainObservation(
                new WorldCoordinate(0, 0, 0), TerrainSurface.EARTH, 5.0, Instant.parse("2026-01-01T00:00:00Z"), source));

        var navigator = new WorldMemoryCategoryNavigator(memory.knowledge());
        org.junit.jupiter.api.Assertions.assertTrue(navigator.categories().size() == 8, "Deben existir ocho familias navegables.");
        org.junit.jupiter.api.Assertions.assertTrue(navigator.entries(WorldMemoryCategory.PEOPLE).size() == 1, "Personas debe contener solo conocimiento adquirido.");
        org.junit.jupiter.api.Assertions.assertTrue(navigator.entries(WorldMemoryCategory.CREATURES).isEmpty(), "No debe inventarse conocimiento de criaturas.");
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().knownCount(WorldMemoryCategory.EXPLORED_TERRITORY) == 1,
                "El territorio explorado debe contar observaciones reales.");
    }

    
}
