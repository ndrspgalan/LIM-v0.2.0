package domain.inventory.item;

public enum WeaponActionMode {
    PRIMARY("Modo principal"),
    ALTERNATIVE("Modo alternativo");

    private final String label;

    WeaponActionMode(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
