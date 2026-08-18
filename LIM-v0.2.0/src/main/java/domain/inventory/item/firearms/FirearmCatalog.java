package domain.inventory.item.firearms;

import domain.inventory.item.LethalityProfile;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.PersonalTransportUseProperties;
import domain.inventory.item.firearmAccessories.FirearmAccessoryMount;
import java.util.List;
import java.util.Set;
import java.util.EnumSet;

/** Catálogo canónico  de firearms V881. */
public final class FirearmCatalog {
    private FirearmCatalog() {}

    public static final String REPEATING_PNEUMATIC_RIFLE_NARRATIVE = """
            El Rifle Neumático de Repetición V881 conserva la arquitectura militar de aire comprimido que sobrevivió a las guerras napoleónicas pese a sus problemas iniciales de depósitos, juntas, válvulas y mantenimiento. Valerian no sustituyó el principio: normalizó sus piezas, reforzó el depósito y permitió al propio tirador restaurar presión mediante el mecanismo frontal, con manómetro permanente y veinte disparos por carga neumática completa.

            Su cartucho tubular lateral contiene veinte proyectiles calibre .46 de plomo sin camisa de cobre. El estándar V881 eleva su perfil a 55 perforante sin convertirlo en un arma de pólvora: mantiene ausencia de fogonazo y residuos de combustión, 150 m de alcance efectivo y una identidad de precisión sigilosa y asalto controlado. Cuando un impacto alcanza la cabeza y su perforación supera la protección perforante efectiva, activa GOLPE DE GRACIA.
            """;

    public static final String BIFILAR_ELECTROMAGNETIC_RIFLE_NARRATIVE = """
            El Fusil Bifilar Electromagnético V881 nació de la convergencia entre almacenamiento eléctrico, generación dinamoeléctrica, conductores, aislantes y aceleración electromagnética. Los experimentos de Kristian Birkeland constituyeron uno de sus antecedentes más claros. Valerian condensó esa rama en un arma de 1,50 metros y 8,70 kilogramos construida alrededor de dos raíles longitudinales de CuCrZr revestidos con una red superficial de tungsteno. La boca rectangular, el guardamanos aislante y el banco de condensadores ocupan su mitad delantera; detrás se encuentran la recámara, el medidor lateral de carga y una culata de nogal reforzado con amortiguación por resorte.

            La munición se alimenta desde un cargador inferior extraíble de cinco cartuchos bifilares unitarios. Cada cartucho reúne un núcleo .46 ojival prismático de tungsteno de 32 gramos, una armadura conductora desechable y un sabot separable que lo centra dentro del canal. Al disparar, la armadura establece contacto con ambos raíles y transmite el impulso al sabot; al abandonar la boca, las piezas auxiliares se separan y solo el proyectil continúa en vuelo. El paquete eleva la eficiencia global y permite seleccionar 50, 60, 70, 80 o 90 perforante, con alcances efectivos entre 180 y 420 metros.

            La energía procede de un banco capaz de almacenar hasta 1.650 J. Una batería extraíble de dos celdas 21700, alojada delante de la culata, carga automáticamente los condensadores, mientras una manivela plegable de 15 centímetros situada en el lado derecho acciona en paralelo una dinamo con transmisión multiplicadora. El personaje gira la manivela automáticamente a una revolución por segundo siempre que el arma necesite carga y ninguna acción preferente lo interrumpa; cada vuelta entrega 47,14 J al banco. Sin batería, el fusil conserva íntegramente la carga dinamoeléctrica automática de hasta 35 vueltas equivalentes.

            El voltímetro analógico del banco muestra carga continua y cinco estados discrecionales P50-P90. Cada disparo consume la carga seleccionada y activa un bloqueo térmico de 2,11 a 16,82 segundos; durante ese intervalo la batería y la dinamo preparan el siguiente disparo. Pesado y deliberado, admite correa, bípode y óptica desmontables para tiro preparado. Su descarga produce un golpe electromecánico grave, un zumbido descendente y un destello azul-blanquecino; FULMINANTE se evalúa exclusivamente sobre HEAD o CHEST cuando la protección perforante porcentual efectiva queda por debajo de la penetración instantánea y la cobertura regional es incompleta: menos del 100 % en cabeza o del 50 % en coraza.
            """;

