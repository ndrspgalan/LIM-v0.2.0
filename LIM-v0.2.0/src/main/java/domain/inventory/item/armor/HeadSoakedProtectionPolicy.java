package domain.inventory.item.armor;

import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.item.ItemPropertyId;

/** EMPAPADO puede afectar al cuerpo sin alcanzar HEAD si una prenda impermeable operativa lo intercepta. */
public final class HeadSoakedProtectionPolicy {
    private HeadSoakedProtectionPolicy() {}

    public static boolean headSoaked(boolean characterSoaked, ArmorEquipmentLayout layout) {
        if (!characterSoaked) return false;
        return layout.piecesAt(domain.inventory.equipment.EquipmentSlot.HEAD).stream()
                .noneMatch(p -> p.hasActiveProperty(ItemPropertyId.WATERPROOF));
    }
}
