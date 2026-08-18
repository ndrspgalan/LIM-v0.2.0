package domain.inventory;

import domain.inventory.logistics.*;
import java.util.*;

/**
 * : autoridad única de admisión automática.
 * Recorre todas las ranuras corporales/logísticas ordinarias disponibles en ese momento:
 * Polainas → Coraza → Pernera → Bandolera → Mochila → Carcaj.
 * Las alforjas NO son destino automático: sólo son accesibles al interactuar con la montura mediante E.
 * El sistema prueba ambas orientaciones antes de declarar falta de espacio.
 */
public final class InventoryAutoPlacementService {
    public static final String NO_SPACE_MESSAGE = "No hay espacio en el inventario para ejecutar esta acción";
    public static final List<InventoryCompartmentType> PRIORITY = List.of(
            InventoryCompartmentType.LEGGINGS_STORAGE,
            InventoryCompartmentType.CHEST_STORAGE,
            InventoryCompartmentType.LEG_POUCH,
            InventoryCompartmentType.BANDOLIER,
            InventoryCompartmentType.BACKPACK,
            InventoryCompartmentType.ARROW_QUIVER);

    public List<InventoryCompartmentType> priority(){ return PRIORITY; }

    public InventoryAdmissionResult admit(InventoryState inventory, InventoryEntry item, InventoryAdmissionSource source){
        Objects.requireNonNull(inventory); Objects.requireNonNull(item); Objects.requireNonNull(source);
        if (domain.inventory.container.ContainerContentsRegistry.hasContents(item)) {
            return new InventoryAdmissionResult(false,inventory,Optional.empty(),
                    "Un contenedor cargado sólo puede asociarse a su ranura de equipamiento correspondiente.");
        }
        InventoryOrientation original=item.inventoryOrientation();
        for (InventoryCompartmentType type : PRIORITY) {
            InventoryCompartment c=inventory.logistics().compartment(type);
            if(!c.available()) continue;
            for(InventoryOrientation orientation: orientationsToTry(item,original)){
                item.setInventoryOrientation(orientation);
                ArrayList<InventoryEntry> entries=new ArrayList<>(c.entries()); entries.add(item);
                try {
                    LogisticsState nextLogistics=inventory.logistics().withCompartment(c.withEntries(entries));
                    return new InventoryAdmissionResult(true,
                            new InventoryState(inventory.equipment(),inventory.quickAccessBar(),nextLogistics,inventory.armorLayout()),
                            Optional.of(type),
                            "Objeto incorporado desde "+source+" en "+type.label()+".");
                } catch (IllegalArgumentException ignored) { }
            }
            item.setInventoryOrientation(original);
        }
        item.setInventoryOrientation(original);
        return new InventoryAdmissionResult(false,inventory,Optional.empty(),NO_SPACE_MESSAGE);
    }

    public InventoryState requireAdmit(InventoryState inventory, InventoryEntry item, InventoryAdmissionSource source){
        InventoryAdmissionResult r=admit(inventory,item,source);
        if(!r.accepted()) throw new IllegalStateException(r.message());
        return r.inventory();
    }

    private static List<InventoryOrientation> orientationsToTry(InventoryEntry item,InventoryOrientation original){
        if(item.canonicalFootprint().isSquare() || !item.canonicalFootprint().hasGridDimensions()) return List.of(original);
        return List.of(original,original.toggled());
    }

    public InventoryAdmissionResult fromWorld(InventoryState inventory,InventoryEntry item){return admit(inventory,item,InventoryAdmissionSource.WORLD_PICKUP);}
    public InventoryAdmissionResult fromTransaction(InventoryState inventory,InventoryEntry item){return admit(inventory,item,InventoryAdmissionSource.TRANSACTION);}
    public InventoryAdmissionResult fromPillage(InventoryState inventory,InventoryEntry item){return admit(inventory,item,InventoryAdmissionSource.PILLAGE);}
    public InventoryAdmissionResult fromCrafting(InventoryState inventory,InventoryEntry item){return admit(inventory,item,InventoryAdmissionSource.CRAFTING);}
    public InventoryAdmissionResult fromDialogue(InventoryState inventory,InventoryEntry item){return admit(inventory,item,InventoryAdmissionSource.DIALOGUE_GRANT);}
    public InventoryAdmissionResult fromReward(InventoryState inventory,InventoryEntry item){return admit(inventory,item,InventoryAdmissionSource.SYSTEM_REWARD);}
}
