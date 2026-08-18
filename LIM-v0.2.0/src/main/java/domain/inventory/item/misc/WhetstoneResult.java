package domain.inventory.item.misc;

public record WhetstoneResult(boolean successful, double durationRealSeconds, boolean durabilityConsumed) {
    public static WhetstoneResult rejected() { return new WhetstoneResult(false, 0.0, false); }
}
