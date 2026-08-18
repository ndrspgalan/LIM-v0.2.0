package qa.regression;
import domain.character.Gender;
import presentation.menu.AdaptabilityNarrative;
public final class DynamicAdaptabilityNarrativeVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){for(int a=1;a<=120;a++){org.junit.jupiter.api.Assertions.assertTrue(!AdaptabilityNarrative.descriptionFor(a,Gender.HOMBRE).isBlank(),"Hombre");org.junit.jupiter.api.Assertions.assertTrue(!AdaptabilityNarrative.descriptionFor(a,Gender.MUJER).isBlank(),"Mujer");} org.junit.jupiter.api.Assertions.assertTrue(!AdaptabilityNarrative.descriptionFor(12,Gender.HOMBRE).equals(AdaptabilityNarrative.descriptionFor(12,Gender.MUJER)),"Segregación sexual"); org.junit.jupiter.api.Assertions.assertTrue(AdaptabilityNarrative.descriptionFor(75,Gender.MUJER).trim().equals("just SiO2 maxxing dude"),"75"); org.junit.jupiter.api.Assertions.assertTrue(AdaptabilityNarrative.descriptionFor(76,Gender.HOMBRE).contains("CONFIGURATIO ORIGINALIS"),"76-120");}
 
}
