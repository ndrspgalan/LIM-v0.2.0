package domain.social;

import java.util.*;

/**
 * : especializaciones narrativas que existen porque representan una función real del mundo V881.
 * No todas las profesiones madre están completas todavía; el catálogo crecerá por áreas del mundo.
 */
public enum Subprofession {
    EBONY_WARRIOR_V881("Guerrero de Ébano V881", Profession.EBONY_WARRIOR),

    RECONSTRUCTION_LABORER("Peón de reconstrucción", Profession.DAY_LABORER),
    ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR("Operario de infraestructura electroatmosférica", Profession.DAY_LABORER),

    KINGDOM_AGENT("Agente del Reino", Profession.SOLDIER),
    V881_RIFLEMAN("Fusilero V881", Profession.SOLDIER),

    COMPANY_CONTRACTOR("Contratista de compañía", Profession.MERCENARY),

    V881_ELECTROMECHANIC("Electromecánico V881", Profession.BLACKSMITH),

    FREQUENCY_PHYSICIAN("Médico frecuencial", Profession.TEACHER),
    FREQUENCY_RESEARCHER("Investigador frecuencial", Profession.TEACHER),
    ELECTROATMOSPHERIC_NETWORK_ENGINEER("Ingeniero de redes electroatmosféricas", Profession.TEACHER),
    ELECTROATMOSPHERIC_CAPTATION_ENGINEER("Ingeniero de captación electroatmosférica", Profession.TEACHER),
    ELECTROATMOSPHERIC_SAFETY_ENGINEER("Ingeniero de seguridad electroatmosférica", Profession.TEACHER),
    ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER("Ingeniero de sistemas de locomoción electromagnética", Profession.TEACHER),
    RAILWAY_INFRASTRUCTURE_ENGINEER("Ingeniero de infraestructura ferroviaria", Profession.TEACHER),
    ELECTROMAGNETIC_TRANSPORT_PLANNER("Planificador de transporte electromagnético", Profession.TEACHER),

    V881_NAVIGATOR("Navegante V881", Profession.SAILOR),
    NAVAL_RAILGUN_GUNNER("Artillero de riel naval", Profession.SAILOR),
    NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER("Maquinista electroatmosférico naval", Profession.SAILOR),

    V881_INDUSTRIAL_BROKER("Corredor industrial V881", Profession.MERCHANT),
    V881_INDUSTRIAL_CONTRACT_AGENT("Agente de contratación industrial V881", Profession.MERCHANT),
    V881_INDUSTRIAL_CONSULTANT("Consultor industrial V881", Profession.MERCHANT),

    STONE_SETTER("Cantero de obra", Profession.STONEMASON),
    STONEWORK_MASTER("Maestro de obra pétrea", Profession.STONEMASON),
    PRECISION_STONECUTTER("Tallista de precisión", Profession.STONEMASON),
    STRUCTURAL_CARPENTER("Carpintero estructural", Profession.CARPENTER),
    BENCH_CARPENTER("Carpintero de banco", Profession.CARPENTER),
    CABINETMAKER("Ebanista", Profession.CARPENTER),
    HIDE_PREPARER("Preparador de pieles", Profession.TANNER),
    INDUSTRIAL_TANNER("Curtidor de proceso", Profession.TANNER),
    LEATHER_FINISHER_GRADER("Acabadora y clasificadora de cuero", Profession.TANNER),
    WORK_TAILOR("Sastre de oficio", Profession.DRESSMAKER),
    PRECISION_PATTERNMAKER("Patronista de precisión", Profession.DRESSMAKER),
    SALON_DRESSMAKER("Modista de salón", Profession.DRESSMAKER),
    BARBER("Barbero", Profession.HAIRDRESSER),
    SALON_HAIRDRESSER("Peluquero de salón", Profession.HAIRDRESSER),
    COMPETITION_RIDER("Jinete de competición", Profession.FAIRGROUND_WORKER),
    V881_MOTORCYCLE_RACER("Piloto de motociclismo V881", Profession.FAIRGROUND_WORKER),
    COMPETITION_CYCLIST("Ciclista de competición", Profession.FAIRGROUND_WORKER),
    TRIATHLETE("Triatleta", Profession.FAIRGROUND_WORKER),
    COMPANION_ANIMAL_BREEDER("Criador de animales de compañía", Profession.DAY_LABORER),

