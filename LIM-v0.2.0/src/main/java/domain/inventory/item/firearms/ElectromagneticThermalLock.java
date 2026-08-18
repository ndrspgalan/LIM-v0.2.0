package domain.inventory.item.firearms;

/** Reloj térmico del Bifilar. No recupera energía: solo bloquea el gatillo. */
public final class ElectromagneticThermalLock {
    private double remainingSeconds;

    public double remainingSeconds() { return remainingSeconds; }
    public boolean locked() { return remainingSeconds > 0.0; }

    public void engage(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("Bloqueo térmico inválido.");
        remainingSeconds = seconds;
    }

    public void advance(double elapsedSeconds) {
        if (!Double.isFinite(elapsedSeconds) || elapsedSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        remainingSeconds = Math.max(0.0, remainingSeconds - elapsedSeconds);
    }
}
