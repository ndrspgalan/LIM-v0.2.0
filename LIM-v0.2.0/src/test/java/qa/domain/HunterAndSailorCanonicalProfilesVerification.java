package qa.domain;
import domain.character.CharacterClass;
import domain.social.*;
import java.util.Map;
/** Historical  profile verification updated in  to the current active class/subprofession matrix. */
public final class HunterAndSailorCanonicalProfilesVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){ check(HunterCanonicalProfiles.all(), Profession.HUNTER, "HunterCanonicalProfiles"); check(SailorCanonicalProfiles.all(), Profession.SAILOR, "SailorCanonicalProfiles"); }
 private static void check(Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all, Profession profession, String label){
  org.junit.jupiter.api.Assertions.assertTrue(all.keySet().equals(new java.util.LinkedHashSet<>(Subprofession.forProfession(profession))), label+" debe cubrir exactamente las subprofesiones canónicas actuales.");
  for(var e:all.entrySet()){
   org.junit.jupiter.api.Assertions.assertTrue(!e.getValue().isEmpty(),"Cada subprofesión debe conservar al menos una clase activa: "+e.getKey());
   for(var p:e.getValue().entrySet()){
    org.junit.jupiter.api.Assertions.assertTrue(p.getValue()!=null,"Perfil no nulo: "+e.getKey()+" / "+p.getKey());
    org.junit.jupiter.api.Assertions.assertTrue(p.getValue().canonicalLevel()==p.getValue().attributes().totalAttributeLevel(),"Nivel derivado de atributos: "+e.getKey()+" / "+p.getKey());
    org.junit.jupiter.api.Assertions.assertTrue(p.getValue().narrativeRationale()!=null && !p.getValue().narrativeRationale().isBlank(),"Justificación narrativa presente: "+e.getKey()+" / "+p.getKey());
    org.junit.jupiter.api.Assertions.assertTrue(p.getValue().genders()!=null && !p.getValue().genders().isEmpty(),"Género canónico presente: "+e.getKey()+" / "+p.getKey());
   }
  }
 }
 
}
