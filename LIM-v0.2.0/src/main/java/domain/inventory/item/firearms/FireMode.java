package domain.inventory.item.firearms;

/** Cadencias canónicas de armas de fuego. */
public enum FireMode {
    ONE_A("1A", 1),
    THREE_A("3A", 3),
    AUTO_A("AA", Integer.MAX_VALUE);

    private final String code;
    private final int maxShotsPerTriggerPress;

    FireMode(String code, int maxShotsPerTriggerPress) {
        this.code = code;
        this.maxShotsPerTriggerPress = maxShotsPerTriggerPress;
    }

    public String code() { return code; }
    public int maxShotsPerTriggerPress() { return maxShotsPerTriggerPress; }
}
