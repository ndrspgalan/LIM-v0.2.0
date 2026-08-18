package domain.ability;

/** Clasificación semántica independiente de la estructura interna de una maestría. */
public enum MasteryCategory {
    CLASS_SPECIALIZED("Especializada por clase"),
    EVOLUTIVE("Evolutiva");

    private final String label;
    MasteryCategory(String label) { this.label = label; }
    public String label() { return label; }
}
