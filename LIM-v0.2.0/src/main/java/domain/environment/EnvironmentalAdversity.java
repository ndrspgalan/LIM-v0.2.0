package domain.environment;

import domain.combat.DamageType;

import java.util.Optional;

public enum EnvironmentalAdversity {
    NORMAL(null, false),
    BITING_FROST(DamageType.FROST, false),
    SUFFOCATING_HEAT(DamageType.BURN, true),
    VIRULENT_TOXICITY(DamageType.POISON, true),
    SOAKED(null, false);

    private final DamageType continuousDamageType;
    private final boolean drainsHealth;

    EnvironmentalAdversity(DamageType continuousDamageType, boolean drainsHealth) {
        this.continuousDamageType = continuousDamageType;
        this.drainsHealth = drainsHealth;
    }

    public Optional<DamageType> continuousDamageType() { return Optional.ofNullable(continuousDamageType); }
    public boolean drainsHealth() { return drainsHealth; }
    public boolean createsNaturalConductor() { return this == SOAKED; }
}
