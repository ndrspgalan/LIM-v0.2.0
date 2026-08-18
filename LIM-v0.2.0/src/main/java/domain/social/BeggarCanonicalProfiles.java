package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles explícitos; el nivel es consecuencia de los nueve atributos, no una meta previa. */
public final class BeggarCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE=active();
    private BeggarCanonicalProfiles(){}

    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null) throw new IllegalArgumentException("Subprofesión fuera de BEGGAR: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("Perfil no materializado: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s)); if(p==null) throw new IllegalArgumentException("Subprofesión fuera de BEGGAR: "+s); return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.BEGGAR) throw new IllegalArgumentException("Profesión incorrecta: "+s);
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
        m.put(Subprofession.PRISONER,Set.of(CharacterClass.LUCHADOR,CharacterClass.INDOMITO,CharacterClass.ESPECIALISTA,CharacterClass.APODERADO));
        m.put(Subprofession.UNEMPLOYED,Set.of(CharacterClass.INTELECTUAL,CharacterClass.ESPECIALISTA,CharacterClass.HERALDO));
        m.put(Subprofession.WORK_DISABLED,Set.of(CharacterClass.INDOMITO,CharacterClass.APODERADO));
        m.put(Subprofession.INDIGENT,Set.of(CharacterClass.INDOMITO,CharacterClass.ESPECIALISTA));
        m.put(Subprofession.DISPLACED_RESIDENT,Set.of(CharacterClass.INTELECTUAL,CharacterClass.INDOMITO,CharacterClass.ESPECIALISTA,CharacterClass.HERALDO));
        return Map.copyOf(m);
    }

    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all=new EnumMap<>(Subprofession.class);
        addSub(all,Subprofession.PRISONER,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.PRISONER,CharacterClass.LUCHADOR,sheet(27,25,10,32,21,18,4,12,5),"Perfil canónico explícito  de PRISONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.PRISONER,CharacterClass.INTELECTUAL,sheet(27,25,10,32,21,18,4,12,5),"Perfil canónico explícito  de PRISONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.PRISONER,CharacterClass.INDOMITO,sheet(26,34,12,27,18,20,3,10,5),"Perfil canónico explícito  de PRISONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.PRISONER,CharacterClass.ESPECIALISTA,sheet(22,23,16,12,31,21,4,14,7),"Perfil canónico explícito  de PRISONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.PRISONER,CharacterClass.APODERADO,sheet(21,20,15,11,20,22,29,13,7),"Perfil canónico explícito  de PRISONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.PRISONER,CharacterClass.HERALDO,sheet(27,25,10,32,21,18,4,12,5),"Perfil canónico explícito  de PRISONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.PRISONER,CharacterClass.MAESTRO,sheet(27,25,10,32,21,18,4,12,5),"Perfil canónico explícito  de PRISONER. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.UNEMPLOYED,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.UNEMPLOYED,CharacterClass.LUCHADOR,sheet(20,15,14,12,17,34,4,18,10),"Perfil canónico explícito  de UNEMPLOYED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.UNEMPLOYED,CharacterClass.INTELECTUAL,sheet(20,15,14,12,17,34,4,18,10),"Perfil canónico explícito  de UNEMPLOYED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.UNEMPLOYED,CharacterClass.INDOMITO,sheet(20,15,14,12,17,34,4,18,10),"Perfil canónico explícito  de UNEMPLOYED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.UNEMPLOYED,CharacterClass.ESPECIALISTA,sheet(19,17,18,10,29,23,4,18,8),"Perfil canónico explícito  de UNEMPLOYED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.UNEMPLOYED,CharacterClass.APODERADO,sheet(20,15,14,12,17,34,4,18,10),"Perfil canónico explícito  de UNEMPLOYED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.UNEMPLOYED,CharacterClass.HERALDO,sheet(18,15,15,9,18,21,6,34,8),"Perfil canónico explícito  de UNEMPLOYED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.UNEMPLOYED,CharacterClass.MAESTRO,sheet(20,15,14,12,17,34,4,18,10),"Perfil canónico explícito  de UNEMPLOYED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.WORK_DISABLED,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.WORK_DISABLED,CharacterClass.LUCHADOR,sheet(15,27,14,12,16,22,3,10,6),"Perfil canónico explícito  de WORK_DISABLED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.WORK_DISABLED,CharacterClass.INTELECTUAL,sheet(15,27,14,12,16,22,3,10,6),"Perfil canónico explícito  de WORK_DISABLED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.WORK_DISABLED,CharacterClass.INDOMITO,sheet(15,27,14,12,16,22,3,10,6),"Perfil canónico explícito  de WORK_DISABLED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.WORK_DISABLED,CharacterClass.ESPECIALISTA,sheet(15,27,14,12,16,22,3,10,6),"Perfil canónico explícito  de WORK_DISABLED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.WORK_DISABLED,CharacterClass.APODERADO,sheet(16,15,16,9,16,22,31,14,7),"Perfil canónico explícito  de WORK_DISABLED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.WORK_DISABLED,CharacterClass.HERALDO,sheet(15,27,14,12,16,22,3,10,6),"Perfil canónico explícito  de WORK_DISABLED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.WORK_DISABLED,CharacterClass.MAESTRO,sheet(15,27,14,12,16,22,3,10,6),"Perfil canónico explícito  de WORK_DISABLED. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.INDIGENT,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.INDIGENT,CharacterClass.LUCHADOR,sheet(18,35,15,20,17,14,3,7,4),"Perfil canónico explícito  de INDIGENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.INDIGENT,CharacterClass.INTELECTUAL,sheet(18,35,15,20,17,14,3,7,4),"Perfil canónico explícito  de INDIGENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.INDIGENT,CharacterClass.INDOMITO,sheet(18,35,15,20,17,14,3,7,4),"Perfil canónico explícito  de INDIGENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.INDIGENT,CharacterClass.ESPECIALISTA,sheet(16,24,20,9,31,17,4,11,7),"Perfil canónico explícito  de INDIGENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.INDIGENT,CharacterClass.APODERADO,sheet(18,35,15,20,17,14,3,7,4),"Perfil canónico explícito  de INDIGENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.INDIGENT,CharacterClass.HERALDO,sheet(18,35,15,20,17,14,3,7,4),"Perfil canónico explícito  de INDIGENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.INDIGENT,CharacterClass.MAESTRO,sheet(18,35,15,20,17,14,3,7,4),"Perfil canónico explícito  de INDIGENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
        ));
        addSub(all,Subprofession.DISPLACED_RESIDENT,Map.ofEntries(
                Map.entry(CharacterClass.LUCHADOR, profile(Subprofession.DISPLACED_RESIDENT,CharacterClass.LUCHADOR,sheet(22,23,18,16,20,32,5,14,9),"Perfil canónico explícito  de DISPLACED_RESIDENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.INTELECTUAL, profile(Subprofession.DISPLACED_RESIDENT,CharacterClass.INTELECTUAL,sheet(22,23,18,16,20,32,5,14,9),"Perfil canónico explícito  de DISPLACED_RESIDENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.INDOMITO, profile(Subprofession.DISPLACED_RESIDENT,CharacterClass.INDOMITO,sheet(25,38,18,29,19,18,3,10,5),"Perfil canónico explícito  de DISPLACED_RESIDENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.ESPECIALISTA, profile(Subprofession.DISPLACED_RESIDENT,CharacterClass.ESPECIALISTA,sheet(21,25,24,12,34,22,4,15,8),"Perfil canónico explícito  de DISPLACED_RESIDENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.APODERADO, profile(Subprofession.DISPLACED_RESIDENT,CharacterClass.APODERADO,sheet(22,23,18,16,20,32,5,14,9),"Perfil canónico explícito  de DISPLACED_RESIDENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad.")),
                Map.entry(CharacterClass.HERALDO, profile(Subprofession.DISPLACED_RESIDENT,CharacterClass.HERALDO,sheet(20,22,18,11,20,21,7,33,7),"Perfil canónico explícito  de DISPLACED_RESIDENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación activa.")),
                Map.entry(CharacterClass.MAESTRO, profile(Subprofession.DISPLACED_RESIDENT,CharacterClass.MAESTRO,sheet(22,23,18,16,20,32,5,14,9),"Perfil canónico explícito  de DISPLACED_RESIDENT. La hoja deriva del trabajo y la biografía antes que del nivel total. Combinación deprecated; se conserva sólo como referencia de compatibilidad."))
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
