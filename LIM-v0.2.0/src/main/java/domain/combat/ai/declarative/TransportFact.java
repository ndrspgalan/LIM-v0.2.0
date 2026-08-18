package domain.combat.ai.declarative;

import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.inventory.logistics.PersonalTransportType;
import java.util.Optional;
import java.util.OptionalDouble;

/** Hecho material  de un transporte presente. */
public record TransportFact(String transportId, PersonalTransportType type, double distanceMeters,
        boolean operational, boolean available, Optional<String> currentDriverId, Optional<String> ownerActorId,
        Optional<FeraeSpecies> mountSpecies, OptionalDouble mountCurrentStamina, OptionalDouble mountMaximumStamina,
        OptionalDouble mountRegenDelaySeconds, OptionalDouble mountFullRegenSeconds, OptionalDouble fuelLiters) {}
