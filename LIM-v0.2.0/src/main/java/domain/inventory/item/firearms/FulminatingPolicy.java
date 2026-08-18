package domain.inventory.item.firearms;

import domain.combat.ArmorCombatHitbox;

/**
 * : FULMINANTE sólo puede activarse en HELMET o CHEST.
 * HELMET exige cobertura <100%; CHEST exige cobertura <50%; en ambos casos
 * la perforación del proyectil debe superar la protección perforante porcentual efectiva.
 */
public final class FulminatingPolicy {
    private FulminatingPolicy() {}

    public static boolean isFulminatingImpact(ArmorCombatHitbox hitbox, double coveragePercent,
            double effectivePiercingProtection, double projectilePiercingLethality) {
        if (hitbox == null) throw new IllegalArgumentException("La hitbox no puede ser nula.");
        validate(coveragePercent, effectivePiercingProtection, projectilePiercingLethality);
        double coverageCap = switch (hitbox) {
            case HELMET -> 100.0;
            case CHEST -> 50.0;
            default -> -1.0;
        };
        return coverageCap > 0.0
                && coveragePercent < coverageCap
                && projectilePiercingLethality > effectivePiercingProtection;
    }

    public static boolean isFulminatingHeadImpact(boolean headImpact, double coveragePercent,
            double effectivePiercingProtection, double projectilePiercingLethality) {
        return headImpact && isFulminatingImpact(ArmorCombatHitbox.HELMET, coveragePercent,
                effectivePiercingProtection, projectilePiercingLethality);
    }

    /** Protección porcentual conjunta de capas secuenciales: 1 - producto de residuales. */
    public static double combinedPiercingProtection(double... layerProtectionPercents) {
        if (layerProtectionPercents == null) throw new IllegalArgumentException("Las capas no pueden ser nulas.");
        double residual = 1.0;
        for (double p : layerProtectionPercents) {
            if (!Double.isFinite(p) || p < 0 || p > 100)
                throw new IllegalArgumentException("Cada protección perforante debe estar entre 0 y 100.");
            residual *= 1.0 - p / 100.0;
        }
        return (1.0 - residual) * 100.0;
    }

    private static void validate(double coverage, double protection, double lethality) {
        if (!Double.isFinite(coverage) || coverage < 0 || coverage > 100)
            throw new IllegalArgumentException("La cobertura debe estar entre 0 y 100.");
        if (!Double.isFinite(protection) || protection < 0 || protection > 100)
            throw new IllegalArgumentException("La protección perforante porcentual debe estar entre 0 y 100.");
        if (!Double.isFinite(lethality) || lethality < 0)
            throw new IllegalArgumentException("La letalidad perforante no puede ser negativa.");
    }
}
