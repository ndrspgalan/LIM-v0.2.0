package application;

import domain.combat.ai.inventory.CombatInventoryResolver;
import domain.combat.ai.inventory.external.*;
import domain.inventory.InventoryState;
import java.util.Objects;

/**
 *  — apertura de inventarios externos.
 * La seguridad se resuelve por disponibilidad de INSPECCIONAR INVENTARIO:
 * - DEAD / UNCONSCIOUS: disponible también durante encuentro hostil.
 * - SLEEPING / CONSCIOUS: requieren Invisibilidad activa, pero el estado hostil ya no bloquea la inspección.
 * Nunca se usa un acceso fallido como detonante de hostilidad.
 */
public final class ExternalInventoryAccessService {
 private final ExternalInventoryAccessPolicy policy=new ExternalInventoryAccessPolicy();
 private final CombatInventoryResolver resolver=new CombatInventoryResolver();

 public boolean inspectOptionAvailable(ExternalInventoryOwnerState ownerState,
                                       boolean invisibilityActive,
                                       boolean hostileEncounter){
  return policy.canOpen(Objects.requireNonNull(ownerState),invisibilityActive,hostileEncounter);
 }

 public SharedInventorySession open(InventoryState targetInventory,
                                    ExternalInventoryOwnerState ownerState,
                                    boolean invisibilityActive,
                                    boolean hostileEncounter){
  Objects.requireNonNull(targetInventory);Objects.requireNonNull(ownerState);
  if(!policy.canOpen(ownerState,invisibilityActive,hostileEncounter))
   throw new IllegalStateException("INSPECCIONAR INVENTARIO no está disponible.");
  return new SharedInventorySession(resolver.resolve(targetInventory),ownerState,invisibilityActive,hostileEncounter);
 }

 /** Compatibilidad explícita para muertos/inconscientes fuera de combate, sin Invisibilidad. */
 public SharedInventorySession open(InventoryState targetInventory,ExternalInventoryOwnerState ownerState){
  return open(targetInventory,ownerState,false,false);
 }

 public boolean triggersSave(){return false;}
 public boolean allowedDuringHostileEncounter(){return true;}
}
