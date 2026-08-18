package domain.inventory.container;
import domain.inventory.*;import domain.inventory.item.armor.*;import domain.inventory.logistics.*;import java.util.*;
/** Dos pestañas: INVENTARIO PROPIO <-> CONTENIDO DEL OBJETO, sin equipar el contenedor. */
public final class ObjectContainerInspectionSession {
 private final InventoryEntry carrier;private boolean open=true;
 public ObjectContainerInspectionSession(InventoryEntry carrier){this.carrier=Objects.requireNonNull(carrier);}
 public List<InventoryEntry> objectContents(){return ContainerContentsRegistry.contentsOf(carrier);}
 public InventoryState take(InventoryEntry item,InventoryState player){requireOpen();List<InventoryEntry> xs=new ArrayList<>(objectContents());if(!xs.removeIf(e->e==item))throw new IllegalArgumentException("El objeto no está dentro del contenedor.");InventoryState next=new InventoryIncomingFlowService().pillage(player,item);ContainerContentsRegistry.attach(carrier,xs);return next;}
 public void storeFromPlayer(InventoryEntry item){requireOpen();List<InventoryEntry> xs=new ArrayList<>(objectContents());xs.add(item);validateCapacity(carrier,xs);ContainerContentsRegistry.attach(carrier,xs);}
 public void close(){open=false;}public boolean open(){return open;}private void requireOpen(){if(!open)throw new IllegalStateException("La inspección está cerrada.");}
 private static void validateCapacity(InventoryEntry carrier,List<InventoryEntry> xs){
  if(carrier instanceof InventoryExpanderItem e){new InventoryCompartment(e.compartmentType(),true,e.compartmentType().grid(),xs,Optional.empty());return;}
  if(carrier instanceof ArmorPiece armor){var profile=GarmentStorageCatalog.profileFor(armor).orElseThrow(()->new IllegalArgumentException("La prenda no aporta almacenamiento."));InventoryCompartmentType type=profile.category()==ArmorInventoryCategory.LEGGINGS?InventoryCompartmentType.LEGGINGS_STORAGE:InventoryCompartmentType.CHEST_STORAGE;InventoryCompartment.modular(type,profile.modules(),xs);return;}
  throw new IllegalArgumentException("El objeto no es un contenedor inspeccionable.");
 }
}
