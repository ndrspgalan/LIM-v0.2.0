package domain.combat.runic;

/** Distingue ataques primarios de efectos secundarios y evita activaciones recursivas. */
public enum ImpactOrigin {
    PRIMARY_ATTACK(true),
    MIRROR_SEQUENCE(false),
    RESONANCE(false),
    RUNIC_COATING(false),
    ENVIRONMENT(false);

    private final boolean triggersRunicOffense;
    ImpactOrigin(boolean triggersRunicOffense) { this.triggersRunicOffense = triggersRunicOffense; }
    public boolean triggersRunicOffense() { return triggersRunicOffense; }
}
