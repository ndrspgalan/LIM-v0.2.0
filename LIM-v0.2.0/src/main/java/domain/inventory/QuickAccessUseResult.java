package domain.inventory;

public record QuickAccessUseResult(boolean allowed, String message) {
    public QuickAccessUseResult {
        if (message == null) message = "";
    }
    public static QuickAccessUseResult permitted() { return new QuickAccessUseResult(true, ""); }
    public static QuickAccessUseResult rejected(String message) { return new QuickAccessUseResult(false, message); }
}
