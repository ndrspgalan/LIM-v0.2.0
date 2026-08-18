package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles explícitos de Cazador; sin affinityGain. */
public final class HunterCanonicalProfiles {
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE=active();

 private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
 private HunterCanonicalProfiles(){}
 public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){var by=DATA.get(Objects.requireNonNull(s));if(by==null)throw new IllegalArgumentException("Fuera de : "+s);var p=by.get(Objects.requireNonNull(c));if(p==null)throw new IllegalArgumentException("Perfil deprecated/no canónico: "+s+" / "+c);return p;}
 public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){var p=DATA.get(Objects.requireNonNull(s));if(p==null)throw new IllegalArgumentException("Fuera de : "+s);return p;}
 public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
 public static boolean isDeprecated(Subprofession s,CharacterClass c){Objects.requireNonNull(s);Objects.requireNonNull(c);if(s.profession()!=Profession.HUNTER)throw new IllegalArgumentException("Profesión incorrecta.");return !ACTIVE.getOrDefault(s,Set.of()).contains(c);}
 public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){
  var all=profiles(s); var out=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  all.forEach((c,p)->{if(!isDeprecated(s,c))out.put(c,p);}); return Map.copyOf(out);
 }
 private static Map<Subprofession,Set<CharacterClass>> active(){var m=new EnumMap<Subprofession,Set<CharacterClass>>(Subprofession.class);
  m.put(Subprofession.ROAD_GUIDE,Set.of(CharacterClass.INTELECTUAL));
  m.put(Subprofession.WILDLIFE_TRACKER,Set.of(CharacterClass.ESPECIALISTA));
  m.put(Subprofession.PROFESSIONAL_HUNTER,Set.of(CharacterClass.LUCHADOR,CharacterClass.ESPECIALISTA));
  m.put(Subprofession.TRAPPER,Set.of(CharacterClass.INDOMITO));
  return Map.copyOf(m);
 }
 private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){var all=new EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>>(Subprofession.class);
  var road_guide=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  road_guide.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.ROAD_GUIDE,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(32,36,22,26,34,42,12,31,28),"El guía de caminos vive de lo que un mapa impreso no puede garantizar: firme, puentes, agua, talleres, bandidaje, clima y compatibilidad de una ruta con cada viajero y vehículo. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  road_guide.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.ROAD_GUIDE,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(37,48,27,31,32,31,12,26,23),"El guía de caminos vive de lo que un mapa impreso no puede garantizar: firme, puentes, agua, talleres, bandidaje, clima y compatibilidad de una ruta con cada viajero y vehículo. En esta referencia, el sello Indómito acompaña una vida de continuidad física y tolerancia a condiciones que obligan a seguir funcionando cuando otros ya deben parar."));
  road_guide.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.ROAD_GUIDE,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(31,36,27,24,45,33,11,29,26),"El guía de caminos vive de lo que un mapa impreso no puede garantizar: firme, puentes, agua, talleres, bandidaje, clima y compatibilidad de una ruta con cada viajero y vehículo. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  all.put(Subprofession.ROAD_GUIDE,Map.copyOf(road_guide));
  var wildlife_tracker=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  wildlife_tracker.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.WILDLIFE_TRACKER,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(36,46,29,28,35,34,11,22,29),"El rastreador de fauna reconstruye presencia animal a partir de huellas, pelo, barro, restos, dormideros y conducta; localizar y explicar el patrón es su producto, aunque no haya captura. En esta referencia, el sello Indómito acompaña una vida de continuidad física y tolerancia a condiciones que obligan a seguir funcionando cuando otros ya deben parar."));
  wildlife_tracker.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.WILDLIFE_TRACKER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(31,37,30,22,47,36,11,23,32),"El rastreador de fauna reconstruye presencia animal a partir de huellas, pelo, barro, restos, dormideros y conducta; localizar y explicar el patrón es su producto, aunque no haya captura. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  all.put(Subprofession.WILDLIFE_TRACKER,Map.copyOf(wildlife_tracker));
  var professional_hunter=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  professional_hunter.put(CharacterClass.LUCHADOR,new CanonicalSubprofessionProfile(Subprofession.PROFESSIONAL_HUNTER,CharacterClass.LUCHADOR,Set.of(Gender.HOMBRE),CharacterSheet.of(39,40,25,46,37,31,10,22,23),"El cazador profesional persigue, captura y extrae recursos sin confundir violencia con oficio: su renta depende de regresar con resultado y de no destruir la población que sostiene futuros encargos. En esta referencia, el sello Luchador coincide con una biografía que ha hecho de la potencia corporal una herramienta cotidiana, sin convertir el oficio en combate."));
  professional_hunter.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.PROFESSIONAL_HUNTER,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(42,50,31,35,36,31,11,21,25),"El cazador profesional persigue, captura y extrae recursos sin confundir violencia con oficio: su renta depende de regresar con resultado y de no destruir la población que sostiene futuros encargos. En esta referencia, el sello Indómito acompaña una vida de continuidad física y tolerancia a condiciones que obligan a seguir funcionando cuando otros ya deben parar."));
  professional_hunter.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.PROFESSIONAL_HUNTER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(34,39,31,27,49,35,10,22,28),"El cazador profesional persigue, captura y extrae recursos sin confundir violencia con oficio: su renta depende de regresar con resultado y de no destruir la población que sostiene futuros encargos. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  all.put(Subprofession.PROFESSIONAL_HUNTER,Map.copyOf(professional_hunter));
  var trapper=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  trapper.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.TRAPPER,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(30,34,25,23,37,46,11,23,32),"El trampero prepara sistemas de captura, lee recorridos y revisa líneas extensas; su paciencia es trabajo acumulado antes de que el animal llegue. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  trapper.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.TRAPPER,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(37,46,31,29,36,35,11,21,28),"El trampero prepara sistemas de captura, lee recorridos y revisa líneas extensas; su paciencia es trabajo acumulado antes de que el animal llegue. En esta referencia, el sello Indómito acompaña una vida de continuidad física y tolerancia a condiciones que obligan a seguir funcionando cuando otros ya deben parar."));
  trapper.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.TRAPPER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(31,35,30,22,48,38,10,22,30),"El trampero prepara sistemas de captura, lee recorridos y revisa líneas extensas; su paciencia es trabajo acumulado antes de que el animal llegue. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  all.put(Subprofession.TRAPPER,Map.copyOf(trapper));
  if(all.size()!=4)throw new IllegalStateException("Taxonomía  incompleta para HUNTER.");return Map.copyOf(all);}
}
