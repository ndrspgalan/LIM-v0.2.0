package domain.combat.ai.declarative;

import domain.combat.ai.loadout.CombatLoadoutResolver;
import domain.combat.ai.loadout.ResolvedCombatLoadout;
import domain.inventory.item.UnarmedWeaponFactory;
import domain.inventory.item.WeaponItem;
import java.util.List;

/**
 *  — fachada única del contrato declarativo. LIM describe el estado y las acciones físicamente
 * disponibles; deliberadamente no existe decide(), score(), priority() ni selección local de objetivo.
 */
public final class DeclarativeCombatModel {
    private final CombatLoadoutResolver loadoutResolver = new CombatLoadoutResolver();
    private final MeleeActionCandidateResolver melee = new MeleeActionCandidateResolver();
    private final LocomotionActionCandidateResolver locomotion = new LocomotionActionCandidateResolver();
    private final RemoteActionCandidateResolver remote = new RemoteActionCandidateResolver();
    private final InventoryActionCandidateResolver inventory = new InventoryActionCandidateResolver();
    private final AbilityActionCandidateResolver abilities = new AbilityActionCandidateResolver();
    private final TransportActionCandidateResolver transport = new TransportActionCandidateResolver();
    private final PerceivedCombatStateResolver perception = new PerceivedCombatStateResolver();
    private final MultiActorCombatResolver multiActor = new MultiActorCombatResolver();
    private final ExternalResourceActionCandidateResolver external = new ExternalResourceActionCandidateResolver();

    public CombatDecisionContext snapshot(CombatDecisionRequest request) {
        var actor=request.actor();
        var observation=request.selfObservation();
        ResolvedCombatLoadout loadout=loadoutResolver.resolve(observation.selfLoadout(), observation.self());
        WeaponItem meleeWeapon = observation.selfLoadout().rightHand().isEmpty() && observation.selfLoadout().leftHand().isEmpty()
                && observation.self().naturalCombatProfile().isEmpty()
                ? UnarmedWeaponFactory.create(actor.sheet(), actor.heightMeters(), actor.gender())
                : loadout.attackingWeapon();

        PerceivedCombatState perceived=perception.resolve(observation,request.perception());
        var meleeActions=melee.resolve(actor, meleeWeapon, request.melee(),
                request.abilities().masteries().isPassiveActive("AURA DE PULSIÓN", actor.sheet()));
        var locomotionActions=locomotion.resolve(actor,request.locomotion(),request.horizontalJumpDistanceMeters());
        var remoteActions=perceived.observedDistanceMeters().isPresent()
                ? remote.resolve(observation.selfRemoteArsenal(),perceived.observedDistanceMeters().getAsDouble()) : List.<RemoteActionCandidate>of();
        var inventoryActions=inventory.resolve(actor,request.inventory());
        var activeEffects=inventory.activeEffects(request.inventory());
        var abilityActions=abilities.resolve(actor,request.abilities());
        var abilityEffects=abilities.activeEffects(request.abilities());
        var transportActions=transport.resolve(actor,request.transport());
        var transportFacts=transport.facts(request.transport());
        var actors=multiActor.actors(request.multiActor());
        var directed=multiActor.directed(actors,meleeActions,abilityActions,request.multiActor());

        return new CombatDecisionContext(actor,perceived,meleeActions,locomotionActions,remoteActions,
                inventoryActions,activeEffects,abilityActions,abilityEffects,transportActions,transportFacts,
                actors,directed,multiActor.intents(request.multiActor()),multiActor.areaConsequences(request.multiActor()),
                request.multiActor().knownRelationships(),external.facts(request.externalResources()),
                external.actions(request.externalResources()),external.feraeFacts(request.externalResources()));
    }
}
