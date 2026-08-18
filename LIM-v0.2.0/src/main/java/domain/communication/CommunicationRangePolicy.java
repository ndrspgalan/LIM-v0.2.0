package domain.communication;

import domain.environment.time.WeatherProfile;
import domain.environment.time.TerrainWeatherPolicy;
import domain.worldmemory.spatial.TerrainSurface;

import java.util.Objects;

/**
 * : alcance climático.
 * - Aeronauta: comunicación terrestre/inducción; el estado del suelo gobierna el acoplamiento.
 * - Panóptico: enlace fotónico en línea de visión; la visibilidad atmosférica gobierna el alcance.
 */
public final class CommunicationRangePolicy {
    public static final double AERONAUT_MIN_METERS=10.0;
    public static final double AERONAUT_MAX_METERS=100.0;
    public static final double PANOPTICON_MIN_METERS=79.0;
    public static final double PANOPTICON_MAX_METERS=213.0;
    private static final double MIN_CANONICAL_VISIBILITY=0.12;

    private CommunicationRangePolicy(){}

    public static double rangeMeters(CommunicationDeviceType device, WeatherProfile profile){
        Objects.requireNonNull(device);
        Objects.requireNonNull(profile);
        return rangeMeters(device, profile, TerrainSurface.UNKNOWN);
    }

    /** el intercom consume la misma política binaria de terreno/clima que TOMA A TIERRA. */
    public static double rangeMeters(CommunicationDeviceType device, WeatherProfile profile, TerrainSurface surface){
        Objects.requireNonNull(device);
        Objects.requireNonNull(profile);
        Objects.requireNonNull(surface);
        return switch(device){
            case AERONAUT_INTERCOM -> terrestrialRange(profile, surface);
            case PANOPTICON -> photonicRange(profile);
        };
    }

    private static double terrestrialRange(WeatherProfile profile, TerrainSurface surface){
        // El casco declara que el intercom necesita una toma a tierra externa.
        // Si asfalto o lluvia vetan esa ruta, no existe enlace terrestre efectivo.
        return TerrainWeatherPolicy.allowsGrounding(surface, profile) ? AERONAUT_MAX_METERS : 0.0;
    }

    private static double photonicRange(WeatherProfile profile){
        double v=Math.max(MIN_CANONICAL_VISIBILITY, Math.min(1.0, profile.visibilityFactor()));
        double normalized=(v-MIN_CANONICAL_VISIBILITY)/(1.0-MIN_CANONICAL_VISIBILITY);
        return PANOPTICON_MIN_METERS + normalized*(PANOPTICON_MAX_METERS-PANOPTICON_MIN_METERS);
    }
}
