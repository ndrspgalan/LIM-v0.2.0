package domain.combat.ai.encounter;

import domain.combat.ai.execution.CombatAction;
import domain.social.RelationshipType;
import java.util.Objects;
import java.util.Optional;

/** Evidencia continua/perceptible de un actor dentro del encuentro. */
public record CombatParticipantSnapshot(
        String actorId,
        RelationshipType relationship,
        EncounterActorState state,
        double distanceMeters,
        double observedMissingHealth,
        double staminaDepletionSignal,
        double observedStaggerSeconds,
        double recentDamagePressureOnObserver,
        double recentDamagePressureOnOthers,
        Optional<CombatAction> visibleCurrentAction,
        boolean actionInterruptible,
        boolean perceptible,
        boolean mounted,
        boolean forcedTargetByEffect,
        double drainRestorationIfKilled
) {
    public CombatParticipantSnapshot {
        actorId=Objects.requireNonNull(actorId);relationship=Objects.requireNonNull(relationship);state=Objects.requireNonNull(state);
        visibleCurrentAction=Objects.requireNonNull(visibleCurrentAction);
        if(!Double.isFinite(distanceMeters)||distanceMeters<0||!Double.isFinite(observedMissingHealth)||observedMissingHealth<0
                ||!Double.isFinite(staminaDepletionSignal)||staminaDepletionSignal<0||staminaDepletionSignal>1
                ||!Double.isFinite(observedStaggerSeconds)||observedStaggerSeconds<0
                ||!Double.isFinite(recentDamagePressureOnObserver)||recentDamagePressureOnObserver<0
                ||!Double.isFinite(recentDamagePressureOnOthers)||recentDamagePressureOnOthers<0
                ||!Double.isFinite(drainRestorationIfKilled)||drainRestorationIfKilled<0) throw new IllegalArgumentException("Magnitudes de participante inválidas.");
    }
    public boolean active(){return state==EncounterActorState.CONSCIOUS;}
    public boolean incapacitated(){return state==EncounterActorState.UNCONSCIOUS||state==EncounterActorState.SLEEPING;}
}
