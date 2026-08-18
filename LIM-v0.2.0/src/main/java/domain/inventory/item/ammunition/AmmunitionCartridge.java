package domain.inventory.item.ammunition;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;

import java.util.List;
import java.util.Objects;

public class AmmunitionCartridge extends InventoryEntry implements AmmunitionSource {
    private final AmmunitionDescriptor descriptor;
    private final int shotsPerCartridge;
    private final double structuralWeightKg;
    private final double roundWeightKg;
    private int remainingShots;

    public AmmunitionCartridge(String name, AmmunitionDescriptor descriptor, int shotsPerCartridge) {
        this(name, descriptor, shotsPerCartridge, 0.0, 0.0, new InventoryFootprint(1,1));
    }

    /** Compatibilidad: weightKg se interpreta como masa llena, con 30% estructural si no se especifica desglose. */
    public AmmunitionCartridge(String name, AmmunitionDescriptor descriptor, int shotsPerCartridge, double weightKg) {
        this(name, descriptor, shotsPerCartridge, weightKg*0.30,
                shotsPerCartridge<=0?0:(weightKg*0.70/shotsPerCartridge), new InventoryFootprint(1,1));
    }

    public AmmunitionCartridge(String name, AmmunitionDescriptor descriptor, int shotsPerCartridge,
                              double weightKg, InventoryFootprint footprint) {
        this(name, descriptor, shotsPerCartridge, weightKg*0.30,
                shotsPerCartridge<=0?0:(weightKg*0.70/shotsPerCartridge), footprint);
    }

    public AmmunitionCartridge(String name, AmmunitionDescriptor descriptor, int shotsPerCartridge,
                              double structuralWeightKg, double roundWeightKg, InventoryFootprint footprint) {
        super(name,
                "Contenedor mecánico persistente de " + descriptor.caliber() + " " + descriptor.material() + ".",
                validatedWeight(structuralWeightKg + shotsPerCartridge*roundWeightKg),
                Objects.requireNonNull(footprint),
                List.of(
                        "Munición | " + descriptor.caliber() + " " + descriptor.material(),
                        "Capacidad interna | " + shotsPerCartridge + " disparos",
                        "RECIPIENTE | Persistente al agotarse",
                        "PESO VACÍO | " + formatKg(structuralWeightKg) + " kg",
                        "PESO POR CARTUCHO | " + formatKg(roundWeightKg) + " kg"
                ));
        this.descriptor = Objects.requireNonNull(descriptor);
        if (descriptor.family() != AmmunitionFamily.CARTRIDGE) throw new IllegalArgumentException("Un cartucho debe ser CARTRIDGE.");
        if (shotsPerCartridge <= 0) throw new IllegalArgumentException("Capacidad inválida.");
        if(structuralWeightKg<0 || roundWeightKg<0) throw new IllegalArgumentException("Masas inválidas.");
        this.shotsPerCartridge=shotsPerCartridge;
        this.structuralWeightKg=structuralWeightKg;
        this.roundWeightKg=roundWeightKg;
        this.remainingShots=shotsPerCartridge;
    }

    public AmmunitionDescriptor ammunitionDescriptor() { return descriptor; }
    public int remainingUnits() { return remainingShots > 0 ? 1 : 0; }
    public int maxUnits() { return 1; }
    public int shotsLoadedPerConsumedUnit() { return remainingShots; }
    @Override public int remainingShots() { return remainingShots; }
    @Override public boolean consumeShots(int quantity) {
        if (quantity <= 0 || quantity > remainingShots) return false;
        remainingShots -= quantity;
        return true;
    }
    public boolean consumeOneUnit() {
        if (remainingShots <= 0) return false;
        remainingShots = 0;
        return true;
    }
    public int capacity() { return shotsPerCartridge; }
    public int roundsRemaining() { return remainingShots; }
    @Override public double weightKg() {
        return structuralWeightKg + remainingShots * roundWeightKg;
    }
    public double structuralWeightKg(){ return structuralWeightKg; }
    public double roundWeightKg(){ return roundWeightKg; }
    public int maxStackSize() { return 1; }
    public String caliber() { return descriptor.caliber(); }
    public String material() { return descriptor.material(); }

    private static double validatedWeight(double weightKg) {
        if (!Double.isFinite(weightKg) || weightKg < 0) {
            throw new IllegalArgumentException("El peso del cartucho debe ser finito y no negativo.");
        }
        return weightKg;
    }

    private static String formatKg(double value) {
        return String.format(java.util.Locale.ROOT, "%.3f", value);
    }
}
