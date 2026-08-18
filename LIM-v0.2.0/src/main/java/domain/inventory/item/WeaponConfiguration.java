package domain.inventory.item;

import java.util.Objects;

public record WeaponConfiguration(GripMode gripMode, WeaponActionMode actionMode) {
    public WeaponConfiguration {
        Objects.requireNonNull(gripMode, "La empuñadura no puede ser nula.");
        Objects.requireNonNull(actionMode, "El modo de acción no puede ser nulo.");
    }

    public String label() {
        return gripMode.label() + " · " + actionMode.label();
    }
}
