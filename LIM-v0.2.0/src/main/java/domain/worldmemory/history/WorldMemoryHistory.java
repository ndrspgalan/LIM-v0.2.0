package domain.worldmemory.history;

import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.evidence.WorldMemoryEvidenceAssessment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Registro longitudinal append-only; el estado vigente permanece en WorldMemoryKnowledge. */
public final class WorldMemoryHistory {
    private final Map<WorldMemoryEntryId, List<WorldMemoryEntryRevision>> revisions = new LinkedHashMap<>();

    public void record(WorldMemoryEntry previous, WorldMemoryEntry current) {
        Objects.requireNonNull(current);
        if (previous != null && previous.equals(current)) return;

        WorldKnowledgeSource currentPrimary = WorldMemoryEvidenceAssessment.primarySource(current);
        var previousPrimary = previous == null ? null : WorldMemoryEvidenceAssessment.primarySource(previous);
        WorldMemoryRevisionType type = classify(previous, current, previousPrimary, currentPrimary);
        String note = describe(previous, current, type);
        List<WorldMemoryEntryRevision> entryHistory = revisions.computeIfAbsent(current.id(), ignored -> new ArrayList<>());
        Instant revisedAt = current.sources().stream().map(WorldKnowledgeSource::acquiredAt)
                .max(Instant::compareTo).orElse(currentPrimary.acquiredAt());
        entryHistory.add(new WorldMemoryEntryRevision(
                entryHistory.size() + 1L,
                revisedAt,
                type,
                currentPrimary.type().name() + " — " + currentPrimary.sourceReference(),
                note,
                previousPrimary == null ? java.util.Optional.empty()
                        : java.util.Optional.of(WorldMemoryEvidenceAssessment.statusOf(previousPrimary)),
                WorldMemoryEvidenceAssessment.statusOf(currentPrimary),
                previousPrimary == null ? java.util.Optional.empty()
                        : java.util.Optional.of(previousPrimary.reliability()),
                currentPrimary.reliability(),
                current
        ));
    }

    public List<WorldMemoryEntryRevision> revisionsOf(WorldMemoryEntryId id) {
        Objects.requireNonNull(id);
        return List.copyOf(revisions.getOrDefault(id, List.of()));
    }

    private WorldMemoryRevisionType classify(WorldMemoryEntry previous, WorldMemoryEntry current,
                                               WorldKnowledgeSource previousPrimary,
                                               WorldKnowledgeSource currentPrimary) {
        if (previous == null) return WorldMemoryRevisionType.ACQUISITION;
        if (previous.spatialMemory().isPresent() && current.spatialMemory().isPresent()
                && !previous.spatialMemory().equals(current.spatialMemory())) {
            return WorldMemoryRevisionType.SPATIAL_REFINEMENT;
        }
        if (previousPrimary.reliability() != currentPrimary.reliability()) {
            return WorldMemoryRevisionType.RELIABILITY_CHANGE;
        }
        if (current.sources().size() > previous.sources().size()) return WorldMemoryRevisionType.SOURCE_ADDITION;
        if (!previous.description().equals(current.description()) && previous.description().isBlank()) {
            return WorldMemoryRevisionType.EXPANSION;
        }
        if (!previous.description().equals(current.description()) || !previous.title().equals(current.title())
                || previous.category() != current.category()) return WorldMemoryRevisionType.CORRECTION;
        return WorldMemoryRevisionType.REPLACEMENT;
    }

    private String describe(WorldMemoryEntry previous, WorldMemoryEntry current, WorldMemoryRevisionType type) {
        if (previous == null) return "La entrada fue incorporada a la Memoria del Mundo.";
        return switch (type) {
            case EXPANSION -> "Se amplió la descripción recordada.";
            case CORRECTION -> "Se rectificó información previamente recordada.";
            case RELIABILITY_CHANGE -> "Cambió la valoración de fiabilidad de la evidencia principal.";
            case SPATIAL_REFINEMENT -> "Se refinó la ubicación o su margen de incertidumbre.";
            case SOURCE_ADDITION -> "Se incorporó una fuente adicional a la entrada.";
            case INVALIDATION -> "El conocimiento anterior quedó invalidado.";
            case REPLACEMENT -> "El estado vigente sustituyó a la versión anterior.";
            case ACQUISITION -> "La entrada fue incorporada a la Memoria del Mundo.";
        };
    }
}
