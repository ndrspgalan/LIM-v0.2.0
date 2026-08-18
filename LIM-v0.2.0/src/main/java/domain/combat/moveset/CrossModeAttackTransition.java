package domain.combat.moveset;

public record CrossModeAttackTransition(ModeAttackRef from, ModeAttackRef to, TransitionContinuity continuity, String rationale) {
    public CrossModeAttackTransition { if(from==null||to==null||continuity==null||rationale==null||rationale.isBlank()) throw new IllegalArgumentException("Transición cruzada incompleta"); }
    public double executionTimeMultiplier(){ return continuity.executionTimeMultiplier(); }
}
