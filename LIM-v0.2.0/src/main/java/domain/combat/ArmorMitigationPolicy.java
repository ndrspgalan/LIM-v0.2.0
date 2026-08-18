package domain.combat;

/** Fuente única de verdad para interpretar protección física 0-100 como porcentaje absorbido. */
public final class ArmorMitigationPolicy {
    private ArmorMitigationPolicy() {}

    public static double transmitted(double incomingDamage, double protectionPercent) {
        validate(incomingDamage, protectionPercent);
        return incomingDamage * (1.0 - protectionPercent / 100.0);
    }

    public static double absorbed(double incomingDamage, double protectionPercent) {
        validate(incomingDamage, protectionPercent);
        return incomingDamage * (protectionPercent / 100.0);
    }

    private static void validate(double incomingDamage, double protectionPercent) {
        if (!Double.isFinite(incomingDamage) || incomingDamage < 0) {
            throw new IllegalArgumentException("El daño entrante debe ser finito y no negativo.");
        }
        if (!Double.isFinite(protectionPercent) || protectionPercent < 0 || protectionPercent > 100) {
            throw new IllegalArgumentException("La protección porcentual debe estar entre 0 y 100.");
        }
    }
}
