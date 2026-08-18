package domain.inventory.item;

import java.util.Objects;

/**
 * Regla universal de modos durante dual wielding:
 * mano derecha en modo principal y mano izquierda en modo alternativo.
 */
public final class DualWieldConfigurationPolicy {
    private DualWieldConfigurationPolicy() {}

    public static void activate(WeaponItem rightHand, WeaponItem leftHand) {
        Objects.requireNonNull(rightHand, "El arma de la mano derecha no puede ser nula.");
        Objects.requireNonNull(leftHand, "El arma de la mano izquierda no puede ser nula.");
        rightHand.selectConfiguration(rightHandConfiguration(rightHand));
        leftHand.selectConfiguration(leftHandConfiguration(leftHand));
    }

    public static WeaponConfiguration rightHandConfiguration(WeaponItem rightHand) {
        Objects.requireNonNull(rightHand, "El arma de la mano derecha no puede ser nula.");
        WeaponConfiguration required = new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.PRIMARY);
        if (!rightHand.supportsConfiguration(required)) {
            throw new IllegalArgumentException(
                    "El objeto de la mano derecha debe admitir uso a una mano en modo principal para dual wielding.");
        }
        return required;
    }

    public static WeaponConfiguration leftHandConfiguration(WeaponItem leftHand) {
        Objects.requireNonNull(leftHand, "El arma de la mano izquierda no puede ser nula.");
        WeaponConfiguration required = new WeaponConfiguration(GripMode.ONE_HANDED, WeaponActionMode.ALTERNATIVE);
        if (!leftHand.supportsConfiguration(required)) {
            throw new IllegalArgumentException(
                    "El objeto de la mano izquierda debe admitir uso a una mano en modo alternativo para dual wielding.");
        }
        return required;
    }

    /**
     * La rueda abandona dual wielding: el objeto izquierdo se guarda y el derecho pasa a alternativo.
     * El envainado físico se aplicará en la capa de ejecución, no mutando la semántica especial de
     * {@link WeaponItem#toggleSheathing(domain.character.sheet.CharacterSheet)}.
     */
    public static DualWieldExitTransition exitToSingleRight(WeaponItem rightHand, WeaponItem leftHand) {
        Objects.requireNonNull(rightHand, "El arma de la mano derecha no puede ser nula.");
        Objects.requireNonNull(leftHand, "El objeto de la mano izquierda no puede ser nulo.");
        WeaponConfiguration alternative = rightHand.configurationPolicyView()
                .preferredFor(WeaponActionMode.ALTERNATIVE, GripMode.ONE_HANDED)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El arma derecha no admite modo alternativo a una mano al abandonar dual wielding."));
        rightHand.selectConfiguration(alternative);
        return new DualWieldExitTransition(alternative, HandDisposition.STOWED);
    }
}
