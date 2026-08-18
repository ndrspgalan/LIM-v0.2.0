package domain.worldmemory.filter;

import domain.worldmemory.category.WorldMemoryCategory;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.KnowledgeSourceType;
import domain.worldmemory.evidence.KnowledgeStatus;

import java.util.Objects;
import java.util.Optional;

/** Criterios combinables sobre conocimiento ya adquirido. Un Optional vacío significa «cualquiera». */
public record WorldMemoryFilter(
        Optional<WorldMemoryCategory> category,
        Optional<KnowledgeStatus> status,
        Optional<KnowledgeReliability> reliability,
        Optional<KnowledgeSourceType> sourceType,
        SpatialMemoryRequirement spatialRequirement
) {
    public WorldMemoryFilter {
        category = Objects.requireNonNull(category);
        status = Objects.requireNonNull(status);
        reliability = Objects.requireNonNull(reliability);
        sourceType = Objects.requireNonNull(sourceType);
        spatialRequirement = Objects.requireNonNull(spatialRequirement);
        if (category.orElse(null) == WorldMemoryCategory.EXPLORED_TERRITORY) {
            throw new IllegalArgumentException("El territorio explorado no es todavía una entrada granular filtrable.");
        }
    }

    public static WorldMemoryFilter none() {
        return new WorldMemoryFilter(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                SpatialMemoryRequirement.ANY);
    }

    public boolean isEmpty() {
        return category.isEmpty() && status.isEmpty() && reliability.isEmpty() && sourceType.isEmpty()
                && spatialRequirement == SpatialMemoryRequirement.ANY;
    }
}
