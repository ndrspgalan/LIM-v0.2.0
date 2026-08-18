package domain.save.snapshot;

import java.io.Serializable;
import java.util.Objects;

/** Persistencia de una pieza de armadura/ropa en su ranura y capa canónicas. */
public record ArmorLayerSnapshot(String equipmentSlot, String layerPosition, String itemKey) implements Serializable {
    public ArmorLayerSnapshot {
        equipmentSlot = Objects.requireNonNull(equipmentSlot, "La ranura de armadura no puede ser nula.");
        layerPosition = Objects.requireNonNull(layerPosition, "La capa de armadura no puede ser nula.");
        itemKey = Objects.requireNonNull(itemKey, "La referencia de pieza no puede ser nula.");
    }
}
