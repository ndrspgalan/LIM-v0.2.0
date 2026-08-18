package presentation.menu;

import domain.character.sheet.CharacterSheet;
import domain.inventory.InventoryEntry;
import domain.inventory.InventoryState;
import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.item.AttributeRequirement;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.WeaponItem;
import domain.inventory.item.misc.MucusCrystalItem;
import domain.inventory.item.misc.MucusTearItem;
import domain.inventory.logistics.InventoryCompartmentType;
import domain.runic.RunicMarkItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/** Proyección inmutable de los efectos que el borrador de atributos produce sobre el Inventario. */
public final class InventoryLevelUpPreview {
    private InventoryLevelUpPreview() {}

    public static List<String> changes(InventoryState inventory, CharacterSheet current, CharacterSheet preview) {
        List<String> changes = new ArrayList<>();
        for (InventoryEntry item : uniqueEntries(inventory)) {
            boolean visibleBefore = visibleTo(item, current);
            boolean visibleAfter = visibleTo(item, preview);
            if (!visibleBefore && visibleAfter) {
                changes.add("[REVELADO] " + item.name());
            }

            if (item instanceof WeaponItem weapon) {
                boolean usableBefore = requirementsSatisfied(weapon.requirements(), current);
                boolean usableAfter = requirementsSatisfied(weapon.requirements(), preview);
                if (usableBefore != usableAfter) {
                    changes.add((usableAfter ? "[REQUISITOS SATISFECHOS] " : "[REQUISITOS DEJADOS DE CUMPLIR] ") + item.name());
                }
            }

            for (ItemProperty property : item.properties()) {
                boolean propertyVisibleBefore = property.isVisibleTo(current);
                boolean propertyVisibleAfter = property.isVisibleTo(preview);
                if (!propertyVisibleBefore && propertyVisibleAfter) {
                    changes.add("[PROPIEDAD REVELADA] " + item.name() + " — " + property.name());
                }
                boolean activeBefore = property.isActiveFor(current);
                boolean activeAfter = property.isActiveFor(preview);
                if (activeBefore != activeAfter) {
                    changes.add((activeAfter ? "[PROPIEDAD ACTIVADA] " : "[PROPIEDAD DESACTIVADA] ")
                            + item.name() + " — " + property.name());
                }
            }
        }
        return List.copyOf(changes);
    }

    private static boolean requirementsSatisfied(List<AttributeRequirement> requirements, CharacterSheet sheet) {
        return requirements.stream().allMatch(requirement ->
                sheet.valueOf(requirement.attribute()) >= requirement.minimumValue());
    }

    private static boolean visibleTo(InventoryEntry item, CharacterSheet sheet) {
        if (item instanceof RunicMarkItem mark) return mark.isAwakenedFor(sheet);
        if (item instanceof MucusTearItem || item instanceof MucusCrystalItem) return domain.knowledge.PropertyKnowledgePolicy.requirementMet(sheet, domain.character.sheet.Attribute.CLARIVIDENCIA, domain.inventory.item.misc.MucusCrystalItem.TRANSPOSITION_CLARIVOYANCE_THRESHOLD);
        return true;
    }

    private static List<InventoryEntry> uniqueEntries(InventoryState inventory) {
        Set<InventoryEntry> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        List<InventoryEntry> result = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            inventory.equipment().itemAt(slot).ifPresent(item -> add(item, seen, result));
        }
        for (InventoryCompartmentType type : InventoryCompartmentType.values()) {
            inventory.logistics().compartment(type).entries().forEach(item -> add(item, seen, result));
        }
        return result;
    }

    private static void add(InventoryEntry item, Set<InventoryEntry> seen, List<InventoryEntry> result) {
        if (seen.add(item)) result.add(item);
    }
}
