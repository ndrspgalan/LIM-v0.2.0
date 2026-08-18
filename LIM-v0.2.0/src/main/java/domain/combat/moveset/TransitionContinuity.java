package domain.combat.moveset;
/** Compatibilidad cinética entre salida y entrada. El multiplicador reduce recuperación/ejecución redundante. */
public enum TransitionContinuity {
    FORCED(1.15), NEUTRAL(1.00), NATURAL(0.90), EXCELLENT(0.80);
    private final double executionTimeMultiplier;
    TransitionContinuity(double executionTimeMultiplier){this.executionTimeMultiplier=executionTimeMultiplier;}
    public double executionTimeMultiplier(){return executionTimeMultiplier;}
}
