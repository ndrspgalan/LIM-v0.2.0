package domain.inventory.item.meleeWeapons.special;

import domain.inventory.item.WeaponItem;
import domain.inventory.item.WeaponTrait;
import java.util.Objects;

/** Ataque fuerte monomanual excepcional: si hay carga y el golpe conecta añade Electricidad 33. */
public final class ElectroMechanicalMacePolicy {
    public static final double RECHARGE_SECONDS = 12.0;
    public static final int ELECTRICITY_DAMAGE = 33;

    public boolean sparksVisible(WeaponItem mace, ElectroMechanicalMaceState state) {
        validateMace(mace); return Objects.requireNonNull(state).charged();
    }

    public void advanceRealTime(WeaponItem mace, ElectroMechanicalMaceState state, double seconds) {
        validateMace(mace); Objects.requireNonNull(state).advance(seconds);
    }

    public ElectroMechanicalMaceImpact resolveHeavyImpact(WeaponItem mace, ElectroMechanicalMaceState state,
                                                            boolean impactLanded) {
        validateMace(mace); Objects.requireNonNull(state);
        boolean chargedBefore = state.charged();
        int electricity = impactLanded && chargedBefore ? ELECTRICITY_DAMAGE : 0;
        boolean consumed = electricity > 0;
        if (consumed) state.discharge();
        return new ElectroMechanicalMaceImpact(true, impactLanded, electricity, consumed, state.charged());
    }

    /** el contacto puede ser local, pero la descarga eléctrica se distribuye por el organismo completo. */
    public ElectroMechanicalMaceResolvedImpact resolveHeavyImpact(WeaponItem mace, ElectroMechanicalMaceState state,
            boolean impactLanded, domain.inventory.equipment.EquipmentState equipment, double electricityResistancePercent) {
        ElectroMechanicalMaceImpact mechanical = resolveHeavyImpact(mace,state,impactLanded);
        domain.combat.WholeBodyElectricalImpactResult electrical = new domain.combat.NonConventionalDamageResolver()
                .resolveWholeBodyElectricity(mechanical.electricityDamage(), equipment, electricityResistancePercent);
        return new ElectroMechanicalMaceResolvedImpact(mechanical,electrical);
    }

    private static void validateMace(WeaponItem mace) {
        Objects.requireNonNull(mace, "La maza no puede ser nula.");
        if (!mace.hasTrait(WeaponTrait.ELECTRO_MECHANICAL_HEAVY)) {
            throw new IllegalArgumentException("El arma no dispone del ataque fuerte electro-mecánico.");
        }
    }
}
