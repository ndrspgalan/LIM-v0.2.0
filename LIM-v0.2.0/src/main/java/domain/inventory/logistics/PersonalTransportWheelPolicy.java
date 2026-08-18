package domain.inventory.logistics;
import java.util.*;
public final class PersonalTransportWheelPolicy {
 public List<PersonalTransportType> options(PersonalTransportState state){return Objects.requireNonNull(state).ownedTypes();}
 public PersonalTransportState select(PersonalTransportState state,PersonalTransportType selected){return Objects.requireNonNull(state).select(Objects.requireNonNull(selected));}
}
