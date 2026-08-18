package domain.inventory;

import domain.inventory.equipment.*;
import domain.inventory.item.*;
import domain.inventory.item.meleeWeapons.RotorRetractionPolicy;
import domain.inventory.logistics.*;
import java.util.*;

/**
 *  — BACK_HAND es la tercera ranura de arma, exclusiva del Espadón de Rotor.
 *
 * BACK_HAND es una ranura de equipamiento persistente, no una tercera mano física. El Rotor puede estar:
 * - retraído en el sistema dorsal (BACK_HAND ocupado, isSheathed=true);
 * - desplegado a dos manos (BACK_HAND sigue ocupado, isSheathed=false).
 *
 * LEFT_HAND y RIGHT_HAND pueden seguir ocupadas por otras armas. Cuando el Rotor se despliega, esas armas
 * permanecen equipadas pero se envainan. BACK_HAND hereda RIGHT_HAND como mano dominante efectiva.
 */
public final class RotorBackHandService {
 private RotorBackHandService(){}

 public static InventoryState equipRetractedRotor(InventoryState state, WeaponItem rotor){
  Objects.requireNonNull(state);Objects.requireNonNull(rotor);
  if(!rotor.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) throw new IllegalArgumentException("BACK_HAND sólo admite un arma DE ROTOR.");
  requireDorsal(state);
  if(state.equipment().itemAt(EquipmentSlot.BACK_HAND).isPresent())
   throw new IllegalStateException("BACK_HAND ya está ocupado.");
  if(!rotor.isSheathed()) new RotorRetractionPolicy().completeRetraction(rotor);

  LogisticsState logistics=state.logistics();
  var source=InventoryObjectActionPolicy.storedCompartment(rotor,logistics);
  if(source.isPresent()){
   var c=logistics.compartment(source.get());
   var xs=new ArrayList<InventoryEntry>(c.entries());
   xs.removeIf(e->e==rotor);
   logistics=logistics.withCompartment(c.withEntries(xs));
  }
  return new InventoryState(
          state.equipment().withItem(EquipmentSlot.BACK_HAND,rotor),
          state.quickAccessBar().clearItem(rotor),
          logistics,state.armorLayout());
 }

 /** Despliega el Rotor y envaina las armas equipadas en LEFT/RIGHT sin desalojar ninguna ranura. */
 public static InventoryState deploy(InventoryState state){
  Objects.requireNonNull(state);
  requireDorsal(state);
  WeaponItem rotor=backRotor(state);
  if(!rotor.isSheathed()) return state;

  for(EquipmentSlot hand:List.of(EquipmentSlot.RIGHT_HAND,EquipmentSlot.LEFT_HAND)){
   state.equipment().itemAt(hand)
           .filter(WeaponItem.class::isInstance)
           .map(WeaponItem.class::cast)
           .ifPresent(w->{ if(!w.isSheathed()) w.stowForHandlingTransition(); });
  }
  new RotorRetractionPolicy().completeDeployment(rotor);
  return new InventoryState(state.equipment(),state.quickAccessBar(),state.logistics(),state.armorLayout());
 }

 /**
  * Retrae el Rotor exclusivamente dentro del sistema dorsal. Sin ese sistema no existe una transición de
  * envainado válida y, por tanto, el Rotor activo no puede apartarse para usar otra arma.
  */
 public static InventoryState retract(InventoryState state){
  Objects.requireNonNull(state);
  requireDorsal(state);
  WeaponItem rotor=backRotor(state);
  if(rotor.isSheathed()) return state;
  new RotorRetractionPolicy().completeRetraction(rotor);
  return new InventoryState(state.equipment(),state.quickAccessBar(),state.logistics(),state.armorLayout());
 }


 /** Traslada un Rotor activo desde RIGHT/LEFT a BACK_HAND cuando el dorsal pasa a estar disponible. */
 public static InventoryState moveActiveHandRotorToBackHand(InventoryState state, WeaponItem rotor){
  Objects.requireNonNull(state);Objects.requireNonNull(rotor);
  requireDorsal(state);
  if(!rotor.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)) throw new IllegalArgumentException("Sólo un arma DE ROTOR puede trasladarse a BACK_HAND.");
  if(state.equipment().itemAt(EquipmentSlot.BACK_HAND).isPresent()) throw new IllegalStateException("BACK_HAND ya está ocupado.");
  EquipmentSlot source=null;
  for(EquipmentSlot hand:List.of(EquipmentSlot.RIGHT_HAND,EquipmentSlot.LEFT_HAND))
   if(state.equipment().itemAt(hand).filter(i->i==rotor).isPresent()) source=hand;
  if(source==null) throw new IllegalStateException("El Espadón de Rotor no está equipado en LEFT_HAND ni RIGHT_HAND.");
  new RotorRetractionPolicy().completeRetraction(rotor);
  EquipmentState equipment=state.equipment().withoutItem(source).withItem(EquipmentSlot.BACK_HAND,rotor);
  return new InventoryState(equipment,state.quickAccessBar().clearItem(rotor),state.logistics(),state.armorLayout());
 }

 public static boolean active(InventoryState state){
  return equippedRotor(state).map(w->!w.isSheathed()).orElse(false);
 }

 public static Optional<WeaponItem> equippedRotor(InventoryState state){
  Objects.requireNonNull(state);
  return state.equipment().itemAt(EquipmentSlot.BACK_HAND)
          .filter(WeaponItem.class::isInstance).map(WeaponItem.class::cast)
          .filter(w->w.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE));
 }

 /**
  * Un Rotor que no está asociado a BACK_HAND + dorsal no puede envainarse ni liberar físicamente las manos.
  */
 public static boolean canRetract(InventoryState state, WeaponItem rotor){
  Objects.requireNonNull(state);Objects.requireNonNull(rotor);
  return rotor.hasTrait(WeaponTrait.DORSAL_ROTOR_COMPATIBLE)
          && state.logistics().compartment(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).available()
          && state.equipment().itemAt(EquipmentSlot.BACK_HAND).filter(i->i==rotor).isPresent();
 }

 private static WeaponItem backRotor(InventoryState state){
  return equippedRotor(state)
          .orElseThrow(()->new IllegalStateException("No hay Espadón de Rotor equipado en BACK_HAND."));
 }

 private static void requireDorsal(InventoryState state){
  if(!state.logistics().compartment(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).available())
   throw new IllegalStateException("Falta el Sistema de Transporte Dorsal del Rotor V881.");
 }
}
