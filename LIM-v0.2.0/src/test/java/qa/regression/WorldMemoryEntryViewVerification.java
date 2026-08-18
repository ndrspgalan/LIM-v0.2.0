package qa.regression;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.IndicatorId;
import domain.worldmemory.entry.IndicatorType;
import domain.worldmemory.entry.RememberedIndicator;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.KnowledgeStatus;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.SpatialPrecision;
import domain.worldmemory.spatial.WorldCoordinate;
import domain.worldmemory.ui.WorldMemoryEntryViewAssembler;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class WorldMemoryEntryViewVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        var conversation = new WorldKnowledgeSource(KnowledgeSourceType.CONVERSATION, "Jacob",
                Instant.parse("2026-01-01T00:00:00Z"), KnowledgeReliability.PLAUSIBLE);
        var direct = new WorldKnowledgeSource(KnowledgeSourceType.DIRECT_EXPLORATION, "Kenan",
                Instant.parse("2026-01-02T00:00:00Z"), KnowledgeReliability.VERIFIED);

        memory.knowledge().rememberEntry(new WorldMemoryEntry(new WorldMemoryEntryId("jacob"),
                WorldMemoryCategory.PEOPLE, "Jacob", "Mercenario conocido.", List.of(conversation), Optional.empty()));
        var personView = new WorldMemoryEntryViewAssembler(memory.knowledge())
                .assemble(memory.knowledge().entry(new WorldMemoryEntryId("jacob")).orElseThrow());
        org.junit.jupiter.api.Assertions.assertTrue(personView.status() == KnowledgeStatus.INFERRED, "La conversación plausible debe conservar carácter inferido.");
        org.junit.jupiter.api.Assertions.assertTrue(!personView.selectableAsReference(), "Una persona sin memoria espacial no debe seleccionarse.");

        RememberedIndicator sanctuary = new RememberedIndicator(new IndicatorId("santuario"), IndicatorType.LOCATION,
                "Santuario", "Ruinas observadas.", new RememberedPosition(new WorldCoordinate(10, 20, 3),
                0, SpatialPrecision.VERIFIED), KnowledgeReliability.VERIFIED, List.of(direct));
        memory.knowledge().remember(sanctuary);
        var placeView = new WorldMemoryEntryViewAssembler(memory.knowledge())
                .assemble(memory.knowledge().entry(new WorldMemoryEntryId("santuario")).orElseThrow());
        org.junit.jupiter.api.Assertions.assertTrue(placeView.status() == KnowledgeStatus.CONFIRMED, "La exploración directa verificada debe quedar confirmada.");
        org.junit.jupiter.api.Assertions.assertTrue(placeView.selectableAsReference(), "Un indicador espacial adquirido debe poder seleccionarse.");
        memory.knowledge().select(placeView.id());
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().selectedIndicator().orElseThrow().id().value().equals("santuario"),
                "La selección desde la ficha debe alimentar el contrato del Astrolabio.");

        boolean rejected = false;
        try { memory.knowledge().select(new WorldMemoryEntryId("jacob")); }
        catch (IllegalArgumentException expected) { rejected = true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected, "No debe seleccionarse una entrada sin ubicación recordada.");
    }

    
}