    public static final String AUTOLOADING_PISTOL_NARRATIVE = """
            La Pistola Autocargadora V881 representa la maduración de la rama de armas cortas de pólvora sin humo. Su innovación no es un selector de fuego, sino la simplicidad operativa: cada presión del gatillo efectúa un único disparo y el propio ciclo mecánico deja preparado el siguiente. Al agotarse la munición, el cargador completo de ocho cartuchos se sustituye de una vez.

            El cartucho .45 utiliza proyectil de plomo con camisa de cobre. La plataforma conserva 65 perforante y 35 contundente, alcance efectivo de 50 m, agarre estrictamente monomanual y GOLPE DE GRACIA sobre cabeza cuando la cabeza no está cubierta al 100 % y la perforación supera su protección. La prioridad sigue siendo fiabilidad personal e inmediatez, no volumen de fuego continuo.
            """;
public static final String SUBMACHINE_GUN_NARRATIVE = """
            El Subfusil Automático V881 no nació de la búsqueda del arma automática más potente. Los arsenales de Valerian ya habían demostrado que podían construir fusiles capaces de disparar de forma continuada, alcanzar distancias mayores y conservar una energía terminal superior. El problema apareció cuando se intentó reunir todas esas prestaciones en una misma plataforma. Cada incremento de alcance, potencia o versatilidad añadía masa, mecanismos, exigencias de control y necesidades logísticas que aportaban poco en aquellas situaciones donde el combate terminaba resolviéndose a unas decenas de metros.
            La respuesta fue dejar de exigir al arma automática que se comportase también como un fusil. Se redujeron deliberadamente alcance y potencia individual, se eliminó cualquier selector de cadencia y se descartó la óptica. El cartucho de 9 mm con proyectil de plomo y camisa de cobre permitía transportar veinticinco disparos en un cargador compacto y alimentar un mecanismo exclusivamente automático cuya cadencia relativamente lenta favorecía tanto el control como la longevidad mecánica. El arma resultante debía sujetarse a dos manos, pero era más corta, manejable y sencilla que los fusiles automáticos que la precedieron.
            Esa especialización resolvió también un problema doctrinal. A 100 metros o menos, una óptica y un sistema destinado a colocar disparos aislados a larga distancia dejaban de justificar su masa y complejidad. El Subfusil renunció incluso al AIMING convencional: el operador orienta directamente el arma y controla una ráfaga cuyo retroceso inicial obliga a corregir solo el primer disparo antes de estabilizarse. La plataforma no intenta sustituir al fusil de servicio. Existe precisamente porque dejó de intentarlo.
            El resultado conserva la lección que había conducido a los arsenales V881 hacia sus mejores armas: una prestación que solo funciona en condiciones ideales tiene menos valor que otra inferior capaz de repetirse miles de veces. Con 85 perforante, 35 contundente y GOLPE DE GRACIA, el Subfusil posee potencia suficiente para su distancia doctrinal sin perseguir cifras que encarecerían el cartucho o comprometerían la fiabilidad. Su cargador, su mecanismo y su munición podían fabricarse y distribuirse a gran escala sin convertir cada unidad desplegada en su propio taller.
            """;
public static final String REPEATING_RIFLE_NARRATIVE = """
            El Fusil de Repetición V881 representa la culminación convencional del cartucho de pólvora sin humo para tiro de servicio a larga distancia. Su arquitectura prioriza robustez, precisión y potencia sobre automatismo: cinco cartuchos 7,92×57 mm de plomo con camisa de cobre se introducen como una unidad completa y cada presión del gatillo resuelve un único disparo 1A.

            Con 400 m de alcance efectivo base, 95 perforante y 35 contundente, el arma supera el umbral de la mayoría de protecciones convencionales sin recurrir a electromagnetismo. Puede activar GOLPE DE GRACIA en cabeza, admite correa y óptica desmontables, y no admite bípode. La bayoneta frontal sustituye al antiguo culatazo como golpe desestabilizador y permite una carga sostenida mientras se mantenga la entrada de ataque cargado, consumiendo PA con la misma tasa que correr. Las ópticas Fiedler, Zeiss y Winchester A5 proporcionan aumentos ×3, ×4 y ×5 respectivamente y PRECISIÓN ASISTIDA, pero no modifican el alcance efectivo intrínseco de 1.500 metros ni la letalidad del proyectil.
            """;

