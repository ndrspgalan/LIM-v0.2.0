package domain.worldmemory.spatial;

import java.util.Objects;

public record RememberedPosition(
        WorldCoordinate coordinate,
        double uncertaintyRadiusMeters,
        SpatialPrecision precision
) {
    public RememberedPosition {
        Objects.requireNonNull(coordinate, "La coordenada recordada no puede ser nula.");
        Objects.requireNonNull(precision, "La precisión espacial no puede ser nula.");
        if (!Double.isFinite(uncertaintyRadiusMeters) || uncertaintyRadiusMeters < 0) {
            throw new IllegalArgumentException("El radio de incertidumbre debe ser finito y no negativo.");
        }
        if (precision == SpatialPrecision.VERIFIED && uncertaintyRadiusMeters != 0) {
            throw new IllegalArgumentException("Una posición verificada no puede conservar incertidumbre espacial.");
        }
    }

    public static RememberedPosition verified(WorldCoordinate coordinate) {
        return new RememberedPosition(coordinate, 0, SpatialPrecision.VERIFIED);
    }
}
