package domain.combat;

/** Preparación temporal del ataque cargado. La cancelación se resuelve mediante ChargedAttackPreparationPolicy. */
public final class ChargedAttackPreparationState {
    private double heldSeconds;
    private boolean preparing;
    public void start() { heldSeconds = 0.0; preparing = true; }
    public void advance(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("El tiempo debe ser finito y no negativo.");
        if (preparing) heldSeconds += seconds;
    }
    void cancelPreparation() { heldSeconds = 0.0; preparing = false; }
    void completePreparation() { heldSeconds = 0.0; preparing = false; }
    public boolean preparing() { return preparing; }
    public double heldSeconds() { return heldSeconds; }
}
