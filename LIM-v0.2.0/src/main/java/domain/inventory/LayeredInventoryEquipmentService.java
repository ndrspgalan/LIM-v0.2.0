package domain.inventory;

import domain.inventory.equipment.*;
import domain.inventory.item.armor.*;
import domain.inventory.logistics.*;
import java.util.*;

/**
 *  — frontera atómica entre inventario físico y equipamiento estratificado.
 * Una pieza no puede existir simultáneamente almacenada y equipada.
 * LEFT_HAND/RIGHT_HAND continúan usando InventoryObjectActionService, que retira el arma del
 * compartimento antes de equiparla. El Rotor retraído es la única fuente dorsal de arma:
 * está almacenado en DORSAL_ROTOR_SYSTEM y no ocupa ninguna mano hasta desplegarse.
 */
public final class LayeredInventoryEquipmentService {
    private static final InventoryAutoPlacementService AUTO=new InventoryAutoPlacementService();
    private LayeredInventoryEquipmentService(){}

    public record State(LayeredEquipmentState equipment, QuickAccessBar quickAccess, LogisticsState logistics) {
        public State {
            Objects.requireNonNull(equipment); Objects.requireNonNull(quickAccess); Objects.requireNonNull(logistics);
            validateNoDuplicateOwnership(equipment,logistics);
        }
    }

    public static State equipArmor(State state, ArmorPiece piece, EquipmentSlot slot, ArmorLayerPosition position){
        Objects.requireNonNull(state); Objects.requireNonNull(piece);
        InventoryCompartmentType source=storedCompartment(piece,state.logistics()).orElseThrow(
                ()->new IllegalStateException("La armadura debe pertenecer físicamente al inventario antes de equiparse."));
        LayeredEquipmentState nextEquipment=state.equipment().equipArmor(slot,position,piece);
        LogisticsState nextLogistics=removeFrom(state.logistics(),source,piece)
                .synchronizeGarmentStorage(nextEquipment.armorLayout());
        return new State(nextEquipment,state.quickAccess().clearItem(piece),nextLogistics);
    }

    public static State unequipArmor(State state, ArmorPiece piece){
        Objects.requireNonNull(state); Objects.requireNonNull(piece);
        if(state.equipment().armorLayout().layers().stream().noneMatch(l->l.piece()==piece))
            throw new IllegalStateException("La armadura no está equipada.");
        LayeredEquipmentState nextEquipment=state.equipment().unequipArmor(piece);
        LogisticsState reduced=state.logistics().synchronizeGarmentStorage(nextEquipment.armorLayout());
        InventoryState bridge=new InventoryState(nextEquipment.activeEquipment(),state.quickAccess().clearItem(piece),reduced,nextEquipment.armorLayout());
        InventoryAdmissionResult admitted=AUTO.admit(bridge,piece,InventoryAdmissionSource.ACTIVE_UNEQUIP);
        if(!admitted.accepted()) throw new IllegalStateException(admitted.message());
        return new State(nextEquipment,admitted.inventory().quickAccessBar(),admitted.inventory().logistics());
    }

    /** Comprueba también LEFT/RIGHT: ningún objeto activo puede seguir ocupando una cuadrícula de inventario. */
    public static void validateNoDuplicateOwnership(LayeredEquipmentState equipment, LogisticsState logistics){
        Set<InventoryEntry> active=Collections.newSetFromMap(new IdentityHashMap<>());
        for(EquipmentSlot slot:EquipmentSlot.values()) active.addAll(equipment.activeItems(slot));
        for(InventoryCompartmentType type:InventoryCompartmentType.values()){
            InventoryCompartment c=logistics.compartment(type);
            if(!c.available()) continue;
            for(InventoryEntry stored:c.entries()){
                if(active.contains(stored))
                    throw new IllegalStateException("El objeto "+stored.name()+" no puede estar simultáneamente equipado y almacenado.");
            }
        }
        // BACK_HAND es la ranura equipada que proporciona el dorsal; nunca coexiste con la misma identidad en manos.
        var back=equipment.activeEquipment().itemAt(EquipmentSlot.BACK_HAND);
        if(back.isPresent()) {
            InventoryEntry rotor=back.get();
            if(rotor instanceof domain.inventory.item.WeaponItem weapon && weapon.isSheathed()
                    && !logistics.compartment(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM).available())
                throw new IllegalStateException("Un Espadón de Rotor retraído en BACK_HAND requiere el Sistema de Transporte Dorsal del Rotor V881.");
            boolean inHand=equipment.activeItems(EquipmentSlot.LEFT_HAND).stream().anyMatch(i->i==rotor)
                    || equipment.activeItems(EquipmentSlot.RIGHT_HAND).stream().anyMatch(i->i==rotor);
            if(inHand) throw new IllegalStateException("El Espadón de Rotor no puede duplicarse entre BACK_HAND y otra ranura de arma.");
        }
    }

    private static Optional<InventoryCompartmentType> storedCompartment(InventoryEntry item,LogisticsState logistics){
        for(InventoryCompartmentType t:InventoryCompartmentType.values()){
            InventoryCompartment c=logistics.compartment(t);
            if(c.available() && c.entries().stream().anyMatch(e->e==item)) return Optional.of(t);
        }
        return Optional.empty();
    }
    private static LogisticsState removeFrom(LogisticsState logistics,InventoryCompartmentType type,InventoryEntry item){
        InventoryCompartment c=logistics.compartment(type);
        ArrayList<InventoryEntry> xs=new ArrayList<>(c.entries());
        if(!xs.removeIf(e->e==item)) throw new IllegalStateException("El objeto no está almacenado.");
        return logistics.withCompartment(c.withEntries(xs));
    }
}
