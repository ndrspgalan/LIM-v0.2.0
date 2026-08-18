package domain.combat.ai.declarative;

import domain.combat.ai.execution.CombatAction;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

/** Única vista del objetivo que  expone al futuro adaptador MDPAR. */
public record PerceivedCombatState(String targetActorId, boolean visualContact, boolean targetLockAllowed,
        OptionalDouble observedDistanceMeters, OptionalDouble observedStaminaDepletionIntensity,
        Optional<CombatAction> visibleCurrentAction, List<String> visibleWeapons, List<String> visibleRemoteSources,
        List<String> rememberedResources, List<SensoryFact> sensoryFacts, List<EncounterOutcomeFact> encounterOutcomes,
        String rememberedTargetConfigurationSignature) {
    public PerceivedCombatState {
        targetActorId=Objects.requireNonNull(targetActorId); observedDistanceMeters=Objects.requireNonNull(observedDistanceMeters);
        observedStaminaDepletionIntensity=Objects.requireNonNull(observedStaminaDepletionIntensity); visibleCurrentAction=Objects.requireNonNull(visibleCurrentAction);
        visibleWeapons=List.copyOf(Objects.requireNonNull(visibleWeapons)); visibleRemoteSources=List.copyOf(Objects.requireNonNull(visibleRemoteSources));
        rememberedResources=List.copyOf(Objects.requireNonNull(rememberedResources)); sensoryFacts=List.copyOf(Objects.requireNonNull(sensoryFacts));
        encounterOutcomes=List.copyOf(Objects.requireNonNull(encounterOutcomes)); rememberedTargetConfigurationSignature=Objects.requireNonNull(rememberedTargetConfigurationSignature);
    }
}
