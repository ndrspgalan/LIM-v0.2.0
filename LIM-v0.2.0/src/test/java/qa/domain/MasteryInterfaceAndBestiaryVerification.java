package qa.domain;

import domain.ability.*;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.control.*;

public final class MasteryInterfaceAndBestiaryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        var pc = PcControlScheme.canonicalBindings();
        org.junit.jupiter.api.Assertions.assertTrue(pc.stream().anyMatch(b -> b.input().equals("Z") && b.gesture() == InputGesture.PRESS && b.action() == ControlAction.EXECUTE_ACTIVE_MASTERY), "Z activa");
        org.junit.jupiter.api.Assertions.assertTrue(pc.stream().anyMatch(b -> b.input().equals("Z") && b.gesture() == InputGesture.HOLD && b.action() == ControlAction.OPEN_ACTIVE_MASTERY_WHEEL), "rueda Z");
        org.junit.jupiter.api.Assertions.assertTrue(pc.stream().anyMatch(b -> b.input().equals("X") && b.gesture() == InputGesture.PRESS && b.action() == ControlAction.TOGGLE_SUSTAINED_MASTERY), "X sostenida");
        org.junit.jupiter.api.Assertions.assertTrue(pc.stream().noneMatch(b -> b.action().name().contains("NEXT_MASTERY") || b.action().name().contains("PREVIOUS_MASTERY")), "sin rueda secuencial");

        StructuredMastery trajectory = (StructuredMastery) MasteryCatalog.require(MasteryId.TRAYECTORIA_CONVERGENTE);
        MasteryStage stage = trajectory.stages().get(0);
        org.junit.jupiter.api.Assertions.assertTrue(stage.natures().contains(MasteryType.PASSIVE), "Trayectoria Convergente pasiva");
        org.junit.jupiter.api.Assertions.assertTrue(stage.natures().contains(MasteryType.PASSIVE), "Trayectoria no consume PA por sí misma; modifica el remate de combo.");

        var npcs = domain.bestiarium.physical_plane.npc.PhysicalNpcCatalog.canonical();
        org.junit.jupiter.api.Assertions.assertTrue(npcs.size() == 7, "siete NPC nominales canónicos aparte de Kenan");
        org.junit.jupiter.api.Assertions.assertTrue(domain.ability.CharacterMasteryCollection.allCanonical().ownedIds().containsAll(java.util.List.of(MasteryId.values())), "Doppelgänger universal");
        var kenanMasteries = domain.ability.CharacterMasteryCollection.kenanCanonical();

        CharacterSheet below = CharacterSheet.of(1,1,1,1,1,1,20,1,1);
        CharacterSheet threshold = CharacterSheet.of(1,1,1,1,1,1,21,1,1);
        org.junit.jupiter.api.Assertions.assertTrue(domain.knowledge.PropertyKnowledgePolicy.requirementMet(below, domain.character.sheet.Attribute.FE, 1), "No existe ya un umbral universal FE 21.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.knowledge.PropertyKnowledgePolicy.requirementMet(threshold, domain.character.sheet.Attribute.FE, 1), "La visibilidad depende del requisito propio, no de FE 21.");
        org.junit.jupiter.api.Assertions.assertTrue(!kenanMasteries.ownedIds().contains(MasteryId.TRAYECTORIA_CONVERGENTE), "Kenan no conoce maestrías de Especialista");
        org.junit.jupiter.api.Assertions.assertTrue(kenanMasteries.selectableManifestations(MasteryType.ACTIVE, threshold).stream()
                .allMatch(m -> kenanMasteries.ownedIds().contains(m.familyId())), "Z solo ofrece activas poseídas");
        org.junit.jupiter.api.Assertions.assertTrue(kenanMasteries.selectableManifestations(MasteryType.SUSTAINED, threshold).stream()
                .allMatch(m -> kenanMasteries.ownedIds().contains(m.familyId())), "X solo ofrece sostenidas poseídas");
        org.junit.jupiter.api.Assertions.assertTrue(kenanMasteries.selectableManifestations(MasteryType.SUSTAINED, threshold).stream()
                .noneMatch(m -> m.familyId() == MasteryId.TRAYECTORIA_CONVERGENTE), "Trayectoria pasiva no aparece en X");
        org.junit.jupiter.api.Assertions.assertTrue(kenanMasteries.knownMasteries(threshold).stream()
                .allMatch(m -> kenanMasteries.ownedIds().contains(m.id())), "Colección visual limitada al conocimiento propio");
        org.junit.jupiter.api.Assertions.assertTrue(!kenanMasteries.knownMasteries(below).isEmpty(), "La colección no debe exigir FE 21");
    }

    
}
