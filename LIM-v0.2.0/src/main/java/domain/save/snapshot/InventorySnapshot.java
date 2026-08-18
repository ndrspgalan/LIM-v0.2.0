package domain.save.snapshot;

import java.io.Serializable;
import java.util.*;

/** Topología completa del inventario y estado mutable de cada instancia. */
public record InventorySnapshot(
        List<InventoryItemSnapshot> items,
        Map<String,String> equipmentSlots,
        List<ArmorLayerSnapshot> armorLayers,
        Map<Integer,String> quickAccessSlots,
        Map<String,CompartmentSnapshot> compartments,
        String selectedTransportType
) implements Serializable {
    public InventorySnapshot {
        items = List.copyOf(items);
        equipmentSlots = Map.copyOf(equipmentSlots);
        armorLayers = List.copyOf(armorLayers);
        quickAccessSlots = Map.copyOf(quickAccessSlots);
        compartments = Map.copyOf(compartments);
        selectedTransportType = selectedTransportType == null ? "" : selectedTransportType;
    }

    /** Compatibilidad con snapshots / sin topología estratificada explícita. */
    public InventorySnapshot(List<InventoryItemSnapshot> items, Map<String,String> equipmentSlots,
                             Map<Integer,String> quickAccessSlots, Map<String,CompartmentSnapshot> compartments,
                             String selectedTransportType) {
        this(items, equipmentSlots, List.of(), quickAccessSlots, compartments, selectedTransportType);
    }

    /** Constructor  histórico. */
    public InventorySnapshot(List<String> itemIds,Map<String,Integer> stackCounts,Map<String,Double> durability,
                             Map<String,Integer> ammunitionRemaining,Set<String> mercuryCoatings) {
        this(List.of(), Map.of(), List.of(), Map.of(), Map.of(), "");
    }
}
