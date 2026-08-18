package domain.targeting;

/** Resultado sobre el observador que estaba fijando al usuario de MIRAGE. */
public record MirageTargetLockResult(boolean targetLockTemporarilyLost,
                                     boolean targetLockAutomaticallyRestored) {
    public MirageTargetLockResult {
        if (targetLockAutomaticallyRestored && !targetLockTemporarilyLost) {
            throw new IllegalArgumentException("No puede restablecerse una fijación que no se perdió.");
        }
    }
}
