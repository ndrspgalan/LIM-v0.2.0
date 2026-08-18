package qa.domain.gold;

import application.mdpar.representation.v1.*;
import application.simulation.combat.*;
import domain.ability.*;
import domain.character.Gender;
import domain.combat.ai.declarative.*;
import domain.combat.ai.loadout.VisibleLoadout;
import domain.combat.ai.memory.CombatOutcomeMemory;
import domain.combat.ai.observation.CombatObservation;
import domain.combat.ai.perception.CombatPerceptionSnapshot;
import domain.combat.ai.remote.RemoteArsenalSnapshot;
import domain.combat.ai.threat.CombatantPresence;
import domain.inventory.InventoryState;
import domain.inventory.item.GripMode;
import domain.inventory.item.WeaponActionMode;
import domain.movement.*;
import domain.status.TherapeuticEffectTracker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class ExhaustiveMdparCombatRepresentationV1Test {
    @Test void unknownFactsNeverLeakTheirUnderlyingValue() {
        var fact=KnowledgeFactV1.unknown("enemy.currentPa","LIM_PERCEPTION");
        assertTrue(fact.value().isEmpty());
        assertEquals(EpistemicStateV1.UNKNOWN,fact.epistemicState());
        assertThrows(IllegalArgumentException.class,()->new KnowledgeFactV1("x",FactValueTypeV1.INTEGER,Optional.of("75"),EpistemicStateV1.UNKNOWN,"bad",0));
    }

    @Test void ordinaryHostileAiCannotUsePredictiveOrHomingAim() {
        var d=TargetingDoctrineV1.canonical();
        assertTrue(d.blindFireSchemaSupported()); assertTrue(d.positionTargetingSupported()); assertTrue(d.areaTargetingSupported());
        assertFalse(d.predictiveInterceptAimingAllowed()); assertFalse(d.postLaunchTrajectoryCorrectionAllowed());
        assertEquals("OBSERVED_OR_LAST_KNOWN_POSITION_AT_COMMIT_TIME",d.aimingBasis());
    }

    @Test void mapperPreservesCombatRepresentationAndAllRequiredSurfaces() {
        var scenario=new DeterministicCombatScenarioFactory().create(new SimulationSeed(0x504L),17,40);
        var selfScenario=scenario.forces().get(0).squads().get(0).members().get(0);
        var targetScenario=scenario.forces().get(1).squads().get(0).members().get(0);
        var origin=selfScenario.subprofession().map(CombatActorOriginFact::subprofession)
                .orElseGet(()->CombatActorOriginFact.ferae(selfScenario.feraeSpecies().orElseThrow(),selfScenario.feraeSex().orElseThrow()));
        var actor=new CombatActorDecisionState(selfScenario.actorId(),selfScenario.gender(),selfScenario.sheet(),selfScenario.heightMeters(),selfScenario.currentPa(),selfScenario.totalPa(),origin);
        var selfPresence=new CombatantPresence(selfScenario.sheet(),selfScenario.heightMeters());
        var targetPresence=new CombatantPresence(targetScenario.sheet(),targetScenario.heightMeters());
        var observation=new CombatObservation(selfPresence,targetPresence,VisibleLoadout.of(null,null),VisibleLoadout.of(null,null),5.0,RemoteArsenalSnapshot.empty(),RemoteArsenalSnapshot.empty());
        var memory=new CombatOutcomeMemory();
        var perception=new PerceptionDecisionState(targetScenario.actorId(),CombatPerceptionSnapshot.neutral(),memory,Optional.empty());
        var request=new CombatDecisionRequest(actor,observation,MeleeDecisionState.initial(WeaponActionMode.PRIMARY,GripMode.ONE_HANDED),
                new LocomotionProfile(SlopeBand.RUN_ALLOWED,EnumSet.of(LocomotionMode.WALKING,LocomotionMode.RUNNING)),OptionalDouble.empty(),
                new InventoryDecisionState(InventoryState.emptyWithoutPersonalTransport(),new TherapeuticEffectTracker(),List.of()),
                new AbilityDecisionState(new CharacterMasteryCollection(List.of()),new MasteryEffectRegistry()),TransportDecisionState.empty(),perception,
                CombatWorldDecisionState.neutral(),MultiActorDecisionState.empty(),ExternalResourceDecisionState.empty());
        var context=new CombatDecisionContext(actor,
                new PerceivedCombatState(targetScenario.actorId(),true,true,OptionalDouble.of(5),OptionalDouble.of(0),Optional.empty(),List.of(),List.of(),List.of(),List.of(),List.of(),""),
                List.of(),List.of(),List.of(),List.of(),List.of(),List.of(),List.of(),List.of(),List.of(),
                List.of(),List.of(),List.of(),List.of(),List.of(),List.of(),List.of(),List.of());
        var representation=new LimCombatRepresentationMapperV1().map(scenario,selfScenario.actorId(),request,context);
        assertEquals(LimCombatRepresentationV1.VERSION,representation.schemaVersion());
        assertEquals(selfScenario.actorId(),representation.self().actorId());
        assertTrue(representation.battlespace().squads().stream().anyMatch(s->s.ownSquad()&&s.memberActorIds().contains(selfScenario.actorId())));
        assertTrue(representation.battlespace().forces().stream().anyMatch(ForceRepresentationV1::ownForce));
        assertTrue(representation.battlespace().squads().stream().allMatch(s->s.memberActorIds().size()<=10));
        assertTrue(CombatRepresentationCoverageAuditV1.missing(representation).isEmpty(),()->"Superficies ausentes: "+CombatRepresentationCoverageAuditV1.missing(representation));
        assertTrue(representation.exhaustiveSelfAndKnownState().stream().anyMatch(f->f.path().contains("attributeValues")));
    }
}
