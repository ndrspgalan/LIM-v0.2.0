package application.rest;

import domain.environment.time.DayPhase;

import java.util.Objects;
import java.util.Optional;

public record SleepResult(boolean slept, Optional<DayPhase> awakenedPhase, String message) {
    public SleepResult {
        awakenedPhase = Objects.requireNonNull(awakenedPhase);
        Objects.requireNonNull(message);
    }

    public static SleepResult blocked(String message) {
        return new SleepResult(false, Optional.empty(), message);
    }

    public static SleepResult completed(DayPhase phase) {
        return new SleepResult(true, Optional.of(phase),
                "Descanso completado. PV y PA restaurados.");
    }

    public static SleepResult forced(DayPhase phase) {
        return new SleepResult(true, Optional.of(phase),
                "Límite de vigilia alcanzado. Sueño forzoso completado; PV y PA restaurados.");
    }
}
