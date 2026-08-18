package domain.inventory.catalog;

import domain.inventory.InventoryFootprint;
import domain.inventory.logistics.InventoryPhysicalDimensions;
import domain.inventory.logistics.InventoryVolumeProjectionPolicy;

import java.util.*;

/**
 * : fuente dimensional canónica para objetos físicos registrados.
 * Las dimensiones se expresan en la envolvente X/Y/Z real; la UI 2D se deriva exclusivamente
 * mediante InventoryVolumeProjectionPolicy.
 */
public final class PhysicalObjectDimensionsCatalog {
    private static final Map<String,InventoryPhysicalDimensions> EXPLICIT=build();
    private PhysicalObjectDimensionsCatalog(){}

    public static InventoryPhysicalDimensions dimensionsFor(String name, InventoryFootprint fallbackFootprint){
        Objects.requireNonNull(name); Objects.requireNonNull(fallbackFootprint);
        InventoryPhysicalDimensions explicit=EXPLICIT.get(name);
        if(explicit!=null) return explicit;
        // los tipos todavía sin geometría singular explícita conservan su extensión XY auditada
        // con una profundidad física de un slot; ya no almacenan un footprint como autoridad.
        int x=Math.max(1,fallbackFootprint.verticalSlots());
        int y=Math.max(1,fallbackFootprint.horizontalSlots());
        return new InventoryPhysicalDimensions(x,y,1);
    }

    public static InventoryFootprint footprintFor(String name, InventoryFootprint fallbackFootprint){
        return InventoryVolumeProjectionPolicy.footprint(dimensionsFor(name,fallbackFootprint));
    }

    public static boolean hasExplicit(String name){ return EXPLICIT.containsKey(name); }
    public static InventoryFootprint auditedFootprintForName(String name){
        InventoryPhysicalDimensions d=EXPLICIT.get(Objects.requireNonNull(name));
        if(d==null) throw new IllegalArgumentException(" exige geometría XYZ explícita para objetos de loadout: "+name);
        return InventoryVolumeProjectionPolicy.footprint(d);
    }
    public static int explicitCount(){ return EXPLICIT.size(); }

    private static InventoryPhysicalDimensions m(double x,double y,double z){
        return InventoryPhysicalDimensions.fromMetricDimensions(x,y,z);
    }

