package domain.inventory;

public enum InventoryOrientation {
    DEFAULT,
    ROTATED_90;

    public InventoryOrientation toggled(){ return this==DEFAULT ? ROTATED_90 : DEFAULT; }
}
