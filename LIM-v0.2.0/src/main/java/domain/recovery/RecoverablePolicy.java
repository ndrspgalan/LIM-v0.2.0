package domain.recovery;

/** Política transversal RECOVERABLE compartida por arrojadizos, flechas y virotes. */
public final class RecoverablePolicy {
    public boolean canRecover(boolean recoverable, boolean destinationAvailable) {
        return recoverable && destinationAvailable;
    }
}
