package domain.combat.ai.declarative;

import domain.combat.ai.observation.CombatObservation;
import domain.movement.LocomotionProfile;
import java.util.Objects;
import java.util.OptionalDouble;

/**
 *  — única entrada canónica del modelo declarativo de combate.
 * Todos los estados pertenecen a LIM; el modelo sólo los materializa como hechos y acciones posibles.
 */
public record CombatDecisionRequest(
        CombatActorDecisionState actor,
        CombatObservation selfObservation,
        MeleeDecisionState melee,
        LocomotionProfile locomotion,
        OptionalDouble horizontalJumpDistanceMeters,
        InventoryDecisionState inventory,
        AbilityDecisionState abilities,
        TransportDecisionState transport,
        PerceptionDecisionState perception,
        CombatWorldDecisionState world,
        MultiActorDecisionState multiActor,
        ExternalResourceDecisionState externalResources
) {
    public CombatDecisionRequest {
        Objects.requireNonNull(actor);
        Objects.requireNonNull(selfObservation);
        Objects.requireNonNull(melee);
        Objects.requireNonNull(locomotion);
        Objects.requireNonNull(horizontalJumpDistanceMeters);
        Objects.requireNonNull(inventory);
        Objects.requireNonNull(abilities);
        Objects.requireNonNull(transport);
        Objects.requireNonNull(perception);
        Objects.requireNonNull(world);
        Objects.requireNonNull(multiActor);
        Objects.requireNonNull(externalResources);
    }
}
