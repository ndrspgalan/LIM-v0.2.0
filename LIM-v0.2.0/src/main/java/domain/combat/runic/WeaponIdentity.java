package domain.combat.runic;

import domain.inventory.item.WeaponItem;
import java.util.Objects;

/** Identidad por instancia: no cambia al alternar ranura, agarre o modo. */
public final class WeaponIdentity {
    private final WeaponItem weapon;
    private WeaponIdentity(WeaponItem weapon) { this.weapon = Objects.requireNonNull(weapon); }
    public static WeaponIdentity of(WeaponItem weapon) { return new WeaponIdentity(weapon); }
    public WeaponItem weapon() { return weapon; }
    @Override public boolean equals(Object other) {
        return this == other || other instanceof WeaponIdentity identity && weapon == identity.weapon;
    }
    @Override public int hashCode() { return System.identityHashCode(weapon); }
}
