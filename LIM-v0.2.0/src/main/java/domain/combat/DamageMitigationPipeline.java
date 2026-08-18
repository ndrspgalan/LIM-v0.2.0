package domain.combat;

import domain.status.HealthProtection;

/** Orden universal: CUSTODIA -> resistencias -> protector de PV -> PV. */
public final class DamageMitigationPipeline {
    private DamageMitigationPipeline() {}

    public static DamageResult resolve(double incomingDamage,
                                       double custodyAbsorption,
                                       double totalResistance,
                                       HealthProtection protection,
                                       double currentHealth) {
        if (!Double.isFinite(incomingDamage) || !Double.isFinite(custodyAbsorption)
                || !Double.isFinite(totalResistance) || !Double.isFinite(currentHealth)
                || incomingDamage < 0 || custodyAbsorption < 0 || totalResistance < 0 || totalResistance > 100 || currentHealth < 0) {
            throw new IllegalArgumentException("Valores de mitigación inválidos.");
        }
        if (protection == null) throw new IllegalArgumentException("El protector no puede ser nulo.");

        double afterCustody = Math.max(0.0, incomingDamage - custodyAbsorption);
        double afterResistance = afterCustody * (1.0 - totalResistance / 100.0);
        double absorbedByProtection = Math.min(afterResistance, protection.currentCapacity());
        HealthProtection protectionAfter = protection.absorb(absorbedByProtection);
        double finalDamage = afterResistance - absorbedByProtection;
        double healthAfter = Math.max(0.0, currentHealth - finalDamage);
        return new DamageResult(incomingDamage, afterCustody, afterResistance,
                absorbedByProtection, finalDamage, protectionAfter, healthAfter);
    }

    public record DamageResult(double incomingDamage, double afterCustody,
                               double afterResistances, double absorbedByProtection,
                               double finalDamage, HealthProtection protectionAfter,
                               double healthAfter) {}
}
