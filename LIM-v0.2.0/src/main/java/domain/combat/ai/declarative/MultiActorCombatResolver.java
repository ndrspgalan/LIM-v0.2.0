package domain.combat.ai.declarative;

import domain.ability.MasteryId;
import domain.combat.ai.encounter.*;
import domain.combat.ai.execution.CombatAction;
import domain.combat.ai.remote.RemoteArsenalSnapshot;
import java.util.*;

/**  — construye hechos y acciones dirigidas sin seleccionar objetivo ni coordinar tácticamente. */
public final class MultiActorCombatResolver {
    private final PerceivedCombatStateResolver perception = new PerceivedCombatStateResolver();
    private final RemoteActionCandidateResolver remote = new RemoteActionCandidateResolver();

    public List<PerceivedActorFact> actors(MultiActorDecisionState state) {
        Objects.requireNonNull(state); List<PerceivedActorFact> out=new ArrayList<>();
        for(MultiActorTargetState t:state.actors()) {
            CombatParticipantSnapshot p=t.participant();
            if(!p.perceptible()&&!p.forcedTargetByEffect()) continue;
            PerceivedCombatState pc=perception.resolve(t.observation(),t.perception());
            boolean visual=pc.visualContact();
            out.add(new PerceivedActorFact(p.actorId(),p.relationship(),p.state(),p.perceptible(),p.mounted(),p.forcedTargetByEffect(),
                    pc.observedDistanceMeters(), visual?OptionalDouble.of(p.observedMissingHealth()):OptionalDouble.empty(),
                    pc.observedStaminaDepletionIntensity(),visual?OptionalDouble.of(p.observedStaggerSeconds()):OptionalDouble.empty(),
                    pc.visibleCurrentAction(), visual&&p.actionInterruptible(),pc));
        }
        return List.copyOf(out);
    }

    public List<ObservedIntentFact> intents(MultiActorDecisionState state) {
        Objects.requireNonNull(state); List<ObservedIntentFact> out=new ArrayList<>();
        for(CombatIntentBroadcast e:state.intentEvents()) {
            double age=state.combatTimeSeconds()-e.timeSeconds();
            if(age<0||age>state.maximumIntentAgeSeconds()) continue;
            boolean v=e.visible()&&state.observerCanSeeIntent(); boolean a=e.audible()&&state.observerCanHearIntent();
            if(v||a) out.add(new ObservedIntentFact(e.actorId(),e.targetId(),e.action(),v,a,age));
        }
        return List.copyOf(out);
    }

    public List<AreaActorConsequenceFact> areaConsequences(MultiActorDecisionState state) {
        return state.knownAreaConsequences().stream().map(a->new AreaActorConsequenceFact(a.actorId(),a.relationship(),a.expectedDamage(),a.expectedControlSeconds())).toList();
    }

    public List<DirectedActionCandidate> directed(List<PerceivedActorFact> actors, List<MeleeActionCandidate> melee,
                                                   List<AbilityActionCandidate> abilities, MultiActorDecisionState state) {
        Objects.requireNonNull(actors);Objects.requireNonNull(melee);Objects.requireNonNull(abilities);Objects.requireNonNull(state);
        Map<String,MultiActorTargetState> inputs=new LinkedHashMap<>(); for(MultiActorTargetState x:state.actors())inputs.put(x.participant().actorId(),x);
        List<DirectedActionCandidate> out=new ArrayList<>();
        for(PerceivedActorFact actor:actors) {
            if(actor.observedDistanceMeters().isEmpty()) continue;
            double d=actor.observedDistanceMeters().getAsDouble();
            for(MeleeActionCandidate m:melee) {
                boolean inRange=d<=m.reachMeters();
                List<String> rel=new ArrayList<>(); rel.add("REACH_METERS="+m.reachMeters()); rel.add("TRANSITION="+m.transitionFromPrevious().map(x->x.continuity().name()).orElse("NONE"));
                out.add(new DirectedActionCandidate(DirectedActionDomain.MELEE,m.weaponName()+":"+m.mode()+":"+m.motionId(),actor.actorId(),actor.relationship(),d,inRange,FriendlyFirePolicy.offensiveTargetEligibility(actor.relationship()),
                        actor.lifeState()==EncounterActorState.CONSCIOUS,actor.visibleActionInterruptible()&&isInterruptingMelee(m),rel));
            }
            MultiActorTargetState input=inputs.get(actor.actorId());
            if(input!=null) {
                RemoteArsenalSnapshot self=input.observation().selfRemoteArsenal();
                for(RemoteActionCandidate r:remote.resolve(self,d)) {
                    boolean inRange=r.distanceState()==domain.combat.ai.remote.RangedDistanceState.ADEQUATE;
                    out.add(new DirectedActionCandidate(DirectedActionDomain.REMOTE,r.sourceName()+":"+r.action(),actor.actorId(),actor.relationship(),d,inRange,FriendlyFirePolicy.offensiveTargetEligibility(actor.relationship()),
                            actor.lifeState()==EncounterActorState.CONSCIOUS,actor.visibleActionInterruptible()&&isInterruptingRemote(r),
                            List.of("READINESS="+r.readiness(),"DISTANCE_STATE="+r.distanceState())));
                }
            }
            for(AbilityActionCandidate a:abilities) if(targetedAbility(a)) {
                boolean conscious=actor.lifeState()==EncounterActorState.CONSCIOUS;
                out.add(new DirectedActionCandidate(DirectedActionDomain.ABILITY,a.familyName()+":"+a.manifestationName()+":"+a.actionType(),actor.actorId(),actor.relationship(),d,true,abilityEligibility(a,actor),conscious,
                        actor.visibleActionInterruptible()&&a.familyId()==MasteryId.INCITAR,List.of(a.mechanicalRelation())));
            }
        }
        return List.copyOf(out);
    }

    private static boolean targetedAbility(AbilityActionCandidate a){
        return a.actionType()==AbilityActionType.ACTIVATE && (a.familyId()==MasteryId.SANAR||a.familyId()==MasteryId.INCITAR||a.familyId()==MasteryId.ANULACION);
    }
    private static DirectedTargetEligibility abilityEligibility(AbilityActionCandidate a,PerceivedActorFact t){
        if(a.familyId()==MasteryId.SANAR) return t.lifeState()==EncounterActorState.DEAD?DirectedTargetEligibility.KNOWN_INVALID:DirectedTargetEligibility.UNRESOLVED_WITH_CURRENT_KNOWLEDGE;
        if(a.familyId()==MasteryId.INCITAR||a.familyId()==MasteryId.ANULACION) {
            if(t.relationship()!=domain.social.RelationshipType.HOSTILE||t.lifeState()!=EncounterActorState.CONSCIOUS) return DirectedTargetEligibility.KNOWN_INVALID;
            return DirectedTargetEligibility.UNRESOLVED_WITH_CURRENT_KNOWLEDGE;
        }
        return DirectedTargetEligibility.UNRESOLVED_WITH_CURRENT_KNOWLEDGE;
    }
    private static boolean isInterruptingMelee(MeleeActionCandidate m){
        return switch(m.action()){case LIGHT_ATTACK,HEAVY_ATTACK,CHARGED_ATTACK,JUMP_ATTACK,DESTABILIZE -> true; default -> false;};
    }
    private static boolean isInterruptingRemote(RemoteActionCandidate r){return r.action()==RemoteActionType.FIRE||r.action()==RemoteActionType.THROW;}
}
