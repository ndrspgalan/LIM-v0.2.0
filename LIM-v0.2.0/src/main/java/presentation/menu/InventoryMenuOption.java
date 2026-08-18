package presentation.menu;

import java.util.Arrays;

public enum InventoryMenuOption {
    ACCEPT(1),
    INSPECT_ITEMS(2),
    RETURN(3);

    private final int code;
    InventoryMenuOption(int code) { this.code = code; }
    public int code() { return code; }

    public static InventoryMenuOption fromCode(int code) {
        return Arrays.stream(values()).filter(option -> option.code == code).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Opción de inventario desconocida: " + code));
    }
}
