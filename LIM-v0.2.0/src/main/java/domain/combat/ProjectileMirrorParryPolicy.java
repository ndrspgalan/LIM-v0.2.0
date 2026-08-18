package domain.combat;

import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.firearms.FirearmItem;
import domain.inventory.item.firearms.PneumaticFirearmItem;

import java.util.Objects;
import java.util.Set;

/**
 * Desvío automático de proyectiles por la Espada Helicoidal.
 *  restringe la bonificación balística al nicho físicamente defendible del .46 de plomo neumático:
 * no es un bloqueo frontal universal, sino una intercepción oblicua durante una hitbox ofensiva activa.
 */
public final class ProjectileMirrorParryPolicy {
    private static final Set<WeaponCombatAction> OFFENSIVE = Set.of(
            WeaponCombatAction.LIGHT_ATTACK,
            WeaponCombatAction.HEAVY_ATTACK,
            WeaponCombatAction.CHARGED_ATTACK,
            WeaponCombatAction.JUMP_ATTACK
    );
    public static final double LEAD_46_DEFLECTION_DEGREES = 35.0;

    public ProjectileMirrorParryResult resolve(
            WeaponItem helicalWeapon,
            WeaponCombatAction activeAttack,
            FirearmItem projectileSource,
            boolean projectileIntersectsActiveBladeHitbox
    ) {
        Objects.requireNonNull(helicalWeapon, "El arma helicoidal no puede ser nula.");
        Objects.requireNonNull(activeAttack, "El ataque activo no puede ser nulo.");
        Objects.requireNonNull(projectileSource, "La fuente del proyectil no puede ser nula.");

        boolean eligibleProjectile = projectileSource instanceof PneumaticFirearmItem
                && ".46".equals(projectileSource.caliber())
                && "Plomo".equalsIgnoreCase(projectileSource.cartridgeDefinition().material());
        boolean success = helicalWeapon.hasTrait(WeaponTrait.HELICOIDAL_CONTROL)
                && OFFENSIVE.contains(activeAttack)
                && eligibleProjectile
                && projectileIntersectsActiveBladeHitbox;
        if (!success) return ProjectileMirrorParryResult.rejected();
        return new ProjectileMirrorParryResult(true, true, false, LEAD_46_DEFLECTION_DEGREES);
    }

}
