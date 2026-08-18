package domain.inventory.container;

import domain.inventory.*;import domain.inventory.equipment.*;import domain.inventory.item.armor.*;import domain.inventory.logistics.*;import domain.worldmemory.*;import domain.worldmemory.spatial.*;import java.util.*;
/** Tirar un contenedor mueve la rama completa al mundo; nunca exige vaciarla. */
public final class ContainerWorldDropService {
 private ContainerWorldDropService(){}
 public record ExpanderDrop(InventoryState inventory,DroppedContainer dropped){}
 public record GarmentDrop(LayeredInventoryEquipmentService.State state,DroppedContainer dropped){}
 public static ExpanderDrop dropExpander(InventoryState state,InventoryExpanderItem item,WorldMemory memory,WorldCoordinate at){
  Objects.requireNonNull(state);Objects.requireNonNull(item);Objects.requireNonNull(memory);Objects.requireNonNull(at);
  InventoryCompartmentType type=item.compartmentType();InventoryCompartment c=state.logistics().compartment(type);if(!c.available())throw new IllegalStateException(type.label()+" no está equipado.");
  List<InventoryEntry> contents=List.copyOf(c.entries());ContainerContentsRegistry.attach(item,contents);
  LogisticsState next=state.logistics().withCompartment(InventoryCompartment.empty(type,false));
  // El dorsal arrastra al Rotor retraído que ocupa BACK_HAND; no lo duplica en el grid.
  EquipmentState e=state.equipment();
  if(type==InventoryCompartmentType.DORSAL_ROTOR_SYSTEM){
   var rotor=e.itemAt(EquipmentSlot.BACK_HAND)
           .filter(domain.inventory.item.WeaponItem.class::isInstance)
           .map(domain.inventory.item.WeaponItem.class::cast);
   // Retraído: el Rotor forma parte física del conjunto que cae. Desplegado: el sistema cae solo;
   // el Rotor permanece activo y ya no podrá retraerse hasta recuperar/equipar un dorsal.
   if(rotor.isPresent() && rotor.get().isSheathed()){
    List<InventoryEntry> all=new ArrayList<>(contents);all.add(rotor.get());
    ContainerContentsRegistry.attach(item,all);contents=List.copyOf(all);e=e.withoutItem(EquipmentSlot.BACK_HAND);
   }
  }
  InventoryState inventory=new InventoryState(e,state.quickAccessBar(),next,state.armorLayout());WorldObjectInstanceId id=WorldObjectInstanceId.create();memory.knowledge().rememberPersistentDroppedInstance(id,at);return new ExpanderDrop(inventory,new DroppedContainer(id,item,contents,at));
 }
 public static GarmentDrop dropGarment(LayeredInventoryEquipmentService.State state,ArmorPiece garment,WorldMemory memory,WorldCoordinate at){
  Objects.requireNonNull(state);Objects.requireNonNull(garment);Objects.requireNonNull(memory);Objects.requireNonNull(at);
  EquipmentSlot slot=garment.inventoryCategory().orElseThrow()==ArmorInventoryCategory.LEGGINGS?EquipmentSlot.LEGGINGS:EquipmentSlot.CHEST;
  InventoryCompartmentType storage=slot==EquipmentSlot.LEGGINGS?InventoryCompartmentType.LEGGINGS_STORAGE:InventoryCompartmentType.CHEST_STORAGE;
  InventoryCompartment c=state.logistics().compartment(storage);Map<InventoryEntry,InventoryStorageModule> allocation=InventoryModuleAllocationPolicy.allocate(c);
  List<InventoryEntry> own=new ArrayList<>(),remain=new ArrayList<>();String prefix=garment.name()+" · ";
  for(InventoryEntry entry:c.entries()){InventoryStorageModule module=allocation.get(entry);if(module!=null&&module.label().startsWith(prefix))own.add(entry);else remain.add(entry);}
  ContainerContentsRegistry.attach(garment,own);LayeredEquipmentState eq=state.equipment().unequipArmor(garment);
  LogisticsState logistics=state.logistics().withCompartment(c.withEntries(remain)).synchronizeGarmentStorage(eq.armorLayout());
  var next=new LayeredInventoryEquipmentService.State(eq,state.quickAccess().clearItem(garment),logistics);WorldObjectInstanceId id=WorldObjectInstanceId.create();memory.knowledge().rememberPersistentDroppedInstance(id,at);return new GarmentDrop(next,new DroppedContainer(id,garment,own,at));
 }
}
