package domain.combat.ai.declarative;

import domain.combat.ai.transport.EncounterTransportSnapshot;
import domain.inventory.InventoryEntry;
import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.inventory.logistics.MotorcycleFuelState;
import java.util.*;

/** Estado factual de transportes conocido por LIM en el instante de decisión. */
public record TransportDecisionState(List<EncounterTransportSnapshot> transports, Optional<String> mountedTransportId,
        Optional<InventoryEntry> activeItem, Map<String,FeraeSpecies> mountSpeciesByTransportId, Map<String,Double> mountCurrentStamina,
        Map<String,Double> mountLoadKg, Optional<MotorcycleFuelState> motorcycleFuel) {
 public TransportDecisionState { transports=List.copyOf(Objects.requireNonNull(transports)); mountedTransportId=Objects.requireNonNull(mountedTransportId); activeItem=Objects.requireNonNull(activeItem); mountSpeciesByTransportId=Map.copyOf(Objects.requireNonNull(mountSpeciesByTransportId)); mountCurrentStamina=Map.copyOf(Objects.requireNonNull(mountCurrentStamina)); mountLoadKg=Map.copyOf(Objects.requireNonNull(mountLoadKg)); motorcycleFuel=Objects.requireNonNull(motorcycleFuel); }
 public static TransportDecisionState empty(){return new TransportDecisionState(List.of(),Optional.empty(),Optional.empty(),Map.of(),Map.of(),Map.of(),Optional.empty());}
}
