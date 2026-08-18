package domain.combat.ai.state;

/** Magnitudes continuas del combatiente; no introduce estados nominales de salud o agotamiento. */
public record CombatResourceState(double health,double maximumHealth,double stamina,double maximumStamina) {
    public CombatResourceState {
        if(!Double.isFinite(maximumHealth)||maximumHealth<=0||health<0||health>maximumHealth) throw new IllegalArgumentException("PV inválidos.");
        if(!Double.isFinite(maximumStamina)||maximumStamina<=0||stamina<0||stamina>maximumStamina) throw new IllegalArgumentException("PA inválidos.");
    }
    public double healthRatio(){return health/maximumHealth;}
    public double staminaRatio(){return stamina/maximumStamina;}
    public double missingHealth(){return maximumHealth-health;}
}
