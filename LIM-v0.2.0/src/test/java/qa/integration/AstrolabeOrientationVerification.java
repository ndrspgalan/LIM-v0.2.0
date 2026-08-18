package qa.integration;

import domain.animation.CanonicalAnimation;
import domain.animation.CharacterAnimationState;
import domain.inventory.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import domain.orientation.*;
import domain.worldmemory.*;
import domain.worldmemory.access.*;
import domain.worldmemory.entry.*;
import domain.worldmemory.evidence.*;
import domain.worldmemory.revision.*;
import domain.worldmemory.spatial.*;

import java.time.Instant;
import java.util.*;

public final class AstrolabeOrientationVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        astrolabeIsPersistentAndCatalogued();
        everyImmediateMiscellaneousObjectRequiresQuickAccess();
        astrolabeRequiresTheSameEquippedInstance();
        orientationRequiresStillnessAndSelectedMemory();
        mountedStillnessIsValid();
    }

    private static void astrolabeIsPersistentAndCatalogued() {
        AstrolabeItem astrolabe = new AstrolabeItem();
        org.junit.jupiter.api.Assertions.assertTrue(astrolabe.weightKg() == 0.9, "El astrolabio debe pesar 0,9 kg.");
        org.junit.jupiter.api.Assertions.assertTrue(astrolabe.footprint().equals(new InventoryFootprint(2, 2)), "El astrolabio debe ocupar 2 x 2.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.item.AccessoryItem.class.isAssignableFrom(AstrolabeItem.class), "El astrolabio debe ser un abalorio-artefacto persistente, no un consumible por cargas.");
        org.junit.jupiter.api.Assertions.assertTrue(domain.inventory.catalog.PhysicalObjectCatalog.containsName("Astrolabio"),
                "El catálogo canónico debe incluir el astrolabio.");
    }

    private static void everyImmediateMiscellaneousObjectRequiresQuickAccess() {
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(domain.inventory.item.misc.MiscellaneousItemCatalog.whetstone()), "Piedra de afilar.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(domain.inventory.item.misc.MiscellaneousItemCatalog.amadou()), "Amadou.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(domain.inventory.item.misc.MiscellaneousItemCatalog.flint()), "Pedernal.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(domain.inventory.item.misc.MiscellaneousItemCatalog.pebble()), "Guijarro.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(new CurrencyStack(CurrencyType.VALERITA, 1)), "Valerita.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(new CurrencyStack(CurrencyType.SUELDO, 1)), "Sueldo.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(new CurrencyStack(CurrencyType.BERYLARE, 1)), "Berylare.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(new CurrencyStack(CurrencyType.REAL_A5, 1)), "Real de A5.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(domain.inventory.item.misc.MiscellaneousItemCatalog.bread()), "Alimentos.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.requiresQuickAccess(domain.inventory.item.misc.MiscellaneousItemCatalog.stimulantInjection()), "Estimulantes.");
    }

    private static void astrolabeRequiresTheSameEquippedInstance() {
        AstrolabeItem assigned = new AstrolabeItem();
        InventoryState inventory = inventoryWithEquippedAccessory(assigned);
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessUsePolicy.isActiveEquipment(assigned, inventory.equipment()), "La instancia equipada debe poder usarse.");
        org.junit.jupiter.api.Assertions.assertTrue(!QuickAccessUsePolicy.isActiveEquipment(new AstrolabeItem(), inventory.equipment()),
                "Otra instancia idéntica no debe heredar el equipamiento.");
    }

    private static void orientationRequiresStillnessAndSelectedMemory() {
        AstrolabeItem astrolabe = new AstrolabeItem();
        InventoryState inventory = inventoryWithEquippedAccessory(astrolabe);
        WorldMemory memory = memoryWithSelectedDestination(new WorldCoordinate(100, 100, 0));
        CharacterAnimationState animation = new CharacterAnimationState();
        AstrolabeUsePolicy policy = new AstrolabeUsePolicy();

        var moving = policy.orient(astrolabe, inventory, memory, new WorldCoordinate(0, 0, 0),
                MovementState.movingOnFoot(), animation);
        org.junit.jupiter.api.Assertions.assertTrue(!moving.successful(), "No puede orientarse en movimiento.");

        var success = policy.orient(astrolabe, inventory, memory, new WorldCoordinate(0, 0, 0),
                MovementState.standingOnFoot(), animation);
        org.junit.jupiter.api.Assertions.assertTrue(success.successful(), "Debe poder orientarse estando quieto.");
        org.junit.jupiter.api.Assertions.assertTrue(success.solution().direction() == OrientationDirection.NORTH_EAST, "La dirección debe ser noreste.");
        org.junit.jupiter.api.Assertions.assertTrue(animation.currentAnimation() == CanonicalAnimation.ORIENT, "Debe activar ORIENTARSE.");

        WorldMemory empty = new WorldMemory();
        var absent = policy.orient(astrolabe, inventory, empty, new WorldCoordinate(0, 0, 0),
                MovementState.standingOnFoot(), new CharacterAnimationState());
        org.junit.jupiter.api.Assertions.assertTrue(!absent.successful(), "Sin destino seleccionado no hay orientación.");
    }

    private static void mountedStillnessIsValid() {
        AstrolabeItem astrolabe = new AstrolabeItem();
        var result = new AstrolabeUsePolicy().orient(astrolabe, inventoryWithEquippedAccessory(astrolabe),
                memoryWithSelectedDestination(new WorldCoordinate(0, 50, 0)), new WorldCoordinate(0, 0, 0),
                MovementState.standingMounted(), new CharacterAnimationState());
        org.junit.jupiter.api.Assertions.assertTrue(result.successful(), "Montado y detenido debe ser válido.");
        org.junit.jupiter.api.Assertions.assertTrue(result.solution().direction() == OrientationDirection.NORTH, "Debe señalar el norte.");
    }

    private static InventoryState inventoryWithEquippedAccessory(InventoryEntry item) {
        var equipment = new EquipmentState(java.util.Map.of(domain.inventory.equipment.EquipmentSlot.ACCESSORY,item));
        EnumMap<InventoryCompartmentType, InventoryCompartment> compartments = new EnumMap<>(InventoryCompartmentType.class);
        for (InventoryCompartmentType type : InventoryCompartmentType.values()) compartments.put(type, InventoryCompartment.empty(type, false));
        return new InventoryState(equipment, QuickAccessBar.empty(), new LogisticsState(compartments, PersonalTransportState.none()));
    }

    private static WorldMemory memoryWithSelectedDestination(WorldCoordinate coordinate) {
        WorldMemory memory = new WorldMemory();
        IndicatorId id = new IndicatorId("destino-m192");
        WorldKnowledgeSource source = new WorldKnowledgeSource(KnowledgeSourceType.DIRECT_EXPLORATION,
                "verificacion-m192", Instant.now(), KnowledgeReliability.OBSERVED);
        RememberedIndicator indicator = new RememberedIndicator(id, IndicatorType.OBJECTIVE_REFERENCE, "Destino", "",
                RememberedPosition.verified(coordinate), KnowledgeReliability.OBSERVED, List.of(source));
        memory.knowledge().remember(indicator);
        memory.knowledge().select(id);
        return memory;
    }

    
}
