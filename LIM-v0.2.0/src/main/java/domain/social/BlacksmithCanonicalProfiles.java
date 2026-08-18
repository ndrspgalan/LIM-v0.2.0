package domain.social;

import domain.character.CharacterClass;
import domain.character.Gender;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/** perfiles explícitos y sexualmente coherentes de Herrero. */
public final class BlacksmithCanonicalProfiles {
    private static final Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> DATA=build();
    private BlacksmithCanonicalProfiles(){}
    public static CanonicalSubprofessionProfile profile(Subprofession s,CharacterClass c){
        var by=DATA.get(Objects.requireNonNull(s)); if(by==null)throw new IllegalArgumentException("Fuera de Herrero: "+s);
        var p=by.get(Objects.requireNonNull(c)); if(p==null)throw new IllegalArgumentException("Perfil deprecated/no canónico: "+s+" / "+c); return p;
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> profiles(Subprofession s){var p=DATA.get(Objects.requireNonNull(s));if(p==null)throw new IllegalArgumentException("Fuera de Herrero: "+s);return p;}
    public static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all(){return DATA;}
    public static boolean isDeprecated(Subprofession s,CharacterClass c){
        Objects.requireNonNull(s);Objects.requireNonNull(c);if(s.profession()!=Profession.BLACKSMITH)throw new IllegalArgumentException("Profesión incorrecta.");return !DATA.get(s).containsKey(c);
    }
    public static Map<CharacterClass,CanonicalSubprofessionProfile> activeProfiles(Subprofession s){return profiles(s);}
    public static String deprecationReason(Subprofession s,CharacterClass c){return isDeprecated(s,c)?"La combinación no representa un perfil canónico suficientemente distintivo.":"";}
    private static Map<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> build(){
        EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all=new EnumMap<>(Subprofession.class);
        put(all,Subprofession.V881_ELECTROMECHANIC,CharacterClass.INTELECTUAL,Gender.HOMBRE,
            CharacterSheet.of(32,34,22,31,37,59,12,23,28),
            "El Electromecánico V881 trabaja donde metalurgia, electricidad y movimiento dejan de poder separarse. Desmonta, mide, diagnostica y repara sistemas que siguen siendo comprensibles y reparables.");
        put(all,Subprofession.DOMESTIC_V881_INSTALLER,CharacterClass.LUCHADOR,Gender.HOMBRE,
            CharacterSheet.of(36,32,21,43,36,40,12,30,23),
            "El Instalador doméstico V881 convierte infraestructura electroatmosférica en una vivienda que funciona. Su jornada exige montaje físico, aislamiento, transporte de material y trabajo prolongado en espacios habitados.");
        put(all,Subprofession.FREQUENCY_INSTRUMENT_MAKER,CharacterClass.ESPECIALISTA,Gender.MUJER,
            CharacterSheet.of(31,33,27,28,60,49,14,24,30),
            "El Instrumentista frecuencial fabrica aquello que permite afirmar que una observación es reproducible. Su trabajo exige tolerancias extremas, destreza manual y control de pequeñas desviaciones.");
        put(all,Subprofession.MATRIX_ARCHITECT,CharacterClass.INTELECTUAL,Gender.HOMBRE,
            CharacterSheet.of(33,35,26,30,45,70,16,27,38),
            "El Arquitecto de matrices fabrica soportes destinados a mantener configuraciones que la materia ordinaria no conservaría con suficiente fidelidad. Trabaja en el límite entre manufactura y experimento, descartando piezas por pureza, estabilidad o tolerancia antes de aceptar una matriz.");
        return Map.copyOf(all);
    }
    private static void put(EnumMap<Subprofession,Map<CharacterClass,CanonicalSubprofessionProfile>> all,Subprofession s,CharacterClass c,Gender g,CharacterSheet sheet,String narrative){
        var m=new EnumMap<CharacterClass,CanonicalSubprofessionProfile>(CharacterClass.class);
        m.put(c,new CanonicalSubprofessionProfile(s,c,Set.of(g),sheet,narrative+" La clase expresa la biografía concreta y no una fórmula de afinidad."));
        all.put(s,Map.copyOf(m));
    }
}
