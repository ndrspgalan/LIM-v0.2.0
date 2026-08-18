package domain.inventory.item.firearms;

/**
 * Curva continua del Lanza-Arcos V881. Las tres bobinas comparten un banco de
 * condensadores de 1.650 J. La carga preferente combina manivela y batería y
 * cruza 3 / 5 / 6 vueltas equivalentes a los 1,2 / 2,1 / 3,0 s.
 */
public final class ArcChargePolicy {
    public static final double MODULE_I_TURNS = 3.0;
    public static final double MODULE_II_TURNS = 5.0;
    public static final double MODULE_III_TURNS = 6.0;
    public static final double MODULE_I_SECONDS = 1.2;
    public static final double MODULE_II_SECONDS = 2.1;
    public static final double MODULE_III_SECONDS = 3.0;
    public static final double MAX_TURNS = MODULE_III_TURNS;
    public static final double MAX_STORED_ELECTRICAL_ENERGY_J = 1650.0;
    public static final double JOULES_PER_EQUIVALENT_TURN = MAX_STORED_ELECTRICAL_ENERGY_J / MAX_TURNS;

    private ArcChargePolicy() {}

    public static ArcDischargeProfile resolve(double turns) {
        if (!Double.isFinite(turns) || turns < 0) throw new IllegalArgumentException("Las vueltas deben ser finitas y no negativas.");
        double t = Math.min(MAX_TURNS, turns);
        double reserve = piecewise(t, 0, 0, MODULE_I_TURNS, 100, MODULE_II_TURNS, 200, MODULE_III_TURNS, 300);
        double thermal = piecewise(t, 0, 0, MODULE_I_TURNS, MODULE_I_SECONDS, MODULE_II_TURNS, MODULE_II_SECONDS, MODULE_III_TURNS, MODULE_III_SECONDS);
        double shock = piecewise(t, 0, 0, MODULE_I_TURNS, 25.0, MODULE_II_TURNS, 50.0, MODULE_III_TURNS, 75.0);
        int modules = t >= MODULE_III_TURNS ? 3 : t >= MODULE_II_TURNS ? 2 : t >= MODULE_I_TURNS ? 1 : 0;
        return new ArcDischargeProfile(t, t * JOULES_PER_EQUIVALENT_TURN, reserve, modules, thermal, shock);
    }

    /** Tiempo de la acción preferente necesario para alcanzar una carga concreta. */
    public static double preferredChargeSecondsForTurns(double turns) {
        double t = Math.max(0.0, Math.min(MAX_TURNS, turns));
        return piecewise(t, 0, 0, MODULE_I_TURNS, MODULE_I_SECONDS, MODULE_II_TURNS, MODULE_II_SECONDS, MODULE_III_TURNS, MODULE_III_SECONDS);
    }

    /** Vueltas equivalentes acumuladas tras mantener la acción preferente. */
    public static double turnsAfterPreferredChargeSeconds(double seconds) {
        if (!Double.isFinite(seconds) || seconds < 0) throw new IllegalArgumentException("El tiempo debe ser finito y no negativo.");
        double s = Math.min(MODULE_III_SECONDS, seconds);
        if (s <= MODULE_I_SECONDS) return interpolate(s, 0, 0, MODULE_I_SECONDS, MODULE_I_TURNS);
        if (s <= MODULE_II_SECONDS) return interpolate(s, MODULE_I_SECONDS, MODULE_I_TURNS, MODULE_II_SECONDS, MODULE_II_TURNS);
        return interpolate(s, MODULE_II_SECONDS, MODULE_II_TURNS, MODULE_III_SECONDS, MODULE_III_TURNS);
    }

    private static double piecewise(double t, double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
        if (t <= x1) return interpolate(t, x0, y0, x1, y1);
        if (t <= x2) return interpolate(t, x1, y1, x2, y2);
        return interpolate(t, x2, y2, x3, y3);
    }

    private static double interpolate(double x, double xa, double ya, double xb, double yb) {
        if (xb == xa) return yb;
        double ratio = Math.max(0.0, Math.min(1.0, (x - xa) / (xb - xa)));
        return ya + (yb - ya) * ratio;
    }
}
