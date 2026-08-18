package domain.combat.moveset;

import domain.inventory.item.WeaponCombatAction;
import java.util.Objects;

/** Descripción interna de una trayectoria ofensiva; no forma parte de la narrativa visible del arma. */
public record MeleeAttackMotion(
        String id,
        WeaponCombatAction action,
        int lightOrdinal,
        String startState,
        String trajectory,
        String contactSurface,
        String endState,
        BodyAdvance bodyAdvance
) {
    public MeleeAttackMotion {
        if(id==null||id.isBlank()) throw new IllegalArgumentException("El id del movimiento es obligatorio.");
        Objects.requireNonNull(action); Objects.requireNonNull(bodyAdvance);
        if(action==WeaponCombatAction.LIGHT_ATTACK && lightOrdinal<1) throw new IllegalArgumentException("Un ligero requiere ordinal >=1.");
        if(action!=WeaponCombatAction.LIGHT_ATTACK && lightOrdinal!=0) throw new IllegalArgumentException("Sólo LIGHT usa ordinal.");
        startState=require(startState); trajectory=require(trajectory); contactSurface=require(contactSurface); endState=require(endState);
    }
    private static String require(String s){if(s==null||s.isBlank())throw new IllegalArgumentException("La descripción cinética es obligatoria.");return s;}
}