    public static final String ARC_INDUCTION_LANCE_NARRATIVE = """
            El Lanza-Arcos Electrodinámico V881 no nació como arma. Sus antecesores fueron aparatos de demostración: máquinas electrostáticas accionadas por manivela, bobinas de inducción, acumuladores y dispositivos capaces de producir chispas visibles para laboratorios, gabinetes científicos y exhibiciones públicas. Aquellos instrumentos demostraban que una carga podía generarse mecánicamente, almacenarse y liberarse a través del aire, pero también mostraban sus límites: poca distancia, trayectorias impredecibles, aislamiento delicado y una gran dificultad para repetir la descarga con regularidad.

            Valerian conservó esa familia tecnológica cuando todavía era, en esencia, curiosidad científica y espectáculo. El refinamiento no consistió en descubrir una electricidad distinta, sino en reducir pérdidas, compactar los generadores, mejorar los aislantes, estabilizar la conmutación y aprender a controlar dónde podía iniciarse una descarga. Las antiguas máquinas de influencia y las bobinas de inducción dejaron de ser aparatos aislados y se convirtieron en componentes de una misma arquitectura portátil.

            El paso decisivo fue abandonar la pretensión de lanzar un rayo recto a gran distancia. El estándar V881 aceptó que el aire seguía imponiendo un límite severo y convirtió esa limitación en la función del arma. Una boquilla frontal ancha concentra el sistema sobre un sector horizontal de apenas unos metros; si existen objetivos válidos dentro de él, los módulos eléctricos disponibles alimentan los canales de arco que consiguen establecerse. Si no existe ningún camino útil, la descarga muere en la propia boquilla.

            El modelo definitivo se construyó alrededor de movilidad y repetición inmediata. Una manivela ligera carga de forma continua la reserva interna mientras el operador se desplaza, y un medidor muestra cuándo se alcanzan el primer, segundo y tercer módulo operativo. Tres vueltas equivalentes bastan para habilitar el primero; cinco para disponer de dos; seis representan la capacidad completa. La carga es continua: a los 1,2 segundos puede dispararse el primer módulo o mantenerse la acción preferente hasta 2,1 o 3 segundos. La reserva acumulada se reparte entre todos los blancos válidos dentro del alcance; E100 entre diez objetivos produce E10 en cada uno.

            Así surgió el Lanza-Arcos Electrodinámico V881: no como sustituto de un rifle, sino como descendiente militar de una tecnología que durante mucho tiempo había servido para hacer visible la electricidad. La revisión tardía V881 adoptó el mismo principio de alimentación híbrida que había madurado en el Fusil Bifilar: una Batería Portátil Electromagnética acoplada estabiliza y asiste el banco de condensadores mientras la manivela, como acción manual preferente, se acciona automáticamente siempre que ninguna acción de mayor prioridad la interrumpa. A diferencia del Bifilar, el consumo energético del Lanza-Arcos no obliga a ciclos de cargador portátil durante su uso ordinario.

            El electrómetro no selecciona una potencia discreta: muestra una carga continua y tres umbrales correspondientes a las tres bobinas. Una bobina completamente cargada aporta E100 bruto; dos aportan E200 y las tres E300. Los canales pueden adquirir blancos distintos, pero cuando convergen sobre un único objetivo sus reservas se suman íntegramente. Por eso una descarga completa contra una pieza conductora puede resultar catastrófica, mientras que repartir los tres canales conserva la función original de adquisición múltiple.

            Su alcance sigue siendo corto, su descarga sigue dependiendo de encontrar un camino válido y su operador debe soportar la sacudida del mecanismo; pero dentro de ese límite convierte la antigua chispa de demostración en un arma móvil de adquisición múltiple.
            """;

