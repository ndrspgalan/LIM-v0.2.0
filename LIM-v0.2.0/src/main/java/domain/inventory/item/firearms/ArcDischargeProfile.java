package domain.inventory.item.firearms;

/** Estado observable de la descarga del Lanza-Arcos. */
public record ArcDischargeProfile(
        double crankTurns,
        double storedElectricalEnergyJ,
        double offensiveReserve,
        int fullyActiveModules,
        double thermalLockSeconds,
        double shockUnits
) {
    public ArcDischargeProfile {
        if (!Double.isFinite(crankTurns) || crankTurns < 0
                || !Double.isFinite(storedElectricalEnergyJ) || storedElectricalEnergyJ < 0
                || !Double.isFinite(offensiveReserve) || offensiveReserve < 0
                || fullyActiveModules < 0 || fullyActiveModules > 3
                || !Double.isFinite(thermalLockSeconds) || thermalLockSeconds < 0
                || !Double.isFinite(shockUnits) || shockUnits < 0) {
            throw new IllegalArgumentException("Perfil de descarga de arco inválido.");
        }
    }

    /**
     * Reparte toda la reserva eléctrica entre todos los objetivos válidos dentro
     * del alcance. Las tres bobinas limitan la reserva máxima (E300), no el número
     * de saltos del arco: E100 entre diez objetivos = E10 por objetivo.
     */
    public double electricalIntensityPerTarget(int validTargets) {
        if (validTargets <= 0) return 0.0;
        return offensiveReserve / validTargets;
    }

    /** el arco es una única exposición espacial repartida 9 % HEAD / 91 % BODY. */
    public domain.combat.AreaBodyDistributionPolicy.Split distributedElectricalIntensityPerTarget(int validTargets) {
        return domain.combat.AreaBodyDistributionPolicy.split(electricalIntensityPerTarget(validTargets));
    }
}
