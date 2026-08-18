package domain.social;

import domain.inventory.logistics.InventoryCompartmentType;

/** dos armas equipadas; tercera sólo para Rotor en BACK_HAND con sistema dorsal. */
public final class CanonicalCombatWeaponSlotPolicy {
    private CanonicalCombatWeaponSlotPolicy(){}
    public static void validate(CanonicalStartingEquipment e){
        int n=e.weaponNames().size();
        long rotor=e.weaponNames().stream().filter("Espadón de Rotor"::equals).count();
        if(n<=2){
            if(rotor>0 && !e.inventoryExpanders().contains(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM))
                throw new IllegalArgumentException("Rotor equipado requiere sistema dorsal.");
            return;
        }
        if(n!=3 || rotor!=1 || !e.inventoryExpanders().contains(InventoryCompartmentType.DORSAL_ROTOR_SYSTEM))
            throw new IllegalArgumentException("Sólo se permiten tres armas cuando una es el Espadón de Rotor en BACK_HAND.");
    }
}
