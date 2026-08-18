package domain.environment.time.randomizer;

import domain.environment.time.WeatherSeason;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class ClimateRandomizerSet {
    private final Map<WeatherSeason, WeatherRandomizer> seasonal;
    private final NeutralPhenomenonRandomizer neutral;

    public ClimateRandomizerSet(
            WeatherRandomizer spring,
            WeatherRandomizer summer,
            WeatherRandomizer autumn,
            WeatherRandomizer winter,
            NeutralPhenomenonRandomizer neutral) {
        this.seasonal = new EnumMap<>(WeatherSeason.class);
        register(spring); register(summer); register(autumn); register(winter);
        if (seasonal.size() != WeatherSeason.values().length) {
            throw new IllegalArgumentException("Debe existir exactamente un randomizador por estación.");
        }
        this.neutral = Objects.requireNonNull(neutral);
    }

    public static ClimateRandomizerSet defaults() {
        return new ClimateRandomizerSet(
                new SpringWeatherRandomizer(), new SummerWeatherRandomizer(),
                new AutumnWeatherRandomizer(), new WinterWeatherRandomizer(),
                new NeutralPhenomenonRandomizer());
    }

    public WeatherRandomizer forSeason(WeatherSeason season) {
        return seasonal.get(Objects.requireNonNull(season));
    }

    public NeutralPhenomenonRandomizer neutral() { return neutral; }

    private void register(WeatherRandomizer randomizer) {
        Objects.requireNonNull(randomizer);
        if (seasonal.put(randomizer.season(), randomizer) != null) {
            throw new IllegalArgumentException("Randomizador estacional duplicado: " + randomizer.season());
        }
    }
}
