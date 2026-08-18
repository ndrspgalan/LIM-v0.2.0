package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import java.util.List;

/** Módulo portátil V881 de dos celdas 21700 compartido por armas electromagnéticas. */
public final class ElectromagneticPortableBatteryItem extends InventoryEntry {
    public static final double MAX_USEFUL_ENERGY_J = 93_300.0;
    private double remainingEnergyJ;

    public ElectromagneticPortableBatteryItem() {
        this(MAX_USEFUL_ENERGY_J);
    }

    public ElectromagneticPortableBatteryItem(double remainingEnergyJ) {
        super("Batería Portátil Electromagnética V881", "Módulo extraíble de dos celdas 21700 compartido por el Fusil Bifilar y el Lanza-Arcos. La reserva energética cuantificada se consume en el Bifilar; en el Lanza-Arcos actúa como asistencia/estabilización y no limita el uso ordinario.", 0.300,
                new InventoryFootprint(1, 2), List.of("Energía útil para Bifilar | 93,3 kJ", "Recarga externa del Bifilar | 90 minutos", "Lanza-Arcos | autonomía no limitante en uso ordinario", "Stack máximo | 1"));
        setRemainingEnergyJ(remainingEnergyJ);
    }

    public double remainingEnergyJ() { return remainingEnergyJ; }
    public double chargeRatio() { return remainingEnergyJ / MAX_USEFUL_ENERGY_J; }
    public boolean depleted() { return remainingEnergyJ <= 0.000001; }
    public double draw(double requestedJ) {
        if (!Double.isFinite(requestedJ) || requestedJ < 0) throw new IllegalArgumentException("La energía solicitada debe ser finita y no negativa.");
        double supplied = Math.min(requestedJ, remainingEnergyJ);
        remainingEnergyJ -= supplied;
        return supplied;
    }
    public void rechargeFully() { remainingEnergyJ = MAX_USEFUL_ENERGY_J; }
    private void setRemainingEnergyJ(double value) {
        if (!Double.isFinite(value) || value < 0 || value > MAX_USEFUL_ENERGY_J) throw new IllegalArgumentException("Carga de batería inválida.");
        remainingEnergyJ = value;
    }
}
