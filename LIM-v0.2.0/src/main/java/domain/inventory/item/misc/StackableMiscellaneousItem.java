package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;

import java.util.List;
import java.util.Objects;

/**
 * Base de objetos con cargas, usos o contenido consumible. Heredar de esta clase no autoriza
 * a apilar unidades físicas en una celda; esa semántica pertenece a PhysicalObjectCatalog.
 */
public class StackableMiscellaneousItem extends InventoryEntry {
    private final MiscellaneousCategory category;
    private int currentUses;
    private final int maximumUses;
    private final double structuralWeightKg;
    private final double contentWeightPerUseKg;
    private final UseResourceKind resourceKind;
    private final UseAnimation useAnimation;

    public StackableMiscellaneousItem(String name, String narrativeDescription, MiscellaneousCategory category,
            int currentUses, int maximumUses, double unitWeightKg, InventoryFootprint footprint,
            UseAnimation useAnimation, List<String> statistics, List<ItemProperty> properties) {
        this(name, narrativeDescription, category, currentUses, maximumUses, 0.0, unitWeightKg,
                UseResourceKind.QUANTITY, footprint, useAnimation, statistics, properties);
    }

    public StackableMiscellaneousItem(String name, String narrativeDescription, MiscellaneousCategory category,
            int currentUses, int maximumUses, double structuralWeightKg, double contentWeightPerUseKg,
            UseResourceKind resourceKind, InventoryFootprint footprint, UseAnimation useAnimation,
            List<String> statistics, List<ItemProperty> properties) {
        super(name, narrativeDescription, checkedWeight(currentUses, maximumUses, structuralWeightKg, contentWeightPerUseKg, resourceKind),
                footprint, statistics, properties);
        this.category = Objects.requireNonNull(category);
        this.currentUses = currentUses;
        this.maximumUses = maximumUses;
        this.structuralWeightKg = structuralWeightKg;
        this.contentWeightPerUseKg = contentWeightPerUseKg;
        this.resourceKind = Objects.requireNonNull(resourceKind);
        this.useAnimation = useAnimation;
    }

    private static double checkedWeight(int current, int maximum, double structural, double perUse, UseResourceKind kind) {
        if (maximum < 1 || current < 0 || current > maximum) throw new IllegalArgumentException("Las cargas no son válidas.");
        if (structural < 0 || perUse < 0) throw new IllegalArgumentException("El peso no puede ser negativo.");
        Objects.requireNonNull(kind, "El tipo de recurso no puede ser nulo.");
        return kind == UseResourceKind.DURABILITY ? structural : structural + current * perUse;
    }

    @Override public double weightKg() {
        return resourceKind == UseResourceKind.DURABILITY
                ? structuralWeightKg
                : structuralWeightKg + currentUses * contentWeightPerUseKg;
    }
    public MiscellaneousCategory category() { return category; }
    public int quantity() { return currentUses; }
    public int maximumStack() { return maximumUses; }
    public int currentUses() { return currentUses; }
    public int maximumUses() { return maximumUses; }
    public double unitWeightKg() { return contentWeightPerUseKg; }
    public double structuralWeightKg() { return structuralWeightKg; }
    public double contentWeightPerUseKg() { return contentWeightPerUseKg; }
    public UseResourceKind resourceKind() { return resourceKind; }
    public UseAnimation useAnimation() { return useAnimation; }
    public boolean isFull() { return currentUses == maximumUses; }
    public boolean isDepleted() { return currentUses == 0; }
    public int remainingCapacity() { return maximumUses - currentUses; }
    public boolean consumeOne() { return removeUnits(1); }

    public boolean removeUnits(int units) {
        if (units < 1) throw new IllegalArgumentException("Las unidades retiradas deben ser positivas.");
        if (currentUses < units) return false;
        currentUses -= units;
        return true;
    }

    public boolean addUnits(int units) {
        if (units < 1) throw new IllegalArgumentException("Las unidades añadidas deben ser positivas.");
        if (currentUses + units > maximumUses) return false;
        currentUses += units;
        return true;
    }

    public void restoreCurrentUses(int uses){if(uses<0||uses>maximumUses)throw new IllegalArgumentException("Cantidad persistida inválida.");currentUses=uses;}
    public void refill() { currentUses = maximumUses; }
}
