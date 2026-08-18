package application.simulation.combat;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/** Estado inicial reproducible de un encuentro hostil pre-MDPAR. */
public record DeterministicCombatScenario(
        String scenarioId,
        SimulationSeed seed,
        long scenarioIndex,
        long tick,
        EncounterKind kind,
        EncounterEnvironment environment,
        List<ScenarioForce> forces,
        String realismRationale
) {
    public DeterministicCombatScenario {
        if (scenarioId == null || scenarioId.isBlank() || scenarioIndex < 0 || tick < 0)
            throw new IllegalArgumentException("Identidad de escenario inválida.");
        Objects.requireNonNull(seed); Objects.requireNonNull(kind); Objects.requireNonNull(environment);
        forces = List.copyOf(Objects.requireNonNull(forces));
        if (forces.size() < 2) throw new IllegalArgumentException("Un encuentro hostil necesita al menos dos fuerzas.");
        if (realismRationale == null || realismRationale.isBlank()) throw new IllegalArgumentException("Todo escenario debe justificar su plausibilidad.");
        var forceIds = new HashSet<String>();
        var actorIds = new HashSet<String>();
        for (var force : forces) {
            if (!forceIds.add(force.forceId())) throw new IllegalArgumentException("Fuerza duplicada: " + force.forceId());
            force.actors().forEach(a -> { if (!actorIds.add(a.actorId())) throw new IllegalArgumentException("Actor duplicado entre fuerzas: " + a.actorId()); });
        }
    }
    public int representedActorCount() { return forces.stream().mapToInt(ScenarioForce::actorCount).sum(); }
    public int squadCount() { return forces.stream().mapToInt(f -> f.squads().size()).sum(); }
}
