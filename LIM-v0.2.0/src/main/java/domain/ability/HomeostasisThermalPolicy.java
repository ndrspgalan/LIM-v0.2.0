package domain.ability;

/** Contrato mecánico de Regulación calórica superior y Adaptación térmica. */
public final class HomeostasisThermalPolicy {
    public static final double POSITIVE_SURVIVAL_STAGE_MULTIPLIER = 2.0;
    public static final double SUSTAINED_COST_PER_REAL_SECOND = 1.0;
    private HomeostasisThermalPolicy() {}

    public static double positiveSurvivalStageHours(double ordinaryHours, boolean superiorRegulationActive) {
        if (ordinaryHours < 0) throw new IllegalArgumentException("La duración ordinaria no puede ser negativa.");
        return superiorRegulationActive ? ordinaryHours * POSITIVE_SURVIVAL_STAGE_MULTIPLIER : ordinaryHours;
    }

    public static double frostBuildUpAfterTick(double currentBuildUp, double incomingBuildUp, boolean sustainedActive) {
        return frostBuildUpAfterThermalAdaptationTick(currentBuildUp, incomingBuildUp, sustainedActive);
    }
    public static double frostBuildUpAfterThermalAdaptationTick(double currentBuildUp, double incomingBuildUp, boolean sustainedActive) {
        if (currentBuildUp < 0 || incomingBuildUp < 0) throw new IllegalArgumentException("Build-up inválido.");
        return sustainedActive && currentBuildUp > 0 ? currentBuildUp : currentBuildUp + incomingBuildUp;
    }
}
