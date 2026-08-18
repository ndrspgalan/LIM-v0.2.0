package domain.maintenance;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.item.armor.*;
import domain.inventory.logistics.InventoryCompartmentType;

import java.util.*;

/** una reparación de armadura consume una unidad de cada material degradable constituyente. */
public final class ArmorRepairMaterialPolicy {

    /**
     * Materiales que realmente necesitan reposición. Los materiales NON_DEGRADING no se consumen;
     * PAPER sí se exige cuando la única condición reparable es WET.
     */
    public Set<ArmorMaterial> requiredMaterials(ArmorPiece armor) {
        Objects.requireNonNull(armor);
        LinkedHashSet<ArmorMaterial> required = new LinkedHashSet<>();
        if (armor.isDegraded()) {
            armor.materials().stream()
                    .filter(m -> m.wearPolicy().permitsWear())
                    .forEach(required::add);
        }
        if (armor.isWet() && armor.containsMaterial(ArmorMaterial.PAPER)) required.add(ArmorMaterial.PAPER);
        return Collections.unmodifiableSet(required);
    }

    public boolean hasRequiredMaterials(ArmorPiece armor, InventoryState inventory) {
        return requiredMaterials(armor).stream().allMatch(m -> availableUnits(inventory, m) >= 1);
    }

    /** Consume de forma atómica: primero valida todas las existencias y después retira una unidad por material. */
    public boolean consumeRequiredMaterials(ArmorPiece armor, InventoryState inventory) {
        Set<ArmorMaterial> required = requiredMaterials(armor);
        if (required.isEmpty() || !hasRequiredMaterials(armor, inventory)) return false;
        for (ArmorMaterial material : required) {
            MaterialItem stack = materialStacks(inventory, material).stream()
                    .filter(item -> item.currentUses() > 0).findFirst().orElseThrow();
            if (!stack.consumeOne()) throw new IllegalStateException("El material validado no pudo consumirse: " + material.label());
        }
        return true;
    }

    private int availableUnits(InventoryState inventory, ArmorMaterial material) {
        return materialStacks(inventory, material).stream().mapToInt(MaterialItem::currentUses).sum();
    }

    private List<MaterialItem> materialStacks(InventoryState inventory, ArmorMaterial material) {
        Objects.requireNonNull(inventory); Objects.requireNonNull(material);
        List<MaterialItem> result = new ArrayList<>();
        for (InventoryCompartmentType type : InventoryCompartmentType.values()) {
            var compartment = inventory.logistics().compartment(type);
            if (compartment == null || !compartment.available()) continue;
            for (InventoryEntry entry : compartment.entries()) {
                if (entry instanceof MaterialItem item && item.material() == material) result.add(item);
            }
        }
        return result;
    }
}
