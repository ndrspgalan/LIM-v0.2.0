package domain.economy;

import domain.inventory.InventoryEntry;
import domain.inventory.item.ammunition.*;
import java.util.*;

/**
 *  — economía de munición.
 *
 * Los recipientes persistentes conservan valor al vaciarse. El valor actual de una instancia es:
 * continente + unidades restantes × valor unitario del contenido.
 */
public final class AmmunitionEconomicCatalog {
    private record Components(EconomicGoodType type,long structure,long unit,String rationale){}
    private static final Map<String,Components> COMPONENTS=components();
    private static final Map<String,EconomicValuation> UNITARY=unitary();
    private AmmunitionEconomicCatalog(){}

    public static Set<String> persistentNames(){ return COMPONENTS.keySet(); }
    public static Set<String> unitaryNames(){ return UNITARY.keySet(); }

    public static QuantityEconomicValuation current(AmmunitionCartridge item){
        Components c=requireComponents(item.name());
        long content=Math.multiplyExact(c.unit(),item.roundsRemaining());
        return q(item.name(),c,item.roundsRemaining(),content);
    }

    public static QuantityEconomicValuation current(LimeCartridgeCase item){
        Components c=requireComponents(item.name());
        long content=Math.multiplyExact(c.unit(),item.remainingUnits());
        return q(item.name(),c,item.remainingUnits(),content);
    }

    public static EconomicValuation unitary(String name){
        EconomicValuation v=UNITARY.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Munición unitaria sin tasación : "+name);
        return v;
    }

    public static long fullReferenceValueValeritas(String name){
        if(UNITARY.containsKey(name)) return UNITARY.get(name).priceValeritas().orElseThrow();
        Components c=requireComponents(name);
        int units=switch(name){
            case "Cartucho .46 de plomo" -> 20;
            case "Cargador .45 de Pistola V881" -> 8;
            case "Cargador de 9 mm V881" -> 25;
            case "Cartucho completo 7,92×57 mm V881" -> 5;
            case "Cargador bifilar .46 V881" -> 5;
            case "Cartucho de 4 proyectiles de 20 mm V881" -> 4;
            case "Estuche de Cartuchos de Cal Viva V881" -> 5;
            default -> throw new IllegalArgumentException(name);
        };
        return c.structure()+c.unit()*units;
    }

    public static long emptyReferenceValueValeritas(String name){ return requireComponents(name).structure(); }
    public static long unitContentValueValeritas(String name){ return requireComponents(name).unit(); }
    public static EconomicGoodType goodType(String name){
        if(UNITARY.containsKey(name)) return UNITARY.get(name).goodType();
        return requireComponents(name).type();
    }

    private static QuantityEconomicValuation q(String name,Components c,int remaining,long content){
        return new QuantityEconomicValuation(name,c.type(),c.structure(),content,c.structure()+content,
                c.rationale()+" La instancia conserva "+remaining+" unidad(es) de contenido; el continente persiste físicamente al agotarse y por eso nunca pierde su valor estructural.");
    }
    private static Components requireComponents(String name){
        Components c=COMPONENTS.get(Objects.requireNonNull(name));
        if(c==null) throw new IllegalArgumentException("Recipiente de munición sin desglose : "+name);
        return c;
    }

