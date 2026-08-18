package domain.inventory.item.meleeWeapons.special;

import domain.combat.WholeBodyElectricalImpactResult;

public record ElectroMechanicalMaceResolvedImpact(
        ElectroMechanicalMaceImpact mechanical,
        WholeBodyElectricalImpactResult electrical
) {}
