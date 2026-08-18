package qa.domain;

import domain.inventory.*;
import domain.inventory.logistics.*;
import domain.runic.*;
import java.util.*;

/** geometría rúnica, Parhelio y admisión automática universal Quick 1→4. */
public final class RunicGeometryAndInventoryAdmissionVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        verifyRunicGeometryAndNarratives();
        verifyUniversalAdmission();
    }

    private static void verifyRunicGeometryAndNarratives(){
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.PARHELIO).geometry()==RunicMarkGeometry.PARHELIO_TRIPLE_SOLAR,"Forma Parhelio incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.ROSA_DE_LOS_VIENTOS).geometry()==RunicMarkGeometry.ROSA_OCHO_RUMBOS,"Forma Rosa incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.RESONANCIA).geometry()==RunicMarkGeometry.RESONANCIA_CONCENTRICA,"Forma Resonancia incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.SILENCIO).geometry()==RunicMarkGeometry.SILENCIO_ANILLO_FONEMICO_ROTO,"Forma Silencio incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.ESPEJO).geometry()==RunicMarkGeometry.ESPEJO_ROMBO_BILATERAL,"Forma Espejo incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.VOTO_VINCULANTE).geometry()==RunicMarkGeometry.VOTO_HEXAGONO_LIGADO,"Forma Voto incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.TRANSPOSICION).geometry()==RunicMarkGeometry.TRANSPOSICION_CRISOL_CONCENTRICO,"Forma Transposición incorrecta.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.PARHELIO).statistics().contains("DÍA | PV REGEN x3"),"Parhelio día debe ser x3.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.PARHELIO).statistics().contains("TARDE | PV REGEN x2,2"),"Parhelio tarde debe ser x2,2.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.SILENCIO).narrativeDescription().contains("fonemas"),"Silencio debe explicar derivación fonémica.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.ESPEJO).narrativeDescription().contains("fotografía"),"Espejo debe conservar doctrina imagen/reflejo.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.VOTO_VINCULANTE).narrativeDescription().contains("Ya has firmado"),"Voto debe cerrar el arco de FE.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCatalog.require(RunicMarkId.TRANSPOSICION).narrativeDescription().contains("reliquia esperando a ser transpuesta"),"Transposición debe conservar su frase identitaria.");
    }

    private static void verifyUniversalAdmission(){
        InventoryAutoPlacementService service=new InventoryAutoPlacementService();
        org.junit.jupiter.api.Assertions.assertTrue(service.priority().equals(List.of(InventoryCompartmentType.LEGGINGS_STORAGE,InventoryCompartmentType.CHEST_STORAGE,InventoryCompartmentType.LEG_POUCH,InventoryCompartmentType.BANDOLIER,InventoryCompartmentType.BACKPACK,InventoryCompartmentType.ARROW_QUIVER)),"Prioridad universal incorrecta .");
        InventoryState base=inventory(true,true,true,true);
        InventoryEntry item=new InventoryEntry("Prueba","Objeto de prueba",0.1,new InventoryFootprint(1,1),List.of());
        var world=service.fromWorld(base,item);
        org.junit.jupiter.api.Assertions.assertTrue(world.accepted()&&world.destination().orElseThrow()==InventoryCompartmentType.LEGGINGS_STORAGE,"Recogida del mundo debe empezar por polainas.");
        InventoryEntry item2=new InventoryEntry("Prueba 2","Objeto de prueba",0.1,new InventoryFootprint(1,1),List.of());
        var trade=service.fromTransaction(world.inventory(),item2);
        org.junit.jupiter.api.Assertions.assertTrue(trade.accepted(),"Transacción debe usar la misma admisión.");
        InventoryEntry item3=new InventoryEntry("Prueba 3","Objeto de prueba",0.1,new InventoryFootprint(1,1),List.of());
        org.junit.jupiter.api.Assertions.assertTrue(service.fromDialogue(trade.inventory(),item3).accepted(),"Diálogo debe usar la misma admisión.");
    }

    private static InventoryState inventory(boolean legs,boolean chest,boolean pouch,boolean band){
        EnumMap<InventoryCompartmentType,InventoryCompartment> map=new EnumMap<>(InventoryCompartmentType.class);
        for(var t:InventoryCompartmentType.values())map.put(t,InventoryCompartment.empty(t,false));
        if(legs)map.put(InventoryCompartmentType.LEGGINGS_STORAGE,modular(InventoryCompartmentType.LEGGINGS_STORAGE));
        if(chest)map.put(InventoryCompartmentType.CHEST_STORAGE,modular(InventoryCompartmentType.CHEST_STORAGE));
        if(pouch)map.put(InventoryCompartmentType.LEG_POUCH,modular(InventoryCompartmentType.LEG_POUCH));
        if(band)map.put(InventoryCompartmentType.BANDOLIER,modular(InventoryCompartmentType.BANDOLIER));
        return new InventoryState(domain.inventory.equipment.EquipmentState.empty(),QuickAccessBar.empty(),new LogisticsState(map,PersonalTransportState.none()));
    }
    private static InventoryCompartment modular(InventoryCompartmentType t){return InventoryCompartment.modular(t,List.of(InventoryStorageModule.fromGrid(t.label(),new InventoryGridDefinition(8,8))),List.of());}
    
}
