package domain.inventory.item;

import java.util.Objects;

public record DualWieldLightComboResolution(
        WeaponActionMode handMode,
        int requestedOrdinal,
        int executedOrdinal,
        boolean restartedForWeapon,
        boolean finisherBonusApplies
) {
    public DualWieldLightComboResolution {
        Objects.requireNonNull(handMode, "El modo de mano no puede ser nulo.");
        if (requestedOrdinal < 1 || executedOrdinal < 1) {
            throw new IllegalArgumentException("Los ordinales deben ser positivos.");
        }
    }
}
