package domain.ability;

import domain.character.Gender;

/** Manifestaciones activas de INCITAR. Ninguna posee cooldown. */
public enum IncitementManifestation {
    PROVOCAR(Gender.HOMBRE, true),
    GRITO_DE_GUERRA(Gender.HOMBRE, false),
    CAPITALIZAR(Gender.MUJER, false),
    RENTABILIZAR(Gender.MUJER, false);

    private final Gender gender;
    private final boolean requiresLockedTarget;

    IncitementManifestation(Gender gender, boolean requiresLockedTarget) {
        this.gender = gender;
        this.requiresLockedTarget = requiresLockedTarget;
    }

    public Gender gender() { return gender; }
    public boolean requiresLockedTarget() { return requiresLockedTarget; }
    public boolean hasCooldown() { return false; }
}
