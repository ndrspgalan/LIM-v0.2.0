package domain.combat.ai.encounter;

import domain.combat.ai.execution.CombatAction;
import java.util.Objects;

/** Intención observable; no es comunicación telepática ni orden de escuadrón. */
public record CombatIntentBroadcast(String actorId,String targetId,CombatAction action,boolean visible,boolean audible,double timeSeconds){
 public CombatIntentBroadcast{Objects.requireNonNull(actorId);targetId=targetId==null?"":targetId;Objects.requireNonNull(action);if(!Double.isFinite(timeSeconds)||timeSeconds<0)throw new IllegalArgumentException("Tiempo inválido.");}
 public boolean perceptible(boolean observerCanSee,boolean observerCanHear){return (visible&&observerCanSee)||(audible&&observerCanHear);}
}
