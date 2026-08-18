package qa.architecture;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;
import domain.social.*;
import java.util.*;

/**  — verificación acumulativa exclusiva de Maestro. */
public final class HardTeacherCanonicalProfilesVerification {
    private HardTeacherCanonicalProfilesVerification(){}
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        exactTaxonomy();
        expectedMatrices();
        explicitSheets();
        genderCoherence();
        teacherEquipment();
        lucidityAndIRnd();
        physicalPlacement();
        differentiatedMaestroNarratives();
        noAffinityGain();
    }
    private static void exactTaxonomy(){
        Set<Subprofession> expected=Set.of(
                Subprofession.KINGDOM_MESSENGER,Subprofession.CYCLIST_MESSENGER,Subprofession.FREQUENCY_PHYSICIAN,
                Subprofession.FREQUENCY_RESEARCHER,Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER,
                Subprofession.ELECTROATMOSPHERIC_CAPTATION_ENGINEER,Subprofession.ELECTROATMOSPHERIC_SAFETY_ENGINEER,
                Subprofession.ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER,Subprofession.RAILWAY_INFRASTRUCTURE_ENGINEER,
                Subprofession.ELECTROMAGNETIC_TRANSPORT_PLANNER,Subprofession.SURGEON,Subprofession.VETERINARIAN,
                Subprofession.SANITARY_MASTER,Subprofession.FORESTRY_MANAGER,Subprofession.PROSPECTOR,
                Subprofession.REGENERATIONIST,Subprofession.CONTINUITY_EPIGENETICIST,Subprofession.NEUROARCHITECT,
                Subprofession.SOUL_RESEARCHER,Subprofession.SOUL_TRANSFUSIONIST,Subprofession.SILICIC_METAMORPHOSIS_RESEARCHER,
                Subprofession.PERMANENCE_RESEARCHER,Subprofession.ENLIGHTENED);
        org.junit.jupiter.api.Assertions.assertTrue(new HashSet<>(Subprofession.forProfession(Profession.TEACHER)).equals(expected),"Taxonomía Maestro  divergente.");
        org.junit.jupiter.api.Assertions.assertTrue(expected.size()==23," debe contener 23 subprofesiones Maestro.");
    }
    private static void expectedMatrices(){
        active(Subprofession.KINGDOM_MESSENGER,CharacterClass.INTELECTUAL);
        active(Subprofession.CYCLIST_MESSENGER,CharacterClass.INDOMITO);
        active(Subprofession.FREQUENCY_PHYSICIAN,CharacterClass.ESPECIALISTA);
        active(Subprofession.FREQUENCY_RESEARCHER,CharacterClass.INTELECTUAL);
        active(Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER,CharacterClass.INTELECTUAL);
        active(Subprofession.ELECTROATMOSPHERIC_CAPTATION_ENGINEER,CharacterClass.ESPECIALISTA);
        active(Subprofession.ELECTROATMOSPHERIC_SAFETY_ENGINEER,CharacterClass.MAESTRO);
        active(Subprofession.ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER,CharacterClass.INTELECTUAL);
        active(Subprofession.RAILWAY_INFRASTRUCTURE_ENGINEER,CharacterClass.MAESTRO);
        active(Subprofession.ELECTROMAGNETIC_TRANSPORT_PLANNER,CharacterClass.HERALDO);
        active(Subprofession.SURGEON,CharacterClass.LUCHADOR);
        active(Subprofession.VETERINARIAN,CharacterClass.APODERADO);
        active(Subprofession.SANITARY_MASTER,CharacterClass.MAESTRO);
        active(Subprofession.FORESTRY_MANAGER,CharacterClass.APODERADO);
        active(Subprofession.PROSPECTOR,CharacterClass.INDOMITO);
        for(Subprofession s:List.of(Subprofession.REGENERATIONIST,Subprofession.CONTINUITY_EPIGENETICIST,Subprofession.NEUROARCHITECT,
                Subprofession.SOUL_RESEARCHER,Subprofession.SOUL_TRANSFUSIONIST,Subprofession.SILICIC_METAMORPHOSIS_RESEARCHER,
                Subprofession.PERMANENCE_RESEARCHER,Subprofession.ENLIGHTENED)) active(s,CharacterClass.MAESTRO);
        org.junit.jupiter.api.Assertions.assertTrue(totalActive()==23," matriz activa debe contener un perfil por subprofesión.");
    }
    private static void explicitSheets(){
        for(var e:TeacherCanonicalProfiles.all().entrySet()){
            for(var p:e.getValue().values()){
                org.junit.jupiter.api.Assertions.assertTrue(p.attributes().attributeValues().size()==9,"Hoja no explícita: "+e.getKey()+"/"+p.characterClass());
                org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(),"Nivel no derivado de atributos: "+e.getKey()+"/"+p.characterClass());
                org.junit.jupiter.api.Assertions.assertTrue(OccupationalNarrativeAccessoryCatalog.priceValeritasFor(e.getKey().name(),p.characterClass().name())>0,"Abalorio sin precio: "+e.getKey());
                var a=TeacherStartingEquipmentCatalog.equipment(e.getKey(),p.characterClass()).equippedAccessory().orElseThrow();
                String n=" "+a.narrativeDescription().toLowerCase(Locale.ROOT)+" ";
                org.junit.jupiter.api.Assertions.assertTrue(n.contains(" yo ")||n.contains(" me ")||n.contains(" mi ")||n.contains(" lo guardo")||n.contains(" la guardo")||n.contains(" lo conserva")||n.contains(" la conserva"),
                        "Abalorio sin primera persona: "+e.getKey());
            }
        }
    }
    private static void genderCoherence(){
        for(var e:TeacherCanonicalProfiles.all().entrySet())for(var p:e.getValue().values()){
            Set<Gender> g=p.genders();
            if(p.characterClass()==CharacterClass.MAESTRO)org.junit.jupiter.api.Assertions.assertTrue(g.equals(Set.of(Gender.HOMBRE,Gender.MUJER)),"Maestro debe admitir ambos sexos: "+e.getKey());
            else{
                Gender expected=switch(p.characterClass()){case LUCHADOR,INTELECTUAL,INDOMITO->Gender.HOMBRE;case ESPECIALISTA,APODERADO,HERALDO->Gender.MUJER;case MAESTRO->null;};
                org.junit.jupiter.api.Assertions.assertTrue(g.equals(Set.of(expected)),"Sexo/clase incoherente: "+e.getKey()+"/"+p.characterClass());
            }
        }
    }
    private static void teacherEquipment(){
        for(var e:TeacherCanonicalProfiles.all().entrySet())for(var c:e.getValue().keySet()){
            var load=TeacherStartingEquipmentCatalog.equipment(e.getKey(),c);
            org.junit.jupiter.api.Assertions.assertTrue(load.equippedAccessory().isPresent(),"Falta abalorio: "+e.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(load.inventoryObjectNames().contains("Esencia de lucidez"),"Falta Esencia de lucidez: "+e.getKey());
            org.junit.jupiter.api.Assertions.assertTrue(!load.weaponNames().stream().anyMatch(x->x.contains("Arrojadizo")),"Arrojadiza en weapon slot: "+e.getKey());
        }
    }
    private static void lucidityAndIRnd(){
        Set<Subprofession> high=Set.of(Subprofession.REGENERATIONIST,Subprofession.CONTINUITY_EPIGENETICIST,Subprofession.NEUROARCHITECT,
                Subprofession.SOUL_RESEARCHER,Subprofession.SOUL_TRANSFUSIONIST,Subprofession.SILICIC_METAMORPHOSIS_RESEARCHER,
                Subprofession.PERMANENCE_RESEARCHER,Subprofession.ENLIGHTENED);
        for(var e:TeacherCanonicalProfiles.all().entrySet())for(var c:e.getValue().keySet()){
            boolean irnd=TeacherStartingEquipmentCatalog.equipment(e.getKey(),c).inventoryObjectNames().contains("Frasco de I-RND");
            org.junit.jupiter.api.Assertions.assertTrue(irnd==high.contains(e.getKey()),"I-RND restringido al bloque 12-19: "+e.getKey());
        }
    }
    private static void physicalPlacement(){
        for(var e:TeacherCanonicalProfiles.all().entrySet())for(var c:e.getValue().keySet()){
            var load=TeacherStartingEquipmentCatalog.equipment(e.getKey(),c);
            var p=TeacherStartingEquipmentCatalog.placement(e.getKey(),c);
            p.validateAgainst(load);
            boolean saddle=load.inventoryExpanders().stream().anyMatch(x->x.name().startsWith("SADDLEBAGS_"));
            if(saddle)org.junit.jupiter.api.Assertions.assertTrue(load.personalTransport().isPresent(),"Alforjas sin transporte: "+e.getKey());
        }
    }
    private static void differentiatedMaestroNarratives(){
        for(Subprofession s:TeacherCanonicalProfiles.all().keySet())if(TeacherCanonicalProfiles.profiles(s).containsKey(CharacterClass.MAESTRO)){
            String n=TeacherCanonicalProfiles.profile(s,CharacterClass.MAESTRO).narrativeRationale();
            org.junit.jupiter.api.Assertions.assertTrue(n.contains("Maestro hombre")&&n.contains("Maestra mujer"),"Faltan dos biografías diferenciadas en Maestro: "+s);
        }
    }
    private static void noAffinityGain(){
        String source;
        try{source=new String(java.nio.file.Files.readAllBytes(java.nio.file.Path.of("src/main/java/domain/social/TeacherCanonicalProfiles.java")));}catch(Exception ex){throw new IllegalStateException(ex);}
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("affinity(")&&!source.contains("gain")&&!source.contains("express(")," no debe usar affinityGain/express.");
    }
    private static int totalActive(){return TeacherCanonicalProfiles.all().values().stream().mapToInt(Map::size).sum();}
    private static void active(Subprofession s,CharacterClass c){org.junit.jupiter.api.Assertions.assertTrue(TeacherCanonicalProfiles.activeProfiles(s).keySet().equals(Set.of(c)),"Matriz Maestro divergente: "+s+" -> "+TeacherCanonicalProfiles.activeProfiles(s).keySet());}
    
}
