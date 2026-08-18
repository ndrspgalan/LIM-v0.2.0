package domain.environment.time.randomizer;

import domain.environment.time.Weather;
import domain.environment.time.WeatherSeason;

import java.util.List;
import java.util.Random;

public final class AutumnWeatherRandomizer extends AbstractSeasonWeatherRandomizer {
    private static final List<Weather> CATALOG = List.of(
            Weather.AUTUMN_CLEAR, Weather.AUTUMN_DRIZZLE, Weather.AUTUMN_CONTINUOUS_RAIN,
            Weather.AUTUMN_FOG, Weather.AUTUMN_STRONG_WIND, Weather.AUTUMN_GALE, Weather.AUTUMN_STORM);
    public AutumnWeatherRandomizer() { this(new Random()); }
    public AutumnWeatherRandomizer(Random random) { super(WeatherSeason.AUTUMN, CATALOG, random); }
}
