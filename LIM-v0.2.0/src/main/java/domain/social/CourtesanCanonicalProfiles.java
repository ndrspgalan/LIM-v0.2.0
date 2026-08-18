package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles explícitos y sexualmente coherentes de Cortesana. */
public final class CourtesanCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private CourtesanCanonicalProfiles(){}
    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s));
        if(by==null) throw new IllegalArgumentException("Fuera de Cortesana: "+s);
        var p=by.get(Objects.requireNonNull(c));
        if(p==null) throw new IllegalArgumentException("Perfil deprecated/no canónico: "+s+" / "+c);
        return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){
        var p=DATA.get(Objects.requireNonNull(s)); if(p==null)throw new IllegalArgumentException("Fuera de Cortesana: "+s); return p;
    }
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);
        if(s.profession()!=Profession.COURTESAN)throw new IllegalArgumentException("Profesión incorrecta.");
        return !DATA.get(s).containsKey(c);
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){return profiles(s);}
    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all=new EnumMap<>(Subprofession.class);
        put(all,Subprofession.SEX_WORKER,CharacterClass.ESPECIALISTA,Gender.MUJER,
            CharacterSheet.of(34,34,39,23,57,34,23,46,32),
            "La Trabajadora sexual convierte encuentros eróticos en trabajo. Esta vida desarrolla lectura corporal, presentación, límites, comunicación no verbal y adaptación profesional sin convertir la intimidad en una propiedad de la persona. El perfil canónico representa una mujer cuya especialización se manifiesta sobre todo en precisión interpersonal y control de la ejecución.");
        put(all,Subprofession.SALON_COURTESAN,CharacterClass.HERALDO,Gender.MUJER,
            CharacterSheet.of(33,33,43,19,39,56,29,75,51),
            "La Cortesana de salón vende ausencia de fricción social. Recuerda nombres, silencios, preferencias y límites mientras atraviesa espacios de comerciantes, mercenarios, juristas, maestros y patrimonios acomodados. Su vida profesional está definida por comunicación, lectura social y discreción, no por autoridad formal.");
        put(all,Subprofession.PROFESSIONAL_COMPANION,CharacterClass.APODERADO,Gender.MUJER,
            CharacterSheet.of(32,32,37,18,35,48,45,52,40),
            "La Acompañante profesional vende presencia delimitada sin exigir sexo, amistad ni continuidad futura. Conversar, pasear, asistir a comidas o sostener una aparición pública desarrolla escucha, memoria interpersonal, regulación de distancia y disciplina para acompañar sin apropiarse de aquello que la compañía significa.");
        return Map.copyOf(all);
    }
    private static void put(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all,Subprofession s,CharacterClass c,Gender g,CharacterSheet sheet,String narrative){
        var m=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
        m.put(c,new CanonicalSubprofessionProfile(s,c,Set.of(g),sheet,narrative+" La clase no añade una bonificación automática: la hoja representa a esta persona concreta."));
        all.put(s,Map.copyOf(m));
    }
}