    private static Map<String,InventoryPhysicalDimensions> build(){
        LinkedHashMap<String,InventoryPhysicalDimensions> m=new LinkedHashMap<>();

        // Materiales canónicos: dimensión de UNA unidad física inventariable.
        m.put("Tela",m(.30,.20,.05));
        m.put("Cuero endurecido",m(.35,.25,.08));
        m.put("Madera",m(.45,.12,.06));
        m.put("Bronce",m(.25,.10,.07));
        m.put("Acero de placas",m(.30,.12,.08));
        m.put("Madera de ébano",m(.45,.12,.06));
        m.put("Compuesto Electromecánico",m(.48,.32,.20));
        m.put("Papel",m(.30,.21,.05));
        m.put("Vidrio laminado",m(.35,.25,.04));
        m.put("Tejido mineral multicapa",m(.35,.25,.08));
        m.put("Caucho",m(.35,.25,.08));
        m.put("Caucho vulcanizado",m(.35,.25,.08));
        m.put("Tela dieléctrica",m(.30,.20,.05));
        m.put("Madera mineralizada",m(.45,.12,.06));
        m.put("Ébano mineralizado",m(.45,.12,.06));
        m.put("Placas de wolframio de 2,5 mm",m(.30,.20,.04));
        m.put("Wolframio",m(.25,.10,.08));

        // Alimentación / terapéuticos / iluminación / instrumentos.
        m.put("Pan",m(.24,.11,.09));
        m.put("Cecina",m(.18,.08,.03));
        m.put("Frutos secos",m(.14,.10,.05));
        m.put("Bizcocho",m(.16,.12,.07));
        m.put("Fruta",m(.09,.09,.09));
        m.put("Uva deshidratada",m(.12,.08,.04));
        m.put("Inyección estimulante",m(.16,.03,.03));
        m.put("Emplasto de milenrama",m(.12,.09,.025));
        m.put("Apósito de musgo de turbera",m(.14,.10,.035));
        m.put("Corteza de sauce",m(.12,.03,.015));
        m.put("Esencia de lucidez",m(.09,.025,.025));
        m.put("Frasco de I-RND",m(.10,.035,.035));
        m.put("Petaca de hidromiel",m(.14,.09,.025));
        m.put("Odre",m(.28,.20,.09));
        m.put("MAGNETLAMPE",m(.14,.09,.05));
        m.put("KNIJPKAT",m(.11,.07,.04));
        m.put("Astrolabio",m(.16,.16,.035));
        m.put("Monocular de Reconocimiento V881",m(.18,.055,.055));
        m.put("Cámara fotográfica V881",m(.18,.13,.10));
        m.put("Contenedor toxicológico Stas-Otto V881",m(.38,.26,.18));
        m.put("Aparato de Marsh V881",m(.34,.22,.16));
        m.put("Tokkosho V881",m(.18,.035,.035));
        m.put("Espejo heliográfico V881",m(.18,.14,.04));
        m.put("Diapasón resonante V881",m(.20,.05,.025));
        m.put("Sismoscopio V881",m(.18,.14,.08));
        m.put("Nocturlabio V881",m(.16,.16,.04));

        // Taller / recursos / combustible.
        m.put("Maletín profesional de Alicia e Iván",m(.48,.32,.20));
        m.put("Caja del Artesano",m(.30,.20,.15));
        m.put("Caja de Herramientas",m(.40,.25,.18));
        m.put("Tarro de Resina",m(.11,.09,.09));
        m.put("Botella de Líquido Refrigerante",m(.32,.12,.12));
        m.put("Bidón de Etanol",m(.23,.14,.09));
        m.put("Bidón de Queroseno Ligero",m(.23,.14,.09));
        m.put("Piedra de afilar",m(.18,.05,.03));
        m.put("Piedra de Mercurio",m(.18,.08,.06));
        m.put("Amadou",m(.10,.07,.025));
        m.put("Pedernal",m(.08,.05,.035));
        m.put("Patata cruda",m(.10,.07,.06));
        m.put("Conversor de combustible improvisado",m(.48,.32,.28));
        m.put("Batería Portátil Electromagnética V881",m(.14,.07,.035));
        m.put("Cargador portátil de Batería Electromagnética V881",m(.16,.09,.05));
        m.put("Guijarro",m(.05,.04,.035));

        // Accesorios desmontables de arma de fuego .
        m.put("Correa de Arma V881",m(.45,.05,.03));
        m.put("Bípode de Arma V881",m(.28,.08,.055));
        m.put("Mirilla Fiedler V881",m(.18,.05,.05));
        m.put("Mirilla Zeiss V881",m(.24,.055,.055));
        m.put("Mirilla Winchester A5 V881",m(.32,.055,.055));

        // Munición/proyectiles con geometría conocida.
        m.put("Cartucho .46 de plomo",m(.22,.045,.045));
        m.put("Cargador .45 de Pistola V881",m(.12,.04,.025));
        m.put("Cargador de 9 mm V881",m(.19,.045,.03));
        m.put("Cartucho completo 7,92×57 mm V881",m(.14,.07,.025));
        m.put("Cargador bifilar .46 V881",m(.18,.06,.035));
        m.put("Cartucho de 4 proyectiles de 20 mm V881",m(.20,.10,.08));
        m.put("Cohete de Racimo V881 de 85 mm",m(.65,.085,.085));
        m.put("Cohete individual de Racimo V881",m(.65,.085,.085));
        m.put("Estuche de Cartuchos de Cal Viva V881",m(.28,.16,.08));
        m.put("Flecha perforante",m(.75,.025,.025));
        m.put("Flecha de Púas",m(.75,.035,.035));
        m.put("Flecha de Hoja",m(.75,.045,.025));
        m.put("Flecha de Yesca",m(.75,.04,.04));

        // Arrojadizas.
        m.put("Cápsula de Gas Amonio V881",m(.10,.055,.055));
        m.put("Granada Incendiaria de Terracota V881",m(.12,.08,.08));
        m.put("Granada de Huevo con Fósforo y Azufre V881",m(.065,.045,.045));
        m.put("Cuchillo Arrojadizo V881",m(.20,.025,.012));

        m.put("CUADERNO DEL DIBUJANTE",m(.21,.15,.025));
        m.put("FAROLILLO LUNAR",m(.12,.07,.05));
        m.put("FAROLILLO PORTÁTIL",m(.15,.08,.06));
        m.put("GUARDAPELO DE KIARA",m(.04,.03,.015));
        m.put("PULSERA DE KENAN",m(.07,.07,.012));
        m.put("CUADERNO DE KIARA",m(.21,.15,.025));
        m.put("COLA DE RATA",m(.18,.025,.02));
        m.put("PLUMA DE CUERVO",m(.20,.04,.01));
        m.put("PEZUÑA DE CERDO",m(.09,.07,.055));
        m.put("CERDA DE CABALLO",m(.25,.015,.015));
        m.put("CAPARAZÓN DE ARMADILLO",m(.18,.12,.07));
        m.put("CORNAMENTA DE CIERVO",m(.42,.28,.12));
        m.put("OREJA DE TORO",m(.18,.12,.03));
        m.put("PIEL DE SERPIENTE",m(.35,.10,.025));
        m.put("COLMILLO DE JABALÍ",m(.16,.035,.035));
        m.put("OJO DE LINCE",m(.035,.035,.03));
        m.put("GARRAS DE ÁGUILA",m(.12,.08,.035));
        m.put("CRÁNEO DE LOBO",m(.25,.16,.15));
        m.put("CRIN DE LEÓN",m(.25,.15,.08));
        m.put("ZARPA DE OSO",m(.24,.18,.10));
        m.put("CUERNO DE RINOCERONTE",m(.45,.14,.14));
        m.put("DADOS TRUCADOS",m(.04,.04,.025));
        m.put("MUÑECA ARTESANAL",m(.22,.08,.055));
        m.put("ESPEJO DE BOLSILLO",m(.08,.06,.015));
        m.put("PEINE CEREMONIAL",m(.14,.04,.015));
        m.put("AMULETO DE COBRE",m(.05,.04,.01));
        m.put("ANILLO DE PROMESA",m(.025,.025,.008));
        m.put("RELICARIO VACÍO",m(.06,.04,.02));
        m.put("COLGANTE GRABADO",m(.05,.03,.01));
        m.put("ASTILLA CON LA QUE CLAVARON A UN MAESTRO",m(.09,.015,.012));
        m.put("CENIZAS DE UN DEVOTO QUE SE ARROJÓ A LA HOGUERA",m(.07,.05,.03));
        m.put("CERA DE UN INTELECTUAL",m(.07,.05,.025));

        // Expansores como objetos desequipados.
        m.put("Pernera Modular de Camino V881",m(.30,.20,.10));
        m.put("Bandolera de Servicio V881",m(.40,.20,.20));
        m.put("Mochila Dorsal de Expedición V881",m(.50,.30,.30));
        m.put("Sistema de Transporte Dorsal del Rotor V881",m(.90,.20,.10));
        m.put("Alforjas de Monta",m(.40,.30,.20));
        m.put("Alforjas de Carrera",m(.30,.20,.20));
        m.put("Alforjas de Carga",m(.60,.30,.30));
        m.put("Bolsas de Portaequipajes Militar V881",m(.40,.20,.20));
        m.put("Maletas Laterales Cardán V881",m(.60,.30,.20));
        m.put("Carcaj para flechas",m(.80,.08,.08));

        return Map.copyOf(m);
    }
}
