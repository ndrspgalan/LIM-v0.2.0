package domain.worldmemory.spatial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class RememberedTerrain {
    private final List<TerrainObservation> observations = new ArrayList<>();

    public void record(TerrainObservation observation) {
        observations.add(Objects.requireNonNull(observation, "La observación no puede ser nula."));
    }

    public List<TerrainObservation> observations() {
        return Collections.unmodifiableList(observations);
    }

    public int observationCount() {
        return observations.size();
    }
}
