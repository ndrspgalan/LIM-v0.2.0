package domain.inventory.logistics;

import domain.worldmemory.spatial.WorldCoordinate;
import java.util.Objects;

public record PersonalTransportUnitState(
        PersonalTransportType type, boolean owned, PersonalTransportOperationState operationState,
        WorldCoordinate coordinate, boolean summonAllowedByLevel, String assignedNpcId
) {
    public PersonalTransportUnitState {
        Objects.requireNonNull(type); Objects.requireNonNull(operationState);
        if(!owned && operationState!=PersonalTransportOperationState.ABSENT) throw new IllegalArgumentException("Un transporte no adquirido debe permanecer ausente.");
        if(operationState!=PersonalTransportOperationState.ABSENT && operationState!=PersonalTransportOperationState.FOLDED_ON_BACK && coordinate==null)
            throw new IllegalArgumentException("Un transporte materializado necesita coordenada persistente.");
        if(operationState==PersonalTransportOperationState.FOLDED_ON_BACK && type!=PersonalTransportType.BICYCLE_FOLDING_V881)
            throw new IllegalArgumentException("Solo la Bicicleta Plegable puede ocupar la espalda.");
    }
    public static PersonalTransportUnitState unavailable(PersonalTransportType type){return new PersonalTransportUnitState(type,false,PersonalTransportOperationState.ABSENT,null,false,null);}
    public static PersonalTransportUnitState acquired(PersonalTransportType type, WorldCoordinate coordinate){return new PersonalTransportUnitState(type,true,PersonalTransportOperationState.PARKED,coordinate,true,null);}
    public boolean physicallyPresent(){return operationState!=PersonalTransportOperationState.ABSENT && operationState!=PersonalTransportOperationState.FOLDED_ON_BACK;}
    public PersonalTransportUnitState withState(PersonalTransportOperationState state, WorldCoordinate coordinate){return new PersonalTransportUnitState(type,owned,state,coordinate,summonAllowedByLevel,assignedNpcId);}
    public PersonalTransportUnitState withCoordinate(WorldCoordinate coordinate){return new PersonalTransportUnitState(type,owned,operationState,coordinate,summonAllowedByLevel,assignedNpcId);}
    public PersonalTransportUnitState assignedTo(String npcId){return new PersonalTransportUnitState(type,owned,PersonalTransportOperationState.ASSIGNED_TO_NPC,coordinate,summonAllowedByLevel,npcId);}
}
