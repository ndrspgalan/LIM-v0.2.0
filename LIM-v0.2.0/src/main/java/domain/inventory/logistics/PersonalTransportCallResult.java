package domain.inventory.logistics;
import domain.worldmemory.spatial.WorldCoordinate;
public record PersonalTransportCallResult(PersonalTransportCallStatus status, PersonalTransportType type, WorldCoordinate coordinate, String responseSignal) {
    public boolean materialized(){return status==PersonalTransportCallStatus.MATERIALIZED_NEAR_PLAYER||status==PersonalTransportCallStatus.DEPLOYED_FROM_BACK;}
}
