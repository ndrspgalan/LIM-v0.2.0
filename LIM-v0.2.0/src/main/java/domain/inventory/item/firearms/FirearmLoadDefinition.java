package domain.inventory.item.firearms;

import domain.inventory.item.ammunition.AmmunitionDescriptor;
import java.util.Objects;

/** Contrato de alimentación del arma, independiente de que la munición llegue en cargador, clip o unidad suelta. */
public record FirearmLoadDefinition(String name, AmmunitionDescriptor descriptor, int capacity) {
    public FirearmLoadDefinition {
        Objects.requireNonNull(name);
        Objects.requireNonNull(descriptor);
        if (name.isBlank()) throw new IllegalArgumentException("El nombre de la alimentación no puede estar vacío.");
        if (capacity <= 0) throw new IllegalArgumentException("La capacidad debe ser positiva.");
    }

    public static FirearmLoadDefinition fromCartridge(FirearmCartridge cartridge) {
        Objects.requireNonNull(cartridge);
        return new FirearmLoadDefinition(cartridge.name(), cartridge.ammunitionDescriptor(), cartridge.capacity());
    }
}
