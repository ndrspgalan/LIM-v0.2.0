package domain.environment.time.randomizer;

import domain.environment.time.Weather;
import domain.environment.time.WeatherSeason;

import java.util.List;
import java.util.Random;

public final class SummerWeatherRandomizer extends AbstractSeasonWeatherRandomizer {
    private static final List<Weather> CATALOG = List.of(
            Weather.SUMMER_CLEAR, Weather.SUMMER_HEAT_HAZE, Weather.SUMMER_HEATWAVE,
            Weather.SUMMER_DRY_STORM, Weather.SUMMER_ELECTRICAL_STORM,
            Weather.SUMMER_RAIN, Weather.SUMMER_RED_SUNSET);
    public SummerWeatherRandomizer() { this(new Random()); }
    public SummerWeatherRandomizer(Random random) { super(WeatherSeason.SUMMER, CATALOG, random); }
}
