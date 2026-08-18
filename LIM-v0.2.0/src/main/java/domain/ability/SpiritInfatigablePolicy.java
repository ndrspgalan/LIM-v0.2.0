package domain.ability;
import domain.movement.LocomotionMode;
/** Fuera de combate, ESPÍRITU INFATIGABLE anula cualquier gasto de PA, incluidas maestrías sostenidas. */
public final class SpiritInfatigablePolicy {
 private SpiritInfatigablePolicy(){}
 public static double globalStaminaCost(double ordinaryCost,boolean masteryUnlocked,boolean inCombat){if(!Double.isFinite(ordinaryCost)||ordinaryCost<0)throw new IllegalArgumentException("El coste no puede ser negativo.");return masteryUnlocked&&!inCombat?0.0:ordinaryCost;}
 public static double staminaCost(double ordinaryCost,boolean masteryUnlocked,boolean inCombat,LocomotionMode mode){return globalStaminaCost(ordinaryCost,masteryUnlocked,inCombat);}
 public static double swimmingStaminaCost(double ordinaryCost,boolean masteryUnlocked,boolean inCombat){return globalStaminaCost(ordinaryCost,masteryUnlocked,inCombat);}
}
