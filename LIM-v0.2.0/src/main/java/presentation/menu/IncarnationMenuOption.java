package presentation.menu;

import java.util.Arrays;

public enum IncarnationMenuOption {
    EXISTING_PERSON(1),
    NEW_PERSON(2),
    RETURN(0);

    private final int code;

    IncarnationMenuOption(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public static IncarnationMenuOption fromCode(int code) {
        return Arrays.stream(values())
                .filter(option -> option.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Opción de encarnación desconocida: " + code));
    }
}
