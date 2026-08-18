package qa.regression;

import domain.orientation.OrientationDirection;
import domain.orientation.WorldOrientationService;
import domain.worldmemory.*;
import domain.worldmemory.access.*;
import domain.worldmemory.entry.*;
import domain.worldmemory.evidence.*;
import domain.worldmemory.revision.*;
import domain.worldmemory.spatial.*;


/** La antigua integración HUD se conserva como verificación del cálculo espacial neutral. */
public final class WorldMemoryHudIntegrationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        WorldMemory memory = new WorldMemory();
        WorldCoordinate viewer = new WorldCoordinate(0, 0, 0);
        WorldOrientationService orientation = new WorldOrientationService();
        org.junit.jupiter.api.Assertions.assertTrue(!orientation.selectedDestination(memory, viewer).available(),
                "Sin destino seleccionado no debe existir orientación.");

        RememberedIndicator indicator = new RememberedIndicator(
                new IndicatorId("refugio"),
                IndicatorType.LOCATION,
                "Refugio",
                "Destino de verificación",
                new RememberedPosition(new WorldCoordinate(100, 100, 0), 5, SpatialPrecision.APPROXIMATE),
                KnowledgeReliability.OBSERVED,
                java.util.List.of(WorldKnowledgeSource.now(
                        KnowledgeSourceType.DIRECT_EXPLORATION,
                        "migración-m19.4",
                        KnowledgeReliability.OBSERVED)));
        memory.knowledge().remember(indicator);
        memory.knowledge().select(indicator.id());
        var solution = orientation.selectedDestination(memory, viewer);
        org.junit.jupiter.api.Assertions.assertTrue(solution.available() && solution.direction() == OrientationDirection.NORTH_EAST,
                "La lógica espacial debe sobrevivir fuera del HUD.");
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(solution.headingDegrees() - 45.0) < 0.000001,
                "El rumbo neutral debe conservar el cálculo angular.");
    }

    
}
