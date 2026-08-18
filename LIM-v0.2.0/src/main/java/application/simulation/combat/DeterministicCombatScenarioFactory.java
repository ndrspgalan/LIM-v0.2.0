package application.simulation.combat;

import domain.bestiarium.physical_plane.ferae.*;
import domain.bestiarium.physical_plane.ferae.intelligence.IntelligenceFeraeProfiles;
import domain.character.Gender;
import domain.social.*;

import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 *  — generador determinista de combate hostil organizado por escuadrones tácticos.
 * Cada actor conserva estado individual; la coordinación se agrupa en unidades de 1..10.
 * Las batallas grandes se construyen como fuerzas de múltiples escuadrones, homogéneos o compuestos.
 */
public final class DeterministicCombatScenarioFactory {
    private static final List<Subprofession> ARMED = Arrays.stream(Subprofession.values())
            .filter(s -> s.profession() == Profession.SOLDIER || s.profession() == Profession.MERCENARY)
            .filter(s -> !CanonicalSubprofessionProfileResolver.active(s).isEmpty()).toList();
    private static final List<Subprofession> CIVIL = Arrays.stream(Subprofession.values())
            .filter(s -> s.profession() != Profession.SOLDIER && s.profession() != Profession.MERCENARY && s.profession() != Profession.EBONY_WARRIOR)
            .filter(s -> !s.uniqueContemporaryHolder())
            .filter(s -> !CanonicalSubprofessionProfileResolver.active(s).isEmpty()).toList();
    private static final List<FeraeSpecies> INTELLIGENCE = Arrays.stream(FeraeSpecies.values())
            .filter(s -> s.branch() == FeraeBranch.INTELIGENCIA).toList();

    public DeterministicCombatScenario create(long rootSeed, long scenarioIndex) {
        return create(new SimulationSeed(rootSeed), scenarioIndex, 0);
    }

    public DeterministicCombatScenario create(SimulationSeed seed, long scenarioIndex, long tick) {
        if (scenarioIndex < 0 || tick < 0) throw new IllegalArgumentException("Índice/tick inválido.");
        RandomGenerator rng = RandomGeneratorFactory.of("L64X128MixRandom").create(seed.derive("scenario", scenarioIndex));
        int roll = rng.nextInt(100);
        EncounterKind kind = roll < 30 ? EncounterKind.FERAE_OCCASIONAL
                : roll < 60 ? EncounterKind.ARMED_PATROL_OR_CONTRACT
                : roll < 82 ? EncounterKind.LARGE_SCALE_ARMED_BATTLE
                : roll < 94 ? EncounterKind.LOCAL_SECURITY_INCIDENT
                : EncounterKind.SYMBOLIC_CIVILIAN_CONFLICT;
        List<ScenarioForce> forces = switch (kind) {
            case FERAE_OCCASIONAL -> feraeEncounter(rng);
            case ARMED_PATROL_OR_CONTRACT -> armedEncounter(rng, false);
            case LARGE_SCALE_ARMED_BATTLE -> armedEncounter(rng, true);
            case LOCAL_SECURITY_INCIDENT -> localSecurity(rng);
            case SYMBOLIC_CIVILIAN_CONFLICT -> civilianSymbolic(rng);
        };
        EncounterEnvironment env = environment(kind, forces, rng);
        String id = "m5031-%016x-%d".formatted(seed.value(), scenarioIndex);
        return new DeterministicCombatScenario(id, seed, scenarioIndex, tick, kind, env, forces, rationale(kind));
    }

