package domain.ability;

import domain.combat.PhysicalDamage;
import domain.combat.StaggerPolicy;
import domain.combat.StaggerResult;

/** EXPLOSIÓN CINÉTICA: radio 2,5 x altura, B bruto = AGUANTE y retroceso por StaggerPolicy. */
public final class KineticExplosionPolicy {
    public Result resolve(double heightMeters, int endurance) {
        return resolveAgainst(heightMeters, endurance, 0.0);
    }

    public Result resolveAgainst(double heightMeters, int endurance, double targetPhysicalStability) {
        if (!Double.isFinite(heightMeters) || heightMeters <= 0) throw new IllegalArgumentException("Altura inválida.");
        if (endurance < 0 || !Double.isFinite(targetPhysicalStability) || targetPhysicalStability < 0)
            throw new IllegalArgumentException("Parámetros inválidos.");
        double damage = endurance;
        double recoil = Math.max(0.0, damage - targetPhysicalStability);
        return new Result(heightMeters * 2.5, new PhysicalDamage(0, 0, damage), StaggerPolicy.resolve(recoil));
    }

    public java.util.Optional<Result> resolveWhenStaminaEmpty(double heightMeters, int endurance,
                                                               boolean sustained, double currentStamina) {
        if (!sustained || !new MalignantEnergyRefinementPolicy().canTrigger(currentStamina)) return java.util.Optional.empty();
        return java.util.Optional.of(resolve(heightMeters, endurance));
    }

    public record Result(double radiusMeters, PhysicalDamage grossDamage, StaggerResult stagger) {}
}
