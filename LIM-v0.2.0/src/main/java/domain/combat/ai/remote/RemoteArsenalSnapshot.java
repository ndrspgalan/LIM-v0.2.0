package domain.combat.ai.remote;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Fotografía de las opciones remotas que el combatiente conoce como propias. */
public record RemoteArsenalSnapshot(List<RemoteCombatOption> options) {
    public RemoteArsenalSnapshot {
        options = List.copyOf(Objects.requireNonNull(options, "Las opciones remotas no pueden ser nulas."));
        if (options.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Opción remota nula.");
    }

    public static RemoteArsenalSnapshot empty() { return new RemoteArsenalSnapshot(List.of()); }
    public boolean emptyArsenal() { return options.isEmpty(); }
    public List<RemoteCombatOption> readyOptions() { return options.stream().filter(RemoteCombatOption::ready).toList(); }
    public double maximumEffectiveRangeMeters() {
        return options.stream().mapToDouble(RemoteCombatOption::maximumEffectiveDistanceMeters).max().orElse(0.0);
    }
    public Optional<RemoteCombatOption> longestRangeReadyOption() {
        return readyOptions().stream().max(Comparator.comparingDouble(RemoteCombatOption::maximumEffectiveDistanceMeters));
    }
}
