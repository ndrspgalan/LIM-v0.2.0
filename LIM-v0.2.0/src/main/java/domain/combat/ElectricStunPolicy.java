package domain.combat;

/** Contrato único : cada punto de daño eléctrico neto produce 0,01 s de aturdimiento. */
public final class ElectricStunPolicy {
    public static final double STUN_SECONDS_PER_NET_DAMAGE = 0.01;

    private ElectricStunPolicy() {}

    public static double stunSeconds(double netElectricityDamage) {
        if (!Double.isFinite(netElectricityDamage) || netElectricityDamage < 0) {
            throw new IllegalArgumentException("El daño eléctrico neto debe ser finito y no negativo.");
        }
        return netElectricityDamage * STUN_SECONDS_PER_NET_DAMAGE;
    }
}
