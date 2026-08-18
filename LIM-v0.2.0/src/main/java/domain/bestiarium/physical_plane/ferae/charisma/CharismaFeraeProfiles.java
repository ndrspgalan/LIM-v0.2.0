package domain.bestiarium.physical_plane.ferae.charisma;

import domain.bestiarium.physical_plane.ferae.*;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — perfiles canónicos macho/hembra de la rama CARISMA. */
public final class CharismaFeraeProfiles {
    private CharismaFeraeProfiles(){}

    private static final Map<FeraeSpecies, List<FeraeProfile>> PROFILES = build();

    public static List<FeraeProfile> all(){ return PROFILES.values().stream().flatMap(Collection::stream).toList(); }
    public static List<FeraeProfile> of(FeraeSpecies species){
        if(species.branch()!=FeraeBranch.CARISMA) throw new IllegalArgumentException("No pertenece a CARISMA: "+species.label());
        return PROFILES.get(species);
    }
    public static FeraeProfile of(FeraeSpecies species,FeraeSex sex){
        return of(species).stream().filter(p->p.sex()==sex).findFirst().orElseThrow();
    }

    private static Map<FeraeSpecies,List<FeraeProfile>> build(){
        EnumMap<FeraeSpecies,List<FeraeProfile>> m=new EnumMap<>(FeraeSpecies.class);
        // VIT, AGU, ADA, FUE, DES, INT, FE, CAR, CLA. El nivel se deriva: nunca se almacena por separado.
        put(m,FeraeSpecies.RATON,       a(4,7,7,1,8,4,1,2,1), a(4,7,7,1,8,4,1,2,1));
        put(m,FeraeSpecies.PALOMA,      a(5,9,7,2,10,5,1,4,1), a(5,9,7,2,10,5,1,4,1));
        put(m,FeraeSpecies.GALLINA,     a(8,8,7,3,6,4,1,6,1), a(8,8,7,3,6,4,1,6,1));
        put(m,FeraeSpecies.GOLONDRINA,  a(4,14,9,1,15,5,1,3,1),a(4,14,9,1,15,5,1,3,1));
        put(m,FeraeSpecies.CONEJO,      a(8,12,10,3,13,5,1,4,1),a(8,12,10,3,13,5,1,4,1));
        put(m,FeraeSpecies.GATO,        a(10,13,11,5,16,9,1,9,2),a(9,12,11,4,16,9,1,9,2));
        put(m,FeraeSpecies.PERRO,       a(15,18,13,10,12,12,1,15,2),a(14,17,13,8,12,12,1,15,2));
        put(m,FeraeSpecies.GALLO,       a(9,10,8,5,7,4,1,7,1), a(8,9,8,3,7,4,1,7,1));
        put(m,FeraeSpecies.PATO,        a(8,12,8,3,8,5,1,6,1), a(8,12,8,3,8,5,1,6,1));
        put(m,FeraeSpecies.LIEBRE,      a(9,15,11,4,16,5,1,4,1),a(9,15,11,4,16,5,1,4,1));
        put(m,FeraeSpecies.CABRA,       a(18,21,15,13,13,8,1,9,1),a(17,20,15,11,13,8,1,9,1));
        put(m,FeraeSpecies.OVEJA,       a(18,18,14,11,9,7,1,10,1), a(17,17,14,9,9,7,1,10,1));
        put(m,FeraeSpecies.BURRO,       a(28,31,20,24,11,9,1,10,1),a(26,29,20,20,11,9,1,10,1));
        put(m,FeraeSpecies.VACA,        a(32,28,20,28,8,8,1,11,1),a(30,26,20,23,8,8,1,11,1));
        put(m,FeraeSpecies.YEGUA_PASEO,    a(35,40,25,27,19,10,1,13,1),a(35,40,25,27,19,10,1,13,1));
        put(m,FeraeSpecies.YEGUA_CARRERAS, a(32,47,23,24,27,10,1,12,1),a(32,47,23,24,27,10,1,12,1));
        put(m,FeraeSpecies.YEGUA_TIRO,      a(44,42,29,40,13,9,1,12,1),a(44,42,29,40,13,9,1,12,1));
        put(m,FeraeSpecies.BUHO,        a(9,13,11,4,15,10,1,4,4), a(10,14,11,5,15,10,1,4,4));
        put(m,FeraeSpecies.CAMELLO,     a(36,43,27,28,11,9,1,9,1),a(33,40,27,24,11,9,1,9,1));
        put(m,FeraeSpecies.YAK,         a(44,42,28,42,9,8,1,9,1), a(39,38,28,34,9,8,1,9,1));
        put(m,FeraeSpecies.ELEFANTE,    a(58,48,36,60,11,17,1,17,3),a(52,45,36,50,11,17,1,17,3));
        return Collections.unmodifiableMap(m);
    }

    private static CharacterSheet a(int v,int ag,int ad,int f,int d,int i,int fe,int c,int cl){
        return CharacterSheet.of(v,ag,ad,f,d,i,fe,c,cl);
    }
    private static void put(Map<FeraeSpecies,List<FeraeProfile>> m,FeraeSpecies s,CharacterSheet male,CharacterSheet female){
        String generic=genericNarrative(s);
        m.put(s,List.of(
                new FeraeProfile(s,FeraeSex.MACHO,male,generic,sexNarrative(s,FeraeSex.MACHO)),
                new FeraeProfile(s,FeraeSex.HEMBRA,female,generic,sexNarrative(s,FeraeSex.HEMBRA))));
    }
    private static String genericNarrative(FeraeSpecies s){
        return s.label()+" perteneciente a la rama CARISMA de Ferae: animal físico de conducta predominantemente huidiza, pacífica o cooperativa. Su anatomía, locomoción y capacidades se representan mediante los nueve atributos canónicos; su nivel no es una categoría de peligrosidad, sino la suma exacta de esos atributos.";
    }
    private static String sexNarrative(FeraeSpecies s,FeraeSex sex){
        return "Variante "+sex.label()+" de "+s.label()+". Conserva la arquitectura biológica de la especie y sólo altera los atributos cuando el dimorfismo sexual tiene relevancia mecánica. "+(sex==FeraeSex.MACHO?"Es la variante habilitada para portar un trofeo Ferae cuando la especie lo tenga definido.":"No porta trofeo Ferae.");
    }
}
