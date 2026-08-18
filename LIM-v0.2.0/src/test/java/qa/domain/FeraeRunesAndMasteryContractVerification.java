package qa.domain;

import domain.ability.*;
import domain.bestiarium.physical_plane.ferae.FeraeCatalog;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import domain.combat.HostileEncounterState;
import domain.inventory.equipment.EquipmentState;
import domain.persona.PersonaProfile;
import domain.runic.RunicMarkId;
import domain.save.SaveSlot;
import domain.status.VitalResourceState;

import java.util.List;

public final class FeraeRunesAndMasteryContractVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyAnimalEmpathyCatalog();
        verifyRunicSelectionDrivesEffects();
        verifyCommonMasteryContract();
    }

    private static void verifyAnimalEmpathyCatalog() {
        StructuredMastery empathy = (StructuredMastery) MasteryCatalog.require(MasteryId.EMPATIA_ANIMAL);
        org.junit.jupiter.api.Assertions.assertTrue(empathy.stages().size() == FeraeCatalog.all().size(),
                "EMPATÍA ANIMAL debe derivar todas sus etapas de FeraeCatalog.");
        FeraeCatalog.all().forEach(species -> org.junit.jupiter.api.Assertions.assertTrue(
                empathy.stages().stream().anyMatch(stage -> stage.name().equalsIgnoreCase(species.label())
                        && stage.threshold() == species.empathyAttributeRequirement()),
                "Falta sincronizar " + species.label() + " en EMPATÍA ANIMAL."));
    }

    private static void verifyRunicSelectionDrivesEffects() {
        CharacterSheet sheet = CharacterSheet.of(27, 40, 12, 30, 20, 30, 60, 50, 75);
        PersonaProfile persona = new PersonaProfile("kenan", "Kenan", Gender.HOMBRE,
                CharacterClass.INDOMITO, 198, List.<SaveSlot>of(), List.of());
        persona.unlockAllRunicMarks();
        persona.equipRunicMark(RunicMarkId.SILENCIO);
        EquipmentState equipment = EquipmentState.empty();
        equipment.synchronizeRunicSelection(persona);
        org.junit.jupiter.api.Assertions.assertTrue(equipment.hasAwakenedRunicMark(RunicMarkId.SILENCIO, sheet),
                "La marca seleccionada en la Hoja debe alimentar todos los efectos rúnicos existentes.");
        org.junit.jupiter.api.Assertions.assertTrue(!equipment.hasAwakenedRunicMark(RunicMarkId.RESONANCIA, sheet),
                "Solo una marca puede permanecer activa para la PERSONA.");
        persona.equipRunicMark(RunicMarkId.RESONANCIA);
        equipment.synchronizeRunicSelection(persona);
        org.junit.jupiter.api.Assertions.assertTrue(equipment.hasAwakenedRunicMark(RunicMarkId.RESONANCIA, sheet)
                        && !equipment.hasAwakenedRunicMark(RunicMarkId.SILENCIO, sheet),
                "Cambiar de marca debe retirar la anterior y activar la nueva.");
    }

    private static void verifyCommonMasteryContract() {
        CharacterSheet sheet = CharacterSheet.of(76, 75, 76, 50, 70, 30, 60, 50, 75);
        CharacterMasteryCollection collection = CharacterMasteryCollection.allCanonical();
        VitalResourceState resources = new VitalResourceState(100, 100);
        MasteryRuntimeContext runtime = new MasteryRuntimeContext(sheet, Gender.HOMBRE, resources,
                new HostileEncounterState(), true);
        MasteryActor actor = new MasteryActor() {
            public CharacterSheet sheet() { return sheet; }
            public Gender gender() { return Gender.HOMBRE; }
            public CharacterMasteryCollection masteries() { return collection; }
            public MasteryRuntimeContext runtimeContext() { return runtime; }
        };
        MasteryUseService service = new MasteryUseService();
        org.junit.jupiter.api.Assertions.assertTrue(!service.available(actor, MasteryType.ACTIVE).isEmpty(),
                "Jugador e IA deben consultar el mismo catálogo de acciones disponibles.");
    }

    
}
