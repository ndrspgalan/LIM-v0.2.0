package domain.combat;

import domain.ability.AttackKind;
import domain.ability.MasteryMath;
import domain.ability.MasteryEffectRegistry;
import domain.ability.CharacterMasteryCollection;
import domain.ability.PulsionCombatPolicy;
import domain.character.sheet.CharacterSheet;
import domain.character.sheet.Attribute;
import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import domain.inventory.InventoryEntry;
import domain.inventory.item.misc.StackableMiscellaneousItem;
import domain.inventory.item.throwingWeapons.ThrowingWeaponItem;

import java.util.Objects;

/** Coste ofensivo universal: masa real del arma/objeto por multiplicador energético de acción. */
public final class CombatStaminaCostPolicy {

    public double cost(WeaponItem attackingWeapon, WeaponCombatAction action) {
        return cost(attackingWeapon, action, false);
    }

    /**
     * : un finisher LIGHT legítimo cuesta siempre masa x1,11, incluso cuando
     * TRAYECTORIA CONVERGENTE eleva su daño a x1,40. El H1 de la Maza que sustituye L4
     * usa también este coste de finisher, porque energéticamente se resuelve como LIGHT.
     */
    public double cost(WeaponItem attackingWeapon, WeaponCombatAction action, boolean lightComboFinisherResolution) {
        Objects.requireNonNull(attackingWeapon, "El arma atacante no puede ser nula.");
        Objects.requireNonNull(action, "La acción no puede ser nula.");
        double effectiveMass = attackingWeapon.weightKg();
        double multiplier = staminaMultiplier(attackingWeapon, action, lightComboFinisherResolution);
        return effectiveMass * multiplier;
    }

    /** Compatibilidad: PULSIÓN/AURA son pasivas y ya no imponen sobrecoste ofensivo global. */
    public double cost(WeaponItem attackingWeapon, WeaponCombatAction action, MasteryEffectRegistry effects) {
        Objects.requireNonNull(effects, "El registro de maestrías no puede ser nulo.");
        return cost(attackingWeapon, action, false);
    }

    /** Forma compacta: el multiplicador PULSIÓN ya no altera el coste ofensivo global. */
    public double cost(WeaponItem attackingWeapon, WeaponCombatAction action, double ignoredPulsionMultiplier) {
        return cost(attackingWeapon, action, false);
    }

    public double feintCost(CharacterSheet sheet, CharacterMasteryCollection masteries) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(masteries, "Las maestrías no pueden ser nulas.");
        boolean recycling = masteries.isPassiveActive("RECICLAJE DE PULSIÓN", sheet);
        return new PulsionCombatPolicy().feintStaminaCost(sheet.valueOf(Attribute.AGUANTE), recycling);
    }

    /** Coste remoto/arrojadizo basal: 1 PA por kg de masa realmente movilizada. */
    public double remoteUseCost(InventoryEntry item) {
        Objects.requireNonNull(item, "El objeto no puede ser nulo.");
        double mass = effectiveActionMassKg(item);
        if (!Double.isFinite(mass) || mass < 0) throw new IllegalArgumentException("Masa ofensiva inválida.");
        return mass;
    }

    /** En stacks arrojadizos se usa la masa de una unidad, nunca la masa del stack completo. */
    public double effectiveActionMassKg(InventoryEntry item) {
        Objects.requireNonNull(item, "El objeto no puede ser nulo.");
        if (item instanceof ThrowingWeaponItem thrown) return thrown.unitWeightKg();
        if (item instanceof StackableMiscellaneousItem stack && stack.contentWeightPerUseKg() > 0) return stack.contentWeightPerUseKg();
        return item.weightKg();
    }

    public double cost(double attackingWeaponWeightKg, WeaponCombatAction action) {
        return cost(attackingWeaponWeightKg, action, false);
    }

    public double cost(double attackingWeaponWeightKg, WeaponCombatAction action, boolean lightComboFinisherResolution) {
        Objects.requireNonNull(action, "La acción no puede ser nula.");
        if (!Double.isFinite(attackingWeaponWeightKg) || attackingWeaponWeightKg < 0) {
            throw new IllegalArgumentException("Masa ofensiva inválida.");
        }
        double multiplier = lightComboFinisherResolution
                ? LightComboFinisherPolicy.staminaMultiplier()
                : attackKind(action).staminaMultiplier();
        return attackingWeaponWeightKg * multiplier;
    }

    /** Forma compacta: PULSIÓN ya no altera el coste ofensivo global. */
    public double cost(double attackingWeaponWeightKg, WeaponCombatAction action, double ignoredPulsionMultiplier) {
        return cost(attackingWeaponWeightKg, action, false);
    }

    public double staminaMultiplier(WeaponItem weapon, WeaponCombatAction action, boolean lightComboFinisherResolution) {
        Objects.requireNonNull(weapon, "El arma atacante no puede ser nula.");
        Objects.requireNonNull(action, "La acción no puede ser nula.");
        if (lightComboFinisherResolution) return LightComboFinisherPolicy.staminaMultiplier();
        if (action == WeaponCombatAction.HEAVY_ATTACK && weapon.hasTrait(WeaponTrait.ELECTRO_MECHANICAL_HEAVY)) {
            return HeavyAttackImpactPolicy.ELECTRO_MECHANICAL_ONE_HANDED_BLUNT_MULTIPLIER;
        }
        if (action == WeaponCombatAction.CHARGED_ATTACK && weapon.hasTrait(WeaponTrait.THERMO_MECHANICAL)) {
            return ChargedAttackImpactPolicy.THERMO_MECHANICAL_DRAW_MULTIPLIER;
        }
        return attackKind(action).staminaMultiplier();
    }

    private static AttackKind attackKind(WeaponCombatAction action) {
        return switch (action) {
            case LIGHT_ATTACK -> AttackKind.LIGHT;
            case HEAVY_ATTACK -> AttackKind.HEAVY;
            case CHARGED_ATTACK -> AttackKind.CHARGED;
            case JUMP_ATTACK -> AttackKind.JUMP;
            case DESTABILIZE -> AttackKind.DESTABILIZE;
            default -> throw new IllegalArgumentException("La acción no es un ataque con coste por masa: " + action);
        };
    }
}
