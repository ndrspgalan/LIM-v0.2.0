package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;

/** Curva continua  gobernada por cinco estados seleccionables P50-P90. */
public final class ElectromagneticChargePolicy {
    public static final double HARD_MAX_TURNS = 35.0;
    public static final double JOULES_PER_EQUIVALENT_TURN = 1650.0 / HARD_MAX_TURNS;
    public static final double REVISED_GLOBAL_EFFICIENCY = 0.30;
    private static final double PROJECTILE_MASS_KG = 0.032;

    private ElectromagneticChargePolicy() {}

    public static ElectromagneticShotProfile resolve(double equivalentTurns) {
        return resolve(equivalentTurns, 8.70);
    }

    public static ElectromagneticShotProfile resolve(double equivalentTurns, double operationalWeaponMassKg) {
        if (!Double.isFinite(equivalentTurns) || equivalentTurns < 0) throw new IllegalArgumentException("Las vueltas equivalentes deben ser finitas y no negativas.");
        if (!Double.isFinite(operationalWeaponMassKg) || operationalWeaponMassKg <= 0) throw new IllegalArgumentException("La masa operativa debe ser positiva.");
        double turns = Math.min(equivalentTurns, HARD_MAX_TURNS);
        double stored = turns * JOULES_PER_EQUIVALENT_TURN;
        double effectiveEnergy = stored * REVISED_GLOBAL_EFFICIENCY;
        double piercing = interpolateSetting(turns, Value.PIERCING);
        double range = interpolateSetting(turns, Value.RANGE);
        double thermal = interpolateSetting(turns, Value.THERMAL);
        double projectileVelocity = effectiveEnergy <= 0 ? 0 : Math.sqrt((2.0 * effectiveEnergy) / PROJECTILE_MASS_KG);
        double recoil = (PROJECTILE_MASS_KG * projectileVelocity) / operationalWeaponMassKg;
        return new ElectromagneticShotProfile(turns, effectiveEnergy, new LethalityProfile(piercing, 0, 0), range, thermal, recoil);
    }

    public static ElectromagneticShotProfile resolve(ElectromagneticChargeSetting setting, double operationalWeaponMassKg) {
        return resolve(setting.equivalentTurns(), operationalWeaponMassKg);
    }

    private enum Value { PIERCING, RANGE, THERMAL }
    private static double interpolateSetting(double x, Value value) {
        double[] p = new double[ElectromagneticChargeSetting.values().length * 2 + 2];
        p[0] = 0; p[1] = 0;
        int i = 2;
        for (ElectromagneticChargeSetting s : ElectromagneticChargeSetting.values()) {
            p[i++] = s.equivalentTurns();
            p[i++] = switch (value) { case PIERCING -> s.piercing(); case RANGE -> s.rangeMeters(); case THERMAL -> s.thermalLockSeconds(); };
        }
        return interpolate(x, p);
    }

    private static double interpolate(double x, double... points) {
        for (int i = 0; i < points.length - 2; i += 2) {
            double x0 = points[i], y0 = points[i + 1], x1 = points[i + 2], y1 = points[i + 3];
            if (x <= x1) return x1 == x0 ? y1 : y0 + (y1 - y0) * ((x - x0) / (x1 - x0));
        }
        return points[points.length - 1];
    }
}
