package domain.ability;

import domain.character.Gender;

/** Efecto persistente de PROVOCAR durante un encuentro hostil. */
public final class ProvokeEncounterPolicy {
    public static final double REGEN_DELAY_SECONDS = 1.20;
    public static final double RECOVERY_SECONDS = 5.0;
    public static final double RECOVERY_WITH_HELICAL_RELEASE_SECONDS = 3.0;

    public record Result(boolean applied, boolean targetLockAllowed, double regenDelaySeconds,
                         double fullRecoverySeconds, String reason) {}

    public Result resolve(Gender userGender, int userStrength, Gender targetGender, int targetStrength,
                          boolean hostileEncounter, boolean targetAttackingUserMelee, boolean targetHasHelicalRelease) {
        if (userGender != Gender.HOMBRE) return rejected("PROVOCAR pertenece al origen masculino.");
        if (targetGender != Gender.HOMBRE) return rejected("PROVOCAR no produce efecto contra mujeres.");
        if (!hostileEncounter) return rejected("PROVOCAR exige un encuentro hostil activo.");
        if (userStrength <= targetStrength) return rejected("FUERZA del usuario debe superar FUERZA del adversario.");
        return new Result(true, false, REGEN_DELAY_SECONDS,
                targetHasHelicalRelease ? RECOVERY_WITH_HELICAL_RELEASE_SECONDS : RECOVERY_SECONDS,
                "El adversario adopta el régimen de PA de tres tercios durante todo el encuentro; si ataca al provocador no puede fijarlo como blanco.");
    }

    private static Result rejected(String reason) { return new Result(false, true, 0, 0, reason); }
}