    public static final String LIME_SPRAYER_NARRATIVE = """
            El Rociador de Cal Viva V881 surgió como un arma química portátil de muy corto alcance destinada tanto al ataque directo como a la negación temporal del terreno. Su funcionamiento no implica combustión ni proyecta llama: el daño por Quemadura procede del agente rociado. A una distancia efectiva máxima de 2,5 metros, el sistema aplica un impacto cada 0,5 segundos de exposición, con Quemadura 67 y Veneno 100. Cuando alcanza una superficie, ésta permanece durante 30 segundos bajo los estados ambientales Toxicidad Virulenta y Quemadura Asfixiante.

            Su depósito admite 3 litros y proporciona 28 segundos de rociado continuo. La carga se suministra mediante cartuchos reemplazables transportados en estuches de hasta cinco unidades. El arma adopta una configuración semejante a una regadera técnica de gran capacidad y admite correa desmontable. Equipada con ella obtiene MONOMANUAL ASISTIDO, permitiendo que el operador la dirija con una sola mano pese a superar las condiciones ideales de un arma intrínsecamente monomanual.

            La propiedad CORROSIVO convirtió al Rociador en una amenaza distinta de las armas convencionales. Cada impacto reduce en un punto la protección Contundente de la región protegida alcanzada mientras todavía conserve protección; sobre un objetivo bajo EMPAPADO, la degradación aumenta a dos puntos por impacto. El resultado fue una presión selectiva sobre la arquitectura defensiva, además del peligro que el agente representaba directamente para el combatiente.

            Las armaduras de escamas y las cotas de malla fueron las primeras grandes perjudicadas. Sus numerosas discontinuidades y su construcción mediante gran cantidad de elementos pequeños dejaron de justificar su relación entre masa y protección cuando el entorno ofensivo dejó de estar dominado exclusivamente por perforación, corte y contundencia. No desaparecieron necesariamente del uso civil o residual, pero dejaron de constituir soluciones militares principales.

            La protección lamelar de acero siguió una evolución diferente. En lugar de intentar conservar una cobertura metálica extensa, concentró el material donde proporcionaba mayor rendimiento defensivo y redujo el acero innecesario. El resultado fue una arquitectura próxima conceptualmente a un chaleco militar, complementable según la misión mediante placas localizadas en hombros, antebrazos, caderas, rodillas u otras regiones. Esos elementos podían utilizarse simétrica o asimétricamente. El soldado V881 dejó así de representar una versión progresivamente aligerada del caballero y pasó a utilizar una doctrina defensiva propia.

            La descendencia de la armadura de placas milanesa afrontó el problema contrario. Su identidad dependía de una cobertura metálica extensa y, para conservarla dentro del nuevo entorno ofensivo, fue necesario incrementar masa, tratamientos y complejidad multicapa al mismo tiempo que se renunciaba a la cobertura corporal completa. El Conjunto del Caballero V881 conserva por ello una genealogía inequívoca de armadura de placas sin limitarse a reproducir una armadura histórica mediante materiales mejores.

            La armadura de ébano también tuvo que transformarse. Su ausencia de desgaste continuó siendo una propiedad militar demasiado valiosa para abandonarla, pero la vulnerabilidad del material impidió confiar exclusivamente en sus características originales. La respuesta fue aumentar masa e introducir protección complementaria. El resultado conservó la longevidad excepcional del ébano pagando por ella mediante una arquitectura defensiva más pesada: su supervivencia dentro del estándar V881 procede de adaptación tecnológica, no de inmunidad material.

            Paralelamente, la generalización de la pólvora sin humo y el desarrollo de una balística experimental suficientemente precisa introdujeron una segunda presión selectiva. En este caso el problema principal no fue la degradación material, sino la distribución de cobertura. Cuando las armas comenzaron a colocar proyectiles con suficiente precisión a distancias tácticas, dedicar una fracción considerable de la masa equipada a una protección craneal incompleta perdió progresivamente rentabilidad militar.

            Una protección de cabeza incapaz de impedir con suficiente regularidad el resultado para el que había sido concebida podía resultar una inversión inferior a dedicar esa misma masa a torso, movilidad, munición u otro equipo. De esta presión surgió la pérdida general de protagonismo del casco en el campo de batalla V881, sobreviviendo principalmente aquellas soluciones especializadas cuya función concreta todavía justificaba su masa y cobertura.

            El estándar defensivo V881 es, por tanto, resultado de dos presiones tecnológicas complementarias. El Rociador de Cal Viva modificó qué materiales y arquitecturas podían seguir justificándose; la pólvora sin humo y la precisión balística modificaron dónde seguía siendo rentable colocar la protección. Las armaduras supervivientes no son versiones progresivamente mejores de sus antecesoras, sino respuestas diferentes a ambas presiones.
            """;

