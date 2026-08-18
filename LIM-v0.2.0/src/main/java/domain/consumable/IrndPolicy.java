package domain.consumable;

import domain.character.sheet.Attribute;

/** Estado bifásico  del I-RND. Los 0-30 min sólo dan bonificaciones (salvo insomnio común); 30-60 min sólo penalizaciones. */
public final class IrndPolicy {
    public static final double BENEFIT_GAME_MINUTES = 30.0;
    public static final double AFTEREFFECT_GAME_MINUTES = 30.0;
    public static final double TOTAL_GAME_MINUTES = 60.0;
    public enum Phase { INACTIVE, BENEFIT, AFTEREFFECT }
    public Phase phase(double elapsedGameMinutes) {
        if (!Double.isFinite(elapsedGameMinutes) || elapsedGameMinutes < 0) throw new IllegalArgumentException("Tiempo inválido.");
        if (elapsedGameMinutes < BENEFIT_GAME_MINUTES) return Phase.BENEFIT;
        if (elapsedGameMinutes < TOTAL_GAME_MINUTES) return Phase.AFTEREFFECT;
        return Phase.INACTIVE;
    }
    public int effectiveAttribute(Attribute attribute, int ordinaryValue, double elapsedGameMinutes) {
        Phase p=phase(elapsedGameMinutes);
        if (p==Phase.BENEFIT && (attribute==Attribute.INTELIGENCIA || attribute==Attribute.FE || attribute==Attribute.CARISMA || attribute==Attribute.CLARIVIDENCIA)) return 75;
        if (p==Phase.AFTEREFFECT && attribute==Attribute.DESTREZA) return Math.min(20, ordinaryValue);
        return ordinaryValue;
    }
    public boolean canSleep(double elapsedGameMinutes) { return phase(elapsedGameMinutes)==Phase.INACTIVE; }
    public boolean canClimb(double elapsedGameMinutes) { return phase(elapsedGameMinutes)!=Phase.AFTEREFFECT; }
    public boolean canSwim(double elapsedGameMinutes) { return phase(elapsedGameMinutes)!=Phase.AFTEREFFECT; }
    public boolean canUsePersonalTransport(double elapsedGameMinutes) { return phase(elapsedGameMinutes)!=Phase.AFTEREFFECT; }
    public int vitalityForHealthRegeneration(int ordinaryVitality,double elapsedGameMinutes){ return phase(elapsedGameMinutes)==Phase.AFTEREFFECT?1:ordinaryVitality; }
    /** Tres tercios de carga: 5 s, expresamente inmune a LIBERACIÓN HELICOIDAL. */
    public double fullStaminaRecoverySeconds(double elapsedGameMinutes,double ordinarySeconds){ return phase(elapsedGameMinutes)==Phase.AFTEREFFECT?5.0:ordinarySeconds; }
    public double staminaRegenDelaySeconds(double elapsedGameMinutes,double ordinaryDelay){ return phase(elapsedGameMinutes)==Phase.AFTEREFFECT?1.20:ordinaryDelay; }
    public boolean aftereffectsUnmitigable(double elapsedGameMinutes){ return phase(elapsedGameMinutes)==Phase.AFTEREFFECT; }
}
