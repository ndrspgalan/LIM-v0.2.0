package domain.movement;

/** Datos internos de una acción de movilidad; no forman parte de la hoja de personaje. */
public record MobilityProfile(double feintDistanceMeters, double jumpDistanceMeters,
                              int feintAnimationFrames, int invulnerabilityFrames,
                              int framesPerSecond) {
    public MobilityProfile {
        if (feintDistanceMeters <= 0 || jumpDistanceMeters <= 0) {
            throw new IllegalArgumentException("Las distancias deben ser positivas.");
        }
        if (feintAnimationFrames <= 0 || invulnerabilityFrames < 0 || framesPerSecond <= 0) {
            throw new IllegalArgumentException("Fotogramas no válidos.");
        }
        if (invulnerabilityFrames > feintAnimationFrames) {
            throw new IllegalArgumentException("La invulnerabilidad no puede superar la animación.");
        }
    }

    public boolean grantsInvulnerability() { return invulnerabilityFrames > 0; }
    public double animationSeconds() { return feintAnimationFrames / (double) framesPerSecond; }
    public double invulnerabilitySeconds() { return invulnerabilityFrames / (double) framesPerSecond; }
}