    private List<ScenarioForce> feraeEncounter(RandomGenerator rng) {
        FeraeSpecies species = INTELLIGENCE.get(rng.nextInt(INTELLIGENCE.size()));
        FeraeSex sex = rng.nextBoolean() ? FeraeSex.MACHO : FeraeSex.HEMBRA;
        int animals = naturalGroupSize(species, rng);
        List<TacticalSquad> feraeSquads = splitFeraeIntoSquads("ferae", species, sex, animals, SquadMission.HUNT, rng);

        // El humano puede ser civil, cazador o actor armado; el encuentro sigue siendo hostil sin convertirlo en arena simétrica.
        Subprofession human = rng.nextInt(100) < 35 ? ARMED.get(rng.nextInt(ARMED.size())) : CIVIL.get(rng.nextInt(CIVIL.size()));
        int humans = 1 + rng.nextInt(3);
        List<TacticalSquad> humanSquads = List.of(humanSquad("human-s0", List.of(human), humans, SquadMission.SURVIVE, rng));
        return List.of(new ScenarioForce("force-ferae", feraeSquads), new ScenarioForce("force-human", humanSquads));
    }

    private List<ScenarioForce> armedEncounter(RandomGenerator rng, boolean large) {
        int countA = large ? 96 + rng.nextInt(2905) : 2 + rng.nextInt(15);
        int countB = large ? 96 + rng.nextInt(2905) : 1 + rng.nextInt(15);
        List<Subprofession> doctrineA = doctrinePool(rng, large ? 3 + rng.nextInt(4) : 1 + rng.nextInt(3));
        List<Subprofession> doctrineB = doctrinePool(rng, large ? 3 + rng.nextInt(4) : 1 + rng.nextInt(3));
        SquadMission missionA = large ? randomBattleMission(rng) : randomPatrolMission(rng);
        SquadMission missionB = large ? randomBattleMission(rng) : randomPatrolMission(rng);
        return List.of(
                new ScenarioForce("force-a", buildArmedForce("a", doctrineA, countA, missionA, rng)),
                new ScenarioForce("force-b", buildArmedForce("b", doctrineB, countB, missionB, rng))
        );
    }

    private List<ScenarioForce> localSecurity(RandomGenerator rng) {
        List<Subprofession> doctrine = doctrinePool(rng, 1 + rng.nextInt(3));
        int securityCount = 1 + rng.nextInt(5);
        Subprofession civilian = CIVIL.get(rng.nextInt(CIVIL.size()));
        int civilianCount = 1 + rng.nextInt(4);
        return List.of(
                new ScenarioForce("force-security", buildArmedForce("security", doctrine, securityCount, SquadMission.LOCAL_SECURITY, rng)),
                new ScenarioForce("force-local", List.of(humanSquad("local-s0", List.of(civilian), civilianCount, SquadMission.SURVIVE, rng)))
        );
    }

    private List<ScenarioForce> civilianSymbolic(RandomGenerator rng) {
        Subprofession a = CIVIL.get(rng.nextInt(CIVIL.size()));
        Subprofession b = CIVIL.get(rng.nextInt(CIVIL.size()));
        int ca = 1 + rng.nextInt(2), cb = 1 + rng.nextInt(2);
        return List.of(
                new ScenarioForce("force-civil-a", List.of(humanSquad("civil-a-s0", List.of(a), ca, SquadMission.SURVIVE, rng))),
                new ScenarioForce("force-civil-b", List.of(humanSquad("civil-b-s0", List.of(b), cb, SquadMission.SURVIVE, rng)))
        );
    }

    private List<TacticalSquad> buildArmedForce(String prefix, List<Subprofession> doctrine, int actorCount,
                                                  SquadMission mission, RandomGenerator rng) {
        List<TacticalSquad> squads = new ArrayList<>();
        int remaining = actorCount, squadIndex = 0;
        while (remaining > 0) {
            int minPreferred = Math.min(4, remaining);
            int size = remaining <= TacticalSquad.MAX_MEMBERS ? remaining
                    : minPreferred + rng.nextInt(TacticalSquad.MAX_MEMBERS - minPreferred + 1);
            size = Math.min(size, remaining);
            int distinct = doctrine.size() > 1 && size > 1 && rng.nextInt(100) < 60
                    ? Math.min(1 + rng.nextInt(Math.min(3, doctrine.size())), size)
                    : 1;
            List<Subprofession> squadPool = shuffledTake(doctrine, distinct, rng);
            squads.add(humanSquad(prefix + "-s" + squadIndex++, squadPool, size, mission, rng));
            remaining -= size;
        }
        return List.copyOf(squads);
    }

