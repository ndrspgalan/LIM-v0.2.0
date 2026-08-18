package domain.observation;

public record MonocularTelemetry(boolean available, double distanceMeters, String display) {
    public static MonocularTelemetry unavailable() { return new MonocularTelemetry(false, 0, "—"); }
    public static MonocularTelemetry measured(double distanceMeters) {
        if (!Double.isFinite(distanceMeters) || distanceMeters < 0) throw new IllegalArgumentException("La distancia debe ser finita y no negativa.");
        return new MonocularTelemetry(true, distanceMeters, String.format(java.util.Locale.ROOT, "%.1f m", distanceMeters));
    }
}
