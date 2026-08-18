package domain.bestiarium.physical_plane.ferae;

public enum FeraeSex {
    MACHO("macho"),
    HEMBRA("hembra");

    private final String label;
    FeraeSex(String label){ this.label=label; }
    public String label(){ return label; }
}
