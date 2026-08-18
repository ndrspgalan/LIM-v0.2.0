package application.worldmemory;

import domain.worldmemory.access.WorldMemoryAccessResult;
import domain.worldmemory.access.WorldMemoryService;
import java.util.Objects;

public final class ToggleWorldMemoryUseCase {
    private final WorldMemoryService service;

    public ToggleWorldMemoryUseCase(WorldMemoryService service) {
        this.service = Objects.requireNonNull(service);
    }

    public WorldMemoryAccessResult execute() {
        return service.toggle();
    }
}
