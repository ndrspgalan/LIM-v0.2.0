package domain.combat.runic;

import domain.combat.MentalPressurePolicy;
import domain.combat.PhysicalDamage;
import domain.combat.StaggerPolicy;
import java.util.Objects;

/** Resuelve los canales físico y maldito y suma sus retrocesos antes de la curva de tambaleo. */
public final class CompositeImpactResolver {
    public CompositeImpact resolve(PhysicalDamage physicalNet, double curseRaw,
                                   double curseResistancePercent,
                                   double physicalStability, double sanity) {
        Objects.requireNonNull(physicalNet, "El daño físico neto no puede ser nulo.");
        validateNonNegative(physicalStability, "estabilidad física");
        var curse = MentalPressurePolicy.resolve(curseRaw, curseResistancePercent, sanity);
        double physicalRecoil = Math.max(0.0, physicalNet.blunt() - physicalStability);
        double mentalRecoil = curse.mentalRecoilUnits();
        return new CompositeImpact(physicalNet, curseRaw, curse.netDamage(), physicalRecoil, mentalRecoil,
                StaggerPolicy.resolve(physicalRecoil + mentalRecoil));
    }
    private static void validateNonNegative(double value, String label) {
        if (!Double.isFinite(value) || value < 0) throw new IllegalArgumentException("Valor inválido de " + label + ".");
    }
}
