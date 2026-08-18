package domain.inventory.container;

import domain.inventory.InventoryEntry;
import java.util.*;
/** Contenido persistente ligado a la identidad física de una prenda/expansor cuando no está conectado al inventario activo. */
public final class ContainerContentsRegistry {
 private static final Map<InventoryEntry,List<InventoryEntry>> CONTENTS=Collections.synchronizedMap(new IdentityHashMap<>());
 private ContainerContentsRegistry(){}
 public static List<InventoryEntry> contentsOf(InventoryEntry carrier){return List.copyOf(CONTENTS.getOrDefault(Objects.requireNonNull(carrier),List.of()));}
 public static boolean hasContents(InventoryEntry carrier){return !contentsOf(carrier).isEmpty();}
 public static void attach(InventoryEntry carrier,List<InventoryEntry> contents){
  Objects.requireNonNull(carrier);Objects.requireNonNull(contents);
  requireAcyclic(carrier,contents);CONTENTS.put(carrier,List.copyOf(contents));
 }
 public static List<InventoryEntry> detachAll(InventoryEntry carrier){List<InventoryEntry> old=CONTENTS.remove(Objects.requireNonNull(carrier));return old==null?List.of():List.copyOf(old);}
 public static void clear(InventoryEntry carrier){CONTENTS.remove(Objects.requireNonNull(carrier));}
 public static void requireAcyclic(InventoryEntry carrier,List<InventoryEntry> contents){
  for(InventoryEntry child:contents){if(child==carrier||containsRecursively(child,carrier,Collections.newSetFromMap(new IdentityHashMap<>())))throw new IllegalArgumentException("Un contenedor no puede contenerse directa o indirectamente a sí mismo.");}
 }
 private static boolean containsRecursively(InventoryEntry current,InventoryEntry sought,Set<InventoryEntry> seen){
  if(!seen.add(current))return false;for(InventoryEntry child:CONTENTS.getOrDefault(current,List.of())){if(child==sought||containsRecursively(child,sought,seen))return true;}return false;
 }
}
