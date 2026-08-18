package domain.combat;

import java.util.List;

public record NonConventionalImpactResult(
        DamageType type,
        double rawDamage,
        double materialAdjustedDamage,
        double effectiveResistancePercent,
        double netDamage,
        double stunSeconds,
        List<String> amplifiedArmor
) {
    public NonConventionalImpactResult {
        if (type == null || type.category() != DamageCategory.NON_CONVENTIONAL_PHYSICAL) {
            throw new IllegalArgumentException("El resultado exige daño físico no convencional.");
        }
        if (rawDamage < 0 || materialAdjustedDamage < 0 || netDamage < 0 || stunSeconds < 0) {
            throw new IllegalArgumentException("Los valores de daño/aturdimiento no pueden ser negativos.");
        }
        if (effectiveResistancePercent < 0 || effectiveResistancePercent > 100) {
            throw new IllegalArgumentException("La resistencia efectiva debe estar entre 0 y 100 %.");
        }
        if (type != DamageType.ELECTRICITY && stunSeconds != 0.0) {
            throw new IllegalArgumentException("Sólo la electricidad puede producir aturdimiento eléctrico.");
        }
        amplifiedArmor = List.copyOf(amplifiedArmor);
    }
}
