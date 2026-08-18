package qa.architecture;

import domain.ability.*;
import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.control.*;
import domain.inventory.logistics.*;
import domain.runic.*;
import domain.social.*;
import domain.worldmemory.spatial.WorldCoordinate;
import java.util.*;

/**  — límite humano Noble, Marca Rúnica humana, TRANSMUTACIÓN concurrente y controles/logística. */
public final class NobleHumanLimitTransportMasteryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("architecture")
        void verifiesCanonicalContract(){
        nobleHumanLimit();
        runicMarkDoesNotRequireEvolutiveMilestones();
        transmutationBranchesAreConcurrent();
        transportUniquenessAndSelection();
        controlSynchronization();
        nobleTravelerCloakRemoved();
    }

    private static void nobleHumanLimit() {
        for (var bySub : NobleCanonicalProfiles.all().entrySet()) for (var p : bySub.getValue().values()) {
            org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(Attribute.VITALIDAD) <= 75, "Noble supera VITALIDAD 75: "+p.subprofession()+"/"+p.characterClass());
            org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(Attribute.ADAPTABILIDAD) <= 75, "Noble supera ADAPTABILIDAD 75: "+p.subprofession()+"/"+p.characterClass());
            Attribute professional = new RunicMarkProgressPolicy().affinityAttribute(p.characterClass());
            org.junit.jupiter.api.Assertions.assertTrue(p.attributes().valueOf(professional) == 75, "Noble de cúspide sin 75 profesional: "+p.subprofession()+"/"+p.characterClass()+" / "+professional);
            org.junit.jupiter.api.Assertions.assertTrue(p.canonicalLevel()==p.attributes().totalAttributeLevel(), "Nivel Noble no derivado de atributos.");
        }
    }

    private static void runicMarkDoesNotRequireEvolutiveMilestones() {
        var p = NobleCanonicalProfiles.profile(Subprofession.PATRIMONIAL_WARLORD, CharacterClass.LUCHADOR);
        CharacterMasteryCollection c = new CharacterMasteryCollection(List.of(), Gender.HOMBRE);
        for (MasteryId id : MasteryId.values()) {
            if (MasteryCatalog.require(id).category() != MasteryCategory.EVOLUTIVE) c.unlock(id);
        }
        org.junit.jupiter.api.Assertions.assertTrue(new RunicMarkProgressPolicy().resolve(CharacterClass.LUCHADOR,c,p.attributes())==RunicMarkProgressState.AWAKENED,
                "La Marca humana no debe exigir maestrías EVOLUTIVE/HITOS aparte.");
        for (MasteryId id : MasteryId.values()) if (MasteryCatalog.require(id).category()==MasteryCategory.EVOLUTIVE)
            org.junit.jupiter.api.Assertions.assertTrue(!c.knowledgeState(id).isUsable(), "La prueba no debe desbloquear maestrías evolutivas.");
    }

    private static void transmutationBranchesAreConcurrent() {
        Set<String> simultaneous = new HashSet<>();
        simultaneous.add("TRANSMUTACION:OVERCLOCK");
        simultaneous.add("TRANSMUTACION:OVERDRIVE");
        simultaneous.add("TRANSMUTACION:METAMORPHOSIS");
        org.junit.jupiter.api.Assertions.assertTrue(simultaneous.size()==3, "Las ramas de TRANSMUTACIÓN deben poder coexistir.");
        // La única exclusión sostenida del dispatcher pertenece a EXPLOSION_CINETICA; TRANSMUTACION no se purga por familia.
        String source = readDispatcherContract();
        org.junit.jupiter.api.Assertions.assertTrue(source.contains("manifestation.familyId() == MasteryId.EXPLOSION_CINETICA"), "Debe conservarse sólo la exclusión específica de EXPLOSIÓN CINÉTICA.");
        org.junit.jupiter.api.Assertions.assertTrue(!source.contains("manifestation.familyId() == MasteryId.TRANSMUTACION"), "TRANSMUTACIÓN no puede imponer exclusión mutua entre nodos.");
    }

    private static String readDispatcherContract() {
        try { return java.nio.file.Files.readString(java.nio.file.Path.of("src/main/java/domain/ability/MasteryExecutionDispatcher.java")); }
        catch (Exception e) { throw new AssertionError("No se pudo auditar el dispatcher de maestrías.", e); }
    }

    private static void transportUniquenessAndSelection() {
        PersonalTransportState state = PersonalTransportState.allAcquired(new WorldCoordinate(0,0,0));
        org.junit.jupiter.api.Assertions.assertTrue(state.units().size()==PersonalTransportType.values().length, "Debe existir exactamente un estado por tipo de transporte.");
        org.junit.jupiter.api.Assertions.assertTrue(new HashSet<>(state.ownedTypes()).size()==state.ownedTypes().size(), "No puede poseerse dos veces el mismo PersonalTransportType.");
        for (PersonalTransportType t : state.ownedTypes()) org.junit.jupiter.api.Assertions.assertTrue(state.select(t).selectedType().orElseThrow()==t, "La rueda debe seleccionar el mismo transporte que usa la llamada.");
    }

    private static void controlSynchronization() {
        org.junit.jupiter.api.Assertions.assertTrue(binding(PcControlScheme.canonicalBindings(),"B",InputGesture.PRESS,ControlAction.CALL_PERSONAL_TRANSPORT),"PC B debe llamar transporte.");
        org.junit.jupiter.api.Assertions.assertTrue(binding(PcControlScheme.canonicalBindings(),"B",InputGesture.HOLD,ControlAction.OPEN_PERSONAL_TRANSPORT_WHEEL),"PC B HOLD debe abrir rueda de transporte.");
        org.junit.jupiter.api.Assertions.assertTrue(binding(PcControlScheme.canonicalBindings(),"Z",InputGesture.PRESS,ControlAction.EXECUTE_ACTIVE_MASTERY),"PC Z debe ejecutar activa.");
        org.junit.jupiter.api.Assertions.assertTrue(binding(PcControlScheme.canonicalBindings(),"Z",InputGesture.HOLD,ControlAction.OPEN_ACTIVE_MASTERY_WHEEL),"PC Z HOLD debe abrir activas.");
        org.junit.jupiter.api.Assertions.assertTrue(binding(PcControlScheme.canonicalBindings(),"X",InputGesture.PRESS,ControlAction.TOGGLE_SUSTAINED_MASTERY),"PC X debe alternar sostenida.");
        org.junit.jupiter.api.Assertions.assertTrue(binding(PcControlScheme.canonicalBindings(),"X",InputGesture.HOLD,ControlAction.OPEN_SUSTAINED_MASTERY_WHEEL),"PC X HOLD debe abrir sostenidas.");
        org.junit.jupiter.api.Assertions.assertTrue(has(Ps4ControlScheme.canonicalBindings(),ControlAction.CALL_PERSONAL_TRANSPORT,InputGesture.PRESS),"PS4 sin llamada de transporte.");
        org.junit.jupiter.api.Assertions.assertTrue(has(Ps4ControlScheme.canonicalBindings(),ControlAction.OPEN_PERSONAL_TRANSPORT_WHEEL,InputGesture.HOLD),"PS4 sin rueda de transporte.");
        org.junit.jupiter.api.Assertions.assertTrue(has(Ps4ControlScheme.canonicalBindings(),ControlAction.EXECUTE_ACTIVE_MASTERY,InputGesture.PRESS),"PS4 sin ejecución activa.");
        org.junit.jupiter.api.Assertions.assertTrue(has(Ps4ControlScheme.canonicalBindings(),ControlAction.TOGGLE_SUSTAINED_MASTERY,InputGesture.PRESS),"PS4 sin sostenida.");
    }
    private static boolean binding(List<ControlBinding> xs,String input,InputGesture g,ControlAction a){return xs.stream().anyMatch(b->b.input().equals(input)&&b.gesture()==g&&b.action()==a);}
    private static boolean has(List<ControlBinding> xs,ControlAction a,InputGesture g){return xs.stream().anyMatch(b->b.action()==a&&b.gesture()==g);}

    private static void nobleTravelerCloakRemoved() {
        for (var e : NobleCanonicalProfiles.all().entrySet()) for (CharacterClass c : e.getValue().keySet())
            org.junit.jupiter.api.Assertions.assertTrue(NobleStartingEquipmentCatalog.equipment(e.getKey(),c).wornGarments().stream().noneMatch(a->a.name().equals("Capa del Viajero V881")),
                    "La Capa del Viajero no pertenece al loadout Noble: "+e.getKey()+"/"+c);
    }
    
}
