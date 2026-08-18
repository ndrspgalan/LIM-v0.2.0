package application.mdpar.representation.v1;

import application.simulation.combat.*;
import domain.combat.ai.declarative.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  — frontera única LIM -> MDPAR. No toma decisiones: estructura todo el estado legítimamente
 * disponible para el actor y conserva jerarquía táctica, incertidumbre y espacio de acciones.
 */
public final class LimCombatRepresentationMapperV1 {
    private final RepresentationFactProjectorV1 facts = new RepresentationFactProjectorV1();

    public LimCombatRepresentationV1 map(DeterministicCombatScenario scenario, String actingActorId,
                                               CombatDecisionRequest request, CombatDecisionContext context) {
        Objects.requireNonNull(scenario); Objects.requireNonNull(request); Objects.requireNonNull(context);
        if (!request.actor().actorId().equals(actingActorId) || !context.actor().actorId().equals(actingActorId))
            throw new IllegalArgumentException("El actor del contrato debe coincidir con request/context.");
        ScenarioLocation location = locate(scenario, actingActorId);

        List<KnowledgeFactV1> selfFacts = new ArrayList<>();
        selfFacts.addAll(facts.exact("self.actor", context.actor(), "LIM_ACTOR_AUTHORITY"));
        selfFacts.addAll(facts.exact("self.presence", request.selfObservation().self(), "LIM_SELF_OBSERVATION"));
        selfFacts.addAll(facts.exact("self.loadout", request.selfObservation().selfLoadout(), "LIM_SELF_LOADOUT"));
        selfFacts.addAll(facts.exact("self.remoteArsenal", request.selfObservation().selfRemoteArsenal(), "LIM_SELF_REMOTE_ARSENAL"));
        selfFacts.addAll(facts.exact("self.meleeState", request.melee(), "LIM_MELEE_STATE"));
        selfFacts.addAll(facts.exact("self.locomotion", request.locomotion(), "LIM_LOCOMOTION"));
        selfFacts.addAll(facts.exact("self.horizontalJump", request.horizontalJumpDistanceMeters(), "LIM_LOCOMOTION"));
        selfFacts.addAll(facts.exact("self.inventory", request.inventory(), "LIM_INVENTORY"));
        selfFacts.addAll(facts.exact("self.abilities", request.abilities(), "LIM_ABILITY"));
        selfFacts.addAll(facts.exact("self.transport", request.transport(), "LIM_TRANSPORT"));
        selfFacts.addAll(facts.exact("world", request.world(), "LIM_WORLD"));
        selfFacts.addAll(facts.exact("knownExternalResources", request.externalResources(), "LIM_EXTERNAL_RESOURCE"));

        ActorKnowledgeV1 self = new ActorKnowledgeV1(actingActorId, origin(context.actor().origin()), originKey(context.actor().origin()),
                EpistemicStateV1.EXACT, selfFacts);

        LinkedHashMap<String,ActorKnowledgeV1> actorViews = new LinkedHashMap<>();
        actorViews.put(context.perceivedCombat().targetActorId(), primaryTarget(context.perceivedCombat(), scenario));
        context.perceivedActors().forEach(p -> actorViews.put(p.actorId(), knownActor(p, scenario)));
        List<ActorKnowledgeV1> knownActors = List.copyOf(actorViews.values());
        BattlespaceRepresentationV1 battlespace = battlespace(scenario, location, request.world(), actorViews.keySet());
        List<ActionRepresentationV1> actions = actions(context);
        List<KnowledgeFactV1> consequences = facts.observed("projected.areaConsequences", context.areaConsequences(), "LIM_PROJECTED_CONSEQUENCES", 1.0);

        List<KnowledgeFactV1> ledger = new ArrayList<>(selfFacts);
        ledger.addAll(facts.observed("perception.primary", context.perceivedCombat(), "LIM_PERCEPTION", 1.0));
        ledger.addAll(facts.observed("perception.actors", context.perceivedActors(), "LIM_PERCEPTION", 1.0));
        ledger.addAll(facts.observed("perception.intents", context.observedIntents(), "LIM_PERCEPTION", 1.0));
        ledger.addAll(facts.observed("knownRelationships", context.knownActorRelationships(), "LIM_SOCIAL_MEMORY", 1.0));
        ledger.addAll(facts.exact("legal.melee", context.meleeActions(), "LIM_ACTION_RESOLVER"));
        ledger.addAll(facts.exact("legal.locomotion", context.locomotionActions(), "LIM_ACTION_RESOLVER"));
        ledger.addAll(facts.exact("legal.remote", context.remoteActions(), "LIM_ACTION_RESOLVER"));
        ledger.addAll(facts.exact("legal.inventory", context.inventoryActions(), "LIM_ACTION_RESOLVER"));
        ledger.addAll(facts.exact("legal.abilities", context.abilityActions(), "LIM_ACTION_RESOLVER"));
        ledger.addAll(facts.exact("legal.transport", context.transportActions(), "LIM_ACTION_RESOLVER"));
        ledger.addAll(facts.exact("legal.directed", context.directedActions(), "LIM_ACTION_RESOLVER"));
        ledger.addAll(facts.exact("effects.active", context.activeEffects(), "LIM_EFFECTS"));
        ledger.addAll(facts.exact("effects.abilities", context.abilityEffects(), "LIM_EFFECTS"));
        ledger.addAll(facts.exact("facts.transport", context.transportFacts(), "LIM_TRANSPORT"));
        ledger.addAll(facts.exact("facts.externalResources", context.externalResources(), "LIM_EXTERNAL_RESOURCE"));
        ledger.addAll(facts.exact("facts.externalInventoryActions", context.externalInventoryActions(), "LIM_EXTERNAL_RESOURCE"));
        ledger.addAll(facts.exact("facts.feraeLoot", context.feraeLootFacts(), "LIM_FERAE"));
        ledger.addAll(consequences);

        return new LimCombatRepresentationV1(LimCombatRepresentationV1.VERSION,self,battlespace,knownActors,
                actions,consequences,List.copyOf(ledger),TargetingDoctrineV1.canonical());
    }

