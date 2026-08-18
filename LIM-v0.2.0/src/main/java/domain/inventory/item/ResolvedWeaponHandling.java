package domain.inventory.item;

import java.util.Objects;

/** Estado coherente y ya resuelto de las dos manos. */
public record ResolvedWeaponHandling(
        ResolvedHand rightHand,
        ResolvedHand leftHand,
        WieldingState wieldingState
) {
    public ResolvedWeaponHandling {
        rightHand = Objects.requireNonNull(rightHand, "La mano derecha no puede ser nula.");
        leftHand = Objects.requireNonNull(leftHand, "La mano izquierda no puede ser nula.");
        wieldingState = Objects.requireNonNull(wieldingState, "El estado de manejo no puede ser nulo.");
    }
}
