package domain.ability;

import domain.combat.PhysicalDamage;
import domain.combat.StaggerResult;
import java.util.Objects;

/** ENDURECIMIENTO POTENCIAL sólo materializa su respuesta cuando la sostenida coincide con PA=0. */
public final class PotentialHardeningPolicy {
    public ToroidalHardeningPolicy.ReflectionResult resolveMeleeImpact(
            PhysicalDamage realDamageThatWouldBeReceived,
            StaggerResult realStaggerThatWouldBeReceived,
            boolean sustained,
            double currentStamina) {
        Objects.requireNonNull(realDamageThatWouldBeReceived);
        Objects.requireNonNull(realStaggerThatWouldBeReceived);
        boolean triggered = sustained && new MalignantEnergyRefinementPolicy().canTrigger(currentStamina);
        return new ToroidalHardeningPolicy().resolveMeleeImpact(realDamageThatWouldBeReceived, realStaggerThatWouldBeReceived, triggered);
    }
}
