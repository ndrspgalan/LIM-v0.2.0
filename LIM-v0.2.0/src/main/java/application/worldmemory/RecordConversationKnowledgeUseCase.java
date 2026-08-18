package application.worldmemory;

import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.revision.MemoryRevisionResult;
import domain.worldmemory.entry.RememberedIndicator;
import domain.worldmemory.WorldMemory;
import java.util.Objects;

public final class RecordConversationKnowledgeUseCase {
    private final WorldMemory memory;

    public RecordConversationKnowledgeUseCase(WorldMemory memory) {
        this.memory = Objects.requireNonNull(memory);
    }

    public MemoryRevisionResult execute(RememberedIndicator indicator) {
        requireSource(indicator, KnowledgeSourceType.CONVERSATION, KnowledgeSourceType.RECEIVED_DIRECTIONS);
        return memory.knowledge().remember(indicator);
    }

    private static void requireSource(RememberedIndicator indicator, KnowledgeSourceType... accepted) {
        Objects.requireNonNull(indicator);
        boolean valid = indicator.sources().stream().anyMatch(source -> {
            for (KnowledgeSourceType type : accepted) if (source.type() == type) return true;
            return false;
        });
        if (!valid) throw new IllegalArgumentException("El conocimiento conversacional debe conservar una fuente conversacional o indicaciones recibidas.");
    }
}
