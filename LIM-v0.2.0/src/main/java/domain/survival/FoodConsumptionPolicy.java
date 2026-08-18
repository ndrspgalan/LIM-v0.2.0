package domain.survival;

import java.util.Objects;
import java.util.Optional;

public final class FoodConsumptionPolicy {
    public FoodConsumptionResult consume(HungerState current, FoodType foodType) {
        return consume(current, foodType, true, false);
    }

    public FoodConsumptionResult consume(HungerState current, FoodType foodType, boolean canActivateSatiated) {
        return consume(current, foodType, canActivateSatiated, false);
    }

    public FoodConsumptionResult consume(HungerState current, FoodType foodType,
                                         boolean canActivateSatiated,
                                         boolean superiorCaloricRegulationActive) {
        Objects.requireNonNull(current, "El estado de hambre no puede ser nulo.");
        Objects.requireNonNull(foodType, "El tipo de alimento no puede ser nulo.");

        boolean combination = current.lastConsumedFood().filter(previous ->
                previous != foodType
                        && previous.canEnableCombinationBonus()
                        && foodType.canReceiveCombinationBonus()
        ).isPresent();
        int reduction = combination ? 2 : foodType.baseHungerReduction();
        int targetSeverity = current.level().severity() - reduction;
        boolean overflow = targetSeverity < 0;
        boolean satiatedBonus = overflow && canActivateSatiated;
        HungerLevel nextLevel = HungerLevel.fromSeverity(targetSeverity);
        double nextHours = nextLevel == HungerLevel.SATIATED
                ? HungerState.positiveStageHours(superiorCaloricRegulationActive)
                : HungerState.ORDINARY_STAGE_HOURS;

        HungerState next = new HungerState(nextLevel, nextHours, Optional.of(foodType));
        int removed = Math.min(current.level().severity(), reduction);
        return new FoodConsumptionResult(next, removed, combination, satiatedBonus);
    }
}
