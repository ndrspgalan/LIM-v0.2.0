package qa.regression;

import domain.worldmemory.WorldMemory;
import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.selection.WorldMemorySelection;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.WorldCoordinate;

import java.util.List;
import java.util.Optional;

public final class WorldMemoryArchitectureVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        separatesGeneralEntriesFromSpatialMemory();
        separatesSelectionFromKnowledgeStore();
        preservesObsoletoWorldMemoryBootstrap();
    }

    private static void separatesGeneralEntriesFromSpatialMemory() {
        var source = WorldKnowledgeSource.now(KnowledgeSourceType.CONVERSATION, "Jacob", KnowledgeReliability.PLAUSIBLE);
        var person = new WorldMemoryEntry(new WorldMemoryEntryId("jacob"), WorldMemoryCategory.PEOPLE,
                "Jacob, el mercenario", "Mercenario conocido por conversación.", List.of(source), Optional.empty());
        if (person.spatialMemory().isPresent()) throw new AssertionError("Una persona no debe requerir posición.");
        var place = new WorldMemoryEntry(new WorldMemoryEntryId("ruinas"), WorldMemoryCategory.PLACES,
                "Ruinas", "Lugar recordado.", List.of(source),
                Optional.of(RememberedPosition.verified(new WorldCoordinate(10, 20, 0))));
        if (place.spatialMemory().isEmpty()) throw new AssertionError("Un lugar puede conservar memoria espacial.");
    }

    private static void separatesSelectionFromKnowledgeStore() {
        var selection = new WorldMemorySelection();
        if (selection.selectedIndicatorId().isPresent()) throw new AssertionError();
    }

    private static void preservesObsoletoWorldMemoryBootstrap() {
        WorldMemory memory = new WorldMemory();
        if (memory.viewState().isOpen()) throw new AssertionError("La memoria debe iniciar cerrada.");
        if (!memory.knowledge().indicators().isEmpty()) throw new AssertionError("No debe inventar conocimiento.");
    }
}
