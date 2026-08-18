package domain.economy;

import domain.inventory.item.armor.ArmorCatalog;
import java.util.*;

/**
 *  — autoridad económica de las 37 piezas HEAD canónicas.
 *
 * La protección no calcula el precio. La tasación se deriva de materia, masa, confección,
 * sombrerería, óptica, metalurgia, tratamiento, mecanismos, sellado, calibración e infraestructura.
 * El tipo técnico ArmorPiece no fuerza por sí mismo la categoría PRIVATE_USE.
 */
public final class HeadArmorEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private HeadArmorEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("HEAD sin tasación : "+name);
        return v;
    }
    public static Map<String,EconomicValuation> all(){return DATA;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        // TACTICAL / profesional
        priv(m,"Casco Replegable del Aeronauta",14000,
                "Casco V881 de 3,5 kg que combina estructura de acero y cobre, módulos móviles de acero y magnetita, visor tripartito de vidrio laminado, articulaciones replegables e intercom terrestre. La fabricación exige mecanizado, aislamiento eléctrico, alineación de piezas móviles, óptica, montaje y pruebas funcionales. Su cobertura, protección e intercom no se convierten en multiplicadores monetarios.");
        priv(m,"Panóptico del Ilustrado",18000,
                "Matriz de 3,5 kg predominantemente vítrea formada por numerosas celdas hexagonales laminadas, cámara ocular, fotófono y herrajes aislados. Tallar, laminar, ensamblar y alinear una arquitectura óptica multidireccional con repetición celular y comunicación fotónica requiere vidrio de alta calidad, utillaje, calibración y una manufactura extremadamente especializada; su inmunidad o perfil defensivo no fijan el precio.");
        social(m,"Respirador Integral V881",650,
                "Equipo profesional de protección respiratoria y craneal compuesto por caucho moldeado, visor segmentado de vidrio laminado, piezas de acero, soporte textil, correaje y módulo filtrante sustituible. El precio responde a materiales heterogéneos, sellado, ajuste facial, válvulas/filtro y pruebas de estanqueidad; su inmunidad o reducción térmica son consecuencias funcionales, no criterios de tasación.");
        priv(m,"Casco de Papel V881",180,
                "Casquete multicapa de 0,5 kg fabricado con papel grueso martillado, prensado y compactado sobre molde alrededor de un forro textil, seguido de barnizado y lacado. Aunque la materia prima es barata, la densificación, secado, conformado y acabados resistentes al agua y eléctricamente aislantes exigen mucho más trabajo que una prenda de papel ordinaria; su protección emergente no se monetiza.");
        priv(m,"Casco Jet de cuero endurecido con vidrio laminado V881",520,
                "Casco compuesto de 1 kg con cuero endurecido, visera segmentada de vidrio laminado y correa de sujeción. Curtido y endurecimiento del cuero, moldeado, corte y laminación del visor, perforaciones, herrajes y ajuste explican el precio de una protección especializada de movilidad; la cobertura parcial no se usa como fórmula de coste.");
        social(m,"Gafas para soldadura V881",160,
                "Protección ocular profesional con lentes de vidrio laminado, filtros abatibles, montura, protección lateral y elementos de articulación. El valor se concentra en vidrio óptico, filtros, bisagras, ajuste y resistencia a chispas y radiación visible intensa, no en el pequeño porcentaje de cobertura corporal.");
        social(m,"Cofia acolchada V881",38,
                "Cofia de paño cosido alrededor de un relleno distribuido, con retícula de costuras que inmoviliza el acolchado y permite usarla como interfaz bajo casco. Tela, relleno, patronaje craneal y numerosas costuras justifican una prima modesta sobre una prenda simple, sin convertirla en equipo privativo.");
        priv(m,"Casco de Caballero",1700,
                "Yelmo cerrado histórico de 2,5 kg formado por calota, protección facial, placas móviles, articulaciones, remaches y forro textil. La gran masa de acero, conformado tridimensional, tratamiento térmico, ajuste de piezas articuladas y acabado manual explican su coste; no se calcula a partir del 100% de cobertura.");
        social(m,"Gafas de visión V881",260,
                "Gafas correctoras de prescripción individual con dos lentes graduadas de vidrio laminado, montura, puente y patillas. Además de fabricar la montura, cada lente debe medirse, tallarse, pulirse y centrarse para compensar el defecto refractivo de un usuario concreto; la prima procede de esa óptica personalizada y del ajuste, no de su protección ocular.");
        priv(m,"Casco Barbudo V881",2200,
                "Yelmo V881 de 2,4 kg con placas de acero conformadas, protección de mandíbula y mejillas, articulaciones, ranuras de observación y forro. Requiere numerosas piezas con tolerancias de solape, remachado, tratamiento térmico y ajuste facial; la continuidad protectora es un resultado del diseño, no una función de precio.");
        priv(m,"Casco del Cruzado V881",1950,
                "Casco de 2,15 kg compuesto por calota compacta y máscara facial de acero desmontable. El coste procede de chapa conformada, anclajes, bisagras o cierres, tratamiento térmico, forro y ajuste que permita retirar la máscara sin perder alineación; su perfil defensivo no interviene como multiplicador.");
        priv(m,"Casco Espartano V881",900,
                "Casco de 1,55 kg de bronce con calota profunda, carrilleras prolongadas y abertura facial, conformado a partir de una masa metálica sustancial. Fundición o batido, recocidos, martillado, recorte, acabado y ajuste craneal explican su coste; la protección de bronce se considera consecuencia material y no tarifa por estadística.");

        // LOWER_ACCESSORY
        social(m,"Cubrecuellos del Viajero",18,
                "Tubo textil denso de 0,15 kg patronado para envolver cuello y mandíbula sin restringir demasiado la cabeza. Tejido resistente, costuras, remates y un acabado pensado para viaje explican un precio bajo pero superior al de un simple pañuelo.");
        social(m,"Bufanda V881",14,
                "Banda larga de tejido flexible de 0,18 kg con bordes rematados y longitud suficiente para varias vueltas. La materia textil y una confección muy sencilla dominan el coste; abrigo, cobertura o protección incidental no elevan artificialmente su precio.");
        social(m,"Bufanda gruesa V881",24,
                "Bufanda de paño más denso y pesado, 0,35 kg, que requiere aproximadamente el doble de material y un tejido/acabado capaz de conservar cuerpo tras vueltas repetidas. Su mayor coste frente a la bufanda normal nace de materia y confección, no de puntos de protección.");
        social(m,"Pañuelo de cuello V881",7,
                "Paño cuadrado ligero de 0,08 kg, cortado, rematado y preparado para plegarse y anudarse. La cadena productiva es elemental y utiliza poca fibra, por lo que se mantiene entre las piezas HEAD más baratas.");
        social(m,"Pañuelo de Jornalero V881",6,
                "Pañuelo de trabajo de 0,07 kg concebido para sudor, polvo y uso diario. Tejido común, corte simple, costuras perimetrales y ausencia de ornamentación permiten una producción barata y repetible.");
        social(m,"Bandana V881",6,
                "Paño triangular de 0,06 kg con material y manufactura mínimos: tejido resistente, corte y remate de bordes. Se valora como accesorio cotidiano, sin convertir su cobertura incidental en un coste defensivo.");
        social(m,"Pañuelo envolvente V881",17,
                "Paño largo de 0,22 kg que utiliza más tejido y longitud que un pañuelo ordinario y debe mantener suficiente resistencia para varias vueltas y extremos remetidos. Materia y acabado justifican la diferencia de precio.");

        // UPPER_ACCESSORY
        social(m,"Capucha del Viajero V881",32,
                "Capucha amplia de 0,32 kg con paño tratado, faldón posterior, patronaje tridimensional y acabado impermeable. Mayor superficie, costuras curvas y tratamiento frente al agua la sitúan por encima de un pañuelo o gorro simple.");
        social(m,"Boina V881",18,
                "Casquete flexible de paño o fieltro de 0,12 kg, conformado en corona circular y ajustado mediante banda perimetral. Requiere poco material y una manufactura de sombrerería sencilla.");
        social(m,"Canotier V881",38,
                "Sombrero de 0,20 kg con copa baja y ala rígida construido mediante trama vegetal revestida y banda/acabado. Mantener una geometría plana y estable exige conformado y secado que lo hacen más costoso que una boina blanda.");
        social(m,"Sombrero de copa V881",75,
                "Sombrero estructurado de 0,32 kg con copa cilíndrica alta, ala, cuerpo textil endurecido, forro y acabado exterior. Moldes, rigidización, ensamblaje y acabado formal requieren trabajo especializado de sombrerería.");
        social(m,"Bombín V881",55,
                "Sombrero de fieltro endurecido de 0,28 kg con copa redondeada y ala corta. Vapor, presión, moldeado, rigidización, recorte y acabado explican su precio por encima de tocados blandos.");
        social(m,"Homburg V881",62,
                "Sombrero formal de fieltro de 0,24 kg con copa hendida y ala levantada, cuya geometría se fija mediante vapor, presión y acabado cuidadoso. La materia es común, pero el conformado y la uniformidad visual requieren sombrerería experta.");
        social(m,"Sombrero de ala ancha V881",58,
                "Sombrero de 0,26 kg con ala de gran diámetro que consume más superficie de paño tratado y requiere rigidización para evitar que pierda forma. Material, moldeado y acabado impermeable dominan el coste.");
        social(m,"Sombrero de Jornalero V881",22,
                "Sombrero utilitario de 0,18 kg, copa baja y ala funcional, construido para tolerar polvo, plegado parcial y uso prolongado. Materiales comunes, poca ornamentación y fabricación repetible mantienen un precio modesto.");
        social(m,"Sombrero de montar V881",68,
                "Sombrero de 0,25 kg con copa ceñida, ala media y estructura suficientemente firme para resistir viento y movimiento ecuestre. Requiere conformado, rigidización, banda interior y ajuste más preciso que un sombrero de trabajo ordinario.");
        social(m,"Sombrero charro V881",145,
                "Sombrero de 0,55 kg con copa elevada, ala muy ancha, borde reforzado y ornamentación sobre una estructura rígida. La gran superficie, material adicional, refuerzos, moldeado y trabajo decorativo lo convierten en la pieza de sombrerería ordinaria más intensiva del catálogo HEAD.");
        social(m,"Sombrero de paseo V881",58,
                "Sombrero urbano de 0,23 kg con estructura moderada, ala, copa y acabado cuidado. Requiere moldeado y terminación de sombrerería, pero usa menos material y refuerzo que modelos de ala muy amplia o ceremonial.");
        social(m,"Capota V881",70,
                "Tocado envolvente de 0,30 kg con cuerpo estructurado, cintas o cierres y patronaje tridimensional que rodea laterales y nuca. El ensamblaje de varias piezas y el mantenimiento de la forma explican una manufactura superior a la de un gorro blando.");
        social(m,"Tocado V881",85,
                "Pieza de 0,28 kg cuyo valor procede principalmente de composición ornamental, estructura ligera, fijaciones, pliegues y acabado social. La cantidad de materia no es extrema, pero el trabajo manual y la presentación elevan el precio.");
        social(m,"Turbante V881",30,
                "Conjunto de 0,38 kg formado por una longitud considerable de tela preparada para enrollarse en múltiples capas. Consume más tejido que muchos sombreros blandos, pero requiere poca estructura rígida y una manufactura inicial sencilla.");
        social(m,"Fez V881",28,
                "Casquete de 0,16 kg en fieltro conformado, copa cilíndrica/troncocónica y acabado compacto. El moldeado y la terminación son sencillos y repetibles, con poca materia y sin armazón complejo.");
        social(m,"Gorro de punto V881",10,
                "Gorro flexible de 0,10 kg obtenido por tejido de punto con poca materia y sin elementos rígidos. La fabricación repetible y la geometría simple justifican un precio bajo de accesorio cotidiano.");
        social(m,"Gorra V881",16,
                "Tocado de 0,14 kg construido mediante paneles cosidos, banda y visera semirrígida. Utiliza poca materia, pero requiere varias piezas y ensamblaje más preciso que un simple gorro de punto.");
        social(m,"Sombrero de Cazador V881",52,
                "Sombrero de exterior de 0,34 kg con mayor masa de tejido, estructura resistente, ala funcional y tratamiento impermeable para uso prolongado. El coste responde a material, refuerzo, acabado frente al agua y confección durable.");

        Set<String> canonical=new LinkedHashSet<>();
        ArmorCatalog.allHeadArmor().forEach(a->canonical.add(a.name()));
        if(canonical.size()!=37) throw new IllegalStateException(" espera 37 piezas HEAD canónicas.");
        if(!m.keySet().equals(canonical)) throw new IllegalStateException(" debe cubrir exactamente ArmorCatalog.allHeadArmor().");
        return Map.copyOf(m);
    }

    private static void social(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.SOCIAL_INTEREST,v,r));
    }
    private static void priv(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));
    }
}
