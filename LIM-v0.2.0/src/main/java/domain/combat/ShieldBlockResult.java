package domain.combat;

public record ShieldBlockResult(double characterDamage, double armorDamage, double appliedShieldWear,
                                double remainingBluntProtection, boolean shieldBroken,
                                StaggerResult stagger) {}
