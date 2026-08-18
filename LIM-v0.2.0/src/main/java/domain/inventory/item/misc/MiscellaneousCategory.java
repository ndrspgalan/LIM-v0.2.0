package domain.inventory.item.misc;

public enum MiscellaneousCategory {
    FOOD("Alimento"),
    BEVERAGE("Bebida"),
    HEALING("Curación"),
    STIMULANT("Estimulante"),
    MONEY("Dinero"),
    OBJECT("Objeto");

    private final String label;
    MiscellaneousCategory(String label) { this.label = label; }
    public String label() { return label; }
}
