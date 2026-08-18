package domain.inventory.logistics;
import domain.worldmemory.spatial.WorldCoordinate;
import java.util.Objects;
public final class PersonalTransportCallPolicy {
    public static final double RENDER_DISTANCE_METERS=1500.0, MIN_RADIUS_METERS=5.0, MAX_RADIUS_METERS=10.0;
    public PersonalTransportCallResult call(PersonalTransportUnitState state,WorldCoordinate player,double materializationDistanceMeters){
        Objects.requireNonNull(state);Objects.requireNonNull(player);
        if(!state.owned()||!state.summonAllowedByLevel()) return new PersonalTransportCallResult(PersonalTransportCallStatus.UNAVAILABLE,state.type(),state.coordinate(),"");
        if(state.operationState()==PersonalTransportOperationState.FOLDED_ON_BACK){
            WorldCoordinate c=near(player,materializationDistanceMeters);return new PersonalTransportCallResult(PersonalTransportCallStatus.DEPLOYED_FROM_BACK,state.type(),c,state.type().responseSignal());
        }
        if(state.physicallyPresent() && distance(state.coordinate(),player)<=RENDER_DISTANCE_METERS)
            return new PersonalTransportCallResult(PersonalTransportCallStatus.RESPONDED_AT_PERSISTENT_POSITION,state.type(),state.coordinate(),state.type().responseSignal());
        WorldCoordinate c=near(player,materializationDistanceMeters);return new PersonalTransportCallResult(PersonalTransportCallStatus.MATERIALIZED_NEAR_PLAYER,state.type(),c,state.type().responseSignal());
    }
    private static WorldCoordinate near(WorldCoordinate p,double d){if(!Double.isFinite(d)||d<MIN_RADIUS_METERS||d>MAX_RADIUS_METERS)throw new IllegalArgumentException("La materialización debe producirse entre 5 y 10 m.");return new WorldCoordinate(p.x()+d,p.y(),p.elevation());}
    public static double distance(WorldCoordinate a,WorldCoordinate b){double x=a.x()-b.x(),y=a.y()-b.y(),z=a.elevation()-b.elevation();return Math.sqrt(x*x+y*y+z*z);}
}
