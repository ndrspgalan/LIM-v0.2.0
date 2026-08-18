package domain.economy;

import domain.inventory.item.firearms.FirearmCatalog;
import domain.inventory.item.rangedWeapons.RangedWeaponCatalog;
import domain.inventory.item.throwingWeapons.ThrowingWeaponCatalog;

import java.util.*;

/**
 *  — autoridad económica de armas arrojadizas, a distancia convencionales y firearms.
 *
 * Cada precio corresponde a una unidad física o plataforma desnuda. Munición, batería,
 * cargadores, ópticas, correa y bípode conservan su valoración independiente.
 * Daño, alcance, cadencia y bonificaciones nunca son multiplicadores monetarios.
 */
public final class RemoteWeaponEconomicCatalog {
    private static final Map<String,EconomicValuation> THROWING=buildThrowing();
    private static final Map<String,EconomicValuation> RANGED=buildRanged();
    private static final Map<String,EconomicValuation> FIREARMS=buildFirearms();
    private static final Map<String,EconomicValuation> ALL=merge();

    private RemoteWeaponEconomicCatalog(){}

    public static EconomicValuation valuation(String name){
        EconomicValuation v=ALL.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Arma remota sin tasación : "+name);
        return v;
    }
    public static Map<String,EconomicValuation> throwingWeapons(){return THROWING;}
    public static Map<String,EconomicValuation> rangedWeapons(){return RANGED;}
    public static Map<String,EconomicValuation> firearms(){return FIREARMS;}
    public static Map<String,EconomicValuation> all(){return ALL;}

    private static Map<String,EconomicValuation> buildThrowing(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        p(m,"Cápsula de Gas Amonio V881",140,
                "Unidad química de 0,35 kg fabricada alrededor de un frasco de cristal grueso que debe sobrevivir al transporte y romperse de forma fiable al impacto. Vidrio seleccionado, cierre estanco, llenado controlado, acondicionamiento de una carga incapacitante y protocolos de seguridad explican el precio. La composición permanece confidencial y el efecto de Veneno/Toxicidad no se monetiza como estadística.");
        p(m,"Granada Incendiaria de Terracota V881",110,
                "Vasija texturizada de terracota de 0,55 kg, cocida para resistir manipulación pero fracturarse al impacto, con carga incendiaria sellada y sistema de iniciación V881. Cerámica, llenado, sellado y preparación segura constituyen una munición arrojadiza desechable; Quemadura y área de efecto no intervienen como multiplicadores.");
        p(m,"Granada de Huevo con Fósforo y Azufre V881",45,
                "Arrojadiza mínima de 0,06 kg basada en una envolvente deliberadamente frágil y una pequeña carga reactiva preparada en condiciones controladas. Consume poca materia y su cuerpo es barato, pero manipulación, dosificación y sellado de reactivos peligrosos impiden valorarla como un huevo ordinario. El vaciado de PA es consecuencia mecánica, no precio.");
        p(m,"Cuchillo Arrojadizo V881",28,
                "Pieza recuperable de 0,10 kg reducida a una geometría estrecha de acero, sin pomo ni mecanismo. Corte de la barra, conformado, tratamiento térmico, rectificado y equilibrio longitudinal son operaciones sencillas y seriables; por ello cuesta mucho menos que una hoja de combate compleja pese a ser un arma canónica.");
        coverageThrowing(m);
        return Map.copyOf(m);
    }

    private static Map<String,EconomicValuation> buildRanged(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        p(m,"Honda",12,
                "Dos cordones resistentes y una cazoleta flexible constituyen una de las armas fabricables más simples de LIM. La materia prima es común, la masa es mínima y no existen mecanismos ni tratamientos metalúrgicos; el precio remunera trenzado, selección de fibra/cuero y acabado durable. Guijarros o balas de plomo se valoran aparte.");
        p(m,"Arco Simple Recurvo",180,
                "Arco de 0,80 kg construido con una pieza o laminado de madera seleccionada, perfil recurvado, tillering, cuerda y acabado protector. La madera debe secarse y trabajarse evitando fibras defectuosas y ambas palas requieren equilibrio elástico; es artesanía especializada pero todavía reparable y materialmente sencilla. Las flechas no están incluidas.");
        p(m,"Arco Compuesto",650,
                "Plataforma artesanal de 0,75 kg que combina madera, cuerno y tendón —con ébano donde aporta rigidez— mediante adhesiones, curado prolongado, conformado y ajuste de materiales con respuestas mecánicas distintas. El coste procede de selección, meses de preparación, riesgo de fallo de laminación y trabajo experto; su mayor alcance y bonificación terminal no fijan la cifra. Las flechas se tasan aparte.");
        coverageRanged(m);
        return Map.copyOf(m);
    }

