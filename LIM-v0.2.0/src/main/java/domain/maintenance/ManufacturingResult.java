package domain.maintenance;

import domain.inventory.InventoryEntry;
import java.util.*;

public record ManufacturingResult(
        boolean successful,
        Optional<InventoryEntry> product,
        String message,
        List<String> consumedInputs
) {
    public ManufacturingResult {
        Objects.requireNonNull(product);
        message=Objects.requireNonNull(message);
        consumedInputs=List.copyOf(consumedInputs);
    }

    public static ManufacturingResult rejected(String message){
        return new ManufacturingResult(false,Optional.empty(),message,List.of());
    }

    public static ManufacturingResult completed(InventoryEntry product,String message,List<String> consumed){
        return new ManufacturingResult(true,Optional.of(Objects.requireNonNull(product)),message,consumed);
    }
}
