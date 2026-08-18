package domain.combat.ai.declarative;

import java.util.List;
import java.util.Objects;

/** Snapshot declarativo : hechos + alternativas legales, incluidas proyecciones multi-actor; ninguna selección vive aquí. */
public record CombatDecisionContext(
        CombatActorDecisionState actor,
        PerceivedCombatState perceivedCombat,
        List<MeleeActionCandidate> meleeActions,
        List<LocomotionActionCandidate> locomotionActions,
        List<RemoteActionCandidate> remoteActions,
        List<InventoryActionCandidate> inventoryActions,
        List<ActiveEffectFact> activeEffects,
        List<AbilityActionCandidate> abilityActions,
        List<AbilityEffectFact> abilityEffects,
        List<TransportActionCandidate> transportActions,
        List<TransportFact> transportFacts,
        List<PerceivedActorFact> perceivedActors,
        List<DirectedActionCandidate> directedActions,
        List<ObservedIntentFact> observedIntents,
        List<AreaActorConsequenceFact> areaConsequences,
        List<KnownActorRelationshipFact> knownActorRelationships,
        List<ExternalResourceFact> externalResources,
        List<ExternalInventoryActionCandidate> externalInventoryActions,
        List<FeraeLootFact> feraeLootFacts
) {
    public CombatDecisionContext {
        Objects.requireNonNull(actor); Objects.requireNonNull(perceivedCombat);
        meleeActions=List.copyOf(Objects.requireNonNull(meleeActions));
        locomotionActions=List.copyOf(Objects.requireNonNull(locomotionActions));
        remoteActions=List.copyOf(Objects.requireNonNull(remoteActions));
        inventoryActions=List.copyOf(Objects.requireNonNull(inventoryActions));
        activeEffects=List.copyOf(Objects.requireNonNull(activeEffects));
        abilityActions=List.copyOf(Objects.requireNonNull(abilityActions));
        abilityEffects=List.copyOf(Objects.requireNonNull(abilityEffects));
        transportActions=List.copyOf(Objects.requireNonNull(transportActions));
        transportFacts=List.copyOf(Objects.requireNonNull(transportFacts));
        perceivedActors=List.copyOf(Objects.requireNonNull(perceivedActors));
        directedActions=List.copyOf(Objects.requireNonNull(directedActions));
        observedIntents=List.copyOf(Objects.requireNonNull(observedIntents));
        areaConsequences=List.copyOf(Objects.requireNonNull(areaConsequences));
        knownActorRelationships=List.copyOf(Objects.requireNonNull(knownActorRelationships));
        externalResources=List.copyOf(Objects.requireNonNull(externalResources));
        externalInventoryActions=List.copyOf(Objects.requireNonNull(externalInventoryActions));
        feraeLootFacts=List.copyOf(Objects.requireNonNull(feraeLootFacts));
    }
}
