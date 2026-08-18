package qa.domain;

import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.inventory.logistics.*;
import java.util.*;

public final class GarmentStorageVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        exactShortlistAndCapacities();
        rejectedGarmentsStayNonInventory();
        layeredChestStorageAddsPhysicalModules();
        leggingsStorageUnlocksQuickOne();
        chestStorageUnlocksQuickTwo();
        moduleGeometryPreventsFakeAggregatePacking();
        removingProviderWithContentsFailsUntilEmptied();
    }

    private static void exactShortlistAndCapacities() {
        org.junit.jupiter.api.Assertions.assertTrue(GarmentStorageCatalog.profileCount()==40,"Deben existir exactamente 40 prendas inventariables.");
        Map<String,Integer> expected = new LinkedHashMap<>();
        expected.put("Camisa de trabajo V881",8); expected.put("Chaleco V881",8); expected.put("Chaleco largo V881",12);
        expected.put("Chaleco de trabajo V881",16); expected.put("Chaleco de montar V881",8); expected.put("Levita V881",12);
        expected.put("Frac V881",8); expected.put("Chaqué V881",8); expected.put("Americana V881",12); expected.put("Chaqueta Norfolk V881",16);
        expected.put("Blusón de trabajo V881",16); expected.put("Gabán V881",20); expected.put("Sobretodo V881",20); expected.put("Ulster V881",24);
        expected.put("Guardapolvo V881",16); expected.put("Gabardina V881",20); expected.put("Chaqueta de montar V881",10); expected.put("Capa Inverness V881",16);
        expected.put("Dolman V881",8); expected.put("Chaqueta de Viaje V881",16); expected.put("Chaqueta de Aeronauta V881",12);
        expected.put("Chaqueta cruzada de motorista V881",12); expected.put("Delantal de Taller V881",20); expected.put("Mono Ignífugo V881",16);
        expected.put("Pantalón recto V881",8); expected.put("Pantalón formal V881",8); expected.put("Pantalón de trabajo V881",16);
        expected.put("Pantalón de cintura alta V881",8); expected.put("Pantalón holgado V881",10); expected.put("Pantalón marinero V881",10);
        expected.put("Pantalón de montar V881",6); expected.put("Breeches V881",6); expected.put("Knickerbockers V881",8);
        expected.put("Falda de paseo V881",12); expected.put("Falda de trabajo V881",16); expected.put("Falda de montar V881",8);
        expected.put("Falda dividida V881",12); expected.put("Pantalón de cuero endurecido V881",12);
        expected.put("Pantalón infantil V881",8); expected.put("Falda infantil V881",8);
        org.junit.jupiter.api.Assertions.assertTrue(expected.size()==40,"Tabla esperada incompleta.");
        expected.forEach((name,slots)->{
            var p=GarmentStorageCatalog.profileForName(name).orElseThrow(()->new IllegalStateException("Falta "+name));
            org.junit.jupiter.api.Assertions.assertTrue(p.capacitySlots()==slots,name+" debe aportar "+slots+" slots y aporta "+p.capacitySlots());
            org.junit.jupiter.api.Assertions.assertTrue(slots<=32,name+" no debe superar individualmente la Bandolera de Servicio (32).");
            p.modules().forEach(m->org.junit.jupiter.api.Assertions.assertTrue(m.physicalDimensions()!=null && m.physicalDimensions().zSlots()==1,
                    name+" debe usar bolsillos textiles/cosidos de profundidad Z=1."));
        });
    }

    private static void rejectedGarmentsStayNonInventory() {
        List<ArmorPiece> rejected=List.of(ArmorCatalog.innerShirt(),ArmorCatalog.middlePaddedWaistcoat(),ArmorCatalog.outerTravelerCloak(),
                ArmorCatalog.middleBloomersV881(),ArmorCatalog.leatherStrapBuckleGaitersV881(),ArmorCatalog.paperChestV881(),ArmorCatalog.insulatingSuit());
        rejected.forEach(p->org.junit.jupiter.api.Assertions.assertTrue(GarmentStorageCatalog.profileFor(p).isEmpty(),p.name()+" no debe aportar inventario."));
    }

    private static void layeredChestStorageAddsPhysicalModules() {
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.INNER,ArmorCatalog.innerWorkShirt())
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,ArmorCatalog.middleWaistcoat())
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,ArmorCatalog.outerTrenchV881());
        LogisticsState state=LogisticsState.emptyWithoutPersonalTransport().synchronizeGarmentStorage(layout);
        InventoryCompartment chest=state.compartment(InventoryCompartmentType.CHEST_STORAGE);
        org.junit.jupiter.api.Assertions.assertTrue(chest.available(),"Las prendas inventariables CHEST deben activar el compartimento.");
        org.junit.jupiter.api.Assertions.assertTrue(chest.capacitySlots()==36,"Camisa 8 + chaleco 8 + gabardina 20 = 36 slots.");
        org.junit.jupiter.api.Assertions.assertTrue(chest.storageModules().size()==2+2+4,"Deben conservarse ocho bolsillos físicos independientes.");
    }

    private static void leggingsStorageUnlocksQuickOne() {
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.MIDDLE,ArmorCatalog.middleWorkTrousersV881());
        LogisticsState state=LogisticsState.emptyWithoutPersonalTransport().synchronizeGarmentStorage(layout);
        org.junit.jupiter.api.Assertions.assertTrue(state.compartment(InventoryCompartmentType.LEGGINGS_STORAGE).capacitySlots()==16,"Pantalón de trabajo = 16.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessPolicy.isSlotAvailable(1,EquipmentState.empty(),state),"Una prenda LEGGINGS inventariable habilita quick 1.");
    }

    private static void chestStorageUnlocksQuickTwo() {
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,ArmorCatalog.outerUlsterV881());
        LogisticsState state=LogisticsState.emptyWithoutPersonalTransport().synchronizeGarmentStorage(layout);
        org.junit.jupiter.api.Assertions.assertTrue(state.compartment(InventoryCompartmentType.CHEST_STORAGE).capacitySlots()==24,"Ulster = 24.");
        org.junit.jupiter.api.Assertions.assertTrue(QuickAccessPolicy.isSlotAvailable(2,EquipmentState.empty(),state),"Una prenda CHEST inventariable habilita quick 2.");
    }

    private static void moduleGeometryPreventsFakeAggregatePacking() {
        ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.CHEST,ArmorLayerPosition.OUTER,ArmorCatalog.outerUlsterV881());
        LogisticsState state=LogisticsState.emptyWithoutPersonalTransport().synchronizeGarmentStorage(layout);
        InventoryCompartment chest=state.compartment(InventoryCompartmentType.CHEST_STORAGE);
        org.junit.jupiter.api.Assertions.assertTrue(chest.capacitySlots()==24,"Control capacidad Ulster.");
        InventoryEntry tooWide=new InventoryEntry("Objeto ancho","Prueba",.1,new InventoryFootprint(2,4),List.of());
        boolean rejected=false;
        try { new InventoryCompartment(chest.type(),true,chest.grid(),List.of(tooWide),Optional.empty(),chest.storageModules()); }
        catch(IllegalArgumentException expected){ rejected=true; }
        org.junit.jupiter.api.Assertions.assertTrue(rejected,"24 slots agregados no deben fingir un bolsillo de 30 cm de ancho inexistente.");
    }

    private static void removingProviderWithContentsFailsUntilEmptied() {
        ArmorEquipmentLayout withPants=ArmorEquipmentLayout.empty()
                .equip(EquipmentSlot.LEGGINGS,ArmorLayerPosition.MIDDLE,ArmorCatalog.middleStraightTrousersV881());
        LogisticsState state=LogisticsState.emptyWithoutPersonalTransport().synchronizeGarmentStorage(withPants);
        InventoryEntry item=new InventoryEntry("Objeto de bolsillo","Prueba",.1,new InventoryFootprint(2,2),List.of());
        InventoryCompartment legs=state.compartment(InventoryCompartmentType.LEGGINGS_STORAGE).withEntries(List.of(item));
        LogisticsState loaded=state.withCompartment(legs);
        boolean rejected=false;
        try { loaded.synchronizeGarmentStorage(ArmorEquipmentLayout.empty()); } catch(IllegalArgumentException expected){rejected=true;}
        org.junit.jupiter.api.Assertions.assertTrue(rejected,"No se puede quitar la prenda dejando objetos suspendidos en un inventario inexistente.");
        LogisticsState emptied=loaded.withCompartment(legs.withEntries(List.of())).synchronizeGarmentStorage(ArmorEquipmentLayout.empty());
        org.junit.jupiter.api.Assertions.assertTrue(!emptied.compartment(InventoryCompartmentType.LEGGINGS_STORAGE).available(),"Vacío sí permite retirar la prenda.");
    }

    
}
