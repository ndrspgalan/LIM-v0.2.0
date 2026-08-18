package domain.environment.time;

import java.time.Duration;
import java.util.Objects;

public record AtmosphericPhenomenonOccurrence(AtmosphericPhenomenon phenomenon, Duration remainingDuration) {
    public AtmosphericPhenomenonOccurrence {
        phenomenon = Objects.requireNonNull(phenomenon);
        remainingDuration = Objects.requireNonNull(remainingDuration);
        if (remainingDuration.isNegative()) throw new IllegalArgumentException("La duración restante no puede ser negativa.");
        if (phenomenon == AtmosphericPhenomenon.NONE && !remainingDuration.isZero()) {
            throw new IllegalArgumentException("La ausencia de fenómeno debe tener duración cero.");
        }
        if (phenomenon != AtmosphericPhenomenon.NONE && remainingDuration.isZero()) {
            throw new IllegalArgumentException("Un fenómeno activo debe tener duración positiva.");
        }
    }

    public static AtmosphericPhenomenonOccurrence none() {
        return new AtmosphericPhenomenonOccurrence(AtmosphericPhenomenon.NONE, Duration.ZERO);
    }

    public boolean isActive() { return phenomenon.isPresent() && !remainingDuration.isZero(); }

    public AtmosphericPhenomenonOccurrence elapse(Duration elapsed) {
        Objects.requireNonNull(elapsed);
        if (elapsed.isNegative()) throw new IllegalArgumentException("No puede retrocederse un fenómeno.");
        if (!isActive()) return this;
        Duration remaining = remainingDuration.minus(elapsed);
        return remaining.isNegative() || remaining.isZero()
                ? none()
                : new AtmosphericPhenomenonOccurrence(phenomenon, remaining);
    }
}
