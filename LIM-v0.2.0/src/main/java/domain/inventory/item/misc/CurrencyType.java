package domain.inventory.item.misc;

public enum CurrencyType {
    VALERITA("Valeritas"),
    SUELDO("Sueldos"),
    BERYLARE("Berylares"),
    REAL_A5("Reales A5");

    private final String label;

    CurrencyType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
