package qa.domain;
import domain.ability.*; import domain.character.Gender; import domain.character.sheet.CharacterSheet; import domain.combat.HostileEncounterState; import domain.status.VitalResourceState; import java.util.*;
public final class MasteryRuntimeCompletionVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
 CharacterSheet s=CharacterSheet.of(120,75,120,75,75,75,75,75,75); CharacterMasteryCollection c=CharacterMasteryCollection.allCanonical();
 for(MasteryType t:List.of(MasteryType.ACTIVE,MasteryType.SUSTAINED))for(MasteryManifestation m:c.selectableManifestations(t,s))org.junit.jupiter.api.Assertions.assertTrue(MasteryExecutionDispatcher.registry().contains(m),"Falta mecánica "+m.name());
 PairMastery r=(PairMastery)MasteryCatalog.require(MasteryId.EXPLOSION_CINETICA); org.junit.jupiter.api.Assertions.assertTrue(r.original().type()==MasteryType.SUSTAINED&&r.refined().type()==MasteryType.SUSTAINED,"Refinamiento sostenido");
 org.junit.jupiter.api.Assertions.assertTrue(SpiritInfatigablePolicy.globalStaminaCost(25,true,false)==0&&SpiritInfatigablePolicy.globalStaminaCost(25,true,true)==25,"Espíritu global fuera combate");}
 }
