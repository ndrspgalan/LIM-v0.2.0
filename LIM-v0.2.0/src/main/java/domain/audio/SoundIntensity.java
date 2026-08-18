package domain.audio;

public record SoundIntensity(double normalizedValue) {
    public SoundIntensity {
        if (normalizedValue < 0 || normalizedValue > 1) {
            throw new IllegalArgumentException("La intensidad debe estar entre 0 y 1.");
        }
    }

    public static SoundIntensity fixed(double value) {
        return new SoundIntensity(value);
    }

    public static SoundIntensity maximum() {
        return new SoundIntensity(1.0);
    }
}
