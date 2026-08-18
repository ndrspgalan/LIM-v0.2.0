package domain.environment.time.randomizer;

import domain.environment.time.AtmosphericPhenomenon;
import domain.environment.time.AtmosphericPhenomenonOccurrence;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Quinto randomizador, siempre habilitado. Cada tirada produce necesariamente
 * una ocurrencia atmosférica extraordinaria válida.
 */
public final class NeutralPhenomenonRandomizer {
    private final List<AtmosphericPhenomenon> catalog;
    private final Random random;

    public NeutralPhenomenonRandomizer() { this(new Random()); }

    public NeutralPhenomenonRandomizer(Random random) {
        this.random = Objects.requireNonNull(random, "El generador aleatorio no puede ser nulo.");
        this.catalog = Arrays.stream(AtmosphericPhenomenon.values())
                .filter(AtmosphericPhenomenon::isPresent)
                .toList();
        if (catalog.isEmpty()) {
            throw new IllegalStateException("Debe existir al menos un fenómeno atmosférico extraordinario.");
        }
    }

    public AtmosphericPhenomenonOccurrence roll() {
        AtmosphericPhenomenon phenomenon = catalog.get(random.nextInt(catalog.size()));
        int min = phenomenon.minimumDurationMinutes();
        int max = phenomenon.maximumDurationMinutes();
        int steps = ((max - min) / 30) + 1;
        int duration = min + random.nextInt(steps) * 30;
        return new AtmosphericPhenomenonOccurrence(phenomenon, Duration.ofMinutes(duration));
    }

    public List<AtmosphericPhenomenon> catalog() { return catalog; }
}
