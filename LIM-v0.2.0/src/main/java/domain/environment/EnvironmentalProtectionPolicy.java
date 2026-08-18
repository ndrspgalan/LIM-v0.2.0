package domain.environment;

import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.ItemPropertyId;

import java.util.Objects;

/**
 * La ausencia de cobertura duplica la intensidad ambiental. La cobertura la
 * reduce linealmente hasta x1. Las propiedades ambientales tipadas, como
 */
public final class EnvironmentalProtectionPolicy {
    public double exposureMultiplier(EnvironmentalAdversity adversity, EquipmentState equipment) {
        Objects.requireNonNull(adversity, "La adversidad no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        double coverageMultiplier = switch (adversity) {
            case VIRULENT_TOXICITY, SUFFOCATING_HEAT -> 2.0 - headCoverage(equipment);
            case BITING_FROST -> 2.0 - bodyCoverage(equipment);
            case NORMAL, SOAKED -> 1.0;
        };
        if (adversity == EnvironmentalAdversity.VIRULENT_TOXICITY) {
            if (equipment.hasArmorProperty(ItemPropertyId.ASSISTED_FILTER)
                    || equipment.hasArmorProperty(ItemPropertyId.INTEGRAL_SEAL)) return 0.0;
            return coverageMultiplier;
        }
        if (adversity == EnvironmentalAdversity.SUFFOCATING_HEAT
                && equipment.hasArmorProperty(ItemPropertyId.THERMAL_CONTROL)) return 0.0;
        if (adversity == EnvironmentalAdversity.SUFFOCATING_HEAT
                && equipment.hasArmorProperty(ItemPropertyId.ASSISTED_FILTER)) {
            return coverageMultiplier * 0.5;
        }
        if (adversity == EnvironmentalAdversity.BITING_FROST
                && (equipment.hasArmorProperty(ItemPropertyId.WARMTH)
                || equipment.hasArmorProperty(ItemPropertyId.THERMAL_CONTROL))) return 0.0;
        if (adversity == EnvironmentalAdversity.SOAKED
                && equipment.hasArmorProperty(ItemPropertyId.INTEGRAL_WATERPROOF)) return 0.0;
        return coverageMultiplier;
    }

    public double headCoverage(EquipmentState equipment) {
        return Math.min(1.0, equipment.equippedArmor().stream()
                .mapToDouble(piece -> piece.headCoverageRatio()).sum());
    }

    public double bodyCoverage(EquipmentState equipment) {
        return Math.min(1.0, equipment.equippedArmor().stream()
                .mapToDouble(piece -> piece.bodyCoverageRatio()).sum());
    }
}
