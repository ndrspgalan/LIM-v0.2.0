package domain.combat.coating;
/** Recubrimiento persistente ligado a una unidad, stack o arma concreta. */
public record MercuryCoatingState(int remainingApplications, boolean untilBluntZero) {
    public MercuryCoatingState {
        if (remainingApplications < 0) throw new IllegalArgumentException("Las aplicaciones no pueden ser negativas.");
    }
    public MercuryCoatingState consumeOne() { return new MercuryCoatingState(Math.max(0, remainingApplications - 1), untilBluntZero); }
    public boolean active() { return untilBluntZero || remainingApplications > 0; }
}
