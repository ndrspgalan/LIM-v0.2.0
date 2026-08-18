package domain.combat.ai.remote;

import domain.combat.ai.execution.CombatAction;

/** Traducción arquitectónica de readiness a la acción preparatoria común. */
public final class RemotePreparationPolicy {
    private RemotePreparationPolicy() {}

    public static CombatAction requiredAction(RemoteReadiness readiness) {
        return switch (readiness) {
            case READY -> CombatAction.RANGED_ATTACK;
            case NEEDS_AMMUNITION, NEEDS_RELOAD -> CombatAction.RELOAD;
            case NEEDS_CHARGE -> CombatAction.CHARGE_WEAPON;
            case RECOVERING -> CombatAction.WAIT;
            case UNAVAILABLE -> CombatAction.SWITCH_WEAPON;
        };
    }
}
