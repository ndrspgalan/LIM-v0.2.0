package domain.combat.ai.inventory.external;

import domain.character.sheet.CurrentCharacterStats;
import domain.inventory.*;
import java.util.Objects;

/**
 *  — transferencia bidireccional durante la vista dual.
 * El objetivo conserva sus límites geométricos reales y su carga máxima derivada de AGUANTE.
 */
public final class ExternalInventoryTransferService {
 private final InventoryIncomingFlowService incoming=new InventoryIncomingFlowService();

 public InventoryState storeOnTarget(SharedInventorySession session,
                                     InventoryEntry item,
                                     InventoryState targetInventory,
                                     CurrentCharacterStats targetStats){
  Objects.requireNonNull(session);Objects.requireNonNull(item);
  Objects.requireNonNull(targetInventory);Objects.requireNonNull(targetStats);
  if(!session.open()) throw new IllegalStateException("La inspección de inventario ya se ha cerrado.");
  InventoryState proposed=incoming.pillage(targetInventory,item);
  ExternalInventoryCapacityPolicy.requireWithinCanonicalLoad(targetStats,proposed);
  return proposed;
 }
}
