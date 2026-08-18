package domain.worldmemory.filter;

import domain.worldmemory.WorldMemoryKnowledge;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.evidence.WorldMemoryEvidenceAssessment;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Evalúa filtros sin salir del conjunto de conocimiento adquirido por el personaje. */
public final class WorldMemoryFilterEngine {
    private static final Comparator<WorldMemoryEntry> ENTRY_ORDER =
            Comparator.comparing((WorldMemoryEntry entry) -> entry.title().toLowerCase())
                    .thenComparing(entry -> entry.id().value());

    private final WorldMemoryKnowledge knowledge;

    public WorldMemoryFilterEngine(WorldMemoryKnowledge knowledge) {
        this.knowledge = Objects.requireNonNull(knowledge);
    }

    public List<WorldMemoryEntry> filter(WorldMemoryFilter filter) {
        return filter(knowledge.entries().values(), filter);
    }

    public List<WorldMemoryEntry> filter(Collection<WorldMemoryEntry> candidates, WorldMemoryFilter filter) {
        Objects.requireNonNull(candidates, "Las entradas candidatas no pueden ser nulas.");
        Objects.requireNonNull(filter, "El filtro no puede ser nulo.");
        return candidates.stream()
                .filter(entry -> knowledge.entry(entry.id()).isPresent())
                .filter(entry -> matches(entry, filter))
                .sorted(ENTRY_ORDER)
                .toList();
    }

    public boolean matches(WorldMemoryEntry entry, WorldMemoryFilter filter) {
        Objects.requireNonNull(entry);
        Objects.requireNonNull(filter);
        if (knowledge.entry(entry.id()).isEmpty()) return false;
        WorldKnowledgeSource primary = WorldMemoryEvidenceAssessment.primarySource(entry);
        return filter.category().map(category -> entry.category() == category).orElse(true)
                && filter.status().map(status -> WorldMemoryEvidenceAssessment.statusOf(primary) == status).orElse(true)
                && filter.reliability().map(reliability -> primary.reliability() == reliability).orElse(true)
                && filter.sourceType().map(type -> primary.type() == type).orElse(true)
                && matchesSpatial(entry, filter.spatialRequirement());
    }

    private boolean matchesSpatial(WorldMemoryEntry entry, SpatialMemoryRequirement requirement) {
        return switch (requirement) {
            case ANY -> true;
            case WITH_REMEMBERED_POSITION -> entry.spatialMemory().isPresent();
            case WITHOUT_REMEMBERED_POSITION -> entry.spatialMemory().isEmpty();
        };
    }
}