    private TacticalSquad humanSquad(String squadId, List<Subprofession> pool, int size,
                                     SquadMission mission, RandomGenerator rng) {
        if (size < 1 || size > TacticalSquad.MAX_MEMBERS) throw new IllegalArgumentException("Tamaño de escuadrón inválido.");
        List<ScenarioActor> actors = new ArrayList<>(size);
        // Garantiza que un escuadrón compuesto use realmente cada subprofesión seleccionada al menos una vez.
        for (int i = 0; i < size; i++) {
            Subprofession s = i < pool.size() ? pool.get(i) : pool.get(rng.nextInt(pool.size()));
            actors.add(humanActor(squadId + "-a" + i, s, rng));
        }
        return new TacticalSquad(squadId, actors, mission);
    }

    private List<TacticalSquad> splitFeraeIntoSquads(String prefix, FeraeSpecies species, FeraeSex sex,
                                                       int actorCount, SquadMission mission, RandomGenerator rng) {
        List<TacticalSquad> squads = new ArrayList<>();
        int remaining = actorCount, squadIndex = 0;
        while (remaining > 0) {
            int size = Math.min(TacticalSquad.MAX_MEMBERS, remaining);
            List<ScenarioActor> members = new ArrayList<>(size);
            for (int i = 0; i < size; i++) members.add(feraeActor(prefix + "-s" + squadIndex + "-a" + i, species, sex, rng));
            squads.add(new TacticalSquad(prefix + "-s" + squadIndex++, members, mission));
            remaining -= size;
        }
        return List.copyOf(squads);
    }

    private ScenarioActor humanActor(String id, Subprofession s, RandomGenerator rng) {
        var profiles = CanonicalSubprofessionProfileResolver.active(s);
        var p = profiles.get(rng.nextInt(profiles.size()));
        var genders = p.genders().stream().sorted(Comparator.comparingInt(Enum::ordinal)).toList();
        Gender g = genders.get(rng.nextInt(genders.size()));
        double maxPa = Math.max(1, p.attributes().valueOf(domain.character.sheet.Attribute.AGUANTE));
        double cur = maxPa * (0.20 + rng.nextDouble() * 0.80);
        return new ScenarioActor(id, Optional.of(s), Optional.empty(), Optional.empty(), g, p.attributes(),
                s.canonicalHeightMeters(g), cur, maxPa);
    }

    private ScenarioActor feraeActor(String id, FeraeSpecies s, FeraeSex sex, RandomGenerator rng) {
        var profile = IntelligenceFeraeProfiles.of(s).stream().filter(p -> p.sex() == sex).findFirst().orElseThrow();
        Gender g = sex == FeraeSex.MACHO ? Gender.HOMBRE : Gender.MUJER;
        double maxPa = Math.max(1, profile.attributes().valueOf(domain.character.sheet.Attribute.AGUANTE));
        double cur = maxPa * (0.25 + rng.nextDouble() * 0.75);
        return new ScenarioActor(id, Optional.empty(), Optional.of(s), Optional.of(sex), g, profile.attributes(),
                profile.canonicalHeightMeters(), cur, maxPa);
    }

    private static List<Subprofession> doctrinePool(RandomGenerator rng, int requested) {
        return shuffledTake(ARMED, Math.max(1, Math.min(requested, ARMED.size())), rng);
    }

    private static <T> List<T> shuffledTake(List<T> source, int count, RandomGenerator rng) {
        ArrayList<T> copy = new ArrayList<>(source);
        for (int i = copy.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            T tmp = copy.get(i); copy.set(i, copy.get(j)); copy.set(j, tmp);
        }
        return List.copyOf(copy.subList(0, Math.min(count, copy.size())));
    }

    private static SquadMission randomBattleMission(RandomGenerator r) {
        return List.of(SquadMission.ASSAULT, SquadMission.DEFEND, SquadMission.INTERDICT,
                SquadMission.SCREEN, SquadMission.ESCORT).get(r.nextInt(5));
    }

