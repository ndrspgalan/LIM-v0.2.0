package domain.worldmemory.evidence;

import java.time.Instant;
import java.util.Objects;

public record WorldKnowledgeSource(
        KnowledgeSourceType type,
        String sourceReference,
        Instant acquiredAt,
        KnowledgeReliability reliability
) {
    public WorldKnowledgeSource {
        Objects.requireNonNull(type, "El tipo de fuente no puede ser nulo.");
        sourceReference = requireText(sourceReference, "La referencia de la fuente no puede estar vacía.");
        Objects.requireNonNull(acquiredAt, "La fecha de adquisición no puede ser nula.");
        Objects.requireNonNull(reliability, "La fiabilidad no puede ser nula.");
    }

    public static WorldKnowledgeSource now(
            KnowledgeSourceType type,
            String sourceReference,
            KnowledgeReliability reliability
    ) {
        return new WorldKnowledgeSource(type, sourceReference, Instant.now(), reliability);
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
        return value.trim();
    }
}
