package domain.inventory.item;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public record WeaponCombatPolicy(Set<WeaponCombatAction> allowedActions) {
    public WeaponCombatPolicy {
        Objects.requireNonNull(allowedActions, "Las acciones permitidas no pueden ser nulas.");
        if (allowedActions.isEmpty()) {
            throw new IllegalArgumentException("Un arma debe permitir al menos una acción de combate.");
        }
        allowedActions = Set.copyOf(allowedActions);
    }

    public static WeaponCombatPolicy unrestricted() {
        return new WeaponCombatPolicy(EnumSet.allOf(WeaponCombatAction.class));
    }

    /** Repertorio agregado; la separación ofensiva/defensiva se concreta por modo. */
    public static WeaponCombatPolicy shield() {
        return new WeaponCombatPolicy(EnumSet.of(
                WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.BLOCK,
                WeaponCombatAction.PARRY
        ));
    }

    /** DESARMADO bloquea con guardia alta; no posee CHARGED. */
    public static WeaponCombatPolicy unarmed() {
        return new WeaponCombatPolicy(EnumSet.of(
                WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.HEAVY_ATTACK,
                WeaponCombatAction.JUMP_ATTACK,
                WeaponCombatAction.DESTABILIZE,
                WeaponCombatAction.BLOCK
        ));
    }

    /** Armas cortas a una mano: sin fuerte ni cargado; conservan salto y desestabilización. */
    public static WeaponCombatPolicy dagger() {
        return new WeaponCombatPolicy(EnumSet.of(
                WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.JUMP_ATTACK,
                WeaponCombatAction.DESTABILIZE
        ));
    }

    public boolean allows(WeaponCombatAction action) {
        return allowedActions.contains(Objects.requireNonNull(action, "La acción no puede ser nula."));
    }
}
