package domain.inventory.item;

import java.util.Objects;

/**
 * Mantiene un ordinal global interdependiente. Cada arma/modo intenta ejecutar ese ordinal
 * con su repertorio propio; si no existe, reinicia en LIGHT 1. Desde , cuando el reinicio
 * ocurre inmediatamente después de una cadena global de al menos tres LIGHT, ese golpe L1
 * conserva la bonificación de final de combo.
 */
public final class DualWieldLightComboPolicy {
    public DualWieldLightComboResolution resolve(
            WeaponItem weapon,
            WeaponActionMode mode,
            DualWieldComboState state
    ) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        Objects.requireNonNull(mode, "El modo no puede ser nulo.");
        Objects.requireNonNull(state, "El estado del combo no puede ser nulo.");
        if (!weapon.combatActionsFor(mode).contains(WeaponCombatAction.LIGHT_ATTACK)) {
            throw new IllegalArgumentException("El modo solicitado no permite ataques ligeros.");
        }
        int requested = state.nextLightAttackOrdinal();
        LightAttackComboProfile profile = weapon.lightAttackComboFor(mode);
        int executed = profile.supports(requested) ? requested : 1;
        boolean restarted = executed != requested;
        boolean ordinaryFinisher = !restarted && profile.hasFinisherBonus() && executed == profile.attackCount();
        boolean transferredFinisher = restarted && requested > 3; // se habían ejecutado al menos L1-L3 antes del reinicio
        state.registerExecutedOrdinal(executed);
        return new DualWieldLightComboResolution(mode, requested, executed, restarted,
                ordinaryFinisher || transferredFinisher);
    }
}
