package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles artesanales explícitos; sin base homogénea ni fórmula automática de afinidad. */
public final class DressmakerCanonicalProfiles {
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE=active();
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private DressmakerCanonicalProfiles(){}

    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null)throw new IllegalArgumentException("Subprofesión fuera de DRESSMAKER: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null)throw new IllegalArgumentException("Perfil  deprecated/no materializado: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s));if(p==null)throw new IllegalArgumentException("Subprofesión fuera de DRESSMAKER: "+s);return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.DRESSMAKER)throw new IllegalArgumentException("Profesión incorrecta: "+s);
        return !ACTIVE.getOrDefault(s,Set.of()).contains(c);
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){return profiles(s);}
    public static String deprecationReason(Subprofession s,CharacterClass c){
        if(!isDeprecated(s,c))return "";
        if(c==CharacterClass.MAESTRO)return "Maestro permanece deprecated en la línea artesanal .";
        return "La combinación no define un perfil artesanal canónico suficientemente distintivo.";
    }
    private static Map<Subprofession,Set<CharacterClass>> active(){
        EnumMap<Subprofession,Set<CharacterClass>> m=new EnumMap<>(Subprofession.class);
        m.put(Subprofession.WORK_TAILOR,Set.of(CharacterClass.INTELECTUAL));
        m.put(Subprofession.PRECISION_PATTERNMAKER,Set.of(CharacterClass.ESPECIALISTA));
        m.put(Subprofession.SALON_DRESSMAKER,Set.of(CharacterClass.HERALDO));
        return Map.copyOf(m);
    }
    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>>all=new EnumMap<>(Subprofession.class);
        add(all,Subprofession.WORK_TAILOR,CharacterClass.INTELECTUAL,sheet(22,21,17,12,31,37,7,20,13),"Perfil canónico explícito  de WORK_TAILOR / INTELECTUAL: la hoja deriva de la función artesanal, la biografía y el sello innato; ninguna fórmula automática de afinidad participa en su construcción.");
        add(all,Subprofession.PRECISION_PATTERNMAKER,CharacterClass.ESPECIALISTA,sheet(20,20,24,9,46,36,6,18,12),"Perfil canónico explícito  de PRECISION_PATTERNMAKER / ESPECIALISTA: la hoja deriva de la función artesanal, la biografía y el sello innato; ninguna fórmula automática de afinidad participa en su construcción.");
        add(all,Subprofession.SALON_DRESSMAKER,CharacterClass.HERALDO,sheet(21,19,18,8,34,31,9,47,13),"Perfil canónico explícito  de SALON_DRESSMAKER / HERALDO: la hoja deriva de la función artesanal, la biografía y el sello innato; ninguna fórmula automática de afinidad participa en su construcción.");
        if(!all.keySet().equals(ACTIVE.keySet()))throw new IllegalStateException("taxonomía DRESSMAKER incompleta.");
        return Map.copyOf(all);
    }
    private static void add(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all,Subprofession s,CharacterClass c,CharacterSheet sh,String rationale){
        var by=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
        var old=all.get(s);if(old!=null)by.putAll(old);
        by.put(c,new CanonicalSubprofessionProfile(s,c,genders(c),sh,rationale));all.put(s,Map.copyOf(by));
    }
    private static Set<Gender> genders(CharacterClass c){return switch(c){
        case LUCHADOR,INTELECTUAL,INDOMITO -> Set.of(Gender.HOMBRE);
        case ESPECIALISTA,APODERADO,HERALDO -> Set.of(Gender.MUJER);
        case MAESTRO -> Set.of(Gender.HOMBRE,Gender.MUJER);
    };}
    private static CharacterSheet sheet(int vit,int agu,int ada,int fue,int des,int intel,int fe,int car,int cla){return CharacterSheet.of(vit,agu,ada,fue,des,intel,fe,car,cla);}
}
