package application;

import domain.combat.HostileEncounterState;
import java.util.Objects;

/**  — el inventario propio puede abrirse/cerrarse también durante un encuentro hostil. */
public final class InventoryAccessService {
    private final HostileEncounterState hostileEncounterState;

    public InventoryAccessService(HostileEncounterState hostileEncounterState) {
        this.hostileEncounterState = Objects.requireNonNull(hostileEncounterState, "El estado de encuentro hostil no puede ser nulo.");
    }

    public InventoryAccessResult requestAccess() {
        return InventoryAccessResult.granted();
    }

    /** El estado hostil se conserva como hecho del mundo, pero ya no bloquea la UI de inventario. */
    public boolean hostileEncounterActive() { return hostileEncounterState.isActive(); }
}
