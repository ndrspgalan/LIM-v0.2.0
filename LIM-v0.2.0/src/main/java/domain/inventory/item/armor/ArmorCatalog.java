package domain.inventory.item.armor;

import domain.character.sheet.Attribute;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.InventoryFootprint;
import domain.inventory.logistics.ArmorPhysicalDimensionsCatalog;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Catálogo canónico de armaduras. Cada llamada crea piezas nuevas para que
 * desgaste y rotura pertenezcan a la instancia equipada, no a la definición.
 */
public final class ArmorCatalog {
    private ArmorCatalog() {}

    public static ArmorPiece enlightenedPanopticon() { return enlightenedPanopticonCanonical(); }

    public static ArmorPiece travelerNeckGaiter() {
        return new ArmorPiece("Cubrecuellos del Viajero",
                "Prenda tubular de tela densa y flexible ajustada al cuello y a la parte baja del rostro. El corte evita costuras voluminosas junto a mandíbula y clavículas y permite llevarla bajo otras prendas sin interferir con la movilidad cervical.",
                0.150, ArmorPhysicalDimensionsCatalog.headFootprintFor("Cubrecuellos del Viajero"), ArmorHitLocation.HEAD,0.20,new ArmorProtectionProfile(6,15,6),ArmorMaterial.CLOTH,ArmorForm.NECK_GAITER,
                List.of("MATERIAL | TELA x3","COBERTURA | CABEZA 20%","PROTECCIÓN | 6% / 15% / 6%"),List.of())
                .withHeadLayer(HeadLayer.LOWER_ACCESSORY);
    }

