package domain.inventory.item.firearms;

/** Lectura analógica continua con selector discreto P50-P90. */
public record ElectromagneticChargeGaugeReading(
        double equivalentTurns,
        double storedEnergyJ,
        double piercing,
        double effectiveRangeMeters,
        double projectedThermalLockSeconds,
        ElectromagneticChargeSetting selectedSetting,
        double turnsRemainingToSelectedSetting,
        double batteryChargeRatio
) {}
