package qa.regression;

import domain.character.Gender;
import presentation.menu.IntelligenceNarrative;

public final class DynamicIntelligenceNarrativeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        same(1,29); diff(29,30); same(30,69); diff(69,70); same(70,74); diff(74,75);
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceNarrative.descriptionFor(1,Gender.HOMBRE).contains("No sería profesional afirmar mucho más"),"Debe conservarse la sátira inicial .");
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceNarrative.descriptionFor(70,Gender.HOMBRE).contains("estadísticamente molesto"),"Debe existir Accidente estadístico.");
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceNarrative.descriptionFor(75,Gender.HOMBRE).contains("pensar más no obliga al mundo a darte la razón"),"INTELIGENCIA 75 debe tener cierre propio.");
    }
    private static void same(int a,int b){org.junit.jupiter.api.Assertions.assertTrue(IntelligenceNarrative.descriptionFor(a,Gender.HOMBRE).equals(IntelligenceNarrative.descriptionFor(b,Gender.HOMBRE)),"Mismo tramo esperado");}
    private static void diff(int a,int b){org.junit.jupiter.api.Assertions.assertTrue(!IntelligenceNarrative.descriptionFor(a,Gender.HOMBRE).equals(IntelligenceNarrative.descriptionFor(b,Gender.HOMBRE)),"Transición esperada");}
    
}
