package domain.economy;

import domain.inventory.item.misc.*;
import java.util.*;

/**
 *  — valoración del mucus transpuesto.
 * El mercado remunera disponibilidad biológica, estabilización y dificultad de obtención;
 * nunca deriva el precio de la inmunidad o bonus mecánico.
 */
public final class MucusEconomicCatalog {
    public static final long WHITE_MUCUS_VALERITAS_PER_ML = 4L;
    private static final Map<String,EconomicValuation> CRYSTALS=build();
    private MucusEconomicCatalog(){}

    public static long tearValueValeritas(MucusTearItem tear){
        Objects.requireNonNull(tear);
        return Math.multiplyExact(tear.currentUses(),WHITE_MUCUS_VALERITAS_PER_ML);
    }

    public static QuantityEconomicValuation tearValuation(MucusTearItem tear){
        long content=tearValueValeritas(tear);
        return new QuantityEconomicValuation(tear.name(),EconomicGoodType.PRIVATE_USE,0,content,content,
                "La Lágrima es el propio mucus blanco transpuesto, no un recipiente recuperable. Cada mL equivale a un gramo y a un uso; su valor escala linealmente con el volumen real hasta el límite físico de 100 mL. La tarifa remunera obtención y transposición de materia biológica útil, no el daño maldito que pueda transportar un recubrimiento.");
    }

    public static EconomicValuation crystal(String name){
        EconomicValuation v=CRYSTALS.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Cristal sin tasación : "+name);
        return v;
    }
    public static Map<String,EconomicValuation> crystals(){return CRYSTALS;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        p(m,"Cristal de Mucus AMARILLENTO",75,
                "Tetraedro obtenido a partir de 50 mL de mucus amarillento. Su volumen precursor es relativamente alto, pero aparece en una fase biológica defensiva comparativamente accesible; extracción, conservación y Transposición explican el precio, no su inmunidad al veneno.");
        p(m,"Cristal de Mucus VERDOSO",120,
                "Octaedro formado con 20 mL de precursor verdoso. La menor disponibilidad de secreciones asociadas a adaptación térmica y la necesidad de conservar la muestra hasta Transposición elevan el coste sobre el amarillento sin valorar directamente la inmunidad concedida.");
        p(m,"Cristal de Mucus MARRÓN",240,
                "Cubo que concentra 5 mL de mucus marrón procedente de estados de esfuerzo y recuperación muy concretos. La oferta útil es mucho menor y requiere una cadena de obtención y manipulación más selectiva; ésa es la prima económica, no la supresión de latencia de PA REGEN.");
        p(m,"Cristal de Mucus ENSANGRENTADO",450,
                "Esfera transpuesta desde 2,5 mL de mucus ensangrentado, material ligado a reparación tisular bajo deterioro persistente. Obtener una muestra viable sin contaminarla y conservarla hasta Transposición restringe fuertemente la oferta; la inmunidad maldita no se usa como multiplicador.");
        p(m,"Cristal de Mucus NEGRUZCO",900,
                "Dodecaedro producido desde sólo 1 mL de mucus negruzco, cuya aparición exige una alteración excepcional de continuidad mental. Su altísimo precio por masa responde a la extrema rareza de una muestra estable y autenticable y al acceso a Transposición, no a la inmunidad a Frenesí.");
        return Map.copyOf(m);
    }
    private static void p(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));
    }
}
