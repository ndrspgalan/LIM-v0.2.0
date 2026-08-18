package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles explícitos de Feriante; sin affinityGain. */
public final class FairgroundWorkerCanonicalProfiles {
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE=active();

 private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
 private FairgroundWorkerCanonicalProfiles(){}
 public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){var by=DATA.get(Objects.requireNonNull(s));if(by==null)throw new IllegalArgumentException("Fuera de : "+s);var p=by.get(Objects.requireNonNull(c));if(p==null)throw new IllegalArgumentException("Perfil deprecated/no canónico: "+s+" / "+c);return p;}
 public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){var p=DATA.get(Objects.requireNonNull(s));if(p==null)throw new IllegalArgumentException("Fuera de : "+s);return p;}
 public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
 public static boolean isDeprecated(Subprofession s,CharacterClass c){Objects.requireNonNull(s);Objects.requireNonNull(c);if(s.profession()!=Profession.FAIRGROUND_WORKER)throw new IllegalArgumentException("Profesión incorrecta.");return !ACTIVE.getOrDefault(s,Set.of()).contains(c);}
 public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){
  var all=profiles(s); var out=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  all.forEach((c,p)->{if(!isDeprecated(s,c))out.put(c,p);}); return Map.copyOf(out);
 }
 private static Map<Subprofession,Set<CharacterClass>> active(){var m=new EnumMap<Subprofession,Set<CharacterClass>>(Subprofession.class);
  m.put(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,Set.of(CharacterClass.INTELECTUAL));
  m.put(Subprofession.FAIRGROUND_ENTREPRENEUR,Set.of(CharacterClass.APODERADO));
  m.put(Subprofession.TAVERN_MUSICIAN,Set.of(CharacterClass.ESPECIALISTA));
  m.put(Subprofession.GAME_MASTER,Set.of(CharacterClass.INTELECTUAL));
  m.put(Subprofession.COMPETITION_RIDER,Set.of(CharacterClass.INDOMITO));
  m.put(Subprofession.V881_MOTORCYCLE_RACER,Set.of(CharacterClass.ESPECIALISTA));
  m.put(Subprofession.COMPETITION_CYCLIST,Set.of(CharacterClass.ESPECIALISTA));
  m.put(Subprofession.TRIATHLETE,Set.of(CharacterClass.INDOMITO));
  return Map.copyOf(m);
 }
 private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){var all=new EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>>(Subprofession.class);
  var itinerant_puppeteer_storyteller=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  itinerant_puppeteer_storyteller.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(25,26,15,18,30,42,13,34,25),"El titiritero y narrador ambulante transporta historias entre plazas y posadas: memoriza repertorios, manipula escena ligera, improvisa y lee públicos distintos mientras vive en movimiento. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  itinerant_puppeteer_storyteller.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(24,25,18,17,44,30,12,35,21),"El titiritero y narrador ambulante transporta historias entre plazas y posadas: memoriza repertorios, manipula escena ligera, improvisa y lee públicos distintos mientras vive en movimiento. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  itinerant_puppeteer_storyteller.put(CharacterClass.HERALDO,new CanonicalSubprofessionProfile(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,CharacterClass.HERALDO,Set.of(Gender.MUJER),CharacterSheet.of(24,24,16,17,31,31,14,46,22),"El titiritero y narrador ambulante transporta historias entre plazas y posadas: memoriza repertorios, manipula escena ligera, improvisa y lee públicos distintos mientras vive en movimiento. En esta referencia, el sello Heraldo se expresa al convertir lectura de otras personas, reputación y comunicación en parte material del resultado profesional."));
  all.put(Subprofession.ITINERANT_PUPPETEER_STORYTELLER,Map.copyOf(itinerant_puppeteer_storyteller));
  var fairground_entrepreneur=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  fairground_entrepreneur.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.FAIRGROUND_ENTREPRENEUR,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(26,25,18,20,25,44,14,36,27),"El empresario de feria convierte ocio disperso en una operación: coordina terreno, permisos, transporte, artistas, puestos, seguridad, calendario, liquidez y riesgo estacional. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  fairground_entrepreneur.put(CharacterClass.APODERADO,new CanonicalSubprofessionProfile(Subprofession.FAIRGROUND_ENTREPRENEUR,CharacterClass.APODERADO,Set.of(Gender.MUJER),CharacterSheet.of(26,26,21,19,25,35,42,34,24),"El empresario de feria convierte ocio disperso en una operación: coordina terreno, permisos, transporte, artistas, puestos, seguridad, calendario, liquidez y riesgo estacional. En esta referencia, el sello Apoderado se expresa como continuidad de propósito, disciplina y capacidad de sostener responsabilidades durante ciclos largos."));
  fairground_entrepreneur.put(CharacterClass.HERALDO,new CanonicalSubprofessionProfile(Subprofession.FAIRGROUND_ENTREPRENEUR,CharacterClass.HERALDO,Set.of(Gender.MUJER),CharacterSheet.of(25,24,19,18,25,35,15,48,26),"El empresario de feria convierte ocio disperso en una operación: coordina terreno, permisos, transporte, artistas, puestos, seguridad, calendario, liquidez y riesgo estacional. En esta referencia, el sello Heraldo se expresa al convertir lectura de otras personas, reputación y comunicación en parte material del resultado profesional."));
  all.put(Subprofession.FAIRGROUND_ENTREPRENEUR,Map.copyOf(fairground_entrepreneur));
  var tavern_musician=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  tavern_musician.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.TAVERN_MUSICIAN,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(24,24,15,18,34,39,12,35,23),"El músico de taberna convierte memoria musical, coordinación corporal y lectura del ambiente en un servicio cotidiano que debe funcionar para conversación, baile, celebración o duelo. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  tavern_musician.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.TAVERN_MUSICIAN,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(24,25,19,17,47,29,11,35,20),"El músico de taberna convierte memoria musical, coordinación corporal y lectura del ambiente en un servicio cotidiano que debe funcionar para conversación, baile, celebración o duelo. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  tavern_musician.put(CharacterClass.HERALDO,new CanonicalSubprofessionProfile(Subprofession.TAVERN_MUSICIAN,CharacterClass.HERALDO,Set.of(Gender.MUJER),CharacterSheet.of(23,23,16,17,36,30,13,47,21),"El músico de taberna convierte memoria musical, coordinación corporal y lectura del ambiente en un servicio cotidiano que debe funcionar para conversación, baile, celebración o duelo. En esta referencia, el sello Heraldo se expresa al convertir lectura de otras personas, reputación y comunicación en parte material del resultado profesional."));
  all.put(Subprofession.TAVERN_MUSICIAN,Map.copyOf(tavern_musician));
  var game_master=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  game_master.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.GAME_MASTER,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(24,23,17,18,27,45,13,32,29),"El Maestro de juegos mantiene reglas, materiales, apuestas y legitimidad social en sistemas pequeños donde entretenimiento, fraude, deuda y conflicto pueden tocarse. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  game_master.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.GAME_MASTER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(23,23,20,17,43,32,12,31,25),"El Maestro de juegos mantiene reglas, materiales, apuestas y legitimidad social en sistemas pequeños donde entretenimiento, fraude, deuda y conflicto pueden tocarse. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  game_master.put(CharacterClass.HERALDO,new CanonicalSubprofessionProfile(Subprofession.GAME_MASTER,CharacterClass.HERALDO,Set.of(Gender.MUJER),CharacterSheet.of(23,22,18,17,28,34,14,46,27),"El Maestro de juegos mantiene reglas, materiales, apuestas y legitimidad social en sistemas pequeños donde entretenimiento, fraude, deuda y conflicto pueden tocarse. En esta referencia, el sello Heraldo se expresa al convertir lectura de otras personas, reputación y comunicación en parte material del resultado profesional."));
  all.put(Subprofession.GAME_MASTER,Map.copyOf(game_master));
  var competition_rider=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  competition_rider.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.COMPETITION_RIDER,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(34,52,28,31,42,26,8,21,17),"Compite sobre una montura de carreras. El desarrollo canónico prioriza resistencia sostenida, adaptación al movimiento del caballo, coordinación y lectura de ritmo; no convierte al jinete en combatiente por proximidad cultural a la caballería."));
  all.put(Subprofession.COMPETITION_RIDER,Map.copyOf(competition_rider));
  var motorcycle_racer=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  motorcycle_racer.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.V881_MOTORCYCLE_RACER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(28,30,32,22,58,37,9,23,24),"La piloto de motociclismo desarrolla precisión bajo velocidad, coordinación fina de una máquina pesada y capacidad para repetir trayectorias sin confundir riesgo con improvisación."));
  all.put(Subprofession.V881_MOTORCYCLE_RACER,Map.copyOf(motorcycle_racer));
  var competition_cyclist=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  competition_cyclist.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.COMPETITION_CYCLIST,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(30,30,35,20,55,31,8,20,19),"La ciclista convierte cadencia, equilibrio y eficiencia mecánica en una profesión competitiva. Su hoja desarrolla coordinación y adaptación sin exceder el límite físico femenino ordinario de AGUANTE."));
  all.put(Subprofession.COMPETITION_CYCLIST,Map.copyOf(competition_cyclist));
  var triathlete=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  triathlete.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.TRIATHLETE,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(38,58,38,30,39,25,7,18,16),"El triatleta desarrolla continuidad entre carrera, bicicleta, natación y desnivel. El sello Indómito representa la capacidad de sostener rendimiento a través de transiciones y fatiga, no una bonificación automática."));
  all.put(Subprofession.TRIATHLETE,Map.copyOf(triathlete));
  if(all.size()!=8)throw new IllegalStateException("Taxonomía  incompleta para FAIRGROUND_WORKER.");return Map.copyOf(all);}
}