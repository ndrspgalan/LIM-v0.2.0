package domain.combat;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;
import domain.inventory.item.armor.ArmorMaterialClass;
import domain.inventory.item.armor.ArmorPiece;

import java.util.Objects;

/** el desgaste del arma depende de colisionar físicamente con una capa HEAVY. */
public final class WeaponDurabilityResolver {
    public WeaponProfileWearResult resolveMeleeHeavyImpact(WeaponItem weapon, WeaponMode mode, ArmorPiece armor) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        Objects.requireNonNull(mode, "El modo no puede ser nulo.");
        Objects.requireNonNull(armor, "La armadura no puede ser nula.");
        if (armor.materialClass() != ArmorMaterialClass.HEAVY) return new WeaponProfileWearResult(0, 0, 0);
        return weapon.applyHeavyArmorWear(mode, armor.currentProtection());
    }

    public boolean resolveUnarmedImpact(double finalInflictedDamage) {
        if (finalInflictedDamage < 0) throw new IllegalArgumentException("El daño final no puede ser negativo.");
        return false;
    }
}
