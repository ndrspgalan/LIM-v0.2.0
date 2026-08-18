package qa.architecture;

import domain.character.Gender;
import presentation.menu.CharismaNarrative;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DynamicCharismaNarrativeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        for (int c=1;c<=50;c++) org.junit.jupiter.api.Assertions.assertTrue(!CharismaNarrative.descriptionFor(c,Gender.HOMBRE).isBlank(),"Cobertura masculina");
        for (int c=1;c<=75;c++) org.junit.jupiter.api.Assertions.assertTrue(!CharismaNarrative.descriptionFor(c,Gender.MUJER).isBlank(),"Cobertura femenina");
        same(Gender.HOMBRE,1,24); different(Gender.HOMBRE,24,25); same(Gender.HOMBRE,25,49); different(Gender.HOMBRE,49,50);
        same(Gender.MUJER,1,17); different(Gender.MUJER,17,18); same(Gender.MUJER,18,20); different(Gender.MUJER,20,21); same(Gender.MUJER,21,39); different(Gender.MUJER,39,40); same(Gender.MUJER,40,74); different(Gender.MUJER,74,75);
        org.junit.jupiter.api.Assertions.assertTrue(CharismaNarrative.descriptionFor(18,Gender.MUJER).toLowerCase().contains("capital erótico"),"Capital erótico desde 18");
        String social=CharismaNarrative.descriptionFor(21,Gender.MUJER).toLowerCase(); org.junit.jupiter.api.Assertions.assertTrue(social.contains("capital erótico")&&social.contains("ahorro social"),"Acumulación desde 21");
        org.junit.jupiter.api.Assertions.assertTrue(CharismaNarrative.descriptionFor(50,Gender.HOMBRE).contains("Papuchón consumado"),"Papuchón en 50");
        org.junit.jupiter.api.Assertions.assertTrue(CharismaNarrative.descriptionFor(75,Gender.MUJER).contains("Mamasita consumada"),"Mamasita en 75");
        try { CharismaNarrative.descriptionFor(51,Gender.HOMBRE); throw new IllegalStateException("Hombre >50 debe rechazarse"); } catch(IllegalArgumentException ok){}
        String screen=Files.readString(Path.of("src/main/java/presentation/menu/CharacterSheetScreen.java"));
        org.junit.jupiter.api.Assertions.assertTrue(screen.contains("CharismaNarrative.descriptionFor(charisma, savedGame.character().identity().gender())"),"La hoja debe resolver CARISMA con género.");
    }
    private static void same(Gender g,int a,int b){org.junit.jupiter.api.Assertions.assertTrue(CharismaNarrative.descriptionFor(a,g).equals(CharismaNarrative.descriptionFor(b,g)),"Mismo tramo");}
    private static void different(Gender g,int a,int b){org.junit.jupiter.api.Assertions.assertTrue(!CharismaNarrative.descriptionFor(a,g).equals(CharismaNarrative.descriptionFor(b,g)),"Transición requerida");}
    
}
