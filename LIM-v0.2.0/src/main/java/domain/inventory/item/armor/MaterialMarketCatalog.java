package domain.inventory.item.armor;

import domain.social.Profession;
import domain.economy.EconomicGoodType;
import java.util.*;

import static domain.social.Profession.*;

/**
 * Referencias de mercado de materiales en la unidad inventariable definida por MaterialCatalog.
 * No implementa transacciones ni cambio monetario: SEV sigue siendo la autoridad económica.
 */
public final class MaterialMarketCatalog {
    private static final EnumMap<ArmorMaterial,MaterialMarketProfile> DATA=build();
    private MaterialMarketCatalog(){}

    public static MaterialMarketProfile profile(ArmorMaterial material){
        MaterialMarketProfile p=DATA.get(Objects.requireNonNull(material));
        if(p==null) throw new IllegalArgumentException("Material sin perfil de mercado: "+material);
        return p;
    }

    public static Map<ArmorMaterial,MaterialMarketProfile> all(){ return Map.copyOf(DATA); }

    private static EnumMap<ArmorMaterial,MaterialMarketProfile> build(){
        EnumMap<ArmorMaterial,MaterialMarketProfile> m=new EnumMap<>(ArmorMaterial.class);
        put(m,ArmorMaterial.CLOTH,18,EconomicGoodType.SOCIAL_INTEREST,
                "Tejido ordinario de amplia producción. Su precio procede sobre todo del hilado, tejido, teñido y transporte; la abundancia mantiene bajo el coste incluso cuando se exige uniformidad suficiente para prendas técnicas.",
                DRESSMAKER,MERCHANT,COURTESAN,SOLDIER,TEACHER,DAY_LABORER);
        put(m,ArmorMaterial.HARDENED_LEATHER,48,EconomicGoodType.SOCIAL_INTEREST,
                "El cuero endurecido incorpora curtido, selección de espesores, calor, prensado y acabado. La materia prima es común; lo que se paga es que una piel flexible llegue al taller con rigidez y estabilidad repetibles.",
                TANNER,BLACKSMITH,MERCENARY,HUNTER,MERCHANT);
        put(m,ArmorMaterial.WOOD,22,EconomicGoodType.SOCIAL_INTEREST,
                "La madera estructural es barata mientras se acepta material común. Esta unidad ya presupone veta seleccionada, secado y corte aprovechable para astas, mecanismos y estructuras, de modo que vale más que simple leña sin abandonar una economía cotidiana.",
                CARPENTER,STONEMASON,MERCENARY,MERCHANT,HUNTER);
        put(m,ArmorMaterial.BRONZE,180,EconomicGoodType.SOCIAL_INTEREST,
                "Cobre, estaño, combustible de fundición y mano de obra metalúrgica concentran el coste. Su facilidad de colada y resistencia a corrosión sostienen una demanda constante en mecanismos, herramientas y protecciones.",
                BLACKSMITH,MERCHANT,SOLDIER,MERCENARY,SAILOR);
        put(m,ArmorMaterial.STEEL,260,EconomicGoodType.SOCIAL_INTEREST,
                "El acero de placas exige hornos, control térmico, laminado, temple y rectificación. El Reino puede producirlo en volumen, pero una unidad apta para protección o mecanismos V881 ya incorpora selección metalúrgica y trabajo industrial.",
                BLACKSMITH,SOLDIER,MERCENARY,EBONY_WARRIOR,MERCHANT);
        put(m,ArmorMaterial.EBONY_WOOD,240,EconomicGoodType.PRIVATE_USE,
                "El ébano superviviente de la Primera Marcha Exaltada es escaso, pero su precio histórico no se disparó. La desaparición simultánea de la antigua casta que lo consumía contrajo también la demanda especializada; buena parte de lo conservado circula por inventarios viejos, depósitos institucionales y piezas heredadas, no por un mercado especulativo de lujo.",
                EBONY_WARRIOR,BLACKSMITH,MERCHANT,NOBLE);
        put(m,ArmorMaterial.ELECTROMECHANICAL_COMPOSITE,2400,EconomicGoodType.PRIVATE_USE,
                "No se compra como una aleación homogénea: se paga un módulo funcional ya ensamblado, probado y calibrado. El valor concentra múltiples materiales, precisión instrumental y horas de integración. Sólo el Maletín profesional de Alicia e Iván permite reproducir el estándar.",
                BLACKSMITH,MERCENARY,TEACHER,MERCHANT,EBONY_WARRIOR);
        put(m,ArmorMaterial.PAPER,5,EconomicGoodType.SOCIAL_INTEREST,
                "Pasta, agua, prensado y secado permiten una producción muy barata. El precio de esta unidad responde al gramaje y uniformidad suficientes para uso técnico, no al valor documental de lo que después pueda imprimirse sobre ella.",
                TEACHER,JURIST,MERCHANT,DRESSMAKER);
        put(m,ArmorMaterial.LAMINATED_GLASS,95,EconomicGoodType.SOCIAL_INTEREST,
                "La lámina intermedia, el control de espesores y el ensamblaje sin defectos encarecen el vidrio frente a una placa ordinaria. Óptica, visores y protección ocular mantienen una demanda técnica estable.",
                BLACKSMITH,MERCHANT,TEACHER,SOLDIER);
        put(m,ArmorMaterial.MINERAL_MULTILAYER_FABRIC,160,EconomicGoodType.SOCIAL_INTEREST,
                "Fibras refractarias, hilo resistente al calor y una arquitectura multicapa precisa elevan el coste por encima de la tela ordinaria. Se compra por su respuesta térmica reproducible, no por apariencia.",
                BLACKSMITH,MERCENARY,SOLDIER,MERCHANT);
        put(m,ArmorMaterial.RUBBER,55,EconomicGoodType.SOCIAL_INTEREST,
                "El caucho flexible requiere purificación, conformado y conservación, pero todavía es una materia industrial de uso amplio en juntas, sellos y superficies continuas.",
                BLACKSMITH,CARPENTER,MERCHANT,MERCENARY);
        put(m,ArmorMaterial.VULCANIZED_RUBBER,85,EconomicGoodType.SOCIAL_INTEREST,
                "La vulcanización añade azufre, calor, tiempo y control de proceso al caucho ordinario. El incremento de precio compra estabilidad mecánica y dieléctrica, especialmente valiosas en equipos técnicos.",
                BLACKSMITH,MERCENARY,MERCHANT,TEACHER);
        put(m,ArmorMaterial.DIELECTRIC_CLOTH,120,EconomicGoodType.SOCIAL_INTEREST,
                "La tela dieléctrica necesita fibras y tratamientos de elevada resistividad, limpieza y control de humedad. La demanda es menor que la textil ordinaria, pero mucho más especializada.",
                BLACKSMITH,MERCENARY,TEACHER,MERCHANT);
        put(m,ArmorMaterial.MINERALIZED_WOOD,70,EconomicGoodType.SOCIAL_INTEREST,
                "A la madera seleccionada se añaden impregnación a presión, silicatos, boratos y curado. El material de partida sigue siendo barato; la infraestructura de estabilización explica la mayor parte del valor.",
                CARPENTER,BLACKSMITH,MERCENARY,MERCHANT);
        put(m,ArmorMaterial.MINERALIZED_EBONY,320,EconomicGoodType.PRIVATE_USE,
                "El precio superior al ébano natural no es una prima de rareza, sino de proceso: impregnación mineral, curado y rechazo de piezas que no toleran la estabilización. La escasez histórica sigue sin convertirse por sí sola en especulación.",
                EBONY_WARRIOR,BLACKSMITH,MERCHANT);
        put(m,ArmorMaterial.TUNGSTEN_PLATES_2_5_MM,450,EconomicGoodType.PRIVATE_USE,
                "La densidad del wolframio hace cara la materia prima y su mecanizado exige herramientas, refrigeración y tolerancias estrictas. En placas finas se paga especialmente la uniformidad de espesor.",
                BLACKSMITH,MERCENARY,SOLDIER,EBONY_WARRIOR,MERCHANT);
        put(m,ArmorMaterial.TUNGSTEN,520,EconomicGoodType.PRIVATE_USE,
                "El wolframio concentra mucho valor en poco volumen por su densidad, temperatura de trabajo y dificultad de conformado. La mayor parte de compradores no lo quiere puro, sino como reserva para aleaciones y componentes extremos.",
                BLACKSMITH,MERCENARY,MERCHANT,TEACHER);
        if(m.size()!=ArmorMaterial.values().length) throw new IllegalStateException("Todo material canónico debe tener mercado .");
        return m;
    }

    private static void put(EnumMap<ArmorMaterial,MaterialMarketProfile> m,ArmorMaterial material,int price,
                            EconomicGoodType economicGoodType,String narrative,Profession... professions){
        m.put(material,new MaterialMarketProfile(price,economicGoodType,narrative,EnumSet.copyOf(List.of(professions))));
    }
}
