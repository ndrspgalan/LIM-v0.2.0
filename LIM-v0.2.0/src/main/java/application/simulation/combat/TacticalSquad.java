package application.simulation.combat;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/** Unidad táctica de coordinación. Máximo diez actores, cada uno con estado individual propio. */
public record TacticalSquad(
        String squadId,
        List<ScenarioActor> members,
        SquadMission mission
) {
    public static final int MAX_MEMBERS = 10;

    public TacticalSquad {
        if (squadId == null || squadId.isBlank()) throw new IllegalArgumentException("Escuadrón sin identidad.");
        members = List.copyOf(Objects.requireNonNull(members));
        Objects.requireNonNull(mission);
        if (members.isEmpty() || members.size() > MAX_MEMBERS)
            throw new IllegalArgumentException("Un escuadrón táctico debe contener entre 1 y 10 actores.");
        var ids = new HashSet<String>();
        for (var member : members) if (!ids.add(member.actorId())) throw new IllegalArgumentException("Actor duplicado en escuadrón: " + member.actorId());
    }

    public SquadCompositionKind compositionKind() {
        String first = signature(members.get(0));
        return members.stream().allMatch(m -> signature(m).equals(first)) ? SquadCompositionKind.HOMOGENEOUS : SquadCompositionKind.COMPOSITE;
    }

    public boolean allArmedHumans() { return members.stream().allMatch(ScenarioActor::armedHuman); }
    public int size() { return members.size(); }

    private static String signature(ScenarioActor a) {
        return a.subprofession().map(s -> "H:" + s.name()).orElseGet(() -> "F:" + a.feraeSpecies().orElseThrow().name());
    }
}
