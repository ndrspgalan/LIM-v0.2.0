package application;

public record InventoryAccessResult(boolean allowed, String message) {
    public InventoryAccessResult {
        message = message == null ? "" : message;
    }

    public static InventoryAccessResult granted() {
        return new InventoryAccessResult(true, "");
    }

    public static InventoryAccessResult denied(String message) {
        return new InventoryAccessResult(false, message);
    }
}
