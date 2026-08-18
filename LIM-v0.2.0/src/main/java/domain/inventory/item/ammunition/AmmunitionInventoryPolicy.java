package domain.inventory.item.ammunition;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.QuickAccessUsePolicy;
import domain.inventory.logistics.InventoryCompartmentType;
import java.util.Objects;
public final class AmmunitionInventoryPolicy {
    public AmmunitionLoadResult consumeSingleShotForEquippedWeapon(InventoryEntry weapon,AmmunitionDescriptor required,InventoryState inventory){
        return consume(weapon, required, inventory, 1, false);
    }

    public AmmunitionLoadResult consumeForEquippedWeapon(InventoryEntry weapon,AmmunitionDescriptor required,InventoryState inventory){
        return consume(weapon, required, inventory, Integer.MAX_VALUE, true);
    }

    private AmmunitionLoadResult consume(InventoryEntry weapon, AmmunitionDescriptor required, InventoryState inventory, int requestedShots, boolean wholeSource) {
        Objects.requireNonNull(weapon);Objects.requireNonNull(required);Objects.requireNonNull(inventory);
        if(!QuickAccessUsePolicy.isActiveEquipment(weapon,inventory.equipment()))
            return AmmunitionLoadResult.rejected("Solo el equipamiento activo puede llamar munición almacenada.");
        for(InventoryCompartmentType type:InventoryCompartmentType.values()){
            var c=inventory.logistics().compartment(type); if(!c.available())continue;
            for(InventoryEntry e:c.entries()){
                if(!(e instanceof AmmunitionSource source)||source.depleted()||!source.ammunitionDescriptor().compatibleWith(required))continue;
                if(!QuickAccessUsePolicy.authorizeRequestedByActiveEquipment(weapon,e,inventory).allowed())continue;
                if (wholeSource) {
                    int shots = source.shotsLoadedPerConsumedUnit();
                    if (shots > 0 && source.consumeOneUnit()) return AmmunitionLoadResult.loaded(shots);
                } else {
                    int shots = Math.min(requestedShots, source.remainingShots());
                    if (shots > 0 && source.consumeShots(shots)) return AmmunitionLoadResult.loaded(shots);
                }
            }
        }
        return AmmunitionLoadResult.rejected("No existe munición compatible disponible en inventario.");
    }
}
