package domain.bestiarium.physical_plane.ferae.pj;
import domain.persona.PersonaProfile; import domain.save.SaveKind; import java.util.*;
public record PersonaSaveArchitecture(PersonaProfile persona,Set<SaveKind> kinds){public PersonaSaveArchitecture{Objects.requireNonNull(persona);kinds=Set.copyOf(kinds);}public static PersonaSaveArchitecture canonical(PersonaProfile persona){return new PersonaSaveArchitecture(persona,EnumSet.allOf(SaveKind.class));}public boolean hasWakeSave(){return !persona.saveSlotsIn(SaveKind.WAKE).isEmpty();}}