    private static Map<String,Components> components(){
        LinkedHashMap<String,Components> m=new LinkedHashMap<>();
        m.put("Cartucho .46 de plomo",new Components(EconomicGoodType.SOCIAL_INTEREST,18,2,
                "Contenedor tubular reutilizable de servicio y veinte cargas sencillas de plomo. El metal trabajado del recipiente conserva valor y cada proyectil añade plomo, fulminante y carga propulsora."));
        m.put("Cargador .45 de Pistola V881",new Components(EconomicGoodType.SOCIAL_INTEREST,30,2,
                "Cargador metálico mecanizado y ocho cartuchos de plomo encamisado en cobre. Muelle, labios de alimentación y tolerancias del cargador explican un continente más caro que la munición que alberga."));
        m.put("Cargador de 9 mm V881",new Components(EconomicGoodType.SOCIAL_INTEREST,38,2,
                "Cargador de alta capacidad con muelle largo, cuerpo estampado y veinticinco cartuchos de servicio. La producción seriada contiene el coste unitario del proyectil mientras el cargador sigue siendo una pieza durable."));
        m.put("Cartucho completo 7,92×57 mm V881",new Components(EconomicGoodType.SOCIAL_INTEREST,8,3,
                "Clip o contenedor ligero para cinco cartuchos de fusil. La mayor carga de latón, cobre, plomo y propelente por disparo eleva el contenido frente a munición de pistola, aunque el continente es sencillo."));
        m.put("Cargador bifilar .46 V881",new Components(EconomicGoodType.PRIVATE_USE,70,18,
                "Cargador especializado para cinco cartuchos con núcleo de wolframio, armadura conductora y sabot separable. Material estratégico, geometría compleja y tolerancias eléctricas restringen su mercado y elevan especialmente el contenido."));
        m.put("Cartucho de 4 proyectiles de 20 mm V881",new Components(EconomicGoodType.PRIVATE_USE,90,45,
                "Cassette estructural para cuatro proyectiles antimaterial de 20 mm. Cada unidad consume mucha más masa metálica, propelente y mecanizado que la munición personal; el continente debe soportar además cargas grandes sin deformarse."));
        m.put("Estuche de Cartuchos de Cal Viva V881",new Components(EconomicGoodType.PRIVATE_USE,65,110,
                "Estuche robusto para cinco cartuchos presurizados de tres litros. Cada carga exige recipiente resistente, agente de cal viva acondicionado y sellado seguro; el riesgo químico y la logística de masa dominan el contenido."));
        return Map.copyOf(m);
    }

    private static Map<String,EconomicValuation> unitary(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        p(m,"Cohete de Racimo V881 de 85 mm",EconomicGoodType.PRIVATE_USE,400,
                "Cohete individual de cuatro kilogramos con motor, carcasa, estabilización y carga de racimo. Es munición militar compleja cuya fabricación y custodia requieren infraestructura especializada; el precio no deriva de su daño.");
        p(m,"Guijarro",EconomicGoodType.SOCIAL_INTEREST,1,
                "Piedra ordinaria seleccionada únicamente por masa y geometría adecuadas para una Honda. Su valor comercial mínimo representa selección y transporte; puede recuperarse del entorno sin cadena manufacturera.");
        p(m,"Flecha perforante",EconomicGoodType.SOCIAL_INTEREST,6,
                "Astil recto, emplumado y punta estrecha endurecida. Requiere madera seleccionada, conformado y alineación, pero usa materiales comunes y una fabricación artesanal reproducible.");
        p(m,"Flecha de Púas",EconomicGoodType.SOCIAL_INTEREST,8,
                "Flecha con cabeza barbada que exige más trabajo de forja y ajuste que una punta perforante simple. La geometría de púas aumenta tiempo de manufactura y riesgo de descarte.");
        p(m,"Flecha de Hoja",EconomicGoodType.SOCIAL_INTEREST,9,
                "Cabeza de hoja más ancha, afilada y equilibrada sobre un astil seleccionado. Consume más metal y acabado de filo que una flecha perforante ordinaria.");
        p(m,"Flecha de Yesca",EconomicGoodType.SOCIAL_INTEREST,11,
                "Flecha preparada para portar yesca de Amadou sin perder estabilidad en vuelo. El precio incluye soporte y acondicionamiento incendiario, pero no Amadou ni Pedernal:  consume esos recursos por separado al encenderla.");
        return Map.copyOf(m);
    }
    private static void p(Map<String,EconomicValuation> m,String n,EconomicGoodType t,long v,String r){
        m.put(n,EconomicValuation.priced(n,t,v,r));
    }
}
