package qa.regression;

import domain.control.ControlAction;
import domain.control.PcControlScheme;
import domain.inventory.InventoryState;
import domain.inventory.InventoryGridDefinition;
import domain.inventory.QuickAccessBar;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.misc.ReconnaissanceMonocularItem;
import domain.inventory.logistics.LogisticsState;
import domain.inventory.logistics.InventoryCompartment;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.inventory.logistics.PersonalTransportState;
import java.util.EnumMap;
import domain.observation.MonocularMagnification;
import domain.observation.MonocularObservationSession;
import domain.orientation.WorldOrientationService;
import domain.settings.GameSettings;
import domain.worldmemory.WorldMemory;
import domain.worldmemory.spatial.WorldCoordinate;

import java.util.List;
import java.util.Optional;

public final class ReconnaissanceMonocularVerification {
    private ReconnaissanceMonocularVerification() {}

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("regression")
        void verifiesCanonicalContract(){
        ReconnaissanceMonocularItem item = new ReconnaissanceMonocularItem();
        org.junit.jupiter.api.Assertions.assertTrue(item.weightKg() == 0.48, "Peso del monocular.");
        org.junit.jupiter.api.Assertions.assertTrue(item.footprint().verticalSlots() == 2 && item.footprint().horizontalSlots() == 1, "Footprint 2x1.");
        org.junit.jupiter.api.Assertions.assertTrue(item.narrativeDescription().equals(ReconnaissanceMonocularItem.DESCRIPTION.trim()), "Narrativa exacta.");

        EnumMap<InventoryCompartmentType, InventoryCompartment> compartments = new EnumMap<>(InventoryCompartmentType.class);
        for (InventoryCompartmentType type : InventoryCompartmentType.values()) {
            compartments.put(type, InventoryCompartment.empty(type, type == InventoryCompartmentType.BODY));
        }
        compartments.put(InventoryCompartmentType.LEGGINGS_STORAGE,
                new InventoryCompartment(InventoryCompartmentType.LEGGINGS_STORAGE, true, new InventoryGridDefinition(2, 7), List.of(item), Optional.empty()));
        InventoryState inventory = new InventoryState(EquipmentState.empty(),
                new QuickAccessBar(List.of(Optional.of(item), Optional.empty(), Optional.empty(), Optional.empty())),
                new LogisticsState(compartments, PersonalTransportState.none()));
        MonocularObservationSession session = new MonocularObservationSession();
        session.activate(item, inventory);
        org.junit.jupiter.api.Assertions.assertTrue(session.active() && session.weaponSheathedByActivation(), "Activar debe envainar semánticamente el arma activa.");
        org.junit.jupiter.api.Assertions.assertTrue(session.magnification() == MonocularMagnification.X3 && session.effectiveObservationRangeMeters() == 750, "×3 por defecto.");
        session.wheelUp(); org.junit.jupiter.api.Assertions.assertTrue(session.effectiveObservationRangeMeters() == 1000, "×4.");
        session.wheelUp(); org.junit.jupiter.api.Assertions.assertTrue(session.effectiveObservationRangeMeters() == 1250, "×5.");
        session.wheelUp(); org.junit.jupiter.api.Assertions.assertTrue(session.effectiveObservationRangeMeters() == 1250, "×5 guardarraíl.");
        session.wheelDown(); org.junit.jupiter.api.Assertions.assertTrue(session.effectiveObservationRangeMeters() == 1000, "Bajar a ×4.");
        org.junit.jupiter.api.Assertions.assertTrue(session.telemetry(999).available(), "Telemetría en rango.");
        org.junit.jupiter.api.Assertions.assertTrue(!session.telemetry(1001).available(), "Telemetría fuera del alcance actual.");

        WorldMemory memory = new WorldMemory();
        WorldCoordinate mark = new WorldCoordinate(100, 0, 0);
        session.toggleObservationMark(memory.knowledge(), mark);
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().observationMarkSelected(), "La marca nueva debe ser la referencia espacial vigente.");
        org.junit.jupiter.api.Assertions.assertTrue(new WorldOrientationService().selectedDestination(memory, new WorldCoordinate(0,0,0)).available(), "Astrolabio debe consumir la marca.");
        session.toggleObservationMark(memory.knowledge(), new WorldCoordinate(200, 0, 0));
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().observationMark().orElseThrow().coordinate().x() == 200, "Nueva marca sustituye a la anterior.");
        org.junit.jupiter.api.Assertions.assertTrue(!memory.knowledge().clearObservationMarkIfReached(new WorldCoordinate(195.0, 0, 0)), "A exactamente 5 m no se elimina.");
        org.junit.jupiter.api.Assertions.assertTrue(memory.onProtagonistPositionChanged(new WorldCoordinate(195.1, 0, 0)), "A menos de 5 m se elimina automáticamente al actualizar posición.");
        session.toggleObservationMark(memory.knowledge(), mark);
        session.toggleObservationMark(memory.knowledge(), mark);
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().observationMark().isEmpty(), "E sobre la misma marca debe retirarla.");
        session.toggleObservationMark(memory.knowledge(), mark);
        memory.knowledge().toggleObservationMarkSelection();
        org.junit.jupiter.api.Assertions.assertTrue(memory.knowledge().observationMark().isEmpty(), "Seleccionar otra vez la marca en Memoria debe retirarla.");

        org.junit.jupiter.api.Assertions.assertTrue(new GameSettings().renderDistanceMeters() == 1500 && new GameSettings().renderDistanceLocked(), "Render distance fija 1500 m.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == ControlAction.TOGGLE_OBSERVATION_MARK && b.input().equals("E")), "E marca observación.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == ControlAction.MONOCULAR_ZOOM_IN), "Rueda arriba zoom.");
        org.junit.jupiter.api.Assertions.assertTrue(PcControlScheme.canonicalBindings().stream().anyMatch(b -> b.action() == ControlAction.MONOCULAR_ZOOM_OUT), "Rueda abajo zoom.");
    }

    
}
