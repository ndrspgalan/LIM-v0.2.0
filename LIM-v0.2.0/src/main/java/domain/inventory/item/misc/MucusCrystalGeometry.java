package domain.inventory.item.misc;

public enum MucusCrystalGeometry {
    TETRAEDRO("Tetraedro"), OCTAEDRO("Octaedro"), CUBO("Cubo"), ESFERA("Esfera"), DODECAEDRO("Dodecaedro");
    private final String label;
    MucusCrystalGeometry(String label){this.label=label;}
    public String label(){return label;}
}
