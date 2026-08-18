package domain.combat;

import domain.ability.AttackKind;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;
import domain.inventory.item.WeaponTrait;

import java.util.Objects;

/**
 * Impacto del ataque fuerte.
 * - HEAVY ordinario: x1,20 contundente.
 * - DE_ROTOR: x1,40 contundente.
 * - Maza Electro-mecánica H1 normal: x1,11 contundente.
 * - Maza H1 sustituyendo legítimamente al L4 finisher: usa el multiplicador de finisher
 *   (x1,11 normal / x1,40 con Trayectoria Convergente), nunca se suma con x1,11 HEAVY.
 */
public final class HeavyAttackImpactPolicy {
    public static final double STANDARD_BLUNT_MULTIPLIER = AttackKind.HEAVY.bluntMultiplier();
    public static final double ROTOR_BLUNT_MULTIPLIER = 1.40;
    public static final double ELECTRO_MECHANICAL_ONE_HANDED_BLUNT_MULTIPLIER = 1.11;

    public PhysicalDamage resolve(WeaponItem weapon, WeaponMode mode) {
        return resolve(weapon, mode, false, false);
    }

    public PhysicalDamage resolve(
            WeaponItem weapon,
            WeaponMode mode,
            boolean substitutesLightComboFinisher,
            boolean convergentTrajectoryUnlocked
    ) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        Objects.requireNonNull(mode, "El modo no puede ser nulo.");
        PhysicalDamage base = MeleeWeaponImpactPolicy.baseImpact(weapon, mode);

        double multiplier;
        if (weapon.hasTrait(WeaponTrait.DE_ROTOR)) {
            multiplier = ROTOR_BLUNT_MULTIPLIER;
        } else if (weapon.hasTrait(WeaponTrait.ELECTRO_MECHANICAL_HEAVY)) {
            multiplier = substitutesLightComboFinisher
                    ? LightComboFinisherPolicy.offensiveMultiplier(convergentTrajectoryUnlocked)
                    : ELECTRO_MECHANICAL_ONE_HANDED_BLUNT_MULTIPLIER;
        } else {
            multiplier = STANDARD_BLUNT_MULTIPLIER;
        }
        return new PhysicalDamage(base.piercing(), base.slashing(), base.blunt() * multiplier);
    }

    public boolean canSubstituteLightComboFinisher(WeaponItem weapon) {
        return Objects.requireNonNull(weapon).hasTrait(WeaponTrait.ELECTRO_MECHANICAL_HEAVY);
    }
}