    public static final String ANTI_MATERIEL_CANNON_NARRATIVE = """
El Cañón Antimaterial V881 apareció cuando los arsenales de Valerian comprobaron que el salto entre las armas personales y la armamento estratégico pesado era demasiado grande. Los cañones de riel estáticos habían alcanzado un poder devastador en tierra, mar y aire, pero precisamente su masa, alimentación y emplazamiento limitaban su capacidad para seguir blancos móviles o acompañar a una unidad que necesitaba cambiar de posición. El objetivo no fue sustituirlos, sino llevar una fracción suficiente de potencia antimaterial allí donde su ergonomía impedía maniobrar con la rapidez necesaria.

La misma doctrina había cambiado todavía más con el bombardeo cinético orbital. Las varas pesadas de tungsteno —seis metros de longitud, treinta centímetros de diámetro y unas ocho toneladas— permitían castigar objetivos estratégicos desde arriba; su versión ligera dispersaba numerosos proyectiles de 4,60 cm por 1 cm, capaces de producir impactos locales muy violentos contra hormigón. Ambos sistemas eran decisivos contra blancos cuya posición podía conocerse y mantenerse, pero una embarcación ligera que maniobraba por mar, una aeronave que cambiaba de vector o una posición táctica parcialmente oculta podían quedar fuera de la solución más eficiente.

El Cañón Antimaterial V881 ocupó ese hueco. Sus veinte kilogramos, la alimentación de cuatro proyectiles de 20 mm y el ciclo deliberadamente lento de un disparo por segundo aceptaban una ergonomía severa a cambio de disponer de una pieza transportable capaz de atacar vehículos logísticos, búnkeres, posiciones protegidas y blancos materiales difíciles. También podía emplearse como defensa antiaérea o contra buques ligeros cuando un cañón de riel estático no podía seguir el objetivo con suficiente maniobrabilidad o cuando la precisión de un bombardeo cinético orbital no justificaba emplearlo sobre un blanco móvil.

El arma conserva AIMING y admite correa, bípode y óptica porque su función depende de colocar con precisión impactos muy costosos. No pretende ser cómoda: opera en un mundo donde tanques y artillería convencional son doctrinalmente obsoletos frente a railguns V881, negación electroatmosférica e infantería especializada. Representa el límite que V881 consideró razonable para convertir una pieza antimaterial convencional en una plataforma todavía desplazable por una dotación.
""";

