package domain.inventory.item.armor;

import java.util.Objects;

/** masa contextual bajo EMPAPADO. PAPER conserva su estado WET propio y HEAVY sus reglas específicas. */
public final class SoakedEquipmentWeightPolicy {
    private SoakedEquipmentWeightPolicy() {}

    public static double effectiveWeightKg(ArmorPiece piece, boolean soaked) {
        Objects.requireNonNull(piece);
        double current = piece.weightKg(); // incluye WET de PAPER si procede
        if (!soaked || piece.containsMaterial(ArmorMaterial.PAPER) || piece.materialClass() == ArmorMaterialClass.HEAVY) return current;
        return switch (piece.material()) {
            case HARDENED_LEATHER -> current * 1.20;
            case CLOTH, DIELECTRIC_CLOTH -> current * 3.00;
            default -> current;
        };
    }
}
