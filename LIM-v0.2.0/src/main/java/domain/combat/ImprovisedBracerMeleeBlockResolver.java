package domain.combat;

import domain.inventory.equipment.EquipmentState;
import domain.inventory.item.armor.ArmorPiece;
import domain.inventory.item.armor.ArmorProtectionProfile;

import java.util.Objects;

/** El brazal recibe todo el impacto, pero solo su cobertura corporal queda respaldada por su protección porcentual. */
public final class ImprovisedBracerMeleeBlockResolver {
    private final ImprovisedBracerBlockPolicy policy = new ImprovisedBracerBlockPolicy();

    public ImprovisedBracerMeleeBlockResult resolve(PhysicalDamage grossDamage, EquipmentState equipment) {
        Objects.requireNonNull(grossDamage, "El daño no puede ser nulo.");
        ArmorPiece bracer = policy.activeBracer(equipment);
        double covered = bracer.bodyCoverageRatio();
        double pending = 1.0 - covered;
        ArmorProtectionProfile protection = bracer.currentProtection();
        PhysicalDamage protectedBranch = grossDamage.scaledBy(covered);
        PhysicalDamage net = grossDamage.scaledBy(pending).plus(new PhysicalDamage(
                ArmorMitigationPolicy.transmitted(protectedBranch.piercing(), protection.piercing()),
                ArmorMitigationPolicy.transmitted(protectedBranch.slashing(), protection.slashing()),
                ArmorMitigationPolicy.transmitted(protectedBranch.blunt(), protection.blunt())
        ));
        return new ImprovisedBracerMeleeBlockResult(net, covered, pending,
                StaggerPolicy.resolve(grossDamage.blunt()));
    }
}
