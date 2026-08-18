package domain.character.canonical;
import domain.inventory.InventoryState;
import domain.inventory.equipment.ArmorEquipmentLayout;
import java.util.List;
import java.util.Objects;
public record CanonicalChildLoadout(InventoryState inventory, ArmorEquipmentLayout armorLayout, List<String> provisionNames) {
    public CanonicalChildLoadout { Objects.requireNonNull(inventory); Objects.requireNonNull(armorLayout); provisionNames=List.copyOf(provisionNames); }
}