    // ----------------  · INNER CHEST ----------------
    private static ArmorPiece innerChestTextile(String name, String narrative, double weightKg,
                                                 InnerChestLayer innerLayer,
                                                 Map<BodyArmorRegion, Double> coverage,
                                                 ArmorProtectionProfile profile, int effectiveClothLayers) {
        var dimensions = ArmorPhysicalDimensionsCatalog.innerChestDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.innerChestFootprintFor(name);
        return new ArmorPiece(name, narrative, weightKg, footprint, ArmorInventoryCategory.CHEST, coverage,
                profile, ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL | TELA x" + effectiveClothLayers,
                        "COBERTURA | " + coverage.entrySet().stream().map(e -> e.getKey().name()+" "+Math.round(e.getValue()*100)+"%").collect(java.util.stream.Collectors.joining(" · ")),
                        "PROTECCIÓN | " + formatProtection(profile),
                        "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weightKg),
                        "DIMENSIONES PLEGADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of())
                .withInnerChestLayer(innerLayer);
    }

    private static ArmorPiece innerChestCorsetry(String name, String narrative, double weightKg,
                                                  Map<BodyArmorRegion, Double> coverage) {
        StructuredCorsetryProfile construction = StructuredCorsetryProfile.canonicalV881();
        var dimensions = ArmorPhysicalDimensionsCatalog.innerChestDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.innerChestFootprintFor(name);
        return new ArmorPiece(name, narrative, weightKg, footprint, ArmorInventoryCategory.CHEST, coverage,
                construction.protection(), ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL DOMINANTE | TELA",
                        "CONSTRUCCIÓN | " + construction.constructionLabel(),
                        "CONTINUIDAD CONDUCTORA | " + (construction.continuousConductivePath() ? "Sí" : "No"),
                        "COBERTURA | " + coverage.entrySet().stream().map(e -> e.getKey().name()+" "+Math.round(e.getValue()*100)+"%").collect(java.util.stream.Collectors.joining(" · ")),
                        "PROTECCIÓN DEL ENSAMBLAJE | " + formatProtection(construction.protection()),
                        "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weightKg),
                        "DIMENSIONES ESTRUCTURADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of())
                .withInnerChestLayer(InnerChestLayer.STRUCTURAL);
    }

    public static ArmorPiece innerUndershirt() {
        return innerChestTextile("Camiseta interior de punto V881",
                "Prenda interior de punto ceñido cuya continuidad de lazadas acompaña la expansión torácica y el movimiento escapular. El tejido mantiene una fina cámara de aire junto a la piel y reparte el rozamiento de las prendas superpuestas sin introducir costuras rígidas sobre las articulaciones.",
                0.180, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.05), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerShirt() {
        return innerChestTextile("Camisa V881",
                "Camisa de paño ligero construida mediante paneles amplios y mangas independientes, con holgura suficiente para trabajar bajo otras prendas. Las costuras se apartan de las principales líneas de flexión y el cuello estabiliza la abertura superior sin rigidizar el torso.",
                0.280, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerWorkShirt() {
        return innerChestTextile("Camisa de trabajo V881",
                "Camisa de faena confeccionada con tejido más denso y refuerzos localizados en costuras, hombros y puntos de tracción. El patronaje conserva amplitud para elevar y cruzar los brazos mientras las zonas sometidas a roce reparten mejor la abrasión repetida del oficio.",
                0.420, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(3,7,3),1);
    }
    public static ArmorPiece innerModularShirtV881() {
        return innerChestTextile("Camisa modular V881",
                "Camisa de confección modular con cuello y puños desmontables. Las piezas que reciben mayor sudor, grasa, almidonado y deformación pueden lavarse o sustituirse sin descoser el cuerpo principal, prolongando la vida útil de la prenda y permitiendo adaptar su acabado al uso social o laboral.",
                0.340, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerBlouse() {
        return innerChestTextile("Blusa V881",
                "Prenda interior de torso de patronaje holgado, recogida en cuello y puños para concentrar el exceso de tela fuera de las articulaciones. La amplitud permite una gran libertad escapular y facilita superponer prendas estructuradas sin tensar el paño sobre pecho y espalda.",
                0.300, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerRegionalBlouse() {
        return innerChestTextile("Blusa regional V881",
                "Blusa de raíz campesina y regional construida con cuerpo amplio, mangas generosas y frunces que concentran la tela en cuello y puños. Su patronaje admite bordado y variaciones locales sin alterar la lógica funcional: libertad de movimiento y fácil reparación mediante paneles de tejido sencillos.",
                0.360, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerChemise() {
        return innerChestTextile("Chemise V881",
                "Prenda interior larga y holgada que separa la piel de corsés, corpiños y vestidos de confección más compleja. El paño continuo distribuye el roce sobre una superficie extensa y absorbe humedad corporal antes de que alcance las estructuras superpuestas.",
                0.350, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.05), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerGomlek() {
        return innerChestTextile("Gömlek V881",
                "Camisa interior de tradición otomana construida aprovechando anchuras completas de telar, plegadas sobre los hombros y prolongadas mediante paneles específicos para las mangas. El corte amplio reduce la concentración de tensión en costuras durante torsión y flexión del tronco.",
                0.320, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerCamisoleV881() {
        return innerChestTextile("Camisola V881",
                "Underbodice ligero y sin estructura rígida concebido como cubierta fina sobre una estructura interior. Su borde superior y tirantes descargan la tela de las axilas, suavizan el contacto y el relieve de la capa estructural inferior y permiten recibir mangas posteriores sin añadir rigidez al torso.",
                0.200, InnerChestLayer.COVER,
                Map.of(BodyArmorRegion.CHEST,.50), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerChemisette() {
        return innerChestTextile("Chemisette V881",
                "Pieza textil reducida concebida para ocupar únicamente el frente superior del torso y cerrar visualmente escotes o aberturas de prendas superpuestas. Su escasa superficie concentra la confección en cuello y pechera y permite sustituir el acabado visible sin vestir otra camisa completa.",
                0.100, InnerChestLayer.COVER,
                Map.of(BodyArmorRegion.CHEST,.15), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerDickey() {
        return innerChestTextile("Pechera desmontable V881",
                "Falso frente de camisa formado por una pechera rígidamente planchada y sus elementos de fijación. Se sujeta bajo la prenda exterior para reproducir el acabado visible de una camisa completa con una fracción del tejido, facilitando lavado, transporte y sustitución de la zona expuesta.",
                0.080, InnerChestLayer.COVER,
                Map.of(BodyArmorRegion.CHEST,.12), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerCorset() {
        return innerChestCorsetry("Corsé V881",
                "Estructura vestida compuesta por paneles textiles tensados mediante cordones y rigidizadores longitudinales alojados en canales cosidos. La protección no procede de dos capas de tela aisladas: el ensamblaje tensado se opone al plegado local y distribuye parte de la carga alrededor de cintura y caja torácica sin comportarse como una coraza continua ni crear por sí solo una vía eléctrica continua.",
                0.650, Map.of(BodyArmorRegion.CHEST,.35));
    }
    public static ArmorPiece innerMaleCorset() {
        return innerChestCorsetry("Corsé masculino V881",
                "Prenda estructurada de cintura y abdomen con menor elevación pectoral y líneas de tensión adaptadas a una silueta masculina decimonónica. Paneles textiles tensados y rigidizadores longitudinales estabilizan el tronco y reparten compresión sin convertirse en una coraza continua; el perfil protector pertenece al ensamblaje, no a una supuesta segunda capa de tela.",
                0.550, Map.of(BodyArmorRegion.CHEST,.35));
    }
    public static ArmorPiece innerCorsetCover() {
        return innerChestTextile("Cubrecorsé V881",
                "Prenda fina colocada sobre una estructura interior para suavizar las aristas de ballenas, cordones y cierres antes de recibir la ropa visible. Su patronaje sigue el torso sin comprimirlo y evita que los elementos rígidos marquen o desgasten prematuramente la siguiente capa textil.",
                0.180, InnerChestLayer.COVER,
                Map.of(BodyArmorRegion.CHEST,.50), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece innerCombinationV881() {
        return innerChestTextile("Combinación interior V881",
                "Prenda interior continua que une cuerpo superior y perneras en una sola confección, reduciendo solapes de cintura y manteniendo el tejido estable durante movimiento prolongado. La unión reparte la tracción entre torso y cadera y evita que las prendas superpuestas descubran la región lumbar.",
                0.450, InnerChestLayer.BASE,
                Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.05, BodyArmorRegion.LEGGINGS,.30), new ArmorProtectionProfile(2,5,2),1)
                .withInnerLeggingsLayer(InnerLeggingsLayer.BASE);
    }

    public static List<ArmorPiece> allInnerChestGarments() {
        return List.of(innerUndershirt(), innerShirt(), innerWorkShirt(), innerModularShirtV881(), innerBlouse(),
                innerRegionalBlouse(), innerChemise(), innerGomlek(), innerCamisoleV881(), innerChemisette(),
                innerDickey(), innerCorset(), innerMaleCorset(), innerCorsetCover(), innerCombinationV881());
    }



    // ----------------  · INNER LEGGINGS ----------------
    private static ArmorPiece innerLeggingsTextile(String name, String narrative, double weightKg,
                                                    InnerLeggingsLayer innerLayer,
                                                    double leggingsCoverage, ArmorProtectionProfile profile,
                                                    int effectiveClothLayers) {
        Map<BodyArmorRegion, Double> coverage = Map.of(BodyArmorRegion.LEGGINGS, leggingsCoverage);
        var dimensions = ArmorPhysicalDimensionsCatalog.innerLeggingsDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.innerLeggingsFootprintFor(name);
        TextileAssemblyProfile construction = effectiveClothLayers == 5
                ? TextileAssemblyProfile.padded(5)
                : TextileAssemblyProfile.ordinary(effectiveClothLayers);
        if (!construction.protection().equals(profile)) {
            throw new IllegalArgumentException("Perfil INNER LEGGINGS incoherente con su construcción: " + name);
        }
        return new ArmorPiece(name, narrative, weightKg, footprint, ArmorInventoryCategory.LEGGINGS, coverage,
                construction.protection(), ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("CONSTRUCCIÓN | " + construction.constructionLabel(),
                        "COBERTURA | LEGGINGS " + Math.round(leggingsCoverage * 100) + "%",
                        "PROTECCIÓN | " + formatProtection(construction.protection()),
                        "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weightKg),
                        "DIMENSIONES PLEGADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of())
                .withInnerLeggingsLayer(innerLayer);
    }

    public static ArmorPiece innerLongDrawersV881() {
        return innerLeggingsTextile("Calzoncillos largos V881",
                "Prenda interior de perneras completas confeccionada para permanecer próxima a la piel sin impedir la flexión de cadera, rodilla y tobillo. El tiro amplio y las costuras desplazadas reducen el roce bajo pantalones, faldas o protecciones superpuestas durante marcha prolongada.",
                0.300, InnerLeggingsLayer.BASE, .30,
                new ArmorProtectionProfile(2,5,2),1);
    }

    public static ArmorPiece innerKneeDrawersV881() {
        return innerLeggingsTextile("Calzoncillos hasta la rodilla V881",
                "Prenda interior bífida que termina alrededor de la rodilla y mantiene libre la parte inferior de la pierna. El corte reduce la acumulación de tejido bajo botas altas y polainas, mientras la holgura del muslo conserva amplitud suficiente para sentarse, montar o agacharse.",
                0.200, InnerLeggingsLayer.BASE, .15,
                new ArmorProtectionProfile(2,5,2),1);
    }

    public static ArmorPiece innerKnittedTrousersV881() {
        return innerLeggingsTextile("Pantalón interior de punto V881",
                "Prenda interior de punto continuo cuyas lazadas acompañan la extensión y torsión de las piernas sin requerir grandes pliegues de holgura. La recuperación elástica mantiene el tejido próximo a muslos y pantorrillas y limita que las capas exteriores arrastren o formen bolsas durante el movimiento.",
                0.320, InnerLeggingsLayer.BASE, .30,
                new ArmorProtectionProfile(2,5,2),1);
    }

    public static ArmorPiece innerWomensDrawersV881() {
        return innerLeggingsTextile("Drawers femeninos V881",
                "Prenda interior de dos perneras holgadas unidas a una cintura común, concebida para interponerse entre la piel y las estructuras de falda sin convertir el volumen inferior en una pieza cerrada y rígida. La amplitud del patronaje mantiene ventilación y libertad de zancada bajo varias capas textiles.",
                0.250, InnerLeggingsLayer.BASE, .20,
                new ArmorProtectionProfile(2,5,2),1);
    }

    public static ArmorPiece innerPetticoatV881() {
        return innerLeggingsTextile("Enagua V881",
                "Falda interior ligera que cae desde la cintura alrededor de las piernas y crea una superficie textil de separación entre la ropa basal y la prenda visible. Su amplitud evita adherencias entre capas y permite que la prenda exterior conserve caída y movimiento sin transmitir directamente el roce a la piel.",
                0.350, InnerLeggingsLayer.COVER, .30,
                new ArmorProtectionProfile(2,5,2),1);
    }

    public static ArmorPiece innerReinforcedPetticoatV881() {
        return innerLeggingsTextile("Enagua reforzada V881",
                "Enagua de paño reforzado mediante una segunda hoja efectiva en las zonas sometidas a mayor roce y tracción. El borde inferior, las uniones y el entorno de la cintura distribuyen mejor los esfuerzos sin convertir la prenda en una estructura rígida ni alterar su función de separación entre capas.",
                0.500, InnerLeggingsLayer.COVER, .30,
                new ArmorProtectionProfile(4,10,4),2);
    }

    public static ArmorPiece innerPaddedPetticoatV881() {
        return innerLeggingsTextile("Enagua acolchada V881",
                "Prenda interior formada por paños textiles que encierran un relleno ligero fijado mediante una retícula de costuras. Las cámaras impiden que el material migre hacia el bajo durante la marcha y reparten sobre una superficie mayor los contactos y golpes transmitidos por las prendas exteriores.",
                0.800, InnerLeggingsLayer.COVER, .30,
                new ArmorProtectionProfile(10,25,10),5);
    }

    public static ArmorPiece innerDividedPetticoatV881() {
        return innerLeggingsTextile("Enagua dividida V881",
                "Prenda interior de apariencia amplia cuya parte inferior se separa en dos conductos independientes para las piernas. La división evita que todo el paño tenga que desplazarse como una sola falda al montar, subir obstáculos o ampliar la zancada, conservando volumen suficiente bajo una prenda exterior.",
                0.400, InnerLeggingsLayer.COVER, .30,
                new ArmorProtectionProfile(2,5,2),1);
    }

    public static List<ArmorPiece> allInnerLeggingsGarments() {
        return List.of(innerLongDrawersV881(), innerKneeDrawersV881(), innerKnittedTrousersV881(), innerWomensDrawersV881(),
                innerPetticoatV881(), innerReinforcedPetticoatV881(), innerPaddedPetticoatV881(), innerDividedPetticoatV881(),
                innerCombinationV881());
    }


    // ----------------  · MIDDLE LEGGINGS ----------------
    private static ArmorPiece middleLeggingsTextile(String name, String narrative, double weightKg,
                                                     double leggingsCoverage,
                                                     ArmorProtectionProfile profile, int effectiveClothLayers) {
        Map<BodyArmorRegion, Double> coverage = Map.of(BodyArmorRegion.LEGGINGS, leggingsCoverage);
        var dimensions = ArmorPhysicalDimensionsCatalog.middleLeggingsDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.middleLeggingsFootprintFor(name);
        TextileAssemblyProfile construction;
        if (profile.equals(new ArmorProtectionProfile(3,7,3))) {
            construction = TextileAssemblyProfile.denseCloth();
        } else if (profile.equals(new ArmorProtectionProfile(3,8,3))) {
            construction = TextileAssemblyProfile.pleatedOverlap();
        } else {
            construction = TextileAssemblyProfile.ordinary(effectiveClothLayers);
        }
        if (!construction.protection().equals(profile)) {
            throw new IllegalArgumentException("Perfil MIDDLE LEGGINGS incoherente con su construcción: " + name);
        }
        return new ArmorPiece(name, narrative, weightKg, footprint, ArmorInventoryCategory.LEGGINGS, coverage,
                construction.protection(), ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("CONSTRUCCIÓN | " + construction.constructionLabel(),
                        "COBERTURA | LEGGINGS " + Math.round(leggingsCoverage * 100) + "%",
                        "PROTECCIÓN | " + formatProtection(construction.protection()),
                        "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weightKg),
                        "DIMENSIONES PLEGADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of());
    }

    public static ArmorPiece middleStraightTrousersV881() {
        return middleLeggingsTextile("Pantalón recto V881",
                "Pantalón civil de perneras rectas construido a partir de paños longitudinales unidos alrededor de cadera y entrepierna. El corte reparte la holgura de forma uniforme y permite caminar, sentarse o montar sin acumular pliegues innecesarios bajo una prenda exterior.",
                0.700, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleFormalTrousersV881() {
        return middleLeggingsTextile("Pantalón formal V881",
                "Pantalón sastreado de caída controlada, con cintura estructurada, costuras planchadas y holgura contenida alrededor de muslo y pantorrilla. El patronaje prioriza una línea estable y limpia sin impedir la flexión ordinaria de cadera y rodilla.",
                0.750, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleWorkTrousersV881() {
        return middleLeggingsTextile("Pantalón de trabajo V881",
                "Pantalón de faena confeccionado con paño más denso y refuerzos localizados en asiento, rodillas, bolsillos y puntos de tracción. Las costuras evitan concentrar esfuerzos en una sola línea y prolongan el servicio de la prenda durante carga, arrastre y trabajo repetitivo.",
                0.950, .30, new ArmorProtectionProfile(3,7,3),1);
    }
    public static ArmorPiece middleHighWaistedTrousersV881() {
        return middleLeggingsTextile("Pantalón de cintura alta V881",
                "Pantalón cuyo talle asciende sobre la cintura natural para estabilizar la prenda durante flexión y marcha prolongada. La mayor altura del cinturón reparte la tensión alrededor del abdomen y reduce que la camisa interior se libere durante el movimiento.",
                0.800, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleLooseTrousersV881() {
        return middleLeggingsTextile("Pantalón holgado V881",
                "Pantalón de perneras amplias y tiro generoso cuya reserva de tejido permite grandes ángulos de cadera y rodilla sin tensar costuras. La amplitud mejora libertad de movimiento a costa de transportar más paño y producir mayor arrastre cuando la prenda se empapa.",
                0.900, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleSailorTrousersV881() {
        return middleLeggingsTextile("Pantalón marinero V881",
                "Pantalón de pernera ancha y cierre frontal amplio, concebido para vestirse y retirarse con rapidez y para no limitar la zancada sobre cubiertas, escalas y superficies inestables. El bajo ancho facilita arremangarlo antes de trabajar cerca del agua.",
                0.850, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleRidingTrousersV881() {
        return middleLeggingsTextile("Pantalón de montar V881",
                "Pantalón ecuestre ajustado para permanecer estable sobre la montura, con holgura suficiente en cadera y refuerzo textil localizado en asiento y caras internas de las piernas. El patronaje reduce pliegues bajo correas, silla y polainas exteriores.",
                0.900, .30, new ArmorProtectionProfile(3,7,3),1);
    }
    public static ArmorPiece middleBreechesV881() {
        return middleLeggingsTextile("Breeches V881",
                "Calzones ajustados que terminan por debajo de la rodilla y dejan la parte inferior de la pierna libre para medias, botas o polainas. Su volumen se concentra en cadera y muslo, reduciendo la superposición de tejido en pantorrilla y tobillo.",
                0.500, .20, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleKnickerbockersV881() {
        return middleLeggingsTextile("Knickerbockers V881",
                "Pantalón corto de pernera holgada recogida bajo la rodilla mediante una banda de cierre. El volumen superior permite amplitud de zancada mientras el ajuste distal impide que el tejido descienda sobre botas o se enganche con facilidad.",
                0.600, .20, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleBloomersV881() {
        return middleLeggingsTextile("Bombachos V881",
                "Pantalón de gran holgura cuyas perneras se recogen hacia el extremo inferior para conservar libertad alrededor de cadera y rodillas. El tejido adicional favorece movimientos amplios y posturas de trabajo, aunque aumenta masa y retención de agua respecto de un corte recto.",
                0.950, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleStraightSkirtV881() {
        return middleLeggingsTextile("Falda recta V881",
                "Falda de caída relativamente estrecha formada por paneles verticales que envuelven las piernas desde la cintura. El bajo contenido reduce material y oscilación, pero exige que aberturas, pliegues o holguras del patronaje absorban la longitud de la zancada.",
                0.650, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleFullSkirtV881() {
        return middleLeggingsTextile("Falda amplia V881",
                "Falda construida con un perímetro de paño amplio que distribuye el volumen alrededor de ambas piernas. Los pliegues de cintura administran el exceso de tejido y permiten una zancada extensa sin someter el material a tensión directa.",
                1.000, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleWalkingSkirtV881() {
        return middleLeggingsTextile("Falda de paseo V881",
                "Falda de longitud y amplitud ajustadas para desplazamiento urbano prolongado, con el bajo suficientemente contenido para disminuir roces con suelo y escalones. El patronaje prioriza una caída estable durante marcha ordinaria sin reducir la movilidad de las piernas.",
                0.800, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleWorkSkirtV881() {
        return middleLeggingsTextile("Falda de trabajo V881",
                "Falda de faena confeccionada con paño resistente, costuras simples y bajo controlado para limitar enganches. Refuerzos localizados en cintura y zonas de roce prolongan su uso sin convertir todo el tejido en una construcción multicapa.",
                0.900, .30, new ArmorProtectionProfile(3,7,3),1);
    }
    public static ArmorPiece middleRidingSkirtV881() {
        return middleLeggingsTextile("Falda de montar V881",
                "Falda ecuestre de gran amplitud redistribuida alrededor de la montura para cubrir las piernas sin inmovilizarlas. El patronaje incorpora solapes y volumen adicional en los sectores sometidos a apertura lateral, evitando que el tejido tire de la cintura durante la monta.",
                1.100, .30, new ArmorProtectionProfile(3,7,3),1);
    }
    public static ArmorPiece middleDividedSkirtV881() {
        return middleLeggingsTextile("Falda dividida V881",
                "Prenda cuya silueta exterior conserva el volumen de una falda mientras el patronaje interior separa ambas piernas. La división permite montar, correr o franquear obstáculos sin desplazar toda la masa textil como una sola envolvente.",
                0.900, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleOrnamentedSkirtV881() {
        return middleLeggingsTextile("Falda ornamentada V881",
                "Falda de tradición regional o ceremonial cuya superficie incorpora bordados, cintas, aplicaciones y remates cosidos sobre el paño portante. La ornamentación modifica masa, rigidez local y caída sin sustituir la continuidad textil que constituye la prenda.",
                1.050, .30, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleOverskirtV881() {
        return middleLeggingsTextile("Sobrefalda V881",
                "Segunda pieza de falda concebida para caer sobre una prenda inferior visible, concentrando el paño en paneles frontales, laterales o posteriores. Su cobertura deliberadamente parcial crea zonas de doble tejido sin ocultar necesariamente toda la pierna.",
                0.600, .20, new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleKiltV881() {
        return middleLeggingsTextile("Kilt V881",
                "Prenda envolvente de paño plisado sujeta alrededor de la cintura y extendida hasta las proximidades de la rodilla. Los pliegues posteriores almacenan tejido para permitir amplitud de zancada, mientras el frente solapado mantiene una superficie estable.",
                0.650, .15, new ArmorProtectionProfile(3,8,3),1);
    }
    public static ArmorPiece middleSarongV881() {
        return middleLeggingsTextile("Sarong V881",
                "Paño rectangular amplio envuelto y asegurado alrededor de la cintura para formar una prenda inferior continua. La ausencia de costuras complejas permite reajustar tensión y longitud con rapidez, y concentra la construcción en la forma de envolver y solapar el tejido.",
                0.550, .30, new ArmorProtectionProfile(2,5,2),1);
    }

    public static List<ArmorPiece> allMiddleLeggingsGarments() {
        return List.of(middleStraightTrousersV881(), middleFormalTrousersV881(), middleWorkTrousersV881(), middleHighWaistedTrousersV881(),
                middleLooseTrousersV881(), middleSailorTrousersV881(), middleRidingTrousersV881(), middleBreechesV881(), middleKnickerbockersV881(),
                middleBloomersV881(), middleStraightSkirtV881(), middleFullSkirtV881(), middleWalkingSkirtV881(), middleWorkSkirtV881(),
                middleRidingSkirtV881(), middleDividedSkirtV881(), middleOrnamentedSkirtV881(), middleOverskirtV881(), middleKiltV881(), middleSarongV881());
    }

    // ----------------  · MIDDLE CHEST ----------------
    private static ArmorPiece middleChestTextile(String name, String narrative, double weightKg,
                                                  Map<BodyArmorRegion, Double> coverage,
                                                  ArmorProtectionProfile profile, int effectiveClothLayers) {
        var dimensions = ArmorPhysicalDimensionsCatalog.middleChestDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.middleChestFootprintFor(name);
        TextileAssemblyProfile construction = TextileAssemblyProfile.ordinary(effectiveClothLayers, profile);
        return new ArmorPiece(name, narrative, weightKg, footprint, ArmorInventoryCategory.CHEST, coverage,
                construction.protection(), ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("CONSTRUCCIÓN | " + construction.constructionLabel(),
                        "COBERTURA | " + coverage.entrySet().stream().map(e -> e.getKey().name()+" "+Math.round(e.getValue()*100)+"%").collect(java.util.stream.Collectors.joining(" · ")),
                        "PROTECCIÓN | " + formatProtection(construction.protection()),
                        "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weightKg),
                        "DIMENSIONES PLEGADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of());
    }

    private static ArmorPiece middleChestPadded(String name, String narrative, double weightKg,
                                                Map<BodyArmorRegion, Double> coverage, int effectiveLayers) {
        var dimensions = ArmorPhysicalDimensionsCatalog.middleChestDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.middleChestFootprintFor(name);
        TextileAssemblyProfile construction = TextileAssemblyProfile.padded(effectiveLayers);
        return new ArmorPiece(name, narrative, weightKg, footprint, ArmorInventoryCategory.CHEST, coverage,
                construction.protection(), ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("CONSTRUCCIÓN | " + construction.constructionLabel(),
                        "COBERTURA | " + coverage.entrySet().stream().map(e -> e.getKey().name()+" "+Math.round(e.getValue()*100)+"%").collect(java.util.stream.Collectors.joining(" · ")),
                        "PROTECCIÓN DEL ENSAMBLAJE | " + formatProtection(construction.protection()),
                        "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weightKg),
                        "DIMENSIONES PLEGADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of());
    }

    private static ArmorPiece middleRegionalBodicePiece() {
        String name = "Corpiño regional V881";
        var dimensions = ArmorPhysicalDimensionsCatalog.middleChestDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.middleChestFootprintFor(name);
        TextileAssemblyProfile construction = TextileAssemblyProfile.partialRegionalBodice();
        Map<BodyArmorRegion, Double> coverage = Map.of(BodyArmorRegion.CHEST,.50);
        return new ArmorPiece(name,
                "Corpiño de tradición regional cuya silueta se obtiene mediante paneles textiles reforzados, cordones y remates visibles en lugar de una estructura interna independiente. La confección concentra resistencia en bordes y cierres: el segundo estrato es parcial, no una segunda superficie completa, por lo que su perfil protector pertenece al ensamblaje distribuido.",
                0.500, footprint, ArmorInventoryCategory.CHEST, coverage,
                construction.protection(), ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("CONSTRUCCIÓN | " + construction.constructionLabel(),
                        "COBERTURA | CHEST 50%",
                        "PROTECCIÓN DEL ENSAMBLAJE | " + formatProtection(construction.protection()),
                        "PESO SECO | 0.500 kg",
                        "DIMENSIONES PLEGADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of());
    }

    public static ArmorPiece middleWaistcoat() {
        return middleChestTextile("Chaleco V881",
                "Prenda sastreada sin mangas que ajusta el frente del torso mediante paneles unidos a una espalda textil más flexible. El cierre frontal mantiene estable la camisa inferior y ofrece una superficie continua para bolsillos, reloj, documentación y pequeños útiles sin comprometer la movilidad de los brazos.",
                0.350, Map.of(BodyArmorRegion.CHEST,.50), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleLongWaistcoat() {
        return middleChestTextile("Chaleco largo V881",
                "Variante alargada del chaleco cuya falda textil desciende más allá de la cintura y reparte las tensiones del cierre sobre una superficie mayor. El patronaje mantiene libre la articulación del hombro y estabiliza las prendas interiores cuando se viste bajo una levita o abrigo abierto.",
                0.450, Map.of(BodyArmorRegion.CHEST,.50), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleWorkWaistcoat() {
        return middleChestTextile("Chaleco de trabajo V881",
                "Chaleco de faena confeccionado con paño más denso y refuerzos en bordes, bolsillos y puntos de tracción. La ausencia de mangas evita acumular material en hombros y codos, mientras el frente resistente soporta el contacto repetido con herramientas, cargas y superficies de trabajo.",
                0.550, Map.of(BodyArmorRegion.CHEST,.50), new ArmorProtectionProfile(3,7,3),1);
    }
    public static ArmorPiece middlePaddedWaistcoat() {
        return middleChestPadded("Chaleco acolchado V881",
                "Prenda sin mangas formada por paños textiles que encierran un relleno distribuido en cámaras cosidas. El acolchado evita que el relleno migre durante el movimiento y transforma cargas breves y rozamientos repetidos en deformación repartida sobre el torso.",
                0.900, Map.of(BodyArmorRegion.CHEST,.50),5);
    }
    public static ArmorPiece middleRidingWaistcoat() {
        return middleChestTextile("Chaleco de montar V881",
                "Chaleco entallado para permanecer estable con el tronco flexionado sobre una montura. El corte reduce sobrantes en cintura y axilas, desplaza bolsillos fuera de las zonas de presión y mantiene el frente ceñido para que riendas, correas y prendas exteriores no enganchen tejido suelto.",
                0.450, Map.of(BodyArmorRegion.CHEST,.50), new ArmorProtectionProfile(3,7,3),1);
    }
    public static ArmorPiece middleBodice() {
        return middleChestTextile("Corpiño V881",
                "Prenda entallada construida mediante paneles curvos, pinzas y costuras longitudinales que conforman el tejido alrededor del torso sin recurrir a la compresión rígida de un corsé. El cierre mantiene la tensión distribuida y deja hombros y brazos libres para superponer mangas o una prenda exterior.",
                0.550, Map.of(BodyArmorRegion.CHEST,.50), new ArmorProtectionProfile(4,10,4),2);
    }
    public static ArmorPiece middleRegionalBodice() {
        return middleRegionalBodicePiece();
    }
    public static ArmorPiece middleSpencer() {
        return middleChestTextile("Spencer V881",
                "Chaqueta corta que termina alrededor de la cintura y envuelve torso y brazos sin faldones. Su poca longitud reduce interferencias con cinturones, monturas y prendas inferiores, mientras el cierre frontal y las mangas ajustadas mantienen una capa térmica estable sobre la camisa.",
                0.600, Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middlePaddedJacket() {
        return middleChestPadded("Chaqueta interior acolchada V881",
                "Chaqueta intermedia de paños cosidos alrededor de un relleno ligero que cubre torso y brazos. Las líneas de acolchado fijan el material aislante y conservan zonas de flexión en codos y hombros para que la prenda pueda llevarse bajo una capa exterior o una protección más rígida.",
                1.400, Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10),6);
    }
    public static ArmorPiece middleDoubletV881() {
        return middleChestTextile("Jubón V881",
                "Jubón entallado de varias hojas textiles cosidas para mantener una silueta compacta alrededor del torso y los brazos. Las costuras longitudinales reparten la tensión, los refuerzos de cierre estabilizan el frente y el patronaje permite usarlo como capa intermedia sin acumular pliegues bajo una prenda exterior.",
                0.800, Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(6,15,6),3);
    }
    public static ArmorPiece middleCardiganV881() {
        return middleChestTextile("Cardigan V881",
                "Prenda de punto abierta por delante, compuesta por paneles flexibles unidos de forma que las lazadas acompañen la expansión torácica y la flexión de los brazos. La abertura frontal permite regular ventilación y facilita retirarla sin alterar las prendas interiores.",
                0.550, Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(2,5,2),1);
    }
    public static ArmorPiece middleKnittedJerseyV881() {
        return middleChestTextile("Jersey de punto V881",
                "Prenda cerrada de punto continuo que cubre torso y brazos con una malla elástica capaz de retener aire sin depender de una abertura frontal. Cuello, puños y cintura concentran la recuperación elástica para limitar la entrada de corrientes y mantener el tejido próximo al cuerpo.",
                0.650, Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10), new ArmorProtectionProfile(3,7,3),1);
    }

    /** El antiguo gambesón de conjunto pasa a ser una prenda MIDDLE autónoma. */
    public static ArmorPiece middleGambesonV881() {
        return middleChestPadded("Gambesón V881",
                "Evolución multicapa del gambesón que combina densidades de fibras para distribuir y disipar la energía de golpes, abrasión y fragmentos sin sacrificar la movilidad. Sus dieciocho capas efectivas forman un volumen acolchado real: no se pliega como una chaqueta ordinaria.",
                4.200, Map.of(BodyArmorRegion.CHEST,.50, BodyArmorRegion.BRACERS,.10),18);
    }

    public static List<ArmorPiece> allMiddleChestGarments() {
        return List.of(middleWaistcoat(), middleLongWaistcoat(), middleWorkWaistcoat(), middlePaddedWaistcoat(),
                middleRidingWaistcoat(), middleBodice(), middleRegionalBodice(), middleSpencer(), middlePaddedJacket(),
                middleDoubletV881(), middleCardiganV881(), middleKnittedJerseyV881(), middleGambesonV881());
    }



    /** MIDDLE CHEST es una única ranura lógica; LIGHT, MEDIUM y HEAVY son alternativas excluyentes. */
    public static List<ArmorPiece> allMiddleChest() {
        java.util.ArrayList<ArmorPiece> result=new java.util.ArrayList<>(allMiddleChestGarments());
        result.addAll(allProtectiveMiddleChest());
        return java.util.List.copyOf(result);
    }

    // ---  | OUTER CHEST -------------------------------------------------
    private static ArmorPiece outerChestTextile(String name, String narrative, double weight,
                                                  Map<BodyArmorRegion, Double> coverage,
                                                  ArmorProtectionProfile declaredProtection, int effectiveClothLayers) {
        var dimensions = ArmorPhysicalDimensionsCatalog.outerChestDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.outerChestFootprintFor(name);
        TextileAssemblyProfile construction;
        if (declaredProtection.equals(new ArmorProtectionProfile(3,7,3)) && effectiveClothLayers == 1) {
            construction = TextileAssemblyProfile.denseCloth();
        } else {
            construction = TextileAssemblyProfile.ordinary(effectiveClothLayers);
            if (!construction.protection().equals(declaredProtection)) {
                throw new IllegalArgumentException("Perfil OUTER CHEST incoherente con sus capas: " + name);
            }
        }
        return new ArmorPiece(name, narrative, weight, footprint, ArmorInventoryCategory.CHEST, coverage,
                construction.protection(), ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("CONSTRUCCIÓN | " + construction.constructionLabel(),
                        "POSICIÓN | OUTER CHEST",
                        "COBERTURA | " + coverage.entrySet().stream()
                                .map(e -> e.getKey().name()+" "+Math.round(e.getValue()*100)+"%")
                                .collect(java.util.stream.Collectors.joining(" · ")),
                        "PROTECCIÓN | " + formatProtection(construction.protection()),
                        "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weight),
                        "DIMENSIONES PLEGADAS XYZ | " + dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " x " + footprint.horizontalSlots()), List.of());
    }

    public static ArmorPiece outerFrockCoatV881() { return outerChestTextile("Levita V881",
            "Abrigo sastreado de faldones largos cuyo cuerpo entallado estabiliza las prendas inferiores mientras las colas distribuyen el tejido sin cerrar la zancada. Solapas, vistas y forro refuerzan el frente y permiten mantener una silueta formal durante el uso prolongado.",
            1.70,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.10),new ArmorProtectionProfile(6,15,6),3); }
    public static ArmorPiece outerTailcoatV881() { return outerChestTextile("Frac V881",
            "Prenda exterior de frente recortado y faldones posteriores separados, construida para liberar la parte anterior de las piernas y conservar caída textil detrás del cuerpo. La concentración de paño en espalda y faldones modifica la distribución de masa sin añadir volumen sobre el abdomen.",
            1.25,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.05),new ArmorProtectionProfile(4,10,4),2); }
    public static ArmorPiece outerMorningCoatV881() { return outerChestTextile("Chaqué V881",
            "Abrigo sastreado cuyo delantero desciende de forma progresiva hacia faldones posteriores, evitando un borde horizontal rígido sobre la cadera. La geometría permite sentarse, caminar y montar sin acumular el frente de la prenda sobre los muslos.",
            1.35,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.05),new ArmorProtectionProfile(4,10,4),2); }
    public static ArmorPiece outerSackCoatV881() { return outerChestTextile("Americana V881",
            "Chaqueta exterior de cuerpo relativamente recto y construcción menos rígida que una levita. Los paneles delanteros y traseros cuelgan desde hombros con holgura suficiente para acompañar la flexión del torso y alojar prendas intermedias sin recurrir a faldones largos.",
            1.00,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10),new ArmorProtectionProfile(4,10,4),2); }
    public static ArmorPiece outerNorfolkV881() { return outerChestTextile("Chaqueta Norfolk V881",
            "Chaqueta exterior de actividad con pliegues verticales y cinturón integrado que controla el exceso de tejido sin inmovilizar hombros ni caja torácica. Los fuelles permiten elevar y proyectar los brazos manteniendo la prenda ceñida durante marcha, caza o trabajo de campo.",
            1.20,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10),new ArmorProtectionProfile(6,15,6),3); }
    public static ArmorPiece outerWorkSmockV881() { return outerChestTextile("Blusón de trabajo V881",
            "Blusón amplio de faena que interpone un paño reemplazable entre la ropa ordinaria y polvo, virutas, grasa o superficies abrasivas. Su holgura evita transmitir tirones directamente a las prendas interiores y permite remangar o ajustar las mangas según la tarea.",
            .65,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10),new ArmorProtectionProfile(3,7,3),1); }
    public static ArmorPiece outerGreatcoatV881() { return outerChestTextile("Gabán V881",
            "Abrigo largo de paño grueso concebido para encerrar varias capas de ropa bajo una envolvente continua. El doble espesor de vistas, solapas y cierres se combina con un faldón amplio que mantiene aislamiento térmico sin impedir una zancada completa.",
            2.80,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.15),new ArmorProtectionProfile(10,25,10),5); }
    public static ArmorPiece outerOvercoatV881() { return outerChestTextile("Sobretodo V881",
            "Prenda exterior de gran volumen diseñada para vestirse sobre chaquetas y prendas intermedias. El patronaje concede holgura en hombros y torso y utiliza forro y paño exterior para crear una cámara térmica continua alrededor del cuerpo.",
            2.35,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.15),new ArmorProtectionProfile(8,20,8),4); }
    public static ArmorPiece outerUlsterV881() { return outerChestTextile("Ulster V881",
            "Abrigo de viaje largo y holgado, construido para superponer ropa y mantener movilidad durante trayectos prolongados. Cuello alto, solapes amplios y faldones extensos reducen la exposición directa del torso y las piernas a viento y suciedad.",
            3.10,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.20),new ArmorProtectionProfile(12,30,12),6); }
    public static ArmorPiece outerDusterV881() { return outerChestTextile("Guardapolvo V881",
            "Sobretúnica larga de tejido ligero destinada a recibir polvo, barro seco y suciedad del camino antes que las prendas vestidas debajo. Su corte amplio y escasa estructura permiten sacudirla, lavarla y plegarla con facilidad tras el viaje.",
            .80,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.20),new ArmorProtectionProfile(2,5,2),1); }
    public static ArmorPiece outerTrenchV881() { return outerChestTextile("Gabardina V881",
            "Abrigo largo confeccionado con tejido densamente trabajado y cierres solapados para limitar la penetración directa de lluvia y viento sin sellar el cuerpo. Canesúes, cinturón y piezas de ajuste controlan la caída del paño mojado y evitan que toda la tensión recaiga sobre hombros y cuello.",
            1.65,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.15),new ArmorProtectionProfile(6,15,6),3); }
    public static ArmorPiece outerRidingJacketV881() { return outerChestTextile("Chaqueta de montar V881",
            "Chaqueta exterior ajustada para mantener el tejido fuera de riendas, cinchas y salientes de la montura. El corte libera la cadera y distribuye el movimiento entre espalda, hombros y mangas para conservar control fino del tren superior.",
            1.05,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10),new ArmorProtectionProfile(4,10,4),2); }
    public static ArmorPiece outerBoleroV881() { return outerChestTextile("Bolero V881",
            "Chaquetilla corta que concentra tejido en la parte alta del torso y brazos, dejando cintura y abdomen con mínima superposición exterior. Su patronaje reduce masa y faldones y convierte bordados, ribetes y cierres en parte visible de la estructura de la prenda.",
            .55,Map.of(BodyArmorRegion.CHEST,.35,BodyArmorRegion.BRACERS,.05),new ArmorProtectionProfile(2,5,2),1); }
    public static ArmorPiece outerKnightCloak() { return outerChestTextile("Capa del Caballero",
            "Capa de paño pesado fijada en torno a hombros mediante puntos de sujeción que reparten la tracción y permiten que el tejido caiga libremente detrás de brazos y espalda. Su construcción prioriza estabilidad de la prenda y porte ceremonial sin convertirla en parte de la armadura rígida.",
            2.00,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10),new ArmorProtectionProfile(6,15,6),3); }
    public static ArmorPiece outerTravelerCloak() { return outerChestTextile("Capa del Viajero V881",
            "Capa de viaje de corte amplio y confección deliberadamente sencilla, diseñada para caer sobre la indumentaria sin requerir una compatibilidad material especial. Aberturas laterales y sujeción estable en hombros permiten apartar el paño durante la marcha y plegarlo cuando deja de utilizarse.",
            1.20,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.10),new ArmorProtectionProfile(2,5,2),1); }
    public static ArmorPiece outerInvernessV881() { return outerChestTextile("Capa Inverness V881",
            "Prenda de viaje que combina un cuerpo de abrigo sin mangas convencionales con una capa corta superpuesta sobre hombros y brazos. La doble geometría protege la abertura frontal y mantiene libertad de movimiento al separar la caída exterior del cuerpo principal.",
            2.10,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.15),new ArmorProtectionProfile(8,20,8),4); }
    public static ArmorPiece outerPonchoV881() { return outerChestTextile("Poncho V881",
            "Paño continuo con abertura central para la cabeza que descansa sobre hombros sin mangas ni costuras longitudinales complejas. Su geometría reparte el material a ambos lados del cuerpo y permite girarlo, recogerlo o levantar sus bordes sin alterar una estructura entallada.",
            1.10,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.10),new ArmorProtectionProfile(2,5,2),1); }
    public static ArmorPiece outerBurnousV881() { return outerChestTextile("Burnús V881",
            "Manto largo y envolvente de tradición norteafricana cuya gran superficie textil cae desde hombros y puede incorporar capucha sin depender de una sastrería rígida. El volumen se distribuye alrededor del cuerpo y permite variar la envoltura según viento, marcha y postura.",
            1.65,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.10,BodyArmorRegion.LEGGINGS,.15),new ArmorProtectionProfile(4,10,4),2); }
    public static ArmorPiece outerDolmanV881() { return outerChestTextile("Dolman V881",
            "Prenda exterior entallada cuyo cuerpo y mangas se integran mediante costuras curvadas y abundante trabajo de cierre y ornamentación. La continuidad del patronaje mantiene una cobertura muy amplia de brazos sin recurrir a piezas defensivas independientes.",
            1.30,Map.of(BodyArmorRegion.CHEST,.50,BodyArmorRegion.BRACERS,.15),new ArmorProtectionProfile(6,15,6),3); }
    public static ArmorPiece outerManteletV881() { return outerChestTextile("Manteleta V881",
            "Prenda corta envolvente que cubre hombros, parte alta del torso y brazos mediante paneles de caída libre sujetos cerca del cuello. Su escasa longitud evita interferir con cintura y piernas y concentra el volumen textil donde una prenda exterior ordinaria presenta más costuras y aberturas.",
            .75,Map.of(BodyArmorRegion.CHEST,.35,BodyArmorRegion.BRACERS,.10),new ArmorProtectionProfile(4,10,4),2); }

    public static List<ArmorPiece> allOuterChestGarments() {
        return List.of(outerFrockCoatV881(),outerTailcoatV881(),outerMorningCoatV881(),outerSackCoatV881(),outerNorfolkV881(),outerWorkSmockV881(),
                outerGreatcoatV881(),outerOvercoatV881(),outerUlsterV881(),outerDusterV881(),outerTrenchV881(),outerRidingJacketV881(),outerBoleroV881(),
                outerKnightCloak(),outerTravelerCloak(),outerInvernessV881(),outerPonchoV881(),outerBurnousV881(),outerDolmanV881(),outerManteletV881());
    }

    public static ArmorPiece hardenedLeatherJetHelmet() {
        ArmorProtectionProfile profile = ArmorProtectionCompositionPolicy.weightedMaterials(List.of(
                new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER, 0.50),
                new ArmorMaterialShare(ArmorMaterial.LAMINATED_GLASS, 0.50)));
        return new ArmorPiece(
                "Casco Jet de cuero endurecido con vidrio laminado V881",
                "Casco de cuero endurecido cuya visera segmentada de vidrio laminado protege los ojos sin encerrar por completo el rostro. La correa de sujeción mantiene estable el conjunto durante el movimiento.",
                1.000, ArmorPhysicalDimensionsCatalog.headFootprintFor("Casco Jet de cuero endurecido con vidrio laminado V881"), ArmorHitLocation.HEAD, 0.60, profile,
                ArmorMaterial.HARDENED_LEATHER, Set.of(ArmorMaterial.HARDENED_LEATHER, ArmorMaterial.LAMINATED_GLASS),
                ArmorForm.STANDARD,
                List.of("MATERIAL | CUERO ENDURECIDO 50% · VIDRIO LAMINADO 50%",
                        "PROPORCIÓN DEFENSIVA | Media ponderada al 50%",
                        "COBERTURA CORPORAL | 60% (cabeza)",
                        "PROTECCIÓN | " + formatProtection(profile)), List.of(), ArmorBlockCapability.NONE)
                .withHeadLayer(HeadLayer.TACTICAL);
    }

    private static ArmorPiece weightedLeatherBodyPiece(String name, double weightKg, ArmorInventoryCategory category,
                                                        Map<BodyArmorRegion, Double> coverage,
                                                        List<ArmorMaterialShare> shares, ArmorMaterial primary,
                                                        Set<ArmorMaterial> materials) {
        ArmorProtectionProfile profile = ArmorProtectionCompositionPolicy.weightedMaterials(shares);
        InventoryFootprint footprint = switch (category) {
            case BRACERS -> ArmorPhysicalDimensionsCatalog.bracersFootprintFor(name);
            case CHEST -> ArmorPhysicalDimensionsCatalog.mediumHeavyChestFootprintFor(name);
            case FEET -> ArmorPhysicalDimensionsCatalog.outerFeetFootprintFor(name);
            case LEGGINGS -> ArmorPhysicalDimensionsCatalog.outerLeggingsFootprintFor(name);
            default -> leatherFootprint(name);
        };
        return new ArmorPiece(name,
                leatherNarrative(name),
                weightKg, footprint, category, coverage, profile, primary, materials, ArmorForm.STANDARD,
                List.of(
                        "COBERTURA | " + coverage.entrySet().stream().map(e -> e.getKey().name()+" "+Math.round(e.getValue()*100)+"%").collect(java.util.stream.Collectors.joining(" · ")),
                        "PROTECCIÓN | " + formatProtection(profile), "PESO SECO | " + String.format(Locale.ROOT, "%.3f kg", weightKg)), List.of());
    }

    private static InventoryFootprint leatherFootprint(String name) {
        if (name.contains("Guantes")) return new InventoryFootprint(2,1);
        if (name.contains("Oxford")) return new InventoryFootprint(3,2);
        if (name.contains("Botas altas")) return new InventoryFootprint(5,3);
        if (name.contains("Botas de trabajo")) return new InventoryFootprint(4,3);
        if (name.contains("Polainas")) return new InventoryFootprint(3,2);
        if (name.contains("Chaparreras")) return new InventoryFootprint(4,3);
        if (name.contains("Pantalón")) return new InventoryFootprint(4,3);
        if (name.contains("Delantal")) return new InventoryFootprint(4,3);
        if (name.contains("aviador")) return new InventoryFootprint(3,3);
        if (name.contains("Chaqueta")) return new InventoryFootprint(4,3);
        return new InventoryFootprint(3,2);
    }

    private static String leatherNarrative(String name) {
        if (name.equals("Chaqueta de Viaje V881")) return "Chaqueta de viaje de panelado mixto: paños textiles flexibles articulan el torso y los brazos mientras el cuero endurecido ocupa las superficies expuestas a abrasión y arrastre. Los cierres y ajustes mantienen los paneles próximos al cuerpo sin convertir la prenda en una coraza rígida.";
        if (name.contains("Guantes de Precisión")) return "Guantes ceñidos de cuero endurecido fino, cortados para conservar sensibilidad en palma y dedos. Las costuras se desplazan fuera de las zonas de pinza y flexión para no interferir con herramientas, gatillos ni mecanismos pequeños.";
        if (name.contains("dedos al aire")) return "Guantes cortos de cuero endurecido que protegen palma, dorso y muñeca dejando libres las falanges distales. La abertura de los dedos sacrifica aislamiento y protección terminal a cambio de contacto directo y mayor tactilidad.";
        if (name.contains("aviador")) return "Chaqueta corta de cuero flexible refinada para no acumular material alrededor de la cintura ni de los mandos. Puños, cuello y zonas elásticas descargan tensión de las costuras mientras el cuero concentra la resistencia superficial en torso y brazos.";
        if (name.contains("motorista")) return "Chaqueta cruzada de cuero grueso con doble solape frontal, cierres desplazados y paneles superpuestos en las zonas de impacto y arrastre. Su patronaje prioriza continuidad superficial y resistencia a la abrasión por encima de ligereza y flexibilidad.";
        if (name.contains("Delantal")) return "Delantal largo de taller en cuero endurecido, suspendido desde pecho y cintura para interponer una superficie sacrificial frente a chispas, aristas, virutas y rozamiento. Los bolsillos y herramientas acopladas son carga de trabajo y no forman parte de su barrera defensiva.";
        if (name.contains("correas y hebillas")) return "Polainas de cuero envolvente cerradas mediante correas y hebillas independientes. El ajuste permite adaptar la tensión a pantorrilla y tobillo, mientras el solape frontal evita presentar una única abertura continua al roce del terreno.";
        if (name.contains("rígidas de cierre lateral")) return "Polainas de cuero endurecido conformadas para mantener su sección alrededor de la pantorrilla y abrirse longitudinalmente por un lateral. La rigidez reduce pliegues y desplazamientos, concentrando la flexión en la línea de cierre.";
        if (name.contains("bordadas")) return "Polainas de tradición hispánica confeccionadas en cuero endurecido y rematadas con bordados y herrajes ornamentales. La decoración se dispone sobre el soporte sin sustituir la continuidad del cuero que recibe abrasión y enganches.";
        if (name.contains("shotgun")) return "Chaparreras cerradas de pernera estrecha que envuelven la pierna desde muslo hasta la parte inferior, siguiendo la tradición vaquera de protección continua frente a maleza, cuerda, montura y arrastre.";
        if (name.contains("batwing")) return "Chaparreras de ala ancha con grandes paneles laterales de cuero que cuelgan desde la cintura y permiten amplitud de rodilla y cadera. La superficie se concentra hacia el exterior de la pierna para combinar movilidad montada y protección frente a vegetación.";
        if (name.contains("charra")) return "Chaparreras de tradición charra y vaquera con cuero de cobertura amplia, refuerzos de borde y ornamentación integrada en la propia confección. Los motivos decorativos acompañan costuras y remates sin sustituir el soporte protector.";
        if (name.contains("Botas altas")) return "Botas altas de montar y campo con caña de cuero endurecido hasta la zona inferior de la pierna y suela de caucho vulcanizado. La caña estabiliza tobillo y pantorrilla frente a estribo, maleza y rozamiento mientras el pie conserva una base flexible.";
        if (name.contains("Botas de trabajo")) return "Botas de trabajo industrial construidas alrededor de una envolvente gruesa de cuero, suela de caucho vulcanizado y refuerzo metálico interno en la puntera. La estructura protege el pie frente a aplastamiento, abrasión y superficies de taller sin convertir el metal en una vía conductora hacia el usuario.";
        if (name.contains("Oxford")) return "Zapatos bajos de cuero de cordones cerrados, derivados de calzado de vestir y campo refinado. La pala ajustada, la menor altura y la suela relativamente compacta favorecen movilidad y presentación a costa de la robustez de una bota.";
        if (name.contains("Pantalón")) return "Pantalón de viaje de construcción híbrida con tejido flexible en las zonas que necesitan amplitud y cuero endurecido donde la pierna recibe abrasión, apoyo o contacto con el terreno. La articulación de rodilla evita que los refuerzos conviertan la prenda en una polaina rígida.";
        return "Prenda de cuero endurecido cuya confección distribuye material protector y zonas flexibles según la anatomía y el uso previsto.";
    }

    public static ArmorPiece hardenedLeatherChest() {
        // Chaqueta de Viaje V881: panelado observado en la prenda de referencia; cuero dominante con grandes paños textiles.
        return weightedLeatherBodyPiece("Chaqueta de Viaje V881", 2.800, ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,0.50, BodyArmorRegion.BRACERS,0.10),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.65), new ArmorMaterialShare(ArmorMaterial.CLOTH,0.35)),
                ArmorMaterial.HARDENED_LEATHER, Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }

    public static ArmorPiece hardenedLeatherBracers() {
        return weightedLeatherBodyPiece("Guantes de Precisión V881",0.160,ArmorInventoryCategory.BRACERS,
                Map.of(BodyArmorRegion.BRACERS,0.05),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.80),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.20)),
                ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }

    public static ArmorPiece hardenedLeatherLeggings() {
        return weightedLeatherBodyPiece("Pantalón de cuero endurecido V881",2.200,ArmorInventoryCategory.LEGGINGS,
                Map.of(BodyArmorRegion.LEGGINGS,0.30),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.60),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.40)),
                ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }

    public static ArmorPiece hardenedLeatherFingerlessGloves() {
        return weightedLeatherBodyPiece("Guantes de cuero endurecido con los dedos al aire V881",0.180,ArmorInventoryCategory.BRACERS,
                Map.of(BodyArmorRegion.BRACERS,0.05),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.85),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.15)),
                ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }

    public static ArmorPiece hardenedLeatherAviatorJacketV881() {
        return weightedLeatherBodyPiece("Chaqueta de Aeronauta V881",1.800,ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,0.50,BodyArmorRegion.BRACERS,0.10),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.75),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.25)),
                ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }

    public static ArmorPiece hardenedLeatherCrossedMotorcycleJacketV881() {
        return weightedLeatherBodyPiece("Chaqueta cruzada de motorista V881",3.400,ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,0.50,BodyArmorRegion.BRACERS,0.10),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.90),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.10)),
                ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }

    public static ArmorPiece workshopLeatherApronV881() {
        return weightedLeatherBodyPiece("Delantal de Taller V881",0.650,ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,0.25,BodyArmorRegion.LEGGINGS,0.10),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,1.0)), ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER));
    }

    public static ArmorPiece leatherStrapBuckleGaitersV881() {
        return weightedLeatherBodyPiece("Polainas de cuero con correas y hebillas V881",0.800,ArmorInventoryCategory.LEGGINGS,
                Map.of(BodyArmorRegion.LEGGINGS,0.12),List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.90),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.10)),
                ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }
    public static ArmorPiece leatherRigidSideClosureGaitersV881() {
        return weightedLeatherBodyPiece("Polainas rígidas de cierre lateral V881",1.100,ArmorInventoryCategory.LEGGINGS,
                Map.of(BodyArmorRegion.LEGGINGS,0.12),List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,1.0)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER));
    }
    public static ArmorPiece leatherOrnamentedHispanicGaitersV881() {
        return weightedLeatherBodyPiece("Polainas de cuero bordadas y ornamentadas V881",1.000,ArmorInventoryCategory.LEGGINGS,
                Map.of(BodyArmorRegion.LEGGINGS,0.12),List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.95),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.05)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }
    public static ArmorPiece leatherShotgunChapsV881() {
        return weightedLeatherBodyPiece("Chaparreras cerradas (shotgun) V881",2.200,ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,0.30),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,1.0)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER));
    }
    public static ArmorPiece leatherBatwingChapsV881() {
        return weightedLeatherBodyPiece("Chaparreras de ala ancha (batwing) V881",1.800,ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,0.30),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.90),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.10)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }
    public static ArmorPiece leatherCharroChapsV881() {
        return weightedLeatherBodyPiece("Chaparreras ornamentadas de tradición charra V881",2.500,ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,0.30),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.95),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.05)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH));
    }
    public static ArmorPiece leatherHighRidingBootsV881() {
        return weightedLeatherBodyPiece("Botas altas de montar y campo V881",1.600,ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,0.05,BodyArmorRegion.LEGGINGS,0.12),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.90),new ArmorMaterialShare(ArmorMaterial.VULCANIZED_RUBBER,0.10)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.VULCANIZED_RUBBER)).withFeetLayer(FeetLayer.OUTER);
    }
    public static ArmorPiece leatherHeavyWorkBootsV881() {
        // Puntera de acero: la clasificación emerge HEAVY, aunque la familia nazca en el catálogo de cuero.
        ArmorProtectionProfile profile=ArmorProtectionCompositionPolicy.weightedMaterials(List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.60),new ArmorMaterialShare(ArmorMaterial.VULCANIZED_RUBBER,0.25),new ArmorMaterialShare(ArmorMaterial.STEEL,0.15)));
        return new ArmorPiece("Botas de trabajo pesado e industria V881",leatherNarrative("Botas de trabajo pesado e industria V881"),1.800,ArmorPhysicalDimensionsCatalog.outerFeetFootprintFor("Botas de trabajo pesado e industria V881"),ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,0.05),profile,ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.VULCANIZED_RUBBER,ArmorMaterial.STEEL),ArmorForm.STANDARD,List.of("PROTECCIÓN | "+formatProtection(profile),"PESO SECO | 1.800 kg"),List.of()).withFeetLayer(FeetLayer.OUTER);
    }
    public static ArmorPiece leatherOxfordBrogueShoesV881() {
        return weightedLeatherBodyPiece("Zapatos Oxford/Brogue V881",0.550,ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,0.05),
                List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,0.65),new ArmorMaterialShare(ArmorMaterial.CLOTH,0.20),new ArmorMaterialShare(ArmorMaterial.VULCANIZED_RUBBER,0.15)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH,ArmorMaterial.VULCANIZED_RUBBER)).withFeetLayer(FeetLayer.OUTER);
    }


    //  — FEET LIGHT/OUTER: prendas interiores y calzado independiente.
    private static ArmorPiece innerFeetTextile(String name, String narrative, double weightKg,
                                                ArmorProtectionProfile protection) {
        var dimensions = ArmorPhysicalDimensionsCatalog.innerFeetDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.innerFeetFootprintFor(name);
        return new ArmorPiece(name,narrative,weightKg,footprint,ArmorInventoryCategory.FEET,
                Map.of(BodyArmorRegion.FEET,0.05),protection,ArmorMaterial.CLOTH,Set.of(ArmorMaterial.CLOTH),ArmorForm.STANDARD,
                List.of("MATERIAL | TELA","PROTECCIÓN | "+formatProtection(protection),
                        "DIMENSIONES PLEGADAS XYZ | "+dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | "+footprint.verticalSlots()+" x "+footprint.horizontalSlots()),List.of()).withFeetLayer(FeetLayer.INNER);
    }
    public static ArmorPiece innerFeetSocksV881() { return innerFeetTextile("Calcetines V881","Par de calcetines de punto fino construido para envolver pie y tobillo sin costuras voluminosas bajo el calzado. El tejido acompaña la flexión de los dedos y separa la piel de suelas y palas más abrasivas.",.09,new ArmorProtectionProfile(2,5,2)); }
    public static ArmorPiece innerFeetHeavyWorkSocksV881() { return innerFeetTextile("Calcetines gruesos de trabajo V881","Calcetines de punto denso con refuerzo localizado en talón y antepié. La mayor cantidad de hilo amortigua rozamiento repetido y distribuye humedad y presión dentro de una bota de uso prolongado.",.16,new ArmorProtectionProfile(4,10,4)); }
    public static ArmorPiece innerFeetStockingsV881() { return innerFeetTextile("Medias V881","Medias textiles de trama fina que ascienden desde los dedos por encima del tobillo y estabilizan su posición mediante tensión elástica y ligaduras. La superficie continua reduce fricción entre piel y calzado sin añadir una estructura rígida.",.11,new ArmorProtectionProfile(2,5,2)); }
    public static ArmorPiece innerFeetHighStockingsV881() { return innerFeetTextile("Medias altas V881","Medias de caña prolongada cuya construcción distribuye la tensión por pantorrilla además de envolver el pie. El tejido se mantiene ceñido para impedir pliegues internos cuando se usa con botas altas o prendas ecuestres.",.18,new ArmorProtectionProfile(2,5,2)); }
    public static ArmorPiece innerFeetHeavyKnitStockingsV881() { return innerFeetTextile("Medias de punto grueso V881","Medias confeccionadas con punto voluminoso y mayor cámara de aire entre fibras. La estructura incrementa aislamiento y amortiguación local sin convertir el pie en una pieza rígida.",.24,new ArmorProtectionProfile(4,10,4)); }
    public static ArmorPiece innerFeetWrapsV881() { return innerFeetTextile("Vendas de pie V881","Tiras textiles largas enrolladas alrededor de dedos, empeine y tobillo con tensión graduada. El usuario puede redistribuir el paño donde aparecen rozaduras y secarlo por separado sin depender de una prenda cosida con forma fija.",.12,new ArmorProtectionProfile(2,5,2)); }
    public static ArmorPiece innerFeetTextileSlippersV881() { return innerFeetTextile("Escarpines textiles V881","Escarpines blandos formados por varias piezas textiles cosidas con planta flexible y empeine cerrado. Funcionan como una envolvente interior completa del pie y añaden un segundo espesor de paño en las zonas de apoyo.",.20,new ArmorProtectionProfile(4,10,4)); }

    private static ArmorPiece outerFeetTextile(String name,String narrative,double weightKg,ArmorProtectionProfile protection) {
        var dimensions = ArmorPhysicalDimensionsCatalog.outerFeetDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.outerFeetFootprintFor(name);
        return new ArmorPiece(name,narrative,weightKg,footprint,ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,0.05),protection,
                ArmorMaterial.CLOTH,Set.of(ArmorMaterial.CLOTH),ArmorForm.STANDARD,
                List.of("MATERIAL | TELA","PROTECCIÓN | "+formatProtection(protection),
                        "DIMENSIONES XYZ DEL PAR | "+dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | "+footprint.verticalSlots()+" x "+footprint.horizontalSlots()),List.of()).withFeetLayer(FeetLayer.OUTER);
    }
    private static ArmorPiece outerFeetLeather(String name,String narrative,double weightKg,
                                                List<ArmorMaterialShare> shares, ArmorMaterial primary, Set<ArmorMaterial> materials) {
        ArmorProtectionProfile profile=ArmorProtectionCompositionPolicy.weightedMaterials(shares);
        var dimensions = ArmorPhysicalDimensionsCatalog.outerFeetDimensionsFor(name);
        var footprint = ArmorPhysicalDimensionsCatalog.outerFeetFootprintFor(name);
        return new ArmorPiece(name,narrative,weightKg,footprint,ArmorInventoryCategory.FEET,Map.of(BodyArmorRegion.FEET,0.05),profile,
                primary,materials,ArmorForm.STANDARD,
                List.of("PROTECCIÓN | "+formatProtection(profile),
                        "DIMENSIONES XYZ DEL PAR | "+dimensions.xSlots()+" x "+dimensions.ySlots()+" x "+dimensions.zSlots()+" slots físicos",
                        "TAMAÑO DE INVENTARIO | "+footprint.verticalSlots()+" x "+footprint.horizontalSlots()),List.of()).withFeetLayer(FeetLayer.OUTER);
    }
    public static ArmorPiece outerEspadrillesV881() { return outerFeetTextile("Alpargatas V881","Calzado ligero construido alrededor de una pala textil flexible y una base trenzada comprimida. La ausencia de una carcasa rígida permite plegar parcialmente el empeine y mantiene el contacto del pie próximo al terreno.",.32,new ArmorProtectionProfile(4,10,4)); }
    public static ArmorPiece outerCanvasShoesV881() { return outerFeetTextile("Zapatillas de lona V881","Zapatos de lona de corte bajo con pala cosida a una base flexible y cordones para ajustar el empeine. La geometría ligera conserva movilidad del pie y evita el volumen de una bota de cuero.",.42,new ArmorProtectionProfile(4,10,4)); }
    public static ArmorPiece outerLeatherWorkShoesV881() { return outerFeetLeather("Zapatos de trabajo de cuero V881","Zapatos bajos de cuero endurecido con pala reforzada, talón cerrado y suela resistente al uso prolongado. El patronaje privilegia abrasión y estabilidad sobre la finura de un zapato de vestir.",.78,List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,.80),new ArmorMaterialShare(ArmorMaterial.VULCANIZED_RUBBER,.20)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.VULCANIZED_RUBBER)); }
    public static ArmorPiece outerLeatherAnkleBootsV881() { return outerFeetLeather("Botines de cuero V881","Botines de cuero endurecido cuya caña termina alrededor del tobillo. El cierre estabiliza la articulación sin ocupar la pantorrilla y la suela mantiene una plataforma más robusta que la de un zapato bajo.",1.05,List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,.85),new ArmorMaterialShare(ArmorMaterial.VULCANIZED_RUBBER,.15)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.VULCANIZED_RUBBER)); }
    public static ArmorPiece outerShortFieldBootsV881() { return outerFeetLeather("Botas cortas de campo V881","Botas de cuero endurecido de caña corta preparadas para barro, maleza y marcha prolongada. La pala y el talón se construyen con paneles gruesos mientras la boca conserva flexibilidad suficiente para la zancada.",1.25,List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,.85),new ArmorMaterialShare(ArmorMaterial.VULCANIZED_RUBBER,.15)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.VULCANIZED_RUBBER)); }
    public static ArmorPiece outerCourtShoesV881() { return outerFeetLeather("Zapatos de salón V881","Calzado bajo de cuero de corte estrecho y empeine despejado, confeccionado para reducir volumen y conservar una silueta limpia. La construcción sacrifica espesor y sujeción frente a botas y zapatos de trabajo.",.44,List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,.70),new ArmorMaterialShare(ArmorMaterial.CLOTH,.30)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH)); }
    public static ArmorPiece outerMoccasinsV881() { return outerFeetLeather("Mocasines V881","Calzado de cuero flexible construido envolviendo el pie con pocas piezas y una costura elevada alrededor de la pala. La ausencia de una estructura pesada facilita flexión y percepción del terreno.",.52,List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,.80),new ArmorMaterialShare(ArmorMaterial.CLOTH,.20)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH)); }
    public static ArmorPiece outerBabouchesV881() { return outerFeetLeather("Babuchas V881","Calzado de cuero blando y perfil bajo, con talón flexible y pala cerrada que puede adaptarse a distintas posturas del pie. Su construcción minimiza herrajes y rigidez, favoreciendo desplazamiento silencioso y plegado parcial.",.46,List.of(new ArmorMaterialShare(ArmorMaterial.HARDENED_LEATHER,.75),new ArmorMaterialShare(ArmorMaterial.CLOTH,.25)),ArmorMaterial.HARDENED_LEATHER,Set.of(ArmorMaterial.HARDENED_LEATHER,ArmorMaterial.CLOTH)); }
    public static List<ArmorPiece> allInnerFeetGarments() { return List.of(innerFeetSocksV881(),innerFeetHeavyWorkSocksV881(),innerFeetStockingsV881(),innerFeetHighStockingsV881(),innerFeetHeavyKnitStockingsV881(),innerFeetWrapsV881(),innerFeetTextileSlippersV881()); }
    public static List<ArmorPiece> allOuterFeetGarments() { return List.of(outerEspadrillesV881(),outerCanvasShoesV881(),outerLeatherWorkShoesV881(),outerLeatherAnkleBootsV881(),outerShortFieldBootsV881(),leatherHighRidingBootsV881(),leatherHeavyWorkBootsV881(),leatherOxfordBrogueShoesV881(),outerCourtShoesV881(),outerMoccasinsV881(),outerBabouchesV881()); }

    public static ArmorPiece workshopGoggles() {
        ArmorProtectionProfile profile=ArmorMaterial.LAMINATED_GLASS.canonicalProtection();
        return new ArmorPiece("Gafas para soldadura V881",
                "Montura ocular de vidrio laminado con protección lateral y filtros abatibles para chispas, polvo y radiación visible intensa. La geometría se concentra alrededor de las órbitas y deja libre el resto de la cabeza.",
                0.070,ArmorPhysicalDimensionsCatalog.headFootprintFor("Gafas para soldadura V881"),ArmorHitLocation.HEAD,0.05,profile,ArmorMaterial.LAMINATED_GLASS,ArmorForm.STANDARD,
                List.of("MATERIAL | VIDRIO LAMINADO","COBERTURA | CABEZA 5%","PROTECCIÓN | "+formatProtection(profile)),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.EYEWEAR,"GAFAS","Óptica llevada directamente sobre la región ocular.","RANURA | TACTICAL HEAD")))
                .withHeadLayer(HeadLayer.TACTICAL);
    }


    public static ArmorPiece workshopBracers() {
        return layeredRegionalPiece("Guantes de Taller V881",
                "Guantes de cuero endurecido destinados a proteger manos y muñecas frente a cortes, abrasión, calor y químicos leves.",
                0.200, ArmorPhysicalDimensionsCatalog.bracersFootprintFor("Guantes de Taller V881"), ArmorInventoryCategory.BRACERS,
                Map.of(BodyArmorRegion.BRACERS, 0.05),
                List.of(new ArmorMaterialLayer(ArmorMaterial.HARDENED_LEATHER, 1)));
    }


    public static ArmorPiece paddedCoif() {
        ArmorProtectionProfile profile=new ArmorProtectionProfile(10,25,10);
        return new ArmorPiece("Cofia acolchada V881",
                "Cofia de paños cosidos alrededor de un relleno distribuido que envuelve cuero cabelludo, sienes, orejas y nuca. La retícula de costuras evita que el acolchado migre y ofrece una interfaz estable bajo cascos compatibles.",
                0.330,ArmorPhysicalDimensionsCatalog.headFootprintFor("Cofia acolchada V881"),ArmorHitLocation.HEAD,0.50,profile,ArmorMaterial.CLOTH,ArmorForm.STANDARD,
                List.of("MATERIAL | TELA ACOLCHADA","COBERTURA | CABEZA 50%","PROTECCIÓN | "+formatProtection(profile)),List.of())
                .withHeadLayer(HeadLayer.TACTICAL);
    }

    public static ArmorPiece paddedGambeson() {
        return layeredRegionalPiece("Gambesón V881",
                "Evolución multicapa del gambesón que combina densidades de fibras para distribuir y disipar la energía de golpes, abrasión y fragmentos sin sacrificar la movilidad.",
                4.200, new InventoryFootprint(4, 2), ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST, 0.50, BodyArmorRegion.BRACERS, 0.10),
                List.of(new ArmorMaterialLayer(ArmorMaterial.CLOTH, 18)));
    }








    public static ArmorPiece integralRespirator() {
        ArmorProtectionProfile profile=ArmorProtectionCompositionPolicy.weightedMaterials(List.of(
                new ArmorMaterialShare(ArmorMaterial.RUBBER,.55),new ArmorMaterialShare(ArmorMaterial.LAMINATED_GLASS,.15),
                new ArmorMaterialShare(ArmorMaterial.STEEL,.25),new ArmorMaterialShare(ArmorMaterial.CLOTH,.05)));
        return new ArmorPiece("Respirador Integral V881",
                "Respirador de máscara facial completa cuyo módulo filtrante y correaje posterior se fijan a una envolvente de tela tratada que continúa alrededor del cráneo, incluida coronilla y nuca. El visor segmentado de vidrio laminado es sustituible y el soporte textil reparte la tensión del sellado; su 100% de cobertura HEAD describe continuidad geométrica de la envolvente, no uniformidad material ni un perfil de casco rígido.",
                1.350,ArmorPhysicalDimensionsCatalog.headFootprintFor("Respirador Integral V881"),ArmorHitLocation.HEAD,1.00,profile,ArmorMaterial.RUBBER,
                Set.of(ArmorMaterial.RUBBER,ArmorMaterial.LAMINATED_GLASS,ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,
                List.of("MATERIAL | CAUCHO · VIDRIO LAMINADO · ACERO · TELA","COBERTURA | CABEZA 100% · envolvente continua, composición heterogénea","PROTECCIÓN | "+formatProtection(profile)),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.ASSISTED_FILTER,"FILTRO ASISTIDO","El módulo filtrante asistido aísla las vías respiratorias frente a agentes virulentos y reduce la agresión térmica asfixiante.","INMUNIDAD | Toxicidad Virulenta · QUEMADURA ASFIXIANTE | Daño x0,5")),ArmorBlockCapability.NONE)
                .withHeadLayer(HeadLayer.TACTICAL);
    }

    public static ArmorPiece fireproofSuit() {
        ArmorProtectionProfile profile = ArmorProtectionCompositionPolicy.additiveLayers(List.of(
                new ArmorMaterialLayer(ArmorMaterial.MINERAL_MULTILAYER_FABRIC, 1),
                new ArmorMaterialLayer(ArmorMaterial.CLOTH, 1)));
        return new ArmorPiece("Mono Ignífugo V881",
                "El fuego no necesita atravesar una tela para matar; le basta con conseguir que el otro lado se caliente. Este mono se construye como una envolvente térmica continua: una superficie mineral refractaria recibe llama, radiación y partículas incandescentes; debajo, separadores textiles conservan cámaras de aire inmovilizadas y obligan al flujo térmico a cruzar sucesivas interfaces antes de alcanzar el forro interior. Muñecas, cintura, entrepierna y tobillos se resuelven mediante fuelles solapados para que la movilidad no abra una ruta directa hacia la piel. El calzado forma parte de la misma prenda y las uniones exteriores se solapan para expulsar líquidos hacia fuera. Mientras el conjunto permanezca estructuralmente operativo, ni la llama ni el agua consiguen convertir el estado exterior en el estado del cuerpo contenido dentro.",
                6.000, ArmorPhysicalDimensionsCatalog.technicalSuitFootprintFor("Mono Ignífugo V881"), 1.00, 0.0, profile,
                ArmorMaterial.MINERAL_MULTILAYER_FABRIC,
                Set.of(ArmorMaterial.MINERAL_MULTILAYER_FABRIC, ArmorMaterial.CLOTH), ArmorForm.INTEGRAL_SUIT,
                List.of("MATERIAL | TEJIDO MINERAL MULTICAPA x1 · TELA x1",
                        "ARQUITECTURA | Superficie refractaria · cámaras de aire estabilizadas · forro interior",
                        "COBERTURA CORPORAL | 100% BODY · FEET integrado",
                        "PROTECCIÓN | " + formatProtection(profile),
                        "PESO | 6.000 kg",
                        "DIMENSIONES XYZ | " + ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Mono Ignífugo V881").xSlots()+" x "+ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Mono Ignífugo V881").ySlots()+" x "+ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Mono Ignífugo V881").zSlots()+" slots físicos"),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.ONE_PIECE_SUIT, "MONO DE UNA PIEZA", "La envolvente corporal constituye una sola prenda técnica y sólo admite capas INNER debajo.", "BODY | Bloquea MIDDLE/OUTER adicionales"),
                        ItemProperty.alwaysActive(ItemPropertyId.INTEGRATED_FOOTWEAR, "CALZADO INTEGRADO", "La envolvente continúa hasta los pies sin una abertura funcional independiente.", "FEET | Ocupado por el mono"),
                        ItemProperty.alwaysActive(ItemPropertyId.INTEGRAL_WATERPROOF, "IMPERMEABILIDAD INTEGRAL", "Solapes, fuelles y cierres mantienen el agua fuera de la envolvente mientras la pieza permanezca operativa.", "INMUNIDAD | Empapado"),
                        ItemProperty.alwaysActive(ItemPropertyId.FIREPROOF, "IGNÍFUGO", "La arquitectura mineral multicapa y sus cámaras estabilizadas impiden que el daño térmico representado como Quemadura alcance al usuario mientras el conjunto conserve su integridad.", "INMUNIDAD | Quemadura")));
    }

    public static ArmorPiece insulatingSuit() {
        ArmorProtectionProfile profile = ArmorProtectionCompositionPolicy.additiveLayers(List.of(
                new ArmorMaterialLayer(ArmorMaterial.VULCANIZED_RUBBER, 2),
                new ArmorMaterialLayer(ArmorMaterial.DIELECTRIC_CLOTH, 2)));
        return new ArmorPiece("Mono Aislante V881",
                "No intenta conducir la corriente ni vencerla: procura que nunca encuentre al hombre que hay dentro. Dos membranas continuas de caucho vulcanizado envuelven el cuerpo entre estratos de tejido dieléctrico; las juntas se solapan, los cierres quedan enterrados bajo faldones aislantes y el calzado nace de la misma envolvente para evitar una discontinuidad en tobillos y plantas. El tejido interior impide que el caucho trabaje directamente contra la piel y distribuye flexión y abrasión para que una articulación repetida no se convierta en una perforación eléctrica. La superficie exterior puede cubrirse de lluvia, barro o agua sin crear por sí misma una ruta hasta el cuerpo. Mientras la continuidad del conjunto permanezca operativa, agua y diferencia de potencial terminan exactamente en el mismo lugar: fuera.",
                6.000, ArmorPhysicalDimensionsCatalog.technicalSuitFootprintFor("Mono Aislante V881"), 1.00, 0.0, profile,
                ArmorMaterial.VULCANIZED_RUBBER,
                Set.of(ArmorMaterial.VULCANIZED_RUBBER, ArmorMaterial.DIELECTRIC_CLOTH), ArmorForm.INTEGRAL_SUIT,
                List.of("MATERIAL | CAUCHO VULCANIZADO x2 · TELA DIELÉCTRICA x2",
                        "ARQUITECTURA | Doble membrana dieléctrica continua · juntas solapadas · cierre protegido",
                        "COBERTURA CORPORAL | 100% BODY · FEET integrado",
                        "PROTECCIÓN | " + formatProtection(profile),
                        "PESO | 6.000 kg",
                        "DIMENSIONES XYZ | " + ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Mono Aislante V881").xSlots()+" x "+ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Mono Aislante V881").ySlots()+" x "+ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Mono Aislante V881").zSlots()+" slots físicos"),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.ONE_PIECE_SUIT, "MONO DE UNA PIEZA", "La envolvente corporal constituye una sola prenda técnica y sólo admite capas INNER debajo.", "BODY | Bloquea MIDDLE/OUTER adicionales"),
                        ItemProperty.alwaysActive(ItemPropertyId.INTEGRATED_FOOTWEAR, "CALZADO INTEGRADO", "El aislamiento continúa hasta los pies como parte inseparable de la envolvente.", "FEET | Ocupado por el mono"),
                        ItemProperty.alwaysActive(ItemPropertyId.INTEGRAL_WATERPROOF, "IMPERMEABILIDAD INTEGRAL", "La doble membrana y sus cierres impiden que EMPAPADO se consolide en el cuerpo mientras la pieza permanezca operativa.", "INMUNIDAD | Empapado"),
                        ItemProperty.alwaysActive(ItemPropertyId.INSULATING, "AISLANTE", "La continuidad dieléctrica de caucho vulcanizado y tejido aislante interrumpe la ruta eléctrica hacia el usuario mientras el conjunto permanezca operativo.", "INMUNIDAD | Electricidad")));
    }


    //  — Conjunto de Papel V881. El perfil base PAPER=1/3/1 describe el pliego;
    // estas piezas publican el resultado efectivo de manufacturas multicapa históricamente documentadas.
    private static List<ItemProperty> paperArmorFinishes() {
        return List.of(
                ItemProperty.alwaysActive(ItemPropertyId.VARNISHED, "BARNIZADO",
                        "El sellado superficial estabiliza las fibras y evita que la saturación por agua deshaga la estructura defensiva; no elimina la fragilidad mecánica del papel seco.",
                        "EMPAPADO | Evita fallo estructural · WET | Permitido"),
                ItemProperty.alwaysActive(ItemPropertyId.LACQUERED, "LACADO",
                        "Capas finas de laca sellan eléctricamente la superficie del paquete sin impedir que éste retenga agua en profundidad.",
                        "WET | Conserva AISLANTE ELÉCTRICO")
        );
    }

    public static ArmorPiece paperHelmetV881() {
        ArmorProtectionProfile profile = new ArmorProtectionProfile(60, 78, 22);
        return new ArmorPiece(
                "Casco de Papel V881",
                "Casquete ligero inspirado en protecciones históricas de papel moldeado y fibras vegetales. El cuerpo no utiliza una simple hoja: superpone papel grueso martillado, prensado y compactado sobre molde alrededor de un forro textil, creando un paquete estructural cuyo comportamiento pertenece al conjunto multicapa y no al papel canónico aislado. El barnizado conserva la cohesión de las fibras ante la lluvia y el lacado mantiene el aislamiento eléctrico incluso después de que el paquete haya absorbido agua.",
                0.500, ArmorPhysicalDimensionsCatalog.headFootprintFor("Casco de Papel V881"), ArmorHitLocation.HEAD, 1.00, profile,
                ArmorMaterial.PAPER, Set.of(ArmorMaterial.PAPER, ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of(
                        "MATERIAL | PAPEL MARTILLADO Y PRENSADO · TELA INTERIOR",
                        "MANUFACTURA | Casquete moldeado multicapa; acabado BARNIZADO + LACADO","PERFIL | Propiedad emergente del paquete prensado; no equivale al perfil del papel suelto",
                        "COBERTURA CORPORAL | CABEZA 100%",
                        "PROTECCIÓN | " + formatProtection(profile),
                        "PESO SECO (kg) | 0.500 · WET | 1.250"),
                paperArmorFinishes(), ArmorBlockCapability.NONE).withHeadLayer(HeadLayer.TACTICAL);
    }

    public static ArmorPiece paperChestV881() {
        // 45% del cuerpo recibe el paquete papel/calico/algodón; el 5% restante conserva sólo el soporte textil.
        ArmorProtectionProfile armoredZone = new ArmorProtectionProfile(88, 100, 38);
        ArmorProtectionProfile textileOnly = new ArmorProtectionProfile(6, 15, 6);
        ArmorProtectionProfile profile = ArmorProtectionCompositionPolicy.weightedCoveredZones(List.of(
                new ArmorProtectionCompositionPolicy.CoveredZoneProtection(0.45, armoredZone),
                new ArmorProtectionCompositionPolicy.CoveredZoneProtection(0.05, textileOnly)));
        return new ArmorPiece(
                "Coraza de Papel V881",
                "Coraza hasta la cintura derivada de la tradición descrita en el Wubeizhi y de las armaduras meridionales del siglo XIX. Alterna numerosos espesores de papel y calicó, incorpora un acolchado interior de algodón y pliega el paquete para obligar a flechas, hojas y proyectiles lentos a atravesar sucesivas redes fibrosas independientes. La construcción retiene el principio histórico de las treinta capas alternas citadas por Bedloe, pero concentra el mayor espesor sobre el torso y conserva una franja textil flexible en las uniones para no inmovilizar hombros y cintura.",
                2.500, ArmorPhysicalDimensionsCatalog.mediumHeavyChestFootprintFor("Coraza de Papel V881"), ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST, 0.50), profile, ArmorMaterial.PAPER,
                Set.of(ArmorMaterial.PAPER, ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of(
                        "MATERIAL | PAPEL + CALICÓ/TEJIDO + ACOLCHADO DE ALGODÓN",
                        "MANUFACTURA | Paquete pleated multicapa; 45% BODY papel/textil reforzado + 5% BODY soporte textil",
                        "COBERTURA CORPORAL | CORAZA 50%",
                        "PROTECCIÓN GLOBAL PONDERADA | " + formatProtection(profile),
                        "PESO SECO (kg) | 2.500 · WET | 6.250"),
                paperArmorFinishes());
    }

    public static ArmorPiece paperBracersV881() {
        // El Wubeizhi describe expresamente guardabrazos de capas de papel, seda y tela.
        ArmorProtectionProfile armoredZone = new ArmorProtectionProfile(80, 100, 30);
        ArmorProtectionProfile textileOnly = new ArmorProtectionProfile(4, 10, 4);
        ArmorProtectionProfile profile = ArmorProtectionCompositionPolicy.weightedCoveredZones(List.of(
                new ArmorProtectionCompositionPolicy.CoveredZoneProtection(0.12, armoredZone),
                new ArmorProtectionCompositionPolicy.CoveredZoneProtection(0.03, textileOnly)));
        return new ArmorPiece(
                "Brazales de Papel V881",
                "Guardabrazos basados directamente en la pieza de papel descrita por el Wubeizhi: hojas superpuestas entre seda y tela, comprimidas alrededor del antebrazo para conservar flexión de codo y muñeca. El paquete defensivo cubre la mayor parte de la región, mientras las zonas de articulación quedan en tejido multicapa para no rasgar el papel durante la pronación; barniz y laca protegen la cohesión y el aislamiento del conjunto.",
                0.700, ArmorPhysicalDimensionsCatalog.bracersFootprintFor("Brazales de Papel V881"), ArmorInventoryCategory.BRACERS,
                Map.of(BodyArmorRegion.BRACERS, 0.05), profile, ArmorMaterial.PAPER,
                Set.of(ArmorMaterial.PAPER, ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of(
                        "MATERIAL | PAPEL + SEDA/TELA",
                        "MANUFACTURA | 12% BODY paquete multicapa + 3% BODY articulación textil",
                        "COBERTURA CORPORAL | BRAZALES 5%",
                        "PROTECCIÓN GLOBAL PONDERADA | " + formatProtection(profile),
                        "PESO SECO (kg) | 0.700 · WET | 1.750"),
                paperArmorFinishes());
    }

    public static ArmorPiece paperLeggingsV881() {
        // Las variantes tardías utilizaron papel de corteza en 30–60 hojas; otras fuentes describen papel grueso
        // martillado hasta volverlo flexible y fijado con elementos discretos. Aquí se concentra esa solución en piernas.
        ArmorProtectionProfile armoredZone = new ArmorProtectionProfile(82, 95, 32);
        ArmorProtectionProfile textileOnly = new ArmorProtectionProfile(4, 10, 4);
        ArmorProtectionProfile profile = ArmorProtectionCompositionPolicy.weightedCoveredZones(List.of(
                new ArmorProtectionCompositionPolicy.CoveredZoneProtection(0.28, armoredZone),
                new ArmorProtectionCompositionPolicy.CoveredZoneProtection(0.07, textileOnly)));
        return new ArmorPiece(
                "Polainas de Papel V881",
                "Protección de muslos, rodillas y espinillas inspirada en las armaduras tardías de papel de corteza de treinta a sesenta hojas y en las variantes de papel grueso ablandado a martillo y fijado con elementos discretos. Los paneles densos ocupan las superficies expuestas; detrás de rodilla y en las transiciones articulares permanece una base textil que permite caminar, arrodillarse y montar sin plegar destructivamente el paquete principal.",
                1.300, ArmorPhysicalDimensionsCatalog.outerLeggingsFootprintFor("Polainas de Papel V881"), ArmorInventoryCategory.LEGGINGS,
                Map.of(BodyArmorRegion.LEGGINGS, 0.12), profile, ArmorMaterial.PAPER,
                Set.of(ArmorMaterial.PAPER, ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of(
                        "MATERIAL | PAPEL DE CORTEZA MULTICAPA + TELA",
                        "MANUFACTURA | 28% BODY panel martillado/compactado + 7% BODY articulación textil",
                        "COBERTURA CORPORAL | POLAINAS 12%",
                        "PROTECCIÓN GLOBAL PONDERADA | " + formatProtection(profile),
                        "PERFIL | Propiedad emergente del paquete multicapa; no equivale al papel suelto",
                        "PESO SECO (kg) | 1.300 · WET | 3.250",
                        "TAMAÑO XYZ | Par físico · " + ArmorPhysicalDimensionsCatalog.outerLeggingsDimensionsFor("Polainas de Papel V881").xSlots() + " x " + ArmorPhysicalDimensionsCatalog.outerLeggingsDimensionsFor("Polainas de Papel V881").ySlots() + " x " + ArmorPhysicalDimensionsCatalog.outerLeggingsDimensionsFor("Polainas de Papel V881").zSlots() + " slots físicos"),
                paperArmorFinishes());
    }


    /** Alias conservado para verificaciones históricas: remite al conjunto histórico. */
    public static ArmorPiece ebonyWarriorChest() { return historicalEbonyWarriorChest(); }
    /** Alias conservado para verificaciones históricas: remite al conjunto histórico. */

    public static ArmorPiece historicalEbonyWarriorChest() {
        return new ArmorPiece("Coraza del Guerrero de Ébano", EbonyWarriorLore.HISTORICAL_CHEST,
                10.530, ArmorPhysicalDimensionsCatalog.mediumHeavyChestFootprintFor("Coraza del Guerrero de Ébano"), ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,0.50), new ArmorProtectionProfile(75,55,60), ArmorMaterial.EBONY_WOOD,
                Set.of(ArmorMaterial.EBONY_WOOD,ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL | ÉBANO x15 · TELA x1","COBERTURA | CORAZA 50%","PROTECCIÓN | 75% / 55% / 60%"),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.FLAMMABLE,"INFLAMABLE","El ébano histórico conserva su matriz orgánica combustible.","DAÑO RECIBIDO | Quemadura x2")));
    }

    public static ArmorPiece historicalEbonyWarriorBracers() {
        return new ArmorPiece("Brazales del Guerrero de Ébano", EbonyWarriorLore.HISTORICAL_BRACERS, 1.053,
                ArmorPhysicalDimensionsCatalog.bracersFootprintFor("Brazales del Guerrero de Ébano"), ArmorInventoryCategory.BRACERS, Map.of(BodyArmorRegion.BRACERS,0.05),
                new ArmorProtectionProfile(75,55,60), ArmorMaterial.EBONY_WOOD, Set.of(ArmorMaterial.EBONY_WOOD,ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL | ÉBANO x15 · TELA x1","COBERTURA | BRAZALES 5%","PROTECCIÓN | 75% / 55% / 60%"), List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.FLAMMABLE,"INFLAMABLE","El ébano histórico conserva su matriz orgánica combustible.","DAÑO RECIBIDO | Quemadura x2"),
                        ItemProperty.alwaysActive(ItemPropertyId.IMPROVISED_SHIELD,"ESCUDO IMPROVISADO","Al alzarse, el brazal izquierdo añade una capa exterior de +2,5 pp exclusivamente a HEAD; conserva perfil y desgaste reales.","COBERTURA ALZADA | HEAD +2,5 pp · PARRY | Requiere DESVIAR")), ArmorBlockCapability.IMPROVISED_LEFT_BRACER);
    }

    public static ArmorPiece historicalEbonyWarriorLeggings() {
        return new ArmorPiece("Polainas del Guerrero de Ébano", EbonyWarriorLore.HISTORICAL_LEGGINGS, 2.527,
                ArmorPhysicalDimensionsCatalog.outerLeggingsFootprintFor("Polainas del Guerrero de Ébano"), ArmorInventoryCategory.LEGGINGS, Map.of(BodyArmorRegion.LEGGINGS,0.12),
                new ArmorProtectionProfile(75,55,60), ArmorMaterial.EBONY_WOOD, Set.of(ArmorMaterial.EBONY_WOOD,ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL | ÉBANO x15 · TELA x1","COBERTURA | POLAINAS 12%","PROTECCIÓN | 75% / 55% / 60%"),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.FLAMMABLE,"INFLAMABLE","El ébano histórico conserva su matriz orgánica combustible.","DAÑO RECIBIDO | Quemadura x2")));
    }

    public static ArmorPiece ebonyWarriorV881Chest() {
        ArmorProtectionProfile profile = new ArmorProtectionProfile(95,100,85);
        return new ArmorPiece("Coraza del Guerrero de Ébano V881", EbonyWarriorLore.V881_CHEST, EbonyArmorMassPolicy.v881ChestMassKg(), ArmorPhysicalDimensionsCatalog.mediumHeavyChestFootprintFor("Coraza del Guerrero de Ébano V881"), ArmorInventoryCategory.CHEST,
                Map.of(BodyArmorRegion.CHEST,0.50), profile, ArmorMaterial.MINERALIZED_EBONY,
                Set.of(ArmorMaterial.MINERALIZED_EBONY,ArmorMaterial.TUNGSTEN_PLATES_2_5_MM,ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL | ÉBANO MINERALIZADO x10 · WOLFRAMIO 2,5 mm · TELA x1","COBERTURA | CORAZA 50%","PROTECCIÓN | "+formatProtection(profile),"PESO DERIVADO | "+String.format(Locale.ROOT,"%.3f kg",EbonyArmorMassPolicy.v881ChestMassKg())),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.MINERALIZED_TUNGSTEN_ENCASEMENT,"ENCAPSULADO MINERALIZADO DE WOLFRAMIO","La mineralización estabiliza la matriz y el wolframio exterior la separa de la exposición térmica directa.","INFLAMABLE | Suprimido por ÉBANO MINERALIZADO + WOLFRAMIO 2,5 mm")));
    }

    public static ArmorPiece ebonyWarriorV881LeftBracer() {
        ArmorProtectionProfile profile = new ArmorProtectionProfile(95,100,85);
        return new ArmorPiece("Brazal izquierdo del Guerrero de Ébano V881", EbonyWarriorLore.V881_LEFT_BRACER, EbonyArmorMassPolicy.v881LeftBracerMassKg(), ArmorPhysicalDimensionsCatalog.bracersFootprintFor("Brazal izquierdo del Guerrero de Ébano V881"), ArmorInventoryCategory.BRACERS,
                Map.of(BodyArmorRegion.BRACERS,0.025), profile, ArmorMaterial.MINERALIZED_EBONY,
                Set.of(ArmorMaterial.MINERALIZED_EBONY,ArmorMaterial.TUNGSTEN_PLATES_2_5_MM,ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL | ÉBANO MINERALIZADO x10 · WOLFRAMIO 2,5 mm · TELA x1","COBERTURA | BRAZAL IZQUIERDO 2,5%","PROTECCIÓN | "+formatProtection(profile),"PESO DERIVADO | "+String.format(Locale.ROOT,"%.3f kg",EbonyArmorMassPolicy.v881LeftBracerMassKg())),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.MINERALIZED_TUNGSTEN_ENCASEMENT,"ENCAPSULADO MINERALIZADO DE WOLFRAMIO","La mineralización estabiliza el ébano y el wolframio exterior separa la matriz orgánica de la exposición térmica directa.","INFLAMABLE | Suprimido por ÉBANO MINERALIZADO + WOLFRAMIO 2,5 mm"),
                        ItemProperty.alwaysActive(ItemPropertyId.IMPROVISED_SHIELD,"ESCUDO IMPROVISADO","La geometría asimétrica permite alzarlo como capa exterior de +2,5 pp exclusivamente sobre HEAD, conservando material y desgaste.","COBERTURA ALZADA | HEAD +2,5 pp · PARRY | Requiere DESVIAR")), ArmorBlockCapability.IMPROVISED_LEFT_BRACER);
    }


    private static List<ItemProperty> historicalKnightConductorProperty() {
        return List.of(ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_CONDUCTOR,"CONDUCTOR ELÉCTRICO",
                "Las placas históricas de acero transmiten descargas y cada pieza equipada amplifica la exposición.",
                "DAÑO RECIBIDO | Electricidad x2 por pieza"));
    }

    public static ArmorPiece historicalKnightChest() { return new ArmorPiece("Coraza de Caballero", "Arnés histórico de placas de acero ajustadas sobre soporte textil y articuladas para repartir la masa entre torso, brazos y piernas. Las superficies rígidas se solapan en las transiciones para mantener continuidad defensiva; cada pieza conserva la vulnerabilidad conductora propia del acero.",11.250,ArmorPhysicalDimensionsCatalog.mediumHeavyChestFootprintFor("Coraza de Caballero"),ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,0.50),new ArmorProtectionProfile(85,100,75),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("MATERIAL | ACERO DE PLACAS · TELA","COBERTURA | CORAZA 50%","PROTECCIÓN | 85% / 100% / 75%","PESO | 11,250 kg"),historicalKnightConductorProperty()); }

    public static ArmorPiece historicalKnightBracers() { return new ArmorPiece("Brazales de Caballero", "Arnés histórico de placas de acero ajustadas sobre soporte textil y articuladas para repartir la masa entre torso, brazos y piernas. Las superficies rígidas se solapan en las transiciones para mantener continuidad defensiva; cada pieza conserva la vulnerabilidad conductora propia del acero.",3.375,ArmorPhysicalDimensionsCatalog.bracersFootprintFor("Brazales de Caballero"),ArmorInventoryCategory.BRACERS,Map.of(BodyArmorRegion.BRACERS,0.15),new ArmorProtectionProfile(85,100,75),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("MATERIAL | ACERO DE PLACAS · TELA","COBERTURA | BRAZALES 15%","PROTECCIÓN | 85% / 100% / 75%","PESO | 3,375 kg"),historicalKnightConductorProperty()); }

    public static ArmorPiece historicalKnightLeggings() { return new ArmorPiece("Polainas de Caballero", "Arnés histórico de placas de acero ajustadas sobre soporte textil y articuladas para repartir la masa entre torso, brazos, piernas y pies. Las grebas y quijotes se continúan con sabatones integrados; cada pieza conserva la vulnerabilidad conductora propia del acero.",7.875,ArmorPhysicalDimensionsCatalog.outerLeggingsFootprintFor("Polainas de Caballero"),ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,0.30, BodyArmorRegion.FEET,0.05),new ArmorProtectionProfile(85,100,75),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("MATERIAL | ACERO DE PLACAS · TELA","COBERTURA | POLAINAS 30% · FEET 5%","PROTECCIÓN | 85% / 100% / 75%","PESO | 7,875 kg"),java.util.stream.Stream.concat(historicalKnightConductorProperty().stream(), java.util.stream.Stream.of(ItemProperty.alwaysActive(ItemPropertyId.INTEGRATED_FOOTWEAR,"CALZADO INTEGRADO","Los sabatones forman parte de la propia protección de piernas.","FEET | Ocupado por la pieza"))).toList()); }

    public static ArmorPiece historicalKnightHelmet() { return new ArmorPiece("Casco de Caballero", "Casco cerrado histórico de acero independiente del arnés corporal, con calota, protección facial y articulaciones que distribuyen la carga alrededor del cráneo y permiten abrir o retirar los elementos móviles sin desmontar el resto del arnés.",2.500,ArmorPhysicalDimensionsCatalog.headFootprintFor("Casco de Caballero"),ArmorInventoryCategory.HEAD,ArmorHitLocation.HEAD,1.00,new ArmorProtectionProfile(85,100,75),ArmorMaterial.STEEL,ArmorForm.STANDARD,List.of("MATERIAL | ACERO DE PLACAS · FORRO TEXTIL","COBERTURA | CABEZA 100%","PROTECCIÓN | 85% / 100% / 75%","PESO | 2,500 kg"),historicalKnightConductorProperty()).withHeadLayer(HeadLayer.TACTICAL); }

    private static List<ItemProperty> knightV881ConductorProperty() {
        return List.of(ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_CONDUCTOR, "CONDUCTOR ELÉCTRICO",
                "La continuidad metálica de la aleación acero-wolframio transmite con facilidad una descarga; cada pieza aplicable conserva por separado la debilidad eléctrica propia del acero.",
                "ELECTRICIDAD | Vulnerabilidad x2 por pieza"));
    }

    public static ArmorPiece knightV881Chest() { return new ArmorPiece("Coraza de Caballero incluido hombro V881", "Arnés V881 de aleación acero-wolframio articulada sobre soporte textil. La revisión integral reduce discontinuidades, concentra material en las superficies expuestas y mantiene libre la cadena de contacto inferior para que el calzado elegido determine si el usuario puede derivar una descarga al terreno.",17.361,ArmorPhysicalDimensionsCatalog.mediumHeavyChestFootprintFor("Coraza de Caballero incluido hombro V881"),ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,0.50),new ArmorProtectionProfile(100,100,80),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.TUNGSTEN,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("MATERIAL | ALEACIÓN ACERO-WOLFRAMIO · TELA","COBERTURA | CORAZA 50%","PROTECCIÓN | 100% / 100% / 80%","PESO | 17,361 kg"),knightV881ConductorProperty()); }
    public static ArmorPiece knightV881Bracers() { return new ArmorPiece("Brazales de Caballero incluidos codera y nudillos V881", "Arnés V881 de aleación acero-wolframio articulada sobre soporte textil. El brazo queda protegido mediante placas escalonadas desde nudillos hasta codo, con articulaciones que preservan el gesto de interposición del brazal izquierdo sin aislar eléctricamente la estructura del resto del arnés.",3.472,ArmorPhysicalDimensionsCatalog.bracersFootprintFor("Brazales de Caballero incluidos codera y nudillos V881"),ArmorInventoryCategory.BRACERS,Map.of(BodyArmorRegion.BRACERS,0.10),new ArmorProtectionProfile(100,100,80),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.TUNGSTEN,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("MATERIAL | ALEACIÓN ACERO-WOLFRAMIO · TELA","COBERTURA | BRAZALES 10%","PROTECCIÓN | 100% / 100% / 80%","PESO | 3,472 kg"),List.of(ItemProperty.alwaysActive(ItemPropertyId.IMPROVISED_SHIELD,"ESCUDO IMPROVISADO","El brazal izquierdo reforzado se alza como capa exterior de +2,5 pp exclusivamente sobre HEAD; su protección y desgaste siguen siendo los del propio brazal.","COBERTURA ALZADA | HEAD +2,5 pp · PARRY | Requiere DESVIAR"),ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_CONDUCTOR,"CONDUCTOR ELÉCTRICO","La continuidad metálica transmite una descarga; el brazal conserva su debilidad material aunque exista contacto con el terreno.","ELECTRICIDAD | Vulnerabilidad x2 por pieza")),ArmorBlockCapability.IMPROVISED_LEFT_BRACER); }
    public static ArmorPiece knightV881Leggings() { return new ArmorPiece("Polainas de Caballero hasta las rodillas V881", "Grebas V881 de acero-wolframio articulado que terminan antes del pie. La ausencia deliberada de sabatón permite que FEET permanezca como decisión independiente: el calzado puede cerrar una vía de contacto con el terreno o, por el contrario, dejar al arnés metálico sin puesta a tierra.",4.167,ArmorPhysicalDimensionsCatalog.outerLeggingsFootprintFor("Polainas de Caballero hasta las rodillas V881"),ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,0.12),new ArmorProtectionProfile(100,100,80),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.TUNGSTEN,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("MATERIAL | ALEACIÓN ACERO-WOLFRAMIO · TELA","COBERTURA | POLAINAS 12%","PROTECCIÓN | 100% / 100% / 80%","PESO | 4,167 kg"),knightV881ConductorProperty()); }

    public static ArmorPiece historicalHeavyLamellarChest() { return new ArmorPiece("Coraza Lamelar Histórica Pesada","Armadura lamelar histórica pesada formada por numerosas láminas rígidas de acero perforadas y enlazadas en hileras solapadas. El solapamiento distribuye cortes y perforaciones sobre varias lamelas, mientras los cordajes permiten flexión entre filas; la masa elevada y la discontinuidad entre placas transmiten más impulso contundente que un arnés integral de placas.",22.388,ArmorPhysicalDimensionsCatalog.mediumHeavyChestFootprintFor("Coraza Lamelar Histórica Pesada"),ArmorInventoryCategory.CHEST,Map.of(BodyArmorRegion.CHEST,0.50),new ArmorProtectionProfile(95,95,50),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("CONSTRUCCIÓN | LAMELAR DE ACERO PESADA","COBERTURA | CORAZA 50%","PROTECCIÓN | 95% / 95% / 50%","PESO | 22,388 kg"),historicalKnightConductorProperty()); }
    public static ArmorPiece historicalHeavyLamellarBracers() { return new ArmorPiece("Brazales Lamelares Históricos Pesados","Armadura lamelar histórica pesada formada por numerosas láminas rígidas de acero perforadas y enlazadas en hileras solapadas. El solapamiento distribuye cortes y perforaciones sobre varias lamelas, mientras los cordajes permiten flexión entre filas; la masa elevada y la discontinuidad entre placas transmiten más impulso contundente que un arnés integral de placas.",2.239,ArmorPhysicalDimensionsCatalog.bracersFootprintFor("Brazales Lamelares Históricos Pesados"),ArmorInventoryCategory.BRACERS,Map.of(BodyArmorRegion.BRACERS,0.05),new ArmorProtectionProfile(95,95,50),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("CONSTRUCCIÓN | LAMELAR DE ACERO PESADA","COBERTURA | BRAZALES 5%","PROTECCIÓN | 95% / 95% / 50%","PESO | 2,239 kg"),historicalKnightConductorProperty()); }

    /** autoridad canónica completa de la ranura BRACERS. Una sola pieza puede ocuparla. */
    public static List<ArmorPiece> allBracers() {
        return List.of(
                hardenedLeatherBracers(),
                hardenedLeatherFingerlessGloves(),
                workshopBracers(),
                paperBracersV881(),
                historicalEbonyWarriorBracers(),
                ebonyWarriorV881LeftBracer(),
                historicalKnightBracers(),
                knightV881Bracers(),
                historicalHeavyLamellarBracers());
    }

    /** autoridad canónica completa de la ranura LEGGINGS, sin identidades CHEST multirregionales. */
    public static List<ArmorPiece> allLeggings() {
        return List.of(
                innerLongDrawersV881(),
                innerKneeDrawersV881(),
                innerKnittedTrousersV881(),
                innerWomensDrawersV881(),
                innerPetticoatV881(),
                innerReinforcedPetticoatV881(),
                innerPaddedPetticoatV881(),
                innerDividedPetticoatV881(),
                middleStraightTrousersV881(),
                middleFormalTrousersV881(),
                middleWorkTrousersV881(),
                middleHighWaistedTrousersV881(),
                middleLooseTrousersV881(),
                middleSailorTrousersV881(),
                middleRidingTrousersV881(),
                middleBreechesV881(),
                middleKnickerbockersV881(),
                middleBloomersV881(),
                middleStraightSkirtV881(),
                middleFullSkirtV881(),
                middleWalkingSkirtV881(),
                middleWorkSkirtV881(),
                middleRidingSkirtV881(),
                middleDividedSkirtV881(),
                middleOrnamentedSkirtV881(),
                middleOverskirtV881(),
                middleKiltV881(),
                middleSarongV881(),
                hardenedLeatherLeggings(),
                leatherStrapBuckleGaitersV881(),
                leatherRigidSideClosureGaitersV881(),
                leatherOrnamentedHispanicGaitersV881(),
                leatherShotgunChapsV881(),
                leatherBatwingChapsV881(),
                leatherCharroChapsV881(),
                paperLeggingsV881(),
                historicalEbonyWarriorLeggings(),
                historicalKnightLeggings(),
                knightV881Leggings(),
                historicalHeavyLamellarLeggings());
    }

    /** autoridad canónica completa de FEET. El peso y volumen de cada identidad representan el par completo. */
    public static List<ArmorPiece> allFeetArmor() {
        return List.of(
                innerFeetSocksV881(),
                innerFeetHeavyWorkSocksV881(),
                innerFeetStockingsV881(),
                innerFeetHighStockingsV881(),
                innerFeetHeavyKnitStockingsV881(),
                innerFeetWrapsV881(),
                innerFeetTextileSlippersV881(),
                outerEspadrillesV881(),
                outerCanvasShoesV881(),
                outerLeatherWorkShoesV881(),
                outerLeatherAnkleBootsV881(),
                outerShortFieldBootsV881(),
                leatherHighRidingBootsV881(),
                leatherHeavyWorkBootsV881(),
                leatherOxfordBrogueShoesV881(),
                outerCourtShoesV881(),
                outerMoccasinsV881(),
                outerBabouchesV881());
    }

    public static ArmorPiece historicalHeavyLamellarLeggings() { return new ArmorPiece("Polainas Lamelares Históricas Pesadas","Armadura lamelar histórica pesada formada por numerosas láminas rígidas de acero perforadas y enlazadas en hileras solapadas. El solapamiento distribuye cortes y perforaciones sobre varias lamelas, mientras los cordajes permiten flexión entre filas; la masa elevada y la discontinuidad entre placas transmiten más impulso contundente que un arnés integral de placas.",5.373,ArmorPhysicalDimensionsCatalog.outerLeggingsFootprintFor("Polainas Lamelares Históricas Pesadas"),ArmorInventoryCategory.LEGGINGS,Map.of(BodyArmorRegion.LEGGINGS,0.12),new ArmorProtectionProfile(95,95,50),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,List.of("CONSTRUCCIÓN | LAMELAR DE ACERO PESADA","COBERTURA | POLAINAS 12%","PROTECCIÓN | 95% / 95% / 50%","PESO | 5,373 kg"),historicalKnightConductorProperty()); }

    public static ArmorPiece retractableAeronautHelmet() {
        return new ArmorPiece("Casco Replegable del Aeronauta","Casco aerodinámico cuya estructura fija combina acero con cobre, mientras los módulos móviles emplean acero y magnetita y el visor tripartito utiliza vidrio laminado. Fuera de un encuentro hostil los módulos permanecen replegados para liberar visión, ventilación y movimiento; al comenzar un encuentro hostil el conjunto se despliega automáticamente y restituye la envolvente protectora integral, replegándose de nuevo al concluir. El intercom de corto alcance acopla su señal al terreno cuando el usuario dispone de una vía externa de toma a tierra mediante calzado compatible.",3.500,ArmorPhysicalDimensionsCatalog.headFootprintFor("Casco Replegable del Aeronauta"),ArmorHitLocation.HEAD,1.00,new ArmorProtectionProfile(85,100,80),ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.LAMINATED_GLASS),ArmorForm.STANDARD,List.of("ESTRUCTURA FIJA | ACERO + COBRE","MÓDULOS | ACERO + MAGNETITA","VISOR | VIDRIO LAMINADO TRIPARTITO","COBERTURA | CABEZA 100%","PROTECCIÓN | 85% / 100% / 80%","PESO | 3,500 kg","CONFIGURACIÓN | REPLEGADO fuera de encuentro hostil · DESPLEGADO durante encuentro hostil","INTERCOM | hasta 100 m; requiere terreno no asfaltado y ausencia de lluvia"),List.of(ItemProperty.alwaysActive(ItemPropertyId.RETRACTABLE,"REPLEGABLE","Los módulos superiores, laterales y visor cambian automáticamente de configuración según el estado del encuentro hostil; no exige una acción manual del usuario.","FUERA DE COMBATE | Replegado · ENCUENTRO HOSTIL | Desplegado"),ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_WEAKNESS_SUPPRESSED,"ARQUITECTURA ELÉCTRICA AISLADA","La estructura metálica no establece una continuidad conductora funcional con el usuario; por construcción, el acero del casco no aporta la debilidad eléctrica material ordinaria.","ELECTRICIDAD | No aporta vulnerabilidad x2"),ItemProperty.alwaysActive(ItemPropertyId.TERRESTRIAL_INTERCOM,"INTERCOM INALÁMBRICO","Comunicación por conducción terrestre e inducción magnética que requiere una toma a tierra externa y calzado equipado.","REQUISITOS | TOMA A TIERRA + FEET equipado + terreno no asfaltado + sin lluvia · ALCANCE | 100 m cuando existe acoplamiento")), ArmorBlockCapability.NONE).withHeadLayer(HeadLayer.TACTICAL);
    }

    public static ArmorPiece enlightenedPanopticonCanonical() {
        return new ArmorPiece("Panóptico del Ilustrado","Matriz predominantemente vítrea de celdas hexagonales y vidrio laminado nacida de la tradición de los Ilustrados, maestros dedicados a reunir perspectivas incompatibles antes de aceptar que una realidad observada era una realidad comprendida. Capta luz desde múltiples direcciones y la organiza hacia la cámara ocular y el fotófono. Su gran masa y la repetición celular reparten muy bien cargas de corte y compresión sobre numerosas paredes laminadas, y conserva frente a penetración puntual la protección perforante estándar del vidrio laminado; su asimetría procede de que la arquitectura celular eleva especialmente corte y compresión sin inventar una resistencia perforante distinta del material. Los pequeños herrajes de acero son accesorios secundarios aislados y no establecen una vía conductora funcional con el usuario.",3.500,ArmorPhysicalDimensionsCatalog.headFootprintFor("Panóptico del Ilustrado"),ArmorHitLocation.HEAD,1.00,new ArmorProtectionProfile(40,100,100),ArmorMaterial.LAMINATED_GLASS,Set.of(ArmorMaterial.LAMINATED_GLASS),ArmorForm.STANDARD,List.of("MATERIAL DEFENSIVO | VIDRIO LAMINADO","ACCESORIOS | Herrajes secundarios de acero sin contacto conductor","COBERTURA | CABEZA 100%","PROTECCIÓN | 40% / 100% / 100%","ARQUITECTURA DEFENSIVA | Celdas laminadas: máxima distribución frente a corte/compresión · perforante estándar del vidrio laminado","ACTIVACIÓN | CLARIVIDENCIA 22","COMUNICACIÓN FOTÓNICA | 79-213 m"),List.of(ItemProperty.hidden(ItemPropertyId.ENLIGHTENED_PANOPTICON,"Panóptico del Ilustrado","Filtra, organiza y presenta información luminosa captada desde todas las direcciones.",Attribute.CLARIVIDENCIA,22,"INMUNIDAD | Frenesí"),ItemProperty.alwaysActive(ItemPropertyId.ELECTRICAL_WEAKNESS_SUPPRESSED,"HERRAJES ELÉCTRICAMENTE DESACOPLADOS","Los pequeños herrajes de acero permanecen aislados de la ruta funcional hacia el usuario y no convierten la matriz vítrea en una armadura conductora.","ELECTRICIDAD | No aporta vulnerabilidad x2"),ItemProperty.alwaysActive(ItemPropertyId.PHOTONIC_COMMUNICATION,"COMUNICACIÓN FOTÓNICA","Comunicación semidúplex mediante línea de visión y fotófono.","ALCANCE | 79-213 m")),ArmorBlockCapability.NONE).withHeadLayer(HeadLayer.TACTICAL);
    }

    public static ArmorPiece engineerSuit() {
        ArmorProtectionProfile protection = ArmorMaterial.ELECTROMECHANICAL_COMPOSITE.canonicalProtection();
        String narrative = "El Conjunto del Ingeniero V881 no fue concebido como una armadura militar, sino como una máquina de trabajo antropomórfica cerrada capaz de mantener a un operario dentro de un entorno que ya no admite presencia humana ordinaria. "
                + "La primera capa es un mono técnico continuo, ceñido únicamente donde un pliegue podría interferir con una junta. Axilas, ingles, parte posterior de las rodillas y base del cuello utilizan fuelles multilaminares sellados: cada fuelle permite desplazamiento angular entre segmentos rígidos sin abrir la envolvente ni convertir la flexión repetida en una grieta. "
                + "Sobre ese mono se monta una segunda anatomía. Dos largueros estructurales ascienden desde la pelvis a ambos lados de la espalda y reciben costillas transversales que rodean parcialmente el torso. Las placas pectorales no forman una única concha: son segmentos solapados anclados a esas costillas para que un impacto puntual se distribuya primero por varios apoyos mecánicos antes de transmitirse al cuerpo. "
                + "La pelvis es el nudo portante del sistema. Allí confluyen los largueros dorsales, los apoyos de cadera, los acumuladores hidromecánicos y las transmisiones que continúan por muslos y tibias hasta un calzado estructural ancho. El peso no termina en los hombros: describe una ruta cerrada casco-collar-espina-cintura-piernas-suelo. "
                + "El módulo craneal nace del collar rígido y apoya parte de sus 3,5 kg de masa cervical sobre el armazón; una abertura frontal estrecha de vidrio laminado protege la visión, mientras placas laterales, respirador y rejillas de servicio quedan encerrados en una carcasa desmontable. El casco no admite otra pieza HEAD porque no existe un espacio anatómico independiente sobre el que colocarla. "
                + "Entre el mono interior y la exoestructura discurren tres redes físicamente separadas. La primera es ambiental: cierres laberínticos, conducciones respiratorias y juntas mantienen una presión interior controlada y separan al usuario de aerosoles, venenos y agua. La segunda es dieléctrica: casquillos, manguitos y apoyos aislantes interrumpen cualquier continuidad peligrosa entre la estructura electromecánica y el cuerpo. La tercera es térmica: conductos y superficies de intercambio trasladan calor entre regiones del traje para impedir que una cara exterior abrasadora o glacial dicte por sí sola la temperatura del interior. "
                + "Los actuadores no intentan convertir al usuario en un atleta. Siguen el movimiento de cadera, rodilla, hombro y codo, descargan la masa propia del conjunto y reducen el par necesario para mover sus segmentos. La geometría conserva suficiente velocidad articular para caminar, trotar y correr, pero no suficiente amplitud ni libertad para agacharse, gatear, nadar, escalar o deslizarse. La motricidad fina queda limitada por el propio acoplamiento hombre-máquina: DESTREZA efectiva no puede superar 20. "
                + "Las manos continúan siendo manos enguantadas y no pinzas motorizadas; el objetivo es manipular herramientas, cierres y maquinaria. Todas las placas principales pueden retirarse mediante tornillería accesible, y cada conducción cruza una articulación mediante un tramo flexible protegido para que mantenimiento y diagnóstico sigan siendo operaciones mecánicas comprensibles. "
                + "En la espalda, entre las costillas estructurales, una columna de pequeños visores de vidrio cian muestra la integridad del circuito hidromecánico. No es un adorno ni una pantalla abstracta: permite que otro operario determine de un vistazo cuánto margen estructural conserva el conjunto. "
                + "El resultado es deliberadamente incómodo, pesado y legible. Veinticinco kilogramos de máquina caminan con el usuario porque la carga se entrega al suelo antes que a sus hombros. Mientras la estructura permanezca cerrada y operativa, veneno, agua, diferencia de potencial y extremos térmicos encuentran subsistemas distintos que les impiden alcanzar al hombre. No es una armadura que un ingeniero aprendió a utilizar; es una herramienta que, al terminar de construirse alrededor de él, descubrió accidentalmente que también era una armadura.";
        return new ArmorPiece("Conjunto del Ingeniero V881", narrative, 25.000,
                ArmorPhysicalDimensionsCatalog.technicalSuitFootprintFor("Conjunto del Ingeniero V881"),
                1.00,1.00,protection,ArmorMaterial.ELECTROMECHANICAL_COMPOSITE,
                Set.of(ArmorMaterial.ELECTROMECHANICAL_COMPOSITE),ArmorForm.INTEGRAL_SUIT,
                List.of("MATERIAL | COMPUESTO ELECTROMECÁNICO",
                        "CLASE MATERIAL | HEAVY",
                        "ARQUITECTURA PORTANTE | Casco -> collar -> espina mecánica -> pelvis -> piernas -> suelo",
                        "ENVOLVENTE | Mono técnico sellado + fuelles multilaminares + placas segmentadas desmontables",
                        "SUBSISTEMA AMBIENTAL | Sellado integral de atmósfera y líquidos",
                        "SUBSISTEMA ELÉCTRICO | Desacoplamiento dieléctrico entre estructura y usuario",
                        "SUBSISTEMA TÉRMICO | Distribución e intercambio interno de calor",
                        "SUBSISTEMA MECÁNICO | Costillas estructurales + largueros + actuadores de cadera/rodilla/hombro/codo",
                        "COBERTURA | 100% BODY · 100% HEAD · FEET integrado",
                        "PROTECCIÓN | "+formatProtection(protection),
                        "PESO TOTAL | 25.000 kg",
                        "MASA CERVICAL | 3.500 kg",
                        "DESTREZA EFECTIVA | Máximo 20",
                        "MOVILIDAD | Caminar · trotar · correr",
                        "MOVILIDAD BLOQUEADA | Agacharse · gatear · nadar · escalar · deslizarse",
                        "INDICADOR DORSAL | Columna cian de integridad hidromecánica",
                        "DIMENSIONES XYZ | "+ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Conjunto del Ingeniero V881").xSlots()+" x "+ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Conjunto del Ingeniero V881").ySlots()+" x "+ArmorPhysicalDimensionsCatalog.technicalSuitDimensionsFor("Conjunto del Ingeniero V881").zSlots()+" slots físicos"),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.ONE_PIECE_SUIT,"MONO DE UNA PIEZA","La plataforma se equipa desde CHEST, ocupa BODY exterior completo y sólo admite capas INNER debajo; su módulo craneal integrado excluye cualquier pieza HEAD independiente.","BODY | Sólo INNER debajo · HEAD | Ocupado"),
                        ItemProperty.alwaysActive(ItemPropertyId.INTEGRAL_SEAL,"SELLADO INTEGRAL","Juntas, fuelles, respiración y cierres laberínticos separan la atmósfera interior del medio exterior.","INMUNIDAD | Veneno · Toxicidad Virulenta"),
                        ItemProperty.alwaysActive(ItemPropertyId.INTEGRAL_WATERPROOF,"IMPERMEABILIDAD INTEGRAL","La envolvente cerrada impide que el agua alcance al usuario mientras la estructura permanezca operativa.","INMUNIDAD | Empapado"),
                        ItemProperty.alwaysActive(ItemPropertyId.DIELECTRIC_ENVELOPE,"ENVOLVENTE DIELÉCTRICA","Casquillos, apoyos y manguitos aislantes desacoplan al usuario del armazón electromecánico.","INMUNIDAD | Electricidad"),
                        ItemProperty.alwaysActive(ItemPropertyId.THERMAL_CONTROL,"CONTROL TERMOMECÁNICO","Conducciones y superficies de intercambio estabilizan el microclima interior frente a extremos térmicos.","INMUNIDAD | Quemadura · Congelación · Quemadura Asfixiante · Frío Escarchante"),
                        ItemProperty.alwaysActive(ItemPropertyId.ELECTROMECHANICAL_STABILITY,"ESTABILIDAD ELECTROMECÁNICA","El armazón redistribuye cada esfuerzo a través de la estructura activa.","FUERZA | +1 ESTABILIDAD FÍSICA por punto"),
                        ItemProperty.alwaysActive(ItemPropertyId.SERVOMOTOR_CAPACITY,"CAPACIDAD SERVOMOTORA","Los actuadores descargan la masa propia del conjunto y acompañan la locomoción sin transformar la capacidad de carga del usuario.","MOVILIDAD | Permite caminar, trotar y correr"),
                        ItemProperty.alwaysActive(ItemPropertyId.HYDROMECHANICAL_ASSISTANCE,"ASISTENCIA HIDROMECÁNICA","La presión hidráulica y la transmisión mecánica convierten la continuidad física en fuerza de choque.","VITALIDAD | +1 daño bruto por contundencia por punto"),
                        ItemProperty.alwaysActive(ItemPropertyId.MATERIAL_SYNERGY,"SINERGIA MATERIAL","El módulo craneal transmite el impulso al collar y a la exoestructura en lugar de concentrarlo sobre la cabeza.","CABEZA | Inhibe el multiplicador x1,5 de daño contundente"),
                        ItemProperty.alwaysActive(ItemPropertyId.INTEGRATED_FOOTWEAR,"CALZADO INTEGRADO","La ruta portante termina en un calzado estructural inseparable del traje.","FEET | Ocupado por el traje"),
                        ItemProperty.alwaysActive(ItemPropertyId.BIOMECHANICAL_RIGIDITY,"RIGIDEZ BIOMECÁNICA","La exoestructura conserva velocidad longitudinal pero limita amplitud articular y motricidad fina.","DESTREZA | Máximo 20 · PERMITE | caminar/trotar/correr · BLOQUEA | agacharse/gatear/nadar/escalar/deslizarse")))
                .withHeadSupportedWeightKg(3.5);
    }


    // ----------------  · HEAD ----------------
    private static ArmorPiece headTextile(String name, String narrative, double weightKg,
                                           HeadLayer layer, double coverage, int clothLayers, boolean waterproof) {
        ArmorProtectionProfile profile=new ArmorProtectionProfile(2.0*clothLayers,5.0*clothLayers,2.0*clothLayers);
        List<ItemProperty> props=waterproof
                ? List.of(ItemProperty.alwaysActive(ItemPropertyId.WATERPROOF,"IMPERMEABLE",
                    "El acabado y la caída de la prenda interceptan el agua antes de que alcance la cabeza.","HEAD | Impide EMPAPADO mientras permanezca operativa"))
                : List.of();
        return new ArmorPiece(name,narrative,weightKg,ArmorPhysicalDimensionsCatalog.headFootprintFor(name),ArmorHitLocation.HEAD,coverage,profile,
                ArmorMaterial.CLOTH,ArmorForm.STANDARD,
                List.of("MATERIAL | TELA x"+clothLayers,"COBERTURA | CABEZA "+Math.round(coverage*100)+"%","PROTECCIÓN | "+formatProtection(profile)),props)
                .withHeadLayer(layer);
    }

    private static ArmorPiece steelHead(String name,String narrative,double weight,double coverage,ArmorProtectionProfile profile) {
        return new ArmorPiece(name,narrative,weight,ArmorPhysicalDimensionsCatalog.headFootprintFor(name),ArmorHitLocation.HEAD,coverage,profile,
                ArmorMaterial.STEEL,Set.of(ArmorMaterial.STEEL,ArmorMaterial.CLOTH),ArmorForm.STANDARD,
                List.of("MATERIAL | ACERO · FORRO TEXTIL","COBERTURA | CABEZA "+Math.round(coverage*100)+"%","PROTECCIÓN | "+formatProtection(profile)),historicalKnightConductorProperty(),ArmorBlockCapability.NONE)
                .withHeadLayer(HeadLayer.TACTICAL);
    }

    public static ArmorPiece normalVisionGlassesV881() {
        ArmorProtectionProfile p=ArmorMaterial.LAMINATED_GLASS.canonicalProtection();
        return new ArmorPiece("Gafas de visión V881","Gafas correctoras clásicas formadas por dos lentes graduadas de vidrio laminado de pequeño espesor, talladas individualmente para compensar el defecto refractivo del usuario y devolverle su agudeza visual funcional. Puente, aros y patillas mantienen cada centro óptico alineado con los ojos; la montura deja libre casi toda la cabeza y la resistencia material se concentra en la pequeña región ocular sin pretender sustituir un casco. La graduación forma parte de la identidad funcional de esta unidad: unas lentes con otra prescripción siguen siendo físicamente gafas de visión, pero no corrigen de manera adecuada a un usuario distinto.",
                .045,ArmorPhysicalDimensionsCatalog.headFootprintFor("Gafas de visión V881"),ArmorHitLocation.HEAD,.04,p,ArmorMaterial.LAMINATED_GLASS,ArmorForm.STANDARD,
                List.of("MATERIAL | VIDRIO LAMINADO","ÓPTICA | Lentes graduadas de prescripción individual · corrigen agudeza visual","COBERTURA | CABEZA 4%","PROTECCIÓN | "+formatProtection(p)),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.EYEWEAR,"GAFAS CORRECTORAS","Óptica graduada llevada directamente sobre la región ocular; la prescripción individual corrige el defecto refractivo del usuario y restituye su agudeza visual funcional.","RANURA | TACTICAL HEAD · FUNCIÓN | Corrección visual graduada")))
                .withHeadLayer(HeadLayer.TACTICAL);
    }
    public static ArmorPiece beardedHelmetV881() { return steelHead("Casco Barbudo V881","Yelmo cerrado de placas conformadas que prolonga la protección facial hacia mandíbula y mejillas mediante superficies anguladas. Las articulaciones y ranuras de observación reducen las discontinuidades abiertas sin inmovilizar por completo cuello y mandíbula.",2.40,.92,new ArmorProtectionProfile(86,100,78)); }
    public static ArmorPiece crusaderHelmetV881() { return steelHead("Casco del Cruzado V881","Máscara de acero de rostro completo unida a una calota compacta mediante anclajes desmontables. El diseño refina la silueta de los grandes yelmos cruzados reduciendo volumen muerto y repartiendo el impacto facial hacia el soporte craneal.",2.15,.88,new ArmorProtectionProfile(84,100,74)); }
    public static ArmorPiece spartanHelmetV881() {
        ArmorProtectionProfile p=ArmorMaterial.BRONZE.canonicalProtection();
        return new ArmorPiece("Casco Espartano V881","Casco de bronce de calota profunda, carrilleras prolongadas y abertura facial amplia. El borde conformado protege sienes y mejillas sin cerrar boca y ojos, conservando una percepción periférica superior a la de un yelmo integral.",1.55,ArmorPhysicalDimensionsCatalog.headFootprintFor("Casco Espartano V881"),ArmorHitLocation.HEAD,.70,p,
                ArmorMaterial.BRONZE,ArmorForm.STANDARD,List.of("MATERIAL | BRONCE","COBERTURA | CABEZA 70%","PROTECCIÓN | "+formatProtection(p)),List.of())
                .withHeadLayer(HeadLayer.TACTICAL);
    }
    public static ArmorPiece travelerHoodV881() { return headTextile("Capucha del Viajero V881","Capucha amplia de paño tratado cuyo borde puede adelantarse sobre frente y sienes sin cerrar el campo visual. El faldón posterior descarga el agua hacia hombros y espalda y permite combinarla con prendas de viaje sin formar una pieza rígida.",.32,HeadLayer.UPPER_ACCESSORY,.45,2,true); }

    public static ArmorPiece headScarfV881() { return headTextile("Bufanda V881","Banda larga de tejido flexible enrollada alrededor de cuello y parte baja del rostro. La tensión puede graduarse mediante vueltas sucesivas sin recurrir a cierres rígidos.",.18,HeadLayer.LOWER_ACCESSORY,.15,1,false); }
    public static ArmorPiece thickHeadScarfV881() { return headTextile("Bufanda gruesa V881","Bufanda de paño de mayor cuerpo cuyas vueltas superpuestas crean una envolvente estable alrededor de cuello y mandíbula inferior.",.35,HeadLayer.LOWER_ACCESSORY,.20,2,false); }
    public static ArmorPiece neckerchiefV881() { return headTextile("Pañuelo de cuello V881","Paño cuadrado plegado en diagonal y anudado al cuello, capaz de ascender parcialmente sobre boca y mentón sin cubrir el cráneo.",.08,HeadLayer.LOWER_ACCESSORY,.10,1,false); }
    public static ArmorPiece laborerKerchiefV881() { return headTextile("Pañuelo de Jornalero V881","Pañuelo de trabajo anudado con firmeza para absorber sudor y proteger cuello y parte inferior del rostro del polvo incidental durante labores físicas.",.07,HeadLayer.LOWER_ACCESSORY,.12,1,false); }
    public static ArmorPiece bandanaV881() { return headTextile("Bandana V881","Paño triangular de tejido resistente que puede fijarse sobre boca, mentón y cuello mediante un nudo posterior sencillo.",.06,HeadLayer.LOWER_ACCESSORY,.14,1,false); }
    public static ArmorPiece wrappedScarfV881() { return headTextile("Pañuelo envolvente V881","Paño largo y estrecho dispuesto en varias vueltas alrededor de cuello y mandíbula, con extremos remetidos para evitar enganches durante la marcha.",.22,HeadLayer.LOWER_ACCESSORY,.22,2,false); }

    public static ArmorPiece beretV881() { return headTextile("Boina V881","Casquete flexible de paño circular ajustado mediante una banda perimetral. La corona blanda se pliega sobre sí misma y se guarda con volumen mínimo.",.12,HeadLayer.UPPER_ACCESSORY,.25,1,true); }
    public static ArmorPiece boaterV881() { return headTextile("Canotier V881","Sombrero de copa baja y ala recta construido con trama vegetal revestida por una capa textil tratada. La rigidez mantiene el ala estable y desvía agua y sol fuera del rostro.",.20,HeadLayer.UPPER_ACCESSORY,.30,1,true); }
    public static ArmorPiece topHatV881() { return headTextile("Sombrero de copa V881","Sombrero alto estructurado mediante cuerpo textil endurecido, ala estrecha y copa cilíndrica. La carcasa conserva su forma aun con capas ligeras superpuestas de acabado.",.32,HeadLayer.UPPER_ACCESSORY,.30,2,true); }
    public static ArmorPiece bowlerV881() { return headTextile("Bombín V881","Sombrero compacto de fieltro endurecido con copa redondeada y ala corta. La estructura continua resiste deformaciones mejor que un sombrero de paño blando.",.28,HeadLayer.UPPER_ACCESSORY,.32,2,true); }
    public static ArmorPiece homburgV881() { return headTextile("Homburg V881","Sombrero de fieltro de copa hendida y ala levantada, conformado por vapor y presión para mantener una geometría estable sin armazón metálico.",.24,HeadLayer.UPPER_ACCESSORY,.32,2,true); }
    public static ArmorPiece broadBrimHatV881() { return headTextile("Sombrero de ala ancha V881","Sombrero de paño tratado con ala de gran diámetro que proyecta agua y radiación lejos de ojos, orejas y nuca.",.26,HeadLayer.UPPER_ACCESSORY,.38,2,true); }
    public static ArmorPiece laborerHatV881() { return headTextile("Sombrero de Jornalero V881","Sombrero de trabajo de copa baja y ala funcional, construido para tolerar plegado parcial, polvo y uso prolongado sin ornamentación estructural.",.18,HeadLayer.UPPER_ACCESSORY,.33,1,true); }
    public static ArmorPiece ridingHatV881() { return headTextile("Sombrero de montar V881","Sombrero firme de ala media y copa ceñida, estabilizado para que el viento y el movimiento de la montura no lo desplacen con facilidad.",.25,HeadLayer.UPPER_ACCESSORY,.34,2,true); }
    public static ArmorPiece charroHatV881() { return headTextile("Sombrero charro V881","Sombrero de copa elevada y ala muy ancha con borde reforzado. Su geometría amplía la proyección frente a sol y lluvia y conserva ornamentación sobre una estructura textil rígida.",.55,HeadLayer.UPPER_ACCESSORY,.42,3,true); }
    public static ArmorPiece walkingHatV881() { return headTextile("Sombrero de paseo V881","Sombrero de paseo de copa media y ala modelada, construido para admitir cintas, flores o velos sin que los adornos formen parte de su protección.",.23,HeadLayer.UPPER_ACCESSORY,.32,2,true); }
    public static ArmorPiece bonnetV881() { return headTextile("Capota V881","Tocado estructurado que rodea la coronilla y proyecta un borde alrededor del rostro. Cintas bajo la mandíbula estabilizan la pieza sin cerrar boca ni ojos.",.30,HeadLayer.UPPER_ACCESSORY,.40,2,true); }
    public static ArmorPiece formalHeaddressV881() { return headTextile("Tocado V881","Estructura textil de ceremonia montada sobre una base ligera que concentra volumen sobre coronilla y laterales sin pretender cubrir el rostro.",.28,HeadLayer.UPPER_ACCESSORY,.28,2,true); }
    public static ArmorPiece turbanV881() { return headTextile("Turbante V881","Banda larga de tela enrollada en sucesivas vueltas alrededor del cráneo. La superposición distribuye tensión y crea múltiples estratos textiles sin piezas rígidas.",.38,HeadLayer.UPPER_ACCESSORY,.50,3,true); }
    public static ArmorPiece fezV881() { return headTextile("Fez V881","Casquete troncocónico de fieltro compactado que se ajusta directamente a la parte superior del cráneo y mantiene su forma mediante la propia densidad del paño.",.16,HeadLayer.UPPER_ACCESSORY,.30,2,true); }
    public static ArmorPiece knittedCapV881() { return headTextile("Gorro de punto V881","Casquete de tejido de punto elástico que sigue la superficie craneal y se comprime casi por completo al almacenarse.",.10,HeadLayer.UPPER_ACCESSORY,.40,1,true); }
    public static ArmorPiece capV881() { return headTextile("Gorra V881","Casquete de paño ajustado con visera frontal reforzada. El cuerpo flexible se adapta al cráneo mientras la visera mantiene una proyección estable sobre los ojos.",.14,HeadLayer.UPPER_ACCESSORY,.30,1,true); }
    public static ArmorPiece hunterHatV881() { return headTextile("Sombrero de Cazador V881","Sombrero de fieltro encerado de copa deformable y ala irregular, refinado para soportar vegetación, lluvia y plegados parciales durante desplazamientos prolongados. La geometría asimétrica favorece escurrimiento y visión lateral sin convertirlo en casco.",.34,HeadLayer.UPPER_ACCESSORY,.40,2,true); }

    public static List<ArmorPiece> allHeadArmor() {
        return List.of(retractableAeronautHelmet(),enlightenedPanopticonCanonical(),integralRespirator(),paperHelmetV881(),hardenedLeatherJetHelmet(),
                workshopGoggles(),paddedCoif(),historicalKnightHelmet(),normalVisionGlassesV881(),beardedHelmetV881(),crusaderHelmetV881(),spartanHelmetV881(),
                travelerNeckGaiter(),headScarfV881(),thickHeadScarfV881(),neckerchiefV881(),laborerKerchiefV881(),bandanaV881(),wrappedScarfV881(),
                travelerHoodV881(),beretV881(),boaterV881(),topHatV881(),bowlerV881(),homburgV881(),broadBrimHatV881(),laborerHatV881(),ridingHatV881(),
                charroHatV881(),walkingHatV881(),bonnetV881(),formalHeaddressV881(),turbanV881(),fezV881(),knittedCapV881(),capV881(),hunterHatV881());
    }

    // ----------------  · OUTER LEGGINGS ----------------
    /**
     * Catálogo canónico de protecciones exteriores de piernas.
     * La posición OUTER no representa una familia social ni un conjunto: sólo la capa protectora
     * MEDIUM/HEAVY que puede superponerse a la indumentaria LIGHT de MIDDLE LEGGINGS.
     * Las botas altas se excluyen porque ocupan FEET y sólo aportan cobertura multirregional sobre LEGGINGS.
     */
    public static List<ArmorPiece> allOuterLeggings() {
        return List.of(
                hardenedLeatherLeggings(),
                leatherStrapBuckleGaitersV881(),
                leatherRigidSideClosureGaitersV881(),
                leatherOrnamentedHispanicGaitersV881(),
                leatherShotgunChapsV881(),
                leatherBatwingChapsV881(),
                leatherCharroChapsV881(),
                paperLeggingsV881(),
                historicalEbonyWarriorLeggings(),
                historicalKnightLeggings(),
                knightV881Leggings(),
                historicalHeavyLamellarLeggings());
    }

    /** catálogo canónico CHEST cuya clase material emergente es MEDIUM o HEAVY. */
    public static List<ArmorPiece> allProtectiveMiddleChest() {
        return List.of(
                hardenedLeatherChest(),
                hardenedLeatherAviatorJacketV881(),
                hardenedLeatherCrossedMotorcycleJacketV881(),
                workshopLeatherApronV881(),
                paperChestV881(),
                historicalEbonyWarriorChest(),
                ebonyWarriorV881Chest(),
                historicalKnightChest(),
                knightV881Chest(),
                historicalHeavyLamellarChest());
    }

    /**
     *  consolidation: el dominio ya no publica conjuntos de armadura.
     * Las piezas canónicas se consultan por región/capa; la composición del loadout pertenece a equipamiento.
     */

    private static ArmorPiece layeredRegionalPiece(String name, String description, double weight,
                                                    InventoryFootprint footprint,
                                                    ArmorInventoryCategory category,
                                                    Map<BodyArmorRegion, Double> coverage,
                                                    List<ArmorMaterialLayer> layers) {
        ArmorProtectionProfile protection = ArmorProtectionCompositionPolicy.additiveLayers(layers);
        ArmorMaterial primary = layers.get(0).material();
        Set<ArmorMaterial> materials = layers.stream().map(ArmorMaterialLayer::material)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        String materialLabel = layers.stream().map(ArmorMaterialLayer::label)
                .collect(java.util.stream.Collectors.joining(" · "));
        String coverageLabel = coverage.entrySet().stream()
                .map(entry -> entry.getKey().label().toUpperCase(Locale.ROOT) + " "
                        + Math.round(entry.getValue() * 100) + "%")
                .collect(java.util.stream.Collectors.joining(" · "));
        return new ArmorPiece(name, description, weight, footprint, category, coverage, protection,
                primary, materials, ArmorForm.STANDARD,
                List.of("MATERIAL | " + materialLabel,
                        "COBERTURA CORPORAL | " + coverageLabel,
                        "PROTECCIÓN | " + formatProtection(protection),
                        "PESO (kg) | " + String.format(Locale.ROOT, "%.3f", weight),
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " verticales x "
                                + footprint.horizontalSlots() + " horizontales"), List.of());
    }

    private static ArmorPiece regionalTextilePiece(String name, String description, double weight,
                                                    InventoryFootprint footprint, int layers,
                                                    java.util.Map<BodyArmorRegion, Double> coverage,
                                                    ArmorProtectionProfile protection) {
        ArmorInventoryCategory category = coverage.containsKey(BodyArmorRegion.CHEST)
                ? ArmorInventoryCategory.CHEST
                : coverage.containsKey(BodyArmorRegion.BRACERS)
                ? ArmorInventoryCategory.BRACERS : ArmorInventoryCategory.LEGGINGS;
        String coverageLabel = coverage.entrySet().stream()
                .map(entry -> entry.getKey().label().toUpperCase(Locale.ROOT) + " "
                        + Math.round(entry.getValue() * 100) + "%")
                .collect(java.util.stream.Collectors.joining(" · "));
        return new ArmorPiece(name, description, weight, footprint, category, coverage, protection,
                ArmorMaterial.CLOTH, Set.of(ArmorMaterial.CLOTH), ArmorForm.STANDARD,
                List.of("MATERIAL | TELA x" + layers,
                        "COBERTURA CORPORAL | " + coverageLabel,
                        "PROTECCIÓN | " + formatProtection(protection),
                        "PESO (kg) | " + String.format(Locale.ROOT, "%.3f", weight),
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() + " verticales x "
                                + footprint.horizontalSlots() + " horizontales"), List.of());
    }

    private static ArmorPiece piece(String name, String description, double weight,
                                    ArmorInventoryCategory category, ArmorHitLocation location,
                                    double coverage, ArmorProtectionProfile protection,
                                    ArmorMaterial material, ArmorForm form, String materialLabel,
                                    List<ItemProperty> properties) {
        return piece(name, description, weight, category, location, coverage, protection, material, form,
                materialLabel, category.footprint(), Set.of(material), properties, ArmorBlockCapability.NONE);
    }

    private static ArmorPiece piece(String name, String description, double weight,
                                    ArmorInventoryCategory category, ArmorHitLocation location,
                                    double coverage, ArmorProtectionProfile protection,
                                    ArmorMaterial material, ArmorForm form, String materialLabel,
                                    Set<ArmorMaterial> materials, List<ItemProperty> properties) {
        return piece(name, description, weight, category, location, coverage, protection, material, form,
                materialLabel, category.footprint(), materials, properties, ArmorBlockCapability.NONE);
    }

    private static ArmorPiece piece(String name, String description, double weight,
                                    ArmorInventoryCategory category, ArmorHitLocation location,
                                    double coverage, ArmorProtectionProfile protection,
                                    ArmorMaterial material, ArmorForm form, String materialLabel,
                                    List<ItemProperty> properties, ArmorBlockCapability blockCapability) {
        return piece(name, description, weight, category, location, coverage, protection, material, form,
                materialLabel, category.footprint(), Set.of(material), properties, blockCapability);
    }

    private static ArmorPiece piece(String name, String description, double weight,
                                    ArmorInventoryCategory category, ArmorHitLocation location,
                                    double coverage, ArmorProtectionProfile protection,
                                    ArmorMaterial material, ArmorForm form, String materialLabel,
                                    Set<ArmorMaterial> materials, List<ItemProperty> properties,
                                    ArmorBlockCapability blockCapability) {
        return piece(name, description, weight, category, location, coverage, protection, material, form,
                materialLabel, category.footprint(), materials, properties, blockCapability);
    }

    private static ArmorPiece piece(String name, String description, double weight,
                                    ArmorInventoryCategory category, ArmorHitLocation location,
                                    double coverage, ArmorProtectionProfile protection,
                                    ArmorMaterial material, ArmorForm form, String materialLabel,
                                    InventoryFootprint footprint, Set<ArmorMaterial> materials,
                                    List<ItemProperty> properties) {
        return piece(name, description, weight, category, location, coverage, protection, material, form,
                materialLabel, footprint, materials, properties, ArmorBlockCapability.NONE);
    }

    private static ArmorPiece piece(String name, String description, double weight,
                                    ArmorInventoryCategory category, ArmorHitLocation location,
                                    double coverage, ArmorProtectionProfile protection,
                                    ArmorMaterial material, ArmorForm form, String materialLabel,
                                    InventoryFootprint footprint, Set<ArmorMaterial> materials,
                                    List<ItemProperty> properties, ArmorBlockCapability blockCapability) {
        return new ArmorPiece(name, description, weight, footprint, location, coverage, protection,
                material, materials, form,
                List.of(
                        "MATERIAL | " + materialLabel,
                        "COBERTURA CORPORAL | " + Math.round(coverage * 100) + "% (" +
                                (location == ArmorHitLocation.HEAD ? "cabeza" : "cuerpo") + ")",
                        "PROTECCIÓN | " + formatProtection(protection),
                        "PESO (kg) | " + String.format(Locale.ROOT, "%.3f", weight),
                        "TAMAÑO DE INVENTARIO | " + footprint.verticalSlots() +
                                " verticales x " + footprint.horizontalSlots() + " horizontales"
                ), properties, blockCapability);
    }

    private static String formatProtection(ArmorProtectionProfile profile) {
        return format(profile.piercing()) + "% perforante - " + format(profile.slashing()) +
                "% cortante - " + format(profile.blunt()) + "% contundente";
    }

    private static String format(double value) {
        return value == Math.rint(value) ? String.format(Locale.ROOT, "%.0f", value)
                : String.format(Locale.ROOT, "%.2f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
