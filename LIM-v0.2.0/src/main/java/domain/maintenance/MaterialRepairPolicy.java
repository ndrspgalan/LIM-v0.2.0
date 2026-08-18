package domain.maintenance;

import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.misc.PortableLaboratoryItem;
import domain.inventory.item.misc.ResinJarItem;
import domain.inventory.item.misc.ReusableRepairToolItem;
import domain.inventory.item.misc.UtilityObjectItem;
import domain.inventory.InventoryEntry;

import java.util.Objects;

/** Resuelve el recurso de mantenimiento por material y categoría sin inventar objetos ya existentes. */
public final class MaterialRepairPolicy {
    public enum ItemCategory { ARMOR, WEAPON, GENERIC }

    public boolean accepts(ArmorMaterial material, ItemCategory category, InventoryEntry resource) {
        Objects.requireNonNull(material); Objects.requireNonNull(category); Objects.requireNonNull(resource);
        if (category == ItemCategory.ARMOR) return acceptsArmor(material, resource);
        return switch (material) {
            case WOOD, MINERALIZED_WOOD -> resource instanceof ResinJarItem;
            case STEEL -> category == ItemCategory.WEAPON && resource instanceof UtilityObjectItem
                    && resource.name().equalsIgnoreCase("Piedra de Afilar");
            case CLOTH, HARDENED_LEATHER, PAPER, BRONZE, LAMINATED_GLASS,
                 MINERAL_MULTILAYER_FABRIC, RUBBER, VULCANIZED_RUBBER, DIELECTRIC_CLOTH,
                 ELECTROMECHANICAL_COMPOSITE, TUNGSTEN_PLATES_2_5_MM, TUNGSTEN -> false;
            case EBONY_WOOD, MINERALIZED_EBONY -> false;
        };
    }

    /**
     * : jerarquía de herramientas para piezas compuestas. La herramienta de mayor complejidad
     * puede resolver operaciones convencionales, evitando excepciones nominales por cada combinación material.
     */
    private boolean acceptsArmor(ArmorMaterial material, InventoryEntry resource) {
        boolean artisan = resource instanceof ReusableRepairToolItem t && t.kind() == ReusableRepairToolItem.Kind.ARTISAN_BOX;
        boolean toolbox = resource instanceof ReusableRepairToolItem t && t.kind() == ReusableRepairToolItem.Kind.TOOLBOX;
        boolean laboratory = resource instanceof PortableLaboratoryItem;
        return switch (material) {
            case CLOTH, HARDENED_LEATHER, PAPER, MINERAL_MULTILAYER_FABRIC, RUBBER,
                 VULCANIZED_RUBBER, DIELECTRIC_CLOTH -> artisan || toolbox || laboratory;
            case BRONZE, STEEL, LAMINATED_GLASS -> toolbox || laboratory;
            case WOOD, MINERALIZED_WOOD -> resource instanceof ResinJarItem || laboratory;
            case ELECTROMECHANICAL_COMPOSITE, TUNGSTEN_PLATES_2_5_MM, TUNGSTEN -> laboratory;
            case EBONY_WOOD, MINERALIZED_EBONY -> false;
        };
    }
}
