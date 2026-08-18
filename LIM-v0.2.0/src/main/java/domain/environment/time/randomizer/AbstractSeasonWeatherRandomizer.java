package domain.environment.time.randomizer;

import domain.environment.time.Weather;
import domain.environment.time.WeatherSeason;

import java.util.List;
import java.util.Objects;
import java.util.Random;

abstract class AbstractSeasonWeatherRandomizer implements WeatherRandomizer {
    private final WeatherSeason season;
    private final List<Weather> catalog;
    private final Random random;

    protected AbstractSeasonWeatherRandomizer(WeatherSeason season, List<Weather> catalog, Random random) {
        this.season = Objects.requireNonNull(season);
        this.catalog = List.copyOf(catalog);
        if (this.catalog.isEmpty()) throw new IllegalArgumentException("El catálogo climático no puede estar vacío.");
        if (this.catalog.stream().anyMatch(weather -> weather.season() != season)) {
            throw new IllegalArgumentException("Todos los climas deben pertenecer a la estación del randomizador.");
        }
        this.random = Objects.requireNonNull(random);
    }

    @Override public WeatherSeason season() { return season; }
    @Override public Weather roll() { return catalog.get(random.nextInt(catalog.size())); }
    public List<Weather> catalog() { return catalog; }
}
