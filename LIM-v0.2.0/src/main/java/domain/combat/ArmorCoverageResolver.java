package domain.combat;

import domain.inventory.equipment.ArmorEquipmentLayout;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.equipment.EquippedArmorLayer;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.BodyArmorRegion;

import java.util.List;
import java.util.Objects;

/** Selecciona armadura por regiones globales obsoleto y por hitboxes bélicas . */
public final class ArmorCoverageResolver {
    private static final double EPSILON = 1.0e-9;

    /** Ruta canónica de combate: devuelve capas ordenadas de exterior a interior. */
    public List<EquippedArmorLayer> applicableArmor(ArmorCombatHitbox hitbox, ArmorEquipmentLayout layout) {
        Objects.requireNonNull(hitbox);
        Objects.requireNonNull(layout);
        return layout.layers().stream()
                .filter(layer -> layer.piece().protects(hitbox))
                .sorted(ArmorLayerOrderPolicy.outerToInner())
                .toList();
    }

    /** Compatibilidad no bélica/obsoleto con HEAD/BODY. */
    public List<ArmorPiece> applicableArmor(ArmorHitLocation location, EquipmentState equipment) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(equipment);
        List<ArmorPiece> pieces = equipment.equippedArmor().stream().filter(piece -> piece.protects(location)).toList();
        if (location == ArmorHitLocation.BODY) {
            for (BodyArmorRegion region : BodyArmorRegion.values()) {
                double regionalCoverage = pieces.stream().mapToDouble(piece -> piece.bodyRegionCoverageRatio(region)).sum();
                // la superposición es legal; la cobertura efectiva se capa, no se rechaza.
                if (!Double.isFinite(regionalCoverage) || regionalCoverage < -EPSILON) {
                    throw new IllegalStateException("Cobertura regional inválida en " + region.label() + ".");
                }
            }
        }
        return pieces;
    }
}
