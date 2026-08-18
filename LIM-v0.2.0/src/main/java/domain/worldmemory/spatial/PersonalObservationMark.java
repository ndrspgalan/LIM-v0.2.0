package domain.worldmemory.spatial;

import java.util.Objects;

/** Marca espacial temporal colocada por el protagonista. Solo puede existir una simultáneamente. */
public record PersonalObservationMark(WorldCoordinate coordinate) {
    public PersonalObservationMark { Objects.requireNonNull(coordinate, "La coordenada de la marca no puede ser nula."); }
    public RememberedPosition asRememberedPosition() { return RememberedPosition.verified(coordinate); }
}
