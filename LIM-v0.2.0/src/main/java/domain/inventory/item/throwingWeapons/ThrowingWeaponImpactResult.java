package domain.inventory.item.throwingWeapons;

import domain.combat.PhysicalDamage;
import domain.combat.StaggerResult;

import java.util.Objects;

/** Consecuencias completas de una unidad arrojadiza tras resolver primero su impacto cinético común. */
public record ThrowingWeaponImpactResult(
        PhysicalDamage physicalDamage,
        double poisonDamage,
        double burnDamage,
        boolean virulentToxicityActivated,
        boolean suffocatingBurnActivated,
        double staminaBefore,
        double staminaAfter,
        StaggerResult stagger,
        boolean coupDeGrace,
        double healthRegenerationInhibitionSeconds
) {
    public ThrowingWeaponImpactResult {
        physicalDamage = Objects.requireNonNull(physicalDamage, "El daño físico no puede ser nulo.");
        stagger = Objects.requireNonNull(stagger, "El aturdimiento no puede ser nulo.");
        if (poisonDamage < 0 || burnDamage < 0 || staminaBefore < 0 || staminaAfter < 0 || staminaAfter > staminaBefore
                || !Double.isFinite(healthRegenerationInhibitionSeconds) || healthRegenerationInhibitionSeconds < 0) {
            throw new IllegalArgumentException("Los valores del impacto arrojadizo no son válidos.");
        }
    }

    public double staminaDrained() { return staminaBefore - staminaAfter; }
}
