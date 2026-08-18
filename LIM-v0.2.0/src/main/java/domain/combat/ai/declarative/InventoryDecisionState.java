package domain.combat.ai.declarative;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.status.TherapeuticEffectTracker;
import java.util.List;
import java.util.Objects;

/** Estado adicional necesario para materializar el dominio de inventario . */
public record InventoryDecisionState(
        InventoryState inventory,
        TherapeuticEffectTracker therapeuticEffects,
        List<InventoryEntry> reachableGroundItems
) {
    public InventoryDecisionState {
        Objects.requireNonNull(inventory); Objects.requireNonNull(therapeuticEffects);
        reachableGroundItems = List.copyOf(Objects.requireNonNull(reachableGroundItems));
    }
    public static InventoryDecisionState of(InventoryState inventory, TherapeuticEffectTracker effects) {
        return new InventoryDecisionState(inventory,effects,List.of());
    }
}
