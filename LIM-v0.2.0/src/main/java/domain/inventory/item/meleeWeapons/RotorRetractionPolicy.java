package domain.inventory.item.meleeWeapons;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;

import java.util.Objects;

/** Transición mecánica del armazón dorsal parcialmente retraíble del Espadón de Rotor. */
public final class RotorRetractionPolicy {
    public RotorRetractionState stateOf(WeaponItem weapon) {
        validateRotor(weapon);
        return weapon.isSheathed() ? RotorRetractionState.RETRACTED : RotorRetractionState.DEPLOYED;
    }

    public RotorRetractionState beginDeployment(WeaponItem weapon, boolean offensiveActionActive) {
        validateRotor(weapon);
        if (offensiveActionActive || !weapon.isSheathed()) return stateOf(weapon);
        return RotorRetractionState.DEPLOYING;
    }

    public RotorRetractionState completeDeployment(WeaponItem weapon) {
        validateRotor(weapon);
        weapon.deployFromDorsalForHandlingTransition();
        return RotorRetractionState.DEPLOYED;
    }

    public RotorRetractionState beginRetraction(WeaponItem weapon, boolean offensiveOrRecoveryActive) {
        validateRotor(weapon);
        if (offensiveOrRecoveryActive || weapon.isSheathed()) return stateOf(weapon);
        return RotorRetractionState.RETRACTING;
    }

    public RotorRetractionState completeRetraction(WeaponItem weapon) {
        validateRotor(weapon);
        weapon.retractIntoDorsalForHandlingTransition();
        return RotorRetractionState.RETRACTED;
    }

    public boolean canAttack(WeaponItem weapon, RotorRetractionState state) {
        validateRotor(weapon);
        return state == RotorRetractionState.DEPLOYED && !weapon.isSheathed();
    }

    private static void validateRotor(WeaponItem weapon) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        if (!weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
            throw new IllegalArgumentException("La retracción solo se aplica a un arma DE ROTOR.");
        }
    }
}
