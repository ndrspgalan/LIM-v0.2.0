package domain.worldmemory.history;

/** Naturaleza semántica de un cambio longitudinal en una entrada recordada. */
public enum WorldMemoryRevisionType {
    ACQUISITION("Adquisición"),
    EXPANSION("Ampliación"),
    CORRECTION("Corrección"),
    RELIABILITY_CHANGE("Cambio de fiabilidad"),
    SPATIAL_REFINEMENT("Refinamiento espacial"),
    SOURCE_ADDITION("Nueva fuente"),
    REPLACEMENT("Sustitución"),
    INVALIDATION("Invalidación");

    private final String label;

    WorldMemoryRevisionType(String label) { this.label = label; }

    public String label() { return label; }
}
