package domain.combat;

public record ImprovisedBracerMeleeBlockResult(
        PhysicalDamage damageAfterArmor,
        double armorCoveredRatio,
        double resistancePendingRatio,
        StaggerResult stagger
) {}
