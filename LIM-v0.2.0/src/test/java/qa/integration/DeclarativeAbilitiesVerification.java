package qa.integration;
import domain.ability.*; import domain.character.Gender; import domain.character.sheet.*; import domain.combat.ai.declarative.*; import java.util.*;
/** Contrato acumulativo . No ejecutar salvo petición expresa. */
public final class DeclarativeAbilitiesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){ verify(); }
 public static void verify(){
  CharacterSheet sheet=CharacterSheet.of(80,75,80,75,75,75,75,75,75); CharacterMasteryCollection c=CharacterMasteryCollection.allCanonical(); c.unlockAvailableTransmutationNodes(sheet);
  AbilityDecisionState s=new AbilityDecisionState(c,new MasteryEffectRegistry());
  CombatActorDecisionState actor=new CombatActorDecisionState("m484",Gender.HOMBRE,sheet,1.80,10,10);
  var actions=new AbilityActionCandidateResolver().resolve(actor,s);
  if(actions.stream().anyMatch(a->a.mechanicalRelation().isBlank()))throw new AssertionError("Toda capacidad debe exponer relación mecánica.");
  if(actions.stream().anyMatch(a->a.actionType()==null))throw new AssertionError("Toda capacidad debe tipar su operación.");
 }
 private DeclarativeAbilitiesVerification(){}
}
