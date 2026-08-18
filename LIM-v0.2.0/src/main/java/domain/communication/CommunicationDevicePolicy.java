package domain.communication;

import domain.inventory.InventoryEntry;
import domain.inventory.item.ItemPropertyId;

public final class CommunicationDevicePolicy {
    private CommunicationDevicePolicy(){}

    public static CommunicationDeviceType deviceTypeOf(InventoryEntry item){
        if(item==null) return null;
        if(item.properties().stream().anyMatch(p->p.id()==ItemPropertyId.TERRESTRIAL_INTERCOM))
            return CommunicationDeviceType.AERONAUT_INTERCOM;
        if(item.properties().stream().anyMatch(p->p.id()==ItemPropertyId.PHOTONIC_COMMUNICATION))
            return CommunicationDeviceType.PANOPTICON;
        return null;
    }
}
