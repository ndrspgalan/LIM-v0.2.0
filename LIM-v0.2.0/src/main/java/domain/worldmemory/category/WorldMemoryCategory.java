package domain.worldmemory.category;

/** Familias consultables de conocimiento de la Memoria del Mundo. */
public enum WorldMemoryCategory {
    PLACES("Lugares", "Espacios, enclaves y referencias geográficas recordadas."),
    PEOPLE("Personas", "Individuos conocidos directa o indirectamente."),
    CREATURES("Criaturas", "Seres no humanos observados, descritos o inferidos."),
    OBJECTS_AND_RESOURCES("Objetos y recursos", "Objetos singulares, materiales y recursos relevantes."),
    INSTITUTIONS_AND_FACTIONS("Instituciones y facciones", "Organizaciones, órdenes, casas y colectivos."),
    EVENTS("Acontecimientos", "Sucesos recordados, presenciados o transmitidos."),
    WORLD_KNOWLEDGE("Conocimiento del mundo", "Ideas, costumbres, hipótesis y saberes adquiridos."),
    EXPLORED_TERRITORY("Territorio explorado", "Huella espacial de las superficies recorridas por el personaje.");

    private final String label;
    private final String description;

    WorldMemoryCategory(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String label() { return label; }
    public String description() { return description; }
}
