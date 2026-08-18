package domain.settings;

public enum ScreenResolution {
    R1280X720("1280 x 720"),
    R1366X768("1366 x 768"),
    R1920X1080("1920 x 1080"),
    R2560X1440("2560 x 1440"),
    R3840X2160("3840 x 2160");

    private final String label;

    ScreenResolution(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
