package domain.worldmemory.access;

import domain.worldmemory.WorldMemory;

import domain.combat.HostileEncounterState;
import java.util.Objects;

/** Servicio de acceso visual. No modifica el conocimiento recordado. */
public final class WorldMemoryService {
    private final HostileEncounterState hostileEncounterState;
    private final WorldMemory memory;

    public WorldMemoryService(HostileEncounterState hostileEncounterState, WorldMemory memory) {
        this.hostileEncounterState = Objects.requireNonNull(hostileEncounterState);
        this.memory = Objects.requireNonNull(memory);
    }

    public WorldMemoryAccessResult toggle() {
        if (hostileEncounterState.isActive()) {
            return new WorldMemoryAccessResult(false, memory.viewState().isOpen(), "",
                    "La Memoria del Mundo no puede abrirse durante un encuentro hostil.");
        }
        memory.viewState().toggle();
        return new WorldMemoryAccessResult(true, memory.viewState().isOpen(), "RECORDAR",
                memory.viewState().isOpen()
                        ? "La Memoria del Mundo se despliega."
                        : "La Memoria del Mundo se repliega.");
    }
}
