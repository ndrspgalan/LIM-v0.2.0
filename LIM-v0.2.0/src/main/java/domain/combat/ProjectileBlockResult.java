package domain.combat;

/** Resultado de interponer un ESCUDO IMPROVISADO ante un proyectil. */
public record ProjectileBlockResult(
        boolean intercepted,
        boolean stopped,
        PhysicalDamage residualDamage,
        double appliedWear,
        boolean parried,
        double parryStunSeconds,
        double parryRecoilMeters
) {
    public ProjectileBlockResult(boolean intercepted, boolean stopped, PhysicalDamage residualDamage, double appliedWear) {
        this(intercepted, stopped, residualDamage, appliedWear, false, 0.0, 0.0);
    }
}
