package domain.ability;

import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.combat.HostileEncounterState;
import domain.status.VitalResourceState;

import java.util.Objects;
import java.util.Optional;

/** Contexto común de jugador o IA para ejecutar la misma mecánica. */
public record MasteryExecutionContext(
        String actorId,
        CharacterSheet sheet,
        Gender gender,
        VitalResourceState resources,
        HostileEncounterState hostileEncounter,
        boolean stationary,
        MasteryTargetContext target,
        MasteryWorldContext world,
        MasteryEffectRegistry effects
) {
    public MasteryExecutionContext {
        actorId = actorId == null || actorId.isBlank() ? "actor" : actorId;
        Objects.requireNonNull(sheet); Objects.requireNonNull(gender); Objects.requireNonNull(resources);
        Objects.requireNonNull(hostileEncounter); Objects.requireNonNull(world); Objects.requireNonNull(effects);
    }
    public Optional<MasteryTargetContext> targetOptional(){return Optional.ofNullable(target);}
    public static MasteryExecutionContext fromRuntimeContext(MasteryRuntimeContext runtimeContext) {
        MasteryEffectRegistry effects = new MasteryEffectRegistry();
        MasteryWorldContext world = new MasteryWorldContext(effects, java.util.List.of());
        return new MasteryExecutionContext("player", runtimeContext.sheet(), runtimeContext.gender(), runtimeContext.resources(),
                runtimeContext.hostileEncounter(), runtimeContext.stationary(), null, world, effects);
    }
}
