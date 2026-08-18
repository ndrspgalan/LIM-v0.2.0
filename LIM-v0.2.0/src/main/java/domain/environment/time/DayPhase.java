package domain.environment.time;

public enum DayPhase {
    DAY,
    AFTERNOON,
    NIGHT;

    public DayPhase next() {
        return switch (this) {
            case DAY -> AFTERNOON;
            case AFTERNOON -> NIGHT;
            case NIGHT -> DAY;
        };
    }
}
