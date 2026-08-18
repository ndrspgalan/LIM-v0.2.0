package domain.ability;

import domain.character.Gender;

import java.util.Objects;

/** Restricciones sexuales comunes de las cuatro manifestaciones de INCITAR. */
public final class IncitementEligibilityPolicy {
    private IncitementEligibilityPolicy() {}
    public static boolean userEligible(IncitementManifestation m, Gender user) {
        return Objects.requireNonNull(m).gender() == Objects.requireNonNull(user);
    }
    public static boolean targetEligible(IncitementManifestation m, Gender target) {
        Objects.requireNonNull(m); Objects.requireNonNull(target);
        return target == Gender.HOMBRE;
    }
}
