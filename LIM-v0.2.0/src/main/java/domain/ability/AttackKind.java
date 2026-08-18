package domain.ability;
public enum AttackKind {
    LIGHT(1.0,1.0), HEAVY(1.20,1.20), CHARGED(1.30,1.30), JUMP(1.30,1.30), DESTABILIZE(1.30,1.30);
    private final double staminaMultiplier, bluntMultiplier;
    AttackKind(double staminaMultiplier,double bluntMultiplier){this.staminaMultiplier=staminaMultiplier;this.bluntMultiplier=bluntMultiplier;}
    public double staminaMultiplier(){return staminaMultiplier;} public double bluntMultiplier(){return bluntMultiplier;}
}
