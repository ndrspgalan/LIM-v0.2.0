package domain.inventory.item.misc;

import domain.survival.FoodType;
import java.util.Optional;

public record SurvivalConsumptionEffect(Optional<FoodType> foodType, int thirstRestored, int thirstAdded,
                                        boolean canActivateHydrated, boolean canActivateSatiated) {
    public SurvivalConsumptionEffect {
        if (foodType == null || thirstRestored < 0 || thirstAdded < 0) throw new IllegalArgumentException("Efecto de supervivencia no válido.");
    }
    public static SurvivalConsumptionEffect none() { return new SurvivalConsumptionEffect(Optional.empty(), 0, 0, false, false); }
}
