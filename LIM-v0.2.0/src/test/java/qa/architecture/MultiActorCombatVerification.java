package qa.architecture;

import domain.combat.ai.declarative.*;
import domain.combat.ai.encounter.*;
import domain.combat.ai.execution.CombatAction;
import domain.social.RelationshipType;
import java.util.List;

/** el antiguo selector multi-actor se sustituye por contexto multi-actor sin target preseleccionado. */
public final class MultiActorCombatVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        var rel=new KnownActorRelationshipFact("a","b",RelationshipType.HOSTILE,KnowledgeTemporalState.OBSERVED_NOW);
        var state=new MultiActorDecisionState(List.of(),
                List.of(new CombatIntentBroadcast("a","b",CombatAction.RELOAD,true,false,1)),
                List.of(new AffectedActorConsequence("ally",RelationshipType.FRIENDLY,12,.5)),
                List.of(rel),true,false,1.5,1);
        var resolver=new MultiActorCombatResolver();
        org.junit.jupiter.api.Assertions.assertTrue(resolver.intents(state).size()==1,"La intención perceptible debe declararse sin seleccionar target.");
        org.junit.jupiter.api.Assertions.assertTrue(resolver.areaConsequences(state).getFirst().expectedDamage()==12,"Friendly fire conserva consecuencia física.");
        org.junit.jupiter.api.Assertions.assertTrue(state.knownRelationships().contains(rel),"La relación conocida se expone como hecho.");
        org.junit.jupiter.api.Assertions.assertTrue(!java.nio.file.Files.exists(java.nio.file.Path.of("src/main/java/domain/combat/ai/encounter/TargetSelectionPolicy.java")),"TargetSelectionPolicy debe desaparecer.");
    }
    
}
