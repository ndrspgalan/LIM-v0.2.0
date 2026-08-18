package domain.bestiarium.physical_plane.ferae.intelligence;

import domain.bestiarium.physical_plane.ferae.*;
import domain.character.sheet.CharacterSheet;
import java.util.*;

/**  — perfiles Ferae INTELIGENCIA reauditorados desde biología y conducta; el nivel sólo emerge del sumatorio. */
public final class IntelligenceFeraeProfiles {
    private IntelligenceFeraeProfiles(){}

    private static final Map<FeraeSpecies, List<FeraeProfile>> PROFILES = build();

    public static List<FeraeProfile> all(){ return PROFILES.values().stream().flatMap(Collection::stream).toList(); }

    public static List<FeraeProfile> of(FeraeSpecies species){
        if(species.branch()!=FeraeBranch.INTELIGENCIA)
            throw new IllegalArgumentException("No pertenece a INTELIGENCIA: "+species.label());
        return PROFILES.get(species);
    }

    public static FeraeProfile of(FeraeSpecies species, FeraeSex sex){
        return of(species).stream().filter(p->p.sex()==sex).findFirst().orElseThrow();
    }

    private static Map<FeraeSpecies,List<FeraeProfile>> build(){
        EnumMap<FeraeSpecies,List<FeraeProfile>> m=new EnumMap<>(FeraeSpecies.class);
        // VIT, AGU, ADA, FUE, DES, INT, FE, CAR, CLA. El nivel se deriva siempre del sumatorio.
        put(m,FeraeSpecies.RATA,        a(6,10,9,2,11,7,1,3,1),       a(6,10,9,1,11,7,1,3,1));
        put(m,FeraeSpecies.CUERVO,      a(8,14,11,3,15,15,1,6,4),    a(8,14,11,3,15,15,1,6,4));
        put(m,FeraeSpecies.CERDO,       a(26,26,18,22,9,12,1,8,2),    a(24,24,18,18,9,12,1,8,2));
        put(m,FeraeSpecies.ARMADILLO,   a(18,18,25,11,7,5,1,3,1),    a(17,17,25,9,7,5,1,3,1));
        put(m,FeraeSpecies.CABALLO_PASEO,    a(37,42,25,34,19,10,1,10,1),a(35,40,25,29,19,10,1,10,1));
        put(m,FeraeSpecies.CABALLO_CARRERAS, a(34,49,23,30,28,10,1,9,1), a(32,47,23,25,28,10,1,9,1));
        put(m,FeraeSpecies.CABALLO_TIRO,      a(47,44,29,50,13,9,1,9,1), a(44,42,29,42,13,9,1,9,1));
        put(m,FeraeSpecies.CIERVO,      a(28,32,22,22,21,9,1,8,2),   a(24,29,22,16,21,9,1,8,2));
        put(m,FeraeSpecies.TORO,        a(45,38,24,50,11,8,1,8,1),   a(34,31,24,31,11,8,1,8,1));
        put(m,FeraeSpecies.AGUILA,      a(14,22,15,9,25,10,1,4,4),    a(16,24,15,11,25,10,1,4,4));
        put(m,FeraeSpecies.SERPIENTE,   a(11,14,20,8,18,5,1,2,3),    a(12,15,20,8,18,5,1,2,3));
        put(m,FeraeSpecies.JABALI,      a(35,34,24,38,14,8,1,5,1),   a(31,31,24,30,14,8,1,5,1));
        put(m,FeraeSpecies.LINCE,       a(20,25,20,16,27,11,1,5,3),  a(19,24,20,14,27,11,1,5,3));
        put(m,FeraeSpecies.LOBO,        a(25,32,22,20,24,12,1,10,2),  a(23,30,22,17,24,12,1,10,2));
        put(m,FeraeSpecies.LEON,        a(38,35,23,42,22,10,1,8,2),   a(32,32,23,31,24,10,1,8,2));
        put(m,FeraeSpecies.OSO,         a(52,45,32,55,13,10,1,6,2),   a(46,41,32,45,13,10,1,6,2));
        put(m,FeraeSpecies.RINOCERONTE, a(62,48,38,68,9,6,1,4,1),    a(55,45,38,56,9,6,1,4,1));
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
        return s.label()+" perteneciente a la rama INTELIGENCIA de Ferae: animal físico cuyo perfil conserva su relación natural y su eventual progresión social como contratos separados de sus capacidades corporales. Sus nueve atributos describen desarrollo biológico total; el nivel canónico es únicamente la suma de dichos atributos y no una medida directa de peligrosidad.";
    }

    private static String sexNarrative(FeraeSpecies s,FeraeSex sex){
        return "Variante "+sex.label()+" de "+s.label()+". El dimorfismo sexual sólo modifica atributos cuando tiene relevancia biológica o mecánica. "+
                (sex==FeraeSex.MACHO
                        ? "Porta el trofeo Ferae canónico de su especie y éste puede formar parte de su pillaje cuando el inventario externo sea accesible."
                        : "No porta trofeo Ferae; su variante sexual no añade ningún recurso de pillaje específico.");
    }
}
