package domain.worldmemory.history;

import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Instantánea inmutable de una entrada en un punto de su historia epistémica. */
public record WorldMemoryEntryRevision(
        long sequence,
        Instant revisedAt,
        WorldMemoryRevisionType type,
        String origin,
        String note,
        Optional<KnowledgeStatus> previousStatus,
        KnowledgeStatus resultingStatus,
        Optional<KnowledgeReliability> previousReliability,
        KnowledgeReliability resultingReliability,
        WorldMemoryEntry snapshot
) {
    public WorldMemoryEntryRevision {
        if (sequence < 1) throw new IllegalArgumentException("La secuencia debe ser positiva.");
        Objects.requireNonNull(revisedAt);
        Objects.requireNonNull(type);
        origin = requireText(origin, "El origen de la revisión no puede estar vacío.");
        note = note == null ? "" : note.trim();
        previousStatus = Objects.requireNonNull(previousStatus);
        Objects.requireNonNull(resultingStatus);
        previousReliability = Objects.requireNonNull(previousReliability);
        Objects.requireNonNull(resultingReliability);
        Objects.requireNonNull(snapshot);
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
        return value.trim();
    }
}
