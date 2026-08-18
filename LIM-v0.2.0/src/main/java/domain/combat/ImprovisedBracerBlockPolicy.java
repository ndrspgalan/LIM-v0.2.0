package domain.combat;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.GripMode;
import domain.inventory.item.WeaponActionMode;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.armor.ArmorPiece;

import java.util.Objects;
import java.util.Optional;

/**
 * Condiciones canónicas de activación de ESCUDO IMPROVISADO.
 * El brazal izquierdo actúa virtualmente en modo alternativo sin ocupar LEFT_HAND.
 */
public final class ImprovisedBracerBlockPolicy {
    public boolean canBlock(EquipmentState equipment) {
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Optional<ArmorPiece> bracers = equipment.armorAt(EquipmentSlot.BRACERS);
        if (bracers.isEmpty() || !bracers.get().supportsImprovisedBlock()) return false;

        Optional<WeaponItem> right = equipment.itemAt(EquipmentSlot.RIGHT_HAND)
                .filter(WeaponItem.class::isInstance)
                .map(WeaponItem.class::cast)
                .filter(weapon -> !weapon.isSheathed());
        if (right.isEmpty()) return false;

        WeaponItem weapon = right.get();
        if (weapon.hasTrait(WeaponTrait.SHIELD)) return false;
        if (weapon.currentConfiguration().gripMode() != GripMode.ONE_HANDED) return false;
        if (weapon.currentConfiguration().actionMode() != WeaponActionMode.PRIMARY) return false;

        return equipment.itemAt(EquipmentSlot.LEFT_HAND)
                .filter(WeaponItem.class::isInstance)
                .map(WeaponItem.class::cast)
                .map(WeaponItem::isSheathed)
                .orElse(true);
    }

    /** PARRY usa la misma elegibilidad de objetivo que el parry ordinario y MirrorParry. */
    public boolean canParry(EquipmentState equipment, WeaponItem opposingWeapon) {
        Objects.requireNonNull(opposingWeapon, "El arma rival no puede ser nula.");
        return canBlock(equipment) && new ParryTargetEligibilityPolicy().isEligible(opposingWeapon);
    }

    public ArmorPiece activeBracer(EquipmentState equipment) {
        if (!canBlock(equipment)) throw new IllegalStateException("ESCUDO IMPROVISADO no está disponible.");
        return equipment.armorAt(EquipmentSlot.BRACERS).orElseThrow();
    }
}
