package domain.consumable;

/**  — efecto canónico de la Inyección estimulante. */
public final class StimulantInjectionPolicy {
    public static final double DURATION_REAL_SECONDS = 6.0;
    public static final double HEALTH_REGEN_TICK_SECONDS = 1.0;
    public static final String INJECTION_SITE = "muslo derecho";
    public double healthRegenTickSeconds(boolean active) { return active ? HEALTH_REGEN_TICK_SECONDS : 6.0; }
    public double staminaCost(double canonicalCost, boolean active) {
        if (!Double.isFinite(canonicalCost) || canonicalCost < 0) throw new IllegalArgumentException("Coste de PA inválido.");
        return active ? 0.0 : canonicalCost;
    }
}
