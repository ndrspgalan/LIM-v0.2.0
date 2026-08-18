package domain.inventory.item;

/** Tramo temporal relevante para efectos de abalorios. */
public enum DayPhase {
    DAY("Día"),
    AFTERNOON("Tarde"),
    NIGHT("Noche");

    private final String label;

    DayPhase(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
