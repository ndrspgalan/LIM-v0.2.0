package domain.environment.time;

import java.util.EnumSet;
import java.util.Set;

/**
 * Capa extraordinaria independiente del clima estacional. Puede coexistir con cualquier Weather.
 */
public enum AtmosphericPhenomenon {
    NONE("Sin fenómeno extraordinario", all(), 0, 0),

    METEOR_SHOWER("Lluvia de estrellas", only(DayPhase.NIGHT), 30, 30),
    AURORA_BOREALIS("Aurora boreal", only(DayPhase.NIGHT), 30, 30),
    SCARLET_AURORA("Aurora escarlata", only(DayPhase.NIGHT), 30, 30),
    BLUE_MOON("Luna azul", only(DayPhase.NIGHT), 30, 30),
    BLOOD_MOON("Luna de sangre", only(DayPhase.NIGHT), 30, 30),
    LUNAR_ECLIPSE("Eclipse lunar", only(DayPhase.NIGHT), 30, 30),
    LIVING_CONSTELLATIONS("Constelaciones vivas", only(DayPhase.NIGHT), 30, 30),
    ASTRAL_MIST("Neblina astral", only(DayPhase.NIGHT), 30, 30),
    STARLESS_NIGHT("Noche sin estrellas", only(DayPhase.NIGHT), 30, 30),
    FIREFLY_RAIN("Lluvia de luciérnagas", only(DayPhase.NIGHT), 30, 30),
    BIOLUMINESCENT_WILDERNESS("Naturaleza bioluminiscente", only(DayPhase.NIGHT), 30, 30),

    SOLAR_ECLIPSE("Eclipse solar", only(DayPhase.AFTERNOON), 30, 30),
    SOLAR_HALO("Halo solar", dayAfternoon(), 30, 60),
    LUNAR_HALO("Halo lunar", only(DayPhase.NIGHT), 30, 30),
    SILENT_LIGHTNING("Relámpagos silenciosos", afternoonNight(), 30, 60),
    PRISMATIC_STORM("Tormenta prismática", afternoonNight(), 30, 60),
    FRACTURED_SKY("Cielo fracturado", afternoonNight(), 30, 60),
    PURPLE_HORIZON("Horizonte púrpura", afternoonNight(), 30, 60),
    CELESTIAL_CASCADE("Cascada celeste", afternoonNight(), 30, 60),

    PETAL_RAIN("Lluvia de pétalos", all(), 90, 90),
    LUMINOUS_CLOUD_SEA("Mar de nubes luminosas", all(), 90, 90),
    HORIZON_RINGS("Anillos luminosos del horizonte", all(), 90, 90),
    ASHEN_DAY("Día de ceniza", all(), 90, 90),
    BLACK_AURORA("Aurora negra", all(), 90, 90),
    CRYSTAL_RAIN("Lluvia de cristal", all(), 90, 90),
    RED_DUST_RAIN("Lluvia de polvo rojo", all(), 90, 90),
    DAYLIGHT_DARKNESS("Oscuridad diurna", all(), 90, 90),
    INVERTED_HEAVENS("Cielos invertidos", all(), 90, 90),

    DISTANT_COMET("Cometa distante", all(), 180, 450),
    FALLING_CELESTIAL_BODY("Caída de un cuerpo celeste", afternoonNight(), 30, 60),
    WORLD_WOUND("Herida del mundo", all(), 90, 270);

    private final String displayName;
    private final Set<DayPhase> manifestationPhases;
    private final int minimumDurationMinutes;
    private final int maximumDurationMinutes;

    AtmosphericPhenomenon(String displayName, Set<DayPhase> manifestationPhases,
                          int minimumDurationMinutes, int maximumDurationMinutes) {
        this.displayName = displayName;
        this.manifestationPhases = Set.copyOf(manifestationPhases);
        this.minimumDurationMinutes = minimumDurationMinutes;
        this.maximumDurationMinutes = maximumDurationMinutes;
    }

    public String displayName() { return displayName; }
    public Set<DayPhase> manifestationPhases() { return manifestationPhases; }
    public boolean manifestsDuring(DayPhase phase) { return this != NONE && manifestationPhases.contains(phase); }
    public int minimumDurationMinutes() { return minimumDurationMinutes; }
    public int maximumDurationMinutes() { return maximumDurationMinutes; }
    public boolean isPresent() { return this != NONE; }

    private static Set<DayPhase> all() { return EnumSet.allOf(DayPhase.class); }
    private static Set<DayPhase> dayAfternoon() { return EnumSet.of(DayPhase.DAY, DayPhase.AFTERNOON); }
    private static Set<DayPhase> afternoonNight() { return EnumSet.of(DayPhase.AFTERNOON, DayPhase.NIGHT); }
    private static Set<DayPhase> only(DayPhase phase) { return EnumSet.of(phase); }
}
