package domain.inventory.item.firearms;

/** Estado sostenido de la carga con bayoneta del Fusil de Repetición. */
public final class BayonetChargeState {
    private boolean charging;
    private double staminaSpent;

    public boolean charging() { return charging; }
    public double staminaSpent() { return staminaSpent; }
    void begin() { charging = true; }
    void stop() { charging = false; }
    void spend(double amount) { staminaSpent += amount; }
}
