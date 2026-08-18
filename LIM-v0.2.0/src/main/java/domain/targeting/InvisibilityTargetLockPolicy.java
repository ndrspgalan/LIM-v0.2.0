package domain.targeting;
/** Una entidad invisible no puede adquirirse ni conservarse como blanco fijado. */
public final class InvisibilityTargetLockPolicy {
    private InvisibilityTargetLockPolicy() {}
    public static boolean canLockTarget(boolean targetInvisible) { return !targetInvisible; }
    public static boolean retainsLock(boolean targetInvisible, boolean previouslyLocked) { return previouslyLocked && !targetInvisible; }
}