    private static SquadMission randomPatrolMission(RandomGenerator r) {
        return List.of(SquadMission.PATROL, SquadMission.ESCORT, SquadMission.INTERDICT,
                SquadMission.SCREEN).get(r.nextInt(4));
    }

    private static int naturalGroupSize(FeraeSpecies s, RandomGenerator r) {
        return switch (s) {
            case RATA -> 1 + r.nextInt(8); case CUERVO -> 1 + r.nextInt(6); case CERDO -> 1 + r.nextInt(5); case ARMADILLO -> 1;
            case CABALLO_PASEO, CABALLO_CARRERAS, CABALLO_TIRO -> 1 + r.nextInt(8); case CIERVO -> 1 + r.nextInt(12); case TORO -> 1 + r.nextInt(3);
            case AGUILA, SERPIENTE, LINCE, OSO -> 1; case JABALI -> 1 + r.nextInt(4); case LOBO -> 2 + r.nextInt(9);
            case LEON -> 1 + r.nextInt(6); case RINOCERONTE -> 1 + r.nextInt(3); default -> 1;
        };
    }

    private static EncounterEnvironment environment(EncounterKind kind, List<ScenarioForce> forces, RandomGenerator r) {
        if (kind == EncounterKind.LARGE_SCALE_ARMED_BATTLE) return r.nextBoolean() ? EncounterEnvironment.OPEN_PLAIN : EncounterEnvironment.INDUSTRIAL;
        if (kind == EncounterKind.ARMED_PATROL_OR_CONTRACT)
            return List.of(EncounterEnvironment.ROAD, EncounterEnvironment.RURAL, EncounterEnvironment.INDUSTRIAL, EncounterEnvironment.PORT).get(r.nextInt(4));
        if (kind == EncounterKind.LOCAL_SECURITY_INCIDENT || kind == EncounterKind.SYMBOLIC_CIVILIAN_CONFLICT)
            return r.nextBoolean() ? EncounterEnvironment.URBAN : EncounterEnvironment.ROAD;
        FeraeSpecies s = forces.stream().flatMap(ScenarioForce::actors).map(ScenarioActor::feraeSpecies)
                .flatMap(Optional::stream).findFirst().orElseThrow();
        return switch (s) {
            case RATA, CUERVO, CERDO -> r.nextBoolean() ? EncounterEnvironment.RURAL : EncounterEnvironment.URBAN;
            case ARMADILLO, SERPIENTE, JABALI -> EncounterEnvironment.FOREST;
            case CABALLO_PASEO, CABALLO_CARRERAS, CABALLO_TIRO, CIERVO, TORO, RINOCERONTE -> EncounterEnvironment.OPEN_PLAIN;
            case AGUILA -> EncounterEnvironment.MOUNTAIN;
            case LINCE, LOBO, OSO -> EncounterEnvironment.FOREST;
            case LEON -> EncounterEnvironment.OPEN_PLAIN;
            default -> EncounterEnvironment.RURAL;
        };
    }

    private static String rationale(EncounterKind k) {
        return switch (k) {
            case FERAE_OCCASIONAL -> "Encuentro hostil incidental con Ferae INTELIGENCIA en agrupación y entorno compatibles con su hábito; cada ejemplar conserva estado individual dentro de escuadrones de hasta diez.";
            case ARMED_PATROL_OR_CONTRACT -> "Choque entre escuadrones armados pequeños, homogéneos o compuestos: patrulla, escolta, recuperación, hostigamiento o contrato.";
            case LARGE_SCALE_ARMED_BATTLE -> "Combate campal entre fuerzas de Soldado/Mercenario descompuestas en escuadrones tácticos de 1..10; ningún bloque de miles actúa como mente colmena.";
            case LOCAL_SECURITY_INCIDENT -> "Intervención armada localizada frente a un grupo civil/profesional; la profesión civil no se convierte artificialmente en unidad militar.";
            case SYMBOLIC_CIVILIAN_CONFLICT -> "Cobertura simbólica de hostilidad entre profesiones civiles mediante grupos mínimos, nunca mediante batalla campal profesional.";
        };
    }
}
