package domain.inventory;

import java.util.*;

/**
 * : toda incorporación física usa la misma prevalidación.
 * Ningún origen debe perder el objeto hasta que la admisión del destino haya sido confirmada.
 */
public final class InventoryIncomingFlowService {
    private final InventoryAutoPlacementService placement=new InventoryAutoPlacementService();

    public InventoryState pickupWithE(InventoryState target,InventoryEntry item){
        return placement.requireAdmit(target,item,InventoryAdmissionSource.WORLD_PICKUP);
    }
    public InventoryState transaction(InventoryState target,InventoryEntry item){
        return placement.requireAdmit(target,item,InventoryAdmissionSource.TRANSACTION);
    }
    public InventoryState pillage(InventoryState target,InventoryEntry item){
        return placement.requireAdmit(target,item,InventoryAdmissionSource.PILLAGE);
    }
    public InventoryState crafting(InventoryState target,InventoryEntry item){
        return placement.requireAdmit(target,item,InventoryAdmissionSource.CRAFTING);
    }

    /** Lotes creados/recibidos: todos caben o no se materializa ninguno. */
    public InventoryState batch(InventoryState target,List<? extends InventoryEntry> items,InventoryAdmissionSource source){
        Objects.requireNonNull(items);
        InventoryState staged=target;
        List<InventoryEntry> rotated=new ArrayList<>();
        List<InventoryOrientation> originals=new ArrayList<>();
        for(InventoryEntry item:items){ rotated.add(item); originals.add(item.inventoryOrientation()); }
        try{
            for(InventoryEntry item:items) staged=placement.requireAdmit(staged,item,source);
            return staged;
        }catch(RuntimeException ex){
            for(int i=0;i<rotated.size();i++) rotated.get(i).setInventoryOrientation(originals.get(i));
            throw ex;
        }
    }
}
