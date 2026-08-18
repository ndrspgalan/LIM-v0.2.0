package domain.combat;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import java.util.Objects;

/** Personalización  del estado previo y la salida de CHARGED por geometría de arma. */
public final class ChargedAttackSpecializationPolicy {
    public ChargedAttackPreparationStyle style(WeaponItem weapon) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        if (weapon.hasTrait(WeaponTrait.HELICOIDAL_CONTROL)) return ChargedAttackPreparationStyle.CONTINUOUS_FLOURISH;
        if (weapon.hasTrait(WeaponTrait.THERMO_MECHANICAL)) return ChargedAttackPreparationStyle.HELD_READY_IN_SHEATH;
        return ChargedAttackPreparationStyle.STANDARD_TIMED;
    }

    public boolean releaseDriven(WeaponItem weapon) {
        return style(weapon) != ChargedAttackPreparationStyle.STANDARD_TIMED;
    }

    public ChargedAttackReleaseVariant releaseVariant(WeaponItem weapon, ChargedAttackReleaseSide flourishExitSide) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        return switch (style(weapon)) {
            case STANDARD_TIMED -> ChargedAttackReleaseVariant.STANDARD;
            case HELD_READY_IN_SHEATH -> ChargedAttackReleaseVariant.FORWARD_DRAW;
            case CONTINUOUS_FLOURISH -> {
                Objects.requireNonNull(flourishExitSide, "La floritura helicoidal requiere el lado visual de salida.");
                yield flourishExitSide == ChargedAttackReleaseSide.LEFT
                        ? ChargedAttackReleaseVariant.LEFT_TO_RIGHT
                        : ChargedAttackReleaseVariant.RIGHT_TO_LEFT;
            }
        };
    }
}
