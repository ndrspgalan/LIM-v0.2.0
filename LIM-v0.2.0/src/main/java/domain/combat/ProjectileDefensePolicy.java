package domain.combat;

import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import domain.inventory.equipment.EquipmentState;
import domain.ability.AuraPulsionProjectilePolicy;
import domain.ability.MasteryEffectRegistry;

import java.util.Objects;

public final class ProjectileDefensePolicy {
    public boolean canBlock(WeaponItem item) {
        Objects.requireNonNull(item);
        return item.hasTrait(WeaponTrait.SHIELD) && item.allowsCombatAction(WeaponCombatAction.BLOCK);
    }

    public boolean canBlock(EquipmentState equipment) {
        return new ImprovisedBracerBlockPolicy().canBlock(equipment);
    }

    /** AURA DE PULSIÓN ya no concede mitigación de proyectiles. */
    public PhysicalDamage mitigateWithAura(PhysicalDamage grossProjectileDamage, MasteryEffectRegistry effects) {
        Objects.requireNonNull(effects, "El registro de maestrías no puede ser nulo.");
        return Objects.requireNonNull(grossProjectileDamage, "El daño bruto no puede ser nulo.");
    }

    public boolean canParry(WeaponItem item) {
        Objects.requireNonNull(item);
        return item.allowsCombatAction(WeaponCombatAction.PARRY);
    }
}
