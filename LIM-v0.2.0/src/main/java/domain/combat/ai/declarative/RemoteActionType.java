package domain.combat.ai.declarative;

/** Acciones remotas materialmente describibles por LIM; no implican preferencia táctica. */
public enum RemoteActionType {
    FIRE,
    THROW,
    RELOAD,
    ACQUIRE_AMMUNITION,
    CHARGE_WEAPON,
    WAIT_RECOVERY,
    TOGGLE_AIM
}
