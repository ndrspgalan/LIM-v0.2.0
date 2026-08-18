package domain.inventory.item.meleeWeapons.special;

/** La celda está abstraída como componente permanente; solo se rastrea la recarga del acumulador. */
public final class ElectroMechanicalMaceState {
    private double secondsSinceDischarge;

    public ElectroMechanicalMaceState() { this(ElectroMechanicalMacePolicy.RECHARGE_SECONDS); }
    public ElectroMechanicalMaceState(double secondsSinceDischarge) {
        if (!Double.isFinite(secondsSinceDischarge) || secondsSinceDischarge < 0) throw new IllegalArgumentException("Tiempo de recarga inválido.");
        this.secondsSinceDischarge = secondsSinceDischarge;
    }

    public double secondsSinceDischarge() { return secondsSinceDischarge; }
    public boolean charged() { return secondsSinceDischarge >= ElectroMechanicalMacePolicy.RECHARGE_SECONDS; }
    void discharge() { secondsSinceDischarge = 0.0; }
    void advance(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
        secondsSinceDischarge = Math.min(ElectroMechanicalMacePolicy.RECHARGE_SECONDS, secondsSinceDischarge + seconds);
    }
}
