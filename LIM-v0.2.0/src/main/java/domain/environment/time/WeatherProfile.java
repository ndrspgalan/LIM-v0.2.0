package domain.environment.time;

import java.util.Objects;

public record WeatherProfile(
        int apparentTemperatureCelsius,
        double visibilityFactor,
        double luminosityFactor,
        double windIntensity,
        PrecipitationKind precipitation,
        GroundCondition groundCondition,
        double environmentalToxicity,
        double extraFatigueFactor
) {
    public WeatherProfile {
        if (visibilityFactor < 0.0 || visibilityFactor > 1.0) throw new IllegalArgumentException("La visibilidad debe estar entre 0 y 1.");
        if (luminosityFactor < 0.0 || luminosityFactor > 1.5) throw new IllegalArgumentException("La luminosidad debe estar entre 0 y 1,5.");
        if (windIntensity < 0.0 || windIntensity > 1.0) throw new IllegalArgumentException("El viento debe estar entre 0 y 1.");
        if (environmentalToxicity < 0.0 || environmentalToxicity > 1.0) throw new IllegalArgumentException("La toxicidad debe estar entre 0 y 1.");
        if (extraFatigueFactor < 0.0) throw new IllegalArgumentException("La fatiga adicional no puede ser negativa.");
        precipitation = Objects.requireNonNull(precipitation);
        groundCondition = Objects.requireNonNull(groundCondition);
    }
}
