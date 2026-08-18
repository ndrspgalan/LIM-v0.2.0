package domain.inventory.logistics;
import domain.worldmemory.spatial.WorldCoordinate;
import java.util.*;
/** Referencias persistentes e independientes de la marca temporal del Monocular. */
public final class PersonalTransportMemoryRegistry {
 private final EnumMap<PersonalTransportType,WorldCoordinate> marks=new EnumMap<>(PersonalTransportType.class);
 public void update(PersonalTransportUnitState unit){if(unit.physicallyPresent())marks.put(unit.type(),unit.coordinate());else marks.remove(unit.type());}
 public Optional<WorldCoordinate> mark(PersonalTransportType type){return Optional.ofNullable(marks.get(type));}
 public Map<PersonalTransportType,WorldCoordinate> marks(){return Map.copyOf(marks);}
}
