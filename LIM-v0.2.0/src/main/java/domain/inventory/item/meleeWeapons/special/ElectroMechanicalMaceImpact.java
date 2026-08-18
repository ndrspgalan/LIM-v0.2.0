package domain.inventory.item.meleeWeapons.special;

public record ElectroMechanicalMaceImpact(boolean heavyAttackExecuted, boolean impactLanded,
                                           int electricityDamage, boolean chargeConsumed,
                                           boolean sparksVisibleAfterImpact) {}
