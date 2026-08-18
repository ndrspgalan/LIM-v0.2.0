package domain.combat.coating;

public enum WeaponCoatingType {
    CURSE("Energía Maldita"),
    POISON("Veneno"),
    BURN("Quemadura"),
    ELECTRICITY("Electricidad");

    private final String label;
    WeaponCoatingType(String label) { this.label = label; }
    public String label() { return label; }
}
