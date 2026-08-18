package qa.integration;
import domain.ability.*;
import domain.character.*;
import domain.inventory.logistics.*;
import domain.social.*;
import java.util.*;
/** deporte competitivo + crianza de animales de compañía. */
public final class CompetitionAndCompanionBreedingVerification {
 private CompetitionAndCompanionBreedingVerification(){}
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){taxonomy();profiles();loadouts();animalEmpathy();}
 private static void taxonomy(){
  activeFair(Subprofession.COMPETITION_RIDER,CharacterClass.INDOMITO);
  activeFair(Subprofession.V881_MOTORCYCLE_RACER,CharacterClass.ESPECIALISTA);
  activeFair(Subprofession.COMPETITION_CYCLIST,CharacterClass.ESPECIALISTA);
  activeFair(Subprofession.TRIATHLETE,CharacterClass.INDOMITO);
  org.junit.jupiter.api.Assertions.assertTrue(DayLaborerCanonicalProfiles.activeProfiles(Subprofession.COMPANION_ANIMAL_BREEDER).keySet().equals(Set.of(CharacterClass.INTELECTUAL)),"Criador debe ser sólo Intelectual.");
 }
 private static void profiles(){
  for(Subprofession s:List.of(Subprofession.COMPETITION_RIDER,Subprofession.V881_MOTORCYCLE_RACER,Subprofession.COMPETITION_CYCLIST,Subprofession.TRIATHLETE)){
   for(var p:FairgroundWorkerCanonicalProfiles.activeProfiles(s).values())org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel deportivo no derivado: "+s);
  }
  var breeder=DayLaborerCanonicalProfiles.profile(Subprofession.COMPANION_ANIMAL_BREEDER,CharacterClass.INTELECTUAL);
  org.junit.jupiter.api.Assertions.assertTrue(breeder.genders().equals(Set.of(Gender.HOMBRE)),"Criador debe ser masculino.");
  org.junit.jupiter.api.Assertions.assertTrue(breeder.canonicalLevel()==breeder.attributes().totalAttributeLevel(),"Nivel de criador no derivado.");
 }
 private static void loadouts(){
  check(Subprofession.COMPETITION_RIDER,CharacterClass.INDOMITO,PersonalTransportType.HORSE_RACING,"Breeches V881");
  check(Subprofession.V881_MOTORCYCLE_RACER,CharacterClass.ESPECIALISTA,PersonalTransportType.MOTORCYCLE_CARDAN_V881,"Chaqueta cruzada de motorista V881");
  check(Subprofession.COMPETITION_CYCLIST,CharacterClass.ESPECIALISTA,PersonalTransportType.BICYCLE_FOLDING_V881,"Bombachos V881");
  check(Subprofession.TRIATHLETE,CharacterClass.INDOMITO,PersonalTransportType.BICYCLE_FOLDING_V881,"Knickerbockers V881");
  var e=DayLaborerStartingEquipmentCatalog.equipment(Subprofession.COMPANION_ANIMAL_BREEDER,CharacterClass.INTELECTUAL);
  CanonicalActiveInventoryEquipmentPolicy.validate(e,DayLaborerStartingEquipmentCatalog.placement(Subprofession.COMPANION_ANIMAL_BREEDER,CharacterClass.INTELECTUAL));
  org.junit.jupiter.api.Assertions.assertTrue(e.equippedAccessory().isPresent(),"Criador sin abalorio.");
 }
 private static void check(Subprofession s,CharacterClass c,PersonalTransportType t,String armor){
  var e=FairgroundWorkerStartingEquipmentCatalog.equipment(s,c);
  org.junit.jupiter.api.Assertions.assertTrue(e.personalTransport().orElseThrow()==t,"Transporte incorrecto: "+s);
  org.junit.jupiter.api.Assertions.assertTrue(e.wornGarments().stream().anyMatch(x->x.name().equals(armor)),"Falta pieza distintiva "+armor+" en "+s);
  org.junit.jupiter.api.Assertions.assertTrue(e.equippedAccessory().isPresent(),"Falta abalorio: "+s);
  CanonicalActiveInventoryEquipmentPolicy.validate(e,FairgroundWorkerStartingEquipmentCatalog.placement(s,c));
 }
 private static void animalEmpathy(){
  org.junit.jupiter.api.Assertions.assertTrue(MasteryResonancePolicy.resonates(MasteryId.EMPATIA_ANIMAL,CharacterClass.INTELECTUAL,Gender.HOMBRE),"EMPATÍA ANIMAL debe resonar con el Criador Intelectual.");
 }
 private static void activeFair(Subprofession s,CharacterClass c){org.junit.jupiter.api.Assertions.assertTrue(FairgroundWorkerCanonicalProfiles.activeProfiles(s).keySet().equals(Set.of(c)),"Clase deportiva incorrecta: "+s);}
 
}
