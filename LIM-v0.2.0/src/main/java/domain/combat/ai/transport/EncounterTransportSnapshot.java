package domain.combat.ai.transport;
import domain.inventory.logistics.PersonalTransportType;
import java.util.Objects;
import java.util.Optional;
/** Transporte físicamente presente que puede entrar en la economía táctica del encuentro. */
public record EncounterTransportSnapshot(String transportId,PersonalTransportType type,double distanceMeters,
        boolean operational,boolean operableByActor,Optional<String> currentDriverId,Optional<String> ownerActorId){
 public EncounterTransportSnapshot{Objects.requireNonNull(transportId);Objects.requireNonNull(type);currentDriverId=Objects.requireNonNull(currentDriverId);ownerActorId=Objects.requireNonNull(ownerActorId);if(distanceMeters<0||!Double.isFinite(distanceMeters))throw new IllegalArgumentException("Distancia inválida.");}
 public boolean available(){return operational&&operableByActor&&currentDriverId.isEmpty();}
}
