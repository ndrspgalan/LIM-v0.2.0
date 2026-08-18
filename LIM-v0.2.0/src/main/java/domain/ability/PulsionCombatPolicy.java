package domain.ability;
/** RECICLAJE DE PULSIÓN: pasiva de AGUANTE 35-50 para finta y salto. */
public final class PulsionCombatPolicy {
    public static final int START_ENDURANCE=35, MAX_ENDURANCE=50;
    public static final double BASE_FEINT_COST=5.0, MIN_FEINT_COST=3.5;
    public static final double BASE_JUMP_HEIGHT_MULTIPLIER=1.0, MAX_JUMP_HEIGHT_MULTIPLIER=1.5;
    public double feintStaminaCost(int endurance, boolean active){return active?MasteryMath.linearMultiplier(endurance,35,50,5.0,3.5):5.0;}
    public double jumpHeightMultiplier(int endurance, boolean active){return active?MasteryMath.linearMultiplier(endurance,35,50,1.0,1.5):1.0;}
    /** Compatibilidad: PULSIÓN ya no amplifica contundencia. */
    public double multiplier(int endurance){return 1.0;}
    public double staminaCostMultiplier(boolean active,int endurance){return 1.0;}
}
