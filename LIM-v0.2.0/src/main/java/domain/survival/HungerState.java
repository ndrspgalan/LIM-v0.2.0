package domain.survival;

import domain.ability.HomeostasisThermalPolicy;

import java.util.Objects;
import java.util.Optional;

public record HungerState(HungerLevel level, double hoursUntilNextStage, Optional<FoodType> lastConsumedFood) {
    public static final double ORDINARY_STAGE_HOURS = 0.5;
    public static final double SUPERIOR_REGULATION_POSITIVE_STAGE_HOURS =
            ORDINARY_STAGE_HOURS * HomeostasisThermalPolicy.POSITIVE_SURVIVAL_STAGE_MULTIPLIER;

    public HungerState {
        Objects.requireNonNull(level, "El nivel de hambre no puede ser nulo.");
        Objects.requireNonNull(lastConsumedFood, "El último alimento no puede ser nulo.");
        if (hoursUntilNextStage < 0) throw new IllegalArgumentException("Las horas restantes no pueden ser negativas.");
    }

    public static HungerState initiallySatiated() {
        return initiallySatiated(false);
    }

    public static HungerState initiallySatiated(boolean superiorCaloricRegulationActive) {
        return new HungerState(HungerLevel.SATIATED,
                positiveStageHours(superiorCaloricRegulationActive), Optional.empty());
    }

    public static double positiveStageHours(boolean superiorCaloricRegulationActive) {
        return HomeostasisThermalPolicy.positiveSurvivalStageHours(
                ORDINARY_STAGE_HOURS, superiorCaloricRegulationActive);
    }

    public double staminaRegenerationMultiplier() {
        return switch (level) {
            case SATIATED, FUNCTIONAL -> 1.0;
            case HUNGRY -> 2.0 / 3.0;
            case MODERATE_HUNGER, ACUTE_HUNGER -> 1.0 / 3.0;
        };
    }

    public boolean staminaRegeneratesWhileMoving() { return level != HungerLevel.ACUTE_HUNGER; }
    public int physicalStabilityBonus() { return level == HungerLevel.SATIATED ? 1 : 0; }
    public int sanityBonus() { return level == HungerLevel.SATIATED ? 1 : 0; }
}
