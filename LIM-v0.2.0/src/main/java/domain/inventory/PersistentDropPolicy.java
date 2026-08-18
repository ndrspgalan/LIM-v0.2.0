package domain.inventory;

import domain.inventory.catalog.PhysicalObjectCatalog;
import java.util.Set;

/** objetos únicos cuya ubicación no puede desaparecer de la Memoria del Mundo al tirarlos. */
public final class PersistentDropPolicy {
    private static final Set<String> TRACKED = Set.of("Maletín profesional de Alicia e Iván");
    private PersistentDropPolicy(){}

    public static boolean requiresWorldMemoryTracking(InventoryEntry item) {
        return TRACKED.contains(PhysicalObjectCatalog.definitionFor(item).displayName());
    }
}
