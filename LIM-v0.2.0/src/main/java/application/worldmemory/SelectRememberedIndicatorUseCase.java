package application.worldmemory;

import domain.worldmemory.entry.IndicatorId;
import domain.worldmemory.WorldMemory;
import java.util.Objects;

public final class SelectRememberedIndicatorUseCase {
    private final WorldMemory memory;

    public SelectRememberedIndicatorUseCase(WorldMemory memory) {
        this.memory = Objects.requireNonNull(memory);
    }

    public void execute(IndicatorId id) {
        memory.knowledge().select(Objects.requireNonNull(id));
    }

    public void clear() {
        memory.knowledge().clearSelection();
    }
}
