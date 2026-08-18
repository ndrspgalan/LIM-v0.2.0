package qa.domain;

import domain.inventory.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.logistics.*;
import java.util.*;

public final class ModularInventoryVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        nakedStartsWithoutCapacityOrQuickAccess();
        volumeProjectionAndCanonicalContainers();
        quickAccessThreeAndFourFollowPhysicalProviders();
        backpackCarriesHelmetExternallyWithoutGridCost();
    }

    private static void nakedStartsWithoutCapacityOrQuickAccess() {
        InventoryState naked=InventoryState.emptyWithoutPersonalTransport();
        for(InventoryCompartmentType type:InventoryCompartmentType.values())
            org.junit.jupiter.api.Assertions.assertTrue(!naked.logistics().compartment(type).available(),"Desnudo no debe disponer de "+type);
        for(int slot=1;slot<=4;slot++)
            org.junit.jupiter.api.Assertions.assertTrue(!QuickAccessPolicy.isSlotAvailable(slot,naked.equipment(),naked.logistics()),"Desnudo no debe tener quick "+slot);
        org.junit.jupiter.api.Assertions.assertTrue(naked.logistics().totalWeightKg()==0.0,"Sin contenedores no debe existir peso logístico basal.");
    }

    private static void volumeProjectionAndCanonicalContainers() {
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.LEG_POUCH.label().equals("Pernera Modular de Camino V881"),"Nombre canónico de pernera.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.BANDOLIER.label().equals("Bandolera de Servicio V881"),"Nombre canónico de bandolera.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.BACKPACK.label().equals("Mochila Dorsal de Expedición V881"),"Nombre canónico de mochila.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.LEG_POUCH.grid().equals(new InventoryGridDefinition(4,2)),"Pernera 4x2x1 -> 4x2.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.BANDOLIER.grid().equals(new InventoryGridDefinition(8,4)),"Bandolera 4x2x2 -> 8x4.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.BACKPACK.grid().equals(new InventoryGridDefinition(15,9)),"Mochila 5x3x3 -> 15x9.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.BACKPACK.grid().capacity()==135,"La mochila debe ofrecer 135 celdas proyectadas.");
        org.junit.jupiter.api.Assertions.assertTrue(close(InventoryCompartmentType.LEG_POUCH.structuralWeightKg(),.45),"Peso pernera.");
        org.junit.jupiter.api.Assertions.assertTrue(close(InventoryCompartmentType.BANDOLIER.structuralWeightKg(),.80),"Peso bandolera.");
        org.junit.jupiter.api.Assertions.assertTrue(close(InventoryCompartmentType.BACKPACK.structuralWeightKg(),1.50),"Peso mochila.");
    }

    private static void quickAccessThreeAndFourFollowPhysicalProviders() {
        LogisticsState state=LogisticsState.emptyWithoutPersonalTransport()
                .withCompartment(InventoryCompartment.empty(InventoryCompartmentType.LEG_POUCH,true))
                .withCompartment(InventoryCompartment.empty(InventoryCompartmentType.BANDOLIER,true));
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessPolicy.isSlotAvailable(3,EquipmentState.empty(),state),"Pernera habilita quick 3.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessPolicy.isSlotAvailable(4,EquipmentState.empty(),state),"Bandolera habilita quick 4.");
        org.junit.jupiter.api.Assertions.assertTrue(!QuickAccessPolicy.isSlotAvailable(1,EquipmentState.empty(),state)," aún debe aportar polainas inventariables.");
        org.junit.jupiter.api.Assertions.assertTrue(!QuickAccessPolicy.isSlotAvailable(2,EquipmentState.empty(),state)," aún debe aportar corazas inventariables.");
    }

    private static void backpackCarriesHelmetExternallyWithoutGridCost() {
        LogisticsState state=LogisticsState.emptyWithoutPersonalTransport()
                .withCompartment(InventoryCompartment.empty(InventoryCompartmentType.BACKPACK,true));
        var helmet=ArmorCatalog.historicalKnightHelmet();
        int freeBefore=state.compartment(InventoryCompartmentType.BACKPACK).freeSlots();
        LogisticsState loaded=state.attachHelmetToBackpack(helmet);
        InventoryCompartment backpack=loaded.compartment(InventoryCompartmentType.BACKPACK);
        org.junit.jupiter.api.Assertions.assertTrue(backpack.externallyCarriedHelmet().orElseThrow()==helmet,"Debe conservar la misma instancia de casco.");
        org.junit.jupiter.api.Assertions.assertTrue(backpack.freeSlots()==freeBefore,"El casco externo no consume celdas internas.");
        org.junit.jupiter.api.Assertions.assertTrue(close(backpack.externalLoadWeightKg(),helmet.weightKg()),"El casco externo sí conserva su masa física.");
        boolean eyewearRejected=false;
        try{state.attachHelmetToBackpack(ArmorCatalog.normalVisionGlassesV881());}catch(IllegalArgumentException expected){eyewearRejected=true;}
        org.junit.jupiter.api.Assertions.assertTrue(eyewearRejected,"Las gafas no pueden usar el portacasco.");
    }

    private static boolean close(double a,double b){return Math.abs(a-b)<1e-9;}
    
}
