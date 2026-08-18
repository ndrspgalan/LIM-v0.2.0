package domain.ability;

import domain.character.Gender;

/** Umbrales sexuados que no caben en el contrato escalar histórico de MasteryVariant. */
public final class MasteryGenderUnlockPolicy {
    private MasteryGenderUnlockPolicy() {}
    public static int kineticExplosionEndurance(Gender gender) { return gender == Gender.HOMBRE ? 20 : 15; }
    public static int potentialHardeningEndurance(Gender gender) { return gender == Gender.HOMBRE ? 40 : 30; }
    public static boolean kineticExplosionUnlocked(Gender gender, int endurance) { return endurance >= kineticExplosionEndurance(gender); }
    public static boolean potentialHardeningUnlocked(Gender gender, int endurance) { return endurance >= potentialHardeningEndurance(gender); }
}
