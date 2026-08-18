package domain.inventory.item.armor;

/**
 * Perfil defensivo porcentual. Cada canal expresa qué porcentaje del daño cubierto
 * absorbe la armadura, con guardarraíl duro universal de 0 a 100.
 */
public record ArmorProtectionProfile(double piercing, double slashing, double blunt) {
    public ArmorProtectionProfile {
        if (!Double.isFinite(piercing) || !Double.isFinite(slashing) || !Double.isFinite(blunt)) {
            throw new IllegalArgumentException("La protección debe ser finita.");
        }
        if (piercing < 0 || slashing < 0 || blunt < 0) {
            throw new IllegalArgumentException("La protección no puede ser negativa.");
        }
        piercing = Math.min(100.0, piercing);
        slashing = Math.min(100.0, slashing);
        blunt = Math.min(100.0, blunt);
    }

    /** Pondera el porcentaje por la fracción determinista de cobertura. */
    public ArmorProtectionProfile scaledBy(double factor) {
        if (!Double.isFinite(factor) || factor < 0 || factor > 1) {
            throw new IllegalArgumentException("La cobertura debe estar entre 0 y 1.");
        }
        return new ArmorProtectionProfile(piercing * factor, slashing * factor, blunt * factor);
    }
}
