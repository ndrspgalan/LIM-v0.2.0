package domain.combat;

import domain.inventory.item.GripMode;
import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;

import java.util.Objects;
import java.util.Set;

/**
 * Resuelve el parry en espejo de la geometría helicoidal.
 * Comparte elegibilidad con PARRY, pero se dispara por colisión de hitboxes ofensivas.
 */
public final class MirrorParryPolicy {
    private static final Set<WeaponCombatAction> OFFENSIVE_ATTACKS = Set.of(
            WeaponCombatAction.LIGHT_ATTACK,
            WeaponCombatAction.HEAVY_ATTACK,
            WeaponCombatAction.CHARGED_ATTACK,
            WeaponCombatAction.JUMP_ATTACK
    );

    private final ParryTargetEligibilityPolicy targetEligibility = new ParryTargetEligibilityPolicy();

    public boolean canMirrorParry(
            WeaponItem helicoidalWeapon,
            GripMode helicoidalGrip,
            WeaponCombatAction helicoidalAttack,
            WeaponItem opposingWeapon,
            GripMode opposingGrip,
            WeaponCombatAction opposingAttack,
            boolean attackHitboxesCollide
    ) {
        Objects.requireNonNull(helicoidalWeapon, "El arma helicoidal no puede ser nula.");
        Objects.requireNonNull(helicoidalGrip, "El agarre helicoidal no puede ser nulo.");
        Objects.requireNonNull(helicoidalAttack, "El ataque helicoidal no puede ser nulo.");
        Objects.requireNonNull(opposingWeapon, "El arma rival no puede ser nula.");
        Objects.requireNonNull(opposingGrip, "El agarre rival no puede ser nulo.");
        Objects.requireNonNull(opposingAttack, "El ataque rival no puede ser nulo.");

        return attackHitboxesCollide
                && helicoidalWeapon.hasTrait(WeaponTrait.HELICOIDAL_CONTROL)
                && OFFENSIVE_ATTACKS.contains(helicoidalAttack)
                && OFFENSIVE_ATTACKS.contains(opposingAttack)
                && targetEligibility.isEligible(opposingWeapon);
    }
    public ParryResolution resolveMirrorParry(
            WeaponItem helicoidalWeapon,
            GripMode helicoidalGrip,
            WeaponCombatAction helicoidalAttack,
            WeaponItem opposingWeapon,
            GripMode opposingGrip,
            WeaponCombatAction opposingAttack,
            boolean attackHitboxesCollide
    ) {
        boolean succeeds = canMirrorParry(helicoidalWeapon, helicoidalGrip, helicoidalAttack,
                opposingWeapon, opposingGrip, opposingAttack, attackHitboxesCollide);
        return new ParryResolutionPolicy().resolve(opposingWeapon, succeeds);
    }

    public ParryResolution resolveMirrorParry(
            WeaponItem helicoidalWeapon, GripMode helicoidalGrip, WeaponCombatAction helicoidalAttack,
            WeaponItem opposingWeapon, GripMode opposingGrip, WeaponCombatAction opposingAttack,
            boolean attackHitboxesCollide, int dexterity, double recoilUnits
    ) {
        boolean succeeds = canMirrorParry(helicoidalWeapon, helicoidalGrip, helicoidalAttack,
                opposingWeapon, opposingGrip, opposingAttack, attackHitboxesCollide);
        return new ParryResolutionPolicy().resolve(opposingWeapon, succeeds, dexterity, recoilUnits);
    }

}
