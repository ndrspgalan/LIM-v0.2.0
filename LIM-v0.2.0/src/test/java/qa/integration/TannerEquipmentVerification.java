package qa.integration;
import domain.character.CharacterClass;
import domain.social.*;
import java.util.Map;
import java.util.function.BiFunction;
/** Historical equipment verification updated in  to current active profile matrices. */
public final class TannerEquipmentVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){ audit(TannerCanonicalProfiles.all(), Profession.TANNER, (s,c)->TannerStartingEquipmentCatalog.equipment(s,c)); }
 private static void audit(Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all, Profession p, BiFunction<Subprofession,CharacterClass,CanonicalStartingEquipment> loader){
  for(Subprofession s:Subprofession.forProfession(p)){
   Map<CharacterClass,CanonicalSubprofessionProfile> active=active(all,s,p);
   org.junit.jupiter.api.Assertions.assertTrue(!active.isEmpty(),"Perfil activo requerido: "+s);
   for(CharacterClass c:active.keySet()){
    CanonicalStartingEquipment e=loader.apply(s,c);
    CanonicalStartingEquipmentPackingPolicy.requireValid(e);
    org.junit.jupiter.api.Assertions.assertTrue(e.equippedAccessory().isPresent(),"Cada perfil profesional activo conserva abalorio: "+s+" / "+c);
   }
  }
 }
 private static Map<CharacterClass,CanonicalSubprofessionProfile> active(Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all,Subprofession s,Profession p){
  return switch(p){
   case BEGGAR -> BeggarCanonicalProfiles.activeProfiles(s);
   case DAY_LABORER -> DayLaborerCanonicalProfiles.activeProfiles(s);
   case HAIRDRESSER -> HairdresserCanonicalProfiles.activeProfiles(s);
   case FAIRGROUND_WORKER -> FairgroundWorkerCanonicalProfiles.activeProfiles(s);
   case TANNER -> TannerCanonicalProfiles.activeProfiles(s);
   case DRESSMAKER -> DressmakerCanonicalProfiles.activeProfiles(s);
   case STONEMASON -> StonemasonCanonicalProfiles.activeProfiles(s);
   case BLACKSMITH -> BlacksmithCanonicalProfiles.activeProfiles(s);
   case CARPENTER -> CarpenterCanonicalProfiles.activeProfiles(s);
   case HUNTER -> HunterCanonicalProfiles.activeProfiles(s);
   case SAILOR -> SailorCanonicalProfiles.activeProfiles(s);
   case SOLDIER -> SoldierCanonicalProfiles.activeProfiles(s);
   case MERCENARY -> MercenaryCanonicalProfiles.activeProfiles(s);
   default -> all.get(s);
  };
 }
 
}
