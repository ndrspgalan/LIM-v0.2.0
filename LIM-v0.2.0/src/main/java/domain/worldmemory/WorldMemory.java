package domain.worldmemory;

import domain.worldmemory.access.WorldMemoryViewState;
import domain.worldmemory.spatial.WorldCoordinate;

import java.util.Objects;

public final class WorldMemory {
    private final WorldMemoryViewState viewState;
    private final WorldMemoryKnowledge knowledge;

    public WorldMemory() {
        this(new WorldMemoryViewState(), new WorldMemoryKnowledge());
    }

    public WorldMemory(WorldMemoryViewState viewState, WorldMemoryKnowledge knowledge) {
        this.viewState = Objects.requireNonNull(viewState, "El estado visual no puede ser nulo.");
        this.knowledge = Objects.requireNonNull(knowledge, "El conocimiento no puede ser nulo.");
    }

    public WorldMemoryViewState viewState() {
        return viewState;
    }

    public WorldMemoryKnowledge knowledge() {
        return knowledge;
    }

    /** Hook de navegación: todo cambio de posición del protagonista aplica el contrato global de proximidad. */
    public boolean onProtagonistPositionChanged(WorldCoordinate position) {
        return knowledge.clearObservationMarkIfReached(Objects.requireNonNull(position));
    }
}
