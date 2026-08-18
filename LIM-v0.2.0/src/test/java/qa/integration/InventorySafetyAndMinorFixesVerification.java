package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.inventory.*;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ammunition.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import domain.character.progression.*;
import domain.character.sheet.CharacterSheet;
import domain.runic.transposition.*;

import java.util.*;

public final class InventorySafetyAndMinorFixesVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        allAvailableSlots();
        automaticRotation();
        manualRotation();
        commonNoSpaceException();
        whiteMucusCap();
        tinderArrowAutoIgnition();
    }

    private static void allAvailableSlots(){
        InventoryAutoPlacementService s=new InventoryAutoPlacementService();
        org.junit.jupiter.api.Assertions.assertTrue(s.priority().containsAll(List.of(
                InventoryCompartmentType.LEGGINGS_STORAGE,InventoryCompartmentType.CHEST_STORAGE,
                InventoryCompartmentType.LEG_POUCH,InventoryCompartmentType.BANDOLIER,
                InventoryCompartmentType.BACKPACK,InventoryCompartmentType.ARROW_QUIVER)),
                "La comprobación debe abarcar todos los almacenamientos ordinarios disponibles.");
        org.junit.jupiter.api.Assertions.assertTrue(s.priority().stream().noneMatch(PersonalTransportSaddlebagPolicy::isSaddlebagType),
                "Las alforjas sólo se abren por interacción E con la montura.");
    }

    private static void automaticRotation(){
        InventoryEntry item=new InventoryEntry("Rectángulo M4531","Prueba de giro automático",.1,new InventoryFootprint(3,2),List.of());
        InventoryState inv=withOnly(InventoryCompartmentType.LEG_POUCH,new InventoryGridDefinition(2,3),List.of());
        InventoryAdmissionResult r=new InventoryAutoPlacementService().fromWorld(inv,item);
        org.junit.jupiter.api.Assertions.assertTrue(r.accepted(),"Debe probar la orientación alternativa antes de rechazar.");
        org.junit.jupiter.api.Assertions.assertTrue(item.footprint().equals(new InventoryFootprint(2,3)),"La orientación elegida debe persistir en la instancia.");
        org.junit.jupiter.api.Assertions.assertTrue(item.inventoryOrientation()==InventoryOrientation.ROTATED_90,"La instancia debe recordar el giro.");
    }

    private static void manualRotation(){
        InventoryEntry item=new InventoryEntry("Rectángulo manual","Prueba de giro manual",.1,new InventoryFootprint(2,3),List.of());
        InventoryState inv=withOnly(InventoryCompartmentType.LEG_POUCH,new InventoryGridDefinition(2,3),List.of(item));
        boolean failed=false;
        try{ InventoryObjectActionService.rotate90(inv,item); }
        catch(IllegalStateException e){failed=InventoryAutoPlacementService.NO_SPACE_MESSAGE.equals(e.getMessage());}
        org.junit.jupiter.api.Assertions.assertTrue(failed,"Girar a una orientación que no cabe debe lanzar la excepción común.");
        org.junit.jupiter.api.Assertions.assertTrue(item.inventoryOrientation()==InventoryOrientation.DEFAULT,"Un giro fallido no puede alterar orientación.");
    }

    private static void commonNoSpaceException(){
        InventoryEntry huge=new InventoryEntry("Objeto enorme M4531","No cabe",1,new InventoryFootprint(9,9),List.of());
        InventoryState empty=InventoryState.emptyWithoutPersonalTransport();
        for(InventoryAdmissionSource source:List.of(InventoryAdmissionSource.WORLD_PICKUP,InventoryAdmissionSource.TRANSACTION,
                InventoryAdmissionSource.PILLAGE,InventoryAdmissionSource.CRAFTING)){
            boolean failed=false;
            try{new InventoryAutoPlacementService().requireAdmit(empty,huge,source);}
            catch(IllegalStateException e){failed=InventoryAutoPlacementService.NO_SPACE_MESSAGE.equals(e.getMessage());}
            org.junit.jupiter.api.Assertions.assertTrue(failed,"Todo flujo debe fallar con el mismo mensaje: "+source);
        }
    }

    private static void whiteMucusCap(){
        org.junit.jupiter.api.Assertions.assertTrue(MucusTearItem.MAXIMUM_AGGREGATE_USES==100,"Una Lágrima no puede superar 100 mL.");
        boolean rejected=false;
        try{new MucusTearItem(101);}catch(IllegalArgumentException e){rejected=true;}
        org.junit.jupiter.api.Assertions.assertTrue(rejected,"101 mL en una sola Lágrima debe ser imposible.");
        org.junit.jupiter.api.Assertions.assertTrue(new MucusTearItem(100).weightKg()==.100,"100 mL = 100 g.");
    }

    private static void tinderArrowAutoIgnition(){
        UtilityObjectItem amadou=MiscellaneousItemCatalog.amadou();
        UtilityObjectItem flint=MiscellaneousItemCatalog.flint();
        TinderArrowItem arrow=AmmunitionCatalog.tinderArrow();
        InventoryState inv=withOnly(InventoryCompartmentType.LEG_POUCH,new InventoryGridDefinition(8,8),List.of(amadou,flint));
        int a=amadou.currentUses(), f=flint.currentUses();
        org.junit.jupiter.api.Assertions.assertTrue(new TinderArrowIgnitionService().igniteIfResourcesAvailable(arrow,inv),"Debe encenderse automáticamente con ambos recursos.");
        org.junit.jupiter.api.Assertions.assertTrue(arrow.lit(),"La flecha debe quedar encendida.");
        org.junit.jupiter.api.Assertions.assertTrue(amadou.currentUses()==a-1 && flint.currentUses()==f-1,"Debe consumir exactamente un uso de Amadou y Pedernal.");
    }

    private static InventoryState withOnly(InventoryCompartmentType type, InventoryGridDefinition grid, List<InventoryEntry> entries){
        EnumMap<InventoryCompartmentType,InventoryCompartment> m=new EnumMap<>(InventoryCompartmentType.class);
        for(var t:InventoryCompartmentType.values())m.put(t,InventoryCompartment.empty(t,false));
        m.put(type,InventoryCompartment.modular(type,List.of(InventoryStorageModule.fromGrid("M4531",grid)),entries));
        return new InventoryState(EquipmentState.empty(),QuickAccessBar.empty(),new LogisticsState(m,PersonalTransportState.none()));
    }

    
}
