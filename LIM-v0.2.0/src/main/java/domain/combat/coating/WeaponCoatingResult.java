package domain.combat.coating;

public record WeaponCoatingResult(boolean successful, int tearUnitsConsumed, WeaponCoating coating) {
    public static WeaponCoatingResult rejected() { return new WeaponCoatingResult(false, 0, null); }
}