    private ActorKnowledgeV1 knownActor(PerceivedActorFact p, DeterministicCombatScenario scenario) {
        List<KnowledgeFactV1> actorFacts = new ArrayList<>(facts.observed("actor."+p.actorId(),p,"LIM_PERCEPTION",p.perceptibleNow()?1.0:0.75));
        Optional<ScenarioActor> scenarioActor = scenario.forces().stream().flatMap(ScenarioForce::actors).filter(a->a.actorId().equals(p.actorId())).findFirst();
        if (scenarioActor.isPresent() && p.perceptibleNow()) {
            ScenarioActor a=scenarioActor.orElseThrow();
            String key=a.subprofession().map(Enum::name).orElseGet(()->a.feraeSpecies().orElseThrow().name());
            actorFacts.add(KnowledgeFactV1.inferred("actor."+p.actorId()+".originKey",key,"VISIBLE_ARCHETYPE_INFERENCE",0.80));
        } else actorFacts.add(KnowledgeFactV1.unknown("actor."+p.actorId()+".originKey","LIM_PERCEPTION"));
        return new ActorKnowledgeV1(p.actorId(),ActorOriginV1.UNSPECIFIED,"",p.perceptibleNow()?EpistemicStateV1.OBSERVED:EpistemicStateV1.LAST_KNOWN,actorFacts);
    }

    private ActorKnowledgeV1 primaryTarget(PerceivedCombatState p, DeterministicCombatScenario scenario) {
        List<KnowledgeFactV1> actorFacts = new ArrayList<>(facts.observed("actor."+p.targetActorId()+".primaryPerception",p,"LIM_PERCEPTION",p.visualContact()?1.0:0.75));
        Optional<ScenarioActor> scenarioActor = scenario.forces().stream().flatMap(ScenarioForce::actors).filter(a->a.actorId().equals(p.targetActorId())).findFirst();
        if (scenarioActor.isPresent() && p.visualContact()) {
            ScenarioActor a=scenarioActor.orElseThrow();
            String key=a.subprofession().map(Enum::name).orElseGet(()->a.feraeSpecies().orElseThrow().name());
            actorFacts.add(KnowledgeFactV1.inferred("actor."+p.targetActorId()+".originKey",key,"VISIBLE_ARCHETYPE_INFERENCE",0.80));
        } else actorFacts.add(KnowledgeFactV1.unknown("actor."+p.targetActorId()+".originKey","LIM_PERCEPTION"));
        return new ActorKnowledgeV1(p.targetActorId(),ActorOriginV1.UNSPECIFIED,"",p.visualContact()?EpistemicStateV1.OBSERVED:EpistemicStateV1.LAST_KNOWN,actorFacts);
    }

