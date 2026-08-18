package domain.combat;

import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.armor.ArmorMaterial;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.ItemPropertyId;
import domain.environment.time.WeatherProfile;
import domain.worldmemory.spatial.TerrainSurface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Resuelve veneno, quemadura, congelación y electricidad. Estas fuentes no
 * erosionan la armadura: solo utilizan la cobertura y las propiedades naturales
 * de su material.
 */
public final class NonConventionalDamageResolver {
    private static final double EPSILON = 1.0e-9;
    private final ArmorCoverageResolver coverageResolver = new ArmorCoverageResolver();

    public NonConventionalImpactResult resolve(
            DamageType type,
            double rawDamage,
            ArmorHitLocation location,
            EquipmentState equipment,
            double resistancePercent,
            boolean naturalConductor
    ) {
        Objects.requireNonNull(type, "El tipo de daño no puede ser nulo.");
        Objects.requireNonNull(location, "La zona no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        if (type.category() != DamageCategory.NON_CONVENTIONAL_PHYSICAL) {
            throw new IllegalArgumentException("Este resolvedor solo admite daño físico no convencional.");
        }
        if (rawDamage < 0) throw new IllegalArgumentException("El daño no puede ser negativo.");
        if (resistancePercent < 0 || resistancePercent > 100) {
            throw new IllegalArgumentException("La resistencia debe estar entre 0 y 100 %.");
        }

        List<ArmorPiece> pieces = coverageResolver.applicableArmor(location, equipment);
        boolean fullCoverage = pieces.stream().mapToDouble(piece -> piece.coverageRatio(location)).sum() >= 1.0 - EPSILON;
        boolean specializedImmunity =
                (type == DamageType.POISON && fullCoverage && equipment.hasArmorProperty(ItemPropertyId.INTEGRAL_SEAL))
                || (type == DamageType.BURN && equipment.hasArmorProperty(ItemPropertyId.FIREPROOF))
                || (type == DamageType.BURN && fullCoverage && equipment.hasArmorProperty(ItemPropertyId.THERMAL_CONTROL))
                || (type == DamageType.FROST && fullCoverage && equipment.hasArmorProperty(ItemPropertyId.THERMAL_CONTROL))
                || (type == DamageType.ELECTRICITY && equipment.hasArmorProperty(ItemPropertyId.INSULATING))
                || (type == DamageType.ELECTRICITY && fullCoverage && equipment.hasArmorProperty(ItemPropertyId.DIELECTRIC_ENVELOPE));
        if (specializedImmunity) {
            return new NonConventionalImpactResult(type, rawDamage, 0.0, resistancePercent, 0.0, 0.0, List.of());
        }
        double coverage = pieces.stream().mapToDouble(piece -> piece.coverageRatio(location)).sum();
        if (coverage > 1.0 + EPSILON) {
            throw new IllegalStateException("La cobertura acumulada no puede superar el 100 %.");
        }

        double materialAdjusted;
        List<String> amplified = new ArrayList<>();

        int stackedWeaknessPieces = 0;
        if (type == DamageType.ELECTRICITY) {
            for (ArmorPiece piece : pieces) {
                if (hasElectricalWeakness(piece)) { stackedWeaknessPieces++; amplified.add(piece.name()); }
            }
        } else if (type == DamageType.BURN) {
            for (ArmorPiece piece : pieces) {
                if (isFlammable(piece)) { stackedWeaknessPieces++; amplified.add(piece.name()); }
            }
        }

        if (stackedWeaknessPieces > 0) {
            // Canon : x2 por cada pieza vulnerable aplicable (1 -> x2, 2 -> x4, 3 -> x6).
            materialAdjusted = rawDamage * (2.0 * stackedWeaknessPieces);
        } else {
            materialAdjusted = rawDamage * Math.max(0, 1.0 - coverage);
            for (ArmorPiece piece : pieces) {
                double branch = rawDamage * piece.coverageRatio(location);
                double multiplier = passiveMaterialMultiplier(piece, type);
                branch *= multiplier;
                if (multiplier > 1.0) amplified.add(piece.name());
                materialAdjusted += branch;
            }
        }

        double effectiveResistance = type == DamageType.ELECTRICITY && naturalConductor
                ? 0.0
                : resistancePercent;
        double net = materialAdjusted * (1.0 - effectiveResistance / 100.0);
        double stunSeconds = type == DamageType.ELECTRICITY ? ElectricStunPolicy.stunSeconds(net) : 0.0;
        return new NonConventionalImpactResult(
                type, rawDamage, materialAdjusted, effectiveResistance, net, stunSeconds, amplified
        );
    }

