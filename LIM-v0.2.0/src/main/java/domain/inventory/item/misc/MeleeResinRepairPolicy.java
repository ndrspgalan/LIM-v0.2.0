package domain.inventory.item.misc;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import java.util.Objects;

/** Reparación con Resina para armas de madera que la declaren explícitamente (Horca). */
public final class MeleeResinRepairPolicy {
    public boolean repair(ResinJarItem resin, WeaponItem weapon) {
        Objects.requireNonNull(resin); Objects.requireNonNull(weapon);
        if (!weapon.hasTrait(WeaponTrait.RESIN_REPAIR) || resin.isDepleted()) return false;
        if (!weapon.restoreAllBluntLethality()) return false;
        resin.consumeOne();
        return true;
    }
}
