package domain.worldmemory.ui;

import domain.worldmemory.WorldMemoryKnowledge;
import domain.worldmemory.entry.WorldMemoryEntry;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.evidence.WorldMemoryEvidenceAssessment;
import domain.worldmemory.relation.WorldMemoryRelation;
import domain.worldmemory.relation.WorldMemoryRelationDirection;

import java.util.Comparator;
import java.util.Objects;
import java.util.List;

/** Proyecta conocimiento adquirido a una ficha de consulta sin revelar datos externos. */
public final class WorldMemoryEntryViewAssembler {
    private final WorldMemoryKnowledge knowledge;

    public WorldMemoryEntryViewAssembler(WorldMemoryKnowledge knowledge) {
        this.knowledge = Objects.requireNonNull(knowledge);
    }

    public WorldMemoryEntryView assemble(WorldMemoryEntry entry) {
        Objects.requireNonNull(entry);
        WorldKnowledgeSource primary = WorldMemoryEvidenceAssessment.primarySource(entry);

        boolean selectable = entry.spatialMemory().isPresent();

        return new WorldMemoryEntryView(
                entry.id(), entry.title(), entry.category(), WorldMemoryEvidenceAssessment.statusOf(primary), primary.type(),
                primary.sourceReference(), primary.acquiredAt(), primary.reliability(), entry.description(),
                entry.spatialMemory(), relationViews(entry), selectable
        );
    }

    private List<WorldMemoryRelationView> relationViews(WorldMemoryEntry entry) {
        return knowledge.relationsOf(entry.id()).stream()
                .map(relation -> relationView(entry, relation))
                .sorted(Comparator.comparing(WorldMemoryRelationView::label)
                        .thenComparing(WorldMemoryRelationView::relatedEntryTitle))
                .toList();
    }

    private WorldMemoryRelationView relationView(WorldMemoryEntry current, WorldMemoryRelation relation) {
        boolean outgoing = relation.source().equals(current.id());
        var relatedId = outgoing ? relation.target() : relation.source();
        var related = knowledge.entry(relatedId)
                .orElseThrow(() -> new IllegalStateException("Una relación adquirida no puede apuntar a una entrada ausente."));
        var direction = outgoing ? WorldMemoryRelationDirection.OUTGOING : WorldMemoryRelationDirection.INCOMING;
        var label = outgoing ? relation.type().outgoingLabel() : relation.type().incomingLabel();
        return new WorldMemoryRelationView(related.id(), related.title(), related.category(),
                relation.type(), direction, label, relation.note());
    }
}
