package domain.character.sheet;

public enum Attribute {
    VITALIDAD("Vitalidad"),
    AGUANTE("Aguante"),
    ADAPTABILIDAD("Adaptabilidad"),
    FUERZA("Fuerza"),
    DESTREZA("Destreza"),
    INTELIGENCIA("Inteligencia"),
    FE("Fe"),
    CARISMA("Carisma"),
    CLARIVIDENCIA("Clarividencia");

    private final String label;

    Attribute(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
