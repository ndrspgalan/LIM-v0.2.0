package qa.domain;

import domain.inventory.*;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.LayeredEquipmentState;
import domain.inventory.item.armor.ArmorLayerPosition;
import domain.inventory.item.armor.ArmorCatalog;
import domain.inventory.item.misc.FoodItem;
import domain.inventory.logistics.*;
import domain.survival.FoodType;
import java.util.*;

public final class InventoryActionsAndTransportStorageVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyTransportSaddlebags();
        verifyInventoryActions();
    }

    private static void verifyTransportSaddlebags(){
        org.junit.jupiter.api.Assertions.assertTrue(PersonalTransportSaddlebagPolicy.compartmentType(PersonalTransportType.BICYCLE_FOLDING_V881).isEmpty(),"La plegable no admite alforjas.");
        org.junit.jupiter.api.Assertions.assertTrue(Arrays.stream(PersonalTransportType.values()).filter(t -> PersonalTransportSaddlebagPolicy.compartmentType(t).isPresent()).count()==5,"Deben existir cinco diseños específicos de alforjas.");
        for(PersonalTransportType t:PersonalTransportType.values()){
            var c=PersonalTransportSaddlebagPolicy.compartmentType(t);
            if(c.isEmpty()) continue;
            double audited=PersonalTransportPayloadPolicy.saddlebagContentsLimitKgWithCopilot(t).orElseThrow();
            double compartment=c.get().maximumWeightKg().orElseThrow();
            org.junit.jupiter.api.Assertions.assertTrue(Math.abs(audited-compartment)<1e-9,"El límite físico debe coincidir con la auditoría de copiloto para "+t.label());
            org.junit.jupiter.api.Assertions.assertTrue(c.get().storedFootprint().occupiedSlots()>0,"Las alforjas desequipadas deben ocupar inventario.");
        }
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_HORSE_RACING.grid().capacity()==24,"Carreras debe ser el diseño ecuestre más compacto.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT.grid().capacity()==162,"Tiro debe ser el diseño de mayor volumen.");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN.maximumWeightKg().orElseThrow()==20.0,"Cardán reserva 20 kg para contenido lateral tras copiloto.");
    }

    private static void verifyInventoryActions(){
        var food=new FoodItem("Fruta de prueba","Fruta utilizable desde acceso rápido.",FoodType.FRUIT,1,1,0.20,new InventoryFootprint(1,1),1.0,0,0);
        var shirt=ArmorCatalog.innerWorkShirt();
        EnumMap<InventoryCompartmentType,InventoryCompartment> compartments=new EnumMap<>(InventoryCompartmentType.class);
        for(InventoryCompartmentType t:InventoryCompartmentType.values()) compartments.put(t,InventoryCompartment.empty(t,false));
        compartments.put(InventoryCompartmentType.LEG_POUCH,new InventoryCompartment(InventoryCompartmentType.LEG_POUCH,true,List.of(food)));
        // la Camisa de trabajo ocupa 3x2 por XYZ y ya no cabe físicamente en una pernera 2x2
        // ni en sus propios bolsillos una vez desequipada. La bandolera ofrece el primer destino automático compatible.
        compartments.put(InventoryCompartmentType.BANDOLIER,InventoryCompartment.empty(InventoryCompartmentType.BANDOLIER,true));
        compartments.put(InventoryCompartmentType.BACKPACK,new InventoryCompartment(InventoryCompartmentType.BACKPACK,true,List.of(shirt)));
        InventoryState state=new InventoryState(domain.inventory.equipment.EquipmentState.empty(),QuickAccessBar.empty(),new LogisticsState(compartments,PersonalTransportState.none()));

        var foodActions=InventoryObjectActionPolicy.evaluate(food,state);
        org.junit.jupiter.api.Assertions.assertTrue(foodActions.allows(InventoryObjectAction.EQUIP_QUICK_ACCESS) && foodActions.eligibleQuickSlots().equals(List.of(3)),"La pernera sólo alimenta Quick 3.");
        org.junit.jupiter.api.Assertions.assertTrue(!foodActions.allows(InventoryObjectAction.USE),"La comida almacenada pero no asignada aún no puede usarse.");
        state=InventoryObjectActionService.equipQuickAccess(state,food,3);
        org.junit.jupiter.api.Assertions.assertTrue(InventoryObjectActionPolicy.evaluate(food,state).allows(InventoryObjectAction.USE),"Quick 3 habilita el uso de la comida.");
        state=InventoryObjectActionService.unequipQuickAccess(state,food);
        org.junit.jupiter.api.Assertions.assertTrue(state.quickAccessBar().slots().get(2).isEmpty(),"Desequipar acceso rápido debe limpiar la referencia.");

        var shirtActions=InventoryObjectActionPolicy.evaluate(shirt,state);
        org.junit.jupiter.api.Assertions.assertTrue(shirtActions.eligibleArmorDestinations().stream().anyMatch(d -> d.slot()==EquipmentSlot.CHEST),"La camisa debe exponer destinos estratificados CHEST.");
        var destination=shirtActions.eligibleArmorDestinations().stream().filter(d -> d.slot()==EquipmentSlot.CHEST && d.position()==ArmorLayerPosition.INNER).findFirst().orElseThrow();
        var layered=new LayeredInventoryEquipmentService.State(new LayeredEquipmentState(state.equipment(),state.armorLayout()),state.quickAccessBar(),state.logistics());
        layered=LayeredInventoryEquipmentService.equipArmor(layered,shirt,destination.slot(),destination.position());
        state=new InventoryState(layered.equipment().activeEquipment(),layered.quickAccess(),layered.logistics(),layered.equipment().armorLayout());
        org.junit.jupiter.api.Assertions.assertTrue(state.armorLayout().layers().stream().anyMatch(l -> l.piece()==shirt),"La camisa debe pasar del almacenamiento al layout estratificado.");
        org.junit.jupiter.api.Assertions.assertTrue(state.logistics().compartment(InventoryCompartmentType.CHEST_STORAGE).available(),"Equipar la camisa de trabajo debe crear su almacenamiento CHEST .");
        org.junit.jupiter.api.Assertions.assertTrue(InventoryObjectActionPolicy.evaluate(shirt,state).allows(InventoryObjectAction.UNEQUIP),"Una pieza activa debe exponer Desequipar.");
        layered=LayeredInventoryEquipmentService.unequipArmor(new LayeredInventoryEquipmentService.State(layered.equipment(),layered.quickAccess(),layered.logistics()),shirt);
        state=new InventoryState(layered.equipment().activeEquipment(),layered.quickAccess(),layered.logistics(),layered.equipment().armorLayout());
        org.junit.jupiter.api.Assertions.assertTrue(state.armorLayout().layers().stream().noneMatch(l -> l.piece()==shirt),"Desequipar debe liberar la capa CHEST.");

        state=InventoryObjectActionService.drop(state,food);
        org.junit.jupiter.api.Assertions.assertTrue(InventoryObjectActionPolicy.storedCompartment(food,state.logistics()).isEmpty(),"Tirar debe retirar físicamente el objeto del inventario.");
        org.junit.jupiter.api.Assertions.assertTrue(!InventoryObjectActionPolicy.evaluate(food,state).allows(InventoryObjectAction.INSPECT),"Un objeto tirado deja de pertenecer al inventario.");
    }

    
}
