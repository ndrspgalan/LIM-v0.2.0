package domain.inventory.logistics;

import domain.inventory.item.armor.ArmorInventoryCategory;
import java.util.List;
import java.util.Objects;

/** Capacidad logística cosida de una prenda; no incluye bolsas o contenedores externos. */
public record GarmentStorageProfile(ArmorInventoryCategory category, List<InventoryStorageModule> modules) {
    public GarmentStorageProfile {
        Objects.requireNonNull(category);
        if (category != ArmorInventoryCategory.CHEST && category != ArmorInventoryCategory.LEGGINGS && category != ArmorInventoryCategory.INTEGRAL_SUIT)
            throw new IllegalArgumentException(" sólo admite almacenamiento CHEST/LEGGINGS o integral.");
        Objects.requireNonNull(modules);
        if (modules.isEmpty() || modules.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("Una prenda inventariable debe aportar al menos un módulo.");
        modules = List.copyOf(modules);
    }
    public int capacitySlots() { return modules.stream().mapToInt(InventoryStorageModule::capacity).sum(); }
}
