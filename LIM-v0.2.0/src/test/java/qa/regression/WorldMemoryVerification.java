package qa.regression;

import application.worldmemory.RecordConversationKnowledgeUseCase;
import application.worldmemory.RecordExplorationUseCase;
import application.worldmemory.RecordReadingKnowledgeUseCase;
import application.worldmemory.SelectRememberedIndicatorUseCase;
import domain.combat.HostileEncounterState;
import domain.worldmemory.*;
import domain.worldmemory.access.*;
import domain.worldmemory.entry.*;
import domain.worldmemory.evidence.*;
import domain.worldmemory.revision.*;
import domain.worldmemory.spatial.*;

import java.time.Instant;
import java.util.List;

public final class WorldMemoryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        WorldMemoryService access = new WorldMemoryService(new HostileEncounterState(), memory);

        org.junit.jupiter.api.Assertions.assertTrue(access.toggle().open(), "La Memoria del Mundo debe abrirse fuera de combate.");
        org.junit.jupiter.api.Assertions.assertTrue(memory.viewState().isOpen(), "El estado visual debe pertenecer al agregado persistente.");
        org.junit.jupiter.api.Assertions.assertTrue(access.toggle().allowed() && !memory.viewState().isOpen(),
                "La Memoria del Mundo debe poder replegarse sin alterar su conocimiento.");

        WorldKnowledgeSource rumorSource = new WorldKnowledgeSource(
                KnowledgeSourceType.CONVERSATION,
                "mercader-del-delta",
                Instant.parse("2026-07-18T00:00:00Z"),
                KnowledgeReliability.PLAUSIBLE
        );
        IndicatorId ruinsId = new IndicatorId("ruinas-del-norte");
        RememberedIndicator rumor = new RememberedIndicator(
                ruinsId,
                IndicatorType.LOCATION,
                "Ruinas del norte",
                "Un mercader sitúa unas ruinas al norte del delta.",
                new RememberedPosition(new WorldCoordinate(120, 480, 18), 300, SpatialPrecision.REGION),
                KnowledgeReliability.PLAUSIBLE,
                List.of(rumorSource)
        );
        var created = new RecordConversationKnowledgeUseCase(memory).execute(rumor);
        org.junit.jupiter.api.Assertions.assertTrue(created.created(), "La conversación debe crear un recuerdo inexistente.");
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().indicators().size() == 1, "El indicador debe quedar conservado.");

        WorldKnowledgeSource observedSource = new WorldKnowledgeSource(
                KnowledgeSourceType.DIRECT_EXPLORATION,
                "kenan",
                Instant.parse("2026-07-18T01:00:00Z"),
                KnowledgeReliability.OBSERVED
        );
        RememberedIndicator observed = new RememberedIndicator(
                ruinsId,
                IndicatorType.LOCATION,
                "Ruinas del norte",
                "Kenan ha observado directamente las ruinas desde la ladera.",
                new RememberedPosition(new WorldCoordinate(143, 501, 21), 20, SpatialPrecision.OBSERVED),
                KnowledgeReliability.OBSERVED,
                List.of(observedSource)
        );
        var revised = memory.knowledge().remember(observed);
        org.junit.jupiter.api.Assertions.assertTrue(revised.precisionImproved() && revised.reliabilityImproved(),
                "La observación directa debe mejorar precisión y fiabilidad.");
        org.junit.jupiter.api.Assertions.assertTrue(revised.indicator().sources().size() == 2,
                "La revisión debe conservar la procedencia histórica del recuerdo.");

        WorldKnowledgeSource terrainSource = WorldKnowledgeSource.now(
                KnowledgeSourceType.DIRECT_EXPLORATION, "recorrido-delta", KnowledgeReliability.OBSERVED);
        new RecordExplorationUseCase(memory).execute(new TerrainObservation(
                new WorldCoordinate(10, 15, 0.2), TerrainSurface.SHALLOW_WATER, 12,
                Instant.now(), terrainSource
        ));
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().terrain().observationCount() == 1,
                "La exploración debe construir el relieve recordado mediante observaciones.");

        new SelectRememberedIndicatorUseCase(memory).execute(ruinsId);
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().selectedIndicator().orElseThrow().id().equals(ruinsId),
                "Solo un indicador recordado puede quedar seleccionado.");

        WorldKnowledgeSource readingSource = WorldKnowledgeSource.now(
                KnowledgeSourceType.READING, "crónica-antigua", KnowledgeReliability.UNVERIFIED);
        IndicatorId roadId = new IndicatorId("camino-antiguo");
        new RecordReadingKnowledgeUseCase(memory).execute(new RememberedIndicator(
                roadId, IndicatorType.ROUTE, "Camino antiguo", "Una crónica menciona un camino desaparecido.",
                new RememberedPosition(new WorldCoordinate(-50, 90, 4), 500, SpatialPrecision.REGION),
                KnowledgeReliability.UNVERIFIED, List.of(readingSource)
        ));
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().indicator(roadId).isPresent(),
                "La lectura debe poder incorporar conocimiento no observado.");
    }

    
}
