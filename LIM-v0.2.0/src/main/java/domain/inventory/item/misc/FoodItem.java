package domain.inventory.item.misc;

import domain.inventory.InventoryFootprint;
import domain.survival.FoodType;
import java.util.List;
import java.util.Objects;

public final class FoodItem extends StackableMiscellaneousItem {
    private final FoodType foodType;
    private final int thirstRestored;
    private final int thirstAdded;

    public FoodItem(String name, String description, FoodType foodType, int uses, int maximumUses,
                    InventoryFootprint footprint, double useDurationRealSeconds) {
        this(name, description, foodType, uses, maximumUses, 0.0, footprint, useDurationRealSeconds, 0, 0);
    }

    public FoodItem(String name, String description, FoodType foodType, int uses, int maximumUses,
                    double weightPerUseKg, InventoryFootprint footprint, double useDurationRealSeconds,
                    int thirstRestored, int thirstAdded) {
        super(name, description, MiscellaneousCategory.FOOD, uses, maximumUses, 0.0, weightPerUseKg,
                UseResourceKind.CHARGES, footprint, commonEatingAnimation(useDurationRealSeconds),
                List.of("HAMBRE | -" + foodType.baseHungerReduction() + " nivel(es)",
                        "BONIFICACIÓN | " + (foodType.canReceiveCombinationBonus() ? "Compatible" : "No compatible"),
                        "USOS | " + uses + " / " + maximumUses,
                        "TIEMPO DE CONSUMO | " + formatSeconds(useDurationRealSeconds)), List.of());
        this.foodType = Objects.requireNonNull(foodType);
        this.thirstRestored = thirstRestored;
        this.thirstAdded = thirstAdded;
    }
    public FoodType foodType() { return foodType; }
    public int thirstRestored() { return thirstRestored; }
    public int thirstAdded() { return thirstAdded; }
    private static UseAnimation commonEatingAnimation(double seconds) {
        return new UseAnimation(seconds, List.of("Extraer el alimento.", "Consumir una carga.", "Guardar el resto o desechar lo agotado."));
    }
    private static String formatSeconds(double seconds) { return String.format(java.util.Locale.ROOT, "%.1f s reales", seconds); }
}
