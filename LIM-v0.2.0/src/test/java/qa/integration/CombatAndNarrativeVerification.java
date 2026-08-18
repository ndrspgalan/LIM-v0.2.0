package qa.integration;

import domain.character.sheet.CharacterSheet;
import domain.combat.*;
import domain.inventory.item.*;
import presentation.menu.AttributeLevelUpGuidance;

public final class CombatAndNarrativeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        CombatTechniqueUnlockPolicy unlock = new CombatTechniqueUnlockPolicy();
        CharacterSheet male = CharacterSheet.of(1,1,1,30,35,30,1,1,1);
        org.junit.jupiter.api.Assertions.assertTrue(unlock.isUnlocked(CombatTechnique.FEINT, male), "Finta no desbloqueada.");
        org.junit.jupiter.api.Assertions.assertTrue(unlock.isUnlocked(CombatTechnique.DEFLECTION, male), "Desvío no desbloqueado.");
        org.junit.jupiter.api.Assertions.assertTrue(unlock.isUnlocked(CombatTechnique.STAGGERING_STRIKE, male), "Golpe no desbloqueado.");
        StaggerResult max = StaggerPolicy.resolve(50);
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(max.staggerDurationSeconds()-2.0)<0.0001, "Aturdimiento máximo incorrecto.");
        WeaponItem unarmed = UnarmedWeaponFactory.create();
        org.junit.jupiter.api.Assertions.assertTrue(unarmed.hasTrait(WeaponTrait.UNARMED), "DESARMADO no está tipado.");
        org.junit.jupiter.api.Assertions.assertTrue(unarmed.isExclusivelyTwoHanded(), "DESARMADO debe ser bimanual.");
        org.junit.jupiter.api.Assertions.assertTrue(AttributeLevelUpGuidance.descriptionOf(domain.character.sheet.Attribute.FUERZA).contains("GOLPE DESESTABILIZADOR"), "Política de Fuerza desactualizada.");
    }
    
}
