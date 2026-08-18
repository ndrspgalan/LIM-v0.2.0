package qa.domain;

import domain.ability.*;
import domain.character.sheet.Attribute;

/** Verifica la separación narrativa/mecánica y el canon de Homeostasis térmica. */
public final class MasteryNarrativeVerification {
    private MasteryNarrativeVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        for (Mastery mastery : MasteryCatalog.canonical().values()) {
            org.junit.jupiter.api.Assertions.assertTrue(!mastery.narrativeDescription().isBlank(), mastery.name() + " carece de descripción narrativa.");
        }

        StructuredMastery homeostasis = (StructuredMastery) MasteryCatalog.require(MasteryId.HOMEOSTASIS_TERMICA);
        org.junit.jupiter.api.Assertions.assertTrue(homeostasis.structure() == MasteryStructure.BINARY, "HOMEOSTASIS TÉRMICA debe ser binaria.");
        org.junit.jupiter.api.Assertions.assertTrue(homeostasis.stages().size() == 2, "HOMEOSTASIS TÉRMICA debe contener dos modos.");
        MasteryStage regulation = homeostasis.stages().get(0);
        MasteryStage adaptation = homeostasis.stages().get(1);
        org.junit.jupiter.api.Assertions.assertTrue(regulation.name().equals("REGULACIÓN CALÓRICA SUPERIOR"), "Primer modo térmico incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(adaptation.name().equals("ADAPTACIÓN TÉRMICA"), "Segundo modo térmico incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(regulation.threshold() == 20 && adaptation.threshold() == 20, "Ambos modos deben exigir DESTREZA 20.");
        org.junit.jupiter.api.Assertions.assertTrue(regulation.progressionAttribute() == Attribute.DESTREZA && adaptation.progressionAttribute() == Attribute.DESTREZA, "Atributo térmico incorrecto.");
        org.junit.jupiter.api.Assertions.assertTrue(regulation.natures().contains(MasteryType.PASSIVE), "REGULACIÓN CALÓRICA SUPERIOR debe ser pasiva.");
        org.junit.jupiter.api.Assertions.assertTrue(adaptation.natures().contains(MasteryType.SUSTAINED), "ADAPTACIÓN TÉRMICA debe ser sostenida.");
        org.junit.jupiter.api.Assertions.assertTrue(adaptation.mechanicalDescription().contains("FRÍO ESCARCHANTE"), "ADAPTACIÓN TÉRMICA debe nombrar FRÍO ESCARCHANTE.");

        StructuredMastery healing = (StructuredMastery) MasteryCatalog.require(MasteryId.SANAR);
        org.junit.jupiter.api.Assertions.assertTrue(!healing.narrativeDescription().isBlank(), "SANAR debe conservar narrativa fisiológica propia.");
        org.junit.jupiter.api.Assertions.assertTrue(healing.stages().get(1).name().equals("RESTAURAR") && healing.stages().get(1).mechanicalDescription().contains("0 PV"), "RESTAURAR debe ser heteroaplicada y excluir muertos.");
        org.junit.jupiter.api.Assertions.assertTrue(healing.stages().get(2).name().equals("CUSTODIA") && healing.stages().get(2).narrativeDescriptionOptional().isPresent(), "CUSTODIA debe conservar descripción narrativa propia.");
    }

    
}
