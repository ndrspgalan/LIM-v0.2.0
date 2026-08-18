package domain.inventory.item.meleeWeapons.special;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import domain.inventory.item.WeaponCombatAction;
import domain.inventory.item.misc.ResinJarItem;
import domain.inventory.item.misc.UtilityObjectItem;
import java.util.Objects;

/** Mecánica térmica: Amadou + Resina preparan 300 s acumulados; desenfundar prende y envainar apaga. */
public final class ThermoMechanicalKatanaPolicy {
    public static final int BURN_DAMAGE = 67;
    public static final int AGGRESSIVE_DRAW_BURN_DAMAGE = 100;

    public boolean prepare(WeaponItem katana, ThermoMechanicalKatanaState state,
                           UtilityObjectItem amadou, ResinJarItem resin) {
        validateKatana(katana); Objects.requireNonNull(state); Objects.requireNonNull(amadou); Objects.requireNonNull(resin);
        if (!"Amadou".equals(amadou.name()) || amadou.isDepleted() || resin.isDepleted()) return false;
        if (!amadou.consumeOne()) return false;
        if (!resin.consumeOne()) { amadou.addUnits(1); return false; }
        state.refill();
        return true;
    }

    public void draw(WeaponItem katana, ThermoMechanicalKatanaState state) {
        validateKatana(katana); Objects.requireNonNull(state).draw();
    }

    public void sheath(WeaponItem katana, ThermoMechanicalKatanaState state) {
        validateKatana(katana); Objects.requireNonNull(state).sheath();
    }

    public void advanceRealTime(WeaponItem katana, ThermoMechanicalKatanaState state, double seconds) {
        validateKatana(katana); Objects.requireNonNull(state).advance(seconds);
    }

    public int additionalBurnDamage(WeaponItem katana, ThermoMechanicalKatanaState state) {
        validateKatana(katana); return Objects.requireNonNull(state).burning() ? BURN_DAMAGE : 0;
    }

    /**
     * HEAVY y CHARGED son el mismo desenvaine agresivo : si existe carga térmica preparada,
     * la ignición súbita del desenvaine eleva Quemadura 67 a 100 para ese impacto concreto.
     */
    public int additionalBurnDamageForAttack(
            WeaponItem katana, ThermoMechanicalKatanaState state, WeaponCombatAction action) {
        validateKatana(katana); Objects.requireNonNull(state); Objects.requireNonNull(action);
        if ((action == WeaponCombatAction.HEAVY_ATTACK || action == WeaponCombatAction.CHARGED_ATTACK)
                && state.prepared()) return AGGRESSIVE_DRAW_BURN_DAMAGE;
        return state.burning() ? BURN_DAMAGE : 0;
    }


    /** Ejecuta el desenvaine ofensivo de H1/C1 y deja la Katana físicamente desenvainada. */
    public int performAggressiveDrawAttack(
            WeaponItem katana, ThermoMechanicalKatanaState state, WeaponCombatAction action) {
        validateKatana(katana); Objects.requireNonNull(state); Objects.requireNonNull(action);
        if (action != WeaponCombatAction.HEAVY_ATTACK && action != WeaponCombatAction.CHARGED_ATTACK) {
            throw new IllegalArgumentException("El desenvaine agresivo sólo pertenece a HEAVY o CHARGED.");
        }
        boolean prepared = state.prepared();
        state.draw();
        return prepared ? AGGRESSIVE_DRAW_BURN_DAMAGE : 0;
    }

    private static void validateKatana(WeaponItem katana) {
        Objects.requireNonNull(katana, "La katana no puede ser nula.");
        if (!katana.hasTrait(WeaponTrait.THERMO_MECHANICAL)) {
            throw new IllegalArgumentException("El arma no dispone de mecanismo termo-mecánico.");
        }
    }
}
