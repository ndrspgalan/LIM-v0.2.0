package domain.combat.moveset;
public record MeleeAttackTransition(String fromId,String toId,TransitionContinuity continuity,String rationale){
    public MeleeAttackTransition{
        if(fromId==null||fromId.isBlank()||toId==null||toId.isBlank())throw new IllegalArgumentException("Transición sin extremos.");
        if(continuity==null)throw new IllegalArgumentException("Continuidad obligatoria.");
        if(rationale==null||rationale.isBlank())throw new IllegalArgumentException("Racional cinético obligatorio.");
    }
    public double executionTimeMultiplier(){return continuity.executionTimeMultiplier();}
}
