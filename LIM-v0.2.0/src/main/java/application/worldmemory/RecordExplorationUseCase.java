package application.worldmemory;

import domain.worldmemory.spatial.TerrainObservation;
import domain.worldmemory.WorldMemory;
import java.util.Objects;

public final class RecordExplorationUseCase {
    private final WorldMemory memory;

    public RecordExplorationUseCase(WorldMemory memory) {
        this.memory = Objects.requireNonNull(memory);
    }

    public void execute(TerrainObservation observation) {
        memory.knowledge().recordTerrain(Objects.requireNonNull(observation));
    }
}
