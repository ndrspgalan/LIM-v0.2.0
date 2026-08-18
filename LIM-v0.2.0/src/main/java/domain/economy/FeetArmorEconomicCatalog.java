package domain.economy;

import domain.inventory.item.armor.ArmorCatalog;
import java.util.*;

/**
 *  — autoridad económica exhaustiva de FEET.
 *
 * Todo el calzado canónico actual es vestimenta o equipo profesional de SOCIAL_INTEREST. La tasación
 * deriva de fibras, cuero, caucho, acero, curtido, tejido, patronaje, suelas, refuerzos y horas de
 * confección; protección, clase de armadura, grounding y aislamiento eléctrico no calculan el precio.
 */
public final class FeetArmorEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private FeetArmorEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("FEET sin tasación : "+name);
        return v;
    }

    public static Map<String,EconomicValuation> all(){return DATA;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        social(m,"Calcetines V881",7,"Par de calcetines de punto fino confeccionado con poca masa de fibra pero con tejido continuo, puntera, talón y remate superior. El precio corresponde al hilo, al proceso de tejido y a los acabados necesarios para que ambas piezas soporten roce y lavado repetido; su cobertura y amortiguación no se monetizan.");
        social(m,"Calcetines gruesos de trabajo V881",12,"Par de calcetines laborales de punto denso con mayor cantidad de hilo y refuerzos localizados en talón y antepié. La tasación recoge más materia textil, tejido de mayor densidad y confección reforzada para uso prolongado dentro de botas; la mejora de protección es una consecuencia física y no una prima de gameplay.");
        social(m,"Medias V881",10,"Par de medias de trama fina cuya longitud supera la de un calcetín ordinario y exige una tensión textil uniforme desde el pie hasta la caña. El coste procede de fibra, tejido, conformado, remates y ligaduras que mantienen cada pieza en posición; no deriva de estadísticas defensivas ni de cobertura anatómica.");
        social(m,"Medias altas V881",15,"Par de medias de caña prolongada que consume más hilo y superficie tejida que la versión ordinaria y requiere mantener elasticidad homogénea a lo largo de pie, tobillo y pantorrilla. Materia adicional, tejido y remates largos justifican la diferencia de precio sin valorar ventajas de uso ecuestre o de protección.");
        social(m,"Medias de punto grueso V881",20,"Par de medias de punto voluminoso con elevada cantidad de hilo y estructura destinada a conservar espesor y cámara de aire bajo calzado robusto. El precio refleja consumo de fibra, tiempo de tejido, remates y control de una malla gruesa y uniforme; aislamiento y amortiguación no actúan como multiplicadores monetarios.");
        social(m,"Vendas de pie V881",6,"Juego de tiras textiles largas para envolver ambos pies, de geometría simple y sin patronaje anatómico fijo. Su bajo precio deriva de paño ordinario cortado en bandas, remates para evitar deshilachado y poca mano de obra de ensamblaje; la capacidad de redistribuirlas o secarlas por separado no se tasa como ventaja de juego.");
        social(m,"Escarpines textiles V881",18,"Par de escarpines blandos formado por varias piezas textiles cosidas alrededor de planta, empeine y talón. Frente a calcetines o vendas requiere patronaje tridimensional, más costuras y dobleces de paño para conservar forma sin una carcasa rígida, lo que explica su mayor coste material y de confección.");

        social(m,"Alpargatas V881",22,"Par de alpargatas construido con pala textil y una base trenzada y comprimida que requiere preparar fibras, formar la suela, coserla al empeine y rematar ambos pies de forma simétrica. Los materiales son corrientes y la estructura carece de carcasa rígida o herrajes complejos, por lo que mantiene una tasación civil moderada.");
        social(m,"Zapatillas de lona V881",28,"Par de zapatos ligeros de lona con pala patronada, base flexible, ojales o pasadores y cordones. La cifra recoge tejido resistente, corte bilateral, costuras de unión, conformado de suela y pequeños elementos de cierre; ligereza, movilidad y protección resultante no intervienen directamente en el cálculo económico.");
        social(m,"Zapatos de trabajo de cuero V881",75,"Par de zapatos laborales de cuero endurecido con pala y talón reforzados y suela de caucho vulcanizado. Curtido, endurecimiento, corte de paneles, conformado sobre horma, cosido de piezas gruesas y fabricación y unión de la suela dieléctrica concentran el coste; el aislamiento eléctrico es consecuencia material, no una prima estadística.");
        social(m,"Botines de cuero V881",95,"Par de botines de cuero endurecido cuya caña rodea el tobillo y exige más superficie de piel, paneles de unión y ajuste que un zapato bajo. Curtido, endurecimiento, hormado, costuras resistentes, cierres y suela de caucho vulcanizado explican la tasación, sin convertir estabilidad o protección en variables económicas abstractas.");
        social(m,"Botas cortas de campo V881",115,"Par de botas de campo fabricado con cuero endurecido grueso, caña corta, pala y talón preparados para barro, maleza y marcha prolongada, más una suela de caucho resistente. El precio refleja piel adicional, curtido, hormas, refuerzos, numerosas costuras y montaje robusto destinado a reparación y uso intensivo.");
        social(m,"Botas altas de montar y campo V881",155,"Par de botas altas de cuero endurecido con caña ecuestre que asciende por la pantorrilla y suela de caucho vulcanizado. Requieren mucha más superficie de piel, selección de paneles largos, hormado de pie y caña, refuerzos, costuras extensas y ajuste bilateral; la invasión anatómica de LEGGINGS no añade valor por sí misma.");
        social(m,"Botas de trabajo pesado e industria V881",185,"Par de botas profesionales con cuero endurecido, suela de caucho vulcanizado y puntera de acero integrada. A curtido, hormado, paneles gruesos y costuras de carga se suman conformado de la puntera metálica, inserción segura, refuerzos y una suela industrial aislante; que el conjunto emerja como HEAVY no determina su precio.");
        social(m,"Zapatos Oxford/Brogue V881",90,"Par de zapatos de vestir elaborado con cuero seleccionado, piezas textiles interiores y suela de caucho, con patronaje ajustado y acabado visible más exigente que el calzado de faena. Corte preciso, perforaciones o brogueado, costuras finas, hormado, pulido y remates elevan las horas de taller pese a su menor masa material.");
        social(m,"Zapatos de salón V881",62,"Par de zapatos bajos de silueta formal, confeccionado con cuero y textil en un corte estrecho que exige ajuste limpio sobre horma y acabados visuales cuidados. El coste deriva de selección de piel, patronaje, cosido fino, forro, conformado y terminación de bordes; su menor espesor defensivo no abarata ni encarece el objeto por estadística.");
        social(m,"Mocasines V881",48,"Par de mocasines de cuero flexible y forro textil construido con pocas piezas pero con una costura elevada y continua alrededor de la pala. El precio corresponde al cuero curtido, corte preciso, ensamblaje bilateral, cosido visible y terminación flexible; percepción del terreno y movilidad son propiedades físicas ajenas a la fórmula económica.");
        social(m,"Babuchas V881",42,"Par de babuchas de cuero blando con apoyo textil y talón flexible, de construcción sencilla y con pocos herrajes. La tasación se explica por piel curtida, corte de pala y planta, forro, costuras perimetrales y acabado del par, manteniéndose por debajo de zapatos hormados o botas que requieren estructura y refuerzos adicionales.");

        Set<String> canonical=new LinkedHashSet<>();
        ArmorCatalog.allFeetArmor().forEach(a->canonical.add(a.name()));
        if(canonical.size()!=18) throw new IllegalStateException(" espera 18 FEET canónicos.");
        if(!m.keySet().equals(canonical)) throw new IllegalStateException(" debe cubrir exactamente ArmorCatalog.allFeetArmor().");
        return Map.copyOf(m);
    }

    private static void social(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.SOCIAL_INTEREST,v,r));
    }
}
