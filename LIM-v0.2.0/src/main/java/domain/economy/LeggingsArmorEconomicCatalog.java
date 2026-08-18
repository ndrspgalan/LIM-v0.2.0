package domain.economy;

import domain.inventory.item.armor.ArmorCatalog;
import java.util.*;

/**
 *  — autoridad económica exhaustiva de LEGGINGS.
 *
 * La ropa interior, civil, laboral, ecuestre y las protecciones de cuero de uso profesional permanecen
 * SOCIAL_INTEREST. Las armaduras dedicadas de papel, Ébano, placas y lamelar son PRIVATE_USE.
 * El precio se justifica por materiales, patronaje, curtido, cierres, forja y horas de manufactura;
 * cobertura, protección y capacidad de inventario no son multiplicadores de gameplay.
 */
public final class LeggingsArmorEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private LeggingsArmorEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("LEGGINGS sin tasación : "+name);
        return v;
    }

    public static Map<String,EconomicValuation> all(){return DATA;}

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        social(m,"Calzoncillos largos V881",20,"Prenda interior de paño ligero con dos perneras completas, cintura, tiro y varias costuras longitudinales. El coste procede del tejido, corte bilateral, remates y confección suficiente para soportar roce continuado bajo otras capas; su cobertura corporal no interviene en la tasación.");
        social(m,"Calzoncillos hasta la rodilla V881",14,"Prenda interior corta que consume menos tejido que la variante de pernera completa y emplea un patronaje sencillo de cintura, tiro y dos perneras rematadas. Material corriente, pocas operaciones de confección y facilidad de reparación justifican una tasación baja de uso cotidiano.");
        social(m,"Pantalón interior de punto V881",24,"Prenda interior confeccionada en tejido de punto continuo, cuya elasticidad exige una estructura textil más trabajada que un paño plano equivalente. El coste recoge hilo, tejido, corte, uniones y acabado elástico estable; no se incrementa por sus estadísticas de protección.");
        social(m,"Drawers femeninos V881",18,"Prenda interior de dos perneras holgadas unidas a una cintura común. Su precio deriva de una cantidad moderada de paño, patronaje amplio, dobladillos, cintura y costuras de unión, con manufactura doméstica o de taller sencilla y reparable; la amplitud de zancada no se monetiza.");
        social(m,"Enagua V881",28,"Falda interior ligera de mayor superficie textil que unos drawers, con cintura, paño continuo y bajo rematado. La tasación recoge principalmente consumo de tejido, corte, fruncido o ajuste de cintura y longitud de costura, manteniéndose como prenda ordinaria de interés social.");
        social(m,"Enagua reforzada V881",40,"Enagua con una segunda hoja efectiva y refuerzos en zonas de tracción y roce. Frente a la versión simple necesita más materia, más líneas de costura y trabajo de montaje para estabilizar cintura, uniones y borde inferior; el incremento responde a construcción física, no a protección abstracta.");
        social(m,"Enagua acolchada V881",62,"Prenda interior compuesta por paños, relleno ligero y una retícula de costuras que inmoviliza el acolchado. El coste adicional procede de varias capas, material de relleno, preparación de cámaras y numerosas pasadas de costura para evitar migraciones durante el uso prolongado.");
        social(m,"Enagua dividida V881",34,"Enagua amplia cuyo paño inferior se divide en dos conductos independientes. Requiere más patronaje y uniones alrededor de entrepierna y perneras que una enagua simple, además de cintura y bajos rematados; la tasación refleja esa confección adicional sin valorar ventajas de movilidad.");

        social(m,"Pantalón recto V881",32,"Pantalón civil de paño ordinario con cintura, tiro, dos perneras y bolsillos integrados. Su precio reúne tejido, patronaje simétrico, cierres, forros o vistas de bolsillo y costuras sometidas a uso cotidiano. Los ocho espacios de almacenamiento emergen de esos bolsillos físicos y no añaden una prima de gameplay.");
        social(m,"Pantalón formal V881",48,"Pantalón sastreado de caída controlada que exige mejor selección de paño, cintura estructurada, planchado de costuras, ajuste más preciso y acabados limpios. Los bolsillos forman parte de la confección ordinaria; su capacidad de inventario no se usa como variable para calcular el precio.");
        social(m,"Pantalón de trabajo V881",42,"Pantalón de faena en paño denso con refuerzos localizados y bolsillos de trabajo. Consume más tejido y requiere costuras gruesas, piezas superpuestas y puntos de carga reforzados para tolerar abrasión y reparaciones. Sus numerosos bolsillos cuestan por material y confección, no por celdas de inventario.");
        social(m,"Pantalón de cintura alta V881",38,"Pantalón civil con talle elevado y cintura más desarrollada que estabiliza la prenda durante movimiento prolongado. El coste adicional frente al corte recto procede del mayor patronaje de cintura, vistas, cierres y ajuste del torso inferior, además de bolsillos convencionales y sus remates.");
        social(m,"Pantalón holgado V881",34,"Pantalón de perneras amplias y tiro generoso que consume más paño que un corte estrecho, pero evita sastrería compleja. La cifra combina superficie de tejido, cintura, uniones largas y bolsillos ordinarios, manteniendo una manufactura sencilla y reparable de amplia difusión social.");
        social(m,"Pantalón marinero V881",40,"Pantalón robusto de inspiración marinera con cierre frontal amplio y patronaje pensado para trabajo y movimiento. La tasación recoge paño resistente, piezas de cierre adicionales, botones o herrajes, costuras reforzadas y bolsillos; no atribuye valor monetario a su comportamiento dentro del inventario.");
        social(m,"Pantalón de montar V881",52,"Pantalón ecuestre con refuerzos en zonas de contacto con silla y montura, patronaje de entrepierna más exigente y ajuste que evita pliegues abrasivos. Paño resistente, piezas de refuerzo, costuras adicionales y confección especializada explican el sobrecoste respecto a un pantalón civil corriente.");
        social(m,"Breeches V881",40,"Pantalón ecuestre corto y ceñido bajo la rodilla, con patronaje específico para montar y cierres o ajustes en el extremo de las perneras. Usa menos paño que un pantalón largo, pero exige más precisión de ajuste y refuerzo en zonas de fricción, equilibrando su coste final.");
        social(m,"Knickerbockers V881",38,"Pantalón recogido bajo la rodilla con volumen controlado por pliegues, puños o cierres. El coste deriva del paño adicional necesario para formar el volumen, del patronaje, de los remates inferiores y de los bolsillos, sin asignar precio a la libertad de movimiento que proporciona.");
        social(m,"Bombachos V881",38,"Prenda de perneras muy amplias que consume una cantidad apreciable de tejido pero utiliza construcción relativamente simple. Cintura, fruncidos, recogidos inferiores y largas costuras explican la cifra; la ausencia de herrajes complejos mantiene la tasación por debajo de prendas sastre o ecuestres especializadas.");
        social(m,"Falda recta V881",32,"Falda civil de geometría contenida con cintura, paneles principales, cierre y bajo. Requiere menos articulación de piezas que un pantalón bífido, pero utiliza una superficie de paño continua y acabados visibles; se tasa como confección ordinaria y no por el porcentaje anatómico que cubre.");
        social(m,"Falda amplia V881",42,"Falda de gran vuelo cuyo principal factor económico es el mayor consumo de tejido, acompañado de fruncido o pliegues en cintura y una longitud considerable de bajo. El patronaje sigue siendo convencional, por lo que el incremento frente a la falda recta procede sobre todo de materia y horas de costura.");
        social(m,"Falda de paseo V881",48,"Falda preparada para marcha con patronaje que controla el volumen y bolsillos integrados de uso cotidiano. Su precio combina una cantidad media-alta de paño, cintura, paneles, cierres, remates y confección de bolsillos; las doce celdas resultantes no se monetizan como bonificación de inventario.");
        social(m,"Falda de trabajo V881",40,"Falda laboral confeccionada en paño resistente, con refuerzos y bolsillos amplios en puntos accesibles. El coste surge del tejido más denso, piezas superpuestas, costuras de carga y facilidad de reparación. Su capacidad de almacenamiento es consecuencia material de esos bolsillos, no una estadística tasable.");
        social(m,"Falda de montar V881",58,"Falda ecuestre de gran superficie y patronaje adaptado a la apertura sobre una montura. Consume más tejido y exige cortes, solapes y refuerzos específicos para evitar tensiones en asiento y piernas, además de bolsillos funcionales. Esa confección especializada justifica la cifra sin recurrir a ventajas de juego.");
        social(m,"Falda dividida V881",50,"Prenda de apariencia amplia que incorpora una separación estructural para cada pierna. Frente a una falda continua añade patronaje de entrepierna, costuras internas y más piezas de unión, además de cintura y bolsillos. La complejidad de confección explica el precio superior a una falda ordinaria.");
        social(m,"Falda ornamentada V881",72,"Falda de paño amplio cuyo coste incorpora bordados, aplicaciones, pasamanería u otros acabados ornamentales realizados después del ensamblaje principal. A materiales y confección base se suman muchas horas de trabajo fino y control visual, justificando una tasación claramente superior a la prenda utilitaria.");
        social(m,"Sobrefalda V881",30,"Segunda capa textil parcial destinada a caer sobre otra falda. Consume menos materia que una falda completa y su estructura es sencilla, aunque requiere cintura o sistema de sujeción, paneles bien rematados y acabado visible. Por ello mantiene una tasación moderada dentro de la indumentaria civil.");
        social(m,"Kilt V881",38,"Prenda envolvente que concentra una cantidad relevante de paño en pliegues regulares y solape frontal. Aunque carece de perneras, el plisado, fijación de cintura, cierres y remates exigen más manipulación del tejido que un simple paño envolvente, justificando su coste de confección intermedio.");
        social(m,"Sarong V881",22,"Paño rectangular amplio cuya transformación en prenda exige poca costura y prácticamente ningún patronaje complejo. El precio corresponde sobre todo a la superficie y calidad del tejido, teñido, remates perimetrales y sistema sencillo de sujeción, por lo que resulta una de las piezas MIDDLE más económicas.");

        social(m,"Pantalón de cuero endurecido V881",190,"Pantalón protector de gran superficie compuesto por cuero endurecido dominante y paneles textiles para articulación y confort. Curtido, selección de pieles grandes, conformado, endurecimiento localizado, uniones resistentes y bolsillos físicos elevan el coste, pero sigue siendo una prenda civil/profesional y no una armadura militar privativa.");
        social(m,"Polainas de cuero con correas y hebillas V881",110,"Par de polainas de cuero endurecido sujeto mediante varias correas y hebillas. El coste reúne selección y curtido de piel, corte simétrico, endurecimiento, perforado, remaches, herrajes y montaje bilateral; la protección resultante es una consecuencia material y no el fundamento de la cifra.");
        social(m,"Polainas rígidas de cierre lateral V881",145,"Polainas realizadas íntegramente en cuero endurecido y conformado para conservar una geometría rígida alrededor de la pierna. Requieren piel gruesa, moldeado, endurecimiento homogéneo, cierre lateral robusto, remates y ajuste por pares, aumentando la manufactura frente a una polaina flexible con correas.");
        social(m,"Polainas de cuero bordadas y ornamentadas V881",180,"Par de polainas de cuero trabajado con bordados y ornamentación visible además de sus cierres funcionales. A curtido, corte, endurecimiento y ensamblaje se añaden hilo, aplicaciones y horas de acabado manual preciso; el sobrecoste se debe a artesanía decorativa, no a mejores estadísticas defensivas.");
        social(m,"Chaparreras cerradas (shotgun) V881",220,"Chaparreras cerradas de cuero endurecido que emplean grandes paneles para envolver las piernas y múltiples líneas de unión y cierre. La cantidad de piel apta, curtido, corte de piezas extensas, conformado y ensamblaje bilateral explican el precio; su extensa cobertura física no se convierte directamente en moneda.");
        social(m,"Chaparreras de ala ancha (batwing) V881",190,"Chaparreras de grandes alas laterales en cuero endurecido con apoyo textil. Consumen paneles amplios de piel, pero su geometría abierta reduce cierres y conformado respecto a una variante completamente envolvente. Curtido, corte, remates, correajes y ensamblaje justifican una tasación elevada pero contenida.");
        social(m,"Chaparreras ornamentadas de tradición charra V881",320,"Chaparreras de gran superficie fabricadas con cuero seleccionado y una carga importante de ornamentación, bordado, aplicaciones y herrajes. El coste combina abundante materia prima, curtido, corte de paneles grandes, montaje y muchas horas de trabajo decorativo especializado, no una prima por protección de gameplay.");

        priv(m,"Polainas de Papel V881",240,"Armadura dedicada de piernas formada por numerosos estratos de papel de corteza y soporte textil, compactados, martillados, conformados y fijados hasta producir paneles estables. Aunque la fibra de partida es barata, el número de hojas, secado, prensado, cosido, articulación y acabado hacen intensiva su manufactura.");
        m.put("Polainas del Guerrero de Ébano", EbonyWarriorArmorEconomicPolicy.valuation("Polainas del Guerrero de Ébano"));
        priv(m,"Polainas de Caballero",3900,"Conjunto histórico de quijotes, grebas y sabatones integrados en placas de acero articuladas sobre soporte textil. La gran masa metálica exige forja, laminado, tratamiento térmico, conformado anatómico, numerosas articulaciones, remaches, correas y ajuste individual; los sabatones añaden piezas móviles y trabajo especializado.");
        priv(m,"Polainas de Caballero hasta las rodillas V881",4400,"Grebas y protecciones de rodilla V881 construidas en aleación acero-wolframio y articuladas sobre textil, deliberadamente separadas del calzado. Aunque contienen menos masa que el conjunto histórico con sabatones, el material denso, mecanizado, tolerancias, piezas solapadas y ajuste fino elevan la manufactura por kilogramo.");
        priv(m,"Polainas Lamelares Históricas Pesadas",2800,"Protección pesada compuesta por una gran cantidad de lamelas de acero perforadas y enlazadas en filas solapadas sobre soporte textil. Cada lámina es relativamente simple, pero la suma de corte, tratamiento, perforado, cordaje, alineado, sustitución de piezas defectuosas y muchas horas de ensamblaje domina el coste.");

        Set<String> canonical=new LinkedHashSet<>();
        ArmorCatalog.allLeggings().forEach(a->canonical.add(a.name()));
        if(canonical.size()!=40) throw new IllegalStateException(" espera 40 LEGGINGS canónicos.");
        if(!m.keySet().equals(canonical)) throw new IllegalStateException(" debe cubrir exactamente ArmorCatalog.allLeggings().");
        return Map.copyOf(m);
    }

    private static void social(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.SOCIAL_INTEREST,v,r));
    }
    private static void priv(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));
    }
}
