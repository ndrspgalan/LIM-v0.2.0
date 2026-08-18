package application.simulation.combat;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/** Fuerza hostil compuesta por escuadrones; nunca sustituye la cognición individual de sus miembros. */
public record ScenarioForce(String forceId, List<TacticalSquad> squads) {
    public ScenarioForce {
        if (forceId == null || forceId.isBlank()) throw new IllegalArgumentException("Fuerza sin identidad.");
        squads = List.copyOf(Objects.requireNonNull(squads));
        if (squads.isEmpty()) throw new IllegalArgumentException("Una fuerza necesita al menos un escuadrón.");
        var squadIds = new HashSet<String>();
        var actorIds = new HashSet<String>();
        for (var squad : squads) {
            if (!squadIds.add(squad.squadId())) throw new IllegalArgumentException("Escuadrón duplicado: " + squad.squadId());
            for (var actor : squad.members()) if (!actorIds.add(actor.actorId())) throw new IllegalArgumentException("Actor duplicado en fuerza: " + actor.actorId());
        }
    }
    public int actorCount() { return squads.stream().mapToInt(TacticalSquad::size).sum(); }
    public Stream<ScenarioActor> actors() { return squads.stream().flatMap(s -> s.members().stream()); }
}
