package domain.inventory.item;

public enum GripMode {
    ONE_HANDED("Una mano"),
    TWO_HANDED("Dos manos");

    private final String label;

    GripMode(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
