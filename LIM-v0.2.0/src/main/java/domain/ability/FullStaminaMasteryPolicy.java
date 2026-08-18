package domain.ability;

/**
 * Las manifestaciones cinéticas dejaron de exigir PA completos.
 */
public final class FullStaminaMasteryPolicy {
    private static final double EPSILON = 1.0e-9;

    private FullStaminaMasteryPolicy() {}

    public static boolean requiresFullStamina(String manifestationName) {
        return false; // Explosión Cinética y Endurecimiento Toroidal usan HalfStaminaMasteryPolicy.
    }

    public static boolean canUse(String manifestationName, double currentStamina, double maximumStamina) {
        validate(currentStamina, maximumStamina);
        if (!requiresFullStamina(manifestationName)) return true;
        return Math.abs(currentStamina - maximumStamina) <= EPSILON;
    }

    /** Devuelve los PA restantes tras ejecutar la manifestación. */
    public static double staminaAfterUse(String manifestationName, double currentStamina, double maximumStamina) {
        validate(currentStamina, maximumStamina);
        if (!canUse(manifestationName, currentStamina, maximumStamina)) {
            throw new IllegalStateException(manifestationName + " solo puede usarse con la barra de PA completa.");
        }
        return requiresFullStamina(manifestationName) ? 0.0 : currentStamina;
    }

    private static void validate(double currentStamina, double maximumStamina) {
        if (!Double.isFinite(currentStamina) || !Double.isFinite(maximumStamina)) {
            throw new IllegalArgumentException("Los PA deben ser finitos.");
        }
        if (maximumStamina <= 0.0) throw new IllegalArgumentException("Los PA máximos deben ser positivos.");
        if (currentStamina < 0.0 || currentStamina - maximumStamina > EPSILON) {
            throw new IllegalArgumentException("Los PA actuales deben estar entre 0 y los PA máximos.");
        }
    }
}
