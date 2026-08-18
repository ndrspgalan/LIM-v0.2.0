package domain.combat.ai.remote;

/** Estado operativo mínimo común de cualquier opción ofensiva remota. */
public enum RemoteReadiness {
    READY,
    NEEDS_AMMUNITION,
    NEEDS_RELOAD,
    NEEDS_CHARGE,
    RECOVERING,
    UNAVAILABLE
}
