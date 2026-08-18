package domain.survival;

/** Escala canónica de hambre: +1, 0, -1, -2 y -3. */
public enum HungerLevel {
    SATIATED(1, 0, "Saciado"),
    FUNCTIONAL(0, 1, "Funcional"),
    HUNGRY(-1, 2, "Hambriento"),
    MODERATE_HUNGER(-2, 3, "Hambre moderada"),
    ACUTE_HUNGER(-3, 4, "Hambre aguda");

    private final int gameplayValue;
    private final int severity;
    private final String label;

    HungerLevel(int gameplayValue, int severity, String label) {
        this.gameplayValue = gameplayValue;
        this.severity = severity;
        this.label = label;
    }

    public int gameplayValue() { return gameplayValue; }
    public int severity() { return severity; }
    public String label() { return label; }
    public boolean penalized() { return gameplayValue < 0; }

    public static HungerLevel fromSeverity(int severity) {
        return switch (Math.max(0, Math.min(4, severity))) {
            case 0 -> SATIATED;
            case 1 -> FUNCTIONAL;
            case 2 -> HUNGRY;
            case 3 -> MODERATE_HUNGER;
            default -> ACUTE_HUNGER;
        };
    }
}
