package domain.inventory.item.armor;

import domain.combat.ai.observation.AttackSourceType;

import java.util.Collection;
import java.util.Objects;

/**
 * : composición regional independiente de la ranura principal de una prenda.
 * Cada pieza aporta cobertura y protección a todas las regiones que declara.
 * La cobertura anatómica se suma hasta el máximo de la región; la protección de
 * las superficies superpuestas se suma ponderada por la fracción regional y
 * conserva el guardarraíl porcentual 0..100 de ArmorProtectionProfile.
 */
public final class BodyArmorCoverageCompositionPolicy {
    private BodyArmorCoverageCompositionPolicy() {}

    public static double effectiveCoverage(Collection<ArmorPiece> pieces, BodyArmorRegion region) {
        Objects.requireNonNull(pieces);
        Objects.requireNonNull(region);
        double sum = pieces.stream().mapToDouble(p -> p.bodyRegionCoverageRatio(region)).sum();
        return Math.min(region.maximumCoverageRatio(), sum);
    }

    public static ArmorProtectionProfile effectiveProtection(Collection<ArmorPiece> pieces, BodyArmorRegion region) {
        return effectiveProtection(pieces, region, null);
    }

    public static ArmorProtectionProfile effectiveProtection(Collection<ArmorPiece> pieces, BodyArmorRegion region,
                                                              AttackSourceType sourceType) {
        Objects.requireNonNull(pieces);
        Objects.requireNonNull(region);
        double max = region.maximumCoverageRatio();
        double p = 0, c = 0, b = 0;
        for (ArmorPiece piece : pieces) {
            double coverage = piece.bodyRegionCoverageRatio(region);
            if (coverage <= 0) continue;
            ArmorProtectionProfile profile = sourceType == null ? piece.currentProtection() : piece.currentProtection(sourceType);
            double factor = coverage / max;
            p += profile.piercing() * factor;
            c += profile.slashing() * factor;
            b += profile.blunt() * factor;
        }
        return new ArmorProtectionProfile(p, c, b);
    }

    public static double totalBodyCoverage(Collection<ArmorPiece> pieces) {
        Objects.requireNonNull(pieces);
        double total = 0;
        for (BodyArmorRegion region : BodyArmorRegion.values()) total += effectiveCoverage(pieces, region);
        return Math.min(1.0, total);
    }
}