    ITINERANT_PUPPETEER_STORYTELLER("Titiritero y narrador ambulante", Profession.FAIRGROUND_WORKER),
    FAIRGROUND_ENTREPRENEUR("Empresario de feria", Profession.FAIRGROUND_WORKER),
    KINGDOM_MESSENGER("Mensajero del Reino", Profession.TEACHER),
    SURGEON("Cirujano", Profession.TEACHER),
    VETERINARIAN("Veterinario", Profession.TEACHER),
    PUBLIC_SCRIBE("Escribano público", Profession.JURIST),
    MAGISTRATE("Magistrado", Profession.JURIST),
    SHOPKEEPER("Tendero", Profession.MERCHANT),
    RAILWAY_GUARD("Guardia ferroviario", Profession.SOLDIER),

    PRISONER("Preso", Profession.BEGGAR),
    UNEMPLOYED("Desempleado", Profession.BEGGAR),
    WORK_DISABLED("Incapacitado laboral", Profession.BEGGAR),
    INDIGENT("Indigente", Profession.BEGGAR),
    DISPLACED_RESIDENT("Habitante desplazado", Profession.BEGGAR),

    SEX_WORKER("Trabajadora sexual", Profession.COURTESAN),
    SALON_COURTESAN("Cortesana de salón", Profession.COURTESAN),
    PROFESSIONAL_COMPANION("Acompañante profesional", Profession.COURTESAN),

    STABLE_HAND("Mozo de cuadras", Profession.DAY_LABORER),
    CYCLIST_MESSENGER("Mensajero ciclista", Profession.TEACHER),
    MOTORCYCLE_COURIER("Correo motociclista", Profession.MERCENARY),
    ROAD_GUIDE("Guía de caminos", Profession.HUNTER),
    WILDLIFE_TRACKER("Rastreador de fauna", Profession.HUNTER),

    DOMESTIC_V881_INSTALLER("Instalador doméstico V881", Profession.BLACKSMITH),
    SANITATION_OPERATOR("Operario de saneamiento", Profession.DAY_LABORER),
    SANITARY_MASTER("Maestro sanitario", Profession.TEACHER),

    TAVERN_KEEPER("Tabernero", Profession.MERCHANT),
    BOOKSELLER("Librero", Profession.MERCHANT),
    TAVERN_MUSICIAN("Músico de taberna", Profession.FAIRGROUND_WORKER),
    GAME_MASTER("Maestro de juegos", Profession.FAIRGROUND_WORKER),

    FARMER("Agricultor", Profession.DAY_LABORER),
    LIVESTOCK_KEEPER("Ganadero", Profession.DAY_LABORER),
    HORTICULTURIST("Horticultor", Profession.DAY_LABORER),
    COASTAL_FISHER("Pescador costero", Profession.SAILOR),
    OFFSHORE_FISHER("Pescador de altura", Profession.SAILOR),
    PROFESSIONAL_HUNTER("Cazador profesional", Profession.HUNTER),
    TRAPPER("Trampero", Profession.HUNTER),
    FOREST_LUMBERJACK("Leñador forestal", Profession.DAY_LABORER),
    FORESTRY_MANAGER("Gestor forestal", Profession.TEACHER),
    EXTRACTION_MINER("Minero de extracción", Profession.DAY_LABORER),
    PROSPECTOR("Prospector", Profession.TEACHER),
    MERCHANT_SAILOR("Marinero mercante", Profession.SAILOR),
    STEVEDORE("Estibador", Profession.DAY_LABORER),
    AGRICULTURAL_SELECTOR_CONDITIONER("Seleccionadora y acondicionadora agrícola", Profession.DAY_LABORER),
    HAULAGE_LABORER("Peón de acarreo", Profession.DAY_LABORER),
    RURAL_AGGREGATOR("Acopiador rural", Profession.MERCHANT),
    CONVOY_ESCORT("Escolta de convoy", Profession.MERCENARY),

    V881_INDUSTRIALIST("Industrial V881", Profession.MERCHANT),
    SHIPOWNER("Armador", Profession.MERCHANT),
    FINANCIER("Financista", Profession.MERCHANT),
    INFRASTRUCTURE_CONCESSIONAIRE("Concesionario de infraestructura", Profession.MERCHANT),
    GRAND_MERCHANT("Gran comerciante", Profession.MERCHANT),

    MERCENARY_COMPANY_DIRECTOR("Director de compañía mercenaria", Profession.MERCENARY),

