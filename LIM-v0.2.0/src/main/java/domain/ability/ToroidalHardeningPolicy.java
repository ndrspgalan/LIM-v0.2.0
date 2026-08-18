package domain.ability;

import domain.combat.PhysicalDamage;
import domain.combat.StaggerResult;

import java.util.Objects;

/** ENDURECIMIENTO POTENCIAL: conserva el efecto reactivo melee, disparado al vaciar PA mientras está sostenido. */
public final class ToroidalHardeningPolicy {
    public ReflectionResult resolveMeleeImpact(PhysicalDamage realDamageThatWouldBeReceived,
                                               StaggerResult realStaggerThatWouldBeReceived,
                                               boolean active) {
        Objects.requireNonNull(realDamageThatWouldBeReceived);
        Objects.requireNonNull(realStaggerThatWouldBeReceived);
        if (!active) return new ReflectionResult(false, realDamageThatWouldBeReceived, realStaggerThatWouldBeReceived);
        return new ReflectionResult(true, realDamageThatWouldBeReceived, realStaggerThatWouldBeReceived);
    }

    public record ReflectionResult(boolean reflectedToAttacker, PhysicalDamage reflectedDamage,
                                   StaggerResult reflectedStagger) {}
}
