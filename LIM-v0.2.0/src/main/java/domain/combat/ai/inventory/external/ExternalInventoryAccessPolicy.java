package domain.combat.ai.inventory.external;
import java.util.Objects;
/** la seguridad se resuelve ocultando la acción, no disparando excepciones/hostilidad. */
public final class ExternalInventoryAccessPolicy {
 public boolean canOpen(ExternalInventoryOwnerState state, boolean invisibilityActive, boolean hostileEncounter){
  Objects.requireNonNull(state);
  return switch(state){
   case DEAD,UNCONSCIOUS -> true;
   case SLEEPING,CONSCIOUS -> invisibilityActive;
  };
 }
 /** Consulta factual sin Invisibilidad activa: sólo inconsciente/muerto. */
 public boolean canOpen(ExternalInventoryOwnerState state){return canOpen(state,false,false);}
 public boolean allowedDuringHostileEncounter(){return true;}
 public boolean triggersSave(){return false;}
 public TransferOutcome attemptTransfer(ExternalInventoryOwnerState state,boolean activeEquipment,boolean invisibilityActive,boolean hostileEncounter){
  if(!canOpen(state,invisibilityActive,hostileEncounter)) return new TransferOutcome(false,false,false,"INSPECCIONAR INVENTARIO no está disponible en el estado actual.");
  return new TransferOutcome(true,false,false,"Transferencia permitida dentro de una sesión de inspección ya autorizada.");
 }
 public record TransferOutcome(boolean allowed,boolean wakesOwner,boolean becomesHostile,String rationale){}
}