    public static final String CLUSTER_CANNON_NARRATIVE = """
El Cañón de Racimo V881 surgió de un problema distinto. Valerian ya podía destruir un punto mediante cañones de riel estáticos de enorme potencia o mediante bombardeo cinético orbital, pero no toda amenaza exigía perforar un objetivo concreto. Trincheras, cubiertas, posiciones dispersas y zonas de paso podían seguir siendo útiles incluso después de que un impacto puntual destruyera parte de ellas. La nueva plataforma se diseñó para invalidar un volumen de terreno, no para competir con el Cañón Antimaterial en penetración.

Los cañones de riel continuaron siendo devastadores en tierra, mar y aire, y las varas orbitales pesadas de tungsteno —seis metros por treinta centímetros y unas ocho toneladas— transformaron la destrucción estratégica. La variante orbital ligera, basada en numerosos proyectiles de 4,60 cm por 1 cm, extendió esa lógica a impactos locales contra hormigón. Sin embargo, los sistemas orbitales seguían dependiendo de acertar sobre un blanco cuya posición podía cambiar y los emplazamientos de riel no siempre podían girar o trasladarse con la rapidez requerida. Un objetivo aéreo bajo, una embarcación ligera en maniobra o una posición terrestre extendida podían exigir una respuesta táctica más inmediata y distribuida.

El Cañón de Racimo V881 trasladó esa respuesta a un cohete individual de 85 mm y cuatro kilogramos. Al actuar por impacto o por un temporizador seleccionable de tres, cuatro o cinco segundos, el mismo evento terminal aplica dentro de veinticinco metros 100 de daño cortante, 100 de Quemadura y Quemadura Asfixiante. Su finalidad es negar posiciones, castigar trincheras y cubiertas, y ofrecer una opción antiaérea o antibuque ligero cuando importa cubrir un espacio probable de paso o maniobra en lugar de perforar un punto único.

La plataforma conserva AIMING y admite correa, bípode y óptica. Su munición se transporta de forma individual y obliga a pagar en masa y espacio cada intento. El resultado no reemplaza a los sistemas estratégicos V881: cubre precisamente los escenarios en los que su potencia devastadora resulta demasiado estática, demasiado imprecisa frente a un blanco móvil o simplemente innecesaria para la escala del problema.
""";

    private static List<ItemProperty> transportProperties(ItemPropertyId... ids) {
        EnumSet<ItemPropertyId> selected = ids.length == 0
                ? EnumSet.noneOf(ItemPropertyId.class)
                : EnumSet.copyOf(List.of(ids));
        return PersonalTransportUseProperties.all().stream()
                .filter(property -> selected.contains(property.id()))
                .toList();
    }

    public static PneumaticFirearmItem repeatingPneumaticRifleV881() {
        FirearmCartridge cartridge = new FirearmCartridge("Cartucho .46 de plomo", ".46", "Plomo", "Tubular lateral", 20, 0.380);
        PneumaticFirearmItem item = new PneumaticFirearmItem(
                "Rifle Neumático de Repetición V881", REPEATING_PNEUMATIC_RIFLE_NARRATIVE,
                4.50, 1.20, 0.20, 150.0, ".46", cartridge,
                new LethalityProfile(55, 0, 0), 0.64, List.of(FireMode.ONE_A), false, true, Set.of(), 20);
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING, FirearmAccessoryMount.BIPOD, FirearmAccessoryMount.OPTIC));
        item.declareItemProperties(transportProperties(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN));
        return item;
    }

    public static ElectromagneticFirearmItem bifilarElectromagneticRifleV881() {
        FirearmCartridge cartridge = new FirearmCartridge("Cargador bifilar .46 V881", ".46", "Núcleo de tungsteno, armadura conductora y sabot separable", "Cartucho bifilar unitario", 5, 0.300, new domain.inventory.InventoryFootprint(1, 2));
        FirearmProjectileDefinition projectile = new FirearmProjectileDefinition(".46", 0.032, "Aleación pesada de tungsteno", "Ojival prismática");
        ElectromagneticFirearmItem item = new ElectromagneticFirearmItem(
                "Fusil Bifilar Electromagnético V881", BIFILAR_ELECTROMAGNETIC_RIFLE_NARRATIVE,
                8.70, 1.50, 0.20, ".46", cartridge, projectile, List.of(FireMode.ONE_A), false, true, Set.of());
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING, FirearmAccessoryMount.BIPOD, FirearmAccessoryMount.OPTIC));
        return item;
    }

    public static AutoloadingPistolFirearmItem autoloadingPistolV881() {
        FirearmCartridge magazine = new FirearmCartridge("Cargador .45 de Pistola V881", ".45", "Plomo con camisa de cobre", "Ojiva redondeada facetada", 8, 0.185);
        AutoloadingPistolFirearmItem item = new AutoloadingPistolFirearmItem(
                "Pistola Autocargadora V881", AUTOLOADING_PISTOL_NARRATIVE,
                0.92, 0.49, 0.16, 50.0, ".45", magazine,
                new LethalityProfile(65, 0, 35), 0.75, Set.of());
        item.declareItemProperties(transportProperties(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN, ItemPropertyId.BICYCLAR, ItemPropertyId.MOTORCYCLAR));
        return item;
    }
