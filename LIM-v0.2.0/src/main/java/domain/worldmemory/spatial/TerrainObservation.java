package domain.worldmemory.spatial;

import domain.worldmemory.evidence.WorldKnowledgeSource;

import java.time.Instant;
import java.util.Objects;

public record TerrainObservation(
        WorldCoordinate coordinate,
        TerrainSurface surface,
        double observationRadiusMeters,
        Instant observedAt,
        WorldKnowledgeSource source
) {
    public TerrainObservation {
        Objects.requireNonNull(coordinate, "La coordenada observada no puede ser nula.");
        Objects.requireNonNull(surface, "La superficie observada no puede ser nula.");
        if (!Double.isFinite(observationRadiusMeters) || observationRadiusMeters <= 0) {
            throw new IllegalArgumentException("El radio de observación debe ser finito y positivo.");
        }
        Objects.requireNonNull(observedAt, "La fecha de observación no puede ser nula.");
        Objects.requireNonNull(source, "La fuente de la observación no puede ser nula.");
    }
}
