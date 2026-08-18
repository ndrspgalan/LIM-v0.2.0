package domain.orientation;

import domain.ability.NullificationPolicy;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;
import domain.runic.RunicMarkActivityPolicy;
import domain.runic.RunicMarkId;
import domain.worldmemory.WorldMemory;
import domain.worldmemory.entry.WorldMemoryEntryId;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.SpatialPrecision;
import domain.worldmemory.spatial.WorldCoordinate;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * VIENTO GUÍA. La memoria sólo prueba que el objetivo está registrado;
 * la coordenada real procede del mundo y no se escribe en el mapa ni en WorldMemory.
 */
public final class CompassRoseGuidancePolicy {
    public OrientationSolution pressE(WorldMemory memory, WorldMemoryEntryId targetId, WorldCoordinate actualTarget,
                                      WorldCoordinate viewer, CharacterSheet sheet, EquipmentState equipment,
                                      NullificationPolicy.SuppressionState suppression) {
        Objects.requireNonNull(memory); Objects.requireNonNull(targetId); Objects.requireNonNull(actualTarget); Objects.requireNonNull(viewer);
        if (!RunicMarkActivityPolicy.active(RunicMarkId.ROSA_DE_LOS_VIENTOS, sheet, equipment, Set.of(), suppression))
            return OrientationSolution.unavailable("Rosa de los Vientos no está activa.");
        var known = memory.knowledge().entry(targetId);
        if (known.isEmpty()) return OrientationSolution.unavailable("El objetivo no está registrado en la Memoria del Mundo.");
        // Si la memoria ya contiene posición, respetamos su precisión cartográfica; si no, el viento usa la posición real sin revelarla al mapa.
        RememberedPosition p = known.get().spatialMemory().orElseGet(() ->
                RememberedPosition.verified(actualTarget));
        return solve(viewer, known.get().title(), p, known.get().spatialMemory().isPresent());
    }

    private OrientationSolution solve(WorldCoordinate viewer, String title, RememberedPosition p, boolean mapped) {
        double dx=p.coordinate().x()-viewer.x(), dy=p.coordinate().y()-viewer.y();
        double d=Math.hypot(dx,dy); double h=d<1e-6?0:Math.toDegrees(Math.atan2(dx,dy)); if(h<0)h+=360;
        return new OrientationSolution(true,title,d<1e-6?OrientationDirection.HERE:WorldOrientationService.direction(h),h,
                p.uncertaintyRadiusMeters(),p.precision(),KnowledgeReliability.VERIFIED,
                mapped?"El viento guía hacia el objetivo marcado.":"El viento guía hacia una referencia registrada cuya ubicación aún no está cartografiada.");
    }
}
