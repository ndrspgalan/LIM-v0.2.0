package domain.economy;

import domain.inventory.catalog.PhysicalObjectCatalog;
import domain.inventory.item.accessory.AccessoryCatalog;
import domain.inventory.item.accessory.OccupationalNarrativeAccessoryCatalog;

import java.util.*;

/**
 *  — autoridad económica de los 32 abalorios canónicos.
 *
 * La capacidad mecánica no fija el precio. Trofeos Ferae se valoran por disponibilidad,
 * obtención, conservación y demanda social; supersticiones por material, trabajo y valor cultural;
 * reliquias de procedencia alegada por el mercado de credibilidad, no por poderes inexistentes.
 * Los objetos personales únicos no reciben un falso precio minorista.
 */
public final class AccessoryEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private AccessoryEconomicCatalog(){}

    public static EconomicValuation valuation(String name) {
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Abalorio sin autoridad económica : "+name);
        return v;
    }

    public static Map<String,EconomicValuation> all(){ return DATA; }

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        // Piezas narrativas singulares o técnicas.
        personal(m,"CUADERNO DEL DIBUJANTE",
                "Cuaderno personal utilizado por Kiara desde niña y convertido durante años en un registro gráfico irrepetible. Papel, encuadernación y tinta tienen un coste material pequeño, pero venderlo como mercancía fungible destruiría precisamente aquello que lo identifica: su procedencia biográfica. No posee precio minorista canónico mientras siga siendo ese objeto concreto.");
        p(m,"FAROLILLO LUNAR",EconomicGoodType.PRIVATE_USE,1250,
                "Farol autónomo de ingeniería refinada cuya carcasa, foco y alimentación estable producen iluminación continua sin combustible ordinario. La fabricación exige componentes V881, ajuste fino y una arquitectura capaz de mantener una referencia operacional en condiciones donde un farol normal no sirve. Su propiedad de Áncora Encarnada no se convierte en multiplicador de gameplay: el precio responde a manufactura extraordinaria y acceso muy restringido.");
        p(m,"Astrolabio",EconomicGoodType.PRIVATE_USE,680,
                "Instrumento V881 de orientación con anillos graduados y mecanismo de lectura espacial. La fabricación exige mecanizado fino, calibración y una adaptación capaz de interpretar referencias de la Memoria del Mundo; su función real como artefacto no se traduce en una bonificación abstracta ni en una prima sobrenatural. El precio corresponde a tecnología restringida, mantenimiento y precisión.");
        p(m,"Tokkosho V881",EconomicGoodType.PRIVATE_USE,2400,"Artefacto electroatmosférico de precisión, con aislamiento, captación y metalurgia conductora de acceso restringido.");
        p(m,"Espejo heliográfico V881",EconomicGoodType.PRIVATE_USE,980,"Óptica militar de precisión y montaje calibrado para señalización a larga distancia.");
        p(m,"Diapasón resonante V881",EconomicGoodType.PRIVATE_USE,1750,"Instrumentación resonante de laboratorio miniaturizada y calibrada individualmente.");
        p(m,"Sismoscopio V881",EconomicGoodType.PRIVATE_USE,1650,"Sensor mecánico portátil de alta sensibilidad y lectura amplificada para vibraciones estructurales.");
        p(m,"Nocturlabio V881",EconomicGoodType.PRIVATE_USE,2100,"Instrumento astronómico y cronológico V881 de fabricación restringida y calibración especializada.");
        p(m,"FAROLILLO PORTÁTIL",EconomicGoodType.SOCIAL_INTEREST,55,
                "Farol compacto de queroseno con depósito, mechero, carcasa metálica y pantalla protectora. Es manufactura reproducible y reparable con materiales comunes; el precio corresponde al conjunto durable y no incluye una prima sobrenatural por la tranquilidad nocturna que produce al portador.");
        personal(m,"GUARDAPELO DE KIARA",
                "Guardapelo concreto que contiene un mechón de Kiara y fue entregado como regalo personal. El metal y el mecanismo de cierre podrían tasarse por separado, pero el objeto canónico no es un guardapelo genérico: identidad, contenido y procedencia forman una unidad no fungible. Por ello carece de precio minorista ordinario.");
        personal(m,"PULSERA DE KENAN",
                "Pulsera concreta confeccionada por Kenan con lavanda y entregada como regalo. Sus materias primas poseen valor comercial mínimo, pero sustituirlas por otra lavanda produciría otro objeto, no esta pulsera.  evita monetizar el vínculo biográfico como si fuera una mercancía repetible y la declara fuera de venta ordinaria.");
        personal(m,"CUADERNO DE KIARA",
                "Cuaderno concreto, deteriorado, con páginas arrancadas y la historia de Kenan escrita por Kiara. Papel y encuadernación no explican el bien económico porque el contenido y la procedencia no son reproducibles mediante una compra de papelería. Se conserva como objeto personal no fungible sin precio minorista canónico.");

        // Trofeos Ferae: todos INTERÉS SOCIAL; no existe fórmula CARISMA -> precio.
        p(m,"COLA DE RATA",EconomicGoodType.SOCIAL_INTEREST,4,
                "Trofeo extremadamente accesible: una cola pequeña exige sólo limpieza, sal y humo para conservarse. La abundancia de rata mantiene el coste cerca de la preparación material y del pequeño trabajo del cazador.");
        p(m,"PLUMA DE CUERVO",EconomicGoodType.SOCIAL_INTEREST,5,
                "Una pluma negra grande es barata de obtener, pero debe seleccionarse intacta por brillo y resistencia y montarse con hilo encerado para que sobreviva como abalorio. El precio remunera selección y presentación, no su +CARISMA.");
        p(m,"PEZUÑA DE CERDO",EconomicGoodType.SOCIAL_INTEREST,8,
                "Pezuña de un ejemplar completo, limpiada y curada. El suministro porcino es común y el tratamiento sencillo, de modo que materia y conservación generan un precio modesto pese a funcionar como prueba de captura.");
        p(m,"CERDA DE CABALLO",EconomicGoodType.SOCIAL_INTEREST,6,
                "Mechón de cerdas gruesas seleccionado, limpiado y anudado. El caballo aporta una materia relativamente accesible que puede obtenerse sin una cadena de conservación compleja; preparación y presentación concentran casi todo el coste.");
        p(m,"CAPARAZÓN DE ARMADILLO",EconomicGoodType.SOCIAL_INTEREST,18,
                "Fragmento articulado que debe extraerse, limpiarse y estabilizarse sin deformar su geometría. La oferta es menor que la de animales domésticos y el acondicionamiento de una estructura compuesta eleva el precio sin convertirla en bien suntuario.");
        p(m,"CORNAMENTA DE CIERVO",EconomicGoodType.SOCIAL_INTEREST,28,
                "La pieza se selecciona por tamaño y simetría, ocupa volumen durante transporte y necesita lijado y tratamiento contra grietas. Caza estacional, selección y acondicionamiento explican una prima clara sobre los trofeos menores.");
        p(m,"OREJA DE TORO",EconomicGoodType.SOCIAL_INTEREST,22,
                "Tejido orgánico grueso de ejemplar adulto que debe curtirse y conservarse para no degradarse. La materia de origen es accesible donde existe ganadería, pero el trabajo de estabilización supera al de pezuñas, cerdas o plumas.");
        p(m,"PIEL DE SERPIENTE",EconomicGoodType.SOCIAL_INTEREST,30,
                "Se paga una sección continua que conserve el dibujo de las escamas. La extracción sin roturas, el curtido y la pérdida de piezas defectuosas explican más del precio que la masa material de la piel.");
        p(m,"COLMILLO DE JABALÍ",EconomicGoodType.SOCIAL_INTEREST,38,
                "Colmillo curvado completo que debe limpiarse, pulirse y estabilizarse para evitar fisuras, con refuerzo localizado de la punta. La obtención de un jabalí adulto añade una oferta menos regular que los trofeos domésticos.");
        p(m,"OJO DE LINCE",EconomicGoodType.SOCIAL_INTEREST,95,
                "El tejido ocular se degrada con rapidez y sólo conserva valor como abalorio si se estabiliza en resina y alcohol y se protege en un estuche metálico sellado. Escasez del ejemplar y conservación especializada dominan el coste.");
        p(m,"GARRAS DE ÁGUILA",EconomicGoodType.SOCIAL_INTEREST,110,
                "Conjunto delantero completo, no una garra suelta: debe curarse, endurecerse en aceite y humo y reforzarse con férulas. Conseguir un conjunto íntegro y conservarlo eleva notablemente el coste de oferta.");
        p(m,"CRÁNEO DE LOBO",EconomicGoodType.SOCIAL_INTEREST,140,
                "Cráneo completo con dentición intacta. Despiece, limpieza, blanqueado, secado y pulido requieren un tratamiento largo en el que una fractura puede inutilizar el trofeo; esa tasa de descarte justifica buena parte de su precio.");
        p(m,"CRIN DE LEÓN",EconomicGoodType.SOCIAL_INTEREST,220,
                "La oferta depende de un animal mucho menos accesible y de conservar un mechón espeso con volumen reconocible. Lavado, curado y trenzado reducen pérdidas posteriores; escasez de procedencia y preparación explican la prima.");
        p(m,"ZARPA DE OSO",EconomicGoodType.SOCIAL_INTEREST,260,
                "Zarpa frontal completa con piel, hueso y garras. Curtir tejido grueso, endurecer las garras y montar un soporte de cuero exige más trabajo y volumen de conservación que un trofeo óseo sencillo, además de una oferta peligrosa y limitada.");
        p(m,"CUERNO DE RINOCERONTE",EconomicGoodType.SOCIAL_INTEREST,420,
                "Sección grande y densa que debe estabilizarse contra fisuras y pérdida de masa. La escasa oferta de ejemplares, el volumen transportado y la dificultad de obtener una pieza íntegra sitúan el bien en el extremo superior del mercado Ferae, sin utilizar su +15 CARISMA como fórmula monetaria.");

        // Superstición ordinaria: el mercado puede pagar símbolos aunque no exista efecto mecánico.
        p(m,"DADOS TRUCADOS",EconomicGoodType.SOCIAL_INTEREST,18,
                "Par de dados cuyo peso se ha descentrado sin dejar una alteración evidente. Materia barata, pero fabricación minuciosa y riesgo comercial asociado a vender instrumental de fraude explican una prima sobre dados ordinarios.");
        p(m,"MUÑECA ARTESANAL",EconomicGoodType.SOCIAL_INTEREST,12,
                "Tela, relleno, hilo y varias operaciones manuales de corte, costura y acabado. La creencia de que acompaña a su dueño puede sostener demanda cultural, pero el precio se ancla en artesanía y no en un efecto sobrenatural inexistente.");
        p(m,"ESPEJO DE BOLSILLO",EconomicGoodType.SOCIAL_INTEREST,22,
                "Pequeña superficie reflectante pulida y protegida por un marco metálico. El trabajo de acabado y montaje domina el coste; la costumbre de mirarlo antes de viajar explica demanda, no una propiedad verificable.");
        p(m,"PEINE CEREMONIAL",EconomicGoodType.SOCIAL_INTEREST,14,
                "Madera fina o hueso seleccionado, cortado y tallado con dientes regulares y ornamentación. La función ceremonial permite una prima estética moderada, pero ninguna parte del precio presupone que produzca éxito social.");
        p(m,"AMULETO DE COBRE",EconomicGoodType.SOCIAL_INTEREST,8,
                "Disco pequeño de cobre estampado o grabado con símbolos populares. El metal es barato en esta masa y la mayor parte del valor procede del grabado, acabado y venta como objeto cultural de protección.");
        p(m,"ANILLO DE PROMESA",EconomicGoodType.SOCIAL_INTEREST,16,
                "Anillo sencillo conformado en metal común con inscripción interior. El valor está en trabajo fino, ajuste y significado ceremonial; la supuesta consecuencia de romper un juramento no interviene en la tasación.");
        p(m,"RELICARIO VACÍO",EconomicGoodType.SOCIAL_INTEREST,28,
                "Pequeña caja metálica articulada con cierre y alojamiento interior. Aunque no contiene reliquia, exige más operaciones de orfebrería que un colgante simple y conserva demanda como soporte ceremonial y objeto de buena fortuna.");
        p(m,"COLGANTE GRABADO",EconomicGoodType.SOCIAL_INTEREST,35,
                "Pieza metálica pequeña cuyo precio se concentra en un grabado elaborado y en el acabado superficial. Las historias de protección elevan su atractivo social, pero no se capitaliza como si poseyera protección real.");

        // Procedencia alegada: precio social construido, no magia ni autenticidad demostrada.
        p(m,"ASTILLA CON LA QUE CLAVARON A UN MAESTRO",EconomicGoodType.SOCIAL_INTEREST,75,
                "La madera en sí vale casi nada. El precio aparece porque vendedor y comprador atribuyen a una astilla indistinguible una procedencia histórica concreta, normalmente acompañada de envoltorio, relato o cadena informal de propietarios. La cifra remunera credibilidad social y demanda de reliquia; no certifica autenticidad ni efecto.");
        p(m,"CENIZAS DE UN DEVOTO QUE SE ARROJÓ A LA HOGUERA",EconomicGoodType.SOCIAL_INTEREST,60,
                "Las cenizas ordinarias carecen de rareza material y el recipiente sellado es barato. La prima comercial procede exclusivamente de la historia de sacrificio que acompaña al lote y de la confianza del comprador en una procedencia que no puede verificarse por medios ordinarios.");
        p(m,"CERA DE UN INTELECTUAL",EconomicGoodType.SOCIAL_INTEREST,45,
                "Un fragmento de cera endurecida es material banal. Su precio sólo supera el de una vela usada porque se atribuye a un Intelectual célebre y circula como reliquia de lucidez; el mercado paga relato, reputación del intermediario y deseo de posesión, no una capacidad cognitiva real.");

        // variantes biográficas ocupacionales. El precio sigue al objeto material reutilizado,
        // mientras la descripción pertenece a la vida concreta del portador y no añade poderes.
        for(var entry:OccupationalNarrativeAccessoryCatalog.allPriced().entrySet()) {
            var pa=entry.getValue();
            p(m,pa.item().name(),EconomicGoodType.SOCIAL_INTEREST,pa.priceValeritas(),
                    "Abalorio biográfico : se tasa por su forma material y trabajo ordinario; la historia en primera persona no se monetiza como poder ni autenticidad sobrenatural.");
        }

        // Cobertura exacta contra la autoridad de AccessoryCatalog y PhysicalObjectCatalog.
        Set<String> canonical=new LinkedHashSet<>();
        for(var a:AccessoryCatalog.all()) canonical.add(a.name());
        for(var a:domain.inventory.item.accessory.ArtifactAccessoryCatalog.all()) canonical.add(a.name());
        int expected=AccessoryCatalog.all().size();
        if(canonical.size()!=expected) throw new IllegalStateException(" espera "+expected+" abalorios canónicos tras poblar Mendigo/Jornalero.");
        if(!m.keySet().equals(canonical)) throw new IllegalStateException("La autoridad económica  no coincide con AccessoryCatalog.");
        for(String name:m.keySet()) {
            var d=PhysicalObjectCatalog.definitionForName(name);
            if(!d.family().equals("accessory")) throw new IllegalStateException(" contiene un objeto ajeno a accessory: "+name);
        }
        return Map.copyOf(m);
    }

    private static void p(Map<String,EconomicValuation> m,String name,EconomicGoodType type,long price,String rationale){
        if(m.put(name,EconomicValuation.priced(name,type,price,rationale))!=null)
            throw new IllegalStateException("Tasación duplicada: "+name);
    }

    private static void personal(Map<String,EconomicValuation> m,String name,String rationale){
        if(m.put(name,EconomicValuation.personalProvenance(name,EconomicGoodType.PRIVATE_USE,rationale))!=null)
            throw new IllegalStateException("Tasación duplicada: "+name);
    }
}
