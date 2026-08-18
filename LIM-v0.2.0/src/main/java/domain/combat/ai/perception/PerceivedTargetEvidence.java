package domain.combat.ai.perception;

/** Evidencias concretas conservadas sin colapsarlas en etiquetas LOW/HIGH/etc. */
public record PerceivedTargetEvidence(boolean visualContact,boolean targetLockAllowed,boolean heard,boolean scented,boolean footprintsObserved,boolean impactOriginObserved,double secondsSinceLastEvidence){
    public PerceivedTargetEvidence{if(secondsSinceLastEvidence<0||!Double.isFinite(secondsSinceLastEvidence))throw new IllegalArgumentException("Tiempo de evidencia inválido.");}
    public boolean hasNonVisualEvidence(){return heard||scented||footprintsObserved||impactOriginObserved;}
    public boolean hasAnyEvidence(){return visualContact||hasNonVisualEvidence();}
    public static PerceivedTargetEvidence visible(){return new PerceivedTargetEvidence(true,true,false,false,false,false,0);}
}
