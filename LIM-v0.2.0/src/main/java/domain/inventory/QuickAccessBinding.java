package domain.inventory;

import domain.inventory.catalog.CanonicalObjectTypeId;
import domain.inventory.logistics.InventoryCompartmentType;

import java.util.Objects;

public record QuickAccessBinding(
        int slotNumber,
        CanonicalObjectTypeId typeId,
        InventoryCompartmentType sourceCompartment,
        InventoryEntry currentInstance
) {
    public QuickAccessBinding {
        if(slotNumber<1 || slotNumber>QuickAccessBar.SLOT_COUNT) throw new IllegalArgumentException("Quick inválido.");
        Objects.requireNonNull(typeId); Objects.requireNonNull(sourceCompartment); Objects.requireNonNull(currentInstance);
    }
}
