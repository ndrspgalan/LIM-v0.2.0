package application.save;
import domain.inventory.*; import domain.inventory.equipment.*; import domain.inventory.item.*; import domain.inventory.item.armor.*; import domain.inventory.item.firearms.*; import domain.inventory.item.misc.*; import domain.inventory.item.rangedWeapons.*; import domain.inventory.logistics.*; import domain.save.snapshot.*; import domain.worldmemory.spatial.WorldCoordinate; import java.util.*;
/** Reconstruye topología e instancias mutables desde el snapshot . */
public final class InventorySnapshotHydrator {
 private InventorySnapshotHydrator(){}
 public static InventoryState restore(InventorySnapshot snapshot,TransportSnapshot transport){
  Map<String,InventoryEntry> items=new LinkedHashMap<>();for(InventoryItemSnapshot s:snapshot.items()){InventoryEntry e=CanonicalInventoryEntryResolver.require(s.name());restoreMutable(e,s);items.put(s.key(),e);}
  EnumMap<EquipmentSlot,InventoryEntry> equipped=new EnumMap<>(EquipmentSlot.class);snapshot.equipmentSlots().forEach((slot,key)->equipped.put(EquipmentSlot.valueOf(slot),require(items,key)));
  EquipmentState equipment=new EquipmentState(equipped);
  QuickAccessBar quick=QuickAccessBar.empty();for(var e:snapshot.quickAccessSlots().entrySet())quick=quick.assign(e.getKey(),require(items,e.getValue()));
  PersonalTransportState pts=transportState(transport);
  EnumMap<InventoryCompartmentType,InventoryCompartment> comps=new EnumMap<>(InventoryCompartmentType.class);
  for(InventoryCompartmentType type:InventoryCompartmentType.values()){
   CompartmentSnapshot c=snapshot.compartments().get(type.name());if(c==null){comps.put(type,InventoryCompartment.empty(type,false));continue;}
   List<InventoryEntry> entries=c.itemKeys().stream().map(k->require(items,k)).toList();
   List<InventoryStorageModule> modules=c.storageModules().stream().map(m->InventoryStorageModule.fromGrid(m.label(),new InventoryGridDefinition(m.verticalSlots(),m.horizontalSlots()))).toList();
   InventoryCompartment restored=modules.isEmpty()?InventoryCompartment.empty(type,c.available()):InventoryCompartment.modular(type,modules,entries);
   if(!c.externalHelmetKey().isBlank())restored=restored.withExternalHelmet((ArmorPiece)require(items,c.externalHelmetKey()));comps.put(type,restored);
  }
  LogisticsState logistics=new LogisticsState(comps,pts);
  ArmorEquipmentLayout layout=restoreArmorLayout(snapshot,equipment,items);
  return new InventoryState(equipment,quick,logistics,layout);
 }

