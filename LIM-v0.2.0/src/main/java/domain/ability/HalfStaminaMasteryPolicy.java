package domain.ability;
/** Compatibilidad : REFINAMIENTO ya no consume el 50 % de PA al activarse. */
public final class HalfStaminaMasteryPolicy {
    private HalfStaminaMasteryPolicy() {}
    public static boolean applies(String manifestationName){return false;}
    public static double cost(double maximumStamina){return 0.0;}
    public static boolean canUse(String manifestationName,double currentStamina,double maximumStamina){return false;}
    public static double staminaAfterUse(String manifestationName,double currentStamina,double maximumStamina){return currentStamina;}
}
