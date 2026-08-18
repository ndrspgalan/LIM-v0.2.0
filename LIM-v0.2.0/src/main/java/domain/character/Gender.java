package domain.character;

public enum Gender {
    HOMBRE("Hombre"),
    MUJER("Mujer");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
