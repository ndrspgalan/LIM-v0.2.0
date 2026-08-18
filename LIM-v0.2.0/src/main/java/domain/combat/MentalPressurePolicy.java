package domain.combat;

/**
 * Contrato único para daño espiritual y su presión sobre CORDURA.
 * La resistencia mitiga el daño como porcentaje; CORDURA mitiga, también como
 * porcentaje, la presión mental que alimenta StaggerPolicy.
 */
public final class MentalPressurePolicy {
    private MentalPressurePolicy() {}

    public static Result resolve(double rawDamage, double resistancePercent, double sanityPercent) {
        validateNonNegative(rawDamage, "daño espiritual");
        validatePercent(resistancePercent, "resistencia espiritual");
        validateNonNegative(sanityPercent, "CORDURA");
        double effectiveSanityPercent = Math.min(100.0, sanityPercent);
        double netDamage = rawDamage * (1.0 - resistancePercent / 100.0);
        double mentalRecoilUnits = netDamage * (1.0 - effectiveSanityPercent / 100.0);
        return new Result(rawDamage, resistancePercent, netDamage, effectiveSanityPercent, mentalRecoilUnits);
    }

    private static void validatePercent(double value, String label) {
        if (!Double.isFinite(value) || value < 0 || value > 100) {
            throw new IllegalArgumentException("La " + label + " debe estar entre 0 y 100 %.");
        }
    }

    private static void validateNonNegative(double value, String label) {
        if (!Double.isFinite(value) || value < 0) {
            throw new IllegalArgumentException("Valor inválido de " + label + ".");
        }
    }

    public record Result(double rawDamage, double resistancePercent, double netDamage,
                         double effectiveSanityPercent, double mentalRecoilUnits) {}
}
