package qa.domain;
import domain.character.*; import domain.character.canonical.*; import domain.control.*; import domain.social.*; import domain.combat.ai.declarative.*; import java.util.*;
public final class CanonicalTimelapseAndDoctrineVerification{
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  org.junit.jupiter.api.Assertions.assertTrue(KenanCanonicalProfile.AGE_YEARS==6&&KenanCanonicalProfile.INITIAL_LEVEL==9,"Kenan debe comenzar CHILD nivel 9");
  org.junit.jupiter.api.Assertions.assertTrue(KenanCanonicalProfile.initialSheet().totalAttributeLevel()==9,"Kenan CHILD 1x9");
  var all=CanonicalCharacterTimelineCatalog.all(); org.junit.jupiter.api.Assertions.assertTrue(all.size()==22,"Debe haber Kenan CHILD + 7 NPC x 3 etapas");
  org.junit.jupiter.api.Assertions.assertTrue(CanonicalCharacterTimelineCatalog.forName("Kenan").size()==1,"Kenan adolescente/adulto queda fuera del canon fijo");
  for(var p:all){org.junit.jupiter.api.Assertions.assertTrue(p.stage().ageYears()==6||p.stage().ageYears()==15||p.stage().ageYears()==18,"Edad de timelapse"); if(p.stage()==CanonicalLifeStage.CHILD){org.junit.jupiter.api.Assertions.assertTrue(p.fixedLevel().orElseThrow()==9&&p.forcedMasteries().size()==2&&p.relationshipAmongCanonicalPeers()==RelationshipType.RELIABLE,"Reglas CHILD");} else {org.junit.jupiter.api.Assertions.assertTrue(p.fixedLevel().isEmpty()&&p.relationshipAmongCanonicalPeers()==RelationshipType.FRIENDLY,"NPC 15/18 sin nivel fijo y amistosos");}}
  var pc=PcControlScheme.canonicalBindings(); org.junit.jupiter.api.Assertions.assertTrue(pc.stream().anyMatch(b->b.input().equals("TAB")&&b.action()==ControlAction.SHEATHE_OR_UNSHEATHE),"TAB envainar"); org.junit.jupiter.api.Assertions.assertTrue(pc.stream().anyMatch(b->b.input().equals("SHIFT")&&b.action()==ControlAction.RUN),"SHIFT correr"); org.junit.jupiter.api.Assertions.assertTrue(pc.stream().anyMatch(b->b.input().equals("Q")&&b.action()==ControlAction.SWITCH_WEAPON),"Q cambiar arma");
  org.junit.jupiter.api.Assertions.assertTrue(FriendlyFirePolicy.committedOffenseForbidden(RelationshipType.FRIENDLY)&&FriendlyFirePolicy.committedOffenseForbidden(RelationshipType.ROMANTIC)&&!FriendlyFirePolicy.committedOffenseForbidden(RelationshipType.HOSTILE),"Veto friendly fire");
  var rp=new InitialRelationshipPolicy(); org.junit.jupiter.api.Assertions.assertTrue(rp.betweenPositiveOnly(List.of(Profession.DRESSMAKER,Profession.HAIRDRESSER),List.of(Profession.TEACHER)).ordinal()>=rp.between(Profession.DRESSMAKER,Profession.TEACHER).ordinal(),"Multiprofesión sólo positiva");
  org.junit.jupiter.api.Assertions.assertTrue(domain.bestiarium.physical_plane.npc.CanonicalNpcCatalog.all().size()==7,"Siete NPC aparte de Kenan");
 }
 
}
