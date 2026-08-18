package domain.character.progression;

public enum MucusType {
    BLANCO("Mucus blanco", 0, 2_000),
    AMARILLENTO("Mucus amarillento", 1, 500),
    VERDOSO("Mucus verdoso", 2, 100),
    MARRON("Mucus marrón", 3, 25),
    ENSANGRENTADO("Mucus ensangrentado", 4, 5),
    NEGRUZCO("Mucus negruzco", 5, 1);

    private final String label;
    private final int rarity;
    private final int maximumReserveMl;

    MucusType(String label, int rarity, int maximumReserveMl) {
        this.label = label;
        this.rarity = rarity;
        this.maximumReserveMl = maximumReserveMl;
    }

    public String label() { return label; }
    public int rarity() { return rarity; }
    public int maximumReserveMl() { return maximumReserveMl; }
    /** Compatibilidad nominal: cada unidad histórica equivale canónicamente a 1 mL. */
    public int maximumAccumulated() { return maximumReserveMl; }

    public static MucusType forRarity(int rarity) {
        int bounded = Math.max(0, Math.min(rarity, NEGRUZCO.rarity));
        for (MucusType type : values()) if (type.rarity == bounded) return type;
        throw new IllegalStateException("Rareza de mucus no representable: " + rarity);
    }
}
