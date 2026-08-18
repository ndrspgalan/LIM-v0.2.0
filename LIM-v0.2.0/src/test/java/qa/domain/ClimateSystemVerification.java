package qa.domain;

import domain.environment.time.*;
import domain.environment.time.randomizer.*;

import java.time.Duration;
import java.util.Random;

public final class ClimateSystemVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyDefaultClimateContract();
        verifyFiveRandomizers();
        verifyWeatherChangesOnlyAtDawn();
        verifySeasonSwitchIsDeferredUntilDawn();
        verifyManifestationWindowsAndDurations();
        verifyNeutralPhenomenonCoexistsWithSeasonalWeather();
        verifyLongPhenomenonPersistsAcrossDawns();
    }

    private static void verifyDefaultClimateContract() {
        EnvironmentalCycle cycle = new EnvironmentalCycle();
        org.junit.jupiter.api.Assertions.assertTrue(cycle.activeSeason() == WeatherSeason.SPRING, "La partida nueva debe comenzar bajo primavera.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.weather().season() == WeatherSeason.SPRING, "El clima inicial debe ser primaveral.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.weather().gameMinutesDuration() == 90, "El clima dominante debe durar un día completo de 90 minutos.");
    }

    private static void verifyFiveRandomizers() {
        ClimateRandomizerSet set = deterministicSet();
        for (WeatherSeason season : WeatherSeason.values()) {
            org.junit.jupiter.api.Assertions.assertTrue(set.forSeason(season).roll().season() == season,
                    "Cada randomizador debe devolver únicamente climas de su estación.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(!set.neutral().catalog().isEmpty(), "El quinto randomizador neutral debe contener fenómenos extraordinarios.");
    }

    private static void verifyWeatherChangesOnlyAtDawn() {
        ClimateRandomizerSet set = deterministicSet();
        EnvironmentalCycle cycle = cycle(Weather.SPRING_CLEAR, WeatherSeason.SPRING,
                AtmosphericPhenomenonOccurrence.none(), set);
        Weather initial = cycle.weather();
        cycle.advance(Duration.ofMinutes(60));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phase() == DayPhase.NIGHT, "Sesenta minutos deben conducir a la noche.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.weather() == initial, "El clima no debe cambiar entre día, tarde y noche.");
        cycle.advance(Duration.ofMinutes(30));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phase() == DayPhase.DAY && cycle.completedDays() == 1,
                "El clima debe renovarse exactamente en el amanecer siguiente.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.weather().season() == WeatherSeason.SPRING, "El amanecer debe consumir el randomizador activo.");
    }

    private static void verifySeasonSwitchIsDeferredUntilDawn() {
        ClimateRandomizerSet set = deterministicSet();
        EnvironmentalCycle cycle = cycle(Weather.SPRING_LIGHT_RAIN, WeatherSeason.SPRING,
                AtmosphericPhenomenonOccurrence.none(), set);
        cycle.activateSeason(WeatherSeason.WINTER);
        org.junit.jupiter.api.Assertions.assertTrue(cycle.weather() == Weather.SPRING_LIGHT_RAIN,
                "Cambiar de randomizador no debe sustituir el clima a mitad del día.");
        cycle.advance(Duration.ofMinutes(90));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.weather().season() == WeatherSeason.WINTER,
                "El amanecer debe aplicar la estación narrativa seleccionada.");
    }

    private static void verifyManifestationWindowsAndDurations() {
        org.junit.jupiter.api.Assertions.assertTrue(Weather.SPRING_MORNING_FOG.manifestsDuring(DayPhase.DAY), "La niebla matinal debe manifestarse de día.");
        org.junit.jupiter.api.Assertions.assertTrue(!Weather.SPRING_MORNING_FOG.manifestsDuring(DayPhase.AFTERNOON), "La niebla matinal debe disiparse por la tarde.");
        EnvironmentalCycle fog = new EnvironmentalCycle(DayPhase.AFTERNOON, Duration.ZERO,
                Weather.SPRING_MORNING_FOG, WeatherSeason.SPRING, AtmosphericPhenomenonOccurrence.none(),
                0, deterministicSet());
        org.junit.jupiter.api.Assertions.assertTrue(fog.weatherProfile().visibilityFactor() == Weather.SPRING_CLEAR.profile().visibilityFactor(),
                "Fuera de su ventana, el perfil efectivo debe volver al despejado basal de la estación.");
        org.junit.jupiter.api.Assertions.assertTrue(Weather.SPRING_THUNDERSTORM.manifestsDuring(DayPhase.AFTERNOON)
                        && Weather.SPRING_THUNDERSTORM.manifestsDuring(DayPhase.NIGHT),
                "La tormenta primaveral debe pertenecer a tarde y noche.");
        org.junit.jupiter.api.Assertions.assertTrue(AtmosphericPhenomenon.METEOR_SHOWER.minimumDurationMinutes() == 30,
                "La lluvia de estrellas debe durar una noche.");
        org.junit.jupiter.api.Assertions.assertTrue(AtmosphericPhenomenon.DISTANT_COMET.maximumDurationMinutes() == 450,
                "El cometa debe poder persistir hasta cinco días de juego.");
    }

    private static void verifyNeutralPhenomenonCoexistsWithSeasonalWeather() {
        AtmosphericPhenomenonOccurrence meteor = new AtmosphericPhenomenonOccurrence(
                AtmosphericPhenomenon.METEOR_SHOWER, Duration.ofMinutes(30));
        EnvironmentalCycle cycle = cycle(Weather.SPRING_PERSISTENT_RAIN, WeatherSeason.SPRING,
                meteor, deterministicSet());
        cycle.advance(Duration.ofMinutes(60));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.weather() == Weather.SPRING_PERSISTENT_RAIN,
                "El fenómeno neutral no debe sustituir al clima estacional.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenon() == AtmosphericPhenomenon.METEOR_SHOWER && cycle.phenomenonManifestsNow(),
                "La lluvia de estrellas debe esperar hasta la noche y coexistir con el clima.");
        cycle.advance(Duration.ofMinutes(30));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenon().isPresent(),
                "Al agotarse un fenómeno, otro debe sustituirlo inmediatamente.");
    }

    private static void verifyLongPhenomenonPersistsAcrossDawns() {
        AtmosphericPhenomenonOccurrence comet = new AtmosphericPhenomenonOccurrence(
                AtmosphericPhenomenon.DISTANT_COMET, Duration.ofMinutes(180));
        EnvironmentalCycle cycle = cycle(Weather.SPRING_CLEAR, WeatherSeason.SPRING,
                comet, deterministicSet());
        cycle.advance(Duration.ofMinutes(90));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenon() == AtmosphericPhenomenon.DISTANT_COMET,
                "Un fenómeno pluridiario no debe ser reemplazado en el amanecer.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenonOccurrence().remainingDuration().equals(Duration.ofMinutes(90)),
                "El fenómeno debe consumir exactamente el tiempo durante el que se manifiesta.");
        cycle.advance(Duration.ofMinutes(90));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenon().isPresent(),
                "El fenómeno agotado debe ser sustituido sin intervalo vacío.");
    }

    private static EnvironmentalCycle cycle(Weather weather, WeatherSeason season,
                                            AtmosphericPhenomenonOccurrence occurrence,
                                            ClimateRandomizerSet set) {
        return new EnvironmentalCycle(DayPhase.DAY, Duration.ZERO, weather, season, occurrence, 0, set);
    }

    private static ClimateRandomizerSet deterministicSet() {
        return new ClimateRandomizerSet(
                new SpringWeatherRandomizer(new Random(1)),
                new SummerWeatherRandomizer(new Random(2)),
                new AutumnWeatherRandomizer(new Random(3)),
                new WinterWeatherRandomizer(new Random(4)),
                new NeutralPhenomenonRandomizer(new Random(5)));
    }

    
}
