package domain.combat.ai.declarative;

import domain.inventory.logistics.PersonalTransportType;
import java.util.Objects;
import java.util.OptionalDouble;

/** Alternativa de transporte factual; no contiene utilidad ni preferencia. */
public record TransportActionCandidate(TransportActionType action, String transportId, PersonalTransportType type,
        TransportResourceOwner resourceOwner, OptionalDouble staminaCostPerSecond, OptionalDouble fuelCostLitersPerKm,
        double speedKmh, boolean activeItemCompatible, String relation) {
 public TransportActionCandidate { Objects.requireNonNull(action); Objects.requireNonNull(transportId); Objects.requireNonNull(type); Objects.requireNonNull(resourceOwner); Objects.requireNonNull(staminaCostPerSecond); Objects.requireNonNull(fuelCostLitersPerKm); if(relation==null||relation.isBlank())throw new IllegalArgumentException("Relación obligatoria."); }
}
