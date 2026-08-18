package domain.survival;

public record FoodConsumptionResult(
        HungerState state,
        int hungerLevelsRemoved,
        boolean varietyBonusApplied,
        boolean extendedSatietyCycle
) {}
