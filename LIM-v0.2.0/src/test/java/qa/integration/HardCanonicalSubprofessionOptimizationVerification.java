package qa.integration;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.social.*;
import java.util.*;

/**  — contrato acumulativo para la migración de subprofesiones: mínima granularidad,
 * sexo coherente, hojas explícitas, patrimonio físico y ausencia de duplicados. */
public final class HardCanonicalSubprofessionOptimizationVerification {
    private HardCanonicalSubprofessionOptimizationVerification(){}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        m4915Refined();
        m4916Refined();
        m4917Taxonomy();
        explicitProfiles();
        sexualCoherence();
        noDuplicateProfiles();
        iRndRestricted();
    }
    private static void m4915Refined(){
        exact(Profession.HAIRDRESSER,Set.of(Subprofession.BARBER,Subprofession.SALON_HAIRDRESSER));
        exact(Profession.TANNER,Set.of(Subprofession.HIDE_PREPARER,Subprofession.INDUSTRIAL_TANNER,Subprofession.LEATHER_FINISHER_GRADER));
        exact(Profession.DRESSMAKER,Set.of(Subprofession.WORK_TAILOR,Subprofession.PRECISION_PATTERNMAKER,Subprofession.SALON_DRESSMAKER));
        exact(Profession.STONEMASON,Set.of(Subprofession.STONE_SETTER,Subprofession.STONEWORK_MASTER,Subprofession.PRECISION_STONECUTTER));
        exact(Profession.CARPENTER,Set.of(Subprofession.STRUCTURAL_CARPENTER,Subprofession.BENCH_CARPENTER,Subprofession.CABINETMAKER));
        active(Subprofession.BARBER,CharacterClass.INTELECTUAL);
        active(Subprofession.SALON_HAIRDRESSER,CharacterClass.HERALDO);
        active(Subprofession.HIDE_PREPARER,CharacterClass.LUCHADOR);
        active(Subprofession.INDUSTRIAL_TANNER,CharacterClass.INTELECTUAL);
        active(Subprofession.LEATHER_FINISHER_GRADER,CharacterClass.ESPECIALISTA);
        active(Subprofession.WORK_TAILOR,CharacterClass.INTELECTUAL);
        active(Subprofession.PRECISION_PATTERNMAKER,CharacterClass.ESPECIALISTA);
        active(Subprofession.SALON_DRESSMAKER,CharacterClass.HERALDO);
        active(Subprofession.STONE_SETTER,CharacterClass.LUCHADOR);
        active(Subprofession.STONEWORK_MASTER,CharacterClass.INTELECTUAL);
        active(Subprofession.PRECISION_STONECUTTER,CharacterClass.ESPECIALISTA);
        active(Subprofession.STRUCTURAL_CARPENTER,CharacterClass.LUCHADOR);
        active(Subprofession.BENCH_CARPENTER,CharacterClass.ESPECIALISTA);
        active(Subprofession.CABINETMAKER,CharacterClass.INTELECTUAL);
    }
    private static void m4916Refined(){
        active(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,CharacterClass.INTELECTUAL);
        active(Subprofession.FAIRGROUND_ENTREPRENEUR,CharacterClass.APODERADO);
        active(Subprofession.TAVERN_MUSICIAN,CharacterClass.ESPECIALISTA);
        active(Subprofession.GAME_MASTER,CharacterClass.INTELECTUAL);
        active(Subprofession.ROAD_GUIDE,CharacterClass.INTELECTUAL);
        active(Subprofession.WILDLIFE_TRACKER,CharacterClass.ESPECIALISTA);
        active(Subprofession.PROFESSIONAL_HUNTER,CharacterClass.LUCHADOR,CharacterClass.ESPECIALISTA);
        active(Subprofession.TRAPPER,CharacterClass.INDOMITO);
        active(Subprofession.COASTAL_FISHER,CharacterClass.LUCHADOR);
        active(Subprofession.OFFSHORE_FISHER,CharacterClass.INDOMITO);
        active(Subprofession.V881_NAVIGATOR,CharacterClass.INTELECTUAL);
        active(Subprofession.NAVAL_RAILGUN_GUNNER,CharacterClass.INTELECTUAL);
        active(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,CharacterClass.INTELECTUAL);
        active(Subprofession.MERCHANT_SAILOR,CharacterClass.APODERADO);
    }
    private static void m4917Taxonomy(){
        exact(Profession.COURTESAN,Set.of(Subprofession.SEX_WORKER,Subprofession.SALON_COURTESAN,Subprofession.PROFESSIONAL_COMPANION));
        exact(Profession.BLACKSMITH,Set.of(Subprofession.V881_ELECTROMECHANIC,Subprofession.DOMESTIC_V881_INSTALLER,Subprofession.FREQUENCY_INSTRUMENT_MAKER,Subprofession.MATRIX_ARCHITECT));
        active(Subprofession.SEX_WORKER,CharacterClass.ESPECIALISTA);
        active(Subprofession.SALON_COURTESAN,CharacterClass.HERALDO);
        active(Subprofession.PROFESSIONAL_COMPANION,CharacterClass.APODERADO);
        active(Subprofession.V881_ELECTROMECHANIC,CharacterClass.INTELECTUAL);
        active(Subprofession.DOMESTIC_V881_INSTALLER,CharacterClass.LUCHADOR);
        active(Subprofession.FREQUENCY_INSTRUMENT_MAKER,CharacterClass.ESPECIALISTA);
        active(Subprofession.MATRIX_ARCHITECT,CharacterClass.INTELECTUAL);
        org.junit.jupiter.api.Assertions.assertTrue(countActive(Profession.COURTESAN)==3,"Cortesana debe tener 3 perfiles activos.");
        org.junit.jupiter.api.Assertions.assertTrue(countActive(Profession.BLACKSMITH)==4,"Herrero debe tener 4 perfiles activos.");
    }
    private static void explicitProfiles(){
        for(var s:allRefined())for(var e:profiles(s).entrySet()){
            var p=e.getValue();
            org.junit.jupiter.api.Assertions.assertTrue(p.attributes().attributeValues().size()==9,"Hoja no explícita: "+s+"/"+e.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel no derivado: "+s+"/"+e.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(OccupationalNarrativeAccessoryCatalog.priceValeritasFor(s.name(),e.getKey().name())>0,"Abalorio sin precio: "+s+"/"+e.getKey());
            var a=equipment(s,e.getKey()).equippedAccessory().orElseThrow();
            String n=" "+a.narrativeDescription().toLowerCase(Locale.ROOT)+" ";
            org.junit.jupiter.api.Assertions.assertTrue(n.contains(" me ")||n.contains(" mi ")||n.contains(" yo ")||n.contains(" guardo ")||n.contains(" conservo ")||n.contains(" llevo "),"Abalorio no narrado en primera persona: "+s+"/"+e.getKey());
            placement(s,e.getKey()).validateAgainst(equipment(s,e.getKey()));
        }
    }
    private static void sexualCoherence(){
        for(var s:allRefined())for(var e:profiles(s).entrySet()){
            Gender expected=switch(e.getKey()){case LUCHADOR,INTELECTUAL,INDOMITO->Gender.HOMBRE;case ESPECIALISTA,APODERADO,HERALDO->Gender.MUJER;case MAESTRO->null;};
            if(expected!=null)org.junit.jupiter.api.Assertions.assertTrue(e.getValue().genders().equals(Set.of(expected)),"Sexo/clase incoherente: "+s+"/"+e.getKey());
        }
    }
    private static void noDuplicateProfiles(){
        for(var profession:List.of(Profession.COURTESAN,Profession.BLACKSMITH)){
            Set<String> signatures=new HashSet<>();
            for(var s:Subprofession.forProfession(profession))for(var e:profiles(s).entrySet()){
                var load=equipment(s,e.getKey());
                String sig=e.getValue().attributes().attributeValues()+"|"+load.wornGarments()+"|"+load.inventoryObjectNames()+"|"+load.weaponNames()+"|"+load.materialUnits()+"|"+load.equippedAccessory().orElseThrow().name();
                org.junit.jupiter.api.Assertions.assertTrue(signatures.add(sig),"Perfil duplicado: "+profession+"/"+s+"/"+e.getKey());
            }
        }
    }
    private static void iRndRestricted(){
        for(var s:allRefined())for(var c:profiles(s).keySet())
            org.junit.jupiter.api.Assertions.assertTrue(equipment(s,c).inventoryObjectNames().stream().noneMatch("Frasco de I-RND"::equals),"I-RND fuera de Maestro/Noble: "+s+"/"+c);
    }
    private static Set<Subprofession> allRefined(){
        var x=new LinkedHashSet<Subprofession>();
        for(var p:List.of(Profession.HAIRDRESSER,Profession.TANNER,Profession.DRESSMAKER,Profession.STONEMASON,Profession.CARPENTER,Profession.FAIRGROUND_WORKER,Profession.HUNTER,Profession.SAILOR,Profession.COURTESAN,Profession.BLACKSMITH))x.addAll(Subprofession.forProfession(p));
        return Set.copyOf(x);
    }
    private static int countActive(Profession p){return Subprofession.forProfession(p).stream().mapToInt(s->profiles(s).size()).sum();}
    private static void exact(Profession p,Set<Subprofession> expected){org.junit.jupiter.api.Assertions.assertTrue(new HashSet<>(Subprofession.forProfession(p)).equals(expected),"Taxonomía divergente: "+p);}
    private static void active(Subprofession s,CharacterClass... cs){org.junit.jupiter.api.Assertions.assertTrue(profiles(s).keySet().equals(Set.of(cs)),"Matriz activa divergente: "+s+" -> "+profiles(s).keySet());}
    private static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){return switch(s.profession()){
        case HAIRDRESSER->HairdresserCanonicalProfiles.activeProfiles(s);case TANNER->TannerCanonicalProfiles.activeProfiles(s);case DRESSMAKER->DressmakerCanonicalProfiles.activeProfiles(s);
        case STONEMASON->StonemasonCanonicalProfiles.activeProfiles(s);case CARPENTER->CarpenterCanonicalProfiles.activeProfiles(s);case FAIRGROUND_WORKER->FairgroundWorkerCanonicalProfiles.activeProfiles(s);
        case HUNTER->HunterCanonicalProfiles.activeProfiles(s);case SAILOR->SailorCanonicalProfiles.activeProfiles(s);case COURTESAN->CourtesanCanonicalProfiles.activeProfiles(s);case BLACKSMITH->BlacksmithCanonicalProfiles.activeProfiles(s);
        default->throw new IllegalArgumentException("Fuera de refinamiento : "+s);};}
    private static CanonicalStartingEquipment equipment(Subprofession s,CharacterClass c){return switch(s.profession()){
        case HAIRDRESSER->HairdresserStartingEquipmentCatalog.equipment(s,c);case TANNER->TannerStartingEquipmentCatalog.equipment(s,c);case DRESSMAKER->DressmakerStartingEquipmentCatalog.equipment(s,c);
        case STONEMASON->StonemasonStartingEquipmentCatalog.equipment(s,c);case CARPENTER->CarpenterStartingEquipmentCatalog.equipment(s,c);case FAIRGROUND_WORKER->FairgroundWorkerStartingEquipmentCatalog.equipment(s,c);
        case HUNTER->HunterStartingEquipmentCatalog.equipment(s,c);case SAILOR->SailorStartingEquipmentCatalog.equipment(s,c);case COURTESAN->CourtesanStartingEquipmentCatalog.equipment(s,c);case BLACKSMITH->BlacksmithStartingEquipmentCatalog.equipment(s,c);
        default->throw new IllegalArgumentException();};}
    private static CanonicalLoadoutPlacementPlan placement(Subprofession s,CharacterClass c){return switch(s.profession()){
        case HAIRDRESSER->HairdresserStartingEquipmentCatalog.placement(s,c);case TANNER->TannerStartingEquipmentCatalog.placement(s,c);case DRESSMAKER->DressmakerStartingEquipmentCatalog.placement(s,c);
        case STONEMASON->StonemasonStartingEquipmentCatalog.placement(s,c);case CARPENTER->CarpenterStartingEquipmentCatalog.placement(s,c);case FAIRGROUND_WORKER->FairgroundWorkerStartingEquipmentCatalog.placement(s,c);
        case HUNTER->HunterStartingEquipmentCatalog.placement(s,c);case SAILOR->SailorStartingEquipmentCatalog.placement(s,c);case COURTESAN->CourtesanStartingEquipmentCatalog.placement(s,c);case BLACKSMITH->BlacksmithStartingEquipmentCatalog.placement(s,c);
        default->throw new IllegalArgumentException();};}
    
}
