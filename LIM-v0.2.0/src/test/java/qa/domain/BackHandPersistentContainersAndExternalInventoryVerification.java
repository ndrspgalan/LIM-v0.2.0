package qa.domain;

import application.ExternalInventoryAccessService;
import domain.combat.ai.inventory.external.*;
import domain.inventory.*;
import domain.inventory.container.*;
import domain.inventory.equipment.*;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.*;
import domain.inventory.logistics.*;
import java.util.*;

/**
 * QA acumulado . Se añade al proyecto, pero la suite no debe ejecutarse en el bucle normal de iteración.
 */
public final class BackHandPersistentContainersAndExternalInventoryVerification {
 @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Tag("domain")
        void verifiesCanonicalContract(){
  backHandContract(); rotorCannotSheatheWithoutDorsal(); loadedContainersDoNotEnterOrdinaryGrid();
  containerInteractionRotation(); externalInventoryVisibility(); externalSessionClosesWithPa();
  cycleProtection(); expanderCoverage();
 }

 private static void backHandContract(){
  WeaponItem rotor=MeleeWeaponCatalog.espadonDeRotor();
  new RotorRetractionPolicy().completeRetraction(rotor);
  InventoryState base=withDorsal();
  InventoryState equipped=RotorBackHandService.equipRetractedRotor(base,rotor);
  org.junit.jupiter.api.Assertions.assertTrue(equipped.equipment().itemAt(EquipmentSlot.BACK_HAND).filter(i->i==rotor).isPresent(),"Rotor debe ocupar BACK_HAND.");
  org.junit.jupiter.api.Assertions.assertTrue(EquipmentState.effectiveDominantHand(EquipmentSlot.BACK_HAND)==EquipmentSlot.RIGHT_HAND,"BACK_HAND hereda RIGHT_HAND.");
  InventoryState deployed=RotorBackHandService.deploy(equipped);
  org.junit.jupiter.api.Assertions.assertTrue(RotorBackHandService.active(deployed),"Rotor debe poder desplegarse sin abandonar BACK_HAND.");
  org.junit.jupiter.api.Assertions.assertTrue(deployed.equipment().itemAt(EquipmentSlot.BACK_HAND).filter(i->i==rotor).isPresent(),"BACK_HAND persiste durante despliegue.");
  InventoryState retracted=RotorBackHandService.retract(deployed);
  org.junit.jupiter.api.Assertions.assertTrue(rotor.isSheathed() && retracted.equipment().itemAt(EquipmentSlot.BACK_HAND).isPresent(),"Rotor vuelve a estado retraído.");
 }

 private static void rotorCannotSheatheWithoutDorsal(){
  WeaponItem rotor=MeleeWeaponCatalog.espadonDeRotor();
  if(rotor.isSheathed()) new RotorRetractionPolicy().completeDeployment(rotor);
  boolean conventional=false;
  try{rotor.stowForHandlingTransition();}catch(IllegalStateException expected){conventional=true;}
  org.junit.jupiter.api.Assertions.assertTrue(conventional,"DE ROTOR no admite envainado convencional.");
  org.junit.jupiter.api.Assertions.assertTrue(!RotorBackHandService.canRetract(InventoryState.emptyWithoutPersonalTransport(),rotor),"Sin dorsal no puede retraerse.");
 }

 private static void loadedContainersDoNotEnterOrdinaryGrid(){
  InventoryExpanderItem backpack=InventoryExpanderCatalog.create(InventoryCompartmentType.BACKPACK);
  ContainerContentsRegistry.attach(backpack,List.of(MeleeWeaponCatalog.daga()));
  InventoryAdmissionResult r=new InventoryAutoPlacementService().fromWorld(InventoryState.emptyWithoutPersonalTransport(),backpack);
  org.junit.jupiter.api.Assertions.assertTrue(!r.accepted() && r.message().contains("ranura de equipamiento"),"Un expansor cargado no puede anidarse en grid ordinario.");
 }

