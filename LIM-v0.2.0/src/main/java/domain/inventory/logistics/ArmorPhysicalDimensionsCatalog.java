package domain.inventory.logistics;

import domain.inventory.InventoryFootprint;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dimensiones físicas canónicas X(ancho) × Y(alto/largo) × Z(profundidad) de armaduras.
 *  cubre HEAD;  incorpora INNER CHEST. En textiles se registra el volumen
 * razonablemente plegado; las estructuras rígidas o semirrígidas conservan su envolvente.
 */
public final class ArmorPhysicalDimensionsCatalog {
    private static final Map<String, InventoryPhysicalDimensions> HEAD = buildHead();
    private static final Map<String, InventoryPhysicalDimensions> INNER_CHEST = buildInnerChest();
    private static final Map<String, InventoryPhysicalDimensions> MIDDLE_CHEST = buildMiddleChest();
    private static final Map<String, InventoryPhysicalDimensions> OUTER_CHEST = buildOuterChest();
    private static final Map<String, InventoryPhysicalDimensions> BRACERS = buildBracers();
    private static final Map<String, InventoryPhysicalDimensions> PROTECTIVE_MIDDLE_CHEST = buildProtectiveMiddleChest();
    private static final Map<String, InventoryPhysicalDimensions> INNER_FEET = buildInnerFeet();
    private static final Map<String, InventoryPhysicalDimensions> OUTER_FEET = buildOuterFeet();
    private static final Map<String, InventoryPhysicalDimensions> INNER_LEGGINGS = buildInnerLeggings();
    private static final Map<String, InventoryPhysicalDimensions> MIDDLE_LEGGINGS = buildMiddleLeggings();
    private static final Map<String, InventoryPhysicalDimensions> OUTER_LEGGINGS = buildOuterLeggings();
    private static final Map<String, InventoryPhysicalDimensions> TECHNICAL_SUITS = buildTechnicalSuits();
    private ArmorPhysicalDimensionsCatalog() {}

    public static InventoryPhysicalDimensions headDimensionsFor(String name) {
        InventoryPhysicalDimensions d = HEAD.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas HEAD para: " + name);
        return d;
    }

