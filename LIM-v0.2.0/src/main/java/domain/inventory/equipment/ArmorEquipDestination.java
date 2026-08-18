package domain.inventory.equipment;
import domain.inventory.item.armor.ArmorLayerPosition;
import java.util.Objects;
public record ArmorEquipDestination(EquipmentSlot slot, ArmorLayerPosition position, String label) {
    public ArmorEquipDestination { Objects.requireNonNull(slot); Objects.requireNonNull(position); Objects.requireNonNull(label); }
}
