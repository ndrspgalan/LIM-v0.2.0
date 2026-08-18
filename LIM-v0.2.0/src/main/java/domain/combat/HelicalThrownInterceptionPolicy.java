package domain.combat;

import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.misc.CurrencyStack;
import domain.inventory.item.throwingWeapons.*;
import java.util.Objects;
import java.util.Set;

/**  — intercepción no balística de la Espada Helicoidal. */
public final class HelicalThrownInterceptionPolicy {
    private static final Set<WeaponCombatAction> OFFENSIVE = Set.of(
            WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.HEAVY_ATTACK,
            WeaponCombatAction.CHARGED_ATTACK, WeaponCombatAction.JUMP_ATTACK);

    private boolean active(WeaponItem helical, WeaponCombatAction attack, boolean intersects) {
        return helical != null && helical.hasTrait(WeaponTrait.HELICOIDAL_CONTROL)
                && OFFENSIVE.contains(attack) && intersects;
    }

    public HelicalThrownInterceptionResult resolve(WeaponItem helical, WeaponCombatAction attack,
                                                    ProjectileAmmunitionItem projectile, boolean intersects) {
        Objects.requireNonNull(projectile);
        if (!active(helical, attack, intersects)) return HelicalThrownInterceptionResult.NOT_ELIGIBLE;
        var family = projectile.ammunitionDescriptor().family();
        return (family == AmmunitionFamily.PEBBLE || family == AmmunitionFamily.ARROW)
                ? HelicalThrownInterceptionResult.DEFLECTED : HelicalThrownInterceptionResult.NOT_ELIGIBLE;
    }

    public HelicalThrownInterceptionResult resolve(WeaponItem helical, WeaponCombatAction attack,
                                                    CurrencyStack coin, boolean intersects) {
        Objects.requireNonNull(coin);
        return active(helical, attack, intersects) ? HelicalThrownInterceptionResult.DEFLECTED
                : HelicalThrownInterceptionResult.NOT_ELIGIBLE;
    }

    public HelicalThrownInterceptionResult resolve(WeaponItem helical, WeaponCombatAction attack,
                                                    ThrowingWeaponItem thrown, boolean intersects) {
        Objects.requireNonNull(thrown);
        if (!active(helical, attack, intersects)) return HelicalThrownInterceptionResult.NOT_ELIGIBLE;
        return switch (thrown.effect()) {
            case THROWING_KNIFE -> HelicalThrownInterceptionResult.DEFLECTED;
            case AMMONIA_CAPSULE, INCENDIARY_TERRACOTTA, PHOSPHORUS_SULFUR_EGG -> HelicalThrownInterceptionResult.DETONATES_ON_BLADE;
        };
    }
}
