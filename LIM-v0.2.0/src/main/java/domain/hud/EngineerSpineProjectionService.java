package domain.hud;

import domain.inventory.equipment.EquipmentSlot;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.ItemPropertyId;

import java.util.Objects;

/** Única proyección visual persistente permitida: indicador integrado en el traje del Ingeniero. */
public final class EngineerSpineProjectionService {
    public EngineerSpineIndicator project(EquipmentState equipment) {
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        return equipment.armorAt(EquipmentSlot.CHEST)
                .filter(this::isEngineerSuit)
                .map(this::indicatorFor)
                .orElseGet(EngineerSpineIndicator::hidden);
    }

    private boolean isEngineerSuit(ArmorPiece armor) {
        return armor.containsMaterial(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE)
                && armor.hasProperty(ItemPropertyId.ONE_PIECE_SUIT);
    }

    private EngineerSpineIndicator indicatorFor(ArmorPiece armor) {
        double maximum = armor.protection().blunt();
        double ratio = maximum <= 0 ? 0 : armor.currentBluntProtection() / maximum;
        ratio = Math.max(0, Math.min(1, ratio));
        return new EngineerSpineIndicator(true, ratio, EngineerSpineIndicator.CYAN_COOLANT);
    }
}
