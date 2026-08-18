package qa.integration;

import domain.bestiarium.physical_plane.ferae.*;
import domain.bestiarium.physical_plane.ferae.charisma.CharismaFeraeProfiles;
import domain.bestiarium.physical_plane.ferae.intelligence.IntelligenceFeraeProfiles;
import domain.character.Gender;
import domain.combat.ai.declarative.*;
import domain.social.*;

public final class MdparFeraeAndAnthropometryVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
  org.junit.jupiter.api.Assertions.assertTrue(FeraeMorphologyCatalog.all().size()==FeraeSpecies.values().length,"Toda Ferae debe tener altura.");
  for(var s:FeraeSpecies.values()) for(var sex:FeraeSex.values()) org.junit.jupiter.api.Assertions.assertTrue(FeraeMorphologyCatalog.canonicalHeightMeters(s,sex)>0,"Altura Ferae inválida: "+s+"/"+sex);
  for(var s:Subprofession.values()){ org.junit.jupiter.api.Assertions.assertTrue(s.canonicalHeightMeters(Gender.HOMBRE)>0,"Altura H ausente: "+s); org.junit.jupiter.api.Assertions.assertTrue(s.canonicalHeightMeters(Gender.MUJER)>0,"Altura M ausente: "+s); }
  for(var p:CharismaFeraeProfiles.all()){ org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(domain.character.sheet.Attribute.FE)==1,"FE animal debe quedar basal: "+p.species()); org.junit.jupiter.api.Assertions.assertTrue(p.canonicalHeightMeters()>0,"Altura CARISMA."); }
  for(var p:IntelligenceFeraeProfiles.all()){ org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(domain.character.sheet.Attribute.FE)==1,"FE animal debe quedar basal: "+p.species()); org.junit.jupiter.api.Assertions.assertTrue(p.canonicalHeightMeters()>0,"Altura INTELIGENCIA."); var a=MdparCombatActorFactory.fromIntelligenceFerae("fera",p,10,10); org.junit.jupiter.api.Assertions.assertTrue(a.origin().kind()==CombatActorOriginKind.FERAE_INTELLIGENCE,"Origen Ferae no serializable."); }
  boolean rejected=false; try{ MdparCombatActorFactory.fromIntelligenceFerae("no",CharismaFeraeProfiles.all().getFirst(),10,10); }catch(IllegalArgumentException e){rejected=true;} org.junit.jupiter.api.Assertions.assertTrue(rejected,"CARISMA no debe entrar en endpoint MDPAR de combate.");
 }
 
}
