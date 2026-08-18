package domain.inventory.item.meleeWeapons.special;

/** Estado térmico persistente de la Katana Termo-mecánica V881. */
public final class ThermoMechanicalKatanaState {
    public static final double FULL_CHARGE_SECONDS = 300.0;
    private double remainingSeconds;
    private boolean drawn;
    private boolean burning;

    public ThermoMechanicalKatanaState() {
        this(0.0, false, false);
    }

    public ThermoMechanicalKatanaState(double remainingSeconds, boolean drawn, boolean burning) {
        if (!Double.isFinite(remainingSeconds) || remainingSeconds < 0 || remainingSeconds > FULL_CHARGE_SECONDS) {
            throw new IllegalArgumentException("La carga térmica debe estar entre 0 y 300 segundos.");
        }
        this.remainingSeconds = remainingSeconds;
        this.drawn = drawn;
        this.burning = burning && drawn && remainingSeconds > 0;
    }

    public double remainingSeconds() { return remainingSeconds; }
    public boolean drawn() { return drawn; }
    public boolean burning() { return burning; }
    public boolean prepared() { return remainingSeconds > 0; }

    void refill() { remainingSeconds = FULL_CHARGE_SECONDS; burning = drawn; }
    void draw() { drawn = true; burning = remainingSeconds > 0; }
    void sheath() { drawn = false; burning = false; }

    void advance(double realSeconds) {
        if (!Double.isFinite(realSeconds) || realSeconds < 0) throw new IllegalArgumentException("El tiempo debe ser finito y no negativo.");
        if (!burning || realSeconds == 0) return;
        remainingSeconds = Math.max(0.0, remainingSeconds - realSeconds);
        if (remainingSeconds == 0.0) burning = false;
    }
}
