package qa.architecture;

import domain.character.Gender;
import presentation.menu.DexterityNarrative;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DynamicDexterityNarrativeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws IOException{
        for (int dexterity = 1; dexterity <= 70; dexterity++) org.junit.jupiter.api.Assertions.assertTrue(!DexterityNarrative.descriptionFor(dexterity, Gender.HOMBRE).isBlank(), "Cobertura masculina.");
        for (int dexterity = 1; dexterity <= 75; dexterity++) org.junit.jupiter.api.Assertions.assertTrue(!DexterityNarrative.descriptionFor(dexterity, Gender.MUJER).isBlank(), "Cobertura femenina.");
        same(Gender.MUJER,1,19); different(Gender.MUJER,19,20); same(Gender.MUJER,20,69); different(Gender.MUJER,69,70); same(Gender.MUJER,70,74); different(Gender.MUJER,74,75);
        org.junit.jupiter.api.Assertions.assertTrue(DexterityNarrative.descriptionFor(20, Gender.MUJER).contains("accidente estadístico"), "Debe conservarse la voz de accidente estadístico.");
        org.junit.jupiter.api.Assertions.assertTrue(DexterityNarrative.descriptionFor(70, Gender.MUJER).contains("Repetid la prueba"), "DESTREZA 70 debe iniciar Error de medición.");
        org.junit.jupiter.api.Assertions.assertTrue(DexterityNarrative.descriptionFor(75, Gender.MUJER).contains("límite humano de la destreza"), "DESTREZA 75 debe cerrar solemnemente.");
        try { DexterityNarrative.descriptionFor(71, Gender.HOMBRE); throw new IllegalStateException("Hombre >70 debe rechazarse."); } catch (IllegalArgumentException ok) {}
        String screen = Files.readString(Path.of("src/main/java/presentation/menu/CharacterSheetScreen.java"));
        org.junit.jupiter.api.Assertions.assertTrue(screen.contains("DexterityNarrative.descriptionFor(dexterity, savedGame.character().identity().gender())"), "DESTREZA debe resolver por sexo.");
    }
    private static void same(Gender g,int a,int b){org.junit.jupiter.api.Assertions.assertTrue(DexterityNarrative.descriptionFor(a,g).equals(DexterityNarrative.descriptionFor(b,g)),"Mismo tramo");}
    private static void different(Gender g,int a,int b){org.junit.jupiter.api.Assertions.assertTrue(!DexterityNarrative.descriptionFor(a,g).equals(DexterityNarrative.descriptionFor(b,g)),"Transición requerida");}
    
}
