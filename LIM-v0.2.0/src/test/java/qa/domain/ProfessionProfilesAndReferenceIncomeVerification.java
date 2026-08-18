package qa.domain;

import domain.social.*;

import java.util.*;

public final class ProfessionProfilesAndReferenceIncomeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        allNineteenAreProfiled();
        salaryAnchors();
        beggarIsTheResidualZeroIncomeProfession();
        descriptionsStayGlobalRatherThanSubprofessionLore();
        existingInitialRelationshipPolicyRemainsIntact();
    }

    private static void allNineteenAreProfiled(){
        org.junit.jupiter.api.Assertions.assertTrue(Profession.values().length==19,"Deben existir exactamente 19 profesiones canónicas.");
        org.junit.jupiter.api.Assertions.assertTrue(ProfessionProfileCatalog.all().size()==19,"Cada profesión necesita un perfil .");
        for(Profession p:Profession.values()){
            ProfessionProfile profile=p.profile();
            org.junit.jupiter.api.Assertions.assertTrue(profile.profession()==p,"Perfil mal asociado: "+p);
            org.junit.jupiter.api.Assertions.assertTrue(profile.narrativeDescription().length()>180,"Descripción demasiado breve: "+p.label());
            org.junit.jupiter.api.Assertions.assertTrue(!profile.monthlyReferenceLabel().isBlank(),"Ingreso sin etiqueta: "+p.label());
            org.junit.jupiter.api.Assertions.assertTrue(profile.monthlyReferenceValeritas()>=0,"Ingreso negativo: "+p.label());
        }
    }

    private static void salaryAnchors(){
        org.junit.jupiter.api.Assertions.assertTrue(ProfessionProfileCatalog.VALERITAS_PER_SUELDO==1000,"1 Sueldo debe equivaler a 1000 Valeritas.");
        org.junit.jupiter.api.Assertions.assertTrue(ProfessionProfileCatalog.SUELDOS_PER_BERYLARE==210,"1 Berylare debe equivaler a 210 Sueldos.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.SOLDIER.monthlyReferenceValeritas()==1100,"Soldado = 1,10 Sueldos/mes.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.SOLDIER.monthlyReferenceLabel().equals("1,10 Sueldos/mes"),"Etiqueta soldado incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceValeritas()==210000,"Noble = 1 Berylare/mes.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.monthlyReferenceLabel().equals("1 Berylare/mes"),"Etiqueta noble incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.NOBLE.incomeKind()==ProfessionIncomeKind.PATRIMONIAL_RENT,
                "La referencia noble debe ser renta patrimonial, no salario.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.CARPENTER.monthlyReferenceValeritas()==1000,"Carpintero debe representar el Sueldo medio.");
        org.junit.jupiter.api.Assertions.assertTrue(Profession.STONEMASON.monthlyReferenceValeritas()==1000,"Cantero debe representar el Sueldo medio.");
    }

    private static void beggarIsTheResidualZeroIncomeProfession(){
        ProfessionProfile p=Profession.BEGGAR.profile();
        org.junit.jupiter.api.Assertions.assertTrue(p.monthlyReferenceValeritas()==0,"Mendigo = 0.");
        org.junit.jupiter.api.Assertions.assertTrue(p.incomeKind()==ProfessionIncomeKind.NONE,"Mendigo no debe fingir salario.");
        String n=p.narrativeDescription().toLowerCase(Locale.ROOT);
        for(String term:List.of("desempleados","presos","niños","adolescentes","dependientes","incapacitados")){
            org.junit.jupiter.api.Assertions.assertTrue(n.contains(term),"Mendigo debe abarcar: "+term);
        }
        org.junit.jupiter.api.Assertions.assertTrue(Profession.canonicalOrBeggar(null)==Profession.BEGGAR,
                "Ausencia de profesión sigue normalizándose a Mendigo.");
    }

    private static void descriptionsStayGlobalRatherThanSubprofessionLore(){
        String merc=Profession.MERCENARY.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(merc.contains("boom tecnológico") && merc.contains("señores de la guerra"),
                "Mercenario debe contextualizar su revalorización post-Marcha.");
        org.junit.jupiter.api.Assertions.assertTrue(merc.contains("cantería") && merc.contains("carpintería"),
                "Mercenario debe declarar su amplitud profesional.");
        String soldier=Profession.SOLDIER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(soldier.contains("civiles") && soldier.contains("guerra"),
                "Soldado debe abarcar policía/orden y guerra.");
        String teacher=Profession.TEACHER.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(teacher.contains("mensajero") && teacher.contains("profesor"),
                "Maestro debe ser una profesión paraguas.");
        String jurist=Profession.JURIST.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(jurist.contains("jueces") && jurist.contains("políticos")
                        && jurist.contains("contables"),
                "Jurista debe ser una profesión paraguas.");
        String ebony=Profession.EBONY_WARRIOR.narrativeDescription();
        org.junit.jupiter.api.Assertions.assertTrue(ebony.contains("Kenan") && ebony.contains("Primera Marcha Exaltada"),
                "Guerrero de Ébano debe ubicar su situación actual sin duplicar todo el lore.");
    }

    private static void existingInitialRelationshipPolicyRemainsIntact(){
        InitialRelationshipPolicy p=new InitialRelationshipPolicy();
        for(Profession profession:Profession.values()){
            org.junit.jupiter.api.Assertions.assertTrue(p.between(Profession.BEGGAR,profession)==RelationshipType.INDIFFERENT,
                    "Mendigo debe seguir indiferente con "+profession.label());
            org.junit.jupiter.api.Assertions.assertTrue(p.between(profession,profession)==(profession==Profession.BEGGAR
                            ? RelationshipType.INDIFFERENT : RelationshipType.FRIENDLY),
                    "La relación intraprofesional debe conservar el contrato previo.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(p.between(Profession.EBONY_WARRIOR,Profession.MERCENARY)==RelationshipType.RELIABLE,
                "Ébano/Mercenario debe conservar relación fiable.");
        org.junit.jupiter.api.Assertions.assertTrue(p.between(Profession.SOLDIER,Profession.BLACKSMITH)==RelationshipType.RELIABLE,
                "Soldado/Herrero debe conservar cadena productiva.");
        org.junit.jupiter.api.Assertions.assertTrue(p.between(Profession.MERCENARY,Profession.NOBLE)==RelationshipType.DISTRUSTFUL,
                "Mercenario/Noble debe conservar desconfianza.");
    }

    
}
