package qa.architecture;

import domain.ability.MasteryCatalog;
import domain.ability.MasteryId;
import domain.ability.MasteryStructure;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import presentation.menu.AttributeLevelUpGuidance;

public final class AttributeGuidanceAndInventoryVerification {
    private AttributeGuidanceAndInventoryVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        CharacterSheet veiled = sheetWithFaith(20);
        CharacterSheet awakened = sheetWithFaith(21);
        org.junit.jupiter.api.Assertions.assertTrue(!AttributeLevelUpGuidance.descriptionOf(Attribute.FE, veiled).contains("FE 21"),
                "La versión censurada no debe revelar el umbral universal.");
        org.junit.jupiter.api.Assertions.assertTrue(AttributeLevelUpGuidance.descriptionOf(Attribute.FE, awakened).contains("3, 13, 32, 40 y 60"),
                ": FE debe mostrar sus softcaps técnicos canónicos.");
        org.junit.jupiter.api.Assertions.assertTrue(!AttributeLevelUpGuidance.descriptionOf(Attribute.CLARIVIDENCIA, awakened).contains("TRANSMUTACIÓN"),
                ": la guía de atributos no debe revelar maestrías.");
        org.junit.jupiter.api.Assertions.assertTrue(MasteryCatalog.require(MasteryId.TRANSMUTACION).structure() == MasteryStructure.BRANCHED,
                "TRANSMUTACIÓN debe conservar estructura BRANCHED.");

        org.junit.jupiter.api.Assertions.assertTrue(!java.nio.file.Files.exists(java.nio.file.Path.of("src/main/java/domain/combat/ai/threat/PresenceAssessmentService.java")),
                " debe retirar la estimación heurística de presencia del camino de IA.");
    }

    private static CharacterSheet sheetWithFaith(int faith) {
        CharacterSheet sheet = CharacterSheet.of(1,1,1,1,1,1,1,1,1);
        while (sheet.valueOf(Attribute.FE) < faith) sheet = sheet.increase(Attribute.FE);
        return sheet;
    }

    
}
