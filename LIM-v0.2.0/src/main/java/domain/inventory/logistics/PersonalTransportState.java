package domain.inventory.logistics;
import domain.worldmemory.spatial.WorldCoordinate;
import java.util.*;
public final class PersonalTransportState {
    private final Map<PersonalTransportType,PersonalTransportUnitState> units; private final PersonalTransportType selected;
    public PersonalTransportState(Map<PersonalTransportType,PersonalTransportUnitState> units, PersonalTransportType selected){
        EnumMap<PersonalTransportType,PersonalTransportUnitState> copy=new EnumMap<>(PersonalTransportType.class);
        for(PersonalTransportType t:PersonalTransportType.values()) copy.put(t,units.getOrDefault(t,PersonalTransportUnitState.unavailable(t)));
        if(selected!=null&&!copy.get(selected).owned()) throw new IllegalArgumentException("El transporte seleccionado debe estar adquirido.");
        this.units=Map.copyOf(copy);this.selected=selected;
    }
    public static PersonalTransportState none(){return new PersonalTransportState(Map.of(),null);}
    public static PersonalTransportState allAcquired(WorldCoordinate origin){
        EnumMap<PersonalTransportType,PersonalTransportUnitState> m=new EnumMap<>(PersonalTransportType.class);
        int i=0; for(PersonalTransportType t:PersonalTransportType.values()) m.put(t,PersonalTransportUnitState.acquired(t,new WorldCoordinate(origin.x()+i++*2,origin.y(),origin.elevation())));
        return new PersonalTransportState(m,PersonalTransportType.HORSE_LEISURE);
    }
    public java.util.Map<PersonalTransportType,PersonalTransportUnitState> units(){return java.util.Map.copyOf(units);}
    public Optional<PersonalTransportType> selectedType(){return Optional.ofNullable(selected);} public PersonalTransportUnitState unit(PersonalTransportType t){return units.get(t);}
    public List<PersonalTransportType> ownedTypes(){return units.values().stream().filter(PersonalTransportUnitState::owned).map(PersonalTransportUnitState::type).toList();}
    public boolean ownsAny(){return units.values().stream().anyMatch(PersonalTransportUnitState::owned);}
    public PersonalTransportState select(PersonalTransportType t){if(!unit(t).owned())throw new IllegalArgumentException("No se posee ese transporte.");return new PersonalTransportState(units,t);}
    public PersonalTransportState update(PersonalTransportUnitState u){EnumMap<PersonalTransportType,PersonalTransportUnitState> m=new EnumMap<>(PersonalTransportType.class);m.putAll(units);m.put(u.type(),u);return new PersonalTransportState(m,selected);}
    public PersonalTransportState lose(PersonalTransportType t){return update(PersonalTransportUnitState.unavailable(t));}
}
