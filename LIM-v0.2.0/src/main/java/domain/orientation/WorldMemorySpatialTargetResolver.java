package domain.orientation;

import domain.worldmemory.WorldMemoryKnowledge;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.evidence.WorldMemoryEvidenceAssessment;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.entry.WorldMemoryEntryId;

import java.util.Objects;
import java.util.Optional;

/** Traduce la selección epistémica vigente de la Memoria del Mundo a un destino navegable.
 * No conserva una copia del destino: siempre proyecta la versión actual de la entrada.
 */
public final class WorldMemorySpatialTargetResolver {
    public Optional<WorldMemorySpatialTarget> selectedTarget(WorldMemoryKnowledge knowledge) {
        Objects.requireNonNull(knowledge, "El conocimiento del mundo no puede ser nulo.");
        if (knowledge.observationMarkSelected()) {
            return knowledge.observationMark().map(mark -> new WorldMemorySpatialTarget(
                    new WorldMemoryEntryId("personal-observation-mark"),
                    "Marca de observación",
                    mark.asRememberedPosition(),
                    KnowledgeReliability.VERIFIED
            ));
        }
        return knowledge.selectedEntry().flatMap(this::fromEntry);
    }

    public Optional<WorldMemorySpatialTarget> fromEntry(WorldMemoryEntry entry) {
        Objects.requireNonNull(entry, "La entrada no puede ser nula.");
        return entry.spatialMemory().map(position -> new WorldMemorySpatialTarget(
                entry.id(),
                entry.title(),
                position,
                WorldMemoryEvidenceAssessment.primarySource(entry).reliability()
        ));
    }
}
