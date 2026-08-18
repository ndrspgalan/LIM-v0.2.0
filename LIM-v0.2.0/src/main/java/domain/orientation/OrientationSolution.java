package domain.orientation;

import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.spatial.SpatialPrecision;

public record OrientationSolution(
        boolean available,
        String targetTitle,
        OrientationDirection direction,
        double headingDegrees,
        double uncertaintyRadiusMeters,
        SpatialPrecision precision,
        KnowledgeReliability reliability,
        String message
) {
    public static OrientationSolution unavailable(String message) {
        return new OrientationSolution(false, "", OrientationDirection.HERE, 0.0, 0.0, null, null, message);
    }
}
