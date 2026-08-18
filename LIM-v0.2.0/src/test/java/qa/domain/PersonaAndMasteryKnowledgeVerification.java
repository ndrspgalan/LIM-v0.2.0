package qa.domain;

import domain.ability.CharacterMasteryCollection;
import domain.ability.MasteryId;
import domain.ability.MasteryKnowledgeState;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.progression.GenderSoftcapProfile;
import domain.character.sheet.Attribute;
import domain.milestone.PersonaMilestone;
import domain.persona.PersonaProfile;
import domain.persona.PersonaRegistry;
import domain.save.SaveSlot;

import java.util.List;

public final class PersonaAndMasteryKnowledgeVerification {
    private PersonaAndMasteryKnowledgeVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifySoftcaps();
        verifyPersonaGuards();
        verifyKnowledgeStates();
    }

    private static void verifySoftcaps() {
        var profile = GenderSoftcapProfile.canonical();
        org.junit.jupiter.api.Assertions.assertTrue(profile.softcaps(Gender.HOMBRE, Attribute.CARISMA).equals(List.of(25, 50)),
                "CARISMA masculina debe usar 25 y hardcap 50.");
        org.junit.jupiter.api.Assertions.assertTrue(profile.softcaps(Gender.MUJER, Attribute.CARISMA).equals(List.of(18, 21, 40)),
                "CARISMA femenina debe usar 25/50.");
        for (Gender gender : Gender.values()) {
            org.junit.jupiter.api.Assertions.assertTrue(profile.softcaps(gender, Attribute.CLARIVIDENCIA).equals(List.of(11, 22, 33, 66, 75)),
                    "CLARIVIDENCIA debe usar 11/22/33/66/75.");
        }
    }

    private static void verifyPersonaGuards() {
        PersonaRegistry registry = new PersonaRegistry(List.of());
        PersonaProfile kenan = persona("kenan", "Kenan", Gender.HOMBRE, CharacterClass.INDOMITO);
        registry.register(kenan);
        org.junit.jupiter.api.Assertions.assertTrue(registry.personas().stream().anyMatch(p -> p.name().equalsIgnoreCase("KENAN")), "Kenan debe estar registrado.");
        org.junit.jupiter.api.Assertions.assertTrue(registry.personas().stream().anyMatch(p -> p.characterClass()==CharacterClass.INDOMITO), "Indómito debe pertenecer a Kenan.");
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> persona("otro", "Otro", Gender.HOMBRE, CharacterClass.INDOMITO),
                "No puede materializarse una segunda identidad jugable: Kenan es la única PERSONA.");
    }

    private static void verifyKnowledgeStates() {
        CharacterMasteryCollection collection = CharacterMasteryCollection.forClass(CharacterClass.INDOMITO);
        org.junit.jupiter.api.Assertions.assertTrue(collection.knowledgeState(MasteryId.PULSION) == MasteryKnowledgeState.REVEALED,
                "Las maestrías afines deben comenzar reveladas.");
        org.junit.jupiter.api.Assertions.assertTrue(collection.knowledgeState(MasteryId.SANAR) == MasteryKnowledgeState.UNKNOWN,
                "Las maestrías no afines deben permanecer desconocidas.");
        org.junit.jupiter.api.Assertions.assertTrue(collection.unlockedIds().isEmpty(), "Revelar no equivale a desbloquear.");
        org.junit.jupiter.api.Assertions.assertTrue(collection.unlock(MasteryId.PULSION), "La transición REVEALED -> UNLOCKED debe funcionar.");
        org.junit.jupiter.api.Assertions.assertTrue(collection.unlockedIds().equals(List.of(MasteryId.PULSION)), "Solo deben seleccionarse maestrías desbloqueadas.");
        org.junit.jupiter.api.Assertions.assertTrue(collection.reveal(MasteryId.SANAR), "Una maestría no afín debe poder revelarse más adelante.");
        org.junit.jupiter.api.Assertions.assertTrue(collection.knowledgeState(MasteryId.SANAR) == MasteryKnowledgeState.REVEALED,
                "La revelación debe conservar únicamente el estado narrativo.");
    }

    private static PersonaProfile persona(String id, String name, Gender gender, CharacterClass characterClass) {
        return new PersonaProfile(id, name, gender, characterClass, 1, List.of(), List.of());
    }

    
}
