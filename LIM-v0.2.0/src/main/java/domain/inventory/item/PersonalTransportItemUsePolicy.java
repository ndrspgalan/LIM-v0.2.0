package domain.inventory.item;

import domain.inventory.InventoryEntry;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.logistics.PersonalTransportType;
import java.util.Objects;

/** Política general para usar cualquier objeto o arma desde Transporte Personal. */
public final class PersonalTransportItemUsePolicy {
    public boolean canUseAsDriver(InventoryEntry item, PersonalTransportType transport) {
        Objects.requireNonNull(item); Objects.requireNonNull(transport);
        return switch (transport.family()) {
            case HORSE -> has(item, ItemPropertyId.EQUESTRIAN);
            case BICYCLE -> has(item, ItemPropertyId.BICYCLAR);
            case MOTORCYCLE -> has(item, ItemPropertyId.MOTORCYCLAR);
        };
    }

    public boolean canUseAsPassenger(InventoryEntry item) { return has(item, ItemPropertyId.COPILOT); }

    public EquipmentSlot forcedSlotWhileDriving(InventoryEntry item, PersonalTransportType transport) {
        Objects.requireNonNull(item); Objects.requireNonNull(transport);
        if (transport.family() == domain.inventory.logistics.PersonalTransportFamily.MOTORCYCLE
                && canUseAsDriver(item, transport)) return EquipmentSlot.LEFT_HAND;
        return EquipmentSlot.RIGHT_HAND;
    }

    private boolean has(InventoryEntry item, ItemPropertyId id) {
        return item.properties().stream().anyMatch(p -> p.id() == id);
    }
}
