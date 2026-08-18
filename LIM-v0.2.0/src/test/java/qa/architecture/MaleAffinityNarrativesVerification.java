package qa.architecture;

import domain.character.Gender;
import presentation.menu.EnduranceNarrative;
import presentation.menu.IntelligenceNarrative;
import presentation.menu.StrengthNarrative;

import java.nio.file.Files;
import java.nio.file.Path;

/** Contratos narrativos : FUERZA, AGUANTE e INTELIGENCIA. */
public final class MaleAffinityNarrativesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        strengthMaleSoftcaps();
        enduranceMaleSoftcaps();
        intelligenceMaleSoftcaps();
        femaleOrdinaryCapsRemainSafe();
        approvedVoicesArePresent();
        noDirectMasteryNames();
        sheetUsesGenderAwareNarratives();
    }

    private static void strengthMaleSoftcaps() {
        same(StrengthNarrative.descriptionFor(1, Gender.HOMBRE), StrengthNarrative.descriptionFor(24, Gender.HOMBRE));
        different(StrengthNarrative.descriptionFor(24, Gender.HOMBRE), StrengthNarrative.descriptionFor(25, Gender.HOMBRE));
        same(StrengthNarrative.descriptionFor(25, Gender.HOMBRE), StrengthNarrative.descriptionFor(49, Gender.HOMBRE));
        different(StrengthNarrative.descriptionFor(49, Gender.HOMBRE), StrengthNarrative.descriptionFor(50, Gender.HOMBRE));
        same(StrengthNarrative.descriptionFor(50, Gender.HOMBRE), StrengthNarrative.descriptionFor(74, Gender.HOMBRE));
        different(StrengthNarrative.descriptionFor(74, Gender.HOMBRE), StrengthNarrative.descriptionFor(75, Gender.HOMBRE));
    }

    private static void enduranceMaleSoftcaps() {
        same(EnduranceNarrative.descriptionFor(1, Gender.HOMBRE), EnduranceNarrative.descriptionFor(19, Gender.HOMBRE));
        different(EnduranceNarrative.descriptionFor(19, Gender.HOMBRE), EnduranceNarrative.descriptionFor(20, Gender.HOMBRE));
        same(EnduranceNarrative.descriptionFor(20, Gender.HOMBRE), EnduranceNarrative.descriptionFor(39, Gender.HOMBRE));
        different(EnduranceNarrative.descriptionFor(39, Gender.HOMBRE), EnduranceNarrative.descriptionFor(40, Gender.HOMBRE));
        same(EnduranceNarrative.descriptionFor(40, Gender.HOMBRE), EnduranceNarrative.descriptionFor(74, Gender.HOMBRE));
        different(EnduranceNarrative.descriptionFor(74, Gender.HOMBRE), EnduranceNarrative.descriptionFor(75, Gender.HOMBRE));
    }

    private static void intelligenceMaleSoftcaps() {
        same(IntelligenceNarrative.descriptionFor(1, Gender.HOMBRE), IntelligenceNarrative.descriptionFor(29, Gender.HOMBRE));
        different(IntelligenceNarrative.descriptionFor(29, Gender.HOMBRE), IntelligenceNarrative.descriptionFor(30, Gender.HOMBRE));
        same(IntelligenceNarrative.descriptionFor(30, Gender.HOMBRE), IntelligenceNarrative.descriptionFor(69, Gender.HOMBRE));
        different(IntelligenceNarrative.descriptionFor(69, Gender.HOMBRE), IntelligenceNarrative.descriptionFor(70, Gender.HOMBRE));
        same(IntelligenceNarrative.descriptionFor(70, Gender.HOMBRE), IntelligenceNarrative.descriptionFor(74, Gender.HOMBRE));
        different(IntelligenceNarrative.descriptionFor(74, Gender.HOMBRE), IntelligenceNarrative.descriptionFor(75, Gender.HOMBRE));
    }

    private static void femaleOrdinaryCapsRemainSafe() {
        org.junit.jupiter.api.Assertions.assertTrue(!StrengthNarrative.descriptionFor(30, Gender.MUJER).isBlank(), "FUERZA femenina 30 debe ser narrable.");
        org.junit.jupiter.api.Assertions.assertTrue(!EnduranceNarrative.descriptionFor(30, Gender.MUJER).isBlank(), "AGUANTE femenino 30 debe ser narrable.");
        org.junit.jupiter.api.Assertions.assertTrue(!IntelligenceNarrative.descriptionFor(70, Gender.MUJER).isBlank(), "INTELIGENCIA femenina 70 debe ser narrable.");
        failure(() -> StrengthNarrative.descriptionFor(31, Gender.MUJER));
        failure(() -> EnduranceNarrative.descriptionFor(31, Gender.MUJER));
        failure(() -> IntelligenceNarrative.descriptionFor(71, Gender.MUJER));
    }

    private static void approvedVoicesArePresent() {
        org.junit.jupiter.api.Assertions.assertTrue(StrengthNarrative.descriptionFor(1, Gender.HOMBRE).contains("principalmente burocrática"), "Falta voz inicial de FUERZA.");
        org.junit.jupiter.api.Assertions.assertTrue(StrengthNarrative.descriptionFor(75, Gender.HOMBRE).contains("¿por qué ibas a hacerlo?"), "Falta cierre de FUERZA 75.");
        org.junit.jupiter.api.Assertions.assertTrue(EnduranceNarrative.descriptionFor(1, Gender.HOMBRE).contains("Se llama esfuerzo"), "Falta síndrome inicial de AGUANTE.");
        org.junit.jupiter.api.Assertions.assertTrue(EnduranceNarrative.descriptionFor(40, Gender.HOMBRE).contains("Solicitud denegada"), "Falta moción de censura de AGUANTE.");
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceNarrative.descriptionFor(70, Gender.HOMBRE).contains("estadísticamente molesto"), "Falta Accidente estadístico.");
        org.junit.jupiter.api.Assertions.assertTrue(IntelligenceNarrative.descriptionFor(75, Gender.HOMBRE).contains("pensar más no obliga al mundo a darte la razón"), "Falta cierre de INTELIGENCIA 75.");
    }

    private static void noDirectMasteryNames() {
        String all = StrengthNarrative.descriptionFor(75, Gender.HOMBRE) + EnduranceNarrative.descriptionFor(75, Gender.HOMBRE)
                + IntelligenceNarrative.descriptionFor(75, Gender.HOMBRE);
        String[] forbidden = {"PULSIÓN", "ANULACIÓN", "EXPLOSIÓN CINÉTICA", "ENDURECIMIENTO", "INCITAR", "EMPATÍA ANIMAL", "TRANSMUTACIÓN"};
        for (String name : forbidden) org.junit.jupiter.api.Assertions.assertTrue(!all.contains(name), "Las narrativas no deben nombrar maestrías: " + name);
    }

    private static void sheetUsesGenderAwareNarratives() throws Exception {
        String source = Files.readString(Path.of("src/main/java/presentation/menu/CharacterSheetScreen.java"));
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("StrengthNarrative.descriptionFor(strength, savedGame.character().identity().gender())"), "FUERZA debe resolver por sexo.");
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("EnduranceNarrative.descriptionFor(endurance, savedGame.character().identity().gender())"), "AGUANTE debe resolver por sexo.");
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("IntelligenceNarrative.descriptionFor(intelligence, savedGame.character().identity().gender())"), "INTELIGENCIA debe resolver por sexo.");
    }

    private static void same(String a, String b) { org.junit.jupiter.api.Assertions.assertTrue(a.equals(b), "Se esperaba el mismo tramo narrativo."); }
    private static void different(String a, String b) { org.junit.jupiter.api.Assertions.assertTrue(!a.equals(b), "Se esperaba transición narrativa."); }
    private static void failure(Runnable r) { try { r.run(); throw new IllegalStateException("Se esperaba rechazo."); } catch (IllegalArgumentException expected) {} }
    
}
