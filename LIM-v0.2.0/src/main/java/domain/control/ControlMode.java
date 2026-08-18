package domain.control;

public enum ControlMode {
    KEYBOARD_MOUSE("TECLADO Y RATÓN"),
    PS4_CONTROLLER("MANDO PS4");

    private final String label;

    ControlMode(String label) { this.label = label; }
    public String label() { return label; }
}
