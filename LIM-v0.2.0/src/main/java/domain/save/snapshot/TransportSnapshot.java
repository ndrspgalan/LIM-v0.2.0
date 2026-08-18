package domain.save.snapshot;
import java.io.Serializable; import java.util.*;
public record TransportSnapshot(String selectedTransportType,List<TransportUnitSnapshot> units,double motorcycleFuelLiters) implements Serializable {
 public TransportSnapshot{selectedTransportType=selectedTransportType==null?"":selectedTransportType;units=List.copyOf(units);}
 public TransportSnapshot(String transportId,double x,double y,double z,double fuelLiters,boolean rendered){this(transportId,List.of(),fuelLiters);}
}
