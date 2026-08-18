package qa.domain.gold;

import application.simulation.combat.*;
import domain.bestiarium.physical_plane.ferae.FeraeBranch;
import domain.social.Profession;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

final class DeterministicCombatScenarioHarnessTest {
    private final DeterministicCombatScenarioFactory factory = new DeterministicCombatScenarioFactory();

    @Test void sameSeedIndexAndTickReplayExactly() {
        var a = factory.create(new SimulationSeed(0x5031L), 638241, 20);
        var b = factory.create(new SimulationSeed(0x5031L), 638241, 20);
        assertEquals(a, b);
        assertEquals(CombatScenarioFingerprint.sha256(a), CombatScenarioFingerprint.sha256(b));
    }

    @Test void differentScenarioIndicesProduceDiverseFingerprints() {
        Set<String> fp = new HashSet<>();
        for (int i = 0; i < 500; i++) fp.add(CombatScenarioFingerprint.sha256(factory.create(0x5031L, i)));
        assertTrue(fp.size() > 490, "El harness no debe colapsar seeds/índices distintos.");
    }

    @Test void everyTacticalSquadContainsAtMostTenIndependentActors() {
        Set<String> globalIds = new HashSet<>();
        for (int i = 0; i < 800; i++) {
            var s = factory.create(0x51L, i);
            for (var force : s.forces()) for (var squad : force.squads()) {
                assertTrue(squad.size() >= 1 && squad.size() <= TacticalSquad.MAX_MEMBERS);
                for (var actor : squad.members()) assertTrue(globalIds.add(s.scenarioId() + ":" + actor.actorId()));
            }
        }
    }

    @Test void largeBattlesAreForcesOfArmedSquadsAndCanRepresentThousands() {
        boolean sawThousands = false;
        boolean sawCompositeSquad = false;
        boolean sawManySquads = false;
        for (int i = 0; i < 1000; i++) {
            var s = factory.create(19L, i);
            if (s.kind() != EncounterKind.LARGE_SCALE_ARMED_BATTLE) continue;
            assertEquals(2, s.forces().size());
            assertTrue(s.forces().stream().flatMap(f -> f.squads().stream()).allMatch(TacticalSquad::allArmedHumans));
            assertTrue(s.forces().stream().flatMap(ScenarioForce::actors).allMatch(a -> a.subprofession().isPresent()
                    && (a.subprofession().orElseThrow().profession() == Profession.SOLDIER || a.subprofession().orElseThrow().profession() == Profession.MERCENARY)));
            sawThousands |= s.representedActorCount() >= 1000;
            sawManySquads |= s.squadCount() >= 20;
            sawCompositeSquad |= s.forces().stream().flatMap(f -> f.squads().stream())
                    .anyMatch(q -> q.compositionKind() == SquadCompositionKind.COMPOSITE);
        }
        assertTrue(sawThousands);
        assertTrue(sawManySquads);
        assertTrue(sawCompositeSquad, "Las batallas deben admitir escuadrones compuestos, no sólo bloques monoclase.");
    }

    @Test void civilianCoverageNeverBecomesPitchedBattle() {
        for (int i = 0; i < 1000; i++) {
            var s = factory.create(23L, i);
            if (s.kind() == EncounterKind.SYMBOLIC_CIVILIAN_CONFLICT) {
                assertTrue(s.representedActorCount() <= 4);
                assertTrue(s.forces().stream().flatMap(ScenarioForce::actors).noneMatch(ScenarioActor::armedHuman));
            }
        }
    }

    @Test void feraeEncountersUseOnlyIntelligenceAndNaturalScale() {
        for (int i = 0; i < 800; i++) {
            var s = factory.create(31L, i);
            if (s.kind() != EncounterKind.FERAE_OCCASIONAL) continue;
            var ferae = s.forces().stream().flatMap(ScenarioForce::actors).filter(a -> a.feraeSpecies().isPresent()).toList();
            assertFalse(ferae.isEmpty());
            assertTrue(ferae.stream().allMatch(a -> a.feraeSpecies().orElseThrow().branch() == FeraeBranch.INTELIGENCIA));
            assertTrue(s.forces().stream().flatMap(f -> f.squads().stream()).allMatch(q -> q.size() <= TacticalSquad.MAX_MEMBERS));
        }
    }

    @Test void coveragePolicyDoesNotDropAnySubprofessionAndPrioritizesArmedActors() {
        for (var sub : domain.social.Subprofession.values()) {
            if (sub.profession() == Profession.EBONY_WARRIOR) continue;
            var depth = RealisticCombatCoveragePolicy.forSubprofession(sub);
            assertNotNull(depth);
            if (sub.profession() == Profession.SOLDIER || sub.profession() == Profession.MERCENARY)
                assertEquals(CombatCoverageDepth.EXHAUSTIVE_PAIRWISE, depth);
        }
        for (var f : domain.bestiarium.physical_plane.ferae.FeraeSpecies.values())
            if (f.branch() == FeraeBranch.INTELIGENCIA)
                assertEquals(CombatCoverageDepth.HABITAT_EXHAUSTIVE, RealisticCombatCoveragePolicy.forFerae(f));
    }
}
