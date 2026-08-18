package domain.inventory.item.firearms;
import java.util.Objects;
/** Resultado de entrada con duración física explícita cuando la acción consume tiempo de manipulación. */
public record FirearmActionResult(FirearmAction action,int shotsFired,boolean allowed,double durationSeconds,String reason){
 public FirearmActionResult{Objects.requireNonNull(action);Objects.requireNonNull(reason);if(shotsFired<0||!Double.isFinite(durationSeconds)||durationSeconds<0)throw new IllegalArgumentException("Resultado firearm inválido.");}
 public FirearmActionResult(FirearmAction action,int shotsFired,boolean allowed,String reason){this(action,shotsFired,allowed,0,reason);}
 public static FirearmActionResult allowed(FirearmAction action,String reason){return new FirearmActionResult(action,0,true,0,reason);}
 public static FirearmActionResult allowed(FirearmAction action,double duration,String reason){return new FirearmActionResult(action,0,true,duration,reason);}
 public static FirearmActionResult fired(int shots,String reason){return new FirearmActionResult(FirearmAction.FIRE,shots,true,0,reason);}
 public static FirearmActionResult blocked(String reason){return new FirearmActionResult(FirearmAction.NONE,0,false,0,reason);}
}
