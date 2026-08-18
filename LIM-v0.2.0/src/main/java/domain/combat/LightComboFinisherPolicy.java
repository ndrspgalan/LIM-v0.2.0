package domain.combat;

/**
 *  — separa la recompensa ofensiva del remate LIGHT de su coste energético.
 * El remate ordinario usa x1,11 en ambos canales. TRAYECTORIA CONVERGENTE eleva
 * exclusivamente el multiplicador ofensivo a x1,40; el coste permanece x1,11.
 */
public final class LightComboFinisherPolicy {
    public static final double STANDARD_OFFENSIVE_MULTIPLIER = 1.11;
    public static final double CONVERGENT_OFFENSIVE_MULTIPLIER = 1.40;
    public static final double STAMINA_MULTIPLIER = 1.11;

    private LightComboFinisherPolicy() {}

    public static double offensiveMultiplier(boolean convergentTrajectoryUnlocked) {
        return convergentTrajectoryUnlocked
                ? CONVERGENT_OFFENSIVE_MULTIPLIER
                : STANDARD_OFFENSIVE_MULTIPLIER;
    }

    public static double staminaMultiplier() {
        return STAMINA_MULTIPLIER;
    }
}