 private static ArmorEquipmentLayout restoreArmorLayout(InventorySnapshot snapshot,EquipmentState equipment,Map<String,InventoryEntry> items){
  if(!snapshot.armorLayers().isEmpty()){
   ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty();
   for(ArmorLayerSnapshot layer:snapshot.armorLayers()){
    InventoryEntry entry=require(items,layer.itemKey());
    if(!(entry instanceof ArmorPiece piece))throw new IllegalArgumentException("La capa persistida no referencia una ArmorPiece: "+layer.itemKey());
    layout=layout.equip(EquipmentSlot.valueOf(layer.equipmentSlot()),ArmorLayerPosition.valueOf(layer.layerPosition()),piece);
   }
   return layout;
  }
  // Fallback de compatibilidad para snapshots anteriores a , que sólo persistían una pieza plana por ranura.
  ArmorEquipmentLayout layout=ArmorEquipmentLayout.empty();
  for(EquipmentSlot slot:List.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.BRACERS,EquipmentSlot.LEGGINGS,EquipmentSlot.FEET)){
   ArmorPiece piece=equipment.armorAt(slot).orElse(null); if(piece==null) continue;
   ArmorLayerPosition[] preferred=preferredPositions(slot,piece);
   boolean restored=false;
   for(ArmorLayerPosition position:preferred){try{layout=layout.equip(slot,position,piece);restored=true;break;}catch(IllegalArgumentException ignored){}}
   if(!restored) throw new IllegalArgumentException("No se pudo restaurar la capa de "+piece.name()+" en "+slot+".");
  }
  return layout;
 }
 private static ArmorLayerPosition[] preferredPositions(EquipmentSlot slot,ArmorPiece piece){
  if(slot==EquipmentSlot.HEAD||slot==EquipmentSlot.BRACERS)return new ArmorLayerPosition[]{ArmorLayerPosition.UNSPECIFIED};
  if(slot==EquipmentSlot.FEET)return piece.feetLayer().orElse(FeetLayer.OUTER)==FeetLayer.INNER
    ?new ArmorLayerPosition[]{ArmorLayerPosition.INNER}:new ArmorLayerPosition[]{ArmorLayerPosition.OUTER};
  if(slot==EquipmentSlot.CHEST){if(piece.innerChestLayer().isPresent())return new ArmorLayerPosition[]{ArmorLayerPosition.INNER};
   if(piece.materialClass()==ArmorMaterialClass.MEDIUM||piece.materialClass()==ArmorMaterialClass.HEAVY)return new ArmorLayerPosition[]{ArmorLayerPosition.MIDDLE};
   return new ArmorLayerPosition[]{ArmorLayerPosition.MIDDLE,ArmorLayerPosition.OUTER,ArmorLayerPosition.INNER};}
  if(slot==EquipmentSlot.LEGGINGS){if(piece.innerLeggingsLayer().isPresent())return new ArmorLayerPosition[]{ArmorLayerPosition.INNER};
   if(piece.materialClass()==ArmorMaterialClass.MEDIUM||piece.materialClass()==ArmorMaterialClass.HEAVY)return new ArmorLayerPosition[]{ArmorLayerPosition.OUTER};
   return new ArmorLayerPosition[]{ArmorLayerPosition.MIDDLE,ArmorLayerPosition.INNER};}
  return new ArmorLayerPosition[]{ArmorLayerPosition.UNSPECIFIED};
 }

 private static PersonalTransportState transportState(TransportSnapshot snapshot){EnumMap<PersonalTransportType,PersonalTransportUnitState> units=new EnumMap<>(PersonalTransportType.class);for(TransportUnitSnapshot s:snapshot.units()){PersonalTransportType type=PersonalTransportType.valueOf(s.type());WorldCoordinate c=s.x()==null?null:new WorldCoordinate(s.x(),s.y(),s.z());units.put(type,new PersonalTransportUnitState(type,s.owned(),PersonalTransportOperationState.valueOf(s.operationState()),c,s.summonAllowedByLevel(),s.assignedNpcId()));}PersonalTransportType selected=snapshot.selectedTransportType().isBlank()?null:PersonalTransportType.valueOf(snapshot.selectedTransportType());return new PersonalTransportState(units,selected);}
 private static InventoryEntry require(Map<String,InventoryEntry> items,String key){InventoryEntry e=items.get(key);if(e==null)throw new IllegalArgumentException("Referencia de inventario inexistente: "+key);return e;}
 private static void restoreMutable(InventoryEntry e,InventoryItemSnapshot s){e.setInventoryOrientation(InventoryOrientation.valueOf(s.orientation()));if(e instanceof StackableMiscellaneousItem m)m.restoreCurrentUses(Math.min(s.quantity(),m.maximumUses()));if(e instanceof FirearmItem f&&s.ammunitionRemaining()>=0)f.restoreAmmunitionRemaining(Math.min(s.ammunitionRemaining(),f.loadDefinition().capacity()));if(e instanceof PneumaticFirearmItem p&&s.pressureRemaining()>=0)p.restorePressureRemaining(s.pressureRemaining());if(e instanceof RangedWeaponItem r)r.restoreWearFraction(s.wearFraction());if(e instanceof ArmorPiece a)a.restoreCurrentProtection(s.armorPiercing(),s.armorSlashing(),s.armorBlunt());if(e instanceof WeaponItem w){w.restoreSheathed(s.sheathed());if(!s.gripMode().isBlank()&&!s.actionMode().isBlank())w.selectConfiguration(new WeaponConfiguration(GripMode.valueOf(s.gripMode()),WeaponActionMode.valueOf(s.actionMode())));}}
}
