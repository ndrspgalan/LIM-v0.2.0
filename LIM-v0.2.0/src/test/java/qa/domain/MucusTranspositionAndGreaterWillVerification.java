package qa.domain;

import domain.ability.*;
import domain.bestiarium.interstice.faerie.*;
import domain.character.CharacterClass;
import domain.character.progression.*;
import domain.character.sheet.CharacterSheet;
import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import domain.runic.*;
import domain.runic.transposition.*;
import presentation.menu.*;
import java.util.*;

/** mucus en mL, Transposición logística, geometría cristalina y arco de Voluntad Mayor. */
public final class MucusTranspositionAndGreaterWillVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
        org.junit.jupiter.api.Assertions.assertTrue(MucusType.BLANCO.maximumReserveMl()==2000 && MucusType.NEGRUZCO.maximumReserveMl()==1,"Reservas mL incorrectas.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusDoctrine.informal(MucusType.NEGRUZCO).contains("¡La verdad está en el mucus!"),"Debe conservar la frase del mucus negruzco.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusDoctrine.CURSE_AND_MUCUS.contains("El mucus no es la Maldición") && MucusDoctrine.CURSE_AND_MUCUS.contains("INTENSIDAD") && MucusDoctrine.CURSE_AND_MUCUS.contains("Frenesí"),"Falta contenido completo de Energía Maldita/mucus.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusDoctrine.TYPES_HEADER.contains("Sin desafío no existe mucus"),"Falta pie doctrinal.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalCatalog.yellow().geometry()==MucusCrystalGeometry.TETRAEDRO,"Amarillento debe ser tetraedro.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalCatalog.greenish().geometry()==MucusCrystalGeometry.OCTAEDRO,"Verdoso debe ser octaedro.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalCatalog.brown().geometry()==MucusCrystalGeometry.CUBO,"Marrón debe ser cubo.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalCatalog.bloodied().geometry()==MucusCrystalGeometry.ESFERA,"Ensangrentado debe ser esfera.");
        org.junit.jupiter.api.Assertions.assertTrue(MucusCrystalCatalog.blackish().geometry()==MucusCrystalGeometry.DODECAEDRO,"Negruzco debe ser dodecaedro.");
        verifyPlacement();
        org.junit.jupiter.api.Assertions.assertTrue(MasteryCollectionScreen.GENERAL_NARRATIVE.contains("estado elevado"),"Falta doctrina general de maestrías.");
        org.junit.jupiter.api.Assertions.assertTrue(RunicMarkCollectionScreen.GENERAL_NARRATIVE.contains("no concede una naturaleza"),"Falta doctrina general de Marcas.");
        org.junit.jupiter.api.Assertions.assertTrue(Doppelganger.NARRATIVE_DESCRIPTION.contains("agresión identitaria"),"Falta doctrina del Doppelgänger.");
        org.junit.jupiter.api.Assertions.assertTrue(DoppelgangerEncounterPolicy.GREATER_WILL_RESOLUTION.contains("ninguna jerarquía"),"Falta resolución de Voluntad Mayor.");
    }

    private static void verifyPlacement() {
        InventoryCompartment legs = modular(InventoryCompartmentType.LEGGINGS_STORAGE, 2, List.of());
        InventoryCompartment chest = modular(InventoryCompartmentType.CHEST_STORAGE, 2, List.of());
        InventoryCompartment pouch = InventoryCompartment.empty(InventoryCompartmentType.LEG_POUCH,false);
        InventoryCompartment band = InventoryCompartment.empty(InventoryCompartmentType.BANDOLIER,false);
        EnumMap<InventoryCompartmentType,InventoryCompartment> map=new EnumMap<>(InventoryCompartmentType.class);
        for(var t:InventoryCompartmentType.values()) map.put(t,InventoryCompartment.empty(t,false));
        map.put(InventoryCompartmentType.LEGGINGS_STORAGE,legs); map.put(InventoryCompartmentType.CHEST_STORAGE,chest);
        map.put(InventoryCompartmentType.LEG_POUCH,pouch); map.put(InventoryCompartmentType.BANDOLIER,band);
        LogisticsState logistics=new LogisticsState(map,PersonalTransportState.none());
        EquipmentState eq=new EquipmentState(Map.of(EquipmentSlot.RUNIC_MARK,RunicMarkCatalog.require(RunicMarkId.TRANSPOSICION)));
        InventoryState inv=new InventoryState(eq,QuickAccessBar.empty(),logistics);
        var r=new TranspositionInventoryService().transpose(MucusType.NEGRUZCO,MucusWallet.of(0,0,0,0,0,1),inv,sheet(),eq);
        org.junit.jupiter.api.Assertions.assertTrue(r.allowed(),"Debe poder transponer.");
        org.junit.jupiter.api.Assertions.assertTrue(r.inventory().logistics().compartment(InventoryCompartmentType.LEGGINGS_STORAGE).entries().size()==1,"Debe priorizar polainas/Quick 1.");
    }
    private static InventoryCompartment modular(InventoryCompartmentType t,int capacity,List<InventoryEntry> entries){
        return InventoryCompartment.modular(t,List.of(InventoryStorageModule.fromGrid(t.label(),new InventoryGridDefinition(capacity,1))),entries);
    }
    private static CharacterSheet sheet(){return CharacterSheet.of(30,30,30,30,30,30,30,30,33);}
    
}
