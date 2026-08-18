package domain.ability;
/** Dos sostenidas mutuamente excluyentes cuyo efecto se dispara al vaciar PA. */
public final class MalignantEnergyRefinementPolicy {
 public enum Mode{KINETIC_EXPLOSION,POTENTIAL_HARDENING}
 public boolean canTrigger(double currentStamina){return currentStamina<=0.0;}
 public boolean mutuallyExclusive(Mode active,Mode requested){return active!=null&&active!=requested;}
}
