package domain.environment.time.randomizer;

import domain.environment.time.Weather;
import domain.environment.time.WeatherSeason;

import java.util.List;
import java.util.Random;

public final class WinterWeatherRandomizer extends AbstractSeasonWeatherRandomizer {
    private static final List<Weather> CATALOG = List.of(
            Weather.WINTER_CLEAR, Weather.WINTER_GREY_SKY, Weather.WINTER_SNOW,
            Weather.WINTER_BLIZZARD, Weather.WINTER_HAIL, Weather.WINTER_FREEZING_FOG,
            Weather.WINTER_SNOWSTORM, Weather.WINTER_PERMANENT_FROST);
    public WinterWeatherRandomizer() { this(new Random()); }
    public WinterWeatherRandomizer(Random random) { super(WeatherSeason.WINTER, CATALOG, random); }
}
