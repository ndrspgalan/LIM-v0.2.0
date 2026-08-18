package domain.bestiarium.interstice.faerie;

import domain.combat.MentalPressurePolicy;
import domain.combat.PhysicalDamage;
import domain.combat.StaggerPolicy;
import domain.combat.StaggerResult;

/**
 * Política ofensiva del Doppelgänger: todo daño neto no maldito se refleja como
 * Frenesí; Maldición conserva su canal. Ambos canales usan resistencias
 * porcentuales y CORDURA porcentual antes de alimentar el stagger mental.
 */
public final class DoppelgangerDamagePolicy {
    public Result transmute(PhysicalDamage netPhysical, double otherNetDamage, double curseRaw,
                            double frenzyResistancePercent, double curseResistancePercent,
                            double sanityPercent) {
        if (netPhysical == null) throw new IllegalArgumentException("Daño físico obligatorio.");
        if (!Double.isFinite(otherNetDamage) || otherNetDamage < 0) throw new IllegalArgumentException("El daño adicional no puede ser negativo.");
        double physicalAsFrenzy = netPhysical.piercing() + netPhysical.slashing() + netPhysical.blunt();
        double rawFrenzy = physicalAsFrenzy + otherNetDamage;
        var frenzy = MentalPressurePolicy.resolve(rawFrenzy, frenzyResistancePercent, sanityPercent);
        var curse = MentalPressurePolicy.resolve(curseRaw, curseResistancePercent, sanityPercent);
        double mentalRecoil = frenzy.mentalRecoilUnits() + curse.mentalRecoilUnits();
        return new Result(rawFrenzy, frenzy.netDamage(), curseRaw, curse.netDamage(),
                frenzy.mentalRecoilUnits(), curse.mentalRecoilUnits(), mentalRecoil,
                StaggerPolicy.resolve(mentalRecoil));
    }

    /** Compatibilidad con la API histórica: Maldición sin resistencia y CORDURA 0. */
    public Result transmute(PhysicalDamage netPhysical, double otherNetDamage, double curseRaw,
                            double frenzyResistancePercent) {
        return transmute(netPhysical, otherNetDamage, curseRaw, frenzyResistancePercent, 0.0, 0.0);
    }

    public record Result(double rawFrenzy, double netFrenzy, double rawCurse, double netCurse,
                         double frenzyMentalRecoilUnits, double curseMentalRecoilUnits,
                         double totalMentalRecoilUnits, StaggerResult stagger) {
        public double curseDamage() { return netCurse; }
        public double totalNetDamage() { return netFrenzy + netCurse; }
    }
}
