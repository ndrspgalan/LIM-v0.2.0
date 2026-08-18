package domain.combat.runic;

import domain.ability.AttackKind;
import domain.inventory.item.WeaponItem;
import java.util.Objects;

/** Firma canónica de Resonancia: tipo, ordinal de combo e instancia de arma. */
public record AttackSignature(AttackKind attackKind, int comboOrdinal, WeaponIdentity weaponIdentity) {
    public AttackSignature {
        Objects.requireNonNull(attackKind, "El tipo de ataque no puede ser nulo.");
        Objects.requireNonNull(weaponIdentity, "La identidad del arma no puede ser nula.");
        if (comboOrdinal < 1) throw new IllegalArgumentException("La posición del combo debe ser positiva.");
        if (attackKind != AttackKind.LIGHT && attackKind != AttackKind.HEAVY && comboOrdinal != 1) {
            throw new IllegalArgumentException("Los ataques cargado, con salto y desestabilizador tienen ordinal 1.");
        }
    }
    public static AttackSignature of(AttackKind kind, int ordinal, WeaponItem weapon) {
        return new AttackSignature(kind, ordinal, WeaponIdentity.of(weapon));
    }
}
