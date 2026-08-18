package domain.worldmemory.entry;

import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.SpatialPrecision;

import java.util.List;
import java.util.Objects;

public record RememberedIndicator(
        IndicatorId id,
        IndicatorType type,
        String title,
        String description,
        RememberedPosition position,
        KnowledgeReliability reliability,
        List<WorldKnowledgeSource> sources
) {
    public RememberedIndicator {
        Objects.requireNonNull(id, "El identificador no puede ser nulo.");
        Objects.requireNonNull(type, "El tipo de indicador no puede ser nulo.");
        title = requireText(title, "El título no puede estar vacío.");
        description = description == null ? "" : description.trim();
        Objects.requireNonNull(position, "La posición no puede ser nula.");
        Objects.requireNonNull(reliability, "La fiabilidad no puede ser nula.");
        sources = List.copyOf(Objects.requireNonNull(sources, "Las fuentes no pueden ser nulas."));
        if (sources.isEmpty()) throw new IllegalArgumentException("Un recuerdo debe conservar al menos una fuente.");
    }

    public SpatialPrecision precision() {
        return position.precision();
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
        return value.trim();
    }
}
