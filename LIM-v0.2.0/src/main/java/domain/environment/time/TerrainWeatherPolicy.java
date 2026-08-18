package domain.environment.time;

import domain.worldmemory.spatial.TerrainSurface;

import java.util.Objects;

/**
 * : una sola semántica ambiental para TOMA A TIERRA, sismoscopio e intercom.
 * No intenta simular resistividad del suelo: el canon sólo distingue asfalto y lluvia activa.
 */
public final class TerrainWeatherPolicy {
    private TerrainWeatherPolicy() {}

    public static boolean isAsphalted(TerrainSurface surface) {
        return Objects.requireNonNull(surface) == TerrainSurface.ASPHALT;
    }

    public static boolean isRainy(WeatherProfile weather) {
        return switch (Objects.requireNonNull(weather).precipitation()) {
            case DRIZZLE, RAIN, HEAVY_RAIN -> true;
            default -> false;
        };
    }

    public static TerrainWeatherState resolve(TerrainSurface surface, WeatherProfile weather) {
        boolean asphalt = isAsphalted(surface);
        boolean rain = isRainy(weather);
        if (asphalt && rain) return TerrainWeatherState.ASPHALT_RAIN;
        if (asphalt) return TerrainWeatherState.ASPHALT_DRY;
        if (rain) return TerrainWeatherState.NATURAL_RAIN;
        return TerrainWeatherState.NATURAL_DRY;
    }

    /** Canon : la TOMA A TIERRA ambiental sólo existe fuera de asfalto y sin lluvia activa. */
    public static boolean allowsGrounding(TerrainSurface surface, WeatherProfile weather) {
        return resolve(surface, weather) == TerrainWeatherState.NATURAL_DRY;
    }
}
