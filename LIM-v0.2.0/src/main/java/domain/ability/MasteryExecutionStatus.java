package domain.ability;

/** Estado tipado de una resolución de maestría. */
public enum MasteryExecutionStatus {
    EXECUTED,
    REJECTED,
    STARTED,
    INTERRUPTED,
    FINISHED,
    EFFECT_APPLIED,
    RESOURCE_CONSUMED
}
