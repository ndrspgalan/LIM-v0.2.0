package domain.inventory.item.rangedWeapons;

public enum RangedWeaponWearProfile {
    NON_DEGRADING(0.0),
    BOW_STANDARD(1.0),
    BOW_DOUBLE(2.0);

    private final double rateMultiplier;
    RangedWeaponWearProfile(double rateMultiplier){this.rateMultiplier=rateMultiplier;}
    public double rateMultiplier(){return rateMultiplier;}
    public boolean degrades(){return this!=NON_DEGRADING;}
}
