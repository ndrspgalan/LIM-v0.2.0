package qa.domain;

import domain.ability.*;
import domain.character.CharacterClass;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.status.VitalResourceState;
import presentation.menu.AttributeLevelUpGuidance;
import presentation.menu.FaithNarrative;

public final class DrainAndFaithVerification {
    private DrainAndFaithVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        CharacterSheet faith1 = CharacterSheet.of(10,10,10,10,10,10,1,10,10);
        org.junit.jupiter.api.Assertions.assertTrue(domain.knowledge.PropertyKnowledgePolicy.requirementMet(faith1, domain.character.sheet.Attribute.FE, 1), "El conocimiento oculto no debe exigir FE 21.");
        org.junit.jupiter.api.Assertions.assertTrue(AttributeLevelUpGuidance.descriptionOf(Attribute.FE, faith1).contains("3, 13, 32, 40 y 60"), ": la guía debe reflejar los softcaps actuales de FE.");
        org.junit.jupiter.api.Assertions.assertTrue(!FaithNarrative.descriptionFor(13, domain.character.Gender.MUJER).equals(FaithNarrative.descriptionFor(40, domain.character.Gender.MUJER)), ": FE debe cambiar en los softcaps 32 y 40.");

        StructuredMastery sanar = (StructuredMastery) MasteryCatalog.require(MasteryId.SANAR);
        org.junit.jupiter.api.Assertions.assertTrue(sanar.structure() == MasteryStructure.TRIAD, "SANAR debe ser una tríada.");
        org.junit.jupiter.api.Assertions.assertTrue(sanar.resonanceClass() == CharacterClass.APODERADO, "La afinidad debe ser APODERADO.");
        org.junit.jupiter.api.Assertions.assertTrue(sanar.stages().size() == 3, "SANAR debe contener tres manifestaciones.");
        MasteryStage drain = sanar.stages().get(0);
        org.junit.jupiter.api.Assertions.assertTrue(drain.name().equals("DRENAR") && drain.natures().contains(MasteryType.PASSIVE), "DRENAR debe ser pasiva.");
        org.junit.jupiter.api.Assertions.assertTrue(drain.progressionAttribute() == Attribute.FE && drain.threshold() == 32, "DRENAR debe requerir FE 32.");
        org.junit.jupiter.api.Assertions.assertTrue(drain.narrativeDescription().contains("glucogenólisis") && drain.narrativeDescription().contains("oxidativas") && drain.narrativeDescription().contains("contaminada"), "La narrativa de DRENAR debe explicar la descarga simpática, la movilización de glucosa y la degradación cualitativa de la sangre inhibida.");

        VitalResourceState fullPotential = new VitalResourceState(100, 500, 50, 50);
        var full = DrainPolicy.onCharacterDeath(fullPotential, 20, 200, false, true);
        org.junit.jupiter.api.Assertions.assertTrue(full.restoredHealth() == 200 && fullPotential.currentHealth() == 300, "Con PV REGEN operativo deben restaurarse los PV TOTALES del enemigo.");

        VitalResourceState inhibited = new VitalResourceState(100, 500, 50, 50);
        var reduced = DrainPolicy.onCharacterDeath(inhibited, 20, 200, true, true);
        org.junit.jupiter.api.Assertions.assertTrue(reduced.restoredHealth() == 20 && inhibited.currentHealth() == 120 && reduced.reducedToRawVitality(), "Con PV REGEN inhibido solo debe restaurarse VITALIDAD.");

        VitalResourceState locked = new VitalResourceState(100, 500, 50, 50);
        var none = DrainPolicy.onCharacterDeath(locked, 20, 200, false, DrainPolicy.accessibleAtFaith(31));
        org.junit.jupiter.api.Assertions.assertTrue(none.restoredHealth() == 0 && locked.currentHealth() == 100, "DRENAR no debe operar antes de FE 32.");
    }

    
}
