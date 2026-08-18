package domain.inventory.item.armor;

/**
 * : masa geométrica de las piezas V881 de ébano + wolframio.
 * Densidades de referencia: ébano seco ≈1030 kg/m3; wolframio ≈19250 kg/m3.
 * Las diez láminas de ébano V881 totalizan 6 mm y la camisa textil se aproxima por masa areal.
 */
public final class EbonyArmorMassPolicy {
    public static final int V881_EBONY_LAYERS = 10;
    public static final double EBONY_TOTAL_THICKNESS_M = 0.006;
    public static final double TUNGSTEN_THICKNESS_M = 0.0025;
    public static final double EBONY_DENSITY_KG_M3 = 1030.0;
    public static final double TUNGSTEN_DENSITY_KG_M3 = 19250.0;
    public static final double TEXTILE_AREAL_DENSITY_KG_M2 = 0.60;

    // Superficie defensiva efectiva del torso y del brazal izquierdo, no superficie corporal total.
    public static final double CHEST_DEFENSIVE_AREA_M2 = 0.470;
    public static final double LEFT_BRACER_DEFENSIVE_AREA_M2 = 0.037;

    private EbonyArmorMassPolicy() {}

    public static double v881PieceMassKg(double defensiveAreaM2) {
        if (!Double.isFinite(defensiveAreaM2) || defensiveAreaM2 <= 0) throw new IllegalArgumentException("El área defensiva debe ser positiva.");
        double ebony = defensiveAreaM2 * EBONY_TOTAL_THICKNESS_M * EBONY_DENSITY_KG_M3;
        double tungsten = defensiveAreaM2 * TUNGSTEN_THICKNESS_M * TUNGSTEN_DENSITY_KG_M3;
        double textile = defensiveAreaM2 * TEXTILE_AREAL_DENSITY_KG_M2;
        return ebony + tungsten + textile;
    }

    public static double v881ChestMassKg() { return v881PieceMassKg(CHEST_DEFENSIVE_AREA_M2); }
    public static double v881LeftBracerMassKg() { return v881PieceMassKg(LEFT_BRACER_DEFENSIVE_AREA_M2); }
}
