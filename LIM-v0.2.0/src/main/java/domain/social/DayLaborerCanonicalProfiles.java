package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles explícitos; el nivel es consecuencia de los nueve atributos, no una meta previa. */
public final class DayLaborerCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE=active();
    private DayLaborerCanonicalProfiles(){}

    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null) throw new IllegalArgumentException("Subprofesión fuera de DAY_LABORER: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("Perfil no materializado: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s)); if(p==null) throw new IllegalArgumentException("Subprofesión fuera de DAY_LABORER: "+s); return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.DAY_LABORER) throw new IllegalArgumentException("Profesión incorrecta: "+s);
        return !ACTIVE.getOrDefault(s,Set.of()).contains(c);
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){
        EnumMap<CharacterClass,CanonicalSubprofessionProfile>x=new EnumMap<>(CharacterClass.class);
        profiles(s).forEach((c,p)->{if(!isDeprecated(s,c))x.put(c,p);});
        return Map.copyOf(x);
    }
    public static String deprecationReason(Subprofession s,CharacterClass c){
        if(!isDeprecated(s,c))return "";
        if(c==CharacterClass.MAESTRO)return "Maestro permanece deprecated.";
        return "La combinación clase/subprofesión no posee un rol canónico suficientemente distintivo.";
    }

    private static Map<Subprofession,Set<CharacterClass>> active(){
        EnumMap<Subprofession,Set<CharacterClass>>m=new EnumMap<>(Subprofession.class);
        m.put(Subprofession.RECONSTRUCTION_LABORER,Set.of(CharacterClass.LUCHADOR,CharacterClass.INDOMITO));
        m.put(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,Set.of(CharacterClass.INTELECTUAL,CharacterClass.ESPECIALISTA));
        m.put(Subprofession.STABLE_HAND,Set.of(CharacterClass.INDOMITO,CharacterClass.INTELECTUAL));
        m.put(Subprofession.SANITATION_OPERATOR,Set.of(CharacterClass.INDOMITO,CharacterClass.ESPECIALISTA,CharacterClass.APODERADO));
        m.put(Subprofession.FARMER,Set.of(CharacterClass.LUCHADOR,CharacterClass.INTELECTUAL,CharacterClass.INDOMITO,CharacterClass.ESPECIALISTA,CharacterClass.APODERADO));
        m.put(Subprofession.LIVESTOCK_KEEPER,Set.of(CharacterClass.INDOMITO,CharacterClass.INTELECTUAL,CharacterClass.APODERADO,CharacterClass.HERALDO));
        m.put(Subprofession.HORTICULTURIST,Set.of(CharacterClass.ESPECIALISTA));
        m.put(Subprofession.FOREST_LUMBERJACK,Set.of(CharacterClass.LUCHADOR,CharacterClass.INDOMITO));
        m.put(Subprofession.EXTRACTION_MINER,Set.of(CharacterClass.INDOMITO));
        m.put(Subprofession.STEVEDORE,Set.of(CharacterClass.LUCHADOR));
        m.put(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,Set.of(CharacterClass.ESPECIALISTA));
        m.put(Subprofession.HAULAGE_LABORER,Set.of(CharacterClass.LUCHADOR,CharacterClass.INDOMITO));
        m.put(Subprofession.COMPANION_ANIMAL_BREEDER,Set.of(CharacterClass.INTELECTUAL));
        return Map.copyOf(m);
    }

    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all=new EnumMap<>(Subprofession.class);
        addSub(all,Subprofession.RECONSTRUCTION_LABORER,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.RECONSTRUCTION_LABORER,CharacterClass.LUCHADOR,sheet(31,31,12,41,20,15,4,10,5),"Perfil canónico explícito  de RECONSTRUCTION_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.RECONSTRUCTION_LABORER,CharacterClass.INTELECTUAL,sheet(31,31,12,41,20,15,4,10,5),"Perfil canónico explícito  de RECONSTRUCTION_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.RECONSTRUCTION_LABORER,CharacterClass.INDOMITO,sheet(29,43,17,34,18,17,3,7,4),"Perfil canónico explícito  de RECONSTRUCTION_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.RECONSTRUCTION_LABORER,CharacterClass.ESPECIALISTA,sheet(31,31,12,41,20,15,4,10,5),"Perfil canónico explícito  de RECONSTRUCTION_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.RECONSTRUCTION_LABORER,CharacterClass.APODERADO,sheet(31,31,12,41,20,15,4,10,5),"Perfil canónico explícito  de RECONSTRUCTION_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.RECONSTRUCTION_LABORER,CharacterClass.HERALDO,sheet(31,31,12,41,20,15,4,10,5),"Perfil canónico explícito  de RECONSTRUCTION_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.RECONSTRUCTION_LABORER,CharacterClass.MAESTRO,sheet(31,31,12,41,20,15,4,10,5),"Perfil canónico explícito  de RECONSTRUCTION_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,CharacterClass.LUCHADOR,sheet(23,20,18,17,28,39,5,12,12),"Perfil canónico explícito  de ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,CharacterClass.INTELECTUAL,sheet(23,20,18,17,28,39,5,12,12),"Perfil canónico explícito  de ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,CharacterClass.INDOMITO,sheet(23,20,18,17,28,39,5,12,12),"Perfil canónico explícito  de ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,CharacterClass.ESPECIALISTA,sheet(21,20,24,10,38,31,5,14,11),"Perfil canónico explícito  de ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,CharacterClass.APODERADO,sheet(23,20,18,17,28,39,5,12,12),"Perfil canónico explícito  de ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,CharacterClass.HERALDO,sheet(23,20,18,17,28,39,5,12,12),"Perfil canónico explícito  de ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,CharacterClass.MAESTRO,sheet(23,20,18,17,28,39,5,12,12),"Perfil canónico explícito  de ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.STABLE_HAND,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.STABLE_HAND,CharacterClass.LUCHADOR,sheet(28,35,18,30,25,20,4,20,8),"Perfil canónico explícito  de STABLE_HAND. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.STABLE_HAND,CharacterClass.INTELECTUAL,sheet(24,25,17,19,27,34,5,22,10),"Perfil canónico explícito  de STABLE_HAND. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.STABLE_HAND,CharacterClass.INDOMITO,sheet(28,35,18,30,25,20,4,20,8),"Perfil canónico explícito  de STABLE_HAND. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.STABLE_HAND,CharacterClass.ESPECIALISTA,sheet(28,35,18,30,25,20,4,20,8),"Perfil canónico explícito  de STABLE_HAND. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.STABLE_HAND,CharacterClass.APODERADO,sheet(28,35,18,30,25,20,4,20,8),"Perfil canónico explícito  de STABLE_HAND. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.STABLE_HAND,CharacterClass.HERALDO,sheet(28,35,18,30,25,20,4,20,8),"Perfil canónico explícito  de STABLE_HAND. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.STABLE_HAND,CharacterClass.MAESTRO,sheet(28,35,18,30,25,20,4,20,8),"Perfil canónico explícito  de STABLE_HAND. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.SANITATION_OPERATOR,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.SANITATION_OPERATOR,CharacterClass.LUCHADOR,sheet(29,39,16,31,19,17,3,8,4),"Perfil canónico explícito  de SANITATION_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.SANITATION_OPERATOR,CharacterClass.INTELECTUAL,sheet(29,39,16,31,19,17,3,8,4),"Perfil canónico explícito  de SANITATION_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.SANITATION_OPERATOR,CharacterClass.INDOMITO,sheet(29,39,16,31,19,17,3,8,4),"Perfil canónico explícito  de SANITATION_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.SANITATION_OPERATOR,CharacterClass.ESPECIALISTA,sheet(22,28,25,13,35,23,4,11,7),"Perfil canónico explícito  de SANITATION_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.SANITATION_OPERATOR,CharacterClass.APODERADO,sheet(22,27,23,13,25,25,30,12,7),"Perfil canónico explícito  de SANITATION_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.SANITATION_OPERATOR,CharacterClass.HERALDO,sheet(29,39,16,31,19,17,3,8,4),"Perfil canónico explícito  de SANITATION_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.SANITATION_OPERATOR,CharacterClass.MAESTRO,sheet(29,39,16,31,19,17,3,8,4),"Perfil canónico explícito  de SANITATION_OPERATOR. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.FARMER,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.FARMER,CharacterClass.LUCHADOR,sheet(29,30,17,35,20,22,4,12,7),"Perfil canónico explícito  de FARMER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.FARMER,CharacterClass.INTELECTUAL,sheet(24,24,20,20,25,37,5,16,10),"Perfil canónico explícito  de FARMER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.FARMER,CharacterClass.INDOMITO,sheet(26,39,14,32,18,13,3,7,4),": Agricultor absorbe la afinidad INDOMITO que antes expresaba Peón agrícola.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.FARMER,CharacterClass.ESPECIALISTA,sheet(22,25,23,13,34,29,5,17,9),"Perfil canónico explícito  de FARMER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.FARMER,CharacterClass.APODERADO,sheet(23,27,22,14,25,29,31,17,8),"Perfil canónico explícito  de FARMER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.FARMER,CharacterClass.HERALDO,sheet(29,30,17,35,20,22,4,12,7),"Perfil canónico explícito  de FARMER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.FARMER,CharacterClass.MAESTRO,sheet(29,30,17,35,20,22,4,12,7),"Perfil canónico explícito  de FARMER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.LIVESTOCK_KEEPER,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.LIVESTOCK_KEEPER,CharacterClass.LUCHADOR,sheet(28,37,17,31,22,22,4,18,7),"Perfil canónico explícito  de LIVESTOCK_KEEPER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.LIVESTOCK_KEEPER,CharacterClass.INTELECTUAL,sheet(24,26,18,20,25,35,5,23,10),"Perfil canónico explícito  de LIVESTOCK_KEEPER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.LIVESTOCK_KEEPER,CharacterClass.INDOMITO,sheet(28,37,17,31,22,22,4,18,7),"Perfil canónico explícito  de LIVESTOCK_KEEPER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.LIVESTOCK_KEEPER,CharacterClass.ESPECIALISTA,sheet(28,37,17,31,22,22,4,18,7),"Perfil canónico explícito  de LIVESTOCK_KEEPER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.LIVESTOCK_KEEPER,CharacterClass.APODERADO,sheet(23,27,20,14,24,25,31,21,9),": Ganadero absorbe APODERADO de Cuidadora ganadera.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.LIVESTOCK_KEEPER,CharacterClass.HERALDO,sheet(21,24,18,13,23,23,15,34,8),": Ganadero absorbe HERALDO de Cuidadora ganadera.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.LIVESTOCK_KEEPER,CharacterClass.MAESTRO,sheet(28,37,17,31,22,22,4,18,7),"Perfil canónico explícito  de LIVESTOCK_KEEPER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.HORTICULTURIST,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.HORTICULTURIST,CharacterClass.LUCHADOR,sheet(17,21,22,10,39,30,7,14,9),"Perfil canónico explícito  de HORTICULTURIST. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.HORTICULTURIST,CharacterClass.INTELECTUAL,sheet(17,21,22,10,39,30,7,14,9),"Perfil canónico explícito  de HORTICULTURIST. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.HORTICULTURIST,CharacterClass.INDOMITO,sheet(17,21,22,10,39,30,7,14,9),"Perfil canónico explícito  de HORTICULTURIST. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.HORTICULTURIST,CharacterClass.ESPECIALISTA,sheet(17,21,22,10,39,30,7,14,9),"Perfil canónico explícito  de HORTICULTURIST. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.HORTICULTURIST,CharacterClass.APODERADO,sheet(17,21,22,10,39,30,7,14,9),"Perfil canónico explícito  de HORTICULTURIST. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.HORTICULTURIST,CharacterClass.HERALDO,sheet(17,21,22,10,39,30,7,14,9),"Perfil canónico explícito  de HORTICULTURIST. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.HORTICULTURIST,CharacterClass.MAESTRO,sheet(17,21,22,10,39,30,7,14,9),"Perfil canónico explícito  de HORTICULTURIST. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.FOREST_LUMBERJACK,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.FOREST_LUMBERJACK,CharacterClass.LUCHADOR,sheet(32,34,10,46,21,13,3,8,4),"Perfil canónico explícito  de FOREST_LUMBERJACK. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.FOREST_LUMBERJACK,CharacterClass.INTELECTUAL,sheet(32,34,10,46,21,13,3,8,4),"Perfil canónico explícito  de FOREST_LUMBERJACK. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.FOREST_LUMBERJACK,CharacterClass.INDOMITO,sheet(31,47,14,39,19,14,3,6,4),"Perfil canónico explícito  de FOREST_LUMBERJACK. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.FOREST_LUMBERJACK,CharacterClass.ESPECIALISTA,sheet(32,34,10,46,21,13,3,8,4),"Perfil canónico explícito  de FOREST_LUMBERJACK. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.FOREST_LUMBERJACK,CharacterClass.APODERADO,sheet(32,34,10,46,21,13,3,8,4),"Perfil canónico explícito  de FOREST_LUMBERJACK. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.FOREST_LUMBERJACK,CharacterClass.HERALDO,sheet(32,34,10,46,21,13,3,8,4),"Perfil canónico explícito  de FOREST_LUMBERJACK. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.FOREST_LUMBERJACK,CharacterClass.MAESTRO,sheet(32,34,10,46,21,13,3,8,4),"Perfil canónico explícito  de FOREST_LUMBERJACK. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.EXTRACTION_MINER,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.EXTRACTION_MINER,CharacterClass.LUCHADOR,sheet(35,49,13,38,16,17,4,5,5),"Perfil canónico explícito  de EXTRACTION_MINER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.EXTRACTION_MINER,CharacterClass.INTELECTUAL,sheet(35,49,13,38,16,17,4,5,5),"Perfil canónico explícito  de EXTRACTION_MINER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.EXTRACTION_MINER,CharacterClass.INDOMITO,sheet(35,49,13,38,16,17,4,5,5),"Perfil canónico explícito  de EXTRACTION_MINER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.EXTRACTION_MINER,CharacterClass.ESPECIALISTA,sheet(35,49,13,38,16,17,4,5,5),"Perfil canónico explícito  de EXTRACTION_MINER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.EXTRACTION_MINER,CharacterClass.APODERADO,sheet(35,49,13,38,16,17,4,5,5),"Perfil canónico explícito  de EXTRACTION_MINER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.EXTRACTION_MINER,CharacterClass.HERALDO,sheet(35,49,13,38,16,17,4,5,5),"Perfil canónico explícito  de EXTRACTION_MINER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.EXTRACTION_MINER,CharacterClass.MAESTRO,sheet(35,49,13,38,16,17,4,5,5),"Perfil canónico explícito  de EXTRACTION_MINER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.STEVEDORE,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.STEVEDORE,CharacterClass.LUCHADOR,sheet(33,34,9,48,24,12,3,11,3),"Perfil canónico explícito  de STEVEDORE. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.STEVEDORE,CharacterClass.INTELECTUAL,sheet(33,34,9,48,24,12,3,11,3),"Perfil canónico explícito  de STEVEDORE. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.STEVEDORE,CharacterClass.INDOMITO,sheet(33,34,9,48,24,12,3,11,3),"Perfil canónico explícito  de STEVEDORE. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.STEVEDORE,CharacterClass.ESPECIALISTA,sheet(33,34,9,48,24,12,3,11,3),"Perfil canónico explícito  de STEVEDORE. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.STEVEDORE,CharacterClass.APODERADO,sheet(33,34,9,48,24,12,3,11,3),"Perfil canónico explícito  de STEVEDORE. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.STEVEDORE,CharacterClass.HERALDO,sheet(33,34,9,48,24,12,3,11,3),"Perfil canónico explícito  de STEVEDORE. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.STEVEDORE,CharacterClass.MAESTRO,sheet(33,34,9,48,24,12,3,11,3),"Perfil canónico explícito  de STEVEDORE. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,CharacterClass.LUCHADOR,sheet(18,23,19,12,36,24,6,13,7),"Perfil canónico explícito  de AGRICULTURAL_SELECTOR_CONDITIONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,CharacterClass.INTELECTUAL,sheet(18,23,19,12,36,24,6,13,7),"Perfil canónico explícito  de AGRICULTURAL_SELECTOR_CONDITIONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,CharacterClass.INDOMITO,sheet(18,23,19,12,36,24,6,13,7),"Perfil canónico explícito  de AGRICULTURAL_SELECTOR_CONDITIONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,CharacterClass.ESPECIALISTA,sheet(18,23,19,12,36,24,6,13,7),"Perfil canónico explícito  de AGRICULTURAL_SELECTOR_CONDITIONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,CharacterClass.APODERADO,sheet(18,23,19,12,36,24,6,13,7),"Perfil canónico explícito  de AGRICULTURAL_SELECTOR_CONDITIONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,CharacterClass.HERALDO,sheet(18,23,19,12,36,24,6,13,7),"Perfil canónico explícito  de AGRICULTURAL_SELECTOR_CONDITIONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,CharacterClass.MAESTRO,sheet(18,23,19,12,36,24,6,13,7),"Perfil canónico explícito  de AGRICULTURAL_SELECTOR_CONDITIONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.HAULAGE_LABORER,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.HAULAGE_LABORER,CharacterClass.LUCHADOR,sheet(31,30,9,44,18,11,3,9,3),"Perfil canónico explícito  de HAULAGE_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.HAULAGE_LABORER,CharacterClass.INTELECTUAL,sheet(31,30,9,44,18,11,3,9,3),"Perfil canónico explícito  de HAULAGE_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.HAULAGE_LABORER,CharacterClass.INDOMITO,sheet(29,44,12,36,17,12,3,7,3),"Perfil canónico explícito  de HAULAGE_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.HAULAGE_LABORER,CharacterClass.ESPECIALISTA,sheet(31,30,9,44,18,11,3,9,3),"Perfil canónico explícito  de HAULAGE_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.HAULAGE_LABORER,CharacterClass.APODERADO,sheet(31,30,9,44,18,11,3,9,3),"Perfil canónico explícito  de HAULAGE_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.HAULAGE_LABORER,CharacterClass.HERALDO,sheet(31,30,9,44,18,11,3,9,3),"Perfil canónico explícito  de HAULAGE_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.HAULAGE_LABORER,CharacterClass.MAESTRO,sheet(31,30,9,44,18,11,3,9,3),"Perfil canónico explícito  de HAULAGE_LABORER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.COMPANION_ANIMAL_BREEDER,Map.ofEntries(
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.COMPANION_ANIMAL_BREEDER,CharacterClass.INTELECTUAL,sheet(25,27,29,18,31,48,8,29,23),"Criador de animales de compañía canónico . La Inteligencia representa observación de generaciones, parentesco, conducta, salud y selección; EMPATÍA ANIMAL aporta lectura interespecífica sin sustituir el oficio."))
        ));
        return Map.copyOf(all);
    }
    private static void addSub(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all,Subprofession s,Map<CharacterClass,CanonicalSubprofessionProfile> p){all.put(s,Map.copyOf(p));}
    private static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c,CharacterSheet sheet,String rationale){
        return new CanonicalSubprofessionProfile(s,c,genders(c),sheet,rationale);
    }
    private static Set<Gender> genders(CharacterClass c){return switch(c){
        case LUCHADOR,INTELECTUAL,INDOMITO -> Set.of(Gender.HOMBRE);
        case ESPECIALISTA,APODERADO,HERALDO -> Set.of(Gender.MUJER);
        case MAESTRO -> Set.of(Gender.HOMBRE,Gender.MUJER);
    };}
    private static CharacterSheet sheet(int vit,int agu,int ada,int fue,int des,int intel,int fe,int car,int cla){return CharacterSheet.of(vit,agu,ada,fue,des,intel,fe,car,cla);}
}
