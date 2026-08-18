package presentation.menu;

import java.util.Arrays;

public enum MainMenuOption {
    ENCARNAR(1, "Encarnar"),
    CONFIGURAR(2, "Configurar"),
    MEMORAR(3, "Memorar"),
    ABANDONAR(4, "Abandonar");

    private final int code;
    private final String label;

    MainMenuOption(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int code() {
        return code;
    }

    public String label() {
        return label;
    }

    public static MainMenuOption fromCode(int code) {
        return Arrays.stream(values())
                .filter(option -> option.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Opción de menú desconocida: " + code));
    }
}
