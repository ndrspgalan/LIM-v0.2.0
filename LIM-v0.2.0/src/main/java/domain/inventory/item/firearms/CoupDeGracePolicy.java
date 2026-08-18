package domain.inventory.item.firearms;

/** GOLPE DE GRACIA sólo puede atravesar una HEAD no cubierta por completo. */
public final class CoupDeGracePolicy {
    private CoupDeGracePolicy() {}

    public static boolean isCoupDeGrace(boolean headImpact, double headCoveragePercent,
            double headPiercingProtection, double projectilePiercingLethality) {
        validate(headCoveragePercent, headPiercingProtection, projectilePiercingLethality);
        return headImpact
                && headCoveragePercent < 100.0
                && projectilePiercingLethality > headPiercingProtection;
    }

    public static double totalHealthAfterImpact(double currentTotalHealth, boolean headImpact, double headCoveragePercent,
            double headPiercingProtection, double rawPiercingDamage) {
        if (!Double.isFinite(currentTotalHealth) || currentTotalHealth < 0)
            throw new IllegalArgumentException("Los PV TOTALES deben ser finitos y no negativos.");
        return isCoupDeGrace(headImpact, headCoveragePercent, headPiercingProtection, rawPiercingDamage)
                ? 0.0 : currentTotalHealth;
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
