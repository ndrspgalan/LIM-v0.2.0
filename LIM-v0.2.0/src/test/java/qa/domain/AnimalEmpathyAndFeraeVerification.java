package qa.domain;

import domain.ability.AnimalEmpathyContext;
import domain.ability.AnimalEmpathyPolicy;
import domain.ability.MasteryKnowledgeState;
import domain.bestiarium.physical_plane.ferae.*;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.persona.PersonaProfile;
import domain.social.RelationshipType;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class AnimalEmpathyAndFeraeVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(FeraeCatalog.branch(FeraeBranch.CARISMA).size() == 21, "Deben existir 21 Ferae de Carisma tras formalizar las tres yeguas.");
        org.junit.jupiter.api.Assertions.assertTrue(FeraeCatalog.branch(FeraeBranch.INTELIGENCIA).size() == 17, "Deben existir 17 Ferae de Inteligencia tras sustituir Caballo genérico por tres variedades.");
        org.junit.jupiter.api.Assertions.assertTrue(FeraeSpecies.RATON.empathyAttributeRequirement() == 12 && FeraeSpecies.ELEFANTE.empathyAttributeRequirement() == 25,
                "El ranking de Carisma debe abarcar 12–25.");
        org.junit.jupiter.api.Assertions.assertTrue(FeraeSpecies.RATA.empathyAttributeRequirement() == 12 && FeraeSpecies.RINOCERONTE.empathyAttributeRequirement() == 30,
                "El ranking de Inteligencia debe abarcar 12–30.");

        AnimalEmpathyContext nonAffineUnknown = new AnimalEmpathyContext(false, MasteryKnowledgeState.UNKNOWN, 25, 30, EnumSet.allOf(HuntingTrophy.class), true);
        org.junit.jupiter.api.Assertions.assertTrue(AnimalEmpathyPolicy.relationship(FeraeSpecies.PERRO, nonAffineUnknown) == RelationshipType.INDIFFERENT,
                "Carisma no afín debe empezar Indiferente.");
        AnimalEmpathyContext nonAffineRevealed = new AnimalEmpathyContext(false, MasteryKnowledgeState.REVEALED, 15, 30, EnumSet.allOf(HuntingTrophy.class), true);
        org.junit.jupiter.api.Assertions.assertTrue(AnimalEmpathyPolicy.relationship(FeraeSpecies.PERRO, nonAffineRevealed) == RelationshipType.FRIENDLY,
                "Al revelarse EMPATÍA ANIMAL, Carisma pasa a Amistosa.");
        org.junit.jupiter.api.Assertions.assertTrue(AnimalEmpathyPolicy.companionEligibility(FeraeSpecies.PERRO, nonAffineRevealed).eligible(),
                "El perro debe poder vincularse en Carisma 15.");

        AnimalEmpathyContext intelligenceReadyNoMilestone = new AnimalEmpathyContext(true, MasteryKnowledgeState.REVEALED, 1, 30,
                EnumSet.allOf(HuntingTrophy.class), false);
        org.junit.jupiter.api.Assertions.assertTrue(AnimalEmpathyPolicy.relationship(FeraeSpecies.CIERVO, intelligenceReadyNoMilestone) == RelationshipType.RELIABLE,
                "El ciervo desconfiado debe evolucionar a Fiable.");
        org.junit.jupiter.api.Assertions.assertTrue(AnimalEmpathyPolicy.companionEligibility(FeraeSpecies.CIERVO, intelligenceReadyNoMilestone).eligible(),
                "Una especie inicialmente desconfiada no requiere el hito.");
        org.junit.jupiter.api.Assertions.assertTrue(AnimalEmpathyPolicy.relationship(FeraeSpecies.LOBO, intelligenceReadyNoMilestone) == RelationshipType.RELIABLE,
                "El lobo hostil también debe evolucionar a Fiable.");
        org.junit.jupiter.api.Assertions.assertTrue(!AnimalEmpathyPolicy.companionEligibility(FeraeSpecies.LOBO, intelligenceReadyNoMilestone).eligible(),
                "El lobo requiere [CAZADOR DE CAZADORES] para el vínculo.");

        PersonaProfile persona = new PersonaProfile("m326", "Kenan", Gender.HOMBRE, CharacterClass.INDOMITO, 198, List.of(), List.of());
        TravelCompanionState companions = new TravelCompanionState();
        companions.bond(FeraeSpecies.PERRO, persona, nonAffineRevealed);
        companions.bond(FeraeSpecies.CIERVO, persona, intelligenceReadyNoMilestone);
        org.junit.jupiter.api.Assertions.assertTrue(companions.charismaCompanion().orElseThrow() == FeraeSpecies.PERRO, "Debe conservar un compañero de Carisma.");
        org.junit.jupiter.api.Assertions.assertTrue(companions.intelligenceCompanion().orElseThrow() == FeraeSpecies.CIERVO, "Debe conservar un compañero de Inteligencia.");
        companions.bond(FeraeSpecies.GATO, persona, new AnimalEmpathyContext(true, MasteryKnowledgeState.REVEALED, 15, 1, Set.of(), false));
        org.junit.jupiter.api.Assertions.assertTrue(companions.charismaCompanion().orElseThrow() == FeraeSpecies.GATO, "El nuevo compañero solo sustituye al de su rama.");
        org.junit.jupiter.api.Assertions.assertTrue(companions.intelligenceCompanion().orElseThrow() == FeraeSpecies.CIERVO, "La otra rama debe permanecer intacta.");

        int expected = 1;
        for (HuntingTrophy trophy : HuntingTrophy.values()) org.junit.jupiter.api.Assertions.assertTrue(trophy.charismaBonus() == expected++, "Bonificación de trofeo fuera de orden.");
    }

    
}
