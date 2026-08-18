package domain.character.sheet;

import domain.environment.time.DayPhase;

import java.util.Objects;

public record RunicStatisticsContext(DayPhase dayPhase) {
    public RunicStatisticsContext {
        Objects.requireNonNull(dayPhase, "La fase del día no puede ser nula.");
    }

    public static RunicStatisticsContext day() {
        return new RunicStatisticsContext(DayPhase.DAY);
    }
}
