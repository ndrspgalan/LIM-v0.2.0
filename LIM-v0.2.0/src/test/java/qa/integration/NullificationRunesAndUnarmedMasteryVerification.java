package qa.integration;
import domain.ability.*; import domain.social.RelationshipType; import domain.combat.*; import java.util.*;
public final class NullificationRunesAndUnarmedMasteryVerification{@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
 org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.eligible(RelationshipType.HOSTILE,7,6),"hostil + aguante"); var n=NullificationPolicy.apply(RelationshipType.HOSTILE,7,6,true,"Astilla",false); org.junit.jupiter.api.Assertions.assertTrue(n.suppressed(),"inhibe abalorio"); org.junit.jupiter.api.Assertions.assertTrue(!NullificationPolicy.accessoryPropertyUsable(n,"Astilla")&&NullificationPolicy.accessoryPropertyUsable(n,"Otro"),"captura instancia/nombre equipado"); org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.masteryUsable(n)&&NullificationPolicy.runicMarkUsable(n),"no inhibe maestría/runa");
 MasteryEffectRegistry effects=new MasteryEffectRegistry(); effects.apply(new MasteryEffect("obsoleto","PULSION:OLD","self",0,true,Map.of("BLUNT_MULTIPLIER",1.5))); PhysicalDamage d=new MasteryPhysicalOffenseResolver().resolveGross(new PhysicalDamage(0,0,30),MasteryPhysicalOffenseResolver.Source.UNARMED,effects); org.junit.jupiter.api.Assertions.assertTrue(d.blunt()==30,"Pulsión no amplifica B");}
 }
