package domain.combat;

import domain.inventory.item.armor.ArmorProtectionProfile;

/** Especificación física de un escudo dedicado. */
public record ShieldSpecification(
        String name,
        double heightMeters,
        double widthMeters,
        double thicknessMeters,
        double weightKg,
        int verticalSlots,
        int horizontalSlots,
        ArmorProtectionProfile protection,
        double raisedCoverageRatio,
        double wearMultiplier,
        boolean repairableWithSteel
) {
    public ShieldSpecification {
        if (heightMeters<=0||widthMeters<=0||thicknessMeters<=0||weightKg<=0) throw new IllegalArgumentException("Geometría de escudo inválida.");
        if (raisedCoverageRatio<=0||raisedCoverageRatio>1) throw new IllegalArgumentException("Cobertura de escudo inválida.");
        if (wearMultiplier<0) throw new IllegalArgumentException("Desgaste inválido.");
    }
}
