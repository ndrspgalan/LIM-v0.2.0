package domain.worldmemory.filter;

/** Condición espacial aplicable a una consulta filtrada. */
public enum SpatialMemoryRequirement {
    ANY("Con o sin ubicación"),
    WITH_REMEMBERED_POSITION("Con ubicación recordada"),
    WITHOUT_REMEMBERED_POSITION("Sin ubicación recordada");

    private final String label;

    SpatialMemoryRequirement(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
