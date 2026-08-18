package qa.integration;

import domain.inventory.item.misc.MiscellaneousItemCatalog;
import domain.combat.HostileEncounterState;
import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.item.misc.*;
import domain.inventory.item.rangedWeapons.*;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;
import domain.inventory.logistics.*;
import java.util.*;

public final class MaintenanceVerification {
    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("integration")
        void verifiesCanonicalContract(){
        var ranged = RangedWeaponCatalog.all();
        org.junit.jupiter.api.Assertions.assertTrue(ranged.size()==3, " deja exactamente tres armas a distancia convencionales.");
        org.junit.jupiter.api.Assertions.assertTrue(ThrowingWeaponCatalog.all().size()==4, " sustituye los placeholders por 4 armas arrojadizas canónicas.");
        var bow = ranged.stream().filter(w->w.name().equals("Arco Simple Recurvo")).findFirst().orElseThrow();
        for(int i=0;i<1000;i++) bow.registerUse();
        org.junit.jupiter.api.Assertions.assertTrue(Math.abs(bow.currentEffectiveRangeMeters()-102.0)<0.0001, "El arco debe detenerse al 85 %.");
        var resin = new ResinJarItem(3);
        var equipment = new EquipmentState(Map.of(EquipmentSlot.RIGHT_HAND,bow));
        var bar = new QuickAccessBar(List.of(Optional.of(resin),Optional.empty(),Optional.empty(),Optional.empty()));
        var compartments = new EnumMap<InventoryCompartmentType,InventoryCompartment>(InventoryCompartmentType.class);
        compartments.put(InventoryCompartmentType.LEGGINGS_STORAGE,new InventoryCompartment(InventoryCompartmentType.LEGGINGS_STORAGE,true,new InventoryGridDefinition(2,7),List.of(resin),Optional.empty()));
        compartments.put(InventoryCompartmentType.LEG_POUCH,InventoryCompartment.empty(InventoryCompartmentType.LEG_POUCH,false));
        compartments.put(InventoryCompartmentType.BANDOLIER,InventoryCompartment.empty(InventoryCompartmentType.BANDOLIER,false));
        compartments.put(InventoryCompartmentType.BACKPACK,InventoryCompartment.empty(InventoryCompartmentType.BACKPACK,false));
        
        var inventory = new InventoryState(equipment,bar,new LogisticsState(compartments,PersonalTransportState.none()));
        var service = new domain.maintenance.MaintenanceService();
        org.junit.jupiter.api.Assertions.assertTrue(service.repairRanged(bow,resin,inventory,new HostileEncounterState()).successful(), "La resina debe reparar el arco equipado.");
        org.junit.jupiter.api.Assertions.assertTrue(resin.currentUses()==2 && bow.currentEffectiveRangeMeters()==bow.originalEffectiveRangeMeters(), "Debe consumir un uso y restaurar alcance.");
        var encounter = new HostileEncounterState(); encounter.begin(); bow.registerUse();
        org.junit.jupiter.api.Assertions.assertTrue(service.repairRanged(bow,resin,inventory,encounter).successful(),
                ": el combate ya no bloquea por sí mismo el mantenimiento si el objeto cumple su política de uso.");
        var coolant = new CoolantBottleItem(0); var water=MiscellaneousItemCatalog.waterskin(); var mead=MiscellaneousItemCatalog.mead();
        org.junit.jupiter.api.Assertions.assertTrue(service.refillCoolant(coolant,water,mead,new PortableLaboratoryItem()), "La mezcla debe producir un uso.");
        org.junit.jupiter.api.Assertions.assertTrue(coolant.currentUses()==1 && water.currentUses()==4 && mead.currentUses()==1, "La receta debe consumir agua e hidromiel.");
    }
    
}
