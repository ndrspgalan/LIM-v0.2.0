package domain.inventory.container;

import domain.inventory.*;import domain.inventory.equipment.*;import domain.inventory.item.*;import domain.inventory.item.armor.*;import domain.inventory.logistics.*;import java.util.*;
/** Un contenedor cargado nunca entra en un grid ordinario: mundo -> ranura compatible directamente. */
public final class ContainerEquipService {
 private ContainerEquipService(){}
 public static InventoryState equipExpanderFromWorld(InventoryState state,InventoryExpanderItem carrier){
  Objects.requireNonNull(state);Objects.requireNonNull(carrier);InventoryCompartmentType type=carrier.compartmentType();
  if(state.logistics().compartment(type).available())throw new IllegalStateException(type.label()+" ya está equipado.");
  if(PersonalTransportSaddlebagPolicy.isSaddlebagType(type)){
   PersonalTransportType transport=PersonalTransportSaddlebagPolicy.transportFor(type).orElseThrow();
   if(!state.logistics().personalTransport().unit(transport).physicallyPresent())
    throw new IllegalStateException("Las alforjas sólo pueden equiparse interactuando con E junto a su transporte físicamente presente.");
  }
  List<InventoryEntry> contents=ContainerContentsRegistry.contentsOf(carrier);
  LogisticsState logistics=state.logistics();EquipmentState equipment=state.equipment();
  if(type==InventoryCompartmentType.DORSAL_ROTOR_SYSTEM){
   if(logistics.compartment(InventoryCompartmentType.BACKPACK).available())throw new IllegalStateException("La mochila y el sistema dorsal son incompatibles.");
   WeaponItem rotor=contents.stream().filter(WeaponItem.class::isInstance).map(WeaponItem.class::cast).filter(w->w.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)).findFirst().orElse(null);
   if(contents.size()>(rotor==null?0:1))throw new IllegalStateException("El sistema dorsal sólo puede transportar el Rotor.");
   logistics=logistics.withCompartment(InventoryCompartment.empty(type,true));
   if(rotor!=null){if(!rotor.isSheathed())new domain.inventory.item.meleeWeapons.RotorRetractionPolicy().completeRetraction(rotor);equipment=equipment.withItem(EquipmentSlot.BACK_HAND,rotor);}
  }else{
   InventoryCompartment next=new InventoryCompartment(type,true,type.grid(),contents,Optional.empty());logistics=logistics.withCompartment(next);
  }
  ContainerContentsRegistry.clear(carrier);return new InventoryState(equipment,state.quickAccessBar(),logistics,state.armorLayout());
 }
 public static LayeredInventoryEquipmentService.State equipGarmentFromWorld(LayeredInventoryEquipmentService.State state,ArmorPiece garment,EquipmentSlot slot,ArmorLayerPosition position){
  List<InventoryEntry> contents=ContainerContentsRegistry.contentsOf(garment);LayeredEquipmentState eq=state.equipment().equipArmor(slot,position,garment);LogisticsState logistics=state.logistics().synchronizeGarmentStorage(eq.armorLayout());
  InventoryCompartmentType type=slot==EquipmentSlot.LEGGINGS?InventoryCompartmentType.LEGGINGS_STORAGE:InventoryCompartmentType.CHEST_STORAGE;InventoryCompartment c=logistics.compartment(type);
  ArrayList<InventoryEntry> merged=new ArrayList<>(c.entries());merged.addAll(contents);logistics=logistics.withCompartment(c.withEntries(merged));ContainerContentsRegistry.clear(garment);return new LayeredInventoryEquipmentService.State(eq,state.quickAccess(),logistics);
 }
}
