package domain.combat.ai.loadout;

import domain.inventory.item.ResolvedWeaponHandling;
import domain.inventory.item.WeaponConfiguration;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponMode;

import java.util.Objects;

/** Loadout de IA resuelto sobre manos físicas y una pieza atacante concreta. */
public record ResolvedCombatLoadout(
        ResolvedWeaponHandling handling,
        WeaponItem attackingWeapon,
        WeaponConfiguration attackingConfiguration,
        WeaponMode lethalityMode
) {
    public ResolvedCombatLoadout {
        handling = Objects.requireNonNull(handling, "El manejo resuelto no puede ser nulo.");
        attackingWeapon = Objects.requireNonNull(attackingWeapon, "El arma atacante no puede ser nula.");
        attackingConfiguration = Objects.requireNonNull(attackingConfiguration,
                "La configuración atacante no puede ser nula.");
        lethalityMode = Objects.requireNonNull(lethalityMode, "El perfil de letalidad no puede ser nulo.");
    }

    public boolean dualWielding() {
        return handling.wieldingState() == domain.inventory.item.WieldingState.DUAL_WIELD;
    }

    public double reachMeters() {
        double equipped = Math.max(
                handling.rightHand().weapon().map(WeaponItem::reachMeters).orElse(0.0),
                handling.leftHand().weapon().map(WeaponItem::reachMeters).orElse(0.0)
        );
        return equipped > 0.0 ? equipped : attackingWeapon.reachMeters();
    }
}
