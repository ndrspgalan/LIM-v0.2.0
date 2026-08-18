package domain.combat;

public enum DamageType {
    PIERCING(DamageCategory.CONVENTIONAL_PHYSICAL),
    SLASHING(DamageCategory.CONVENTIONAL_PHYSICAL),
    BLUNT(DamageCategory.CONVENTIONAL_PHYSICAL),
    POISON(DamageCategory.NON_CONVENTIONAL_PHYSICAL),
    BURN(DamageCategory.NON_CONVENTIONAL_PHYSICAL),
    FROST(DamageCategory.NON_CONVENTIONAL_PHYSICAL),
    ELECTRICITY(DamageCategory.NON_CONVENTIONAL_PHYSICAL),
    CURSE(DamageCategory.SPIRITUAL),
    FRENZY(DamageCategory.SPIRITUAL);

    private final DamageCategory category;

    DamageType(DamageCategory category) { this.category = category; }
    public DamageCategory category() { return category; }
    public boolean causesArmorWear() { return category == DamageCategory.CONVENTIONAL_PHYSICAL; }
}
