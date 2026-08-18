package qa.regression;

import domain.combat.ai.declarative.*;
import domain.combat.ai.memory.*;
import domain.combat.ai.perception.*;
import domain.combat.ai.execution.CombatAction;
import java.util.*;

/** QA acumulado . No ejecutar salvo petición expresa. */
public final class PerceptionAndEncounterMemoryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){ verify(); }
 public static void verify(){
  CombatOutcomeMemory memory=new CombatOutcomeMemory();
  memory.advanceTime(4.0); memory.reveal("target","WEAPON:visible");
  memory.observePerception(new CombatPerceptionSnapshot(.35,List.of(),new PerceivedTargetEvidence(false,false,true,false,false,false,0)));
  memory.record(new OffensiveOutcome(new CombatActionKey(CombatAction.LIGHT_ATTACK,"arma"),true,12,0.4,1,Optional.empty(),0,4));
  org.junit.jupiter.api.Assertions.assertTrue(memory.offensiveOutcomes().size()==1,"La memoria declarativa debe conservar resultados brutos.");
  org.junit.jupiter.api.Assertions.assertTrue(memory.revealedResources().stream().anyMatch(r->r.resourceId().equals("WEAPON:visible")),"Debe conservar recursos revelados.");
  org.junit.jupiter.api.Assertions.assertTrue(memory.sensoryEvidence().lastHeardSeconds()>=0,"Debe conservar procedencia auditiva.");
 }
 
 private PerceptionAndEncounterMemoryVerification(){}
}
