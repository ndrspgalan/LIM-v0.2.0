package domain.bestiarium.physical_plane.aspirant;

import domain.environment.time.DayPhase;
import domain.social.Subprofession;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Catálogo funcional . No pretende enumerar toda la zoología: enumera arquitecturas
 * suficientemente distintas para cubrir deriva terrestre, aérea, acuática y anfibia sin
 * abandonar la continuidad evolutiva que fundamenta ASPIRANT.
 */
public final class AspirantReferenceCatalog {
    private static final Map<AspirantReferenceId, AspirantReferenceProfile> PROFILES = build();

    private AspirantReferenceCatalog() {}

    public static AspirantReferenceProfile profile(AspirantReferenceId id) {
        var p = PROFILES.get(id);
        if (p == null) throw new IllegalArgumentException("Referente ASPIRANT no catalogado: " + id);
        return p;
    }

    public static Map<AspirantReferenceId, AspirantReferenceProfile> all() {
        return Map.copyOf(PROFILES);
    }

    private static Map<AspirantReferenceId, AspirantReferenceProfile> build() {
        EnumMap<AspirantReferenceId, AspirantReferenceProfile> m = new EnumMap<>(AspirantReferenceId.class);

        put(m, AspirantReferenceId.PRIMATE, "primate", "Pan troglodytes", AspirantEvolutionaryAffinity.PRIMATE_NEAR,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.HUMID,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.HIERARCHY, AspirantHumanDrive.IMITATION, AspirantHumanDrive.SOCIAL_BELONGING, AspirantHumanDrive.DOMINANCE),
                shelters(AspirantShelter.FOREST, AspirantShelter.URBAN, AspirantShelter.DOMESTIC),
                subs(Subprofession.GAME_MASTER, Subprofession.FAIRGROUND_ENTREPRENEUR, Subprofession.KINGDOM_AGENT, Subprofession.MERCENARY_COMPANY_DIRECTOR),
                "La pertenencia, la imitación y la negociación jerárquica terminan gobernando la conducta.",
                "La síntesis conserva manos, cintura escapular y rostro humano derivado, pero intensifica potencia braquial, prognatismo, prensión y señales sociales corporales sin convertirse en otro primate." );

