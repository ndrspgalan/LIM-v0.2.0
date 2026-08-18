package domain.bestiarium.physical_plane.ancient;

/** Sexo canónico propio de ANCIENT; no amplía Gender para no alterar reglas humanas ordinarias. */
public enum AncientSex {
    HOMBRE("Hombre"),
    MUJER("Mujer"),
    HERMAFRODITA("Hermafrodita");

    private final String label;
    AncientSex(String label) { this.label = label; }
    public String label() { return label; }
}
