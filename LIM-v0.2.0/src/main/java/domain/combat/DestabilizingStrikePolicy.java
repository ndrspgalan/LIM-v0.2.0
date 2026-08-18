package domain.combat;

import domain.inventory.item.LethalityProfile;

/** Contrato común del golpe desestabilizador cuerpo a cuerpo: FUERZA se convierte en contundencia. */
public final class DestabilizingStrikePolicy {
    public LethalityProfile profile(int strength) {
        if (strength < 0) throw new IllegalArgumentException("La FUERZA no puede ser negativa.");
        return new LethalityProfile(0, 0, strength);
    }
}
