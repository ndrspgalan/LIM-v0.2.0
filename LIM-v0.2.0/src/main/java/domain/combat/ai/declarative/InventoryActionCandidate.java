package domain.combat.ai.declarative;

import domain.inventory.equipment.EquipmentSlot;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/** Acción de inventario/equipamiento declarada como legal por las autoridades de LIM. */
public record InventoryActionCandidate(
        InventoryActionType action,
        InventoryItemFact item,
        Optional<EquipmentSlot> targetEquipmentSlot,
        OptionalInt targetQuickAccessSlot,
        List<String> preconditions,
        List<String> consequences
) {
    public InventoryActionCandidate {
        Objects.requireNonNull(action); Objects.requireNonNull(item);
        targetEquipmentSlot = targetEquipmentSlot == null ? Optional.empty() : targetEquipmentSlot;
        targetQuickAccessSlot = targetQuickAccessSlot == null ? OptionalInt.empty() : targetQuickAccessSlot;
        preconditions = List.copyOf(Objects.requireNonNull(preconditions));
        consequences = List.copyOf(Objects.requireNonNull(consequences));
    }
}
