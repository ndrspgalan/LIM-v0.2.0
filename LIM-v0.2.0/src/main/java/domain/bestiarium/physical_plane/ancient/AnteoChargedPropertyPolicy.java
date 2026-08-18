package domain.bestiarium.physical_plane.ancient;

/**
 *  — propiedad CARGADO de Anteo. Tras resistencias, la electricidad neta rellena PA primero;
 * sólo el excedente llega a PV. No almacena electricidad ni modifica el siguiente ataque.
 */
public final class AnteoChargedPropertyPolicy {
    public record Result(double netElectricity, double staminaBefore, double staminaAfter,
                         double electricityConvertedToStamina, double healthDamage) {}

    public Result resolve(double netElectricity, double currentStamina, double maximumStamina) {
        if (!Double.isFinite(netElectricity) || netElectricity < 0) throw new IllegalArgumentException("Electricidad neta inválida.");
        if (!Double.isFinite(maximumStamina) || maximumStamina <= 0 || !Double.isFinite(currentStamina)
                || currentStamina < 0 || currentStamina > maximumStamina) throw new IllegalArgumentException("PA inválidos.");
        double missing = maximumStamina - currentStamina;
        double converted = Math.min(netElectricity, missing);
        double after = Math.min(maximumStamina, currentStamina + converted);
        double healthDamage = netElectricity - converted;
        return new Result(netElectricity, currentStamina, after, converted, healthDamage);
    }
}
