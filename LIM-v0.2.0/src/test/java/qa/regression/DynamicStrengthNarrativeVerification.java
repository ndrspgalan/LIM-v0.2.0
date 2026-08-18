package qa.regression;

import domain.character.Gender;
import presentation.menu.StrengthNarrative;

public final class DynamicStrengthNarrativeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        same(1,24); diff(24,25); same(25,49); diff(49,50); same(50,74); diff(74,75);
        org.junit.jupiter.api.Assertions.assertTrue(StrengthNarrative.descriptionFor(1, Gender.HOMBRE).contains("principalmente burocrática"), "Debe conservarse la sátira inicial .");
        org.junit.jupiter.api.Assertions.assertTrue(StrengthNarrative.descriptionFor(75, Gender.HOMBRE).contains("Aquí termina la fuerza humana"), "FUERZA 75 debe tener cierre propio.");
    }
    private static void same(int a,int b){org.junit.jupiter.api.Assertions.assertTrue(StrengthNarrative.descriptionFor(a,Gender.HOMBRE).equals(StrengthNarrative.descriptionFor(b,Gender.HOMBRE)),"Mismo tramo esperado");}
    private static void diff(int a,int b){org.junit.jupiter.api.Assertions.assertTrue(!StrengthNarrative.descriptionFor(a,Gender.HOMBRE).equals(StrengthNarrative.descriptionFor(b,Gender.HOMBRE)),"Transición esperada");}
    
}
