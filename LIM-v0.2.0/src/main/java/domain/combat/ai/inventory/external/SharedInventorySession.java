package domain.combat.ai.inventory.external;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.InventoryIncomingFlowService;
import domain.combat.ai.inventory.CombatInventorySnapshot;
import java.util.*;
/** Vista dual de inventario.  nunca convierte el acceso fallido en combate: la opción simplemente no existe. */
public final class SharedInventorySession {
 private final CombatInventorySnapshot actorInventory;
 private final ExternalInventoryOwnerState ownerState;
 private final boolean openedUsingInvisibility;
 private final boolean hostileEncounter;
 private boolean open=true;
 private final List<InventoryEntry> transferred=new ArrayList<>();
 private final ExternalInventoryAccessPolicy policy=new ExternalInventoryAccessPolicy();
 public SharedInventorySession(CombatInventorySnapshot actorInventory,ExternalInventoryOwnerState ownerState){this(actorInventory,ownerState,false,false);}
 public SharedInventorySession(CombatInventorySnapshot actorInventory,ExternalInventoryOwnerState ownerState,boolean invisibilityActive,boolean hostileEncounter){
  this.actorInventory=Objects.requireNonNull(actorInventory);this.ownerState=Objects.requireNonNull(ownerState);
  this.hostileEncounter=hostileEncounter;
  this.openedUsingInvisibility=(ownerState==ExternalInventoryOwnerState.CONSCIOUS||ownerState==ExternalInventoryOwnerState.SLEEPING)&&invisibilityActive;
  if(!policy.canOpen(ownerState,invisibilityActive,hostileEncounter)) throw new IllegalArgumentException("INSPECCIONAR INVENTARIO no está disponible.");
 }
 public CombatInventorySnapshot actorInventory(){return actorInventory;} public ExternalInventoryOwnerState ownerState(){return ownerState;}
 public boolean open(){return open;} public boolean hostile(){return hostileEncounter;} public boolean triggersSave(){return false;} public boolean allowedDuringHostileEncounter(){return true;}
 public List<InventoryEntry> transferred(){return List.copyOf(transferred);}
 /** Si la sesión dependía de Invisibilidad, PA=0 o perder Invisibilidad cierra inmediatamente las dos pestañas. */
 public boolean synchronizeInvisibility(double currentPa,boolean invisibilityActive){
  if(openedUsingInvisibility && (currentPa<=0.0 || !invisibilityActive)){open=false;return false;} return open;
 }
 public InventoryState takeToPlayer(InventoryEntry item,InventoryState playerInventory){
  requireOpen();Objects.requireNonNull(item);Objects.requireNonNull(playerInventory);
  InventoryState next=new InventoryIncomingFlowService().pillage(playerInventory,item);transferred.add(item);return next;
 }
 public ExternalInventoryAccessPolicy.TransferOutcome take(InventoryEntry item){
  requireOpen();Objects.requireNonNull(item);transferred.add(item);return new ExternalInventoryAccessPolicy.TransferOutcome(true,false,false,"Transferencia permitida.");
 }
 public void close(){open=false;}
 private void requireOpen(){if(!open)throw new IllegalStateException("La inspección de inventario ya se ha cerrado.");}
}
