package domain.combat;

/** Puntos de letalidad perdidos por canal al colisionar un arma cuerpo a cuerpo con HEAVY. */
public record WeaponProfileWearResult(double piercing, double slashing, double blunt) {
    public boolean any() { return piercing > 0 || slashing > 0 || blunt > 0; }
}
