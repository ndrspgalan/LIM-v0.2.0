package domain.settings;

public enum TextSize {
    SMALL("PEQUEÑO"),
    MEDIUM("MEDIANO"),
    LARGE("GRANDE");

    private final String label;

    TextSize(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
