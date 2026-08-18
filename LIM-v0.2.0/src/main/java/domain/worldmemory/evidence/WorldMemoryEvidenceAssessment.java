package domain.worldmemory.evidence;

import domain.worldmemory.entry.WorldMemoryEntry;

import java.util.Comparator;
import java.util.Objects;

/** Reúne la valoración epistémica derivada para que búsqueda, filtros y fichas usen el mismo criterio. */
public final class WorldMemoryEvidenceAssessment {
    private static final Comparator<WorldKnowledgeSource> PRIMARY_SOURCE_ORDER =
            Comparator.comparing(WorldKnowledgeSource::reliability)
                    .thenComparing(WorldKnowledgeSource::acquiredAt);

    private WorldMemoryEvidenceAssessment() {
    }

    public static WorldKnowledgeSource primarySource(WorldMemoryEntry entry) {
        Objects.requireNonNull(entry, "La entrada no puede ser nula.");
        return entry.sources().stream()
                .max(PRIMARY_SOURCE_ORDER)
                .orElseThrow(() -> new IllegalStateException("Una entrada consultable debe conservar una fuente."));
    }

    public static KnowledgeStatus statusOf(WorldKnowledgeSource source) {
        Objects.requireNonNull(source, "La fuente no puede ser nula.");
        if (source.reliability() == KnowledgeReliability.RUMOR) return KnowledgeStatus.DOUBTFUL;
        if (source.reliability() == KnowledgeReliability.UNVERIFIED) return KnowledgeStatus.REFERRED;
        if (source.type() == KnowledgeSourceType.DIRECT_EXPLORATION
                || source.type() == KnowledgeSourceType.OBSERVATION
                || source.reliability() == KnowledgeReliability.VERIFIED) {
            return KnowledgeStatus.CONFIRMED;
        }
        return KnowledgeStatus.INFERRED;
    }
}
