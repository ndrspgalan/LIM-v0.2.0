package domain.bestiarium.physical_plane.npc;
import domain.character.CharacterClass; import java.util.*;
/** siete NPC canónicos aparte de Kenan; profesiones aquí describen el estado ADULT. */
public record CanonicalNpcCatalog(String name, CharacterClass characterClass, String profession){
 public static List<CanonicalNpcCatalog> all(){return List.of(
  new CanonicalNpcCatalog("Jacob",CharacterClass.MAESTRO,"Mecenas ilustrado"),
  new CanonicalNpcCatalog("Iván",CharacterClass.INTELECTUAL,"Arquitecto de matrices"),
  new CanonicalNpcCatalog("Alicia",CharacterClass.ESPECIALISTA,"Escolta móvil"),
  new CanonicalNpcCatalog("Rhoy",CharacterClass.LUCHADOR,"Agente del Reino"),
  new CanonicalNpcCatalog("Sofía",CharacterClass.APODERADO,"Modista de salón + Peluquera de salón"),
  new CanonicalNpcCatalog("Kiara",CharacterClass.HERALDO,"Mensajera del Reino"),
  new CanonicalNpcCatalog("Elena",CharacterClass.APODERADO,"Investigadora forense") );}
}
