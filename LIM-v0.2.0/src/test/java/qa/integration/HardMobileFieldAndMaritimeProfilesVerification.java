package qa.integration;
import domain.character.CharacterClass;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.social.*;
import java.util.*;
/** contrato duro del bloque Feriante/Cazador/Marinero. No se ejecuta automáticamente. */
public final class HardMobileFieldAndMaritimeProfilesVerification {
 private HardMobileFieldAndMaritimeProfilesVerification(){}
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){taxonomy();profiles();loadouts();specialContracts();}
 private static void taxonomy(){
  exact(Profession.HUNTER,Set.of(Subprofession.ROAD_GUIDE,Subprofession.WILDLIFE_TRACKER,Subprofession.PROFESSIONAL_HUNTER,Subprofession.TRAPPER));
  exact(Profession.SAILOR,Set.of(Subprofession.COASTAL_FISHER,Subprofession.OFFSHORE_FISHER,Subprofession.V881_NAVIGATOR,Subprofession.NAVAL_RAILGUN_GUNNER,Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,Subprofession.MERCHANT_SAILOR));
  active(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,Set.of(CharacterClass.INTELECTUAL));
  active(Subprofession.FAIRGROUND_ENTREPRENEUR,Set.of(CharacterClass.APODERADO));
  active(Subprofession.TAVERN_MUSICIAN,Set.of(CharacterClass.ESPECIALISTA));
  active(Subprofession.GAME_MASTER,Set.of(CharacterClass.INTELECTUAL));
  active(Subprofession.ROAD_GUIDE,Set.of(CharacterClass.INTELECTUAL));
  active(Subprofession.WILDLIFE_TRACKER,Set.of(CharacterClass.ESPECIALISTA));
  active(Subprofession.PROFESSIONAL_HUNTER,Set.of(CharacterClass.LUCHADOR,CharacterClass.ESPECIALISTA));
  active(Subprofession.TRAPPER,Set.of(CharacterClass.INDOMITO));
  active(Subprofession.COASTAL_FISHER,Set.of(CharacterClass.LUCHADOR));
  active(Subprofession.OFFSHORE_FISHER,Set.of(CharacterClass.INDOMITO));
  active(Subprofession.V881_NAVIGATOR,Set.of(CharacterClass.INTELECTUAL));
  active(Subprofession.NAVAL_RAILGUN_GUNNER,Set.of(CharacterClass.INTELECTUAL));
  active(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,Set.of(CharacterClass.INTELECTUAL));
  active(Subprofession.MERCHANT_SAILOR,Set.of(CharacterClass.APODERADO));
  org.junit.jupiter.api.Assertions.assertTrue(all().stream().mapToInt(s->active(s).size()).sum()==15," refina  a exactamente 15 perfiles activos.");
 }
 private static void profiles(){for(var s:all())for(var e:active(s).entrySet()){var c=e.getKey();var p=e.getValue();org.junit.jupiter.api.Assertions.assertTrue(p.attributes().attributeValues().size()==9,"Hoja incompleta "+s+"/"+c);org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel no derivado "+s+"/"+c);var a=equipment(s,c).equippedAccessory().orElseThrow();org.junit.jupiter.api.Assertions.assertTrue(OccupationalNarrativeAccessoryCatalog.priceValeritasFor(s.name(),c.name())>0,"Abalorio sin precio "+s+"/"+c);String n=" "+a.narrativeDescription().toLowerCase(Locale.ROOT)+" ";org.junit.jupiter.api.Assertions.assertTrue(n.contains(" me ")||n.contains(" mi ")||n.contains(" lo ")||n.contains(" llevo ")||n.contains(" guardo ")||n.contains(" conservo "),"Abalorio no biográfico "+s+"/"+c);}}
 private static void loadouts(){for(var s:all())for(var c:active(s).keySet()){var e=equipment(s,c);var p=placement(s,c);CanonicalActiveInventoryEquipmentPolicy.validate(e,p);org.junit.jupiter.api.Assertions.assertTrue(e.inventoryObjectNames().stream().noneMatch("Frasco de I-RND"::equals),"I-RND reservado a Maestro/Noble: "+s+"/"+c);}}
 private static void specialContracts(){
  for(var c:FairgroundWorkerCanonicalProfiles.activeProfiles(Subprofession.TAVERN_MUSICIAN).keySet())org.junit.jupiter.api.Assertions.assertTrue(FairgroundWorkerStartingEquipmentCatalog.equipment(Subprofession.TAVERN_MUSICIAN,c).equippedAccessory().orElseThrow().name().startsWith("INSTRUMENTO DE TABERNA"),"El instrumento debe ser el abalorio del músico: "+c);
  for(var c:HunterCanonicalProfiles.activeProfiles(Subprofession.PROFESSIONAL_HUNTER).keySet()){var e=HunterStartingEquipmentCatalog.equipment(Subprofession.PROFESSIONAL_HUNTER,c);org.junit.jupiter.api.Assertions.assertTrue(e.ammunitionNames().size()==12,"Cazador profesional: 12 flechas");org.junit.jupiter.api.Assertions.assertTrue(e.inventoryExpanders().contains(InventoryCompartmentType.ARROW_QUIVER),"Cazador profesional sin carcaj");var p=HunterStartingEquipmentCatalog.placement(Subprofession.PROFESSIONAL_HUNTER,c);org.junit.jupiter.api.Assertions.assertTrue(p.contents(InventoryCompartmentType.ARROW_QUIVER).size()==12,"Las 12 flechas deben residir en el carcaj");}
  for(var s:Subprofession.forProfession(Profession.SAILOR))for(var c:SailorCanonicalProfiles.activeProfiles(s).keySet())org.junit.jupiter.api.Assertions.assertTrue(SailorStartingEquipmentCatalog.equipment(s,c).personalTransport().isEmpty(),"El buque no es transporte personal: "+s+"/"+c);
 }
 private static Set<Subprofession> all(){return Set.of(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,Subprofession.FAIRGROUND_ENTREPRENEUR,Subprofession.TAVERN_MUSICIAN,Subprofession.GAME_MASTER,Subprofession.ROAD_GUIDE,Subprofession.WILDLIFE_TRACKER,Subprofession.PROFESSIONAL_HUNTER,Subprofession.TRAPPER,Subprofession.COASTAL_FISHER,Subprofession.OFFSHORE_FISHER,Subprofession.V881_NAVIGATOR,Subprofession.NAVAL_RAILGUN_GUNNER,Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,Subprofession.MERCHANT_SAILOR);}
 private static Map<CharacterClass,CanonicalSubprofessionProfile> active(Subprofession s){return switch(s.profession()){case FAIRGROUND_WORKER->FairgroundWorkerCanonicalProfiles.activeProfiles(s);case HUNTER->HunterCanonicalProfiles.activeProfiles(s);case SAILOR->SailorCanonicalProfiles.activeProfiles(s);default->throw new IllegalArgumentException();};}
 private static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){return switch(s.profession()){case FAIRGROUND_WORKER->FairgroundWorkerStartingEquipmentCatalog.equipment(s,c);case HUNTER->HunterStartingEquipmentCatalog.equipment(s,c);case SAILOR->SailorStartingEquipmentCatalog.equipment(s,c);default->throw new IllegalArgumentException();};}
 private static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return switch(s.profession()){case FAIRGROUND_WORKER->FairgroundWorkerStartingEquipmentCatalog.placement(s,c);case HUNTER->HunterStartingEquipmentCatalog.placement(s,c);case SAILOR->SailorStartingEquipmentCatalog.placement(s,c);default->throw new IllegalArgumentException();};}
 private static void active(Subprofession s,Set<CharacterClass>e){org.junit.jupiter.api.Assertions.assertTrue(active(s).keySet().equals(e)," matriz activa divergente: "+s+" -> "+active(s).keySet());}
 private static void exact(Profession p,Set<Subprofession>e){org.junit.jupiter.api.Assertions.assertTrue(new HashSet<>(Subprofession.forProfession(p)).equals(e),"Taxonomía divergente: "+p);}
 
}
