package domain.inventory.item.meleeWeapons;

import domain.character.sheet.Attribute;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.*;
import domain.combat.moveset.ToolMovesetCatalog;
import domain.combat.moveset.ShortBladeMovesetCatalog;
import domain.combat.moveset.BluntToolMovesetCatalog;
import domain.combat.moveset.SwordMovesetCatalog;
import domain.combat.moveset.RotorGreatswordMovesetCatalog;
import domain.combat.moveset.PitchforkMovesetCatalog;
import domain.combat.moveset.HookedPolearmMovesetCatalog;
import domain.combat.moveset.BoStaffMovesetCatalog;

import java.util.EnumSet;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

/** Fuente canónica de las armas cuerpo a cuerpo ordinarias de LIM. */
public final class MeleeWeaponCatalog {
    private static final Set<WeaponCombatAction> FULL_TWO_HANDED = EnumSet.of(
            WeaponCombatAction.LIGHT_ATTACK,
            WeaponCombatAction.HEAVY_ATTACK,
            WeaponCombatAction.CHARGED_ATTACK,
            WeaponCombatAction.JUMP_ATTACK,
            WeaponCombatAction.DESTABILIZE
    );
    private static final Set<WeaponCombatAction> ORDINARY_ONE_HANDED = EnumSet.of(
            WeaponCombatAction.LIGHT_ATTACK,
            WeaponCombatAction.JUMP_ATTACK,
            WeaponCombatAction.DESTABILIZE
    );
    private static final Set<WeaponCombatAction> ROTOR_TWO_HANDED = EnumSet.of(
            WeaponCombatAction.LIGHT_ATTACK,
            WeaponCombatAction.HEAVY_ATTACK,
            WeaponCombatAction.JUMP_ATTACK,
            WeaponCombatAction.DESTABILIZE
    );

    private MeleeWeaponCatalog() {}

    public static List<WeaponItem> all() {
        return List.of(pico(), zapapico(), piqueta(), cuchilloDeCarnicero(), daga(), hachaDeLenador(), cimitarra());
    }

    /** Catálogo completo: siete armas ordinarias y dos armas especializadas. */
    public static List<WeaponItem> allCanonical() {
        java.util.ArrayList<WeaponItem> result = new java.util.ArrayList<>(all());
        result.add(espadaHelicoidal());
        result.add(espadonDeRotor());
        result.add(katanaTermoMecanicaV881());
        result.add(mazaElectroMecanicaV881());
        result.add(martilloDeBola());
        result.add(hoz());
        result.add(guadana());
        result.add(horca());
        result.add(bo());
        result.add(boathook());
        result.add(ShieldCatalog.pavesinaCementadaDeAsaltoV881());
        return List.copyOf(result);
    }

    public static WeaponItem pico() {
        return weapon(
                "Pico",
                "El Pico combina una cabeza de acero perpendicular a un mango de madera. Uno de sus extremos termina en una punta estrecha destinada a fracturar roca, suelo endurecido y materiales compactos; el otro presenta un borde ancho capaz de cortar, desterronar y separar material. Es una herramienta bimanual de agarre principal cuya eficiencia procede de concentrar la energía del movimiento sobre una superficie reducida.",
                2.40, 8, 2, 0.80, "Principal", 90, 65, 65,
                WeaponConfigurationPolicy.twoHandedPrimaryOnly(), Set.of(WeaponTrait.DE_ROTOR), ROTOR_TWO_HANDED
        ).withOffensiveMoveset(ToolMovesetCatalog.pico());
    }

    public static WeaponItem zapapico() {
        return weapon(
                "Zapapico",
                "El Zapapico conserva la estructura bimanual del Pico, pero sustituye su borde ancho por una hoja alargada semejante a una azada o pala estrecha. Esta modificación reduce ligeramente su capacidad de penetración puntual y aumenta su utilidad sobre tierra, raíces, zanjas y superficies que deben cortarse, removerse o arrastrarse, no solo fracturarse. Su cabeza continúa siendo pesada y descompensada, por lo que utiliza exclusivamente agarre principal.",
                2.40, 8, 2, 0.80, "Principal", 95, 65, 60,
                WeaponConfigurationPolicy.twoHandedPrimaryOnly(), Set.of(WeaponTrait.DE_ROTOR), ROTOR_TWO_HANDED
        ).withOffensiveMoveset(ToolMovesetCatalog.zapapico());
    }

