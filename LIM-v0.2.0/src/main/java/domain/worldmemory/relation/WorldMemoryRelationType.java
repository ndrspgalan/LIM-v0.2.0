package domain.worldmemory.relation;

/**
 * Vocabulario direccional del grafo de la Memoria del Mundo.
 * La etiqueta inversa permite consultar la misma arista desde cualquiera de sus extremos
 * sin duplicar conocimiento en persistencia.
 */
public enum WorldMemoryRelationType {
    KNOWS("Conoce", "Es conocido por"),
    TRADES_WITH("Comercia con", "Comercia con"),
    BELONGS_TO("Pertenece a", "Incluye a"),
    MENTIONED("Mencionó", "Fue mencionado por"),
    OCCURRED_AT("Ocurrió en", "Albergó"),
    INHABITS("Habita en", "Es habitado por"),
    OWNS("Posee", "Pertenece a"),
    ORIGINATES_FROM("Proviene de", "Es origen de"),
    CONFLICTS_WITH("Está enfrentado a", "Está enfrentado a"),
    RELATED_TO("Está relacionado con", "Está relacionado con");

    private final String outgoingLabel;
    private final String incomingLabel;

    WorldMemoryRelationType(String outgoingLabel, String incomingLabel) {
        this.outgoingLabel = outgoingLabel;
        this.incomingLabel = incomingLabel;
    }

    public String outgoingLabel() { return outgoingLabel; }
    public String incomingLabel() { return incomingLabel; }
}
