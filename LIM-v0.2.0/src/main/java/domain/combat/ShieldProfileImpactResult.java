package domain.combat;

import domain.inventory.item.armor.ArmorProtectionProfile;

public record ShieldProfileImpactResult(
        PhysicalDamage residualDamage,
        ArmorProtectionProfile remainingProtection,
        ArmorProfileWearResult wear,
        StaggerResult stagger
) {}
