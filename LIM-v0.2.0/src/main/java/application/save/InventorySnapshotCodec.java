package application.save;
import domain.inventory.*; import domain.inventory.equipment.*; import domain.inventory.item.*; import domain.inventory.item.armor.*; import domain.inventory.item.firearms.*; import domain.inventory.item.misc.*; import domain.inventory.item.rangedWeapons.*; import domain.save.snapshot.*; import java.util.*;
/** serializa topología e identidad por instancia sin depender del layout visual. */
public final class InventorySnapshotCodec {
 private InventorySnapshotCodec(){}
 public static InventorySnapshot snapshot(InventoryState inventory){
  IdentityHashMap<InventoryEntry,String> keys=new IdentityHashMap<>(); List<InventoryItemSnapshot> items=new ArrayList<>(); int[] seq={0};
  java.util.function.Function<InventoryEntry,String> keyFor=e->{String existing=keys.get(e);if(existing!=null)return existing;String key="item-"+(++seq[0]);keys.put(e,key);items.add(item(key,e));return key;};
  Map<String,String> equipment=new LinkedHashMap<>(); inventory.equipment().equippedItems().forEach((slot,e)->equipment.put(slot.name(),keyFor.apply(e)));
  List<ArmorLayerSnapshot> armorLayers=inventory.armorLayout().layers().stream()
    .map(layer->new ArmorLayerSnapshot(layer.slot().name(),layer.position().name(),keyFor.apply(layer.piece()))).toList();
  Map<Integer,String> quick=new LinkedHashMap<>(); for(int i=1;i<=QuickAccessBar.SLOT_COUNT;i++){int slot=i;inventory.quickAccessBar().binding(i).ifPresent(b->quick.put(slot,keyFor.apply(b.currentInstance())));}
  Map<String,CompartmentSnapshot> compartments=new LinkedHashMap<>(); inventory.logistics().compartments().forEach((type,c)->{List<String> ks=new ArrayList<>();for(InventoryEntry e:c.entries())ks.add(keyFor.apply(e));String helmet=c.externallyCarriedHelmet().map(keyFor).orElse("");List<InventoryStorageModuleSnapshot> modules=c.storageModules().stream().map(m->new InventoryStorageModuleSnapshot(m.label(),m.grid().verticalSlots(),m.grid().horizontalSlots())).toList();compartments.put(type.name(),new CompartmentSnapshot(c.available(),ks,helmet,modules));});
  return new InventorySnapshot(items,equipment,armorLayers,quick,compartments,inventory.logistics().selectedPersonalTransportType().map(Enum::name).orElse(""));
 }
 private static InventoryItemSnapshot item(String key,InventoryEntry e){
  int q=e instanceof StackableMiscellaneousItem s?s.quantity():1; int ammo=e instanceof FirearmItem f?f.ammunitionRemaining():-1; int pressure=e instanceof PneumaticFirearmItem p?p.pressureRemaining():-1;
  double wear=e instanceof RangedWeaponItem r?r.wearFraction():0; double ap=0,as=0,ab=0;if(e instanceof ArmorPiece a){ap=a.currentPiercingProtection();as=a.currentSlashingProtection();ab=a.currentBluntProtection();}
  boolean sheathed=e instanceof WeaponItem w&&w.isSheathed();String grip="",mode="";if(e instanceof WeaponItem w){grip=w.currentConfiguration().gripMode().name();mode=w.currentConfiguration().actionMode().name();}
  return new InventoryItemSnapshot(key,e.name(),e.inventoryOrientation().name(),q,ammo,pressure,wear,ap,as,ab,sheathed,grip,mode);
 }
}
