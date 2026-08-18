package application.mdpar.representation.v1;

/** Estado de conocimiento que acompaña a cada hecho expuesto a MDPAR. */
public enum EpistemicStateV1 {
    EXACT,
    OBSERVED,
    INFERRED,
    LAST_KNOWN,
    UNKNOWN
}
