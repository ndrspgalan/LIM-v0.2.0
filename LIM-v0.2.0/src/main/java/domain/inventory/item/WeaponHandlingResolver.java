package domain.inventory.item;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;

import java.util.Objects;
import java.util.Optional;

/**
 * Resuelve manos físicas, configuraciones efectivas y estado de manejo.
 * No interpreta entradas de jugador ni repertorios concretos de armas.
 */
public final class WeaponHandlingResolver {
    private WeaponHandlingResolver() {}

    public static ResolvedWeaponHandling resolve(EquipmentState equipment, boolean dualWielding) {
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Optional<WeaponItem> right = weaponAt(equipment, EquipmentSlot.RIGHT_HAND);
        Optional<WeaponItem> left = weaponAt(equipment, EquipmentSlot.LEFT_HAND);

        if (dualWielding) {
            WeaponItem rightWeapon = right.orElseThrow(() ->
                    new IllegalStateException("Dual wielding requiere un arma en la mano derecha."));
            WeaponItem leftWeapon = left.orElseThrow(() ->
                    new IllegalStateException("Dual wielding requiere un arma en la mano izquierda."));
            WeaponConfiguration rightConfiguration = DualWieldConfigurationPolicy.rightHandConfiguration(rightWeapon);
            WeaponConfiguration leftConfiguration = DualWieldConfigurationPolicy.leftHandConfiguration(leftWeapon);
            return new ResolvedWeaponHandling(
                    ResolvedHand.active(EquipmentSlot.RIGHT_HAND, rightWeapon, rightConfiguration),
                    ResolvedHand.active(EquipmentSlot.LEFT_HAND, leftWeapon, leftConfiguration),
                    WieldingState.DUAL_WIELD
            );
        }

        if (right.isPresent() && left.isPresent()) {
            throw new IllegalStateException(
                    "Dos armas activas requieren dual wielding o una disposición explícita de envainado.");
        }
        if (right.isPresent()) {
            WeaponItem weapon = right.get();
            return new ResolvedWeaponHandling(
                    ResolvedHand.active(EquipmentSlot.RIGHT_HAND, weapon, weapon.currentConfiguration()),
                    ResolvedHand.empty(EquipmentSlot.LEFT_HAND),
                    WieldingState.SINGLE_WIELD
            );
        }
        if (left.isPresent()) {
            WeaponItem weapon = left.get();
            return new ResolvedWeaponHandling(
                    ResolvedHand.empty(EquipmentSlot.RIGHT_HAND),
                    ResolvedHand.active(EquipmentSlot.LEFT_HAND, weapon, weapon.currentConfiguration()),
                    WieldingState.SINGLE_WIELD
            );
        }
        return new ResolvedWeaponHandling(
                ResolvedHand.empty(EquipmentSlot.RIGHT_HAND),
                ResolvedHand.empty(EquipmentSlot.LEFT_HAND),
                WieldingState.UNARMED
        );
    }