 private static void containerInteractionRotation(){
  var actions=ContainerInteractionPolicy.actions(InventoryExpanderCatalog.create(InventoryCompartmentType.LEG_POUCH),true);
  org.junit.jupiter.api.Assertions.assertTrue(actions.equals(List.of(ContainerInteractionPolicy.Action.INSPECT_OBJECT,ContainerInteractionPolicy.Action.EQUIP)),"E/Q debe ofrecer inspeccionar/equipar.");
  org.junit.jupiter.api.Assertions.assertTrue(ContainerInteractionPolicy.rotateWithQ(actions,ContainerInteractionPolicy.Action.INSPECT_OBJECT)==ContainerInteractionPolicy.Action.EQUIP,"Q rota a EQUIPAR.");
 }

 private static void externalInventoryVisibility(){
  ExternalInventoryAccessPolicy p=new ExternalInventoryAccessPolicy();
  org.junit.jupiter.api.Assertions.assertTrue(p.canOpen(ExternalInventoryOwnerState.DEAD,false,false),"Muerto: inspección segura.");
  org.junit.jupiter.api.Assertions.assertTrue(p.canOpen(ExternalInventoryOwnerState.UNCONSCIOUS,false,false),"Inconsciente: inspección segura.");
  org.junit.jupiter.api.Assertions.assertTrue(!p.canOpen(ExternalInventoryOwnerState.SLEEPING,false,false),"Dormido visible: opción ausente.");
  org.junit.jupiter.api.Assertions.assertTrue(!p.canOpen(ExternalInventoryOwnerState.CONSCIOUS,false,false),"Vivo visible: opción ausente.");
  org.junit.jupiter.api.Assertions.assertTrue(p.canOpen(ExternalInventoryOwnerState.SLEEPING,true,false),"Dormido + invisibilidad: opción disponible.");
  org.junit.jupiter.api.Assertions.assertTrue(p.canOpen(ExternalInventoryOwnerState.CONSCIOUS,true,false),"Vivo + invisibilidad: opción disponible.");
  org.junit.jupiter.api.Assertions.assertTrue(p.canOpen(ExternalInventoryOwnerState.DEAD,false,true),"+: un cadáver puede inspeccionarse también durante encuentro hostil.");
 }

 private static void externalSessionClosesWithPa(){
  SharedInventorySession s=new ExternalInventoryAccessService().open(
          InventoryState.emptyWithoutPersonalTransport(),ExternalInventoryOwnerState.CONSCIOUS,true,false);
  org.junit.jupiter.api.Assertions.assertTrue(s.open(),"Sesión invisible debe abrirse.");
  org.junit.jupiter.api.Assertions.assertTrue(!s.synchronizeInvisibility(0.0,true) && !s.open(),"PA=0 debe expulsar automáticamente de la inspección.");
 }

 private static void cycleProtection(){
  InventoryExpanderItem a=InventoryExpanderCatalog.create(InventoryCompartmentType.BACKPACK);
  InventoryExpanderItem b=InventoryExpanderCatalog.create(InventoryCompartmentType.BANDOLIER);
  ContainerContentsRegistry.attach(a,List.of(b));
  boolean rejected=false;
  try{ContainerContentsRegistry.attach(b,List.of(a));}catch(IllegalArgumentException expected){rejected=true;}
  org.junit.jupiter.api.Assertions.assertTrue(rejected,"La contención cíclica debe rechazarse.");
 }

 private static void expanderCoverage(){
  org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderCatalog.types().size()==10,"Deben existir diez expansores físicos independientes.");
  org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderCatalog.types().contains(InventoryCompartmentType.ARROW_QUIVER),"Carcaj es expansor físico.");
  org.junit.jupiter.api.Assertions.assertTrue(InventoryExpanderCatalog.types().contains(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM),"Sistema dorsal es expansor físico.");
 }

 private static InventoryState withDorsal(){
  EnumMap<InventoryCompartmentType,InventoryCompartment> m=new EnumMap<>(InventoryCompartmentType.class);
  for(var t:InventoryCompartmentType.values())m.put(t,InventoryCompartment.empty(t,false));
  m.put(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM,InventoryCompartment.empty(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM,true));
  return new InventoryState(EquipmentState.empty(),QuickAccessBar.empty(),new LogisticsState(m,PersonalTransportState.none()));
 }
 
}
