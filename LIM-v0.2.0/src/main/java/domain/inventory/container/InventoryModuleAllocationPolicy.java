package domain.inventory.container;
import domain.inventory.*;import domain.inventory.logistics.*;import java.util.*;
/** Reconstruye determinísticamente a qué módulo físico pertenece cada entrada de un compartimento modular. */
public final class InventoryModuleAllocationPolicy {
 private InventoryModuleAllocationPolicy(){}
 public static Map<InventoryEntry,InventoryStorageModule> allocate(InventoryCompartment c){
  List<InventoryEntry> entries=new ArrayList<>(c.entries());entries.sort(Comparator.comparingInt((InventoryEntry e)->e.footprint().occupiedSlots()).reversed());
  IdentityHashMap<InventoryEntry,InventoryStorageModule> out=new IdentityHashMap<>();int[] remain=c.storageModules().stream().mapToInt(InventoryStorageModule::capacity).toArray();
  if(!assign(entries,0,c.storageModules(),remain,out))throw new IllegalStateException("El contenido no puede reconstruirse sobre sus módulos físicos.");return out;
 }
 private static boolean assign(List<InventoryEntry> es,int idx,List<InventoryStorageModule> ms,int[] rem,IdentityHashMap<InventoryEntry,InventoryStorageModule> out){
  if(idx==es.size())return true;InventoryEntry e=es.get(idx);int area=e.footprint().occupiedSlots();
  for(int i=0;i<ms.size();i++){var g=ms.get(i).grid();if(rem[i]<area||!e.footprint().fitsInside(g))continue;rem[i]-=area;out.put(e,ms.get(i));if(assign(es,idx+1,ms,rem,out))return true;out.remove(e);rem[i]+=area;}return false;
 }
}
