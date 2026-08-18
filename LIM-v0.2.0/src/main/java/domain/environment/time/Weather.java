package domain.environment.time;

import java.util.EnumSet;
import java.util.Set;

public enum Weather {
    /** Alias histórico conservado para partidas y verificaciones anteriores. */
    CLEAR(WeatherSeason.SPRING, "Despejado", all(), profile(18, 1.0, 1.0, 0.10, PrecipitationKind.NONE, GroundCondition.DRY, 0, 0)),

    SPRING_CLEAR(WeatherSeason.SPRING, "Despejado primaveral", all(), profile(18, 1.0, 1.0, 0.10, PrecipitationKind.NONE, GroundCondition.DRY, 0, 0)),
    SPRING_PARTLY_CLOUDY(WeatherSeason.SPRING, "Parcialmente nublado", dayAfternoon(), profile(16, .92, .85, .15, PrecipitationKind.NONE, GroundCondition.DRY, 0, 0)),
    SPRING_OVERCAST(WeatherSeason.SPRING, "Nublado primaveral", dayAfternoon(), profile(14, .82, .65, .18, PrecipitationKind.NONE, GroundCondition.DAMP, 0, .02)),
    SPRING_LIGHT_RAIN(WeatherSeason.SPRING, "Lluvia ligera", dayAfternoon(), profile(13, .72, .58, .20, PrecipitationKind.DRIZZLE, GroundCondition.WET, 0, .04)),
    SPRING_PERSISTENT_RAIN(WeatherSeason.SPRING, "Lluvia persistente", all(), profile(11, .58, .48, .28, PrecipitationKind.RAIN, GroundCondition.FLOODED, 0, .08)),
    SPRING_THUNDERSTORM(WeatherSeason.SPRING, "Tormenta eléctrica", afternoonNight(), profile(12, .45, .42, .60, PrecipitationKind.HEAVY_RAIN, GroundCondition.FLOODED, 0, .15)),
    SPRING_MORNING_FOG(WeatherSeason.SPRING, "Niebla matinal", only(DayPhase.DAY), profile(12, .30, .55, .05, PrecipitationKind.NONE, GroundCondition.DAMP, 0, .03)),
    SPRING_LUMINOUS_BLOOM(WeatherSeason.SPRING, "Floración luminosa", afternoonNight(), profile(17, .95, 1.08, .08, PrecipitationKind.PETALS, GroundCondition.DRY, 0, 0)),

    SUMMER_CLEAR(WeatherSeason.SUMMER, "Despejado estival", all(), profile(30, 1.0, 1.12, .08, PrecipitationKind.NONE, GroundCondition.DRY, 0, .05)),
    SUMMER_HEAT_HAZE(WeatherSeason.SUMMER, "Calima", dayAfternoon(), profile(34, .55, .95, .12, PrecipitationKind.NONE, GroundCondition.DRY, .06, .18)),
    SUMMER_HEATWAVE(WeatherSeason.SUMMER, "Ola de calor", only(DayPhase.AFTERNOON), profile(41, .75, 1.15, .06, PrecipitationKind.NONE, GroundCondition.DRY, 0, .35)),
    SUMMER_DRY_STORM(WeatherSeason.SUMMER, "Tormenta seca", afternoonNight(), profile(35, .62, .72, .72, PrecipitationKind.NONE, GroundCondition.DRY, .03, .22)),
    SUMMER_ELECTRICAL_STORM(WeatherSeason.SUMMER, "Tormenta eléctrica intensa", afternoonNight(), profile(25, .38, .40, .82, PrecipitationKind.HEAVY_RAIN, GroundCondition.FLOODED, 0, .25)),
    SUMMER_RAIN(WeatherSeason.SUMMER, "Lluvia de verano", afternoonNight(), profile(24, .70, .62, .25, PrecipitationKind.RAIN, GroundCondition.WET, 0, .04)),
    SUMMER_RED_SUNSET(WeatherSeason.SUMMER, "Atardecer rojo", only(DayPhase.AFTERNOON), profile(29, .95, .92, .12, PrecipitationKind.NONE, GroundCondition.DRY, 0, 0)),

