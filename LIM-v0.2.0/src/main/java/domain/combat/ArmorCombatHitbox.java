package domain.combat;

import domain.inventory.item.armor.BodyArmorRegion;

import java.util.Optional;

/**
 * : hitboxes bélicas canónicas. HEAD/BODY permanecen como regiones globales
 * para sistemas no bélicos, pero el intercambio físico se resuelve contra una pieza anatómica concreta.
 */
public enum ArmorCombatHitbox {
    HELMET(1.00, null, "casco"),
    CHEST(0.50, BodyArmorRegion.CHEST, "coraza"),
    BRACERS(0.15, BodyArmorRegion.BRACERS, "brazales"),
    LEGGINGS(0.30, BodyArmorRegion.LEGGINGS, "polainas"),
    FEET(0.05, BodyArmorRegion.FEET, "calzado");

    private final double maximumGlobalCoverageRatio;
    private final BodyArmorRegion bodyRegion;
    private final String label;

    ArmorCombatHitbox(double maximumGlobalCoverageRatio, BodyArmorRegion bodyRegion, String label) {
        this.maximumGlobalCoverageRatio = maximumGlobalCoverageRatio;
        this.bodyRegion = bodyRegion;
        this.label = label;
    }

    public double maximumGlobalCoverageRatio() { return maximumGlobalCoverageRatio; }
    public Optional<BodyArmorRegion> bodyRegion() { return Optional.ofNullable(bodyRegion); }
    public String label() { return label; }
    public boolean isHead() { return this == HELMET; }
}
