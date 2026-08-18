package domain.combat;

/** Canales defensivos realmente degradados por un impacto. */
public record ArmorProfileWearResult(double piercing, double slashing, double blunt) {
    public boolean any() { return piercing > 0 || slashing > 0 || blunt > 0; }
}
