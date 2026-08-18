package qa.domain;

import domain.social.*;

import java.util.*;

public final class PostMarchWorldSubprofessionsVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        catalogShape();
        ebonyStartsAndEndsWithKenan();
        wagesAndParents();
        postMarchOrderAndMercenaryWorld();
        frequencyMedicine();
        electroatmosphericDivergence();
        seaPower();
        professionUmbrellasRemainCanonical();
    }

    private static void catalogShape(){
        org.junit.jupiter.api.Assertions.assertTrue(Subprofession.values().length>=15,"El catálogo debe conservar como mínimo la primera tanda .");
        org.junit.jupiter.api.Assertions.assertTrue(SubprofessionProfileCatalog.all().size()==Subprofession.values().length,
                "Toda subprofesión actualmente canónica debe tener perfil.");
        
        org.junit.jupiter.api.Assertions.assertTrue(Profession.EBONY_WARRIOR.authoredSubprofessions().size()==1,
                "Guerrero de Ébano debe empezar y acabar con una sola subprofesión contemporánea.");
        for(Subprofession s:Subprofession.values()){
            org.junit.jupiter.api.Assertions.assertTrue(s.narrativeDescription().length()>80,"Descripción insuficientemente minuciosa: "+s.label());
            if(s.profession()!=Profession.BEGGAR)
                org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()>0,"Las subprofesiones remuneradas deben tener paga positiva: "+s.label());
        }
    }

    private static void ebonyStartsAndEndsWithKenan(){
        Subprofession s=Subprofession.EBONY_WARRIOR_V881;
        org.junit.jupiter.api.Assertions.assertTrue(s.profession()==Profession.EBONY_WARRIOR,"Parent incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(s.uniqueContemporaryHolder(),"Guerrero de Ébano V881 debe ser único en el presente.");
        org.junit.jupiter.api.Assertions.assertTrue(s.contemporaryHolder().orElseThrow().equals("Kenan"),"El único titular contemporáneo debe ser Kenan.");
        org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==2000,"Kenan = 2 Sueldos/mes.");
        String n=s.narrativeDescription();
        for(String term:List.of("Kenan fue jornalero","Primera Marcha Exaltada","OGC","primer Guerrero de Ébano","Caballero V881")){
            org.junit.jupiter.api.Assertions.assertTrue(n.contains(term),"La descripción de Kenan debe contener: "+term);
        }
    }

    private static void wagesAndParents(){
        check(Subprofession.RECONSTRUCTION_LABORER,Profession.DAY_LABORER,600);
        check(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,Profession.DAY_LABORER,800);
        check(Subprofession.KINGDOM_AGENT,Profession.SOLDIER,1050);
        check(Subprofession.V881_RIFLEMAN,Profession.SOLDIER,1200);
        check(Subprofession.COMPANY_CONTRACTOR,Profession.MERCENARY,1500);
        check(Subprofession.V881_ELECTROMECHANIC,Profession.BLACKSMITH,1500);
        check(Subprofession.FREQUENCY_PHYSICIAN,Profession.TEACHER,1450);
        check(Subprofession.FREQUENCY_RESEARCHER,Profession.TEACHER,1700);
        check(Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER,Profession.TEACHER,1800);
        check(Subprofession.ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER,Profession.TEACHER,1750);
        check(Subprofession.V881_NAVIGATOR,Profession.SAILOR,1250);
        check(Subprofession.NAVAL_RAILGUN_GUNNER,Profession.SAILOR,1450);
        check(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,Profession.SAILOR,1400);
        check(Subprofession.V881_INDUSTRIAL_BROKER,Profession.MERCHANT,1700);
    }

    private static void postMarchOrderAndMercenaryWorld(){
        String agent=Subprofession.KINGDOM_AGENT.narrativeDescription();
        for(String term:List.of("saqueadores","bandas","desertores","contrabando","civiles")){
            org.junit.jupiter.api.Assertions.assertTrue(agent.contains(term),"Agente del Reino debe mostrar el caos post-Marcha: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(agent.contains("El orden ha regresado") && agent.contains("No ha regresado lo suficiente"),
                "Debe quedar claro que la recuperación social sigue incompleta.");

        String merc=Subprofession.COMPANY_CONTRACTOR.narrativeDescription();
        for(String term:List.of("carpintero","cantero","compañías","señores de la guerra","contrato")){
            org.junit.jupiter.api.Assertions.assertTrue(merc.contains(term),"Mercenario debe mostrar amplitud y revalorización: "+term);
        }

        String rifle=Subprofession.V881_RIFLEMAN.narrativeDescription();
        for(String term:List.of("fusiles de repetición","bifilares","cañones antimaterial","armas de racimo")){
            org.junit.jupiter.api.Assertions.assertTrue(rifle.toLowerCase(Locale.ROOT).contains(term.toLowerCase(Locale.ROOT)),
                    "Fusilero debe anclar armamento terrestre V881: "+term);
        }
    }

    private static void frequencyMedicine(){
        String physician=Subprofession.FREQUENCY_PHYSICIAN.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(physician.contains("médicos") && physician.contains("veterinarios")
                        && physician.contains("cirujanos") && physician.contains("investigadores"),
                "Maestro médico debe mostrar el cuerpo sanitario/investigador.");
        org.junit.jupiter.api.Assertions.assertTrue(physician.contains("firma frecuencial"),"Medicina debe usar firmas frecuenciales.");

        String researcher=Subprofession.FREQUENCY_RESEARCHER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(researcher.contains("ADN"),"Debe explicitar la bifurcación respecto a la descodificación del ADN.");
        org.junit.jupiter.api.Assertions.assertTrue(researcher.contains("No todas las frecuencias catalogadas aparecen en manuales civiles"),
                "Debe insinuarse la contraparte bélica sin convertir la ficha en manual operativo.");
    }

    private static void electroatmosphericDivergence(){
        String e=Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(e.contains("captación electroatmosférica"),"Ingeniería electroatmosférica debe conservar su núcleo de captación.");
        org.junit.jupiter.api.Assertions.assertTrue(e.contains("ionización"),"Ingeniería electroatmosférica debe explicitar ionización.");
        org.junit.jupiter.api.Assertions.assertTrue(e.contains("infraestructura"),"Ingeniería electroatmosférica debe seguir siendo una disciplina de infraestructura.");

        String rail=Subprofession.ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(rail!=null && !rail.isBlank(),"Locomoción electromagnética debe conservar descripción canónica.");
    }

    private static void seaPower(){
        String nav=Subprofession.V881_NAVIGATOR.narrativeDescription();
        for(String term:List.of("energía del viento","velas solares","electricidad atmosférica","motores")){
            org.junit.jupiter.api.Assertions.assertTrue(nav.contains(term),"Navegante debe integrar las tres ramas energéticas: "+term);
        }

        String machinist=Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER.narrativeDescription();
        for(String term:List.of("energía captada del viento","captación solar","campo eléctrico atmosférico","motores")){
            org.junit.jupiter.api.Assertions.assertTrue(machinist.contains(term),"Maquinista naval debe integrar captación y propulsión: "+term);
        }

        String gunner=Subprofession.NAVAL_RAILGUN_GUNNER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(gunner.contains("cañones de riel") && gunner.contains("defensas costeras"),
                "El dominio pesado marítimo/costero debe recaer en cañones de riel.");
    }

    private static void professionUmbrellasRemainCanonical(){
        String teacher=Profession.TEACHER.narrativeDescription();
        for(String term:List.of("médicos","cirujanos","veterinarios","investigadores","mensajeros")){
            org.junit.jupiter.api.Assertions.assertTrue(teacher.contains(term),"Maestro madre debe abarcar: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(Profession.TEACHER.monthlyReferenceValeritas()==1200,
                " no debe alterar el salario madre de Maestro.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.SOLDIER.monthlyReferenceValeritas()==1100,
                " no debe alterar el salario madre de Soldado.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceValeritas()==210000,
                " no debe tocar Noble.");
    }

    private static void check(Subprofession s,Profession p,int valeritas){
        org.junit.jupiter.api.Assertions.assertTrue(s.profession()==p,s.label()+" debe pertenecer a "+p.label());
        org.junit.jupiter.api.Assertions.assertTrue(s.monthlyReferenceValeritas()==valeritas,s.label()+" paga incorrecta.");
    }

    
}