    public static WeaponItem piqueta() {
        return weapon(
                "Piqueta",
                "La Piqueta es una herramienta compacta de construcción, cantería y albañilería. Su cabeza combina una cara de martillo con un cincel, permitiendo ajustar piezas, desprender mortero, abrir juntas, cortar material quebradizo y efectuar pequeños trabajos de demolición. Carece de una punta funcional de penetración, por lo que su rendimiento se distribuye entre el borde cincelado y el impacto de la cabeza.",
                0.80, 4, 2, 0.40, "Principal", 0, 65, 30,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), Set.of(), ORDINARY_ONE_HANDED
        ).withOffensiveMoveset(ToolMovesetCatalog.piqueta());
    }

    public static WeaponItem cuchilloDeCarnicero() {
        return weapon(
                "Cuchillo de Carnicero",
                "El Cuchillo de Carnicero es una hachuela de hoja rectangular, ancha y pesada, diseñada para dividir trozos gruesos de carne, separar articulaciones y atravesar huesos pequeños mediante golpes controlados. No está concebido para filetear ni producir láminas finas: el espesor de la hoja y la masa situada detrás del filo priorizan resistencia y corte profundo frente a precisión.",
                0.70, 4, 2, 0.40, "Principal", 0, 65, 15,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), Set.of(), ORDINARY_ONE_HANDED
        ).withOffensiveMoveset(ShortBladeMovesetCatalog.cuchilloDeCarnicero());
    }

    public static WeaponItem daga() {
        Set<WeaponTrait> traits = Set.of();
        List<AttributeRequirement> requirements = WeaponRequirementPolicy.calculate(
                0.40, 0.40, GripMode.ONE_HANDED, traits);
        WeaponItem dagger = new WeaponItem(
                "Daga",
                "La Daga es una pieza corta de doble filo, remate agudo y hoja rígida, provista de guarda para proteger la mano. Su geometría permite alternar cortes breves y estocadas controladas, al tiempo que conserva utilidad en tareas cotidianas que requieren una hoja compacta.",
                0.40, domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Daga"), 0.40,
                List.of(
                        new WeaponMode("Oscilatorio", new LethalityProfile(65, 65, 10)),
                        new WeaponMode("Invertido", new LethalityProfile(65, 65, 10))
                ),
                requirements, List.of(),
                List.of(
                        "PESO (kg) | 0,40",
                        "ALCANCE (m) | 0,40",
                        "FUERZA | 4",
                        "DESTREZA | 4",
                        "MODO PRINCIPAL | Oscilatorio",
                        "MODO ALTERNATIVO | Invertido",
                        "LETALIDAD | 65 perforante - 65 cortante - 10 contundente",
                        "ARROJADIZA | No"
                ), OptionalDouble.empty(), 0, false,
                WeaponConfigurationPolicy.oneHandedPrimaryAndAlternative(), traits
        ).withCombatPolicy(new WeaponCombatPolicy(ORDINARY_ONE_HANDED));
        dagger.withCombatActionsFor(WeaponActionMode.PRIMARY, ORDINARY_ONE_HANDED);
        dagger.withCombatActionsFor(WeaponActionMode.ALTERNATIVE, ORDINARY_ONE_HANDED);
        dagger.withOffensiveMovesetFor(WeaponActionMode.PRIMARY, ShortBladeMovesetCatalog.dagaOscilatorio());
        dagger.withOffensiveMovesetFor(WeaponActionMode.ALTERNATIVE, ShortBladeMovesetCatalog.dagaInvertido());
        return dagger;
    }

    public static WeaponItem hachaDeLenador() {
        return weapon(
                "Hacha de Leñador",
                "El Hacha de Leñador está diseñada para talar, desramar y dividir madera. Su cabeza de acero concentra la masa detrás de una única hoja funcional, mientras el lado opuesto de la cabeza actúa como contrapeso estructural y el mango proporciona palanca y control. La recuperación de cada golpe debe volver a presentar ese mismo filo antes del siguiente corte.",
                1.40, 6, 2, 0.60, "Principal", 0, 65, 15,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), Set.of(WeaponTrait.ERGONOMIA_SUFICIENTE), ORDINARY_ONE_HANDED
        ).withOffensiveMoveset(BluntToolMovesetCatalog.hachaDeLenador())
                .withProperties(List.of(sufficientErgonomics()));
    }

    public static WeaponItem cimitarra() {
        return weapon(
                "Cimitarra",
                "La Cimitarra es una espada de un solo filo y hoja marcadamente curva, concebida para realizar cortes amplios y continuos. La distribución de su masa y la curvatura favorecen tajos de barrido y permiten que la hoja acompañe la trayectoria del brazo o de una montura sin quedar fácilmente atrapada. Se utiliza a una mano mediante modo de agarre principal y no está diseñada para competir con armas especializadas en estocada.",
                0.95, 10, 2, 1.00, "Principal", 0, 65, 15,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), Set.of(WeaponTrait.ERGONOMIA_SUFICIENTE), ORDINARY_ONE_HANDED
        ).withOffensiveMoveset(SwordMovesetCatalog.cimitarra())
                .withProperties(combine(List.of(sufficientErgonomics()), PersonalTransportUseProperties.all()));
    }


    /** Espada técnica V881 de cinto: la torsión longitudinal controla armas y proyectiles. */
    public static WeaponItem espadaHelicoidal() {
        Set<WeaponTrait> traits = Set.of(WeaponTrait.HELICOIDAL_CONTROL);
        List<AttributeRequirement> requirements = WeaponRequirementPolicy.calculate(
                1.10, 1.16, GripMode.TWO_HANDED, traits);
        Set<WeaponCombatAction> actions = FULL_TWO_HANDED;
        WeaponItem item = new WeaponItem(
                "Espada Helicoidal",
                "La Espada Helicoidal es una espada larga compacta de hoja rígida cuya sección desarrolla una torsión continua de doce grados desde la guarda hasta la punta. Su hoja no presenta las ondulaciones laterales propias de una flamígera: sus caras, filos y planos rotan gradualmente alrededor del eje longitudinal, modificando el ángulo de contacto sin alterar la rectitud estructural. En combate puede ocupar planos muy distintos mediante cortes, una descarga desde guardia alta y una floritura cargada continua; cuando una de esas trayectorias ofensivas intersecta limpiamente otra arma o determinados proyectiles ligeros, la torsión favorece su desplazamiento lateral. Puede reconducir el .46 de plomo neumático y desviar flechas, guijarros, monedas o cuchillos arrojadizos, pero no neutraliza munición de mayor rigidez ni cargas frágiles: una cápsula de amonio, una terracota incendiaria o un huevo de azufre detonan con normalidad al chocar contra la hoja. Su principio no aumenta el daño: aumenta el control.",
                1.16, domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Espada Helicoidal"), 1.10,
                List.of(new WeaponMode("Principal", new LethalityProfile(65, 65, 20))),
                requirements, List.of(),
                List.of(
                        "LONGITUD TOTAL | 1,10 m",
                        "LONGITUD DE HOJA | 0,84 m",
                        "EMPUÑADURA | 21 cm",
                        "GUARDA | 16,5 cm",
                        "ANCHURA MÁXIMA DE HOJA | 4,3 cm",
                        "GROSOR MÁXIMO | 5,2 mm",
                        "TORSIÓN HELICOIDAL | 12° desde la guarda hasta la punta",
                        "MASA DE HOJA | 0,85 kg",
                        "MASA DE EMPUÑADURA Y GUARDA | 0,31 kg",
                        "CENTRO DE MASAS | 8 cm delante de la guarda",
                        "FUERZA | 9",
                        "DESTREZA | 11",
                        "LETALIDAD | 65 perforante - 65 cortante - 20 contundente",
                        "TRANSPORTE | Cinto reforzado",
                        "MIRROR PARRY | Armas elegibles; .46 de plomo neumático; flechas, guijarros, monedas y cuchillos arrojadizos",
                        "ARROJADIZA | No"
                ), OptionalDouble.empty(), 0, false,
                WeaponConfigurationPolicy.twoHandedPrimaryOnly(), traits
        ).withCombatPolicy(new WeaponCombatPolicy(actions));
        item.withCombatActionsFor(WeaponActionMode.PRIMARY, actions);
        return item.withOffensiveMoveset(SwordMovesetCatalog.espadaHelicoidal());
    }

    /** Espadón V881 parcialmente retraíble y acoplable al sistema dorsal exclusivo. */
    public static WeaponItem espadonDeRotor() {
        Set<WeaponTrait> traits = Set.of(WeaponTrait.DE_ROTOR, WeaponTrait.DORSAL_ROTOR_COMPATIBLE);
        List<AttributeRequirement> requirements = WeaponRequirementPolicy.calculate(
                1.30, 3.80, GripMode.TWO_HANDED, traits);
        Set<WeaponCombatAction> actions = EnumSet.of(WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.HEAVY_ATTACK, WeaponCombatAction.CHARGED_ATTACK, WeaponCombatAction.JUMP_ATTACK);
        WeaponItem item = new WeaponItem(
                "Espadón de Rotor",
                "El Espadón de Rotor es un espadón asimétrico de gran pala cuyo principio mecánico recuerda al rotor de un aerostato. Se gobierna de forma ordinaria a dos manos, pero admite un agarre alternativo monomanual de enorme exigencia física para explotar trayectorias que el control bimanual no permite con la misma libertad. El borde conductor es grueso, redondeado y estructural; el borde de resolución es fino y afilado; y la punta concentra el impulso sobre una superficie reducida. Su masa adelantada permite alternar barridos, descargas descendentes, punzadas de máxima extensión, semipiruetas y ataques aéreos de gran arco: algunas transiciones conservan casi toda la inercia y otras la sacrifican deliberadamente para ganar espacio, alcance o alterar el ritmo. No posee un golpe desestabilizador separado; esa entrada ejecuta su ataque cargado. Para transportarlo, una parte de la hoja se retrae dentro del armazón proximal y el conjunto se acopla al Sistema de Transporte Dorsal del Rotor V881, que sustituye a la mochila.",
                3.80, domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Espadón de Rotor"), 1.30,
                List.of(new WeaponMode("Principal", new LethalityProfile(65, 65, 100))),
                requirements, List.of(),
                List.of(
                        "LONGITUD DESPLEGADA | 1,30 m",
                        "LONGITUD RETRAÍDA | 0,82 m",
                        "GUARDA A PUNTA | 0,98 m",
                        "EMPUÑADURA UTILIZABLE | 27 cm",
                        "POMO Y REMATE POSTERIOR | 5 cm",
                        "ANCHURA MÁXIMA DE HOJA | 15 cm",
                        "ANCHURA EN EL ÚLTIMO TERCIO | 10,5 cm",
                        "NERVIO ESTRUCTURAL | 10 mm",
                        "GROSOR MEDIO DEL CUERPO | 5,5 mm",
                        "MASA DE HOJA | 2,92 kg",
                        "MASA DE EMPUÑADURA, GUARDA, POMO Y MECANISMO | 0,88 kg",
                        "CENTRO DE MASAS | 19 cm delante de la guarda",
                        "FUERZA 2H PRIMARY | 38",
                        "DESTREZA 2H PRIMARY | 13",
                        "FUERZA 1H ALTERNATIVE | 48 (base x1,25)",
                        "DESTREZA 1H ALTERNATIVE | 20 (base x1,50)",
                        "TRANSPORTE DORSAL | 9 x 2 retraído",
                        "COMBATE | 13 x 2 desplegado",
                        "ARROJADIZA | No"
                ), OptionalDouble.of(3.80), java.util.Optional.of(domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Espadón de Rotor [RETRAÍDO]")),
                1, true, WeaponConfigurationPolicy.rotorGreatsword(), traits
        ).withCombatPolicy(new WeaponCombatPolicy(actions));
        Set<WeaponCombatAction> rotorActions = EnumSet.of(WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.HEAVY_ATTACK, WeaponCombatAction.CHARGED_ATTACK, WeaponCombatAction.JUMP_ATTACK);
        item.withCombatActionsFor(WeaponActionMode.PRIMARY, rotorActions);
        item.withCombatActionsFor(WeaponActionMode.ALTERNATIVE, rotorActions);
        item.withOffensiveMovesetFor(WeaponActionMode.PRIMARY, RotorGreatswordMovesetCatalog.twoHanded());
        item.withOffensiveMovesetFor(WeaponActionMode.ALTERNATIVE, RotorGreatswordMovesetCatalog.oneHanded());
        item.withCrossModeTransitionProfile(RotorGreatswordMovesetCatalog.crossMode());
        item.withProperties(List.of(transportProperty(ItemPropertyId.COPILOT)));
        return item;
    }

    /** Katana V881 bimanual con canal térmico de amadou y resina, activado por desenvaine. */
    public static WeaponItem katanaTermoMecanicaV881() {
        Set<WeaponTrait> traits = Set.of(WeaponTrait.THERMO_MECHANICAL);
        WeaponConfigurationPolicy configurations = WeaponConfigurationPolicy.twoHandedPrimaryOnly();
        List<AttributeRequirement> requirements = WeaponRequirementPolicy.calculate(1.00, 1.25, GripMode.TWO_HANDED, traits);
        WeaponItem item = new WeaponItem(
                "Katana Termo-mecánica V881",
                "Espada larga de un solo filo cuya hoja, ligeramente más gruesa que la de sus equivalentes convencionales, incorpora junto al lomo una sucesión de pequeños alojamientos destinados a contener amadou impregnado de resina. La base de la vaina integra el mecanismo de ignición: al desenfundar, el desplazamiento de la hoja provoca la ignición del material preparado, produciendo pequeñas llamas distribuidas a lo largo de su lado romo sin recubrir directamente de combustible el acero ni ocultar el filo al portador. Al volver a envainarla, la combustión se extingue y conserva el material restante para el siguiente uso. Una preparación completa proporciona hasta cinco minutos acumulados de combustión antes de que sea necesario renovar el amadou y la resina.",
                1.25, domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor("Katana Termo-mecánica V881"), 1.00,
                List.of(new WeaponMode("Principal", new LethalityProfile(65, 65, 20))),
                requirements, List.of(),
                List.of(
                        "LONGITUD TOTAL | 1,00 m",
                        "PESO | 1,25 kg",
                        "FUERZA | 10",
                        "DESTREZA | 10",
                        "LETALIDAD | 65 perforante - 65 cortante - 20 contundente",
                        "QUEMADURA | +67 mientras la carga térmica está encendida",
                        "CARGA TÉRMICA | 300 s reales acumulados por Amadou + Resina",
                        "MODO | Principal",
                        "ARROJADIZA | No"
                ), OptionalDouble.empty(), 0, false, configurations, traits
        ).withCombatPolicy(new WeaponCombatPolicy(FULL_TWO_HANDED));
        item.withCombatActionsFor(WeaponActionMode.PRIMARY, FULL_TWO_HANDED);
        return item.withOffensiveMoveset(SwordMovesetCatalog.katanaTermoMecanica())
                .withProperties(PersonalTransportUseProperties.all());
    }

    /** Maza V881 monomanual: el ataque fuerte consume una carga eléctrica disponible. */
    public static WeaponItem mazaElectroMecanicaV881() {
        Set<WeaponTrait> traits = Set.of(WeaponTrait.ELECTRO_MECHANICAL_HEAVY);
        Set<WeaponCombatAction> actions = EnumSet.of(
                WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.HEAVY_ATTACK,
                WeaponCombatAction.JUMP_ATTACK, WeaponCombatAction.DESTABILIZE);
        WeaponItem item = weapon(
                "Maza Electro-mecánica V881",
                "Maza compacta provista de una cabeza de acero atravesada por contactos conductores eléctricamente aislados entre sí. En el interior del arma, una celda galvánica alimenta lentamente un pequeño acumulador capaz de liberar su energía durante el impacto. Cuando la carga está disponible, pequeñas chispas entre los contactos de la cabeza delatan el estado del mecanismo; al ejecutar un golpe fuerte y alcanzar el objetivo, la energía acumulada se descarga a través de los contactos y el sistema comienza inmediatamente un nuevo ciclo de carga de doce segundos. Si el acumulador todavía no está preparado, el mismo gesto conserva su función como ataque fuerte puramente físico, sin exigir al portador ningún indicador o control adicional.",
                1.00, 5, 2, 0.50, "Principal", 0, 0, 80,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), traits, actions
        );
        return item.withOffensiveMoveset(BluntToolMovesetCatalog.mazaElectroMecanica())
                .withProperties(PersonalTransportUseProperties.all());
    }

    public static WeaponItem martilloDeBola() {
        WeaponItem item = weapon(
                "Martillo de bola", ConventionalMeleeWeaponBasicCatalog.martilloDeBola().narrativeDescription(),
                0.65, 4, 2, 0.35, "Principal", 0, 0, 30,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(),
                Set.of(WeaponTrait.ERGONOMIA_SUFICIENTE), ORDINARY_ONE_HANDED
        );
        return item.withOffensiveMoveset(BluntToolMovesetCatalog.martilloDeBola())
                .withProperties(combine(List.of(sufficientErgonomics()), PersonalTransportUseProperties.all()));
    }

    public static WeaponItem hoz() {
        Set<WeaponCombatAction> actions = EnumSet.of(WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.HEAVY_ATTACK, WeaponCombatAction.JUMP_ATTACK, WeaponCombatAction.DESTABILIZE);
        WeaponItem item = weapon(
                "Hoz", ConventionalMeleeWeaponBasicCatalog.hoz().narrativeDescription(),
                0.25, 4, 2, 0.40, "Principal", 65, 65, 10,
                WeaponConfigurationPolicy.oneHandedPrimaryOnly(), Set.of(), actions
        );
        return item.withOffensiveMoveset(HookedPolearmMovesetCatalog.hoz())
                .withProperties(combine(List.of(hookProperty()), PersonalTransportUseProperties.all()));
    }

    public static WeaponItem guadana() {
        WeaponItem item = weapon(
                "Guadaña", ConventionalMeleeWeaponBasicCatalog.guadana().narrativeDescription(),
                1.30, 16, 2, 1.60, "Principal", 65, 65, 30,
                WeaponConfigurationPolicy.twoHandedPrimaryOnly(), Set.of(), FULL_TWO_HANDED
        );
        return item.withOffensiveMoveset(HookedPolearmMovesetCatalog.guadana())
                .withProperties(List.of(hookProperty(), transportProperty(ItemPropertyId.COPILOT)));
    }

    public static WeaponItem horca() {
        Set<WeaponCombatAction> actions = EnumSet.of(WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.JUMP_ATTACK, WeaponCombatAction.DESTABILIZE);
        return weapon(
                "Horca", ConventionalMeleeWeaponBasicCatalog.horca().narrativeDescription(),
                1.80, 16, 3, 1.60, "Principal", 25, 20, 15,
                WeaponConfigurationPolicy.twoHandedPrimaryOnly(), Set.of(WeaponTrait.RESIN_REPAIR), actions
        ).withOffensiveMoveset(PitchforkMovesetCatalog.horca());
    }

    public static WeaponItem bo() {
        Set<WeaponTrait> traits = Set.of(WeaponTrait.NON_DEGRADING, WeaponTrait.STAFF_FLOURISH_HANDLING);
        Set<WeaponCombatAction> actions = EnumSet.of(WeaponCombatAction.LIGHT_ATTACK, WeaponCombatAction.HEAVY_ATTACK,
                WeaponCombatAction.CHARGED_ATTACK, WeaponCombatAction.JUMP_ATTACK);
        WeaponItem item = weapon(
                "Bō", ConventionalMeleeWeaponBasicCatalog.varaDeMadera().narrativeDescription(),
                1.00, 18, 1, 1.80, "Principal", 0, 0, 15,
                WeaponConfigurationPolicy.boStaff(), traits, actions
        );
        item.withCombatActionsFor(WeaponActionMode.PRIMARY, actions);
        item.withCombatActionsFor(WeaponActionMode.ALTERNATIVE, actions);
        item.withOffensiveMovesetFor(WeaponActionMode.PRIMARY, BoStaffMovesetCatalog.twoHanded());
        item.withOffensiveMovesetFor(WeaponActionMode.ALTERNATIVE, BoStaffMovesetCatalog.oneHanded());
        item.withCrossModeTransitionProfile(BoStaffMovesetCatalog.crossMode());
        return item.withProperties(List.of(transportProperty(ItemPropertyId.COPILOT)));
    }

    public static WeaponItem boathook() {
        Set<WeaponCombatAction> actions = EnumSet.of(WeaponCombatAction.LIGHT_ATTACK,
                WeaponCombatAction.HEAVY_ATTACK, WeaponCombatAction.JUMP_ATTACK, WeaponCombatAction.DESTABILIZE);
        WeaponItem item = weapon(
                "Boathook", ConventionalMeleeWeaponBasicCatalog.boathook().narrativeDescription(),
                0.85, 18, 2, 1.80, "Principal", 0, 0, 80,
                WeaponConfigurationPolicy.twoHandedPrimaryOnly(),
                Set.of(WeaponTrait.NON_DEGRADING, WeaponTrait.HOOKS_WITH_BLUNT), actions
        );
        return item.withOffensiveMoveset(HookedPolearmMovesetCatalog.boathook())
                .withProperties(List.of(hookProperty(), dismountProperty(), transportProperty(ItemPropertyId.COPILOT)));
    }

    private static ItemProperty hookProperty() {
        return ItemProperty.alwaysActive(ItemPropertyId.HOOK, "ENGANCHAR",
                "El ataque fuerte que produzca el tipo de daño real exigido por el arma atrae al adversario dos tercios de la longitud total del arma y aplica su respuesta de staggering.",
                "Atracción de 2/3 del alcance en ataque fuerte válido");
    }

    private static ItemProperty dismountProperty() {
        return ItemProperty.alwaysActive(ItemPropertyId.DISMOUNT, "DESMONTAR",
                "Un impacto válido que produzca daño contundente real contra una persona montada la hace caer de su Transporte Personal y aplica la fórmula de retroceso.",
                "Desmonta por impacto contundente real");
    }

    private static WeaponItem weapon(
            String name, String narrative, double weight, int verticalSlots, int horizontalSlots,
            double reach, String modeName, double piercing, double slashing, double blunt,
            WeaponConfigurationPolicy configurations, Set<WeaponTrait> traits,
            Set<WeaponCombatAction> actions
    ) {
        GripMode effectiveGrip = configurations.configurations().getFirst().gripMode();
        List<AttributeRequirement> requirements = WeaponRequirementPolicy.calculate(reach, weight, effectiveGrip, traits);
        WeaponItem item = new WeaponItem(
                name, narrative, weight, domain.inventory.logistics.WeaponPhysicalDimensionsCatalog.footprintFor(name), reach,
                List.of(new WeaponMode(modeName, new LethalityProfile(piercing, slashing, blunt))),
                requirements, List.of(),
                List.of(
                        "PESO (kg) | " + format(weight),
                        "ALCANCE (m) | " + format(reach),
                        "FUERZA | " + requirement(requirements, Attribute.FUERZA),
                        "DESTREZA | " + requirement(requirements, Attribute.DESTREZA),
                        "LETALIDAD | " + format(piercing) + " perforante - " + format(slashing)
                                + " cortante - " + format(blunt) + " contundente",
                        "ARROJADIZA | No"
                ), OptionalDouble.empty(), 0, false, configurations, traits
        ).withCombatPolicy(new WeaponCombatPolicy(actions));
        for (WeaponConfiguration configuration : configurations.configurations()) {
            item.withCombatActionsFor(configuration.actionMode(), actions);
        }
        return item;
    }

    private static ItemProperty sufficientErgonomics() {
        return ItemProperty.alwaysActive(
                ItemPropertyId.SUFFICIENT_ERGONOMICS,
                "ERGONOMÍA SUFICIENTE",
                "Permite blandir el arma a una mano, aunque no cumpla los requisitos para ello.",
                "Uso monomanual permitido sin satisfacer FUERZA ideal"
        );
    }

    private static ItemProperty transportProperty(ItemPropertyId id) {
        return PersonalTransportUseProperties.all().stream()
                .filter(property -> property.id() == id)
                .findFirst().orElseThrow();
    }

    private static List<ItemProperty> combine(List<ItemProperty> first, List<ItemProperty> second) {
        java.util.ArrayList<ItemProperty> result = new java.util.ArrayList<>(first);
        result.addAll(second);
        return List.copyOf(result);
    }

    private static int requirement(List<AttributeRequirement> requirements, Attribute attribute) {
        return requirements.stream().filter(r -> r.attribute() == attribute).findFirst().orElseThrow().minimumValue();
    }

    private static String format(double value) {
        if (Math.rint(value) == value) return Integer.toString((int) value);
        return String.format(java.util.Locale.ROOT, "%.2f", value).replace('.', ',');
    }
}
