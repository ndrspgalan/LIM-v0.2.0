package domain.combat;

import domain.inventory.item.WeaponItem;
import domain.movement.LocomotionMode;
import java.util.Objects;

/** Durante las preparaciones cargadas personalizadas de  sólo se permite caminar. */
public final class ChargedAttackMovementPolicy {
    private final ChargedAttackSpecializationPolicy specialization = new ChargedAttackSpecializationPolicy();

    public boolean allows(WeaponItem weapon, ChargedAttackPreparationState state, LocomotionMode requested) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        Objects.requireNonNull(requested, "El modo de locomoción no puede ser nulo.");
        if (state == null || !state.preparing() || !specialization.releaseDriven(weapon)) return true;
        return requested == LocomotionMode.WALKING;
    }
}
