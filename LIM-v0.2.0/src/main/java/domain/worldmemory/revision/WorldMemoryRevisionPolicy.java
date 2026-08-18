package domain.worldmemory.revision;

import domain.worldmemory.entry.IndicatorType;
import domain.worldmemory.entry.RememberedIndicator;
import domain.worldmemory.evidence.KnowledgeReliability;
import domain.worldmemory.evidence.WorldKnowledgeSource;
import domain.worldmemory.spatial.RememberedPosition;
import domain.worldmemory.spatial.SpatialPrecision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class WorldMemoryRevisionPolicy {
    public MemoryRevisionResult revise(RememberedIndicator current, RememberedIndicator incoming) {
        Objects.requireNonNull(incoming, "El indicador entrante no puede ser nulo.");
        if (current == null) {
            return new MemoryRevisionResult(incoming, true, true, !incoming.description().isBlank(),
                    incoming.precision() != SpatialPrecision.UNKNOWN,
                    incoming.reliability() != KnowledgeReliability.RUMOR, true);
        }
        if (!current.id().equals(incoming.id())) {
            throw new IllegalArgumentException("Solo pueden fusionarse indicadores con el mismo identificador.");
        }

        boolean precisionImproved = incoming.precision().isMorePreciseThan(current.precision());
        boolean reliabilityImproved = incoming.reliability().isMoreReliableThan(current.reliability());
        boolean positionUpdated = precisionImproved
                || (incoming.precision() == current.precision()
                && incoming.position().uncertaintyRadiusMeters() < current.position().uncertaintyRadiusMeters());
        boolean descriptionUpdated = !incoming.description().isBlank()
                && (current.description().isBlank() || reliabilityImproved || precisionImproved);

        RememberedPosition position = positionUpdated ? incoming.position() : current.position();
        KnowledgeReliability reliability = reliabilityImproved ? incoming.reliability() : current.reliability();
        String description = descriptionUpdated ? incoming.description() : current.description();
        IndicatorType type = reliabilityImproved || precisionImproved ? incoming.type() : current.type();
        String title = reliabilityImproved || precisionImproved ? incoming.title() : current.title();

        List<WorldKnowledgeSource> sources = new ArrayList<>(current.sources());
        boolean sourceAdded = false;
        for (WorldKnowledgeSource source : incoming.sources()) {
            if (!sources.contains(source)) {
                sources.add(source);
                sourceAdded = true;
            }
        }

        RememberedIndicator revised = new RememberedIndicator(
                current.id(), type, title, description, position, reliability, sources
        );
        return new MemoryRevisionResult(revised, false, positionUpdated, descriptionUpdated,
                precisionImproved, reliabilityImproved, sourceAdded);
    }
}