public static SubmachineGunFirearmItem submachineGunV881() {
        FirearmCartridge magazine = new FirearmCartridge("Cargador de 9 mm V881", "9 mm", "Plomo con camisa de cobre", "Ojiva de servicio V881", 25, 0.400);
        SubmachineGunFirearmItem item = new SubmachineGunFirearmItem(
                "Subfusil Automático V881", SUBMACHINE_GUN_NARRATIVE,
                3.85, 0.83, 0.24, 100.0, "9 mm", magazine,
                new LethalityProfile(85, 0, 35), 0.38, 500, Set.of());
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING));
        item.declareItemProperties(transportProperties(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN));
        return item;
    }
public static RepeatingRifleFirearmItem repeatingRifleV881() {
        FirearmCartridge clip = new FirearmCartridge("Cartucho completo 7,92×57 mm V881", "7,92×57 mm", "Plomo con camisa de cobre", "Cartucho de fusil V881", 5, 0.145);
        RepeatingRifleFirearmItem item = new RepeatingRifleFirearmItem(
                "Fusil de Repetición V881", REPEATING_RIFLE_NARRATIVE,
                4.05, 1.10, 0.20, 1500.0, "7,92×57 mm", clip,
                new LethalityProfile(95, 0, 35), 0.70, Set.of());
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING, FirearmAccessoryMount.OPTIC));
        item.declareItemProperties(transportProperties(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN));
        return item;
    }

    public static AntiMaterielCannonFirearmItem antiMaterielCannonV881() {
        FirearmCartridge cartridge = new FirearmCartridge(
                "Cartucho de 4 proyectiles de 20 mm V881", "20 mm", "Proyectil antimaterial V881",
                "20 mm × 180 mm · 0,130 kg por proyectil", 4, 0.650, new domain.inventory.InventoryFootprint(2, 2));
        AntiMaterielCannonFirearmItem item = new AntiMaterielCannonFirearmItem(ANTI_MATERIEL_CANNON_NARRATIVE, cartridge);
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING, FirearmAccessoryMount.BIPOD, FirearmAccessoryMount.OPTIC));
        return item;
    }

    public static ClusterCannonFirearmItem clusterCannonV881() {
        FirearmLoadDefinition load = new FirearmLoadDefinition(
                "Cohete individual de Racimo V881",
                new domain.inventory.item.ammunition.AmmunitionDescriptor(
                        domain.inventory.item.ammunition.AmmunitionFamily.ROCKET, "85 mm", "Carga de racimo V881",
                        "Cohete individual 85 mm × 650 mm", false), 1);
        ClusterCannonFirearmItem item = new ClusterCannonFirearmItem(CLUSTER_CANNON_NARRATIVE, load);
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING, FirearmAccessoryMount.BIPOD, FirearmAccessoryMount.OPTIC));
        return item;
    }

    public static ArcInductionFirearmItem arcInductionLanceV881() {
        ArcInductionFirearmItem item = new ArcInductionFirearmItem(ARC_INDUCTION_LANCE_NARRATIVE);
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING));
        item.declareItemProperties(transportProperties(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN));
        return item;
    }

    public static LimeSprayerItem limeSprayerV881() {
        LimeSprayerItem item = new LimeSprayerItem(LIME_SPRAYER_NARRATIVE);
        item.declareFirearmAccessoryMounts(Set.of(FirearmAccessoryMount.SLING));
        item.declareItemProperties(transportProperties(ItemPropertyId.COPILOT, ItemPropertyId.EQUESTRIAN, ItemPropertyId.BICYCLAR, ItemPropertyId.MOTORCYCLAR));
        return item;
    }

    public static List<FirearmItem> all() {
        return List.of(repeatingPneumaticRifleV881(), bifilarElectromagneticRifleV881(), autoloadingPistolV881(),
                submachineGunV881(), repeatingRifleV881(), antiMaterielCannonV881(), clusterCannonV881(), arcInductionLanceV881(), limeSprayerV881());
    }
}
