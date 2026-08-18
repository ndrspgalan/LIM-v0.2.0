package domain.save.snapshot;
import java.io.Serializable;
public record TransportUnitSnapshot(String type,boolean owned,String operationState,Double x,Double y,Double z,boolean summonAllowedByLevel,String assignedNpcId) implements Serializable {}
