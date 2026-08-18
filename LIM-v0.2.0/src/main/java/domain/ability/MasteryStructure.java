package domain.ability;

/** Estructura evolutiva de una familia de maestrías, independiente de su naturaleza. */
public enum MasteryStructure {
    PAIRS("Pares"),
    BRANCHED("Ramificada"),
    BINARY("Binaria"),
    CONCATENATED("Concatenada"),
    DUAL("Dual"),
    TRIAD("Tríada"),
    UNITARY("Unitaria");

    private final String label;
    MasteryStructure(String label) { this.label = label; }
    public String label() { return label; }
}
