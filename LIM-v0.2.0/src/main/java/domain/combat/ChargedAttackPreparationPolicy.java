package domain.combat;

import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;

import java.util.Objects;

/**
 * Preparación de CHARGED.  elimina el antiguo placeholder universal de 1,5 s:
 * los CHARGED temporizados consultan ChargedAttackTimingPolicy según arma/modo.  mantiene preparaciones RELEASE_DRIVEN:
 * Helicoidal y Katana pueden mantenerse
 * indefinidamente hasta liberar la entrada. DE_ROTOR carece normalmente de CHARGED_ATTACK;  exceptúa el Espadón de Rotor.
 */
public final class ChargedAttackPreparationPolicy {
        private final ChargedAttackSpecializationPolicy specialization = new ChargedAttackSpecializationPolicy();

    public boolean canPrepare(WeaponItem weapon) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        return weapon.allowsCombatAction(WeaponCombatAction.CHARGED_ATTACK);
    }

    /**
     * : las preparaciones personalizadas de Helicoidal y Katana son gobernadas por liberación,
     * por lo que no autoejecutan por tiempo.
     */
    public boolean ready(WeaponItem weapon, ChargedAttackPreparationState state) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        if (specialization.releaseDriven(weapon)) return false;
        return state != null && state.preparing()
                && state.heldSeconds() >= ChargedAttackTimingPolicy.preparationSeconds(weapon);
    }

    public boolean canRelease(WeaponItem weapon, ChargedAttackPreparationState state) {
        return canPrepare(weapon) && specialization.releaseDriven(weapon)
                && state != null && state.preparing();
    }


    public ChargedAttackReleaseVariant release(
            WeaponItem weapon,
            ChargedAttackPreparationState state,
            ChargedAttackReleaseSide flourishExitSide) {
        if (!canRelease(weapon, state)) {
            throw new IllegalStateException("El arma no está en una preparación cargada gobernada por liberación.");
        }
        ChargedAttackReleaseVariant variant = specialization.releaseVariant(weapon, flourishExitSide);
        state.completePreparation();
        return variant;
    }

    public boolean canCancel(WeaponItem weapon, ChargedAttackPreparationState state) {
        return canPrepare(weapon) && state != null && state.preparing()
                && (specialization.releaseDriven(weapon) || !ready(weapon, state));
    }

    public boolean cancel(WeaponItem weapon, ChargedAttackPreparationState state) {
        if (!canCancel(weapon, state)) return false;
        state.cancelPreparation();
        return true;
    }

}
