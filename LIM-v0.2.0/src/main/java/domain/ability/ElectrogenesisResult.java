package domain.ability;

/** Electrogénesis sólo declara daño bruto; el aturdimiento se resuelve sobre electricidad neta. */
public record ElectrogenesisResult(int electricityDamage, boolean ignoredMirageIFrames) {
    public ElectrogenesisResult {
        if (electricityDamage < 0) throw new IllegalArgumentException("Resultado eléctrico inválido.");
    }
    public static ElectrogenesisResult none() { return new ElectrogenesisResult(0, false); }
}
