package domain.combat.ai.declarative;

import domain.combat.ai.memory.SensoryEvidenceMemory;
import domain.combat.ai.observation.CombatObservation;
import domain.combat.ai.perception.PerceivedTargetEvidence;
import java.util.*;

/**  — filtra el mundo autoritativo según evidencia actual + memoria episódica del actor. */
public final class PerceivedCombatStateResolver {
    public PerceivedCombatState resolve(CombatObservation o, PerceptionDecisionState s){
        Objects.requireNonNull(o); Objects.requireNonNull(s);
        PerceivedTargetEvidence e=s.current().targetEvidence(); boolean visual=e.visualContact();
        List<SensoryFact> sensory=new ArrayList<>();
        if(visual) sensory.add(new SensoryFact(KnowledgeOrigin.VISUAL,KnowledgeTemporalState.OBSERVED_NOW,0,1,"Contacto visual actual con el objetivo."));
        if(e.heard()) sensory.add(new SensoryFact(KnowledgeOrigin.AUDITORY,KnowledgeTemporalState.OBSERVED_NOW,0,1,"Evidencia auditiva actual."));
        if(e.scented()) sensory.add(new SensoryFact(KnowledgeOrigin.OLFACTORY,KnowledgeTemporalState.OBSERVED_NOW,0,1,"Evidencia olfativa actual."));
        if(e.footprintsObserved()) sensory.add(new SensoryFact(KnowledgeOrigin.FOOTPRINT,KnowledgeTemporalState.OBSERVED_NOW,0,1,"Huellas observadas."));
        if(e.impactOriginObserved()) sensory.add(new SensoryFact(KnowledgeOrigin.IMPACT_ORIGIN,KnowledgeTemporalState.OBSERVED_NOW,0,1,"Origen de impacto observado."));
        SensoryEvidenceMemory m=s.memory().sensoryEvidence(); double now=s.memory().combatTimeSeconds();
        addMemory(sensory,KnowledgeOrigin.VISUAL,m.lastVisualSeconds(),now,"Último contacto visual recordado.");
        addMemory(sensory,KnowledgeOrigin.AUDITORY,m.lastHeardSeconds(),now,"Última evidencia auditiva recordada.");
        addMemory(sensory,KnowledgeOrigin.OLFACTORY,m.lastScentSeconds(),now,"Última evidencia olfativa recordada.");
        addMemory(sensory,KnowledgeOrigin.FOOTPRINT,m.lastFootprintSeconds(),now,"Últimas huellas recordadas.");
        addMemory(sensory,KnowledgeOrigin.IMPACT_ORIGIN,m.lastImpactOriginSeconds(),now,"Último origen de impacto recordado.");
        List<String> resources=s.memory().revealedResources().stream().filter(r->r.actorId().equals(s.targetActorId())).map(r->r.resourceId()).distinct().toList();
        List<EncounterOutcomeFact> outcomes=new ArrayList<>(); s.memory().offensiveOutcomes().forEach(x->outcomes.add(EncounterOutcomeFact.from(x))); s.memory().defensiveOutcomes().forEach(x->outcomes.add(EncounterOutcomeFact.from(x)));
        return new PerceivedCombatState(s.targetActorId(),visual,e.targetLockAllowed(),visual?OptionalDouble.of(o.currentDistanceMeters()):OptionalDouble.empty(),
                e.hasAnyEvidence()?OptionalDouble.of(s.current().observedStaminaDepletionIntensity()):OptionalDouble.empty(),
                visual?s.visibleCurrentAction():Optional.empty(), visual?o.targetLoadout().weapons().stream().map(w->w.name()).toList():List.of(),
                visual?o.targetVisibleRemoteArsenal().options().stream().map(x->x.name()).toList():List.of(), resources,sensory,List.copyOf(outcomes),s.memory().targetConfigurationSignature());
    }
    private void addMemory(List<SensoryFact> out,KnowledgeOrigin origin,double at,double now,String detail){if(at>=0)out.add(new SensoryFact(origin,KnowledgeTemporalState.REMEMBERED,Math.max(0,now-at),1,detail));}
}
