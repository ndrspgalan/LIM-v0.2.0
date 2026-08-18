package domain.combat.ai.perception;

/** Contrato  de acciones compatibles con INVISIBILIDAD. */
public final class InvisibilityInteractionPolicy {
    public enum Interaction { UNARMED_ATTACK, SMALL_THROWABLE, QUICK_ACCESS_CONSUMABLE, MASTERY, DRAW_OR_EQUIP_WEAPON, EQUIP_ARMOR, OTHER }
    public boolean preserves(Interaction i){return switch(i){case UNARMED_ATTACK,SMALL_THROWABLE,QUICK_ACCESS_CONSUMABLE,MASTERY -> true;case DRAW_OR_EQUIP_WEAPON,EQUIP_ARMOR -> false;case OTHER -> true;};}
    public boolean targetLockAllowed(boolean invisible){return !invisible;}
    public PerceivedTargetEvidence evidenceWhileInvisible(boolean heard,boolean scented,boolean footprints,boolean impactOrigin,double seconds){
        return new PerceivedTargetEvidence(false,false,heard,scented,footprints,impactOrigin,seconds);
    }
}
