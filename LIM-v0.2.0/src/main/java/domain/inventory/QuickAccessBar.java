package domain.inventory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record QuickAccessBar(List<Optional<InventoryEntry>> slots) {
    public static final int SLOT_COUNT = 4;

    public QuickAccessBar {
        Objects.requireNonNull(slots, "Los accesos rápidos no pueden ser nulos.");
        if (slots.size() != SLOT_COUNT) {
            throw new IllegalArgumentException("Deben existir exactamente cuatro accesos rápidos.");
        }
        if (slots.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Un acceso rápido no puede ser nulo.");
        }
        slots = List.copyOf(slots);
    }

    public QuickAccessBar assign(int slotNumber, InventoryEntry item) {
        if (slotNumber < 1 || slotNumber > SLOT_COUNT) throw new IllegalArgumentException("El acceso rápido debe estar entre 1 y 4.");
        Objects.requireNonNull(item);
        java.util.ArrayList<Optional<InventoryEntry>> copy=new java.util.ArrayList<>(slots); copy.set(slotNumber-1,Optional.of(item)); return new QuickAccessBar(copy);
    }

    public QuickAccessBar clear(int slotNumber) {
        if (slotNumber < 1 || slotNumber > SLOT_COUNT) throw new IllegalArgumentException("El acceso rápido debe estar entre 1 y 4.");
        java.util.ArrayList<Optional<InventoryEntry>> copy=new java.util.ArrayList<>(slots); copy.set(slotNumber-1,Optional.empty()); return new QuickAccessBar(copy);
    }

    public QuickAccessBar clearItem(InventoryEntry item) {
        Objects.requireNonNull(item); java.util.ArrayList<Optional<InventoryEntry>> copy=new java.util.ArrayList<>(slots);
        for(int i=0;i<copy.size();i++) if(copy.get(i).filter(v -> v==item).isPresent()) copy.set(i,Optional.empty());
        return new QuickAccessBar(copy);
    }


    public Optional<QuickAccessBinding> binding(int slotNumber) {
        if (slotNumber < 1 || slotNumber > SLOT_COUNT) throw new IllegalArgumentException("El acceso rápido debe estar entre 1 y 4.");
        return slots.get(slotNumber-1).map(item -> new QuickAccessBinding(
                slotNumber,
                item.canonicalTypeId(),
                QuickAccessPolicy.sourceCompartment(slotNumber),
                item));
    }

    public static QuickAccessBar empty() {
        return new QuickAccessBar(List.of(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
        ));
    }
}
