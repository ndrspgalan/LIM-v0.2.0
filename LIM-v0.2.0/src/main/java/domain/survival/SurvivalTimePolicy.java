package domain.survival;

import java.time.Duration;
import java.util.Objects;

public final class SurvivalTimePolicy {
    private final HungerTimePolicy hungerTimePolicy = new HungerTimePolicy();

    public HungerState advance(HungerState hunger, ThirstState thirst, Duration realDuration) {
        return advance(hunger, thirst, realDuration, false);
    }

    public HungerState advance(HungerState hunger, ThirstState thirst, Duration realDuration,
                               boolean superiorCaloricRegulationActive) {
        Objects.requireNonNull(hunger); Objects.requireNonNull(thirst); Objects.requireNonNull(realDuration);
        if (realDuration.isNegative()) throw new IllegalArgumentException("El tiempo no puede ser negativo.");
        double gameHours = realDuration.toMillis() / 3_600_000.0 * 16.0;
        thirst.advanceHours(gameHours, superiorCaloricRegulationActive);
        return hungerTimePolicy.advance(hunger, gameHours, superiorCaloricRegulationActive);
    }
}
