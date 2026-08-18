package domain.settings;

public record BindingUpdateResult(boolean accepted, String message) {
    public static BindingUpdateResult accepted(String message) {
        return new BindingUpdateResult(true, message);
    }

    public static BindingUpdateResult rejected(String message) {
        return new BindingUpdateResult(false, message);
    }
}
