package domain.inventory.container;

import domain.inventory.logistics.InventoryCompartmentType;
import java.util.*;

/**  — identidades físicas de los diez expansores logísticos equipables. */
public final class InventoryExpanderCatalog {
 private static final List<InventoryCompartmentType> TYPES=List.of(
   InventoryCompartmentType.LEG_POUCH, InventoryCompartmentType.BANDOLIER, InventoryCompartmentType.BACKPACK,
   InventoryCompartmentType.DORSAL_ROTOR_SYSTEM, InventoryCompartmentType.SADDLEBAGS_HORSE_LEISURE,
   InventoryCompartmentType.SADDLEBAGS_HORSE_RACING, InventoryCompartmentType.SADDLEBAGS_HORSE_DRAFT,
   InventoryCompartmentType.SADDLEBAGS_BICYCLE_MILITARY, InventoryCompartmentType.SADDLEBAGS_MOTORCYCLE_CARDAN,
   InventoryCompartmentType.ARROW_QUIVER);
 private InventoryExpanderCatalog(){}
 public static List<InventoryCompartmentType> types(){return TYPES;}
 public static InventoryExpanderItem create(InventoryCompartmentType type){
  if(!TYPES.contains(Objects.requireNonNull(type))) throw new IllegalArgumentException("La ranura no corresponde a un expansor físico independiente: "+type);
  return new InventoryExpanderItem(type);
 }
 public static List<InventoryExpanderItem> all(){return TYPES.stream().map(InventoryExpanderItem::new).toList();}
}
