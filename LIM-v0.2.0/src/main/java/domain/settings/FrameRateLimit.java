package domain.settings;

public enum FrameRateLimit {
    FPS_30("30 FPS"),
    FPS_60("60 FPS"),
    FPS_120("120 FPS"),
    UNLIMITED("SIN LÍMITE");

    private final String label;

    FrameRateLimit(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
