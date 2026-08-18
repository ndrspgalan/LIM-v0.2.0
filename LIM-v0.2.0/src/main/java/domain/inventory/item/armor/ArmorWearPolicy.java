package domain.inventory.item.armor;

/**
 * Política material que determina si los impactos contundentes ordinarios
 * reducen la protección efectiva de una pieza.
 */
public enum ArmorWearPolicy {
    DEGRADABLE,
    NON_DEGRADING;

    public boolean permitsWear() { return this == DEGRADABLE; }

    /** Alias semántico para consumidores del contrato compacto. */
    public boolean permitsBluntWear() { return permitsWear(); }
}
