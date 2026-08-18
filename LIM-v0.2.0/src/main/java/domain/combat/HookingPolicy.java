package domain.combat;

import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;

import java.util.Objects;

/** ENGANCHAR: solo HEAVY; Hoz/Guadaña exigen perforante real y Boathook contundente real. */
public final class HookingPolicy {
    public HookResult resolve(WeaponItem weapon, WeaponCombatAction action,
                              double realPiercingDamage, double realBluntDamage,
                              double physicalStability) {
        Objects.requireNonNull(weapon); Objects.requireNonNull(action);
        if (!hasProperty(weapon, ItemPropertyId.HOOK) || action != WeaponCombatAction.HEAVY_ATTACK) {
            return HookResult.rejected("La acción no ejecuta ENGANCHAR.");
        }
        double relevant = weapon.hasTrait(WeaponTrait.HOOKS_WITH_BLUNT) ? realBluntDamage : realPiercingDamage;
        if (relevant <= 0) return HookResult.rejected("ENGANCHAR requiere daño real positivo.");
        double pull = weapon.reachMeters() * (2.0 / 3.0);
        double recoilUnits = Math.max(0.0, relevant - Math.max(0.0, physicalStability));
        return new HookResult(true, pull, StaggerPolicy.resolve(recoilUnits), "El objetivo es atraído hacia el usuario.");
    }

    private static boolean hasProperty(WeaponItem weapon, ItemPropertyId id) {
        return weapon.properties().stream().anyMatch(p -> p.id() == id);
    }
}
