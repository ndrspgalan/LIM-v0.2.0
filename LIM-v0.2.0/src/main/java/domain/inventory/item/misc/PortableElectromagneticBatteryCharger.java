package domain.inventory.item.misc;

import domain.environment.time.DayPhase;
import domain.environment.time.EnvironmentalCycle;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/** Cargador portátil V881 acoplado directamente al reloj de EnvironmentalCycle. */
public final class PortableElectromagneticBatteryCharger extends InventoryEntry {
    private ElectromagneticPortableBatteryItem battery;
    private Duration startedAt;

    public PortableElectromagneticBatteryCharger() {
        super("Cargador portátil de Batería Electromagnética V881", "Unidad reforzada para recargar el módulo portátil empleado por el Fusil Bifilar desde una fuente eléctrica compatible.",
                0.250, new InventoryFootprint(2, 2), List.of("Capacidad | 1 batería", "Tiempo | 90 minutos", "Dimensiones | 16 × 9 × 5 cm"));
    }

    public boolean begin(ElectromagneticPortableBatteryItem battery, EnvironmentalCycle cycle) {
        Objects.requireNonNull(battery); Objects.requireNonNull(cycle);
        if (this.battery != null) return false;
        this.battery = battery;
        this.startedAt = absoluteTime(cycle);
        return true;
    }

    public boolean synchronize(EnvironmentalCycle cycle) {
        Objects.requireNonNull(cycle);
        if (battery == null) return false;
        if (absoluteTime(cycle).minus(startedAt).compareTo(EnvironmentalCycle.DAY_DURATION) < 0) return false;
        battery.rechargeFully();
        battery = null;
        startedAt = null;
        return true;
    }

    public boolean charging() { return battery != null; }
    public Duration remaining(EnvironmentalCycle cycle) {
        if (battery == null) return Duration.ZERO;
        Duration elapsed = absoluteTime(cycle).minus(startedAt);
        Duration left = EnvironmentalCycle.DAY_DURATION.minus(elapsed);
        return left.isNegative() ? Duration.ZERO : left;
    }

    private static Duration absoluteTime(EnvironmentalCycle cycle) {
        long phaseIndex = switch (cycle.phase()) { case DAY -> 0; case AFTERNOON -> 1; case NIGHT -> 2; };
        return EnvironmentalCycle.DAY_DURATION.multipliedBy(cycle.completedDays())
                .plus(EnvironmentalCycle.PHASE_DURATION.multipliedBy(phaseIndex))
                .plus(cycle.elapsedInPhase());
    }
}
