package qa.regression;

import domain.character.Gender;
import presentation.menu.EnduranceNarrative;

public final class DynamicEnduranceNarrativeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        same(1,19); diff(19,20); same(20,39); diff(39,40); same(40,74); diff(74,75);
        org.junit.jupiter.api.Assertions.assertTrue(EnduranceNarrative.descriptionFor(1,Gender.HOMBRE).contains("Se llama esfuerzo"),"Debe conservarse el síndrome inicial .");
        org.junit.jupiter.api.Assertions.assertTrue(EnduranceNarrative.descriptionFor(40,Gender.HOMBRE).contains("Solicitud denegada"),"Debe conservarse la voz burocrática avanzada.");
        org.junit.jupiter.api.Assertions.assertTrue(EnduranceNarrative.descriptionFor(75,Gender.HOMBRE).contains("límite humano del aguante"),"AGUANTE 75 debe tener cierre propio.");
    }
    private static void same(int a,int b){org.junit.jupiter.api.Assertions.assertTrue(EnduranceNarrative.descriptionFor(a,Gender.HOMBRE).equals(EnduranceNarrative.descriptionFor(b,Gender.HOMBRE)),"Mismo tramo esperado");}
    private static void diff(int a,int b){org.junit.jupiter.api.Assertions.assertTrue(!EnduranceNarrative.descriptionFor(a,Gender.HOMBRE).equals(EnduranceNarrative.descriptionFor(b,Gender.HOMBRE)),"Transición esperada");}
    
}
