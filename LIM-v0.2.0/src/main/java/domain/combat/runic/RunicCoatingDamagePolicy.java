package domain.combat.runic;

import domain.combat.coating.WeaponCoatingType;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;

import java.util.Objects;

/** Deriva el canal maldito de la contundencia vigente del perfil que impacta. */
public final class RunicCoatingDamagePolicy {
    public double rawCurseDamage(WeaponItem weapon, WeaponMode impactMode, boolean attackConnected) {
        Objects.requireNonNull(weapon, "El arma no puede ser nula.");
        Objects.requireNonNull(impactMode, "El perfil de impacto no puede ser nulo.");
        if (!attackConnected) return 0.0;
        if (weapon.coating().filter(c -> c.type() == WeaponCoatingType.CURSE).isEmpty()) return 0.0;
        return weapon.currentBluntLethality(impactMode);
    }

    public ImpactOrigin origin() { return ImpactOrigin.RUNIC_COATING; }
}
