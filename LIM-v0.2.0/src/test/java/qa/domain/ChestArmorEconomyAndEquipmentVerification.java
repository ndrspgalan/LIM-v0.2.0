package qa.domain;

import domain.economy.*;
import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import domain.inventory.logistics.*;
import java.util.*;

public final class ChestArmorEconomyAndEquipmentVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  coverage(); middleIsOneSlot(); rename(); ogc(); equipmentSynchronization(); economics();
 }
 static void coverage(){
  Set<String> n=new LinkedHashSet<>();
  ArmorCatalog.allInnerChestGarments().forEach(a->n.add(a.name()));
  ArmorCatalog.allMiddleChest().forEach(a->n.add(a.name()));
  ArmorCatalog.allOuterChestGarments().forEach(a->n.add(a.name()));
  org.junit.jupiter.api.Assertions.assertTrue(n.size()==58,"CHEST debe contener 58 piezas.");
  org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.allMiddleChest().size()==23,"MIDDLE debe unificar 13 LIGHT + 10 MEDIUM/HEAVY.");
  org.junit.jupiter.api.Assertions.assertTrue(ChestArmorEconomicCatalog.all().keySet().equals(n),"Cobertura económica CHEST incompleta.");
 }
 static void middleIsOneSlot(){
  ArmorPiece light=ArmorCatalog.allMiddleChestGarments().get(0);
  ArmorPiece medium=ArmorCatalog.allProtectiveMiddleChest().stream().filter(a->a.materialClass()==ArmorMaterialClass.MEDIUM).findFirst().orElseThrow();
  ArmorPiece heavy=ArmorCatalog.allProtectiveMiddleChest().stream().filter(a->a.materialClass()==ArmorMaterialClass.HEAVY).findFirst().orElseThrow();
  ArmorEquipmentLayout a=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,light);
  rejects(()->a.equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,medium),"LIGHT MIDDLE y MEDIUM no pueden coexistir.");
  ArmorEquipmentLayout b=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,medium);
  rejects(()->b.equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,heavy),"MEDIUM y HEAVY no pueden coexistir.");
  ArmorEquipmentLayout c=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,heavy);
  rejects(()->c.equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,light),"HEAVY y LIGHT MIDDLE no pueden coexistir.");
 }
 static void rename(){
  org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.allMiddleChest().stream().anyMatch(a->a.name().equals("Chaqueta de Aeronauta V881")),"Falta Chaqueta de Aeronauta.");
  org.junit.jupiter.api.Assertions.assertTrue(ArmorCatalog.allMiddleChest().stream().noneMatch(a->a.name().toLowerCase().contains("aviador")),"No debe sobrevivir el nombre de aviador.");
 }
 static void ogc(){
  for(String n:List.of("Coraza del Guerrero de Ébano","Brazales del Guerrero de Ébano",
          "Coraza del Guerrero de Ébano V881","Brazal izquierdo del Guerrero de Ébano V881")){
   var v=EbonyWarriorArmorEconomicPolicy.valuation(n);
   org.junit.jupiter.api.Assertions.assertTrue(v.status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING,"Debe quedar pendiente OGC: "+n);
   org.junit.jupiter.api.Assertions.assertTrue(v.priceValeritas().isEmpty() && !v.ordinarilySellable(),"No debe fingirse precio: "+n);
  }
  org.junit.jupiter.api.Assertions.assertTrue(ChestArmorEconomicCatalog.valuation("Coraza del Guerrero de Ébano").status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING,"CHEST debe delegar OGC histórica.");
 }
 static void equipmentSynchronization(){
  // Contrato de identidad: una pieza no puede estar simultáneamente en layout y cuadrícula.
  ArmorPiece chest=ArmorCatalog.allMiddleChestGarments().get(0);
  ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty().equip(EquipmentSlot.CHEST,ArmorLayerPosition.MIDDLE,chest);
  LayeredEquipmentState eq=new LayeredEquipmentState(EquipmentState.empty(),layout);
  LogisticsState empty=LogisticsState.emptyWithoutPersonalTransport();
  LayeredInventoryEquipmentService.validateNoDuplicateOwnership(eq,empty);

  // LEFT/RIGHT forman parte de la misma comprobación.
  var dagger=MeleeWeaponCatalog.daga();
  EquipmentState hand=EquipmentState.empty().withItem(EquipmentSlot.RIGHT_HAND,dagger);
  LayeredEquipmentState handEq=new LayeredEquipmentState(hand,ArmorEquipmentLayout.empty());
  LayeredInventoryEquipmentService.validateNoDuplicateOwnership(handEq,empty);

  // Rotor dorsal: almacenado no es una tercera mano; la política lo prohíbe si comparte identidad con una mano.
  org.junit.jupiter.api.Assertions.assertTrue(new DorsalRotorTransportPolicy().canDock(MeleeWeaponCatalog.espadonDeRotor()),"El contrato dorsal del Rotor debe conservarse.");
 }
 static void economics(){
  long pending=ChestArmorEconomicCatalog.all().values().stream().filter(v->v.status()==EconomicValuationStatus.OGC_APPRAISAL_PENDING).count();
  org.junit.jupiter.api.Assertions.assertTrue(pending==2,"CHEST debe tener exactamente dos corazas pendientes OGC.");
  org.junit.jupiter.api.Assertions.assertTrue(ChestArmorEconomicCatalog.valuation("Delantal de Taller V881").goodType()==EconomicGoodType.SOCIAL_INTEREST,"Delantal es equipo profesional.");
  org.junit.jupiter.api.Assertions.assertTrue(ChestArmorEconomicCatalog.valuation("Coraza de Caballero").goodType()==EconomicGoodType.PRIVATE_USE,"Coraza dedicada es privativa.");
  org.junit.jupiter.api.Assertions.assertTrue(ChestArmorEconomicCatalog.valuation("Gambesón V881").priceValeritas().orElseThrow()>ChestArmorEconomicCatalog.valuation("Chaleco V881").priceValeritas().orElseThrow(),"Gambesón debe reflejar manufactura multicapa.");
 }
 static void rejects(Runnable r,String m){try{r.run();throw new IllegalStateException(m);}catch(IllegalArgumentException expected){}}
 
}
