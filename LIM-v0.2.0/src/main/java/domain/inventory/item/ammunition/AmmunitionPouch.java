package domain.inventory.item.ammunition;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;

import java.util.List;
import java.util.Objects;

/**
 * Bolsa de proyectiles unitarios. Desde  el peso es dinámico:
 * bolsa vacía + masa individual de cada proyectil todavía contenido.
 */
public final class AmmunitionPouch extends InventoryEntry implements AmmunitionSource {
    private final AmmunitionDescriptor descriptor;
    private final int maxUnits;
    private int remainingUnits;
    private final double emptyPouchWeightKg;
    private final double unitWeightKg;

    public AmmunitionPouch(String name, AmmunitionDescriptor descriptor, int initialUnits, int maxUnits) {
        this(name, descriptor, initialUnits, maxUnits, 0.0, 0.0);
    }

    public AmmunitionPouch(
            String name,
            AmmunitionDescriptor descriptor,
            int initialUnits,
            int maxUnits,
            double emptyPouchWeightKg,
            double unitWeightKg
    ) {
        super(name,
                "Bolsita especializada de munición " + descriptor.caliber() + " " + descriptor.material() + ".",
                validatedTotalWeight(initialUnits, emptyPouchWeightKg, unitWeightKg),
                new InventoryFootprint(1, 1),
                List.of(
                        "Munición | " + descriptor.caliber() + " " + descriptor.material(),
                        "Capacidad | " + maxUnits + " proyectiles",
                        "Stack máximo | " + maxUnits,
                        "Peso vacío | " + formatKg(emptyPouchWeightKg) + " kg",
                        "Peso por proyectil | " + formatKg(unitWeightKg) + " kg"
                ));
        this.descriptor = Objects.requireNonNull(descriptor);
        if (descriptor.family() != AmmunitionFamily.BULLET) {
            throw new IllegalArgumentException("La bolsita debe contener BULLET.");
        }
        if (maxUnits <= 0 || initialUnits < 0 || initialUnits > maxUnits) {
            throw new IllegalArgumentException("Cantidad inválida.");
        }
        this.maxUnits = maxUnits;
        this.remainingUnits = initialUnits;
        this.emptyPouchWeightKg = validatedWeight(emptyPouchWeightKg, "El peso vacío de la bolsita");
        this.unitWeightKg = validatedWeight(unitWeightKg, "El peso unitario del proyectil");
    }

    @Override
    public double weightKg() {
        return emptyPouchWeightKg + remainingUnits * unitWeightKg;
    }

    public double emptyPouchWeightKg() { return emptyPouchWeightKg; }
    public double unitWeightKg() { return unitWeightKg; }
    public AmmunitionDescriptor ammunitionDescriptor() { return descriptor; }
    public int remainingUnits() { return remainingUnits; }
    public int maxUnits() { return maxUnits; }
    public int shotsLoadedPerConsumedUnit() { return 1; }
    public boolean consumeOneUnit() { if (remainingUnits <= 0) return false; remainingUnits--; return true; }
    public boolean addOneUnit() { if (remainingUnits >= maxUnits) return false; remainingUnits++; return true; }

    private static double validatedTotalWeight(int initialUnits, double emptyWeight, double unitWeight) {
        if (initialUnits < 0) throw new IllegalArgumentException("Cantidad inválida.");
        return validatedWeight(emptyWeight, "El peso vacío de la bolsita")
                + initialUnits * validatedWeight(unitWeight, "El peso unitario del proyectil");
    }

    private static double validatedWeight(double value, String label) {
        if (!Double.isFinite(value) || value < 0) {
            throw new IllegalArgumentException(label + " debe ser finito y no negativo.");
        }
        return value;
    }

    private static String formatKg(double value) {
        return String.format(java.util.Locale.ROOT, "%.3f", value);
    }
}
