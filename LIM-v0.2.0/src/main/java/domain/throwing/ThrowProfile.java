package domain.throwing;

import domain.inventory.item.LethalityProfile;

import java.util.Objects;
import java.util.Optional;

/** Propiedades físicas y ofensivas permanentes de una unidad arrojable. */
public record ThrowProfile(double massKg, boolean recoverable, Optional<LethalityProfile> lethalityProfile) {
    public ThrowProfile {
        if (!Double.isFinite(massKg) || massKg <= 0) {
            throw new IllegalArgumentException("La masa arrojable debe ser positiva y finita.");
        }
        lethalityProfile = Objects.requireNonNull(lethalityProfile, "El perfil de letalidad opcional no puede ser nulo.");
    }

    public static ThrowProfile improvised(double massKg, boolean recoverable) {
        return new ThrowProfile(massKg, recoverable, Optional.empty());
    }

    public static ThrowProfile weapon(double massKg, boolean recoverable, LethalityProfile lethalityProfile) {
        return new ThrowProfile(massKg, recoverable, Optional.of(Objects.requireNonNull(lethalityProfile)));
    }
}
