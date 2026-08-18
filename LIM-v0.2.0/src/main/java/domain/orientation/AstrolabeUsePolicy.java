package domain.orientation;

import domain.animation.CharacterAnimationState;
import domain.inventory.InventoryState;
import domain.inventory.QuickAccessUsePolicy;
import domain.inventory.item.misc.AstrolabeItem;
import domain.worldmemory.spatial.WorldCoordinate;
import domain.worldmemory.WorldMemory;

import java.util.Objects;

public final class AstrolabeUsePolicy {
    private final WorldOrientationService orientation = new WorldOrientationService();

    public AstrolabeUseResult orient(AstrolabeItem astrolabe, InventoryState inventory,
                                     WorldMemory memory, WorldCoordinate viewer,
                                     MovementState movement, CharacterAnimationState animation) {
        Objects.requireNonNull(astrolabe);
        Objects.requireNonNull(movement);
        Objects.requireNonNull(animation);
        if (!QuickAccessUsePolicy.isActiveEquipment(astrolabe, inventory.equipment()))
            return AstrolabeUseResult.rejected("El Astrolabio debe estar equipado como abalorio para orientarse con E.");
        if (!movement.canOrient()) {
            return AstrolabeUseResult.rejected(movement.moving()
                    ? "El personaje debe detenerse antes de orientarse."
                    : "Otra acción impide consultar el astrolabio.");
        }
        OrientationSolution solution = orientation.selectedDestination(memory, viewer);
        if (!solution.available()) return AstrolabeUseResult.rejected(solution.message());
        animation.orient();
        return new AstrolabeUseResult(true, true, solution, solution.message());
    }

    /** E sin destino espacial no inventa coordenadas: deriva a Memoria del Mundo. */
    public AstrolabeResult pressE(WorldMemory memory, WorldCoordinate viewer) {
        Objects.requireNonNull(memory); Objects.requireNonNull(viewer);
        OrientationSolution solution = orientation.selectedDestination(memory, viewer);
        return solution.available() ? new AstrolabeResult(AstrolabeAction.ORIENT, solution)
                : new AstrolabeResult(AstrolabeAction.OPEN_WORLD_MEMORY, solution);
    }

    public enum AstrolabeAction { ORIENT, OPEN_WORLD_MEMORY }
    public record AstrolabeResult(AstrolabeAction action, OrientationSolution orientation) {}
}
