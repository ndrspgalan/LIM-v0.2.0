package domain.inventory.item.armor;

import java.util.Collection;
import java.util.Objects;

/** penalización biomecánica continua por cobertura de extremidades inferiores. FEET completa el antiguo 35%. */
public final class ArmorErgonomicsPolicy {
    private ArmorErgonomicsPolicy() {}
    public static final double LOWER_LIMB_MAX_COVERAGE = 0.35;

    public static double lowerLimbStaminaMultiplier(Collection<ArmorPiece> pieces) {
        Objects.requireNonNull(pieces);
        double heavy = 0, medium = 0;
        for (ArmorPiece p : pieces) {
            double c = p.bodyRegionCoverageRatio(BodyArmorRegion.LEGGINGS) + p.bodyRegionCoverageRatio(BodyArmorRegion.FEET);
            if (p.materialClass() == ArmorMaterialClass.HEAVY) heavy += c;
            else if (p.materialClass() == ArmorMaterialClass.MEDIUM) medium += c;
        }
        heavy = Math.min(LOWER_LIMB_MAX_COVERAGE, heavy);
        medium = Math.min(LOWER_LIMB_MAX_COVERAGE - heavy, medium);
        // HEAVY: 35% => x2. MEDIUM: 35% => x1.15. LIGHT no penaliza.
        return 1.0 + (heavy / LOWER_LIMB_MAX_COVERAGE) * 1.0 + (medium / LOWER_LIMB_MAX_COVERAGE) * 0.15;
    }

    /** FEET nunca recibe bonificación logística de peso, aunque sí participa en PA. */
    public static boolean eligibleForEquippedWeightBonus(BodyArmorRegion region) { return region != BodyArmorRegion.FEET; }
}
