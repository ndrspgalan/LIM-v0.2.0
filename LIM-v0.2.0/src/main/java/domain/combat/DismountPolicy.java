package domain.combat;

import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;
import java.util.Objects;
import java.util.Set;

/** DESMONTAR del Boathook: LIGHT/JUMP/DESTABILIZE + contundente real sobre objetivo montado. */
public final class DismountPolicy {
    private static final Set<WeaponCombatAction> VALID = Set.of(
            WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.JUMP_ATTACK, WeaponCombatAction.DESTABILIZE);

    public DismountResult resolve(WeaponItem weapon, WeaponCombatAction action, boolean targetMounted,
                                  double realBluntDamage, double physicalStability) {
        Objects.requireNonNull(weapon); Objects.requireNonNull(action);
        boolean has = weapon.properties().stream().anyMatch(p -> p.id() == ItemPropertyId.DISMOUNT);
        if (!has || !VALID.contains(action)) return DismountResult.rejected("La acción no aplica DESMONTAR.");
        if (!targetMounted) return DismountResult.rejected("El objetivo no está montado.");
        if (realBluntDamage <= 0) return DismountResult.rejected("DESMONTAR requiere daño contundente real.");
        double recoilUnits = Math.max(0.0, realBluntDamage - Math.max(0.0, physicalStability));
        return new DismountResult(true, StaggerPolicy.resolve(recoilUnits), "El objetivo cae de su Transporte Personal.");
    }
}
