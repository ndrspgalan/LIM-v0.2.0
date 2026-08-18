package domain.combat.ai.memory;

import domain.combat.ai.execution.CombatAction;
import domain.combat.ai.observation.AttackSourceType;
import domain.inventory.item.WeaponCombatAction;
import java.util.Objects;

/** Consecuencia observada de una respuesta defensiva, sin declarar que sea buena o mala. */
public record DefensiveOutcome(
        AttackSourceType sourceType,
        WeaponCombatAction incomingAction,
        CombatAction response,
        boolean avoided,
        double residualDamage,
        double residualStaggerSeconds,
        double resourceCost,
        double combatTimeSeconds
) {
    public DefensiveOutcome {
        Objects.requireNonNull(sourceType); Objects.requireNonNull(incomingAction); Objects.requireNonNull(response);
        if (!Double.isFinite(residualDamage) || residualDamage < 0) throw new IllegalArgumentException("Daño residual inválido.");
        if (!Double.isFinite(residualStaggerSeconds) || residualStaggerSeconds < 0) throw new IllegalArgumentException("Stagger residual inválido.");
        if (!Double.isFinite(resourceCost) || resourceCost < 0) throw new IllegalArgumentException("Coste inválido.");
        if (!Double.isFinite(combatTimeSeconds) || combatTimeSeconds < 0) throw new IllegalArgumentException("Tiempo inválido.");
    }
}
