package qa.domain;
import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.social.*;
import java.util.Map;
public final class HardActiveInventoryAndSocialProfilesVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  audit(DayLaborerCanonicalProfiles.all(),Profession.DAY_LABORER);
  audit(BeggarCanonicalProfiles.all(),Profession.BEGGAR);
  audit(HairdresserCanonicalProfiles.all(),Profession.HAIRDRESSER);
  audit(FairgroundWorkerCanonicalProfiles.all(),Profession.FAIRGROUND_WORKER);
  audit(TannerCanonicalProfiles.all(),Profession.TANNER);
  audit(DressmakerCanonicalProfiles.all(),Profession.DRESSMAKER);
  audit(StonemasonCanonicalProfiles.all(),Profession.STONEMASON);
 }
 private static void audit(Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all,Profession p){
  for(Subprofession s:Subprofession.forProfession(p)) for(var e:active(s,p).entrySet()){
   var cp=e.getValue(); org.junit.jupiter.api.Assertions.assertTrue(cp.canonicalLevel()==cp.attributes().totalAttributeLevel(),"Nivel derivado: "+s+"/"+e.getKey());
   var acc=OccupationalNarrativeAccessoryCatalog.forProfile(s.name(),e.getKey().name());
   org.junit.jupiter.api.Assertions.assertTrue(acc!=null,"Abalorio: "+s+"/"+e.getKey());
   org.junit.jupiter.api.Assertions.assertTrue(OccupationalNarrativeAccessoryCatalog.priceValeritasFor(s.name(),e.getKey().name())>=0,"Precio: "+s+"/"+e.getKey());
  }
 }
 private static Map<CharacterClass,CanonicalSubprofessionProfile> active(Subprofession s,Profession p){return switch(p){
  case DAY_LABORER->DayLaborerCanonicalProfiles.activeProfiles(s); case BEGGAR->BeggarCanonicalProfiles.activeProfiles(s);
  case HAIRDRESSER->HairdresserCanonicalProfiles.activeProfiles(s); case FAIRGROUND_WORKER->FairgroundWorkerCanonicalProfiles.activeProfiles(s);
  case TANNER->TannerCanonicalProfiles.activeProfiles(s); case DRESSMAKER->DressmakerCanonicalProfiles.activeProfiles(s);
  case STONEMASON->StonemasonCanonicalProfiles.activeProfiles(s); default->Map.of();};}
 
}
