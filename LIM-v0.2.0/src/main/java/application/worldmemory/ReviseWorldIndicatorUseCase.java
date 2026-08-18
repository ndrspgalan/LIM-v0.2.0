package application.worldmemory;

import domain.worldmemory.revision.MemoryRevisionResult;
import domain.worldmemory.entry.RememberedIndicator;
import domain.worldmemory.WorldMemory;
import java.util.Objects;

public final class ReviseWorldIndicatorUseCase {
    private final WorldMemory memory;

    public ReviseWorldIndicatorUseCase(WorldMemory memory) {
        this.memory = Objects.requireNonNull(memory);
    }

    public MemoryRevisionResult execute(RememberedIndicator indicator) {
        return memory.knowledge().remember(Objects.requireNonNull(indicator));
    }
}