    private static Map<String,EconomicValuation> buildFirearms(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        p(m,"Rifle Neumático de Repetición V881",3500,
                "Plataforma de 4,50 kg con depósito y circuito neumático de alta presión, válvulas repetibles, cañón mecanizado, acción de alimentación y elementos estructurales capaces de mantener estanqueidad y alineación. Su coste combina acero, sellos, mecanizado y pruebas de presión. Se tasa descargada: el Cartucho .46, la correa, el bípode y la óptica son bienes independientes.");
        p(m,"Fusil Bifilar Electromagnético V881",18000,
                "Sistema de 8,70 kg que sustituye una parte de la propulsión convencional por componentes electromagnéticos de precisión: conductores, aislamiento, elementos de conmutación, geometría de aceleración, estructura rígida y controles capaces de trabajar con cartuchos bifilares. Material eléctrico, tolerancias, calibración y baja escala productiva dominan el coste. Cargador bifilar, batería y accesorios no están incluidos.");
        p(m,"Pistola Autocargadora V881",2500,
                "Arma compacta de 0,92 kg compuesta por cañón, corredera, cierre, muelles, disparador, armazón y superficies sometidas a tolerancias repetibles. La fabricación en serie reduce su coste frente a plataformas largas especializadas, pero tratamiento térmico, mecanizado y control de seguridad la mantienen como una inversión privativa. Cargador y munición se tasan aparte.");
        p(m,"Subfusil Automático V881",4800,
                "Plataforma automática de 3,85 kg con cajón de mecanismos, cierre, conjunto de disparo, cañón, sistema de alimentación y piezas capaces de soportar ciclos repetidos a alta cadencia. Aunque admite fabricación seriada, el número de componentes móviles, tratamientos y pruebas funcionales supera al de una pistola. No incluye cargador, cartuchos ni correa.");
        p(m,"Fusil de Repetición V881",4200,
                "Fusil de 4,05 kg con cañón largo mecanizado, acción de repetición robusta, cierre resistente, disparador y cama estructural que deben conservar alineación a lo largo de una plataforma de 1,10 m. Su coste procede de precisión del cañón, acero tratado, ajuste de cierre y ensamblaje; alcance y letalidad no se convierten en precio. Clip, munición y óptica quedan separados.");
        p(m,"Cañón Antimaterial V881",26000,
                "Plataforma de gran calibre que debe contener presiones y reacciones muy superiores a las de un fusil personal. Cañón y recámara de sección elevada, cierre, bastidor, absorción de retroceso, mecanizado de grandes piezas y ensayos no destructivos convierten cada unidad en producción industrial especializada. El cassette de 20 mm, correa, bípode y óptica se valoran independientemente.");
        p(m,"Cañón de Racimo V881",22000,
                "Lanzador especializado para cohetes individuales de 85 mm: tubo y cierre deben soportar gases de lanzamiento, calor y manipulación de una munición de cuatro kilogramos, incorporando además selección de temporización y elementos de puntería/interfaz. La baja escala y las pruebas de seguridad dominan el coste. Cada cohete y cada accesorio se compran por separado.");
        p(m,"Lanza-Arcos Electrodinámico V881",15000,
                "Arma electrodinámica de 2,62 kg construida alrededor de tres bobinas, banco de condensadores de 1.650 J, conductores, aislamiento dieléctrico, electrodos, control térmico y manivela de carga. El coste refleja componentes eléctricos especializados, devanado, encapsulado, calibración y seguridad frente a descarga. La Batería Portátil Electromagnética V881 y la correa no están incluidas.");
        p(m,"Rociador de Cal Viva V881",6500,
                "Plataforma presurizada para dosificar y proyectar un agente químico desde cartuchos de tres litros. Depósito/interfaz, válvulas, mangueras o conductos resistentes, sellos, boquilla, mecanismos de control y materiales compatibles con un agente cáustico requieren fabricación y pruebas de estanqueidad especializadas. Estuche y cartuchos de Cal Viva se tasan fuera del arma.");
        coverageFirearms(m);
        return Map.copyOf(m);
    }

    private static void coverageThrowing(Map<String,EconomicValuation> m){
        Set<String> canonical=new LinkedHashSet<>();
        ThrowingWeaponCatalog.all().forEach(w->canonical.add(w.name()));
        if(canonical.size()!=4 || !m.keySet().equals(canonical))
            throw new IllegalStateException(" debe tasar exactamente las cuatro arrojadizas canónicas.");
    }
    private static void coverageRanged(Map<String,EconomicValuation> m){
        Set<String> canonical=new LinkedHashSet<>();
        RangedWeaponCatalog.all().forEach(w->canonical.add(w.name()));
        if(canonical.size()!=3 || !m.keySet().equals(canonical))
            throw new IllegalStateException(" debe tasar exactamente las tres armas a distancia canónicas.");
    }
    private static void coverageFirearms(Map<String,EconomicValuation> m){
        Set<String> canonical=new LinkedHashSet<>();
        FirearmCatalog.all().forEach(w->canonical.add(w.name()));
        if(canonical.size()!=9 || !m.keySet().equals(canonical))
            throw new IllegalStateException(" debe tasar exactamente las nueve firearms canónicas.");
    }
    private static Map<String,EconomicValuation> merge(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();
        for(Map<String,EconomicValuation> group:List.of(THROWING,RANGED,FIREARMS))
            for(var e:group.entrySet())
                if(m.put(e.getKey(),e.getValue())!=null) throw new IllegalStateException("Arma duplicada: "+e.getKey());
        if(m.size()!=16) throw new IllegalStateException(" debe contener 16 armas.");
        return Map.copyOf(m);
    }
    private static void p(Map<String,EconomicValuation> m,String n,long v,String r){
        m.put(n,EconomicValuation.priced(n,EconomicGoodType.PRIVATE_USE,v,r));
    }
}
