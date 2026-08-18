package domain.environment.time.randomizer;

import domain.environment.time.Weather;
import domain.environment.time.WeatherSeason;

public interface WeatherRandomizer {
    WeatherSeason season();
    Weather roll();
}
