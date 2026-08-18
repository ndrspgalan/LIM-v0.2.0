package qa.regression;

import domain.environment.time.*;
import domain.environment.time.randomizer.*;

import java.time.Duration;
import java.util.Random;

public final class ExtraordinaryWeatherContinuityVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        verifyEveryRollProducesPhenomenon();
        verifyNewGameStartsWithPhenomenon();
        verifyHistoricalNoneIsNormalized();
        verifyExhaustedPhenomenonIsReplacedImmediately();
        verifyLatentPhenomenonStillExists();
        verifyMultiDayPhenomenonPersistsAcrossDawn();
    }

    private static void verifyEveryRollProducesPhenomenon() {
        NeutralPhenomenonRandomizer randomizer = new NeutralPhenomenonRandomizer(new Random(146));
        for (int i = 0; i < 1_000; i++) {
            AtmosphericPhenomenonOccurrence occurrence = randomizer.roll();
            org.junit.jupiter.api.Assertions.assertTrue(occurrence.isActive(), "El quinto randomizador no puede devolver NONE.");
            org.junit.jupiter.api.Assertions.assertTrue(occurrence.phenomenon().isPresent(), "Cada tirada debe seleccionar un fenómeno extraordinario.");
        }
    }

    private static void verifyNewGameStartsWithPhenomenon() {
        EnvironmentalCycle cycle = new EnvironmentalCycle();
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenonOccurrence().isActive(),
                "Toda partida nueva debe comenzar con una ocurrencia extraordinaria.");
    }

    private static void verifyHistoricalNoneIsNormalized() {
        EnvironmentalCycle cycle = cycle(DayPhase.DAY, AtmosphericPhenomenonOccurrence.none());
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenonOccurrence().isActive(),
                "Un snapshot histórico NONE debe normalizarse al construirse el ciclo.");
    }

    private static void verifyExhaustedPhenomenonIsReplacedImmediately() {
        AtmosphericPhenomenonOccurrence expiring = new AtmosphericPhenomenonOccurrence(
                AtmosphericPhenomenon.PETAL_RAIN, Duration.ofMinutes(1));
        EnvironmentalCycle cycle = cycle(DayPhase.DAY, expiring);
        cycle.advance(Duration.ofMinutes(1));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenonOccurrence().isActive(),
                "No puede existir un intervalo vacío después de agotar una ocurrencia.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenon() != AtmosphericPhenomenon.NONE,
                "La sustitución inmediata nunca puede producir NONE.");
    }

    private static void verifyLatentPhenomenonStillExists() {
        AtmosphericPhenomenonOccurrence meteor = new AtmosphericPhenomenonOccurrence(
                AtmosphericPhenomenon.METEOR_SHOWER, Duration.ofMinutes(30));
        EnvironmentalCycle cycle = cycle(DayPhase.DAY, meteor);
        cycle.advance(Duration.ofMinutes(20));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenon() == AtmosphericPhenomenon.METEOR_SHOWER,
                "Un fenómeno fuera de su franja debe permanecer latente.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenonOccurrence().remainingDuration().equals(Duration.ofMinutes(30)),
                "La duración latente no debe consumirse fuera de la franja de manifestación.");
        org.junit.jupiter.api.Assertions.assertTrue(!cycle.phenomenonManifestsNow(),
                "La ocurrencia puede existir sin manifestarse en el tramo actual.");
    }

    private static void verifyMultiDayPhenomenonPersistsAcrossDawn() {
        AtmosphericPhenomenonOccurrence comet = new AtmosphericPhenomenonOccurrence(
                AtmosphericPhenomenon.DISTANT_COMET, Duration.ofMinutes(180));
        EnvironmentalCycle cycle = cycle(DayPhase.DAY, comet);
        cycle.advance(Duration.ofMinutes(90));
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenon() == AtmosphericPhenomenon.DISTANT_COMET,
                "Un fenómeno pluridiario activo debe persistir durante el amanecer.");
        org.junit.jupiter.api.Assertions.assertTrue(cycle.phenomenonOccurrence().remainingDuration().equals(Duration.ofMinutes(90)),
                "El amanecer no debe reiniciar la duración de una ocurrencia persistente.");
    }

    private static EnvironmentalCycle cycle(DayPhase phase, AtmosphericPhenomenonOccurrence occurrence) {
        return new EnvironmentalCycle(phase, Duration.ZERO, Weather.SPRING_CLEAR, WeatherSeason.SPRING,
                occurrence, 0, deterministicSet());
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
