package domain.combat.ai.inventory;
import domain.inventory.*;
import domain.inventory.equipment.*;
import domain.inventory.logistics.*;
import java.util.*;
/** Deriva el estado de combate del inventario real; no recibe booleanos de objetos pretraducidos. */
public final class CombatInventoryResolver {
 public CombatInventorySnapshot resolve(InventoryState inventory){
  Objects.requireNonNull(inventory);
  List<InventoryEntry> equipped=new ArrayList<>();
  for(EquipmentSlot slot:EquipmentSlot.values()) inventory.equipment().itemAt(slot).ifPresent(equipped::add);
  List<InventoryEntry> quick=inventory.quickAccessBar().slots().stream().flatMap(Optional::stream).toList();
  List<InventoryEntry> carried=new ArrayList<>();
  for(InventoryCompartmentType type:InventoryCompartmentType.values()){
   InventoryCompartment c=inventory.logistics().compartment(type);
   if(c!=null&&c.available()) carried.addAll(c.entries());
  }
  return new CombatInventorySnapshot(equipped,quick,carried);
 }
}
