package domain.ability;

/** Conversión continua de PA completos en PV hasta restaurar la salud o vaciar la barra. */
public final class ThetaRegenerationPolicy {
    private static final double EPSILON = 1e-9;
    private ThetaRegenerationPolicy() {}
    public static boolean canActivate(double currentStamina, double maximumStamina, boolean stationary, boolean inCombat) {
        validate(currentStamina, maximumStamina);
        return stationary && !inCombat && Math.abs(currentStamina - maximumStamina) <= EPSILON;
    }
    public static ThetaTick tick(double currentHealth, double maximumHealth, double currentStamina,
                                 double maximumStamina, double fullStaminaRegenerationSeconds, double elapsedSeconds) {
        return tick(currentHealth, maximumHealth, currentStamina, maximumStamina,
                fullStaminaRegenerationSeconds, elapsedSeconds, 1.0, 1.0);
    }

    /**
     * La Corteza de sauce acelera la PA REGEN y OVERCLOCK multiplica además la
     * restitución de Theta. Ambos factores se componen multiplicativamente.
     */
    public static ThetaTick tick(double currentHealth, double maximumHealth, double currentStamina,
                                 double maximumStamina, double fullStaminaRegenerationSeconds,
                                 double elapsedSeconds, double staminaRegenerationMultiplier,
                                 double overclockRestitutionMultiplier) {
        validate(currentStamina, maximumStamina);
        if (currentHealth < 0 || maximumHealth <= 0 || currentHealth > maximumHealth) throw new IllegalArgumentException("PV inválidos.");
        if (fullStaminaRegenerationSeconds <= 0 || elapsedSeconds < 0
                || staminaRegenerationMultiplier <= 0 || overclockRestitutionMultiplier <= 0) {
            throw new IllegalArgumentException("Tiempo o multiplicadores inválidos.");
        }
        double paPerSecond = maximumStamina / fullStaminaRegenerationSeconds;
        paPerSecond *= staminaRegenerationMultiplier * overclockRestitutionMultiplier;
        double healthMissing = maximumHealth - currentHealth;
        double convertible = Math.min(currentStamina, paPerSecond * elapsedSeconds);
        double restored = Math.min(healthMissing, convertible * (maximumHealth / maximumStamina));
        double spent = healthMissing == 0 ? currentStamina : Math.min(currentStamina, restored * (maximumStamina / maximumHealth));
        double remaining = Math.max(0, currentStamina - spent);
        double health = Math.min(maximumHealth, currentHealth + restored);
        boolean complete = health >= maximumHealth - EPSILON;
        if (complete) remaining = 0;
        return new ThetaTick(health, remaining, complete);
    }
    private static void validate(double current, double maximum) {
        if (!Double.isFinite(current) || !Double.isFinite(maximum) || maximum <= 0 || current < 0 || current > maximum) throw new IllegalArgumentException("PA inválidos.");
    }
    public record ThetaTick(double currentHealth, double currentStamina, boolean complete) {}
}
