package application.mdpar.boundary.v1;

/** Estados observables del lifecycle. Ningún estado crea una decisión local en LIM. */
public enum MdparRequestLifecycleStateV1 {
    CREATED,
    SERIALIZED,
    SENT,
    RECEIVED,
    ACCEPTED,
    ACTIVE,
    COMPLETED,
    TIMED_OUT,
    SUPERSEDED,
    INVALID,
    TRANSPORT_ERROR
}
