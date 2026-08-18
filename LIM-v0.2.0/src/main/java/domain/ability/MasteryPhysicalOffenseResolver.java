package domain.ability;

import domain.combat.PhysicalDamage;
import java.util.Objects;

/** RECICLAJE/AURA DE PULSIÓN ya no aplican amplificación contundente global. */
public final class MasteryPhysicalOffenseResolver {
    public enum Source { MELEE, UNARMED, RANGED_PROJECTILE, THROWN }
    public PhysicalDamage resolveGross(PhysicalDamage baseGross, Source source, MasteryEffectRegistry effects) {
        Objects.requireNonNull(source); Objects.requireNonNull(effects);
        return Objects.requireNonNull(baseGross);
    }
}
