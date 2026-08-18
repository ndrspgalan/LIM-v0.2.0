package domain.combat.ai.observation;

/** Naturaleza perceptible de un ataque entrante para defensas contextuales. */
public enum AttackSourceType {
    MELEE,
    THROWN,
    RANGED_PROJECTILE,
    FIREARM_PROJECTILE,
    REMOTE_SPECIAL;

    public boolean physicalProjectile() {
        return this == THROWN || this == RANGED_PROJECTILE || this == FIREARM_PROJECTILE;
    }
}
