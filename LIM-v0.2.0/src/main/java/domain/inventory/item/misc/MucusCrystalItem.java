package domain.inventory.item.misc;

import domain.character.sheet.Attribute;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.ItemProperty;
import domain.runic.EffectImmunitySet;

import java.util.List;
import java.util.Objects;

/** Cristal transpuesto: objeto misceláneo por origen y abalorio por equipamiento. */
public final class MucusCrystalItem extends AccessoryItem {
    public static final int TRANSPOSITION_CLARIVOYANCE_THRESHOLD = 33;
    private final String formDescription;
    private final MucusCrystalGeometry geometry;
    private final double baseSaleValue;

    public MucusCrystalItem(String name, String narrativeDescription, MucusCrystalGeometry geometry, String formDescription,
                            EffectImmunitySet immunities, String statistic, double baseSaleValue) {
        super(name, narrativeDescription, MucusCrystalPhysicalPolicy.massKg(Objects.requireNonNull(geometry)),
                domain.inventory.logistics.InventoryVolumeProjectionPolicy.footprint(MucusCrystalPhysicalPolicy.dimensions(geometry)), 1.0,
                Attribute.CLARIVIDENCIA, TRANSPOSITION_CLARIVOYANCE_THRESHOLD, 0.0,
                List.of("FORMA | " + geometry.label().toUpperCase(java.util.Locale.ROOT) + " · " + Objects.requireNonNull(formDescription),
                        "VOLUMEN TRANSPUESTO | " + formatMl(MucusCrystalPhysicalPolicy.convertedVolumeMl(geometry)) + " mL",
                        "MASA | " + String.format(java.util.Locale.ROOT,"%.3f kg",MucusCrystalPhysicalPolicy.massKg(geometry)),
                        "ENVOLVENTE XYZ | " + String.format(java.util.Locale.ROOT,"%.1f cm",MucusCrystalPhysicalPolicy.boundingDimensionMeters(geometry)*100.0),
                        statistic),
                List.<ItemProperty>of(), immunities);
        if (!Double.isFinite(baseSaleValue) || baseSaleValue < 0) {
            throw new IllegalArgumentException("El valor de venta no puede ser negativo.");
        }
        this.formDescription = formDescription;
        this.geometry = geometry;
        this.baseSaleValue = baseSaleValue;
    }

    public String formDescription() { return formDescription; }
    public MucusCrystalGeometry geometry() { return geometry; }
    public boolean sellable() { return true; }
    public double baseSaleValue() { return baseSaleValue; }
    @Override public double weightKg() { return MucusCrystalPhysicalPolicy.massKg(geometry); }
    @Override public domain.inventory.logistics.InventoryPhysicalDimensions physicalDimensions() {
        return MucusCrystalPhysicalPolicy.dimensions(geometry);
    }
    @Override public domain.inventory.InventoryFootprint footprint() {
        return domain.inventory.logistics.InventoryVolumeProjectionPolicy.footprint(physicalDimensions());
    }
    public boolean mechanicallyActive(domain.character.sheet.CharacterSheet sheet) {
        return domain.knowledge.PropertyKnowledgePolicy.requirementMet(sheet,Attribute.CLARIVIDENCIA,TRANSPOSITION_CLARIVOYANCE_THRESHOLD);
    }
    private static String formatMl(double ml){
        return Math.abs(ml-Math.rint(ml))<1e-9 ? Long.toString(Math.round(ml))
                : String.format(java.util.Locale.ROOT,"%.1f",ml);
    }
}
