package domain.combat.runic;

import domain.combat.ArmorCoverageResolver;
import domain.combat.ArmorMitigationPolicy;
import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorHitLocation;
import domain.inventory.item.armor.ArmorPiece;
import java.util.Objects;

/** Filtra Maldición mediante protección contundente porcentual sin desgaste ni tambaleo físico. */
public final class PhonemicDerivationResolver {
    private final ArmorCoverageResolver coverageResolver;
    public PhonemicDerivationResolver() { this(new ArmorCoverageResolver()); }
    public PhonemicDerivationResolver(ArmorCoverageResolver coverageResolver) { this.coverageResolver = Objects.requireNonNull(coverageResolver); }

    public Result resolve(double rawCurseDamage, ArmorHitLocation location, EquipmentState equipment,
                          double curseResistancePercent, boolean derivationActive) {
        if (!Double.isFinite(rawCurseDamage) || rawCurseDamage < 0) throw new IllegalArgumentException("Daño inválido.");
        if (!Double.isFinite(curseResistancePercent) || curseResistancePercent < 0 || curseResistancePercent > 100) {
            throw new IllegalArgumentException("Resistencia inválida.");
        }
        double afterArmor = rawCurseDamage;
        if (derivationActive) {
            var pieces = coverageResolver.applicableArmor(location, equipment);
            double covered = pieces.stream().mapToDouble(piece -> piece.coverageRatio(location)).sum();
            afterArmor = rawCurseDamage * Math.max(0, 1.0 - covered);
            for (ArmorPiece piece : pieces) {
                double branch = rawCurseDamage * piece.coverageRatio(location);
                afterArmor += ArmorMitigationPolicy.transmitted(branch, piece.currentProtection().blunt());
            }
        }
        double net = afterArmor * (1.0 - curseResistancePercent / 100.0);
        return new Result(rawCurseDamage, afterArmor, net);
    }
    public record Result(double rawCurseDamage, double afterArmor, double netCurseDamage) {}
}
