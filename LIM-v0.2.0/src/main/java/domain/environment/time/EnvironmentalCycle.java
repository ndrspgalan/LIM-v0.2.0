package domain.environment.time;

import domain.environment.time.randomizer.ClimateRandomizerSet;

import java.time.Duration;
import java.util.Objects;

/**
 * Ciclo ambiental de tres tramos de 30 minutos. Un día completo dura 90 minutos de juego.
 * El clima dominante se selecciona únicamente al amanecer. El quinto randomizador neutral
 * permanece habilitado con independencia de la estación y mantiene en todo momento exactamente
 * una ocurrencia atmosférica extraordinaria, aunque pueda permanecer latente fuera de su franja.
 */
public final class EnvironmentalCycle {
    public static final Duration PHASE_DURATION = Duration.ofMinutes(30);
    public static final Duration DAY_DURATION = PHASE_DURATION.multipliedBy(3);

    private DayPhase phase;
    private Duration elapsedInPhase;
    private Weather weather;
    private WeatherSeason activeSeason;
    private AtmosphericPhenomenonOccurrence phenomenonOccurrence;
    private long completedDays;
    private final ClimateRandomizerSet randomizers;

    public EnvironmentalCycle() {
        this(DayPhase.DAY, Duration.ZERO, Weather.SPRING_CLEAR, WeatherSeason.SPRING,
                AtmosphericPhenomenonOccurrence.none(), 0, ClimateRandomizerSet.defaults());
    }

    /** Constructor histórico conservado para snapshots y verificaciones anteriores. */
    public EnvironmentalCycle(DayPhase phase, Duration elapsedInPhase, Weather weather) {
        this(phase, elapsedInPhase, weather, weather.season(), AtmosphericPhenomenonOccurrence.none(),
                0, ClimateRandomizerSet.defaults());
    }

    public EnvironmentalCycle(DayPhase phase, Duration elapsedInPhase, Weather weather,
                              WeatherSeason activeSeason,
                              AtmosphericPhenomenonOccurrence phenomenonOccurrence,
                              long completedDays,
                              ClimateRandomizerSet randomizers) {
        this.phase = Objects.requireNonNull(phase, "El tramo ambiental no puede ser nulo.");
        this.weather = Objects.requireNonNull(weather, "El tiempo atmosférico no puede ser nulo.");
        this.activeSeason = Objects.requireNonNull(activeSeason, "La estación climática activa no puede ser nula.");
        AtmosphericPhenomenonOccurrence suppliedOccurrence = Objects.requireNonNull(
                phenomenonOccurrence, "El fenómeno no puede ser nulo.");
        this.randomizers = Objects.requireNonNull(randomizers, "Los randomizadores no pueden ser nulos.");
        this.phenomenonOccurrence = suppliedOccurrence.isActive()
                ? suppliedOccurrence
                : this.randomizers.neutral().roll();
        if (completedDays < 0) throw new IllegalArgumentException("Los días completados no pueden ser negativos.");
        this.completedDays = completedDays;
        this.elapsedInPhase = normalizeElapsed(Objects.requireNonNull(elapsedInPhase,
                "El tiempo transcurrido no puede ser nulo."));
        if (weather.season() != activeSeason && weather != Weather.CLEAR) {
            throw new IllegalArgumentException("El clima inicial debe pertenecer a la estación activa.");
        }
    }

    public void advance(Duration elapsed) {
        Objects.requireNonNull(elapsed, "El tiempo avanzado no puede ser nulo.");
        if (elapsed.isNegative()) throw new IllegalArgumentException("No puede retrocederse el ciclo ambiental.");

        Duration remaining = elapsed;
        while (!remaining.isZero()) {
            Duration untilBoundary = PHASE_DURATION.minus(elapsedInPhase);
            Duration step = remaining.compareTo(untilBoundary) < 0 ? remaining : untilBoundary;
            elapsePhenomenonDuringCurrentPhase(step);
            elapsedInPhase = elapsedInPhase.plus(step);
            remaining = remaining.minus(step);

            if (elapsedInPhase.equals(PHASE_DURATION)) {
                transitionToNextPhase();
            }
        }
    }

    /** Finaliza el tramo actual y despierta exactamente al comienzo del siguiente. */
    public DayPhase completeCurrentPhase() {
        Duration remaining = remainingInPhase();
        elapsePhenomenonDuringCurrentPhase(remaining);
        transitionToNextPhase();
        return phase;
    }

    /**
     * Selecciona cuál de los cuatro randomizadores estacionales gobierna los amaneceres siguientes.
     * No fuerza un cambio climático a mitad del día.
     */
    public void activateSeason(WeatherSeason season) {
        this.activeSeason = Objects.requireNonNull(season);
    }

    public DayPhase phase() { return phase; }
    public Duration elapsedInPhase() { return elapsedInPhase; }
    public Duration remainingInPhase() { return PHASE_DURATION.minus(elapsedInPhase); }
    public Weather weather() { return weather; }
    public WeatherSeason activeSeason() { return activeSeason; }
    public AtmosphericPhenomenon phenomenon() { return phenomenonOccurrence.phenomenon(); }
    public AtmosphericPhenomenonOccurrence phenomenonOccurrence() { return phenomenonOccurrence; }
    public long completedDays() { return completedDays; }
    public boolean weatherManifestsNow() { return weather.manifestsDuring(phase); }
    public boolean phenomenonManifestsNow() { return phenomenonOccurrence.isActive() && phenomenon().manifestsDuring(phase); }
    /** Perfil mecánico efectivo del tramo actual. Fuera de su ventana, el clima dominante
     * conserva identidad narrativa pero cede al despejado basal de la estación. */
    public WeatherProfile weatherProfile() {
        return weatherManifestsNow() ? weather.profile() : Weather.clearBaselineFor(activeSeason).profile();
    }
    public WeatherProfile dominantWeatherProfile() { return weather.profile(); }

    private void transitionToNextPhase() {
        DayPhase previous = phase;
        phase = phase.next();
        elapsedInPhase = Duration.ZERO;
        if (previous == DayPhase.NIGHT && phase == DayPhase.DAY) {
            completedDays++;
            rollAtDawn();
        }
    }

    private void rollAtDawn() {
        weather = randomizers.forSeason(activeSeason).roll();
        normalizePhenomenonOccurrence();
    }

    private void elapsePhenomenonDuringCurrentPhase(Duration elapsed) {
        Duration remainingStep = elapsed;
        while (!remainingStep.isZero()) {
            normalizePhenomenonOccurrence();
            if (!phenomenon().manifestsDuring(phase)) {
                return;
            }

            Duration occurrenceRemaining = phenomenonOccurrence.remainingDuration();
            if (remainingStep.compareTo(occurrenceRemaining) < 0) {
                phenomenonOccurrence = phenomenonOccurrence.elapse(remainingStep);
                return;
            }

            remainingStep = remainingStep.minus(occurrenceRemaining);
            phenomenonOccurrence = randomizers.neutral().roll();
        }
    }

    private void normalizePhenomenonOccurrence() {
        if (!phenomenonOccurrence.isActive()) {
            phenomenonOccurrence = randomizers.neutral().roll();
        }
    }

    private static Duration normalizeElapsed(Duration elapsed) {
        if (elapsed.isNegative() || elapsed.compareTo(PHASE_DURATION) >= 0) {
            throw new IllegalArgumentException("El tiempo dentro de un tramo debe estar entre 0 y 30 minutos.");
        }
        return elapsed;
    }
}
