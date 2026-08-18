package qa.architecture;

import domain.ability.NullificationPolicy;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import presentation.menu.CharacterSheetInspectionEntry;
import presentation.menu.ClairvoyanceNarrative;
import presentation.menu.FaithNarrative;

import java.nio.file.Files;
import java.nio.file.Path;

public final class FaithClairvoyanceAndNullificationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract() throws Exception{
        verifyFaithIntervals(); verifyClairvoyanceIntervals(); verifyNoStaticAttributeDescriptions(); verifyInspectionIntegration(); verifyNullificationUsesEndurance();
    }
    private static void verifyFaithIntervals() {
        same(f(1),f(2)); different(f(2),f(3)); same(f(3),f(12)); different(f(12),f(13)); same(f(13),f(31)); different(f(31),f(32)); same(f(32),f(39)); different(f(39),f(40)); same(f(40),f(59)); different(f(59),f(60)); same(f(60),f(74)); different(f(74),f(75));
        invalid(() -> FaithNarrative.descriptionFor(0,Gender.MUJER)); invalid(() -> FaithNarrative.descriptionFor(76,Gender.MUJER)); invalid(() -> FaithNarrative.descriptionFor(61,Gender.HOMBRE));
    }
    private static String f(int v){return FaithNarrative.descriptionFor(v,Gender.MUJER);}
    private static void verifyClairvoyanceIntervals() {
        same(ClairvoyanceNarrative.descriptionFor(1), ClairvoyanceNarrative.descriptionFor(75)); invalid(() -> ClairvoyanceNarrative.descriptionFor(0)); invalid(() -> ClairvoyanceNarrative.descriptionFor(76));
    }
    private static void verifyNoStaticAttributeDescriptions() { for (Attribute attribute : Attribute.values()) { var entry=CharacterSheetInspectionEntry.canonicalEntries().stream().filter(c->c.label().equals(attribute.label())).findFirst().orElseThrow(); org.junit.jupiter.api.Assertions.assertTrue(!entry.hasStaticDescription(),"Descripción estática residual: "+attribute.label()); } }
    private static void verifyInspectionIntegration() throws Exception { String screen=Files.readString(Path.of("src/main/java/presentation/menu/CharacterSheetScreen.java")); org.junit.jupiter.api.Assertions.assertTrue(screen.contains("FaithNarrative.descriptionFor(faith, savedGame.character().identity().gender())"),"FE no resuelve por sexo."); org.junit.jupiter.api.Assertions.assertTrue(screen.contains("ClairvoyanceNarrative.descriptionFor"),"CLARIVIDENCIA no está integrada."); }
    private static void verifyNullificationUsesEndurance() throws Exception { org.junit.jupiter.api.Assertions.assertTrue(Double.isInfinite(NullificationPolicy.suppressionSeconds(6)),"ANULACIÓN persiste hasta fin del encuentro."); org.junit.jupiter.api.Assertions.assertTrue(NullificationPolicy.eligible(domain.social.RelationshipType.HOSTILE,7,6),"Debe exigir HOSTIL y AGUANTE superior."); org.junit.jupiter.api.Assertions.assertTrue(!NullificationPolicy.eligible(domain.social.RelationshipType.FRIENDLY,7,6),"No actúa fuera de HOSTIL."); String policy=Files.readString(Path.of("src/main/java/domain/ability/NullificationPolicy.java")); org.junit.jupiter.api.Assertions.assertTrue(!policy.contains("CLARIVIDENCIA"),"NullificationPolicy conserva CLARIVIDENCIA."); }
    private static void same(String a,String b){org.junit.jupiter.api.Assertions.assertTrue(a.equals(b),"El intervalo narrativo se ha fragmentado.");} private static void different(String a,String b){org.junit.jupiter.api.Assertions.assertTrue(!a.equals(b),"Falta una frontera narrativa.");} private static void invalid(Runnable a){try{a.run();throw new AssertionError("Debía rechazarse el valor fuera de rango.");}catch(IllegalArgumentException expected){}} 
}
