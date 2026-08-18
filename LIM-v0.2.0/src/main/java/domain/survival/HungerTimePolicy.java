package domain.survival;

public final class HungerTimePolicy {
    public HungerState advance(HungerState state, double hours) {
        return advance(state, hours, false);
    }

    public HungerState advance(HungerState state, double hours, boolean superiorCaloricRegulationActive) {
        if (hours < 0) throw new IllegalArgumentException("No puede retrocederse el hambre.");
        HungerLevel level = state.level();
        double remaining = state.hoursUntilNextStage();
        double elapsed = hours;
        while (elapsed >= remaining && level != HungerLevel.ACUTE_HUNGER) {
            elapsed -= remaining;
            level = HungerLevel.fromSeverity(level.severity() + 1);
            remaining = stageHours(level, superiorCaloricRegulationActive);
        }
        if (level != HungerLevel.ACUTE_HUNGER) remaining -= elapsed;
        return new HungerState(level, Math.max(0, remaining), state.lastConsumedFood());
    }

    private double stageHours(HungerLevel level, boolean superiorCaloricRegulationActive) {
        return level == HungerLevel.SATIATED
                ? HungerState.positiveStageHours(superiorCaloricRegulationActive)
                : HungerState.ORDINARY_STAGE_HOURS;
    }
}
