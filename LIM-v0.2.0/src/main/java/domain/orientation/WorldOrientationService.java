package domain.orientation;

import domain.worldmemory.spatial.WorldCoordinate;
import domain.worldmemory.WorldMemory;

import java.util.Objects;

/** Cálculo espacial neutral. Desde  consume WorldMemoryEntry a través de un resolver
 * y deja RememberedIndicator fuera del contrato del Astrolabio. */
public final class WorldOrientationService {
    private final WorldMemorySpatialTargetResolver targetResolver = new WorldMemorySpatialTargetResolver();

    public OrientationSolution selectedDestination(WorldMemory memory, WorldCoordinate viewer) {
        Objects.requireNonNull(memory, "La Memoria del Mundo no puede ser nula.");
        Objects.requireNonNull(viewer, "La posición del observador no puede ser nula.");
        return targetResolver.selectedTarget(memory.knowledge())
                .map(target -> solve(viewer, target))
                .orElseGet(() -> OrientationSolution.unavailable(
                        "No hay ningún destino espacial válido seleccionado en la Memoria del Mundo."));
    }

    private OrientationSolution solve(WorldCoordinate viewer, WorldMemorySpatialTarget target) {
        WorldCoordinate destination = target.position().coordinate();
        double dx = destination.x() - viewer.x();
        double dy = destination.y() - viewer.y();
        double distance = Math.hypot(dx, dy);
        if (distance < 0.000001) {
            return new OrientationSolution(true, target.title(), OrientationDirection.HERE, 0.0,
                    target.position().uncertaintyRadiusMeters(), target.position().precision(), target.reliability(),
                    "El destino seleccionado coincide con la posición actual.");
        }
        double heading = Math.toDegrees(Math.atan2(dx, dy));
        if (heading < 0) heading += 360.0;
        return new OrientationSolution(true, target.title(), direction(heading), heading,
                target.position().uncertaintyRadiusMeters(), target.position().precision(), target.reliability(),
                "El astrolabio señala la dirección del destino seleccionado según la Memoria del Mundo vigente.");
    }

    static OrientationDirection direction(double degrees) {
        int sector = (int) Math.floor((degrees + 22.5) / 45.0) % 8;
        return switch (sector) {
            case 0 -> OrientationDirection.NORTH;
            case 1 -> OrientationDirection.NORTH_EAST;
            case 2 -> OrientationDirection.EAST;
            case 3 -> OrientationDirection.SOUTH_EAST;
            case 4 -> OrientationDirection.SOUTH;
            case 5 -> OrientationDirection.SOUTH_WEST;
            case 6 -> OrientationDirection.WEST;
            default -> OrientationDirection.NORTH_WEST;
        };
    }
}