    /** manejo físico con hoja completa para ERGONOMÍA INTRINCADA. */
    public static ResolvedWeaponHandling resolve(EquipmentState equipment, boolean dualWielding, domain.character.sheet.CharacterSheet sheet) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Optional<WeaponItem> right = weaponAt(equipment, EquipmentSlot.RIGHT_HAND);
        Optional<WeaponItem> left = weaponAt(equipment, EquipmentSlot.LEFT_HAND);
        for (WeaponItem weapon : java.util.List.of(right.orElse(null), left.orElse(null))) {
            if (weapon == null) continue;
            if (weapon.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) {
                WeaponConfiguration cfg = weapon.currentConfiguration();
                int reqStr = WeaponRequirementPolicy.strengthRequirement(weapon.weightKg(), cfg.gripMode(), weapon.traits());
                int reqDex = WeaponRequirementPolicy.dexterityRequirementForGrip(weapon.reachMeters(), cfg.gripMode(), weapon.traits(), EquipmentSlot.RIGHT_HAND, false);
                if (sheet.valueOf(domain.character.sheet.Attribute.FUERZA) < reqStr || sheet.valueOf(domain.character.sheet.Attribute.DESTREZA) < reqDex) {
                    throw new IllegalStateException("Requisitos insuficientes para " + cfg.label() + " de " + weapon.name() + ": FUERZA " + reqStr + " / DESTREZA " + reqDex + ".");
                }
            }
            if (weapon.hasTrait(WeaponTrait.ERGONOMIA_INTRINCADA)
                    && !domain.combat.ShieldCombatPolicy.canWieldIntricateOneHanded(sheet, weapon)) {
                throw new IllegalStateException("FUERZA + AGUANTE no bastan para gobernar " + weapon.name() + " a una mano.");
            }
        }
        if (dualWielding && left.isPresent()) {
            int requiredDexterity = WeaponRequirementPolicy.dexterityRequirement(
                    left.get().reachMeters(), EquipmentSlot.LEFT_HAND, true);
            int availableDexterity = sheet.valueOf(domain.character.sheet.Attribute.DESTREZA);
            if (availableDexterity < requiredDexterity) {
                throw new IllegalStateException("La DESTREZA disponible no permite gobernar " + left.get().name()
                        + " en LEFT_HAND durante dual wielding; requiere " + requiredDexterity + ".");
            }
        }
        return resolve(equipment, dualWielding);
    }


    /**
     * Resolución de manejo con FUERZA efectiva. Si un arma admite 1H/2H pero no alcanza
     * el requisito a una mano, fuerza automáticamente una configuración a dos manos.
     */
    public static ResolvedWeaponHandling resolve(EquipmentState equipment, boolean dualWielding, int availableStrength) {
        if (availableStrength < 0) {
            throw new IllegalArgumentException("La FUERZA disponible no puede ser negativa.");
        }
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        Optional<WeaponItem> right = weaponAt(equipment, EquipmentSlot.RIGHT_HAND);
        Optional<WeaponItem> left = weaponAt(equipment, EquipmentSlot.LEFT_HAND);

        if (dualWielding) {
            WeaponItem rightWeapon = right.orElseThrow(() ->
                    new IllegalStateException("Dual wielding requiere un arma en la mano derecha."));
            WeaponItem leftWeapon = left.orElseThrow(() ->
                    new IllegalStateException("Dual wielding requiere un arma en la mano izquierda."));
            requireOneHandedStrength(rightWeapon, availableStrength);
            requireOneHandedStrength(leftWeapon, availableStrength);
            return resolve(equipment, true);
        }

        if (right.isPresent() && left.isPresent()) {
            throw new IllegalStateException(
                    "Dos armas activas requieren dual wielding o una disposición explícita de envainado.");
        }
        if (right.isEmpty() && left.isEmpty()) {
            return resolve(equipment, false);
        }

        WeaponItem weapon = right.orElseGet(left::get);
        WeaponGripEligibility eligibility = weapon.gripEligibilityForStrength(availableStrength);
        if (eligibility == WeaponGripEligibility.CANNOT_WIELD) {
            throw new IllegalStateException("La FUERZA disponible no permite empuñar " + weapon.name() + ".");
        }
        if (eligibility == WeaponGripEligibility.FORCED_TWO_HANDED
                || eligibility == WeaponGripEligibility.TWO_HANDED_ONLY) {
            WeaponConfiguration twoHanded = weapon.availableConfigurations().stream()
                    .filter(configuration -> configuration.gripMode() == GripMode.TWO_HANDED)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("El arma no dispone de configuración bimanual válida."));
            weapon.selectConfiguration(twoHanded);
        }
        return resolve(equipment, false);
    }

    private static void requireOneHandedStrength(WeaponItem weapon, int availableStrength) {
        if (!weapon.supportsOneHandedUse()) {
            throw new IllegalStateException("Dual wielding requiere armas compatibles con una mano.");
        }
        int requirement = WeaponRequirementPolicy.strengthRequirement(
                weapon.weightKg(), GripMode.ONE_HANDED, weapon.traits());
        if (availableStrength < requirement) {
            throw new IllegalStateException("La FUERZA disponible no permite empuñar " + weapon.name()
                    + " a una mano; no puede entrar en dual wielding.");
        }
    }

    private static Optional<WeaponItem> weaponAt(EquipmentState equipment, EquipmentSlot hand) {
        return equipment.itemAt(hand)
                .filter(WeaponItem.class::isInstance)
                .map(WeaponItem.class::cast)
                .filter(weapon -> !weapon.isSheathed());
    }
}
