package domain.environment.time.randomizer;

import domain.environment.time.Weather;
import domain.environment.time.WeatherSeason;

import java.util.List;
import java.util.Random;

public final class SpringWeatherRandomizer extends AbstractSeasonWeatherRandomizer {
    private static final List<Weather> CATALOG = List.of(
            Weather.SPRING_CLEAR, Weather.SPRING_PARTLY_CLOUDY, Weather.SPRING_OVERCAST,
            Weather.SPRING_LIGHT_RAIN, Weather.SPRING_PERSISTENT_RAIN, Weather.SPRING_THUNDERSTORM,
            Weather.SPRING_MORNING_FOG, Weather.SPRING_LUMINOUS_BLOOM);
    public SpringWeatherRandomizer() { this(new Random()); }
    public SpringWeatherRandomizer(Random random) { super(WeatherSeason.SPRING, CATALOG, random); }
}
