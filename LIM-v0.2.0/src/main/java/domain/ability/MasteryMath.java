package domain.ability;

public final class MasteryMath {
    private MasteryMath() {}

    public static double linearMultiplier(int value, int minimum, int maximum, double from, double to) {
        if (maximum <= minimum) throw new IllegalArgumentException("Intervalo inválido.");
        int clamped = Math.max(minimum, Math.min(maximum, value));
        double ratio = (clamped - minimum) / (double) (maximum - minimum);
        return from + ratio * (to - from);
    }

    public static double attackStaminaCost(double weaponWeightKg, AttackKind attack, double pulsionMultiplier) {
        if (weaponWeightKg < 0 || pulsionMultiplier < 1) throw new IllegalArgumentException("Valores inválidos.");
        return weaponWeightKg * attack.staminaMultiplier() * pulsionMultiplier;
    }

    public static double feintStaminaCost(double equippedLoadKg, double pulsionMultiplier) {
        return equippedLoadKg * pulsionMultiplier;
    }

    public static double jumpStaminaCost(double equippedLoadKg, double pulsionMultiplier) {
        return equippedLoadKg * 1.5 * pulsionMultiplier;
    }

    public static double explosionRadiusMeters(double heightMeters, int endurance) {
        if (!Double.isFinite(heightMeters) || heightMeters <= 0) {
            throw new IllegalArgumentException("La altura debe ser positiva y finita.");
        }
        return heightMeters * 2.5;
    }

    public static double explosionLethality(int endurance) {
        if (endurance < 0) throw new IllegalArgumentException("El AGUANTE no puede ser negativo.");
        return endurance;
    }

    public static double nullificationDurationSeconds(int endurance) {
        return NullificationPolicy.suppressionSeconds(endurance);
    }

    /** Compatibilidad: ni RECICLAJE DE PULSIÓN ni TRAYECTORIA son multiplicadores globales permanentes. */
    public static double unarmedBluntDamageMultiplier(double ignoredPulsionMultiplier,
                                                       boolean convergentTrajectoryActive) {
        return 1.0;
    }

    public static double foundationalRadiusMeters(double heightMeters) { return heightMeters * 2.5; }
}