    DYNASTIC_NOBLE("Noble de sangre", Profession.NOBLE),
    CONCESSIONARY_NOBLE("Noble concesionario", Profession.NOBLE),
    ENLIGHTENED_PATRON("Mecenas ilustrado", Profession.NOBLE),
    PATRIMONIAL_WARLORD("Señor de guerra patrimonial", Profession.NOBLE),

    REGENERATIONIST("Regeneracionista", Profession.TEACHER),
    CONTINUITY_EPIGENETICIST("Epigenetista de continuidad", Profession.TEACHER),
    NEUROARCHITECT("Neuroarquitecto", Profession.TEACHER),
    SOUL_RESEARCHER("Investigador álmico", Profession.TEACHER),
    SOUL_TRANSFUSIONIST("Trasvasista", Profession.TEACHER),
    SILICIC_METAMORPHOSIS_RESEARCHER("Investigador de metamorfosis silícica", Profession.TEACHER),
    PERMANENCE_RESEARCHER("Investigador de permanencia", Profession.TEACHER),
    ENLIGHTENED("Ilustrado", Profession.TEACHER),

    CONTINUITY_JURIST("Jurista de continuidad", Profession.JURIST),
    DOCTRINE_CUSTODIAN("Custodio de doctrina", Profession.JURIST),

    FREQUENCY_INSTRUMENT_MAKER("Instrumentista frecuencial", Profession.BLACKSMITH),
    MATRIX_ARCHITECT("Arquitecto de matrices", Profession.BLACKSMITH),

    EXCEPTIONAL_ASSET_RECOVERER("Recuperador de activos excepcionales", Profession.MERCENARY),
    CONTRACTUAL_SHOCK_COMBATANT("Combatiente de choque contractual", Profession.MERCENARY),
    FRONTIER_SKIRMISHER("Hostigadora de frontera", Profession.MERCENARY),
    MOBILE_ESCORT("Escolta móvil", Profession.MERCENARY),
    TECHNICAL_RECOVERY_OPERATOR("Operadora de recuperación técnica", Profession.MERCENARY),
    SABOTAGE_DENIAL_SPECIALIST("Especialista de sabotaje y negación", Profession.MERCENARY),
    STRATEGIC_INSTALLATION_CUSTODIAN("Custodia de instalación estratégica", Profession.SOLDIER),
    V881_CAMPAIGN_SAPPER("Zapador de campaña V881", Profession.SOLDIER),
    V881_HEAVY_WEAPONS_SPECIALIST("Especialista de armas pesadas V881", Profession.SOLDIER),
    INSTITUTIONAL_SHOCK_COMBATANT("Combatiente de choque institucional", Profession.SOLDIER),
    V881_SUPPORT_MARKSWOMAN("Tiradora de apoyo V881", Profession.SOLDIER),
    RESTRICTED_MATERIALS_BROKER("Corredor de materiales restringidos", Profession.MERCHANT),

    STRATEGIC_COMMUNICATIONS_OFFICER("Oficial de comunicaciones estratégicas", Profession.NOBLE),
    FORENSIC_INVESTIGATOR("Investigador forense", Profession.NOBLE),
    INTELLIGENCE_AGENT("Agente de inteligencia", Profession.NOBLE),
    FIELD_ELECTROATMOSPHERIC_SPECIALIST("Electroatmosferista de campo", Profession.NOBLE),
    PERMANENCE_PRETENDER("Pretendiente a la Permanencia", Profession.NOBLE);

    private final String label;
    private final Profession profession;

    Subprofession(String label, Profession profession) {
        this.label=Objects.requireNonNull(label);
        this.profession=Objects.requireNonNull(profession);
    }

    public String label(){ return label; }
    public Profession profession(){ return profession; }
    public double canonicalHeightMeters(domain.character.Gender gender){ return SubprofessionCanonicalHeightPolicy.heightMeters(this,gender); }
    public SubprofessionProfile profile(){ return SubprofessionProfileCatalog.profile(this); }
    public int monthlyReferenceValeritas(){ return profile().monthlyReferenceValeritas(); }
    public String monthlyReferenceLabel(){ return profile().monthlyReferenceLabel(); }
    public String narrativeDescription(){ return profile().narrativeDescription(); }
    public boolean uniqueContemporaryHolder(){ return profile().uniqueContemporaryHolder(); }
    public Optional<String> contemporaryHolder(){ return profile().contemporaryHolder(); }

    public static List<Subprofession> forProfession(Profession profession){
        Objects.requireNonNull(profession);
        return Arrays.stream(values()).filter(s->s.profession==profession).toList();
    }
}
