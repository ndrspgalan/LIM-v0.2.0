package domain.combat;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;
import domain.inventory.item.WeaponTrait;

import java.util.Objects;

/**
 * Fuente única de impacto físico basal de un WeaponItem cuerpo a cuerpo.
 * DE_ROTOR y ERGONOMIA_SUFICIENTE convierten cada kilogramo de masa propia
 * en +1 punto de contundente.
 * DESARMADO no pasa por esta bonificación: su factory ya construye canónicamente
 * la letalidad como FUERZA + masa ofensiva equivalente y volver a sumar aquí duplicaría la masa.
 */
public final class MeleeWeaponImpactPolicy {
    private MeleeWeaponImpactPolicy() {}

    public static PhysicalDamage baseImpact(WeaponItem weapon, WeaponMode mode) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        Objects.requireNonNull(mode, "El modo no puede ser nulo.");
        LethalityProfile current = weapon.currentLethality(mode);
        boolean massCoupled = weapon.hasTrait(WeaponTrait.DE_ROTOR)
                || weapon.hasTrait(WeaponTrait.ERGONOMIA_SUFICIENTE);
        double massBonus = massCoupled ? weapon.weightKg() : 0.0;
        return new PhysicalDamage(current.piercing(), current.slashing(), current.blunt() + massBonus);
    }
}
