package domain.inventory.item.misc;

public record IgnitionResult(boolean successful, UtilityAction action, double durationRealSeconds,
                             boolean amadouConsumed, boolean flintDurabilityConsumed) {
    public static IgnitionResult rejected(UtilityAction action) {
        return new IgnitionResult(false, action, 0.0, false, false);
    }
}
