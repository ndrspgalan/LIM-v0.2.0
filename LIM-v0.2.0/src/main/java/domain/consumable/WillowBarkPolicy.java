package domain.consumable;

import domain.combat.DamageType;
/** Corteza de sauce: durante 30 min de juego sólo reduce VENENO en un tercio. */
public final class WillowBarkPolicy {
    public static final double POISON_DAMAGE_MULTIPLIER = 2.0 / 3.0;
    public double resolve(double netDamage, DamageType type, boolean active) {
        if (!Double.isFinite(netDamage) || netDamage < 0) throw new IllegalArgumentException("Daño inválido.");
        return active && type == DamageType.POISON ? netDamage * POISON_DAMAGE_MULTIPLIER : netDamage;
    }
}