    AUTUMN_CLEAR(WeatherSeason.AUTUMN, "Despejado otoñal", all(), profile(14, .98, .88, .18, PrecipitationKind.NONE, GroundCondition.DRY, 0, 0)),
    AUTUMN_DRIZZLE(WeatherSeason.AUTUMN, "Llovizna", dayAfternoon(), profile(11, .70, .55, .20, PrecipitationKind.DRIZZLE, GroundCondition.WET, 0, .05)),
    AUTUMN_CONTINUOUS_RAIN(WeatherSeason.AUTUMN, "Lluvia continua", all(), profile(9, .55, .45, .35, PrecipitationKind.RAIN, GroundCondition.FLOODED, 0, .10)),
    AUTUMN_FOG(WeatherSeason.AUTUMN, "Niebla", only(DayPhase.DAY), profile(8, .25, .48, .06, PrecipitationKind.NONE, GroundCondition.DAMP, 0, .05)),
    AUTUMN_STRONG_WIND(WeatherSeason.AUTUMN, "Viento fuerte", all(), profile(10, .90, .75, .65, PrecipitationKind.NONE, GroundCondition.DRY, 0, .12)),
    AUTUMN_GALE(WeatherSeason.AUTUMN, "Vendaval", afternoonNight(), profile(8, .62, .52, .92, PrecipitationKind.RAIN, GroundCondition.WET, 0, .30)),
    AUTUMN_STORM(WeatherSeason.AUTUMN, "Tormenta otoñal", afternoonNight(), profile(9, .42, .40, .75, PrecipitationKind.HEAVY_RAIN, GroundCondition.FLOODED, 0, .20)),

    WINTER_CLEAR(WeatherSeason.WINTER, "Despejado invernal", all(), profile(1, 1.0, .88, .12, PrecipitationKind.NONE, GroundCondition.FROZEN, 0, .08)),
    WINTER_GREY_SKY(WeatherSeason.WINTER, "Cielo gris", dayAfternoon(), profile(-1, .82, .48, .18, PrecipitationKind.NONE, GroundCondition.FROZEN, 0, .10)),
    WINTER_SNOW(WeatherSeason.WINTER, "Nevada", all(), profile(-4, .58, .62, .28, PrecipitationKind.SNOW, GroundCondition.SNOW_COVERED, 0, .18)),
    WINTER_BLIZZARD(WeatherSeason.WINTER, "Ventisca", afternoonNight(), profile(-10, .18, .35, .95, PrecipitationKind.SNOW, GroundCondition.SNOW_COVERED, 0, .45)),
    WINTER_HAIL(WeatherSeason.WINTER, "Granizada", only(DayPhase.AFTERNOON), profile(-2, .48, .48, .60, PrecipitationKind.HAIL, GroundCondition.FROZEN, 0, .25)),
    WINTER_FREEZING_FOG(WeatherSeason.WINTER, "Niebla helada", only(DayPhase.DAY), profile(-7, .20, .42, .08, PrecipitationKind.NONE, GroundCondition.FROZEN, 0, .20)),
    WINTER_SNOWSTORM(WeatherSeason.WINTER, "Tormenta de nieve", afternoonNight(), profile(-12, .12, .28, 1.0, PrecipitationKind.SNOW, GroundCondition.SNOW_COVERED, 0, .55)),
    WINTER_PERMANENT_FROST(WeatherSeason.WINTER, "Escarcha permanente", all(), profile(-6, .88, .72, .15, PrecipitationKind.NONE, GroundCondition.FROZEN, 0, .20));

    private final WeatherSeason season;
    private final String displayName;
    private final Set<DayPhase> manifestationPhases;
    private final WeatherProfile profile;

    Weather(WeatherSeason season, String displayName, Set<DayPhase> manifestationPhases, WeatherProfile profile) {
        this.season = season;
        this.displayName = displayName;
        this.manifestationPhases = Set.copyOf(manifestationPhases);
        this.profile = profile;
    }

    public WeatherSeason season() { return season; }
    public String displayName() { return displayName; }
    public Set<DayPhase> manifestationPhases() { return manifestationPhases; }
    public WeatherProfile profile() { return profile; }
    public boolean manifestsDuring(DayPhase phase) { return manifestationPhases.contains(phase); }
    public int gameMinutesDuration() { return 90; }

    public static Weather clearBaselineFor(WeatherSeason season) {
        return switch (season) {
            case SPRING -> SPRING_CLEAR;
            case SUMMER -> SUMMER_CLEAR;
            case AUTUMN -> AUTUMN_CLEAR;
            case WINTER -> WINTER_CLEAR;
        };
    }

    private static WeatherProfile profile(int temperature, double visibility, double luminosity, double wind,
                                          PrecipitationKind precipitation, GroundCondition ground, double toxicity, double fatigue) {
        return new WeatherProfile(temperature, visibility, luminosity, wind, precipitation, ground, toxicity, fatigue);
    }
    private static Set<DayPhase> all() { return EnumSet.allOf(DayPhase.class); }
    private static Set<DayPhase> dayAfternoon() { return EnumSet.of(DayPhase.DAY, DayPhase.AFTERNOON); }
    private static Set<DayPhase> afternoonNight() { return EnumSet.of(DayPhase.AFTERNOON, DayPhase.NIGHT); }
    private static Set<DayPhase> only(DayPhase phase) { return EnumSet.of(phase); }
}
