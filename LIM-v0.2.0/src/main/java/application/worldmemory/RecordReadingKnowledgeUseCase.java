package application.worldmemory;

import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.revision.MemoryRevisionResult;
import domain.worldmemory.entry.RememberedIndicator;
import domain.worldmemory.WorldMemory;
import java.util.Objects;

public final class RecordReadingKnowledgeUseCase {
    private final WorldMemory memory;

    public RecordReadingKnowledgeUseCase(WorldMemory memory) {
        this.memory = Objects.requireNonNull(memory);
    }

    public MemoryRevisionResult execute(RememberedIndicator indicator) {
        Objects.requireNonNull(indicator);
        if (indicator.sources().stream().noneMatch(source -> source.type() == KnowledgeSourceType.READING)) {
            throw new IllegalArgumentException("El conocimiento leído debe conservar una fuente de lectura.");
        }
        return memory.knowledge().remember(indicator);
    }
}
