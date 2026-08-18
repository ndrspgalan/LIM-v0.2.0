package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles explícitos de Marinero; sin affinityGain. */
public final class SailorCanonicalProfiles {
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE=active();

 private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
 private SailorCanonicalProfiles(){}
 public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){var by=DATA.get(Objects.requireNonNull(s));if(by==null)throw new IllegalArgumentException("Fuera de : "+s);var p=by.get(Objects.requireNonNull(c));if(p==null)throw new IllegalArgumentException("Perfil deprecated/no canónico: "+s+" / "+c);return p;}
 public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){var p=DATA.get(Objects.requireNonNull(s));if(p==null)throw new IllegalArgumentException("Fuera de : "+s);return p;}
 public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
 public static boolean isDeprecated(Subprofession s,CharacterClass c){Objects.requireNonNull(s);Objects.requireNonNull(c);if(s.profession()!=Profession.SAILOR)throw new IllegalArgumentException("Profesión incorrecta.");return !ACTIVE.getOrDefault(s,Set.of()).contains(c);}
 public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){
  var all=profiles(s); var out=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  all.forEach((c,p)->{if(!isDeprecated(s,c))out.put(c,p);}); return Map.copyOf(out);
 }
 private static Map<Subprofession,Set<CharacterClass>> active(){var m=new EnumMap<Subprofession,Set<CharacterClass>>(Subprofession.class);
  m.put(Subprofession.COASTAL_FISHER,Set.of(CharacterClass.LUCHADOR));
  m.put(Subprofession.OFFSHORE_FISHER,Set.of(CharacterClass.INDOMITO));
  m.put(Subprofession.V881_NAVIGATOR,Set.of(CharacterClass.INTELECTUAL));
  m.put(Subprofession.NAVAL_RAILGUN_GUNNER,Set.of(CharacterClass.INTELECTUAL));
  m.put(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,Set.of(CharacterClass.INTELECTUAL));
  m.put(Subprofession.MERCHANT_SAILOR,Set.of(CharacterClass.APODERADO));
  return Map.copyOf(m);
 }
 private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){var all=new EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>>(Subprofession.class);
  var coastal_fisher=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  coastal_fisher.put(CharacterClass.LUCHADOR,new CanonicalSubprofessionProfile(Subprofession.COASTAL_FISHER,CharacterClass.LUCHADOR,Set.of(Gender.HOMBRE),CharacterSheet.of(36,38,22,42,33,27,10,23,18),"El pescador costero trabaja cerca de un puerto pero sobre una plataforma inestable: captura, aparejos, averías, mareas y conservación inmediata mezclan esfuerzo físico y conocimiento local. En esta referencia, el sello Luchador coincide con una biografía que ha hecho de la potencia corporal una herramienta cotidiana, sin convertir el oficio en combate."));
  coastal_fisher.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.COASTAL_FISHER,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(40,48,28,33,32,28,11,22,21),"El pescador costero trabaja cerca de un puerto pero sobre una plataforma inestable: captura, aparejos, averías, mareas y conservación inmediata mezclan esfuerzo físico y conocimiento local. En esta referencia, el sello Indómito acompaña una vida de continuidad física y tolerancia a condiciones que obligan a seguir funcionando cuando otros ya deben parar."));
  all.put(Subprofession.COASTAL_FISHER,Map.copyOf(coastal_fisher));
  var offshore_fisher=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  offshore_fisher.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.OFFSHORE_FISHER,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(45,53,32,36,35,31,11,23,25),"El pescador de altura trabaja cuando regresar deja de ser la respuesta inmediata: autonomía, guardias, conservación, mantenimiento y mar abierto convierten cada jornada en una cadena de dependencias. En esta referencia, el sello Indómito acompaña una vida de continuidad física y tolerancia a condiciones que obligan a seguir funcionando cuando otros ya deben parar."));
  offshore_fisher.put(CharacterClass.APODERADO,new CanonicalSubprofessionProfile(Subprofession.OFFSHORE_FISHER,CharacterClass.APODERADO,Set.of(Gender.MUJER),CharacterSheet.of(38,43,31,28,34,36,44,27,27),"El pescador de altura trabaja cuando regresar deja de ser la respuesta inmediata: autonomía, guardias, conservación, mantenimiento y mar abierto convierten cada jornada en una cadena de dependencias. En esta referencia, el sello Apoderado se expresa como continuidad de propósito, disciplina y capacidad de sostener responsabilidades durante ciclos largos."));
  all.put(Subprofession.OFFSHORE_FISHER,Map.copyOf(offshore_fisher));
  var v881_navigator=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  v881_navigator.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.V881_NAVIGATOR,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(31,35,25,21,35,49,12,28,38),"El navegante V881 integra posición, mar, viento, corriente, meteorología y respuesta del buque para convertir información incompleta en rumbo seguro. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  v881_navigator.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.V881_NAVIGATOR,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(30,34,29,20,47,41,11,27,35),"El navegante V881 integra posición, mar, viento, corriente, meteorología y respuesta del buque para convertir información incompleta en rumbo seguro. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  all.put(Subprofession.V881_NAVIGATOR,Map.copyOf(v881_navigator));
  var naval_railgun_gunner=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  naval_railgun_gunner.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.NAVAL_RAILGUN_GUNNER,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(32,36,23,27,35,48,11,22,34),"El artillero de riel naval opera un sistema instalado en el buque cuya escala exige cálculo, procedimiento, seguridad de cubierta y disciplina sobre cuándo no disparar. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  naval_railgun_gunner.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.NAVAL_RAILGUN_GUNNER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(31,35,27,25,49,39,10,21,31),"El artillero de riel naval opera un sistema instalado en el buque cuya escala exige cálculo, procedimiento, seguridad de cubierta y disciplina sobre cuándo no disparar. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  all.put(Subprofession.NAVAL_RAILGUN_GUNNER,Map.copyOf(naval_railgun_gunner));
  var naval_electroatmospheric_engineer=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  naval_electroatmospheric_engineer.put(CharacterClass.INTELECTUAL,new CanonicalSubprofessionProfile(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,CharacterClass.INTELECTUAL,Set.of(Gender.HOMBRE),CharacterSheet.of(31,35,25,23,35,52,12,22,37),"El maquinista electroatmosférico naval mantiene captación, regulación, aislamiento, propulsión y distribución bajo cubierta cuando una cadena invisible de fallos puede inmovilizar todo el buque. En esta referencia, el sello Intelectual encuentra expresión en comprender causas, relaciones y procedimientos del oficio, no en recibir una bonificación matemática."));
  naval_electroatmospheric_engineer.put(CharacterClass.ESPECIALISTA,new CanonicalSubprofessionProfile(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,CharacterClass.ESPECIALISTA,Set.of(Gender.MUJER),CharacterSheet.of(30,34,29,22,48,43,11,21,33),"El maquinista electroatmosférico naval mantiene captación, regulación, aislamiento, propulsión y distribución bajo cubierta cuando una cadena invisible de fallos puede inmovilizar todo el buque. En esta referencia, el sello Especialista se reconoce en precisión, coordinación y ejecución repetible; la hoja completa sigue perteneciendo a una persona, no a una afinidad automática."));
  all.put(Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,Map.copyOf(naval_electroatmospheric_engineer));
  var merchant_sailor=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
  merchant_sailor.put(CharacterClass.LUCHADOR,new CanonicalSubprofessionProfile(Subprofession.MERCHANT_SAILOR,CharacterClass.LUCHADOR,Set.of(Gender.HOMBRE),CharacterSheet.of(39,41,25,43,32,28,10,29,19),"El marinero mercante sostiene la continuidad material del comercio marítimo mediante carga, estiba, maniobra, mantenimiento, guardias y convivencia prolongada. En esta referencia, el sello Luchador coincide con una biografía que ha hecho de la potencia corporal una herramienta cotidiana, sin convertir el oficio en combate."));
  merchant_sailor.put(CharacterClass.INDOMITO,new CanonicalSubprofessionProfile(Subprofession.MERCHANT_SAILOR,CharacterClass.INDOMITO,Set.of(Gender.HOMBRE),CharacterSheet.of(43,51,31,35,32,29,11,27,22),"El marinero mercante sostiene la continuidad material del comercio marítimo mediante carga, estiba, maniobra, mantenimiento, guardias y convivencia prolongada. En esta referencia, el sello Indómito acompaña una vida de continuidad física y tolerancia a condiciones que obligan a seguir funcionando cuando otros ya deben parar."));
  merchant_sailor.put(CharacterClass.APODERADO,new CanonicalSubprofessionProfile(Subprofession.MERCHANT_SAILOR,CharacterClass.APODERADO,Set.of(Gender.MUJER),CharacterSheet.of(36,42,30,27,31,34,43,34,24),"El marinero mercante sostiene la continuidad material del comercio marítimo mediante carga, estiba, maniobra, mantenimiento, guardias y convivencia prolongada. En esta referencia, el sello Apoderado se expresa como continuidad de propósito, disciplina y capacidad de sostener responsabilidades durante ciclos largos."));
  all.put(Subprofession.MERCHANT_SAILOR,Map.copyOf(merchant_sailor));
  if(all.size()!=6)throw new IllegalStateException("Taxonomía  incompleta para SAILOR.");return Map.copyOf(all);}
}
