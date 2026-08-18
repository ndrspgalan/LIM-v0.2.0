package domain.survival;

import domain.inventory.item.misc.BeverageItem;
import domain.inventory.item.misc.FoodItem;
import domain.inventory.item.misc.TherapeuticItem;
import java.util.Objects;

/** Política exclusiva de hambre/sed. Los efectos terapéuticos tipados se resuelven aparte. */
public final class ConsumptionPolicy {
    private final FoodConsumptionPolicy foodPolicy = new FoodConsumptionPolicy();

    public ConsumptionResult consumeFood(HungerState hunger, ThirstState thirst, FoodItem item) {
        return consumeFood(hunger, thirst, item, false);
    }

    public ConsumptionResult consumeFood(HungerState hunger, ThirstState thirst, FoodItem item,
                                         boolean superiorCaloricRegulationActive) {
        Objects.requireNonNull(item);
        if (!item.consumeOne()) return ConsumptionResult.notConsumed(hunger);
        var food = foodPolicy.consume(hunger, item.foodType(), true, superiorCaloricRegulationActive);
        boolean hydrated = false;
        for (int i = 0; i < item.thirstRestored(); i++) hydrated |= thirst.restoreOne(false);
        for (int i = 0; i < item.thirstAdded(); i++) thirst.addOneLevel();
        return new ConsumptionResult(food.state(), true, hydrated, food.extendedSatietyCycle());
    }

    public ConsumptionResult drinkWater(HungerState hunger, ThirstState thirst, BeverageItem item) {
        return drinkWater(hunger, thirst, item, false);
    }

    public ConsumptionResult drinkWater(HungerState hunger, ThirstState thirst, BeverageItem item,
                                        boolean superiorCaloricRegulationActive) {
        if (!item.consumeOne()) return ConsumptionResult.notConsumed(hunger);
        boolean hydrated = thirst.drinkWater(superiorCaloricRegulationActive);
        return new ConsumptionResult(hunger, true, hydrated, false);
    }

    public ConsumptionResult drinkDirectly(HungerState hunger, ThirstState thirst) {
        return drinkDirectly(hunger, thirst, false);
    }

    public ConsumptionResult drinkDirectly(HungerState hunger, ThirstState thirst,
                                           boolean superiorCaloricRegulationActive) {
        return new ConsumptionResult(hunger, true, thirst.drinkWater(superiorCaloricRegulationActive), false);
    }

    public ConsumptionResult consumeTherapeutic(HungerState hunger, ThirstState thirst, TherapeuticItem item) {
        Objects.requireNonNull(item);
        if (item.isDepleted()) return ConsumptionResult.notConsumed(hunger);
        var effect = item.survivalEffect();
        HungerState next = hunger;
        boolean satiated = false;
        if (effect.foodType().isPresent()) {
            var food = foodPolicy.consume(hunger, effect.foodType().get(), effect.canActivateSatiated());
            next = food.state();
            satiated = food.extendedSatietyCycle();
        }
        boolean hydrated = false;
        for (int i = 0; i < effect.thirstRestored(); i++) {
            hydrated |= thirst.restoreOne(effect.canActivateHydrated());
        }
        for (int i = 0; i < effect.thirstAdded(); i++) thirst.addOneLevel();
        if (!item.consumeOne()) return ConsumptionResult.notConsumed(hunger);
        return new ConsumptionResult(next, true, hydrated, satiated);
    }

    public void refill(BeverageItem item) { item.refill(); }

    public record ConsumptionResult(HungerState hungerState, boolean consumed,
                                    boolean hydratedActivated, boolean satiatedActivated) {
        static ConsumptionResult notConsumed(HungerState hunger) {
            return new ConsumptionResult(hunger, false, false, false);
        }
    }
}
