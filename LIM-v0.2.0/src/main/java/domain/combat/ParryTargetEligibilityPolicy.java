package domain.combat;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;

import java.util.Objects;

/** Regla común de elegibilidad del arma rival para PARRY y MirrorParry. */
public final class ParryTargetEligibilityPolicy {
    public boolean isEligible(WeaponItem opposingWeapon) {
        Objects.requireNonNull(opposingWeapon, "El arma rival no puede ser nula.");
        if (opposingWeapon.isExclusivelyTwoHanded()) return false;
        if (opposingWeapon.hasTrait(WeaponTrait.DE_ROTOR)) return false;
        if (opposingWeapon.hasTrait(WeaponTrait.SHIELD)) return false;
        return opposingWeapon.modes().stream()
                .anyMatch(mode -> mode.lethality().slashing() > 0.0);
    }
}
