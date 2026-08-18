package domain.inventory;

import domain.inventory.item.ItemProperty;

import java.util.List;
import java.util.Objects;

public class InventoryEntry {
    private final String name;
    private final String narrativeDescription;
    private final double weightKg;
    private final InventoryFootprint footprint;
    private final List<String> statistics;
    private final List<ItemProperty> properties;
    private InventoryOrientation inventoryOrientation = InventoryOrientation.DEFAULT;

    public InventoryEntry(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            List<String> statistics
    ) {
        this(name, narrativeDescription, weightKg, footprint, statistics, List.of());
    }

    public InventoryEntry(
            String name,
            String narrativeDescription,
            double weightKg,
            InventoryFootprint footprint,
            List<String> statistics,
            List<ItemProperty> properties
    ) {
        this.name = requireText(name, "El nombre del objeto no puede estar vacío.");
        this.narrativeDescription = requireText(
                narrativeDescription,
                "La descripción narrativa no puede estar vacía."
        );
        if (weightKg < 0) {
            throw new IllegalArgumentException("El peso del objeto no puede ser negativo.");
        }
        this.footprint = Objects.requireNonNull(footprint, "El tamaño del objeto no puede ser nulo.");
        Objects.requireNonNull(statistics, "Las estadísticas del objeto no pueden ser nulas.");
        Objects.requireNonNull(properties, "Las propiedades del objeto no pueden ser nulas.");
        if (statistics.stream().anyMatch(Objects::isNull) || properties.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("El objeto no puede contener estadísticas o propiedades nulas.");
        }
        this.weightKg = weightKg;
        this.statistics = List.copyOf(statistics);
        this.properties = List.copyOf(properties);
    }

    public String name() { return name; }
    public String narrativeDescription() { return narrativeDescription; }
    public double weightKg() { return weightKg; }
    public InventoryFootprint canonicalFootprint() {
        return domain.inventory.catalog.PhysicalObjectCatalog.containsName(name)
                ? domain.inventory.catalog.PhysicalObjectDimensionsCatalog.footprintFor(name, footprint)
                : footprint;
    }
    public InventoryFootprint footprint() {
        InventoryFootprint base=canonicalFootprint();
        return inventoryOrientation==InventoryOrientation.ROTATED_90 ? base.rotated90() : base;
    }
    public InventoryOrientation inventoryOrientation(){ return inventoryOrientation; }
    public void setInventoryOrientation(InventoryOrientation orientation){ this.inventoryOrientation=Objects.requireNonNull(orientation); }
    public void rotate90(){ inventoryOrientation=inventoryOrientation.toggled(); }
    public domain.inventory.logistics.InventoryPhysicalDimensions physicalDimensions() {
        return domain.inventory.catalog.PhysicalObjectDimensionsCatalog.dimensionsFor(name, footprint);
    }
    public List<String> statistics() { return statistics; }
    public List<ItemProperty> properties() { return properties; }
    /** Identidad estable de tipo usada por quick-access y serialización lógica . */
    public domain.inventory.catalog.CanonicalObjectTypeId canonicalTypeId() {
        return domain.inventory.catalog.PhysicalObjectCatalog.typeIdOf(this);
    }


    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        String normalizedValue = value.trim();
        if (normalizedValue.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return normalizedValue;
    }
}
