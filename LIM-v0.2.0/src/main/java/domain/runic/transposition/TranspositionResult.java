package domain.runic.transposition;

import domain.character.progression.MucusWallet;
import domain.inventory.logistics.InventoryCompartment;

import java.util.Objects;

public record TranspositionResult(
        boolean allowed,
        double mucusConsumed,
        int itemsCreated,
        MucusWallet wallet,
        InventoryCompartment compartment,
        String message
) {
    public TranspositionResult {
        Objects.requireNonNull(wallet); Objects.requireNonNull(compartment); Objects.requireNonNull(message);
        if (mucusConsumed < 0 || itemsCreated < 0) throw new IllegalArgumentException("Los contadores no pueden ser negativos.");
    }
}
