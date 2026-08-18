package domain.inventory.item.armor;

/** Subregiones canónicas de la hitbox CUERPO. Sus coberturas máximas suman el 100 %. */
public enum BodyArmorRegion {
    CHEST(0.50, "coraza"),
    BRACERS(0.15, "brazales"),
    LEGGINGS(0.30, "polainas"),
    FEET(0.05, "calzado");

    private final double maximumCoverageRatio;
    private final String label;

    BodyArmorRegion(double maximumCoverageRatio, String label) {
        this.maximumCoverageRatio = maximumCoverageRatio;
        this.label = label;
    }

    public double maximumCoverageRatio() { return maximumCoverageRatio; }
    public String label() { return label; }
    /** Toda la extremidad inferior, incluido FEET, participa en la ergonomía de PA. La bonificación logística de peso se resuelve aparte. */
    public boolean contributesToErgonomics() { return true; }
}
