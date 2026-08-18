package domain.movement;

import domain.character.Gender;
import java.util.Objects;

/**  — velocidad canónica escalada por altura corporal. Valores en metros por segundo. */
public final class LocomotionDistancePolicy {
    public double metersPerSecond(LocomotionMode mode, Gender gender, double heightMeters) {
        Objects.requireNonNull(mode); Objects.requireNonNull(gender);
        if (!Double.isFinite(heightMeters) || heightMeters <= 0) throw new IllegalArgumentException("Altura inválida.");
        return heightMeters * coefficient(mode, gender);
    }

    public double coefficient(LocomotionMode mode, Gender gender) {
        Objects.requireNonNull(mode); Objects.requireNonNull(gender);
        boolean male = gender == Gender.HOMBRE;
        return switch (mode) {
            case WALKING, SWIMMING -> male ? 0.80 : 0.82;
            case TROTTING, DIVING -> male ? 1.75 : 1.72;
            case RUNNING, FAST_SWIMMING -> male ? 3.20 : 3.05;
            case CROUCH_WALKING, CLIMBING -> male ? 0.45 : 0.46;
            case CRAWLING -> 0.30;
        };
    }
}
