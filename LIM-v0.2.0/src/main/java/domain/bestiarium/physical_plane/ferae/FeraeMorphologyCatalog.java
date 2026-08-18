package domain.bestiarium.physical_plane.ferae;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**  — escala corporal adulta canónica de toda Ferae.
 * Cuadrúpedos: altura funcional a la cruz; aves: altura corporal en estación;
 * serpiente: longitud corporal de referencia, porque su locomoción no dispone de altura a la cruz.
 */
public final class FeraeMorphologyCatalog {
    private FeraeMorphologyCatalog() {}
    public record Height(double maleMeters,double femaleMeters){
        public Height { if(!Double.isFinite(maleMeters)||maleMeters<=0||!Double.isFinite(femaleMeters)||femaleMeters<=0) throw new IllegalArgumentException("Altura Ferae inválida."); }
        public double forSex(FeraeSex sex){ return Objects.requireNonNull(sex)==FeraeSex.MACHO?maleMeters:femaleMeters; }
    }
    private static final Map<FeraeSpecies,Height> DATA=build();
    public static double canonicalHeightMeters(FeraeSpecies species,FeraeSex sex){ return require(species).forSex(sex); }
    public static Height require(FeraeSpecies species){ var h=DATA.get(Objects.requireNonNull(species)); if(h==null)throw new IllegalArgumentException("Ferae sin altura canónica: "+species); return h; }
    public static Map<FeraeSpecies,Height> all(){ return DATA; }
    private static Map<FeraeSpecies,Height> build(){
        EnumMap<FeraeSpecies,Height> m=new EnumMap<>(FeraeSpecies.class);
        put(m,FeraeSpecies.RATON,.09,.085); put(m,FeraeSpecies.PALOMA,.32,.31); put(m,FeraeSpecies.GALLINA,.40,.38);
        put(m,FeraeSpecies.GOLONDRINA,.18,.18); put(m,FeraeSpecies.CONEJO,.30,.29); put(m,FeraeSpecies.GATO,.25,.24);
        put(m,FeraeSpecies.PERRO,.58,.54); put(m,FeraeSpecies.GALLO,.50,.40); put(m,FeraeSpecies.PATO,.45,.43);
        put(m,FeraeSpecies.LIEBRE,.55,.53); put(m,FeraeSpecies.CABRA,.72,.68); put(m,FeraeSpecies.OVEJA,.76,.72);
        put(m,FeraeSpecies.BURRO,1.12,1.08); put(m,FeraeSpecies.VACA,1.45,1.38);
        put(m,FeraeSpecies.YEGUA_PASEO,1.55,1.55); put(m,FeraeSpecies.YEGUA_CARRERAS,1.63,1.63); put(m,FeraeSpecies.YEGUA_TIRO,1.70,1.70);
        put(m,FeraeSpecies.BUHO,.52,.55); put(m,FeraeSpecies.CAMELLO,1.90,1.82); put(m,FeraeSpecies.YAK,1.65,1.55); put(m,FeraeSpecies.ELEFANTE,3.05,2.65);
        put(m,FeraeSpecies.RATA,.23,.21); put(m,FeraeSpecies.CUERVO,.62,.59); put(m,FeraeSpecies.CERDO,.78,.73); put(m,FeraeSpecies.ARMADILLO,.36,.34);
        put(m,FeraeSpecies.CABALLO_PASEO,1.58,1.55); put(m,FeraeSpecies.CABALLO_CARRERAS,1.66,1.63); put(m,FeraeSpecies.CABALLO_TIRO,1.74,1.70);
        put(m,FeraeSpecies.CIERVO,1.20,1.02); put(m,FeraeSpecies.TORO,1.55,1.38); put(m,FeraeSpecies.AGUILA,.88,.96);
        put(m,FeraeSpecies.SERPIENTE,1.50,1.60); put(m,FeraeSpecies.JABALI,.92,.82); put(m,FeraeSpecies.LINCE,.68,.62);
        put(m,FeraeSpecies.LOBO,.82,.76); put(m,FeraeSpecies.LEON,1.20,1.08); put(m,FeraeSpecies.OSO,1.35,1.20); put(m,FeraeSpecies.RINOCERONTE,1.80,1.65);
        if(m.size()!=FeraeSpecies.values().length) throw new IllegalStateException("Cobertura incompleta de alturas Ferae: "+m.size()+"/"+FeraeSpecies.values().length);
        return Map.copyOf(m);
    }
    private static void put(EnumMap<FeraeSpecies,Height> m,FeraeSpecies s,double male,double female){ m.put(s,new Height(male,female)); }
}
