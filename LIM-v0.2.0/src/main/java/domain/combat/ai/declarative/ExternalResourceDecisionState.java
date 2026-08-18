package domain.combat.ai.declarative;

import domain.inventory.InventoryEntry;
import java.util.List;
import java.util.Objects;

/** fuentes externas conocidas y objetos del mundo físicamente alcanzables. */
public record ExternalResourceDecisionState(
        List<ExternalResourceSourceState> sources,
        List<InventoryEntry> reachableWorldItems
) {
    public ExternalResourceDecisionState {
        sources=List.copyOf(Objects.requireNonNull(sources));
        reachableWorldItems=List.copyOf(Objects.requireNonNull(reachableWorldItems));
    }
    public static ExternalResourceDecisionState empty(){ return new ExternalResourceDecisionState(List.of(),List.of()); }
}
