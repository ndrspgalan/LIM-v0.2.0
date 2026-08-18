package domain.survival;

public enum FoodType {
    BREAD(1, true, true),
    JERKY(1, true, true),
    NUTS(1, true, true),
    CAKE(2, false, false),
    FRUIT(1, false, false),
    DRIED_GRAPES(1, true, true),
    MEAD(1, false, false);

    private final int baseHungerReduction;
    private final boolean canReceiveCombinationBonus;
    private final boolean canEnableCombinationBonus;

    FoodType(int baseHungerReduction, boolean canReceiveCombinationBonus, boolean canEnableCombinationBonus) {
        this.baseHungerReduction = baseHungerReduction;
        this.canReceiveCombinationBonus = canReceiveCombinationBonus;
        this.canEnableCombinationBonus = canEnableCombinationBonus;
    }

    public int baseHungerReduction() { return baseHungerReduction; }
    public boolean canReceiveCombinationBonus() { return canReceiveCombinationBonus; }
    public boolean canEnableCombinationBonus() { return canEnableCombinationBonus; }
}
