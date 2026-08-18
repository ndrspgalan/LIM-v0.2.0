package domain.runic.transposition;

import domain.character.progression.MucusWallet;
import domain.inventory.InventoryState;
import java.util.Objects;

public record InventoryTranspositionResult(boolean allowed, double mucusConsumedMl, int itemsCreated,
                                           MucusWallet wallet, InventoryState inventory, String message) {
    public InventoryTranspositionResult {
        Objects.requireNonNull(wallet); Objects.requireNonNull(inventory); Objects.requireNonNull(message);
        if (mucusConsumedMl < 0 || itemsCreated < 0) throw new IllegalArgumentException("Los contadores no pueden ser negativos.");
    }
}