    private BattlespaceRepresentationV1 battlespace(DeterministicCombatScenario scenario, ScenarioLocation own, CombatWorldDecisionState world, Set<String> knownActorIds) {
        List<ForceRepresentationV1> forces = new ArrayList<>();
        List<TacticalSquadRepresentationV1> squads = new ArrayList<>();
        for (var force : scenario.forces()) {
            boolean ownForce=force.forceId().equals(own.forceId);
            List<String> visibleSquadIds=new ArrayList<>();
            for (var squad : force.squads()) {
                boolean ownSquad=squad.squadId().equals(own.squadId);
                List<String> memberIds = ownForce
                        ? squad.members().stream().map(ScenarioActor::actorId).toList()
                        : squad.members().stream().map(ScenarioActor::actorId).filter(knownActorIds::contains).toList();
                if (memberIds.isEmpty()) continue; // no se filtra una unidad enemiga completamente desconocida
                visibleSquadIds.add(squad.squadId());
                squads.add(new TacticalSquadRepresentationV1(squad.squadId(),force.forceId(),squad.mission().name(),squad.compositionKind().name(),
                        memberIds,ownSquad,ownForce?EpistemicStateV1.EXACT:EpistemicStateV1.OBSERVED));
            }
            if (!visibleSquadIds.isEmpty()) forces.add(new ForceRepresentationV1(force.forceId(),visibleSquadIds,ownForce,
                    ownForce?EpistemicStateV1.EXACT:EpistemicStateV1.OBSERVED));
        }
        List<KnowledgeFactV1> env=new ArrayList<>();
        env.add(KnowledgeFactV1.exact("scenario.environment",scenario.environment(),"SIMULATION_HARNESS"));
        env.addAll(facts.exact("world",world,"LIM_WORLD"));
        return new BattlespaceRepresentationV1(scenario.scenarioId(),scenario.seed().value(),scenario.scenarioIndex(),scenario.tick(),
                scenario.kind().name(),scenario.environment().name(),forces,squads,env);
    }

    private List<ActionRepresentationV1> actions(CombatDecisionContext c) {
        List<ActionRepresentationV1> out=new ArrayList<>(); int i=0;
        for(var x:c.meleeActions()) out.add(action("melee-"+(i++),"MELEE",ActionTargetKindV1.ACTOR,x,List.of()));
        i=0;for(var x:c.locomotionActions()) out.add(action("locomotion-"+(i++),"LOCOMOTION",ActionTargetKindV1.NONE,x,List.of()));
        i=0;for(var x:c.remoteActions()){
            List<String> blocks=x.immediatelyExecutable()?List.of():List.of("REMOTE_READINESS_"+x.readiness().name());
            out.add(new ActionRepresentationV1("remote-"+(i++),"REMOTE",x.immediatelyExecutable()?ActionAvailabilityV1.LEGAL_NOW:ActionAvailabilityV1.KNOWN_BUT_BLOCKED,
                    x.action()==RemoteActionType.FIRE||x.action()==RemoteActionType.THROW?ActionTargetKindV1.ACTOR:ActionTargetKindV1.NONE,blocks,
                    facts.exact("action.remote",x,"LIM_ACTION_RESOLVER")));
        }
        i=0;for(var x:c.inventoryActions()) out.add(action("inventory-"+(i++),"INVENTORY",ActionTargetKindV1.SELF,x,List.of()));
        i=0;for(var x:c.abilityActions()) out.add(action("ability-"+(i++),"ABILITY",ActionTargetKindV1.ACTOR,x,List.of()));
        i=0;for(var x:c.transportActions()) out.add(action("transport-"+(i++),"TRANSPORT",ActionTargetKindV1.SELF,x,List.of()));
        i=0;for(var x:c.directedActions()) out.add(action("directed-"+(i++),"DIRECTED",ActionTargetKindV1.ACTOR,x,List.of()));
        return List.copyOf(out);
    }
    private ActionRepresentationV1 action(String id,String family,ActionTargetKindV1 target,Object value,List<String>blocks){return new ActionRepresentationV1(id,family,blocks.isEmpty()?ActionAvailabilityV1.LEGAL_NOW:ActionAvailabilityV1.KNOWN_BUT_BLOCKED,target,blocks,facts.exact("action."+family.toLowerCase(Locale.ROOT),value,"LIM_ACTION_RESOLVER"));}

    private ScenarioLocation locate(DeterministicCombatScenario scenario,String actorId){
        for(var force:scenario.forces())for(var squad:force.squads())if(squad.members().stream().anyMatch(a->a.actorId().equals(actorId)))return new ScenarioLocation(force.forceId(),squad.squadId());
        throw new IllegalArgumentException("Actor no pertenece al escenario: "+actorId);
    }
    private record ScenarioLocation(String forceId,String squadId){}
    private static ActorOriginV1 origin(CombatActorOriginFact o){return switch(o.kind()){case SUBPROFESSION->ActorOriginV1.SUBPROFESSION;case FERAE_INTELLIGENCE->ActorOriginV1.FERAE_INTELLIGENCE;case UNSPECIFIED->ActorOriginV1.UNSPECIFIED;};}
    private static String originKey(CombatActorOriginFact o){return o.subprofession().map(Enum::name).orElseGet(()->o.feraeSpecies().map(Enum::name).orElse(""));}
}
