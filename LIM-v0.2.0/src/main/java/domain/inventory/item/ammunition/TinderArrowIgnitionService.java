package domain.inventory.item.ammunition;

import domain.inventory.*;
import domain.inventory.item.misc.*;
import domain.inventory.logistics.*;
import java.util.*;

/** al preparar/disparar una Flecha de Yesca busca automáticamente Amadou + Pedernal accesibles. */
public final class TinderArrowIgnitionService {
    public boolean igniteIfResourcesAvailable(TinderArrowItem arrow, InventoryState inventory) {
        Objects.requireNonNull(arrow); Objects.requireNonNull(inventory);
        if(arrow.lit()) return true;
        UtilityObjectItem amadou=find(inventory,"Amadou");
        UtilityObjectItem flint=find(inventory,"Pedernal");
        if(amadou==null || flint==null || amadou.isDepleted() || flint.isDepleted()) return false;
        // IgnitionPolicy comprueba ambos antes de consumir: consumo atómico de un uso de cada uno.
        return arrow.ignite(amadou,flint);
    }

    private static UtilityObjectItem find(InventoryState inventory,String name){
        for(InventoryCompartmentType t:InventoryAutoPlacementService.PRIORITY){
            InventoryCompartment c=inventory.logistics().compartment(t);
            if(!c.available()) continue;
            for(InventoryEntry e:c.entries())
                if(e instanceof UtilityObjectItem u && u.name().equals(name)) return u;
        }
        return null;
    }
}
