package domain.inventory.item.firearms;

/** Cinco estados discrecionales seleccionables del medidor de carga bifilar . */
public enum ElectromagneticChargeSetting {
    P50(5.0, 50.0, 180.0, 2.11),
    P60(8.0, 60.0, 240.0, 4.18),
    P70(14.0, 70.0, 300.0, 6.73),
    P80(23.0, 80.0, 360.0, 10.93),
    P90(35.0, 90.0, 420.0, 16.82);

    private final double equivalentTurns;
    private final double piercing;
    private final double rangeMeters;
    private final double thermalLockSeconds;

    ElectromagneticChargeSetting(double equivalentTurns, double piercing, double rangeMeters, double thermalLockSeconds) {
        this.equivalentTurns = equivalentTurns;
        this.piercing = piercing;
        this.rangeMeters = rangeMeters;
        this.thermalLockSeconds = thermalLockSeconds;
    }

    public double equivalentTurns() { return equivalentTurns; }
    public double piercing() { return piercing; }
    public double rangeMeters() { return rangeMeters; }
    public double thermalLockSeconds() { return thermalLockSeconds; }
    public double storedEnergyJ() { return equivalentTurns * ElectromagneticChargePolicy.JOULES_PER_EQUIVALENT_TURN; }

    public ElectromagneticChargeSetting next() {
        ElectromagneticChargeSetting[] values = values();
        return values[(ordinal() + 1) % values.length];
    }
}