    /** la electricidad se distribuye por HEAD/BODY aunque el contacto inicial sea localizado. */
    public WholeBodyElectricalImpactResult resolveWholeBodyElectricity(
            double rawDamage, EquipmentState equipment, double resistancePercent) {
        AreaBodyDistributionPolicy.Split split = AreaBodyDistributionPolicy.split(rawDamage);
        boolean grounded = domain.inventory.equipment.GroundingPolicy.fullBodyGroundingPath(equipment);
        NonConventionalImpactResult head = resolve(DamageType.ELECTRICITY, split.head(), ArmorHitLocation.HEAD, equipment, resistancePercent, !grounded);
        NonConventionalImpactResult body = resolve(DamageType.ELECTRICITY, split.body(), ArmorHitLocation.BODY, equipment, resistancePercent, !grounded);
        return new WholeBodyElectricalImpactResult(head, body, grounded);
    }

    /** variante efectiva que incluye asfalto y lluvia en la resolución de TOMA A TIERRA. */
    public WholeBodyElectricalImpactResult resolveWholeBodyElectricity(
            double rawDamage, EquipmentState equipment, double resistancePercent,
            TerrainSurface surface, WeatherProfile weather) {
        AreaBodyDistributionPolicy.Split split = AreaBodyDistributionPolicy.split(rawDamage);
        boolean grounded = domain.inventory.equipment.GroundingPolicy.fullBodyGroundingPath(equipment, surface, weather);
        NonConventionalImpactResult head = resolve(DamageType.ELECTRICITY, split.head(), ArmorHitLocation.HEAD, equipment, resistancePercent, !grounded);
        NonConventionalImpactResult body = resolve(DamageType.ELECTRICITY, split.body(), ArmorHitLocation.BODY, equipment, resistancePercent, !grounded);
        return new WholeBodyElectricalImpactResult(head, body, grounded);
    }

    /**
     * : la debilidad eléctrica pertenece a cada pieza aplicable.
     * El contacto con el terreno no borra el x2 de acero/bronce; sólo una excepción
     * constructiva explícita de esa propia pieza puede suprimirlo.
     */
    private static boolean hasElectricalWeakness(ArmorPiece piece) {
        if (piece.hasProperty(ItemPropertyId.ELECTRICAL_WEAKNESS_SUPPRESSED)) return false;
        return piece.hasProperty(ItemPropertyId.ELECTRICAL_CONDUCTOR)
                || piece.material() == ArmorMaterial.STEEL
                || piece.material() == ArmorMaterial.BRONZE;
    }

    private static boolean isFlammable(ArmorPiece piece) {
        return piece.hasProperty(ItemPropertyId.FLAMMABLE)
                || piece.material() == ArmorMaterial.WOOD
                || piece.material() == ArmorMaterial.EBONY_WOOD;
    }

    private static double passiveMaterialMultiplier(ArmorPiece piece, DamageType type) {
        // Las vulnerabilidades x2 se resuelven arriba por pieza; aquí permanecen sólo resistencias naturales.
        if (piece.hasProperty(ItemPropertyId.ANTI_CORROSIVE) && type == DamageType.POISON) return 0.75;
        return 1.0;
    }

}
