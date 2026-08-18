package domain.economy;

import domain.inventory.item.meleeWeapons.MeleeWeaponCatalog;
import java.util.*;

/**
 *  — autoridad económica completa de armas cuerpo a cuerpo.
 *
 * Las herramientas capaces de combatir conservan naturaleza económica de interés social:
 * ser WeaponItem no convierte un pico, una hoz o un martillo profesional en armamento privativo.
 * Las armas concebidas primariamente para combate y las plataformas V881 especializadas son PRIVATE_USE.
 *
 * Daño, alcance, stagger, acciones y propiedades de gameplay no calculan el precio.
 */
public final class MeleeWeaponEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private MeleeWeaponEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Arma cuerpo a cuerpo sin tasación : "+name);
        return v;
    }
    public static Map<String,EconomicValuation> all(){return DATA;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        // Herramientas de trabajo que también funcionan como armas: INTERÉS SOCIAL.
        social(m,"Pico",75,
                "Herramienta bimanual de 2,4 kg formada por una cabeza de acero forjada con punta y borde de trabajo, ojo ajustado y mango largo de madera dura. El coste procede de masa metálica, forja, temple localizado y encabado resistente a impactos repetidos. Su elevada capacidad perforante en combate no interviene en la tasación.");
        social(m,"Zapapico",80,
                "Comparte mango y masa con el Pico, pero la cabeza combina punta con una hoja alargada tipo azada que exige más superficie de acero trabajada y un perfil de forja distinto. Acero, tratamiento térmico, conformado de la hoja y montaje sobre mango justifican una pequeña prima frente al Pico; sus estadísticas ofensivas no fijan el precio.");
        social(m,"Piqueta",45,
                "Herramienta compacta de 0,8 kg para cantería y albañilería, con una cabeza de acero que integra cara de martillo y cincel sobre un mango corto. Requiere forja y temple de dos superficies de trabajo diferentes, pero poca masa y una fabricación estandarizable mantienen el precio en el rango de herramienta profesional ordinaria.");
        social(m,"Cuchillo de Carnicero",55,
                "Hachuela profesional de 0,7 kg con hoja rectangular gruesa, filo resistente, espiga o remachado robusto y mango lavable. Acero apto para soportar impactos sobre articulaciones y huesos pequeños, tratamiento térmico y rectificado explican el coste; se valora como herramienta de carnicería aunque pueda emplearse como arma.");
        social(m,"Hacha de Leñador",85,
                "Cabeza de acero de doble filo sobre mango de madera seleccionada, con 1,4 kg de masa total. Forjar dos filos con geometrías de trabajo diferenciadas, templarlos sin fragilizar el ojo y ajustar un mango que soporte palanca e impactos prolongados exige más material y oficio que una herramienta corta común.");
        social(m,"Martillo de bola",35,
                "Martillo profesional de 0,65 kg con una cara plana y una peña esférica endurecidas para trabajo de metal, montadas sobre un mango resistente. La geometría es simple, la producción seriable y la masa de acero contenida; se paga tratamiento de las caras, acabado y encabado, no su contundencia como arma improvisada.");
        social(m,"Hoz",28,
                "Herramienta agrícola ligera de 0,25 kg: hoja curva de acero relativamente delgada, filo continuo y mango corto. Requiere conformado, temple y afilado, pero consume poca materia y admite producción artesanal repetitiva. Su capacidad de enganchar o cortar en combate no transforma su naturaleza económica.");
        social(m,"Guadaña",95,
                "Conjunto agrícola de 1,3 kg y gran longitud que exige una hoja larga, delgada y elástica, tratamiento térmico cuidadoso, afilado extenso y asta con empuñaduras ajustadas a la ergonomía de siega. El coste mayor que el de la Hoz procede de longitud de hoja, riesgo de deformación y montaje, no del alcance en combate.");
        social(m,"Horca",60,
                "Asta larga de madera con cabeza dentada capaz de soportar carga, torsión y trabajo agrícola repetido. La fabricación exige seleccionar una vara recta, conformar dientes resistentes, fijarlos con seguridad y proteger el conjunto frente a humedad. Se tasa como herramienta rural durable aunque su geometría permita punzar.");
        social(m,"Boathook",75,
                "Asta larga y ligera terminada en un herraje de gancho y apoyo contundente para aproximar, separar o gobernar embarcaciones. Madera recta, cabeza metálica forjada, unión reforzada y resistencia al ambiente húmedo dominan el coste; enganchar o desmontar combatientes es una consecuencia de su forma, no un multiplicador monetario.");

        // Armas diseñadas primariamente para combate: USO PRIVATIVO.
        privateUse(m,"Daga",110,
                "Arma corta de 0,4 kg con hoja rígida de doble filo, punta funcional, guarda, empuñadura y pomo. Frente a un cuchillo utilitario requiere simetría, dos filos, tratamiento térmico homogéneo, ajuste de la guarda y equilibrio controlado. La prima remunera manufactura armamentística y acabado, no sus dos modos de agarre ni su letalidad.");
        privateUse(m,"Cimitarra",280,
                "Espada de 0,95 kg y un metro de longitud con hoja curva de un solo filo, espiga completa, guarda y empuñadura. Forjar o rectificar una hoja larga manteniendo curvatura, sección, temple y alineación sin alabeos exige mucho más tiempo y control que una herramienta de filo corta; su capacidad de barrido a caballo no se monetiza.");
        privateUse(m,"Bō",30,
                "Arma de asta extremadamente sencilla: una vara de aproximadamente 1,8 m obtenida de madera recta y resistente, secada para limitar deformación, dimensionada, redondeada y acabada para evitar astillas. Su bajo precio refleja ausencia de metal y mecanismos; se clasifica como uso privativo porque su identidad canónica es un arma, no una herramienta laboral.");
        privateUse(m,"Espada Helicoidal",1400,
                "Espada técnica de 1,16 kg cuya hoja recta mantiene una torsión longitudinal continua de doce grados desde guarda a punta sin perder rigidez ni alineación. Conseguir que caras, filos y planos roten de forma progresiva exige utillaje, rectificado y control geométrico muy superiores a los de una espada convencional, además de tratamiento térmico sin distorsionar la hélice. Su MIRROR PARRY no fija el precio.");
        privateUse(m,"Espadón de Rotor",6500,
                "Espadón V881 de 3,8 kg con pala asimétrica de gran sección, distribución de masa deliberadamente adelantada y un mecanismo que retrae parte de la hoja dentro del armazón proximal. La cantidad de acero, nervio estructural, guías, bloqueos, mecanizado de piezas móviles, ajuste y pruebas de seguridad lo convierten en una plataforma de fabricación especializada. El Sistema Dorsal se tasa por separado.");
        privateUse(m,"Katana Termo-mecánica V881",2800,
                "Espada de 1,25 kg con hoja, vaina y mecanismo de desenvaine integrados para encender automáticamente alojamientos de amadou impregnado de resina junto al lomo y extinguirlos al envainar. A la manufactura de una hoja larga tratada se añaden canales, aislamiento térmico, mecanismo de ignición y vaina de precisión. Amadou y resina son consumibles independientes y el daño de Quemadura no calcula el precio.");
        privateUse(m,"Maza Electro-mecánica V881",2200,
                "Maza compacta de 1 kg con cabeza de acero, contactos conductores aislados, celda galvánica, acumulador, cableado y mecanismo capaz de descargar sólo al impacto fuerte y reiniciar después su ciclo de carga. La combinación de metal de impacto, aislamiento, componentes eléctricos y encapsulado resistente a golpes explica el precio; la descarga adicional de gameplay no se valora por daño.");

        // Pavesina ya fijada en : se integra aquí como autoridad melee completa sin duplicar doctrina.
        EconomicValuation pavesina=WeaponEconomicCatalog.valuation(WeaponEconomicCatalog.PAVESINA);
        m.put(pavesina.objectName(),pavesina);

        Set<String> canonical=new LinkedHashSet<>();
        MeleeWeaponCatalog.allCanonical().forEach(w->canonical.add(w.name()));
        if(canonical.size()!=18) throw new IllegalStateException("El catálogo melee canónico debe contener 18 armas incluida Pavesina.");
        if(!m.keySet().equals(canonical)) throw new IllegalStateException(" debe cubrir exactamente MeleeWeaponCatalog.allCanonical().");
        return Map.copyOf(m);
    }

    private static void social(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.SOCIAL_INTEREST,v,r));
    }
    private static void privateUse(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));
    }
}
