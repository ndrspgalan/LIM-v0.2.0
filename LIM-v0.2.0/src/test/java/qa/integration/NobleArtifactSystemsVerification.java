package qa.integration;

import domain.character.CharacterClass;
import domain.inventory.item.ArtifactAccessory;
import domain.inventory.item.accessory.*;
import domain.social.*;

public final class NobleArtifactSystemsVerification {
 private NobleArtifactSystemsVerification(){}
 
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
  org.junit.jupiter.api.Assertions.assertTrue(NobleCanonicalProfiles.profiles(Subprofession.ENLIGHTENED_PATRON).get(CharacterClass.MAESTRO).genders().size()==2,"Mecenas debe admitir Maestro hombre y mujer.");
  org.junit.jupiter.api.Assertions.assertTrue(NobleCanonicalProfiles.profiles(Subprofession.STRATEGIC_COMMUNICATIONS_OFFICER).keySet().equals(java.util.Set.of(CharacterClass.INTELECTUAL)),"Comunicaciones debe ser Intelectual.");
  org.junit.jupiter.api.Assertions.assertTrue(NobleCanonicalProfiles.profiles(Subprofession.FORENSIC_INVESTIGATOR).keySet().equals(java.util.Set.of(CharacterClass.INTELECTUAL)),"Forense debe ser Intelectual.");
  org.junit.jupiter.api.Assertions.assertTrue(NobleCanonicalProfiles.profiles(Subprofession.INTELLIGENCE_AGENT).keySet().equals(java.util.Set.of(CharacterClass.ESPECIALISTA)),"Inteligencia debe ser Especialista.");
  org.junit.jupiter.api.Assertions.assertTrue(NobleCanonicalProfiles.profiles(Subprofession.FIELD_ELECTROATMOSPHERIC_SPECIALIST).keySet().equals(java.util.Set.of(CharacterClass.INTELECTUAL)),"Electroatmosferista debe ser Intelectual.");
  for(var a:ArtifactAccessoryCatalog.all()){ org.junit.jupiter.api.Assertions.assertTrue(a instanceof ArtifactAccessory,"Todo artefacto debe implementar contrato."); var x=(ArtifactAccessory)a; org.junit.jupiter.api.Assertions.assertTrue(x.activationMinimum()==22,"CLA 22 en "+a.name()); }
  org.junit.jupiter.api.Assertions.assertTrue(V881ArtifactUsePolicy.TOKKOSHO_ELECTRIC_DAMAGE==100.0,"Tokkosho = 100 electricidad.");
  var ok=new V881ArtifactUsePolicy.Context(true,true,true,true,true,true,false,true);
  org.junit.jupiter.api.Assertions.assertTrue(V881ArtifactUsePolicy.tokkosho(ok).activated(),"Tokkosho requiere contexto válido.");
  org.junit.jupiter.api.Assertions.assertTrue(!V881ArtifactUsePolicy.tokkosho(new V881ArtifactUsePolicy.Context(true,false,true,true,true,true,false,true)).activated(),"Tokkosho exige blanco fijado.");
  org.junit.jupiter.api.Assertions.assertTrue(V881ArtifactUsePolicy.heliograph(ok).effect().equals("INTERRUPT_CURRENT_ACTION"),"Heliógrafo interrumpe.");
  org.junit.jupiter.api.Assertions.assertTrue(!V881ArtifactUsePolicy.heliograph(new V881ArtifactUsePolicy.Context(true,true,true,true,true,true,true,true)).activated(),"Cara protegida contrarresta heliógrafo.");
  org.junit.jupiter.api.Assertions.assertTrue(V881ArtifactUsePolicy.nocturlabe(ok).value()==3.0,"Nocturlabio reproduce 3 s.");
  org.junit.jupiter.api.Assertions.assertTrue(!V881ArtifactUsePolicy.nocturlabe(new V881ArtifactUsePolicy.Context(true,true,true,true,true,false,false,true)).activated(),"Nocturlabio sólo de noche.");
  org.junit.jupiter.api.Assertions.assertTrue(V881ArtifactUsePolicy.SEISMOSCOPE_NOMINAL_RADIUS_METERS==20.0,"Sismoscopio 20 m nominales.");
  var forensic=NobleStartingEquipmentCatalog.equipment(Subprofession.FORENSIC_INVESTIGATOR,CharacterClass.INTELECTUAL);
  org.junit.jupiter.api.Assertions.assertTrue(forensic.inventoryObjectNames().containsAll(java.util.List.of("Cámara fotográfica V881","Contenedor toxicológico Stas-Otto V881","Aparato de Marsh V881")),"Instrumental forense incompleto.");
 }
}
