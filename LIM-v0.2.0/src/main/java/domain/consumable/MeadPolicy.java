package domain.consumable;

/** Hidromiel : normaliza ataques caros, neutraliza penalización por carga y fuerza latencia 1,20 s, a cambio de lock-on nulo y tambaleo. */
public final class MeadPolicy {
    public static final double DURATION_GAME_MINUTES = 30.0;
    public static final double STAMINA_REGEN_DELAY_SECONDS = 1.20;
    public double attackStaminaMultiplier(double canonicalMultiplier, boolean active) {
        if (!Double.isFinite(canonicalMultiplier) || canonicalMultiplier <= 0) throw new IllegalArgumentException("Multiplicador inválido.");
        return active && canonicalMultiplier > 1.0 ? 1.0 : canonicalMultiplier;
    }
    /** Devuelve el régimen sin penalización de carga: recuperación completa en 1 s. */
    public double fullStaminaRecoverySeconds(boolean active, double ordinarySeconds) {
        if (!Double.isFinite(ordinarySeconds) || ordinarySeconds <= 0) throw new IllegalArgumentException("Tiempo inválido.");
        return active ? 1.0 : ordinarySeconds;
    }
    public double regenerationDelaySeconds(boolean active, double ordinaryDelay) { return active ? STAMINA_REGEN_DELAY_SECONDS : ordinaryDelay; }
    public boolean canTargetLock(boolean active) { return !active; }
    public boolean constantSway(boolean active) { return active; }
}