    public static InventoryFootprint headFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(headDimensionsFor(name));
    }

    public static int headProfileCount() { return HEAD.size(); }


    public static InventoryPhysicalDimensions innerChestDimensionsFor(String name) {
        InventoryPhysicalDimensions d = INNER_CHEST.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas INNER CHEST para: " + name);
        return d;
    }

    public static InventoryFootprint innerChestFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(innerChestDimensionsFor(name));
    }

    public static int innerChestProfileCount() { return INNER_CHEST.size(); }

    public static InventoryPhysicalDimensions middleChestDimensionsFor(String name) {
        InventoryPhysicalDimensions d = MIDDLE_CHEST.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas MIDDLE CHEST para: " + name);
        return d;
    }

    public static InventoryFootprint middleChestFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(middleChestDimensionsFor(name));
    }

    public static int middleChestProfileCount() { return MIDDLE_CHEST.size(); }

    public static InventoryPhysicalDimensions outerChestDimensionsFor(String name) {
        InventoryPhysicalDimensions d = OUTER_CHEST.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas OUTER CHEST para: " + name);
        return d;
    }

    public static InventoryFootprint outerChestFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(outerChestDimensionsFor(name));
    }

    public static int outerChestProfileCount() { return OUTER_CHEST.size(); }

    public static InventoryPhysicalDimensions bracersDimensionsFor(String name) {
        InventoryPhysicalDimensions d = BRACERS.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas BRACERS para: " + name);
        return d;
    }

    public static InventoryFootprint bracersFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(bracersDimensionsFor(name));
    }

    public static int bracersProfileCount() { return BRACERS.size(); }

    public static InventoryPhysicalDimensions mediumHeavyChestDimensionsFor(String name) {
        InventoryPhysicalDimensions d = PROTECTIVE_MIDDLE_CHEST.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas CHEST MEDIUM/HEAVY para: " + name);
        return d;
    }

    public static InventoryFootprint mediumHeavyChestFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(mediumHeavyChestDimensionsFor(name));
    }

    public static int protectiveMiddleChestProfileCount() { return PROTECTIVE_MIDDLE_CHEST.size(); }

    public static InventoryPhysicalDimensions innerFeetDimensionsFor(String name) {
        InventoryPhysicalDimensions d = INNER_FEET.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas INNER FEET para: " + name);
        return d;
    }

    public static InventoryFootprint innerFeetFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(innerFeetDimensionsFor(name));
    }

    public static int innerFeetProfileCount() { return INNER_FEET.size(); }

    public static InventoryPhysicalDimensions outerFeetDimensionsFor(String name) {
        InventoryPhysicalDimensions d = OUTER_FEET.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas OUTER FEET para: " + name);
        return d;
    }

    public static InventoryFootprint outerFeetFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(outerFeetDimensionsFor(name));
    }

    public static int outerFeetProfileCount() { return OUTER_FEET.size(); }

    public static InventoryPhysicalDimensions innerLeggingsDimensionsFor(String name) {
        InventoryPhysicalDimensions d = INNER_LEGGINGS.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas INNER LEGGINGS para: " + name);
        return d;
    }

    public static InventoryFootprint innerLeggingsFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(innerLeggingsDimensionsFor(name));
    }

    public static int innerLeggingsProfileCount() { return INNER_LEGGINGS.size(); }

    public static InventoryPhysicalDimensions middleLeggingsDimensionsFor(String name) {
        InventoryPhysicalDimensions d = MIDDLE_LEGGINGS.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas MIDDLE LEGGINGS para: " + name);
        return d;
    }

    public static InventoryFootprint middleLeggingsFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(middleLeggingsDimensionsFor(name));
    }

    public static int middleLeggingsProfileCount() { return MIDDLE_LEGGINGS.size(); }

    public static InventoryPhysicalDimensions outerLeggingsDimensionsFor(String name) {
        InventoryPhysicalDimensions d = OUTER_LEGGINGS.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas OUTER LEGGINGS para: " + name);
        return d;
    }

    public static InventoryFootprint outerLeggingsFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(outerLeggingsDimensionsFor(name));
    }

    public static int outerLeggingsProfileCount() { return OUTER_LEGGINGS.size(); }

    public static InventoryPhysicalDimensions technicalSuitDimensionsFor(String name) {
        InventoryPhysicalDimensions d = TECHNICAL_SUITS.get(name);
        if (d == null) throw new IllegalArgumentException("No hay dimensiones físicas de traje técnico para: " + name);
        return d;
    }

    public static InventoryFootprint technicalSuitFootprintFor(String name) {
        return InventoryVolumeProjectionPolicy.footprint(technicalSuitDimensionsFor(name));
    }

    public static int technicalSuitProfileCount() { return TECHNICAL_SUITS.size(); }

    private static InventoryPhysicalDimensions m(double x,double y,double z) {
        return InventoryPhysicalDimensions.fromMetricDimensions(x,y,z);
    }


    private static Map<String, InventoryPhysicalDimensions> buildTechnicalSuits() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();
        // Monos especializados: se pliegan, pero sus membranas/cámaras impiden una compresión textil extrema.
        m.put("Mono Ignífugo V881", m(.45,.35,.15));
        m.put("Mono Aislante V881", m(.45,.35,.15));
        // Ingeniero: armazón articulado de 25 kg; plegable por juntas, no colapsable como una prenda.
        m.put("Conjunto del Ingeniero V881", m(.70,.55,.30));
        if (m.size()!=3) throw new IllegalStateException(" debe definir exactamente tres trajes técnicos V881.");
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildInnerLeggings() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();
        m.put("Calzoncillos largos V881", m(.20,.15,.05));
        m.put("Calzoncillos hasta la rodilla V881", m(.15,.12,.04));
        m.put("Pantalón interior de punto V881", m(.20,.15,.06));
        m.put("Drawers femeninos V881", m(.20,.15,.06));
        m.put("Enagua V881", m(.25,.20,.07));
        m.put("Enagua reforzada V881", m(.25,.20,.08));
        m.put("Enagua acolchada V881", m(.30,.25,.12));
        m.put("Enagua dividida V881", m(.25,.20,.08));
        if (m.size()!=8) throw new IllegalStateException(" debe definir 8 INNER LEGGINGS independientes.");
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildMiddleLeggings() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();
        m.put("Pantalón recto V881", m(.25,.20,.07));
        m.put("Pantalón formal V881", m(.25,.20,.07));
        m.put("Pantalón de trabajo V881", m(.28,.22,.08));
        m.put("Pantalón de cintura alta V881", m(.25,.20,.07));
        m.put("Pantalón holgado V881", m(.30,.25,.08));
        m.put("Pantalón marinero V881", m(.30,.25,.08));
        m.put("Pantalón de montar V881", m(.28,.22,.08));
        m.put("Breeches V881", m(.20,.15,.06));
        m.put("Knickerbockers V881", m(.25,.20,.07));
        m.put("Bombachos V881", m(.30,.25,.08));
        m.put("Falda recta V881", m(.25,.20,.07));
        m.put("Falda amplia V881", m(.30,.25,.08));
        m.put("Falda de paseo V881", m(.30,.25,.08));
        m.put("Falda de trabajo V881", m(.30,.25,.08));
        m.put("Falda de montar V881", m(.35,.30,.10));
        m.put("Falda dividida V881", m(.30,.25,.08));
        m.put("Falda ornamentada V881", m(.30,.25,.09));
        m.put("Sobrefalda V881", m(.25,.20,.06));
        m.put("Kilt V881", m(.25,.20,.08));
        m.put("Sarong V881", m(.25,.20,.05));
        if (m.size()!=20) throw new IllegalStateException(" debe definir 20 MIDDLE LEGGINGS.");
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildOuterLeggings() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();
        // Cada entrada plural representa físicamente el par; weightKg ya es el peso total del conjunto.
        m.put("Pantalón de cuero endurecido V881", m(.35,.30,.12));
        m.put("Polainas de cuero con correas y hebillas V881", m(.60,.20,.12));
        m.put("Polainas rígidas de cierre lateral V881", m(.60,.20,.15));
        m.put("Polainas de cuero bordadas y ornamentadas V881", m(.60,.20,.14));
        m.put("Chaparreras cerradas (shotgun) V881", m(.70,.30,.15));
        m.put("Chaparreras de ala ancha (batwing) V881", m(.70,.35,.12));
        m.put("Chaparreras ornamentadas de tradición charra V881", m(.75,.35,.15));
        m.put("Polainas de Papel V881", m(.60,.25,.15));
        m.put("Polainas del Guerrero de Ébano", m(.70,.25,.20));
        m.put("Polainas de Caballero", m(.80,.35,.25));
        m.put("Polainas de Caballero hasta las rodillas V881", m(.70,.25,.20));
        m.put("Polainas Lamelares Históricas Pesadas", m(.70,.30,.20));
        if (m.size()!=12) throw new IllegalStateException(" debe definir 12 OUTER LEGGINGS.");
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildInnerFeet() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();
        // Cada entrada representa el par completo, plegado o apilado. weightKg ya es el peso del par.
        m.put("Calcetines V881", m(.15,.10,.03));
        m.put("Calcetines gruesos de trabajo V881", m(.18,.12,.05));
        m.put("Medias V881", m(.18,.12,.03));
        m.put("Medias altas V881", m(.25,.12,.04));
        m.put("Medias de punto grueso V881", m(.25,.15,.06));
        m.put("Vendas de pie V881", m(.15,.10,.04));
        m.put("Escarpines textiles V881", m(.25,.15,.06));
        if (m.size()!=7) throw new IllegalStateException(" debe definir 7 INNER FEET.");
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildOuterFeet() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();
        // Pares completos. El calzado rígido conserva el aire de su envolvente y no se pliega como textil.
        m.put("Alpargatas V881", m(.28,.20,.08));
        m.put("Zapatillas de lona V881", m(.30,.22,.10));
        m.put("Zapatos de trabajo de cuero V881", m(.32,.22,.12));
        m.put("Botines de cuero V881", m(.32,.25,.15));
        m.put("Botas cortas de campo V881", m(.35,.30,.18));
        m.put("Botas altas de montar y campo V881", m(.45,.35,.20));
        m.put("Botas de trabajo pesado e industria V881", m(.35,.30,.20));
        m.put("Zapatos Oxford/Brogue V881", m(.31,.22,.12));
        m.put("Zapatos de salón V881", m(.30,.20,.10));
        m.put("Mocasines V881", m(.30,.22,.10));
        m.put("Babuchas V881", m(.28,.20,.08));
        if (m.size()!=11) throw new IllegalStateException(" debe definir 11 OUTER FEET.");
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildProtectiveMiddleChest() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();

        // MEDIUM: cuero/papel. Se registra volumen plegado cuando el material lo permite.
        m.put("Chaqueta de Viaje V881", m(.35,.30,.12));
        m.put("Chaqueta de Aeronauta V881", m(.30,.25,.10));
        m.put("Chaqueta cruzada de motorista V881", m(.40,.32,.15));
        m.put("Delantal de Taller V881", m(.35,.25,.08));
        m.put("Coraza de Papel V881", m(.45,.55,.20));

        // HEAVY: envolvente estructurada. La lamelar conserva cierta compactación por articulación.
        m.put("Coraza del Guerrero de Ébano", m(.50,.60,.25));
        m.put("Coraza del Guerrero de Ébano V881", m(.55,.65,.30));
        m.put("Coraza de Caballero", m(.50,.60,.25));
        m.put("Coraza de Caballero incluido hombro V881", m(.55,.65,.28));
        m.put("Coraza Lamelar Histórica Pesada", m(.60,.70,.20));

        if (m.size() != 10) throw new IllegalStateException(
                " debe definir exactamente 10 piezas CHEST MEDIUM/HEAVY canónicas, no " + m.size());
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildBracers() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();

        // Guantes: el par se pliega/apila como un único paquete blando.
        m.put("Guantes de Precisión V881", m(.18,.12,.05));
        m.put("Guantes de cuero endurecido con los dedos al aire V881", m(.18,.12,.05));
        m.put("Guantes de Taller V881", m(.20,.14,.06));

        // Brazales: salvo la pieza V881 unilateral del Guerrero de Ébano, son pares.
        // El volumen conserva explícitamente las dos unidades, no una abstracción de "ranura BRACERS".
        m.put("Brazales de Papel V881", m(.60,.20,.10));                    // par: 6x2
        m.put("Brazales del Guerrero de Ébano", m(.60,.20,.15));           // par: 12x4
        m.put("Brazal izquierdo del Guerrero de Ébano V881", m(.30,.20,.15)); // único: 6x4
        m.put("Brazales de Caballero", m(.80,.25,.20));                    // par: 16x6
        m.put("Brazales de Caballero incluidos codera y nudillos V881", m(.80,.25,.20)); // par: 16x6
        m.put("Brazales Lamelares Históricos Pesados", m(.80,.25,.20));    // par: 16x6

        if (m.size() != 9) throw new IllegalStateException(
                " debe definir exactamente 9 conceptos BRACERS canónicos, no " + m.size());
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildOuterChest() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();

        // Volumen razonablemente plegado. X/Y describen el paquete textil; Z conserva
        // el espesor real de paños, solapes, faldones y estructuras blandas.
        m.put("Levita V881", m(.35,.30,.10));
        m.put("Frac V881", m(.30,.25,.08));
        m.put("Chaqué V881", m(.30,.25,.08));
        m.put("Americana V881", m(.28,.22,.07));
        m.put("Chaqueta Norfolk V881", m(.30,.25,.08));
        m.put("Blusón de trabajo V881", m(.28,.22,.07));
        m.put("Gabán V881", m(.40,.35,.14));
        m.put("Sobretodo V881", m(.38,.32,.12));
        m.put("Ulster V881", m(.45,.38,.15));
        m.put("Guardapolvo V881", m(.35,.28,.07));
        m.put("Gabardina V881", m(.38,.30,.10));
        m.put("Chaqueta de montar V881", m(.28,.22,.07));
        m.put("Bolero V881", m(.22,.18,.06));
        m.put("Capa del Caballero", m(.40,.30,.12));
        m.put("Capa del Viajero V881", m(.35,.30,.08));
        m.put("Capa Inverness V881", m(.40,.35,.12));
        m.put("Poncho V881", m(.35,.30,.08));
        m.put("Burnús V881", m(.38,.32,.10));
        m.put("Dolman V881", m(.30,.25,.09));
        m.put("Manteleta V881", m(.25,.20,.07));

        if (m.size() != 20) throw new IllegalStateException(
                " debe definir exactamente 20 piezas OUTER CHEST, no " + m.size());
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildMiddleChest() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();

        // Textiles sastreables y plegables.
        m.put("Chaleco V881", m(.22,.18,.05));
        m.put("Chaleco largo V881", m(.25,.20,.06));
        m.put("Chaleco de trabajo V881", m(.25,.20,.07));
        m.put("Chaleco acolchado V881", m(.30,.25,.08));
        m.put("Chaleco de montar V881", m(.22,.18,.05));
        m.put("Corpiño V881", m(.25,.25,.08));
        m.put("Corpiño regional V881", m(.25,.25,.08));
        m.put("Spencer V881", m(.28,.22,.08));
        m.put("Chaqueta interior acolchada V881", m(.35,.30,.12));
        m.put("Jubón V881", m(.30,.25,.08));
        m.put("Cardigan V881", m(.25,.20,.07));
        m.put("Jersey de punto V881", m(.25,.20,.08));
        m.put("Gambesón V881", m(.40,.30,.18));

        if (m.size() != 13) throw new IllegalStateException(
                " debe definir exactamente 13 piezas MIDDLE CHEST, no " + m.size());
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildInnerChest() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();

        // BASE: textiles plegados; Z permanece por debajo de 10 cm.
        m.put("Camiseta interior de punto V881", m(.15,.12,.04));
        m.put("Camisa V881", m(.20,.15,.05));
        m.put("Camisa de trabajo V881", m(.25,.18,.06));
        m.put("Camisa modular V881", m(.20,.15,.06));
        m.put("Blusa V881", m(.20,.15,.06));
        m.put("Blusa regional V881", m(.25,.20,.07));
        m.put("Chemise V881", m(.25,.20,.08));
        m.put("Gömlek V881", m(.25,.20,.07));

        // COVER: piezas textiles pequeñas o finas.
        m.put("Camisola V881", m(.15,.12,.04));
        m.put("Chemisette V881", m(.12,.10,.03));
        m.put("Pechera desmontable V881", m(.15,.10,.03));

        // STRUCTURAL: la corsetería no se colapsa como una camisa sin deformar sus rigidizadores.
        m.put("Corsé V881", m(.30,.40,.08));
        m.put("Corsé masculino V881", m(.30,.35,.08));

        m.put("Cubrecorsé V881", m(.15,.12,.04));
        m.put("Combinación interior V881", m(.30,.25,.08));

        if (m.size() != 15) throw new IllegalStateException(
                " debe definir exactamente 15 piezas INNER CHEST, no " + m.size());
        return Map.copyOf(m);
    }

    private static Map<String, InventoryPhysicalDimensions> buildHead() {
        LinkedHashMap<String, InventoryPhysicalDimensions> m = new LinkedHashMap<>();

        // TACTICAL: envolvente rígida real salvo textiles/ópticas plegables.
        m.put("Casco Replegable del Aeronauta", m(.30,.34,.28));
        m.put("Panóptico del Ilustrado", m(.32,.35,.30));
        m.put("Respirador Integral V881", m(.28,.32,.24));
        m.put("Casco de Papel V881", m(.28,.30,.24));
        m.put("Casco Jet de cuero endurecido con vidrio laminado V881", m(.28,.27,.22));
        m.put("Gafas para soldadura V881", m(.18,.08,.08));
        m.put("Cofia acolchada V881", m(.20,.15,.08));
        m.put("Casco de Caballero", m(.30,.35,.30));
        m.put("Gafas de visión V881", m(.15,.05,.04));
        m.put("Casco Barbudo V881", m(.30,.35,.30));
        m.put("Casco del Cruzado V881", m(.29,.34,.29));
        m.put("Casco Espartano V881", m(.32,.30,.28));

        // LOWER_ACCESSORY: volumen plegado real.
        m.put("Cubrecuellos del Viajero", m(.12,.10,.05));
        m.put("Bufanda V881", m(.15,.10,.05));
        m.put("Bufanda gruesa V881", m(.18,.12,.08));
        m.put("Pañuelo de cuello V881", m(.10,.10,.03));
        m.put("Pañuelo de Jornalero V881", m(.10,.10,.03));
        m.put("Bandana V881", m(.10,.10,.03));
        m.put("Pañuelo envolvente V881", m(.16,.10,.06));

        // UPPER_ACCESSORY flexible/deformable: volumen plegado.
        m.put("Capucha del Viajero V881", m(.20,.15,.08));
        m.put("Boina V881", m(.10,.10,.04));
        m.put("Sombrero de Jornalero V881", m(.25,.12,.20));
        m.put("Turbante V881", m(.18,.14,.08));
        m.put("Gorro de punto V881", m(.10,.10,.04));
        m.put("Gorra V881", m(.25,.12,.18));
        m.put("Sombrero de Cazador V881", m(.24,.12,.20));

        // UPPER_ACCESSORY estructurados: no se colapsan sin deformar su geometría.
        m.put("Canotier V881", m(.34,.10,.34));
        m.put("Sombrero de copa V881", m(.30,.30,.30));
        m.put("Bombín V881", m(.30,.16,.27));
        m.put("Homburg V881", m(.32,.16,.28));
        m.put("Sombrero de ala ancha V881", m(.45,.15,.42));
        m.put("Sombrero de montar V881", m(.31,.15,.27));
        m.put("Sombrero charro V881", m(.55,.22,.55));
        m.put("Sombrero de paseo V881", m(.32,.16,.28));
        m.put("Capota V881", m(.30,.25,.25));
        m.put("Tocado V881", m(.28,.25,.20));
        m.put("Fez V881", m(.22,.22,.20));

        if (m.size() != 37) throw new IllegalStateException(" debe definir exactamente 37 piezas HEAD, no " + m.size());
        return Map.copyOf(m);
    }
}
