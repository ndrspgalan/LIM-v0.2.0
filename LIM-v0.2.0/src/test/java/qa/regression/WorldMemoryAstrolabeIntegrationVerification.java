package qa.regression;

import domain.orientation.OrientationDirection;
import domain.orientation.WorldOrientationService;
import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.SpatialPrecision;
import domain.worldmemory.spatial.WorldCoordinate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class WorldMemoryAstrolabeIntegrationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        WorldMemoryEntryId id = new WorldMemoryEntryId("torre-del-norte");
        WorldKnowledgeSource source = new WorldKnowledgeSource(
                KnowledgeSourceType.OBSERVATION, "Kenan", Instant.parse("2026-07-28T08:00:00Z"),
                KnowledgeReliability.OBSERVED);

        WorldMemoryEntry initial = new WorldMemoryEntry(
                id, WorldMemoryCategory.PLACES, "Torre del Norte", "Visible desde el valle.", List.of(source),
                Optional.of(new RememberedPosition(new WorldCoordinate(0, 100, 0), 25, SpatialPrecision.APPROXIMATE)));
        memory.knowledge().rememberEntry(initial);
        memory.knowledge().select(id);

        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().selectedIndicator().isEmpty(),
                "Una entrada espacial neutral no debe necesitar un RememberedIndicator obsoleto.");
        var first = new WorldOrientationService().selectedDestination(memory, new WorldCoordinate(0, 0, 0));
        org.junit.jupiter.api.Assertions.assertTrue(first.available(), "El Astrolabio debe orientar hacia cualquier WorldMemoryEntry espacial adquirida.");
        org.junit.jupiter.api.Assertions.assertTrue(first.direction() == OrientationDirection.NORTH, "La primera ubicación debe señalar al norte.");
        org.junit.jupiter.api.Assertions.assertTrue(first.uncertaintyRadiusMeters() == 25, "Debe respetarse la incertidumbre recordada vigente.");

        WorldKnowledgeSource verified = new WorldKnowledgeSource(
                KnowledgeSourceType.DIRECT_EXPLORATION, "Kenan", Instant.parse("2026-07-28T09:00:00Z"),
                KnowledgeReliability.VERIFIED);
        WorldMemoryEntry revised = new WorldMemoryEntry(
                id, WorldMemoryCategory.PLACES, "Torre del Norte", "Ubicación confirmada personalmente.",
                List.of(source, verified), Optional.of(RememberedPosition.verified(new WorldCoordinate(100, 0, 0))));
        memory.knowledge().rememberEntry(revised);

        var refreshed = new WorldOrientationService().selectedDestination(memory, new WorldCoordinate(0, 0, 0));
        org.junit.jupiter.api.Assertions.assertTrue(refreshed.direction() == OrientationDirection.EAST,
                "Una revisión espacial debe actualizar el Astrolabio sin exigir reselección.");
        org.junit.jupiter.api.Assertions.assertTrue(refreshed.uncertaintyRadiusMeters() == 0,
                "La referencia vigente debe usar la nueva precisión espacial.");
        org.junit.jupiter.api.Assertions.assertTrue(refreshed.reliability() == KnowledgeReliability.VERIFIED,
                "La orientación debe reflejar la fiabilidad principal de la entrada vigente.");

        WorldMemoryEntryId nonSpatialId = new WorldMemoryEntryId("ley-del-puerto");
        memory.knowledge().rememberEntry(new WorldMemoryEntry(
                nonSpatialId, WorldMemoryCategory.WORLD_KNOWLEDGE, "Ley del Puerto", "Norma comercial.",
                List.of(source), Optional.empty()));
        boolean rejected = false;
        try { memory.knowledge().select(nonSpatialId); }
        catch (IllegalArgumentException expected) { rejected = true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected, "Una entrada sin memoria espacial no debe poder seleccionarse para el Astrolabio.");
    }

    
}
