package domain.combat.moveset;

import domain.inventory.item.WeaponCombatAction;
import java.util.*;

/** Grafo ofensivo interno: movimientos concretos y transiciones que preservan momento/postura. */
public record MeleeMovesetProfile(List<MeleeAttackMotion> motions,List<MeleeAttackTransition> transitions){
    public MeleeMovesetProfile{
        motions=List.copyOf(Objects.requireNonNull(motions)); transitions=List.copyOf(Objects.requireNonNull(transitions));
        if(motions.isEmpty())throw new IllegalArgumentException("Un moveset requiere movimientos.");
        Set<String> ids=new HashSet<>(); for(var m:motions)if(!ids.add(m.id()))throw new IllegalArgumentException("Movimiento duplicado: "+m.id());
        for(var t:transitions)if(!ids.contains(t.fromId())||!ids.contains(t.toId()))throw new IllegalArgumentException("Transición referencia movimiento inexistente.");
    }
    public int lightAttackCount(){return (int)motions.stream().filter(m->m.action()==WeaponCombatAction.LIGHT_ATTACK).count();}
    public Optional<MeleeAttackMotion> motion(String id){return motions.stream().filter(m->m.id().equals(id)).findFirst();}
    public Optional<MeleeAttackTransition> transition(String from,String to){return transitions.stream().filter(t->t.fromId().equals(from)&&t.toId().equals(to)).findFirst();}
}
