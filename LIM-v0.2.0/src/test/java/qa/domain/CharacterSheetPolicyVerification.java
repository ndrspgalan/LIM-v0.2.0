package qa.domain;

import domain.character.sheet.Attribute;
import presentation.menu.AttributeLevelUpGuidance;
import presentation.menu.CharacterSheetInspectionEntry;

import java.util.List;

public final class CharacterSheetPolicyVerification {
    private CharacterSheetPolicyVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifiesEveryAttributeHasExclusiveLevelUpGuidance();
        verifiesNormalSheetAttributeNarrativesContainNoProgressionPolicy();
    }

    private static void verifiesEveryAttributeHasExclusiveLevelUpGuidance() {
        for (Attribute attribute : Attribute.values()) {
            String guidance = AttributeLevelUpGuidance.descriptionOf(attribute);
            org.junit.jupiter.api.Assertions.assertTrue(guidance != null && !guidance.isBlank(),
                    "Falta política de subida de nivel para " + attribute.label() + ".");
        }
    }

    private static void verifiesNormalSheetAttributeNarrativesContainNoProgressionPolicy() {
        List<CharacterSheetInspectionEntry> entries = CharacterSheetInspectionEntry.canonicalEntries();
        for (Attribute attribute : Attribute.values()) {
            CharacterSheetInspectionEntry entry = entries.stream()
                    .filter(candidate -> candidate.label().equals(attribute.label()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Falta la entrada narrativa de " + attribute.label() + "."));
            org.junit.jupiter.api.Assertions.assertTrue(!entry.hasStaticDescription(),
                    "Los atributos no deben conservar descripciones narrativas estáticas: " + attribute.label());
        }
    }

    
}
