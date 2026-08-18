package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles artesanales explícitos; sin base homogénea ni fórmula automática de afinidad. */
public final class StonemasonCanonicalProfiles {
    private static final Map<Subprofession,Set<CharacterClass>> ACTIVE=active();
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private StonemasonCanonicalProfiles(){}

    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null)throw new IllegalArgumentException("Subprofesión fuera de STONEMASON: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null)throw new IllegalArgumentException("Perfil  deprecated/no materializado: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s));if(p==null)throw new IllegalArgumentException("Subprofesión fuera de STONEMASON: "+s);return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.STONEMASON)throw new IllegalArgumentException("Profesión incorrecta: "+s);
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
        m.put(Subprofession.STONE_SETTER,Set.of(CharacterClass.LUCHADOR));
        m.put(Subprofession.STONEWORK_MASTER,Set.of(CharacterClass.INTELECTUAL));
        m.put(Subprofession.PRECISION_STONECUTTER,Set.of(CharacterClass.ESPECIALISTA));
        return Map.copyOf(m);
    }
    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>>all=new EnumMap<>(Subprofession.class);
        add(all,Subprofession.STONE_SETTER,CharacterClass.LUCHADOR,sheet(35,36,11,49,24,16,3,8,4),"Perfil canónico explícito  de STONE_SETTER / LUCHADOR: la hoja deriva de la función artesanal, la biografía y el sello innato; ninguna fórmula automática de afinidad participa en su construcción.");
        add(all,Subprofession.STONEWORK_MASTER,CharacterClass.INTELECTUAL,sheet(30,31,17,27,30,43,7,20,16),"Perfil canónico explícito  de STONEWORK_MASTER / INTELECTUAL: la hoja deriva de la función artesanal, la biografía y el sello innato; ninguna fórmula automática de afinidad participa en su construcción.");
        add(all,Subprofession.PRECISION_STONECUTTER,CharacterClass.ESPECIALISTA,sheet(25,28,20,14,45,34,5,14,11),"Perfil canónico explícito  de PRECISION_STONECUTTER / ESPECIALISTA: la hoja deriva de la función artesanal, la biografía y el sello innato; ninguna fórmula automática de afinidad participa en su construcción.");
        if(!all.keySet().equals(ACTIVE.keySet()))throw new IllegalStateException("taxonomía STONEMASON incompleta.");
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
