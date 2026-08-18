package domain.character.sheet;

import domain.character.Gender;
import java.util.Objects;

/** Cadencia basal de aplicación de PV REGEN; la inyección estimulante la sustituye temporalmente por 1 s. */
public final class HealthRegenerationCadencePolicy {
    public double baselineSeconds(Gender gender) {
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        return gender == Gender.MUJER ? 5.0 : 6.0;
    }

    public double resolveSeconds(Gender gender, boolean stimulantInjectionActive) {
        return stimulantInjectionActive ? 1.0 : baselineSeconds(gender);
    }
}
