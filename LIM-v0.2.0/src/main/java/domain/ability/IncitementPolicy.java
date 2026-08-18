package domain.ability;

import domain.character.Gender;
import java.util.Objects;

/** Política común  de elegibilidad para INCITAR. */
public final class IncitementPolicy {
    private IncitementPolicy() {}
    public static boolean availableTo(IncitementManifestation manifestation, Gender userGender) {
        return IncitementEligibilityPolicy.userEligible(Objects.requireNonNull(manifestation), Objects.requireNonNull(userGender));
    }
    public static WarCryStaminaPolicy.Prepared warCry(Gender userGender, double currentPa, double totalPa) {
        return new WarCryStaminaPolicy().prepare(userGender, currentPa, totalPa);
    }
}
