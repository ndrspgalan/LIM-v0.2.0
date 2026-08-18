package domain.social;

public enum RelationshipType {
    HOSTILE("Hostil"),
    ANTIPATHETIC("Antipática"),
    DISTRUSTFUL("Desconfiada"),
    INDIFFERENT("Indiferente"),
    RELIABLE("Fiable"),
    FRIENDLY("Amistosa"),
    ROMANTIC("Romántica");

    private final String label;
    RelationshipType(String label) { this.label = label; }
    public String label() { return label; }

    public RelationshipType shift(int steps) {
        RelationshipType[] values = values();
        return values[Math.max(0, Math.min(values.length - 1, ordinal() + steps))];
    }
}
