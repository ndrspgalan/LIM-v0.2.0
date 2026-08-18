package domain.economy;

import domain.inventory.item.armor.ArmorCatalog;
import java.util.*;

/**
 *  — autoridad económica de BRACERS.
 *
 * Ser ArmorPiece no fuerza PRIVATE_USE. Guantes profesionales permanecen SOCIAL_INTEREST;
 * armaduras dedicadas son PRIVATE_USE. Protección, bloqueo improvisado y estadísticas de gameplay
 * no calculan el precio.
 */
public final class BracersArmorEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private BracersArmorEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("BRACERS sin tasación : "+name);
        return v;
    }

    public static Map<String,EconomicValuation> all(){return DATA;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        social(m,"Guantes de Precisión V881",45,
                "Guantes profesionales ligeros de cuero endurecido y textil concebidos para conservar sensibilidad y control manual. Selección de piel fina, curtido, endurecimiento localizado, patronaje estrecho de dedos, costuras pequeñas y ajuste anatómico explican el precio; su protección incidental no se monetiza.");
        social(m,"Guantes de cuero endurecido con los dedos al aire V881",52,
                "Guantes técnicos de cuero endurecido con refuerzos en palma y dorso y terminación abierta en los dedos para preservar tactilidad. Requieren más cuero estructural, remates alrededor de cada abertura y costuras resistentes al esfuerzo que unos guantes ordinarios; su capacidad defensiva no fija la cifra.");
        social(m,"Guantes de Taller V881",38,
                "Equipo de protección profesional destinado a trabajo manual abrasivo y térmico. Cuero resistente, refuerzos, costuras gruesas y una confección deliberadamente sencilla y reparable mantienen el precio moderado. Se tasan como EPI de taller, no como armamento.");

        priv(m,"Brazales de Papel V881",120,
                "Protección dedicada construida mediante numerosas capas de papel y soporte textil compactados, prensados y tratados para conservar forma alrededor del antebrazo. La fibra es barata, pero laminación, secado, conformado, barnizado, cosido y ajuste bilateral requieren trabajo considerable; sus porcentajes de protección no calculan el precio.");

        m.put("Brazales del Guerrero de Ébano",
                EbonyWarriorArmorEconomicPolicy.valuation("Brazales del Guerrero de Ébano"));
        m.put("Brazal izquierdo del Guerrero de Ébano V881",
                EbonyWarriorArmorEconomicPolicy.valuation("Brazal izquierdo del Guerrero de Ébano V881"));

        priv(m,"Brazales de Caballero",1650,
                "Conjunto histórico de placas de acero para antebrazo y mano, con piezas conformadas, remaches, correas y articulaciones destinadas a acompañar el movimiento sin abrir huecos. Masa metálica, forja, tratamiento térmico, ajuste individual y ensamblaje manual explican el coste; el bloqueo improvisado no forma parte de la fórmula.");
        priv(m,"Brazales de Caballero incluidos codera y nudillos V881",3200,
                "Sistema V881 de acero y wolframio que integra antebrazo, codera y protección de nudillos mediante placas articuladas y solapadas. Materiales densos, piezas pequeñas de geometría compleja, mecanizado, perforado, remachado, tolerancias articulares y ajuste bilateral elevan notablemente la manufactura frente al conjunto histórico. El uso como escudo improvisado es consecuencia física del diseño, no una prima de gameplay.");
        priv(m,"Brazales Lamelares Históricos Pesados",1900,
                "Protección pesada formada por numerosas lamelas de acero perforadas y enlazadas sobre soporte textil alrededor del antebrazo. Aunque cada lámina individual es sencilla, la cantidad de piezas, tratamiento, perforado, cordaje, solape y horas de ensamblaje convierten el conjunto en una manufactura intensiva.");

        Set<String> canonical=new LinkedHashSet<>();
        ArmorCatalog.allBracers().forEach(a->canonical.add(a.name()));
        if(canonical.size()!=9) throw new IllegalStateException(" espera 9 BRACERS canónicos.");
        if(!m.keySet().equals(canonical)) throw new IllegalStateException(" debe cubrir exactamente ArmorCatalog.allBracers().");
        return Map.copyOf(m);
    }

    private static void social(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.SOCIAL_INTEREST,v,r));
    }
    private static void priv(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));
    }
}
