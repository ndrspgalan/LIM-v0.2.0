package domain.economy;

import domain.inventory.catalog.PhysicalObjectCatalog;
import java.util.*;

/**
 *  — autoridad económica de los misceláneos.
 *
 * El Sueldo (1.000 V) es el salario mensual de referencia, no una unidad de balance.
 * Los precios se justifican por materia, trabajo, infraestructura, logística, conservación,
 * durabilidad y regulación. La potencia mecánica del objeto nunca constituye por sí sola un coste.
 */
public final class MiscellaneousEconomicCatalog {
    private static final Map<String,EconomicValuation> DATA=build();
    private MiscellaneousEconomicCatalog(){}

    public static EconomicValuation valuation(String name) {
        EconomicValuation v=DATA.get(Objects.requireNonNull(name));
        if(v==null) throw new IllegalArgumentException("Misceláneo sin autoridad económica : "+name);
        return v;
    }
    public static Map<String,EconomicValuation> all(){ return DATA; }

    private static Map<String,EconomicValuation> build(){
        LinkedHashMap<String,EconomicValuation> m=new LinkedHashMap<>();

        // Primera necesidad: alimentación, agua, sanidad elemental e ignición doméstica.
        p(m,"Pan",EconomicGoodType.FIRST_NECESSITY,3,"Hogaza ordinaria: cereal molido, agua, sal, fermentación y una cocción compartida de panadería. La materia prima y la producción por hornadas mantienen el precio en pocas Valeritas.");
        p(m,"Cecina",EconomicGoodType.FIRST_NECESSITY,8,"La carne aporta más coste que el cereal y debe salarse, secarse y perder masa útil antes de venderse; a cambio soporta transporte y almacenamiento prolongados.");
        p(m,"Frutos secos",EconomicGoodType.FIRST_NECESSITY,6,"Tres raciones pequeñas de fruto descascarado y seco. Cultivo, selección y retirada de cáscara pesan más que el envase simple, pero su conservación reduce merma comercial.");
        p(m,"Bizcocho",EconomicGoodType.FIRST_NECESSITY,7,"Harina, huevo y endulzante requieren más ingredientes y preparación que una hogaza común; dos porciones y una manufactura todavía doméstica impiden que alcance precio suntuario.");
        p(m,"Fruta",EconomicGoodType.FIRST_NECESSITY,2,"Pieza agrícola fresca de temporada. Su manipulación es mínima y la elevada perecibilidad obliga a rotación rápida, de modo que en el mercado ordinario se mantiene en Valeritas.");
        p(m,"Uva deshidratada",EconomicGoodType.FIRST_NECESSITY,5,"La uva pierde agua y peso durante el secado y requiere selección y tiempo de exposición controlada; esa concentración eleva su coste frente a fruta fresca sin volverla rara.");
        p(m,"Odre",EconomicGoodType.FIRST_NECESSITY,22,"Recipiente reutilizable de cuero curtido, cosido y sellado para transportar agua. El precio corresponde principalmente al continente durable; el agua ordinaria representa una fracción mínima.");
        p(m,"Emplasto de milenrama",EconomicGoodType.FIRST_NECESSITY,6,"Milenrama común recolectada y machacada sobre tela limpia. El coste procede más de preparación higiénica, vendaje y distribución que de una planta escasa.");
        p(m,"Apósito de musgo de turbera",EconomicGoodType.FIRST_NECESSITY,10,"Musgo absorbente seleccionado, limpiado, comprimido y fijado a una venda. Requiere más acondicionamiento sanitario que una hierba seca, aunque sus materias siguen siendo accesibles.");
        p(m,"Corteza de sauce",EconomicGoodType.FIRST_NECESSITY,3,"Corteza recolectada, secada y cortada en dosis masticable. Materia vegetal abundante y proceso sencillo justifican un coste de primera necesidad muy bajo.");
        p(m,"Amadou",EconomicGoodType.FIRST_NECESSITY,4,"Hongo yesquero secado y acondicionado para diez encendidos. La materia es barata; el precio remunera recolección, secado y preparación fiable frente a usar biomasa húmeda.");
        p(m,"Pedernal",EconomicGoodType.FIRST_NECESSITY,5,"Sílex seleccionado y preparado con aristas útiles para ignición. Es geológicamente común y reutilizable; selección y tallado ligero explican casi todo su valor.");
        p(m,"Patata cruda",EconomicGoodType.FIRST_NECESSITY,1,"Tubérculo agrícola sin cocción ni preparación. Es alimento potencial y materia prima de fermentación, pero la unidad inventariable se vende cruda y con trabajo poscosecha mínimo.");

        // Interés social: herramientas, movilidad, iluminación, mantenimiento y consumibles profesionales accesibles.
        p(m,"Petaca de hidromiel",EconomicGoodType.SOCIAL_INTEREST,18,"Incluye una petaca metálica persistente y 240 mL de hidromiel. La mayor parte del valor queda en el recipiente reutilizable; miel, fermentación y llenado explican el contenido.");
        p(m,"Inyección estimulante",EconomicGoodType.SOCIAL_INTEREST,65,"Autoinyector monodosis de campaña: principio estimulante dosificado, esterilidad, aguja protegida y mecanismo de inyección elevan el coste muy por encima de una preparación vegetal.");
        p(m,"MAGNETLAMPE",EconomicGoodType.SOCIAL_INTEREST,92,"Carcasa, resorte, volante, generador magnético, cordón y montaje mecánico preciso forman una lámpara durable sin consumibles eléctricos. El precio remunera metal y ajuste de piezas.");
        p(m,"KNIJPKAT",EconomicGoodType.SOCIAL_INTEREST,76,"Dinamo compacta, tren de engranajes y palanca estampada requieren manufactura mecánica fina, aunque su construcción seriable y menor masa la mantienen por debajo de la MAGNETLAMPE.");
        p(m,"Caja del Artesano",EconomicGoodType.SOCIAL_INTEREST,190,"Tres kilogramos de agujas, leznas, tenazas, raspadores, remaches y útiles reutilizables. Acumula muchas piezas de acero trabajado y una caja organizada, por lo que constituye inversión profesional.");
        p(m,"Caja de Herramientas",EconomicGoodType.SOCIAL_INTEREST,340,"Seis kilogramos de martillos, llaves, extractores, terrajas, calibres y útiles mecánicos. Mayor masa metálica, diversidad de tratamientos y tolerancias explican que supere ampliamente a la caja artesanal.");
        p(m,"Tarro de Resina",EconomicGoodType.SOCIAL_INTEREST,38,"Tarro robusto reutilizable con tres cargas de resina seleccionada y acondicionada. El recipiente y la preparación comercial justifican que comprarlo sea sensiblemente más caro que recolectar resina.");
        p(m,"Botella de Líquido Refrigerante",EconomicGoodType.SOCIAL_INTEREST,72,"Botella técnica reutilizable con hasta dos litros de mezcla estabilizada de agua destilada y etanol. Destilación, medición y preparación con instrumental especializado dominan el precio.");
        p(m,"Piedra de afilar",EconomicGoodType.SOCIAL_INTEREST,24,"Abrasivo mineral seleccionado, cortado y rectificado para cien usos. Su vida útil hace muy bajo el coste por mantenimiento aunque la pieza requiera geometría y grano consistentes.");
        p(m,"Bidón de Etanol",EconomicGoodType.SOCIAL_INTEREST,44,"Bidón reforzado más un litro de etanol combustible. El continente reutilizable, fermentación de materia amilácea y destilación concentran el coste; puede evitarse la compra fabricándolo con medios propios.");
        p(m,"Bidón de Queroseno Ligero",EconomicGoodType.SOCIAL_INTEREST,58,"Bidón reforzado más un litro de queroseno ligero refinado. La cadena de refino y distribución es menos accesible al individuo que la fermentación de etanol y explica la prima comercial.");
        p(m,"Conversor de combustible improvisado",EconomicGoodType.SOCIAL_INTEREST,125,"Conjunto portátil de trituración, fermentación cerrada, calentamiento y condensación montado con piezas comunes. 'Improvisado' describe que puede ensamblarse fuera de una planta, no ausencia de metal, recipientes ni trabajo.");

        // Uso privativo: instrumental o sustancias cuyo acceso/uso no pertenece al consumo ordinario.
        p(m,"Esencia de lucidez",EconomicGoodType.PRIVATE_USE,95,"Ampolla inhalante de principio activo concentrado y volátil. Preparación, pureza, dosificación y sellado de una dosis estable requieren laboratorio y control superiores a los estimulantes ordinarios.");
        p(m,"Frasco de I-RND",EconomicGoodType.PRIVATE_USE,480,"Psicotrópico/nootrópico de alteración cognitiva intensa, comparable por función narrativa a sustancias capaces de modificar radicalmente percepción y procesamiento. Síntesis, purificación, dosificación, estabilización fotosensible y control de acceso dominan el coste; su efecto de juego no se usa como multiplicador.");
        p(m,"Monocular de Reconocimiento V881",EconomicGoodType.PRIVATE_USE,720,"Óptica de precisión, cuerpo mecanizado y telemetría V881 requieren vidrio seleccionado, alineación, calibración e instrumentación especializada. Es equipamiento profesional de reconocimiento, no consumo doméstico.");
        p(m,"Piedra de Mercurio",EconomicGoodType.PRIVATE_USE,165,"Soporte de fricción preparado para transferir mercurio de forma utilizable. La manipulación segura, contención del metal tóxico y preparación controlada explican un precio muy superior al de un abrasivo ordinario.");
        p(m,"Batería Portátil Electromagnética V881",EconomicGoodType.PRIVATE_USE,900,
                "Módulo recargable de dos celdas 21700 dentro de una carcasa reforzada con contactos, aislamiento, protección frente a cortocircuito y geometría normalizada para Fusil Bifilar y Lanza-Arcos. El precio remunera almacenamiento electroquímico fiable, selección de celdas, encapsulado y control de seguridad; no se calcula a partir del daño de las armas que alimenta.");
        p(m,"Cargador portátil de Batería Electromagnética V881",EconomicGoodType.PRIVATE_USE,650,
                "Unidad portátil reforzada con regulación, protección térmica y eléctrica, alojamiento mecánico para una batería y electrónica capaz de completar una recarga controlada desde una fuente compatible. Es instrumental energético especializado y durable; la batería que se introduce en él se tasa por separado.");

        // instrumental forense introducido en  recibe la autoridad económica que faltaba.
        p(m,"Cámara fotográfica V881",EconomicGoodType.PRIVATE_USE,780,"Cámara profesional V881 con óptica de precisión, obturador, soporte fotosensible y carcasa de campo. Su precio remunera vidrio seleccionado, mecanizado, calibración y un proceso de fabricación especializado; es instrumental forense durable, no un consumible ni un valor derivado de su utilidad probatoria.");
        p(m,"Contenedor toxicológico Stas-Otto V881",EconomicGoodType.PRIVATE_USE,460,"Contenedor profesional para conservar y separar muestras toxicológicas sin contaminación cruzada. Vidrio y metal resistentes, cierres estancos, compartimentación, limpieza verificable y fabricación de baja escala explican su coste como instrumental de laboratorio de campo.");
        p(m,"Aparato de Marsh V881",EconomicGoodType.PRIVATE_USE,690,"Instrumental analítico V881 inspirado en el aparato de Marsh, construido para ensayos toxicológicos controlados con conducciones, recipientes resistentes, sellos y piezas calibradas. El precio procede de la precisión de fabricación y de su uso profesional especializado, no del resultado de una investigación concreta.");

        m.put("Maletín profesional de Alicia e Iván", EconomicValuation.ogcPending(
                "Maletín profesional de Alicia e Iván", EconomicGoodType.PRIVATE_USE,
                "Objeto único compuesto por instrumental mecánico, hidromecánico, eléctrico y de laboratorio de procedencia singular. La OGC mantiene su tasación pendiente: no existe precio minorista canónico ni puede venderse ordinariamente hasta que una tasación formal determine su valor. La ausencia de cifra es un estado económico explícito, no información incompleta."));

        // Cobertura:  sólo debe contener tipos físicos de la familia misc.
        for(String name:m.keySet()) {
            var d=PhysicalObjectCatalog.definitionForName(name);
            if(!d.family().equals("misc")) throw new IllegalStateException(" contiene un objeto ajeno a misc: "+name);
        }
        return Map.copyOf(m);
    }

    private static void p(Map<String,EconomicValuation> m,String name,EconomicGoodType type,long price,String rationale){
        if(m.put(name,EconomicValuation.priced(name,type,price,rationale))!=null)
            throw new IllegalStateException("Tasación duplicada: "+name);
    }
}
