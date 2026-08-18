package domain.ability;

public enum MasteryType {
    ACTIVE("ACTIVA"),
    SUSTAINED("SOSTENIDA"),
    PASSIVE("PASIVA");

    private final String label;
    MasteryType(String label) { this.label = label; }
    public String label() { return label; }
}
