package domain.bestiarium.physical_plane.ferae.pj;
import domain.character.*; import domain.persona.*; import java.util.*;
public final class CanonicalPlayerPersonas {
 private CanonicalPlayerPersonas(){}
 public static PersonaProfile kenan(){return new PersonaProfile("kenan-indomito","Kenan",Gender.HOMBRE,CharacterClass.INDOMITO,domain.character.KenanCanonicalProfile.INITIAL_LEVEL,List.of(),List.of());}
 public static List<PersonaProfile> all(){return List.of(kenan());}
}
