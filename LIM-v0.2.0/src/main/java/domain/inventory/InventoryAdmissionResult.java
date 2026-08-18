package domain.inventory;

import domain.inventory.logistics.InventoryCompartmentType;
import java.util.Optional;

public record InventoryAdmissionResult(
        boolean accepted,
        InventoryState inventory,
        Optional<InventoryCompartmentType> destination,
        String message
) {
    public InventoryAdmissionResult {
        if (inventory == null || destination == null || message == null) throw new IllegalArgumentException("Resultado de admisión inválido.");
    }
}
