package domain.combat.ai.declarative;

import domain.environment.time.AtmosphericPhenomenon;
import domain.environment.time.DayPhase;
import domain.environment.time.Weather;
import domain.worldmemory.spatial.TerrainSurface;

import java.util.List;
import java.util.Objects;

/**
 *  — estado ambiental actor-específico que puede alterar una decisión hostil.
 * No decide utilidad: sólo expone hechos del campo de batalla conocidos por LIM.
 */
public record CombatWorldDecisionState(
        DayPhase dayPhase,
        Weather weather,
        TerrainSurface terrainSurface,
        AtmosphericPhenomenon atmosphericPhenomenon,
        boolean indoors,
        double waterDepthMeters,
        double elevationMeters,
        boolean directLineOfSight,
        boolean coverAvailable,
        List<String> activeHazards
) {
    public CombatWorldDecisionState {
        Objects.requireNonNull(dayPhase); Objects.requireNonNull(weather); Objects.requireNonNull(terrainSurface);
        Objects.requireNonNull(atmosphericPhenomenon);
        activeHazards = List.copyOf(Objects.requireNonNull(activeHazards));
        if (!Double.isFinite(waterDepthMeters) || waterDepthMeters < 0 || !Double.isFinite(elevationMeters))
            throw new IllegalArgumentException("Geometría ambiental inválida.");
    }

    public static CombatWorldDecisionState neutral() {
        return new CombatWorldDecisionState(DayPhase.DAY, Weather.SPRING_CLEAR, TerrainSurface.UNKNOWN,
                AtmosphericPhenomenon.NONE, false, 0.0, 0.0, true, false, List.of());
    }
}
