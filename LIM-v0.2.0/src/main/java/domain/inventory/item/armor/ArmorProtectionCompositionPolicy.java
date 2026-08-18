package domain.inventory.item.armor;

import java.util.List;
import java.util.Objects;

/** Cálculos trazables de protección a partir de capas o proporciones defensivas. */
public final class ArmorProtectionCompositionPolicy {
    private ArmorProtectionCompositionPolicy() {}

    public static ArmorProtectionProfile additiveLayers(List<ArmorMaterialLayer> layers) {
        Objects.requireNonNull(layers);
        double p = 0, c = 0, b = 0;
        for (ArmorMaterialLayer layer : layers) {
            ArmorProtectionProfile v = layer.additiveProtection();
            p += v.piercing(); c += v.slashing(); b += v.blunt();
        }
        return new ArmorProtectionProfile(p, c, b);
    }

    public static ArmorProtectionProfile weightedMaterials(List<ArmorMaterialShare> shares) {
        Objects.requireNonNull(shares);
        double total = shares.stream().mapToDouble(ArmorMaterialShare::ratio).sum();
        if (Math.abs(total - 1.0) > 1.0e-9) throw new IllegalArgumentException("Las proporciones deben sumar 1.");
        double p = 0, c = 0, b = 0;
        for (ArmorMaterialShare share : shares) {
            ArmorProtectionProfile v = share.material().canonicalProtection();
            p += v.piercing() * share.ratio();
            c += v.slashing() * share.ratio();
            b += v.blunt() * share.ratio();
        }
        return rounded(p, c, b);
    }

    public static ArmorProtectionProfile weightedCoveredZones(List<CoveredZoneProtection> zones) {
        Objects.requireNonNull(zones);
        double coverage = zones.stream().mapToDouble(CoveredZoneProtection::coverageRatio).sum();
        if (coverage <= 0) throw new IllegalArgumentException("Debe existir cobertura positiva.");
        double p = 0, c = 0, b = 0;
        for (CoveredZoneProtection zone : zones) {
            p += zone.profile().piercing() * zone.coverageRatio();
            c += zone.profile().slashing() * zone.coverageRatio();
            b += zone.profile().blunt() * zone.coverageRatio();
        }
        return rounded(p / coverage, c / coverage, b / coverage);
    }

    private static ArmorProtectionProfile rounded(double p, double c, double b) {
        return new ArmorProtectionProfile(Math.round(p), Math.round(c), Math.round(b));
    }

    public record CoveredZoneProtection(double coverageRatio, ArmorProtectionProfile profile) {
        public CoveredZoneProtection {
            if (!Double.isFinite(coverageRatio) || coverageRatio <= 0 || coverageRatio > 1) {
                throw new IllegalArgumentException("La cobertura debe estar en (0,1].");
            }
            Objects.requireNonNull(profile);
        }
    }
}
