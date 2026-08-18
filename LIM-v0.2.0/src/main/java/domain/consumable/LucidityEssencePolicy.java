package domain.consumable;

/** Esencia de lucidez: inhalación, arranque inmediato de PA REGEN hasta completar y supresión de i-frames de MIRAGE frente al rival actual. */
public final class LucidityEssencePolicy {
    public static final double DURATION_GAME_MINUTES = 30.0;
    public double immediateRegeneration(double current, double maximum) {
        if (current < 0 || maximum <= 0 || current > maximum) throw new IllegalArgumentException("PA inválidos.");
        return maximum;
    }
    public double mirageInvulnerabilityMultiplier(boolean active, boolean sameHostileOpponent) {
        return active && sameHostileOpponent ? 0.0 : 1.0;
    }
}
