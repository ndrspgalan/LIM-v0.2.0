package domain.combat.ai.declarative;

import domain.inventory.InventoryEntry;
import domain.inventory.catalog.CanonicalObjectTypeId;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

/** Identidad y estado material de una instancia de objeto conocida por LIM. */
public record InventoryItemFact(
        InventoryEntry instance,
        CanonicalObjectTypeId canonicalTypeId,
        String name,
        double weightKg,
        InventoryLocationFact location,
        int currentUses,
        int maximumUses,
        OptionalDouble useDurationRealSeconds,
        List<ConsumableEffectFact> effects
) {
    public InventoryItemFact {
        Objects.requireNonNull(instance); Objects.requireNonNull(canonicalTypeId); Objects.requireNonNull(name); Objects.requireNonNull(location);
        if(weightKg < 0 || currentUses < 0 || maximumUses < currentUses) throw new IllegalArgumentException("Estado material de objeto no válido.");
        useDurationRealSeconds = useDurationRealSeconds == null ? OptionalDouble.empty() : useDurationRealSeconds;
        effects = List.copyOf(Objects.requireNonNull(effects));
    }
}
