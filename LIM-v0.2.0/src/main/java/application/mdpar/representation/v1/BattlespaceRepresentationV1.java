package application.mdpar.representation.v1;

import java.util.List;
import java.util.Objects;

/** Contexto jerárquico: actor -> escuadrón -> fuerza -> campo de batalla. */
public record BattlespaceRepresentationV1(
        String scenarioId,
        long seed,
        long scenarioIndex,
        long tick,
        String encounterKind,
        String encounterEnvironment,
        List<ForceRepresentationV1> forces,
        List<TacticalSquadRepresentationV1> squads,
        List<KnowledgeFactV1> environmentFacts
) {
    public BattlespaceRepresentationV1 {
        if(scenarioId==null||scenarioId.isBlank()||scenarioIndex<0||tick<0)throw new IllegalArgumentException("Escenario inválido.");
        encounterKind=Objects.requireNonNull(encounterKind); encounterEnvironment=Objects.requireNonNull(encounterEnvironment);
        forces=List.copyOf(Objects.requireNonNull(forces));squads=List.copyOf(Objects.requireNonNull(squads));environmentFacts=List.copyOf(Objects.requireNonNull(environmentFacts));
    }
}
