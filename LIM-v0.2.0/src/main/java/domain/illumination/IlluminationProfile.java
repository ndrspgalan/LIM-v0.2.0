package domain.illumination;
public record IlluminationProfile(double radiusMeters, IlluminationColor color, boolean active) {
    public IlluminationProfile {
        if (!Double.isFinite(radiusMeters) || radiusMeters < 0) throw new IllegalArgumentException("Radio luminoso inválido.");
        java.util.Objects.requireNonNull(color);
    }
}
