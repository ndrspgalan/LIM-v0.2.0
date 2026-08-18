package domain.inventory.item.armor;

import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;

import java.util.List;

/** Catálogo inventariable de materiales en bruto. */
public final class MaterialCatalog {
    private MaterialCatalog() {}

    public static MaterialItem cloth(int quantity) {
        return item(ArmorMaterial.CLOTH, "Capas textiles", quantity, 20, 0.300, 2, 2,
                "Material textil diseñado principalmente para el aislamiento ambiental y la confección de prendas interiores y exteriores. Su reducido peso permite gran movilidad y facilita su combinación con cuero, metales y estructuras compuestas. Su capacidad defensiva directa es mínima, pero constituye la base textil de numerosos conjuntos y aporta protección climática cuando cubre las regiones corporales requeridas.",
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.WARMTH, "ABRIGO", "La capa textil protege al portador frente a la exposición térmica extrema.", "INMUNIDAD | Frío Escarchante"),
                        ItemProperty.alwaysActive(ItemPropertyId.MATERIAL_COMPATIBILITY, "COMPATIBILIDAD", "La tela puede incorporarse como capa base junto a cualquier otro material.", "COMPATIBLE | Todos los materiales")
                ));
    }

    public static MaterialItem hardenedLeather(int quantity) {
        return item(ArmorMaterial.HARDENED_LEATHER, "Capas de cuero tratado", quantity, 10, 1.200, 3, 2,
                "Cuero sometido a endurecimiento térmico, prensado y tratamiento superficial para incrementar su rigidez sin comprometer excesivamente la movilidad. Se emplea en armaduras ligeras y estructuras flexibles que necesitan soportar abrasiones, cortes y golpes moderados. Continúa siendo vulnerable frente a perforaciones concentradas y al deterioro localizado.", List.of());
    }

    public static MaterialItem wood(int quantity) {
        return item(ArmorMaterial.WOOD, "Láminas seleccionadas", quantity, 8, 1.500, 4, 1,
                "Material estructural de origen vegetal empleado en la fabricación de astas, escudos, arcos, ballestas y mecanismos menores de tensión controlada. Su calidad depende de la especie, el secado, la orientación de la veta y el tratamiento aplicado. Combinado con bronce, permite construir mecanismos híbridos extraordinarios como el Rifle Neumático de Repetición V881.",
                List.of(ItemProperty.alwaysActive(ItemPropertyId.FLAMMABLE, "INFLAMABLE", "La estructura orgánica transmite y conserva con facilidad el daño térmico.", "DAÑO RECIBIDO | Quemadura ×2")));
    }

    public static MaterialItem bronze(int quantity) {
        return item(ArmorMaterial.BRONZE, "Lingote", quantity, 10, 5.000, 2, 1,
                "Aleación metálica de cobre y estaño empleada en armaduras, herramientas, mecanismos de precisión y componentes sometidos a fricción. Presenta buena resistencia a la corrosión y una elevada facilidad de fundición. Aunque sus protecciones defensivas son inferiores a las del acero, su combinación con madera permite manufacturar mecanismos bélicos complejos y sistemas de armas a distancia avanzados.",
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.ANTI_CORROSIVE, "ANTI-CORROSIVO", "La aleación de cobre y estaño forma productos superficiales relativamente estables frente al agente corrosivo del rociador y reduce la transferencia de determinadas toxinas.", "VENENO RECIBIDO | ×0,75 · CORROSIVO | Inmunidad"),
                        ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_CONDUCTOR, "CONDUCTOR ELÉCTRICO", "La aleación transmite con facilidad las descargas eléctricas.", "DAÑO RECIBIDO | Electricidad ×2")
                ));
    }

    public static MaterialItem plateSteel(int quantity) {
        return item(ArmorMaterial.STEEL, "Lingote", quantity, 10, 6.000, 2, 1,
                "Material estructural de máxima calidad para armaduras tradicionales. Combina resistencia mecánica, tenacidad y capacidad para distribuir cargas e impactos. Su fabricación exige control térmico, laminado, templado y trabajo especializado. Permite producir superficies curvadas que desvían ataques y distribuyen la energía recibida sobre una región más amplia.",
                List.of(ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_CONDUCTOR, "CONDUCTOR ELÉCTRICO", "El acero transmite con facilidad las descargas eléctricas.", "DAÑO RECIBIDO | Electricidad ×2")));
    }

    public static MaterialItem ebonyWood(int quantity) {
        return item(ArmorMaterial.EBONY_WOOD, "Láminas seleccionadas", quantity, 8, 2.800, 4, 1,
                EbonyWarriorLore.MATERIAL,
                List.of(ItemProperty.alwaysActive(ItemPropertyId.FLAMMABLE, "INFLAMABLE", "Pese a su densidad y longevidad, el ébano sigue siendo materia orgánica combustible.", "DAÑO RECIBIDO | Quemadura ×2")));
    }

    public static MaterialItem electromechanicalComposite() {
        return item(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE, "Módulo técnico integrado", 1, 1, 12.000, 7, 4,
                "Denominación técnica introducida por Alicia e Iván para una arquitectura manufacturada que sólo adquiere sentido como conjunto. No existe un lingote de Compuesto Electromecánico: una unidad reúne estructura de acero y bronce, soportes textiles y de cuero, aislamiento de caucho vulcanizado y tela dieléctrica, conducciones hidráulicas y neumáticas, cableado, actuadores, juntas, regulación y puertos de refrigeración calibrados hasta comportarse como un único módulo funcional. Alicia desarrolló su aplicación corporal alrededor del Conjunto del Ingeniero; Iván sistematizó la instrumentación, medida y estabilización de sus subsistemas. Separadas, las partes vuelven a ser materiales ordinarios; integradas con el Maletín profesional de Alicia e Iván, su respuesta ya no puede explicarse como la simple suma de componentes.",
                List.of(ItemProperty.alwaysActive(ItemPropertyId.INTRICATE_MANUFACTURE, "MANUFACTURA INTRINCADA",
                        "La continuidad funcional depende de numerosas uniones, conducciones y mecanismos susceptibles de desajuste bajo carga mecánica. "
                                + domain.maintenance.ElectromechanicalCompositeRecipe.technicalSummary(),
                        "DESGASTE | ×2 · MANUFACTURA | Requiere Maletín profesional de Alicia e Iván")));
    }


    public static MaterialItem paper(int quantity) {
        return item(ArmorMaterial.PAPER, "Pliegos", quantity, 30, 0.100, 2, 2,
                "Material laminar de baja densidad obtenido mediante dispersión de fibras vegetales en agua, formación de una red fibrosa, prensado y secado. Su resistencia depende de la longitud y orientación de las fibras, el gramaje, la compactación, la humedad y los tratamientos superficiales. Se emplea en escritura, impresión, dibujo, cartografía, documentación, embalaje, filtración, aislamiento, artesanía y manufacturas compuestas; su plegado, laminación, martillado y combinación con tejidos permitieron también usos históricos de protección corporal.",
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.FRAGILE, "FRÁGIL", "La red fibrosa expuesta acumula rasgado, abrasión y daño localizado con rapidez.", "DESGASTE | ×2"),
                        ItemProperty.alwaysActive(ItemPropertyId.FLAMMABLE, "INFLAMABLE", "La celulosa seca sostiene la combustión cuando queda expuesta a una fuente térmica suficiente.", "DAÑO RECIBIDO | Quemadura ×2"),
                        ItemProperty.alwaysActive(ItemPropertyId.ANTI_CORROSIVE, "ANTI-CORROSIVO", "Su matriz celulósica no sufre la degradación metálica que explota la propiedad CORROSIVO y reduce la transferencia de determinadas toxinas.", "VENENO RECIBIDO | ×0,75 · CORROSIVO | Inmunidad"),
                        ItemProperty.alwaysActive(ItemPropertyId.INSULATING, "AISLANTE ELÉCTRICO", "El papel seco presenta elevada resistividad eléctrica mientras su estructura no quede humedecida.", "ELECTRICIDAD | Inmunidad mientras permanezca seco")
                ));
    }

    public static MaterialItem laminatedGlass(int quantity) {
        return item(ArmorMaterial.LAMINATED_GLASS, "Paneles laminados", quantity, 6, 1.800, 4, 3,
                "Material transparente compuesto por láminas de vidrio unidas permanentemente mediante una capa intermedia flexible. La unión conserva gran parte de la continuidad del conjunto cuando una lámina se fractura, retiene los fragmentos y reduce su dispersión. Su elevada resistencia frente al corte lo hace adecuado para visores, gafas protectoras y protecciones oculares; su respuesta perforante y contundente depende del espesor, el número de láminas y la geometría del soporte.", List.of());
    }

    public static MaterialItem mineralMultilayerFabric(int quantity) {
        return item(ArmorMaterial.MINERAL_MULTILAYER_FABRIC, "Paños minerales multicapa", quantity, 8, 1.000, 4, 3,
                "Material textil mineral formado por fibras resistentes al calor, comprimidas y entrelazadas en varias capas mediante hilo refractario. Presenta una elevada resistencia frente a llama, chispas, calor radiante y abrasión térmica. Su arquitectura rígida limita la movilidad respecto a los tejidos ordinarios, por lo que requiere un forro interior flexible que reduzca la fricción contra el cuerpo.", List.of());
    }

    public static MaterialItem rubber(int quantity) {
        return item(ArmorMaterial.RUBBER, "Láminas flexibles", quantity, 8, 1.200, 4, 3,
                "Material elastomérico flexible empleado para formar capuchas, juntas, sellos y superficies continuas capaces de adaptarse al cuerpo. Absorbe deformaciones moderadas, resiste humedad y agentes ambientales y permite construir cierres herméticos, aunque ofrece una protección convencional limitada frente a perforaciones concentradas.", List.of());
    }

    public static MaterialItem vulcanizedRubber(int quantity) {
        return item(ArmorMaterial.VULCANIZED_RUBBER, "Láminas vulcanizadas", quantity, 6, 1.500, 4, 3,
                "Caucho sometido a vulcanización para aumentar su estabilidad, elasticidad, resistencia al desgarro y conservación de forma. Su baja conductividad eléctrica permite utilizarlo como barrera aislante en guantes, botas, juntas y prendas protectoras, especialmente cuando se dispone en láminas continuas sin herrajes conductores expuestos.", List.of());
    }

    public static MaterialItem dielectricCloth(int quantity) {
        return item(ArmorMaterial.DIELECTRIC_CLOTH, "Capas dieléctricas", quantity, 12, 0.400, 3, 2,
                "Tejido de fibras naturales secas dispuesto en capas compactas y protegido frente a humedad y contaminación superficial. Su función principal es separar el cuerpo de las capas externas de caucho, distribuir tensiones y mantener cámaras aislantes flexibles sin introducir elementos conductores.", List.of());
    }


    public static MaterialItem mineralizedWood(int quantity) {
        return item(ArmorMaterial.MINERALIZED_WOOD, "Láminas mineralizadas", quantity, 8, 1.800, 4, 1,
                "Madera sometida a impregnación a presión con silicatos y boratos, seguida de curado controlado. El tratamiento conserva la geometría, la orientación de veta y el perfil defensivo de la madera original, pero elimina su condición inflamable. Continúa siendo un material estructural degradable y reparable con resina cuando el diseño lo permite.", List.of());
    }

    public static MaterialItem mineralizedEbony(int quantity) {
        return item(ArmorMaterial.MINERALIZED_EBONY, "Láminas mineralizadas", quantity, 8, 3.100, 4, 1,
                "Ébano superviviente sometido a impregnación a presión con silicatos y boratos y curado hasta fijar una fase mineral estable dentro de su estructura. La operación no pretende crear una nueva especie de madera, sino volver reproducible un material antiguo que ya no puede desperdiciarse. La mineralización estabiliza la matriz y la prepara para la reconstrucción V881, pero no basta por sí sola para suprimir la inflamabilidad: la nueva armadura exige además encapsulado exterior de wolframio de 2,5 mm. Su disponibilidad continúa limitada por las escasas reservas de ébano natural recuperable.", List.of(ItemProperty.alwaysActive(ItemPropertyId.FLAMMABLE, "INFLAMABLE", "La matriz orgánica mineralizada conserva vulnerabilidad térmica mientras no quede encapsulada por la construcción V881 de wolframio.", "DAÑO RECIBIDO | Quemadura ×2")));
    }

    public static MaterialItem tungstenPlates25mm(int quantity) {
        return item(ArmorMaterial.TUNGSTEN_PLATES_2_5_MM, "Placas de 2,5 mm", quantity, 6, 4.500, 3, 2,
                "Placas superficiales de wolframio de 2,5 mm destinadas a reforzar productos defensivos ya estructurados. Su elevada densidad concentra mucha masa en poco espesor y su punto de fusión dificulta cualquier rectificación. Presentan desgaste x0,5 y solo pueden repararse mediante la Maletín profesional de Alicia e Iván cuando existe al menos una unidad de Líquido Refrigerante disponible.", List.of());
    }

    public static MaterialItem tungsten(int quantity) {
        return item(ArmorMaterial.TUNGSTEN, "Lingote de aleación", quantity, 6, 8.000, 2, 1,
                "Metal extremadamente denso y refractario cuyo empleo puro resulta inviable para la mayor parte del equipamiento portátil. Se conserva como material canónico para aleaciones controladas con acero, donde aporta dureza, estabilidad térmica y resistencia localizada sin imponer íntegramente su masa ni sus dificultades de manufactura.", List.of());
    }

    /** Descripción técnica conservada para consumidores antiguos. */
    public static String laminatedGlassTechnicalDescription() {
        return "Material transparente compuesto por láminas de vidrio unidas permanentemente mediante una capa intermedia flexible. La unión conserva gran parte de la continuidad del conjunto cuando una lámina se fractura, retiene los fragmentos y reduce su dispersión. Su elevada resistencia frente al corte lo hace adecuado para visores, gafas protectoras y protecciones oculares; su respuesta perforante y contundente depende del espesor, el número de láminas y la geometría del soporte.";
    }

    public static List<MaterialItem> allCanonicalUnits() {
        return List.of(cloth(1), hardenedLeather(1), wood(1), bronze(1), plateSteel(1), ebonyWood(1),
                electromechanicalComposite(), paper(1), laminatedGlass(1), mineralMultilayerFabric(1),
                rubber(1), vulcanizedRubber(1), dielectricCloth(1), mineralizedWood(1),
                mineralizedEbony(1), tungstenPlates25mm(1), tungsten(1));
    }

    private static MaterialItem item(ArmorMaterial material, String format, int quantity, int max, double weight,
                                     int vertical, int horizontal, String description, List<ItemProperty> properties) {
        ArmorProtectionProfile p = material.canonicalProtection();
        if (quantity != 1) throw new IllegalArgumentException("solicite unidades de material como instancias independientes.");
        return new MaterialItem(material, format, description, 1, 1, weight,
                new InventoryFootprint(vertical, horizontal),
                List.of(
                        "FORMATO | " + format,
                        "PROTECCIÓN CANÓNICA | " + p.piercing() + "% / " + p.slashing() + "% / " + p.blunt() + "%",
                        "POLÍTICA DE DESGASTE | " + (material.wearPolicy() == ArmorWearPolicy.NON_DEGRADING ? "No degradable" : "Degradable ×" + formatMultiplier(material.bluntWearMultiplier())),
                        "UNIDAD FÍSICA | Individual",
                        "PESO POR UNIDAD | " + String.format(java.util.Locale.ROOT, "%.3f kg", weight),
                        "PRECIO DE REFERENCIA | " + MaterialMarketCatalog.profile(material).referencePriceValeritasPerUnit() + " Valeritas por unidad inventariable",
                        "TIPO ECONÓMICO | " + MaterialMarketCatalog.profile(material).economicGoodType(),
                        "MONEDAS ADMITIDAS | " + MaterialMarketCatalog.profile(material).acceptedCurrencies().stream().map(domain.inventory.item.misc.CurrencyType::label).sorted().collect(java.util.stream.Collectors.joining(" · ")),
                        "MERCADO | " + MaterialMarketCatalog.profile(material).marketNarrative(),
                        "PROFESIONES INTERESADAS | " + MaterialMarketCatalog.profile(material).interestedProfessions().stream()
                                .map(domain.social.Profession::label).sorted().collect(java.util.stream.Collectors.joining(" · "))
                ), properties);
    }

    private static String formatMultiplier(double value) {
        return value == Math.rint(value) ? Long.toString(Math.round(value)) : Double.toString(value).replace('.', ',');
    }
}
