package domain.inventory.item;

import java.util.Objects;

/** Resultado canónico al abandonar dual wielding mediante cambio de modo. */
public record DualWieldExitTransition(
        WeaponConfiguration rightHandConfiguration,
        HandDisposition leftHandDisposition
) {
    public DualWieldExitTransition {
        rightHandConfiguration = Objects.requireNonNull(
                rightHandConfiguration, "La configuración derecha no puede ser nula.");
        leftHandDisposition = Objects.requireNonNull(
                leftHandDisposition, "La disposición izquierda no puede ser nula.");
        if (leftHandDisposition != HandDisposition.STOWED) {
            throw new IllegalArgumentException("La salida de dual wielding debe envainar el objeto izquierdo.");
        }
    }
}
