package domain.inventory.item;

/** Perfil ofensivo con guardarraíl duro universal de 0 a 100 por canal. */
public record LethalityProfile(double piercing, double slashing, double blunt) {
    public LethalityProfile {
        if (piercing < 0 || slashing < 0 || blunt < 0) {
            throw new IllegalArgumentException("La letalidad no puede contener valores negativos.");
        }
        piercing = Math.min(100.0, piercing);
        slashing = Math.min(100.0, slashing);
        blunt = Math.min(100.0, blunt);
    }
}