        put(m, AspirantReferenceId.PORCINE, "porcino", "Sus scrofa", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.TEMPERATE, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.CONSUMPTION, AspirantHumanDrive.COMFORT, AspirantHumanDrive.OPPORTUNISM),
                shelters(AspirantShelter.FARMLAND, AspirantShelter.URBAN, AspirantShelter.DOMESTIC),
                subs(Subprofession.TAVERN_KEEPER, Subprofession.RURAL_AGGREGATOR, Subprofession.LIVESTOCK_KEEPER, Subprofession.STEVEDORE, Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER),
                "La abundancia, el desperdicio y la calidad dejan de ser categorías morales distintas: todo recurso alimentario acaba siendo evaluado por disponibilidad.",
                "Hocico olfativo, cuello y cintura escapular se refundan con mandíbula, manos todavía prensiles y pelvis humana; la forma extrema puede alternar apoyo bípedo y cuatro puntos sin ser un cerdo erguido." );

        put(m, AspirantReferenceId.CANID, "cánido", "Canis lupus", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.VARIABLE, AspirantMoistureBand.VARIABLE,
                phases(DayPhase.DAY, DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.TERRITORIALITY, AspirantHumanDrive.LOYALTY, AspirantHumanDrive.GROUP_CONFORMITY, AspirantHumanDrive.PURSUIT),
                shelters(AspirantShelter.FOREST, AspirantShelter.OPEN_COUNTRY, AspirantShelter.URBAN),
                subs(Subprofession.ROAD_GUIDE, Subprofession.WILDLIFE_TRACKER, Subprofession.PROFESSIONAL_HUNTER, Subprofession.CONVOY_ESCORT, Subprofession.FRONTIER_SKIRMISHER),
                "Lealtad y pertenencia pueden hipertrofiarse hasta convertir personas, rutas y lugares en territorio defendible.",
                "El eje corporal se adelanta, oído y olfato dominan, metacarpos y metatarsos se alargan y la carrera admite apoyo múltiple conservando manipulación humana residual." );

        put(m, AspirantReferenceId.FELID, "felino", "Felidae", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.BALANCED,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.PREDATION, AspirantHumanDrive.INDEPENDENCE, AspirantHumanDrive.PATIENCE, AspirantHumanDrive.VIGILANCE),
                shelters(AspirantShelter.FOREST, AspirantShelter.ROCKY, AspirantShelter.URBAN),
                subs(Subprofession.WILDLIFE_TRACKER, Subprofession.PROFESSIONAL_HUNTER, Subprofession.TRAPPER, Subprofession.MOBILE_ESCORT),
                "El individuo aprende a invertir esfuerzo sólo cuando puede controlar la aproximación y escoger vulnerabilidad.",
                "Columna, cintura pélvica, uñas y tobillos ganan elasticidad y tracción; el rostro se acorta y los ojos se especializan sin producir una cabeza felina injertada." );

        put(m, AspirantReferenceId.URSID, "ursino", "Ursidae", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.COOL, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.ACCUMULATION, AspirantHumanDrive.ISOLATION, AspirantHumanDrive.COMFORT, AspirantHumanDrive.TERRITORIALITY),
                shelters(AspirantShelter.FOREST, AspirantShelter.CAVE, AspirantShelter.ROCKY),
                subs(Subprofession.FOREST_LUMBERJACK, Subprofession.FORESTRY_MANAGER, Subprofession.PROSPECTOR, Subprofession.EXTRACTION_MINER),
                "Acumular reservas y reducir exposición al exterior acaba organizando estaciones enteras de su vida.",
                "Torso, cuello y extremidades se densifican; manos y pies se ensanchan y la postura puede bascular hacia cuadrupedia sin perder del todo la arquitectura humana." );

        put(m, AspirantReferenceId.BOVID, "bóvido", "Bovidae", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.TEMPERATE, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.DOMINANCE, AspirantHumanDrive.ROUTINE, AspirantHumanDrive.TERRITORIALITY, AspirantHumanDrive.PERSISTENCE),
                shelters(AspirantShelter.FARMLAND, AspirantShelter.OPEN_COUNTRY, AspirantShelter.DOMESTIC),
                subs(Subprofession.LIVESTOCK_KEEPER, Subprofession.HAULAGE_LABORER, Subprofession.STONE_SETTER, Subprofession.INSTITUTIONAL_SHOCK_COMBATANT),
                "La estabilidad y la imposición frontal sustituyen gradualmente negociación, rodeo y retirada.",
                "Caja torácica, cuello y apoyo distal se vuelven masivos; estructuras córneas pueden emerger del cráneo sin convertir la silueta en un bovino literal." );

        put(m, AspirantReferenceId.CERVID, "cérvido", "Cervidae", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.COOL, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.DISPLAY, AspirantHumanDrive.COMPETITION, AspirantHumanDrive.VIGILANCE, AspirantHumanDrive.MOBILITY),
                shelters(AspirantShelter.FOREST, AspirantShelter.OPEN_COUNTRY),
                subs(Subprofession.COMPETITION_RIDER, Subprofession.FRONTIER_SKIRMISHER, Subprofession.ROAD_GUIDE, Subprofession.V881_SUPPORT_MARKSWOMAN),
                "Comparación, exhibición y vigilancia convierten cualquier encuentro en una medición tácita de posición y capacidad.",
                "Piernas largas, caja torácica ligera y estructuras craneales de exhibición se integran con un rostro aún humano derivado y manos funcionales." );

        put(m, AspirantReferenceId.EQUID, "équido", "Equus caballus", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.TEMPERATE, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.PERFORMANCE, AspirantHumanDrive.OBEDIENCE, AspirantHumanDrive.MOBILITY, AspirantHumanDrive.ROUTINE),
                shelters(AspirantShelter.OPEN_COUNTRY, AspirantShelter.FARMLAND, AspirantShelter.DOMESTIC),
                subs(Subprofession.STABLE_HAND, Subprofession.COMPETITION_RIDER, Subprofession.CYCLIST_MESSENGER, Subprofession.KINGDOM_MESSENGER, Subprofession.MOTORCYCLE_COURIER),
                "Ser útil, llegar antes y mantener rendimiento se convierten en fines independientes de aquello que debía justificar el esfuerzo.",
                "Extremidades distales y pelvis se alargan para desplazamiento sostenido; tórax y cuello compensan la nueva zancada mientras las manos se reducen sólo en desviaciones altas." );

        put(m, AspirantReferenceId.RODENT, "roedor", "Rattus norvegicus", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.VARIABLE, AspirantMoistureBand.VARIABLE,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.ACCUMULATION, AspirantHumanDrive.OPPORTUNISM, AspirantHumanDrive.SHELTER_SEEKING, AspirantHumanDrive.CURIOSITY),
                shelters(AspirantShelter.URBAN, AspirantShelter.UNDERGROUND, AspirantShelter.INDUSTRIAL),
                subs(Subprofession.SANITATION_OPERATOR, Subprofession.EXTRACTION_MINER, Subprofession.STEVEDORE, Subprofession.TRAPPER, Subprofession.DISPLACED_RESIDENT),
                "Lo guardado deja de necesitar utilidad: basta con que pueda ser recuperado más tarde o negado a otro.",
                "Incisivos, flexibilidad axial y manos muy hábiles crecen junto a una postura cada vez más baja, sin sustituir el tronco por el de una rata." );

        put(m, AspirantReferenceId.LAGOMORPH, "lagomorfo", "Oryctolagus cuniculus", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.TEMPERATE, AspirantMoistureBand.BALANCED,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.AVOIDANCE, AspirantHumanDrive.VIGILANCE, AspirantHumanDrive.MOBILITY, AspirantHumanDrive.SHELTER_SEEKING),
                shelters(AspirantShelter.OPEN_COUNTRY, AspirantShelter.UNDERGROUND, AspirantShelter.FARMLAND),
                subs(Subprofession.ROAD_GUIDE, Subprofession.CYCLIST_MESSENGER, Subprofession.MOTORCYCLE_COURIER, Subprofession.FRONTIER_SKIRMISHER),
                "La capacidad de salir de una situación sustituye progresivamente la capacidad de resolverla.",
                "Pelvis y miembros inferiores dominan la silueta; oído y respiración se especializan mientras brazos y cara conservan suficiente continuidad humana para resultar incómodos." );

        put(m, AspirantReferenceId.BAT, "quiróptero", "Chiroptera", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.AERIAL, AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.BALANCED,
                phases(DayPhase.NIGHT), drives(AspirantHumanDrive.ISOLATION, AspirantHumanDrive.NOCTURNALITY, AspirantHumanDrive.VIGILANCE, AspirantHumanDrive.MOBILITY),
                shelters(AspirantShelter.CAVE, AspirantShelter.URBAN, AspirantShelter.ROCKY),
                subs(Subprofession.KINGDOM_MESSENGER, Subprofession.V881_NAVIGATOR, Subprofession.ROAD_GUIDE, Subprofession.PROSPECTOR, Subprofession.STRATEGIC_COMMUNICATIONS_OFFICER),
                "Horarios y refugios se reorganizan para reducir encuentros y explotar un mundo casi vacío de noche.",
                "El vuelo funcional exige que los brazos se conviertan progresivamente en superficies alares mediante elongación digital y patagio; nunca aparecen dos alas extra independientes." );

        put(m, AspirantReferenceId.PINNIPED, "pinnípedo", "Pinnipedia", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.AQUATIC, AspirantMobilityDomain.AMPHIBIOUS), AspirantTemperatureBand.COOL, AspirantMoistureBand.AQUATIC,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.COMFORT, AspirantHumanDrive.SOCIAL_BELONGING, AspirantHumanDrive.CONSUMPTION),
                shelters(AspirantShelter.COASTAL, AspirantShelter.OPEN_WATER),
                subs(Subprofession.COASTAL_FISHER, Subprofession.OFFSHORE_FISHER, Subprofession.MERCHANT_SAILOR, Subprofession.STEVEDORE),
                "Calor, alimento, descanso y proximidad social pasan de necesidades a objetivo vital dominante.",
                "Tejido subcutáneo, tórax y extremidades se reorganizan para propulsión acuática, conservando apoyo terrestre torpe y manipulación parcial." );

        put(m, AspirantReferenceId.CETACEAN, "cetáceo", "Cetacea", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.AQUATIC), AspirantTemperatureBand.VARIABLE, AspirantMoistureBand.AQUATIC,
                phases(DayPhase.DAY, DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.SOCIAL_BELONGING, AspirantHumanDrive.STIMULATION, AspirantHumanDrive.CURIOSITY, AspirantHumanDrive.MOBILITY),
                shelters(AspirantShelter.OPEN_WATER, AspirantShelter.COASTAL),
                subs(Subprofession.V881_NAVIGATOR, Subprofession.OFFSHORE_FISHER, Subprofession.MERCHANT_SAILOR, Subprofession.TRIATHLETE),
                "Interacción, exploración y estímulo continuo ocupan progresivamente el espacio de la quietud y la introspección.",
                "La columna y el tórax se vuelven hidrodinámicos, los miembros posteriores pierden protagonismo y los anteriores conservan una ambigua capacidad de dirección y manipulación." );

        put(m, AspirantReferenceId.ELEPHANTID, "elefántido", "Elephantidae", AspirantEvolutionaryAffinity.PLACENTAL_MAMMAL,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.MEMORY, AspirantHumanDrive.SOCIAL_BELONGING, AspirantHumanDrive.TERRITORIALITY, AspirantHumanDrive.PERSISTENCE),
                shelters(AspirantShelter.OPEN_COUNTRY, AspirantShelter.FOREST),
                subs(Subprofession.FORESTRY_MANAGER, Subprofession.HAULAGE_LABORER, Subprofession.INFRASTRUCTURE_CONCESSIONAIRE, Subprofession.DOCTRINE_CUSTODIAN),
                "Recuerdo, apego y continuidad vuelven casi imposible abandonar personas, rutas y agravios antiguos.",
                "Masa, pies, columna y musculatura cervical crecen hasta sostener una cara proyectada y una nariz manipuladora sin reproducir un elefante sobre dos piernas." );

        put(m, AspirantReferenceId.RAPTOR_BIRD, "ave rapaz", "Accipitridae", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.AERIAL, AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.VARIABLE, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.VIGILANCE, AspirantHumanDrive.DOMINANCE, AspirantHumanDrive.PREDATION, AspirantHumanDrive.PATIENCE),
                shelters(AspirantShelter.ROCKY, AspirantShelter.OPEN_COUNTRY, AspirantShelter.FOREST),
                subs(Subprofession.V881_SUPPORT_MARKSWOMAN, Subprofession.WILDLIFE_TRACKER, Subprofession.STRATEGIC_COMMUNICATIONS_OFFICER, Subprofession.ROAD_GUIDE),
                "La distancia se convierte en seguridad: observar y seleccionar desde una posición superior reemplaza la exposición directa.",
                "Brazos-alas, esternón y cintura escapular se refundan con piernas aún humanas derivadas; el rostro sólo desarrolla estructuras córneas graduales, no una cabeza de ave completa." );

        put(m, AspirantReferenceId.CORVID, "córvido", "Corvidae", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.AERIAL, AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.VARIABLE, AspirantMoistureBand.VARIABLE,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.IMITATION, AspirantHumanDrive.OPPORTUNISM, AspirantHumanDrive.ACCUMULATION, AspirantHumanDrive.CURIOSITY),
                shelters(AspirantShelter.URBAN, AspirantShelter.FOREST, AspirantShelter.OPEN_COUNTRY),
                subs(Subprofession.BOOKSELLER, Subprofession.GAME_MASTER, Subprofession.INTELLIGENCE_AGENT, Subprofession.RURAL_AGGREGATOR, Subprofession.RESTRICTED_MATERIALS_BROKER),
                "Aprender del otro puede degenerar en apropiarse de gestos, información y objetos cuyo valor procede precisamente de que otro los desea.",
                "El vuelo comparte la exigencia de brazos-alas, pero la cabeza conserva una transición craneofacial humana y las manos sólo sobreviven en desviaciones medias." );

        put(m, AspirantReferenceId.GALLIFORM, "galliforme", "Gallus gallus", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.TERRESTRIAL, AspirantMobilityDomain.AERIAL), AspirantTemperatureBand.TEMPERATE, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.HIERARCHY, AspirantHumanDrive.DISPLAY, AspirantHumanDrive.ROUTINE, AspirantHumanDrive.SOCIAL_BELONGING),
                shelters(AspirantShelter.FARMLAND, AspirantShelter.DOMESTIC, AspirantShelter.OPEN_COUNTRY),
                subs(Subprofession.LIVESTOCK_KEEPER, Subprofession.FAIRGROUND_ENTREPRENEUR, Subprofession.TAVERN_KEEPER, Subprofession.SHOPKEEPER),
                "Jerarquía cotidiana, exhibición y rutina social terminan dominando incluso interacciones sin conflicto real.",
                "Piernas, cintura pélvica y esternón se especializan; el vuelo sólo es corto y costoso y nunca aparece un ave completa con cara humana." );

        put(m, AspirantReferenceId.SERPENT, "serpiente", "Serpentes", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.DRY,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.PATIENCE, AspirantHumanDrive.CONSUMPTION, AspirantHumanDrive.CONCEALMENT, AspirantHumanDrive.EFFICIENCY),
                shelters(AspirantShelter.ROCKY, AspirantShelter.UNDERGROUND, AspirantShelter.OPEN_COUNTRY),
                subs(Subprofession.INTELLIGENCE_AGENT, Subprofession.TRAPPER, Subprofession.SABOTAGE_DENIAL_SPECIALIST, Subprofession.PROFESSIONAL_HUNTER),
                "El mínimo movimiento posible y la espera correcta reemplazan la acción frecuente; cuando llega el momento intenta resolverlo todo de una vez.",
                "La columna se alarga y las extremidades pueden reducirse de forma progresiva, pero la región torácica y craneal conserva una genealogía humana visible." );

        put(m, AspirantReferenceId.CROCODILIAN, "cocodriliano", "Crocodylia", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.AMPHIBIOUS, AspirantMobilityDomain.AQUATIC, AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.HUMID,
                phases(DayPhase.DAY, DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.PATIENCE, AspirantHumanDrive.TERRITORIALITY, AspirantHumanDrive.AMBUSH, AspirantHumanDrive.EFFICIENCY),
                shelters(AspirantShelter.WETLAND, AspirantShelter.COASTAL),
                subs(Subprofession.COASTAL_FISHER, Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN, Subprofession.PROFESSIONAL_HUNTER, Subprofession.MERCHANT_SAILOR),
                "Puede tolerar largos periodos de aparente pasividad porque sólo acepta actuar cuando la ventaja parece suficiente.",
                "Tronco bajo, cola axial, mandíbula y placas dérmicas se integran con una pelvis y cintura escapular todavía derivadas del humano." );

        put(m, AspirantReferenceId.LIZARD, "lagarto", "Lacertidae", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.DRY,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.ADAPTABILITY, AspirantHumanDrive.THERMAL_SEEKING, AspirantHumanDrive.VIGILANCE, AspirantHumanDrive.AVOIDANCE),
                shelters(AspirantShelter.ROCKY, AspirantShelter.URBAN, AspirantShelter.OPEN_COUNTRY),
                subs(Subprofession.FIELD_ELECTROATMOSPHERIC_SPECIALIST, Subprofession.PROSPECTOR, Subprofession.ROAD_GUIDE, Subprofession.SANITATION_OPERATOR),
                "La vida diaria se organiza alrededor de microambientes favorables y retiradas rápidas ante cualquier pérdida de ventaja.",
                "Piel, cola axial y apoyo distal cambian sin borrar por completo manos, tórax y cabeza humanos; la termorregulación condiciona la actividad." );

        put(m, AspirantReferenceId.AMPHIBIAN, "anfibio", "Anura", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.AMPHIBIOUS, AspirantMobilityDomain.AQUATIC, AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.TEMPERATE, AspirantMoistureBand.HUMID,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.HUMIDITY_SEEKING, AspirantHumanDrive.ADAPTABILITY, AspirantHumanDrive.AMBUSH, AspirantHumanDrive.AVOIDANCE),
                shelters(AspirantShelter.WETLAND, AspirantShelter.COASTAL, AspirantShelter.UNDERGROUND),
                subs(Subprofession.COASTAL_FISHER, Subprofession.SANITATION_OPERATOR, Subprofession.HORTICULTURIST, Subprofession.TRAPPER),
                "El individuo empieza a vivir sólo donde piel, temperatura y humedad le permiten permanecer inmóvil o escapar con poco coste.",
                "Piel permeable, pelvis y miembros posteriores ganan protagonismo; respiración y cuello se reconfiguran sin borrar totalmente el tronco humano." );

        put(m, AspirantReferenceId.TELEOST, "pez teleósteo", "Teleostei", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.AQUATIC), AspirantTemperatureBand.VARIABLE, AspirantMoistureBand.AQUATIC,
                phases(DayPhase.DAY, DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.MOBILITY, AspirantHumanDrive.GROUP_CONFORMITY, AspirantHumanDrive.CONSUMPTION, AspirantHumanDrive.ADAPTABILITY),
                shelters(AspirantShelter.OPEN_WATER, AspirantShelter.COASTAL),
                subs(Subprofession.COASTAL_FISHER, Subprofession.OFFSHORE_FISHER, Subprofession.MERCHANT_SAILOR, Subprofession.V881_NAVIGATOR),
                "El entorno social y físico se vuelve corriente: permanecer integrado en ella importa más que detenerse a decidir dirección propia.",
                "Musculatura axial, piel y superficies propulsoras dominan una forma que conserva restos funcionales de cintura escapular, manos palmeadas o cara humana derivada." );

        put(m, AspirantReferenceId.SHARK, "tiburón", "Selachimorpha", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.AQUATIC), AspirantTemperatureBand.VARIABLE, AspirantMoistureBand.AQUATIC,
                phases(DayPhase.DAY, DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.PURSUIT, AspirantHumanDrive.MOBILITY, AspirantHumanDrive.PREDATION, AspirantHumanDrive.VIGILANCE),
                shelters(AspirantShelter.OPEN_WATER, AspirantShelter.COASTAL),
                subs(Subprofession.OFFSHORE_FISHER, Subprofession.V881_NAVIGATOR, Subprofession.MOBILE_ESCORT, Subprofession.EXCEPTIONAL_ASSET_RECOVERER),
                "Detenerse equivale a perder iniciativa: toda la vida acaba convertida en movimiento, búsqueda y selección de una oportunidad vulnerable.",
                "La forma se vuelve axial e hidrodinámica, con dentición y piel especializadas, pero conserva una transición anatómica continua desde el tronco humano." );

        put(m, AspirantReferenceId.LAMPREY, "lamprea", "Petromyzon marinus", AspirantEvolutionaryAffinity.OTHER_VERTEBRATE,
                mobility(AspirantMobilityDomain.AQUATIC), AspirantTemperatureBand.COOL, AspirantMoistureBand.AQUATIC,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.PARASITISM, AspirantHumanDrive.ATTACHMENT, AspirantHumanDrive.PATIENCE, AspirantHumanDrive.CONSUMPTION),
                shelters(AspirantShelter.OPEN_WATER, AspirantShelter.COASTAL),
                subs(Subprofession.INFRASTRUCTURE_CONCESSIONAIRE, Subprofession.CONCESSIONARY_NOBLE, Subprofession.FINANCIER, Subprofession.OFFSHORE_FISHER),
                "La relación ideal deja de ser poseer o destruir una fuente y pasa a permanecer adherido a ella extrayendo lo suficiente para continuar.",
                "Mandíbula y rostro pueden reorganizarse hacia una cavidad oral circular mientras el tronco se hace axial; nunca se convierte simplemente en una lamprea grande." );

        put(m, AspirantReferenceId.DIPTERAN, "díptero", "Drosophila melanogaster", AspirantEvolutionaryAffinity.DISTANT_METAZOAN,
                mobility(AspirantMobilityDomain.AERIAL, AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.HUMID,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.DECAY_ATTRACTION, AspirantHumanDrive.OPPORTUNISM, AspirantHumanDrive.CONSUMPTION, AspirantHumanDrive.STIMULATION),
                shelters(AspirantShelter.URBAN, AspirantShelter.INDUSTRIAL, AspirantShelter.FARMLAND),
                subs(Subprofession.SANITATION_OPERATOR, Subprofession.HIDE_PREPARER, Subprofession.INDUSTRIAL_TANNER, Subprofession.LIVESTOCK_KEEPER),
                "Lo que el resto descarta por podrido, sucio o muerto se transforma en una concentración extraordinaria de oportunidad.",
                "Sólo desviaciones extremas pueden producir tórax de vuelo, superficies alares, ojos parcialmente facetados y aparato oral nuevo; ninguna forma suave recibe alas de insecto gratuitamente." );

        put(m, AspirantReferenceId.ARACHNID, "arácnido", "Araneae", AspirantEvolutionaryAffinity.DISTANT_METAZOAN,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.BALANCED,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.CONTROL, AspirantHumanDrive.PATIENCE, AspirantHumanDrive.PREDATION, AspirantHumanDrive.MANIPULATION),
                shelters(AspirantShelter.URBAN, AspirantShelter.CAVE, AspirantShelter.FOREST),
                subs(Subprofession.TRAPPER, Subprofession.INTELLIGENCE_AGENT, Subprofession.SABOTAGE_DENIAL_SPECIALIST, Subprofession.V881_INDUSTRIAL_BROKER),
                "La satisfacción deja de venir de moverse personalmente y aparece cuando otros recorren rutas previamente preparadas.",
                "La solución extrema redistribuye cintura, apéndices y soporte corporal; cualquier multiplicación funcional de miembros exige una refundición profunda, nunca patas de araña pegadas a un torso humano." );

        put(m, AspirantReferenceId.ANT, "hormiga", "Formicidae", AspirantEvolutionaryAffinity.DISTANT_METAZOAN,
                mobility(AspirantMobilityDomain.TERRESTRIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.WORK_COMPULSION, AspirantHumanDrive.GROUP_CONFORMITY, AspirantHumanDrive.HIERARCHY, AspirantHumanDrive.PERSISTENCE),
                shelters(AspirantShelter.UNDERGROUND, AspirantShelter.INDUSTRIAL, AspirantShelter.URBAN),
                subs(Subprofession.RECONSTRUCTION_LABORER, Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER, Subprofession.STONE_SETTER, Subprofession.STRUCTURAL_CARPENTER, Subprofession.RAILWAY_INFRASTRUCTURE_ENGINEER),
                "Trabajar deja de necesitar propósito: transportar, ordenar, repetir y ocupar una función se convierten en la propia justificación.",
                "Sólo una desviación remota puede segmentar profundamente tronco, soporte externo y apéndices; el resultado conserva continuidad de desarrollo humano y no es una hormiga aumentada." );

        put(m, AspirantReferenceId.COLEOPTERAN, "coleóptero", "Coleoptera", AspirantEvolutionaryAffinity.DISTANT_METAZOAN,
                mobility(AspirantMobilityDomain.TERRESTRIAL, AspirantMobilityDomain.AERIAL), AspirantTemperatureBand.WARM, AspirantMoistureBand.BALANCED,
                phases(DayPhase.DAY, DayPhase.AFTERNOON), drives(AspirantHumanDrive.PERSISTENCE, AspirantHumanDrive.SHELTER_SEEKING, AspirantHumanDrive.ACCUMULATION, AspirantHumanDrive.EFFICIENCY),
                shelters(AspirantShelter.UNDERGROUND, AspirantShelter.FOREST, AspirantShelter.INDUSTRIAL),
                subs(Subprofession.EXTRACTION_MINER, Subprofession.STONE_SETTER, Subprofession.HAULAGE_LABORER, Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN),
                "Protección y capacidad de seguir funcionando justifican capas cada vez mayores entre el individuo y el exterior.",
                "Placas, tórax y superficies alares sólo aparecen en síntesis muy desviadas; la masa central sigue delatando una arquitectura humana profundamente rehecha." );

        put(m, AspirantReferenceId.GASTROPOD, "gasterópodo", "Gastropoda", AspirantEvolutionaryAffinity.DISTANT_METAZOAN,
                mobility(AspirantMobilityDomain.TERRESTRIAL, AspirantMobilityDomain.AMPHIBIOUS), AspirantTemperatureBand.TEMPERATE, AspirantMoistureBand.HUMID,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.COMFORT, AspirantHumanDrive.SHELTER_SEEKING, AspirantHumanDrive.AVOIDANCE, AspirantHumanDrive.PERSISTENCE),
                shelters(AspirantShelter.WETLAND, AspirantShelter.URBAN, AspirantShelter.FOREST),
                subs(Subprofession.HORTICULTURIST, Subprofession.SANITATION_OPERATOR, Subprofession.DISPLACED_RESIDENT, Subprofession.INDIGENT),
                "Reducir exposición, llevar el refugio consigo y no abandonar condiciones favorables puede consumir toda voluntad de desplazamiento.",
                "La locomoción, piel y soporte protector se refundan de forma extrema; no aparece una concha sobre un humano normal ni desaparece sin transición el eje corporal previo." );

        put(m, AspirantReferenceId.CEPHALOPOD, "cefalópodo", "Octopus bimaculoides", AspirantEvolutionaryAffinity.DISTANT_METAZOAN,
                mobility(AspirantMobilityDomain.AQUATIC), AspirantTemperatureBand.WARM, AspirantMoistureBand.AQUATIC,
                phases(DayPhase.AFTERNOON, DayPhase.NIGHT), drives(AspirantHumanDrive.ADAPTABILITY, AspirantHumanDrive.CONCEALMENT, AspirantHumanDrive.MANIPULATION, AspirantHumanDrive.CURIOSITY),
                shelters(AspirantShelter.COASTAL, AspirantShelter.OPEN_WATER, AspirantShelter.ROCKY),
                subs(Subprofession.V881_NAVIGATOR, Subprofession.TECHNICAL_RECOVERY_OPERATOR, Subprofession.INTELLIGENCE_AGENT, Subprofession.FORENSIC_INVESTIGATOR),
                "Cambiar apariencia, estrategia y forma de relacionarse resuelve tantos problemas que una identidad estable acaba pareciendo una desventaja.",
                "Musculatura hidrostática, piel dinámica y apéndices se redistribuyen alrededor de un eje corporal todavía derivado del humano; nunca es un pulpo con una cabeza humana ni un humano con tentáculos añadidos." );

        return Map.copyOf(m);
    }

    private static void put(Map<AspirantReferenceId, AspirantReferenceProfile> m,
                            AspirantReferenceId id, String common, String scientific,
                            AspirantEvolutionaryAffinity affinity,
                            Set<AspirantMobilityDomain> mobility,
                            AspirantTemperatureBand temperature,
                            AspirantMoistureBand moisture,
                            Set<DayPhase> phases,
                            Set<AspirantHumanDrive> drives,
                            Set<AspirantShelter> shelters,
                            Set<Subprofession> subs,
                            String rationale,
                            String anatomy) {
        AspirantEcologicalProfile ecology = new AspirantEcologicalProfile(
                mobility, temperature, moisture, phases, drives, shelters, subs, rationale);
        m.put(id, new AspirantReferenceProfile(id, new ConvergentAnimalReference(common, scientific), affinity, ecology, anatomy));
    }

    private static Set<AspirantMobilityDomain> mobility(AspirantMobilityDomain... v) { return EnumSet.of(v[0], v); }
    private static Set<DayPhase> phases(DayPhase... v) { return EnumSet.of(v[0], v); }
    private static Set<AspirantHumanDrive> drives(AspirantHumanDrive... v) { return EnumSet.of(v[0], v); }
    private static Set<AspirantShelter> shelters(AspirantShelter... v) { return EnumSet.of(v[0], v); }
    private static Set<Subprofession> subs(Subprofession... v) { return v.length == 0 ? Set.of() : EnumSet.of(v[0], v); }
}
