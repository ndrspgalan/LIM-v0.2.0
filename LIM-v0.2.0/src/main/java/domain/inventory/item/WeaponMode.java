package domain.inventory.item;

import java.util.Objects;

public record WeaponMode(String name, LethalityProfile lethality) {
    public WeaponMode {
        Objects.requireNonNull(name, "El nombre del modo de ataque no puede ser nulo.");
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("El nombre del modo de ataque no puede estar vacío.");
        }
        Objects.requireNonNull(lethality, "El perfil de letalidad no puede ser nulo.");
    }
}
