package domain.inventory.item.armor;

public enum ArmorForm {
    STANDARD,
    NECK_GAITER,
    INTEGRAL_SUIT;

    public boolean grantsNaturalFilter() { return this == NECK_GAITER; }
}
