package domain.inventory.item.misc;

import domain.inventory.InventoryEntry;
import domain.inventory.InventoryFootprint;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;
import domain.inventory.item.PersonalTransportUseProperties;
import domain.survival.FoodType;
import domain.status.TimeScale;
import domain.status.TimedEffect;
import domain.status.TherapeuticEffectProfile;
import domain.throwing.ThrowProfile;

import java.util.List;
import java.util.Optional;

/**
 * Constructores de conveniencia de la vertical slice.
 * : la autoridad canónica de identidad es domain.inventory.catalog.PhysicalObjectCatalog.
 */
public final class MiscellaneousItemCatalog {
    private MiscellaneousItemCatalog() {}

    public static FoodItem bread() { return bread(4); }

    public static FoodItem bread(int uses) {
        return new FoodItem(
                "Pan",
                "Hogaza compacta de harina, agua, sal y masa fermentada, cocida hasta formar una corteza firme y una miga densa. Cada uso representa una porción arrancada de la misma pieza.",
                FoodType.BREAD,
                uses,
                4,
                0.150,
                new InventoryFootprint(4, 1),
                3.0,
                0,
                0
        );
    }

    public static FoodItem jerky() { return jerky(1); }

    public static FoodItem jerky(int uses) {
        return new FoodItem(
                "Cecina",
                "Tiras de carne curada con sal y secada al aire para reducir su humedad. Cada uso corresponde a una tira fibrosa que debe desgarrarse y masticarse antes de tragarla.",
                FoodType.JERKY,
                uses,
                2,
                0.125,
                new InventoryFootprint(2, 1),
                4.5,
                0,
                0
        );
    }

    public static FoodItem nuts() { return nuts(3); }

    public static FoodItem nuts(int uses) {
        return new FoodItem(
                "Frutos secos",
                "Bolsa pequeña con tres raciones de frutos secos sin cáscara. Cada uso corresponde a un puñado; su escasa humedad reduce el hambre, pero aumenta un nivel de sed.",
                FoodType.NUTS,
                uses,
                3,
                0.050,
                new InventoryFootprint(1, 1),
                1.5,
                0,
                1
        );
    }

    public static FoodItem cake() {
        return new FoodItem(
                "Bizcocho",
                "Bizcocho compacto elaborado con harina, huevo y una cantidad moderada de endulzante, dividido en dos porciones. Cada uso restaura dos niveles de hambre, pero no participa en la bonificación por variedad.",
                FoodType.CAKE,
                2,
                2,
                0.150,
                new InventoryFootprint(2, 2),
                3.0,
                0,
                0
        );
    }

    public static TherapeuticItem stimulantInjection() {
        return new TherapeuticItem(
                "Inyección estimulante",
                "Autoinyector de campaña monodosis concebido para administración intramuscular en el muslo derecho. La descarga produce un estado estimulante de seis segundos: comprime el intervalo ordinario de PV REGEN de seis segundos a uno y elimina temporalmente todo coste de PA.",
                MiscellaneousCategory.STIMULANT, 1, 1, 0.020, 0.060, new InventoryFootprint(1,1), 1.2,
                List.of("Extraer el autoinyector.","Retirar el seguro con el pulgar.","Clavarlo contra el muslo derecho y accionar el mecanismo.","Mantener la presión hasta vaciar la dosis.","Desechar el dispositivo usado."),
                List.of("PV REGEN | Intervalo 1 s durante 6 s reales","GASTO DE PA | 0 durante 6 s reales","ADMINISTRACIÓN | Intramuscular, muslo derecho","TAMAÑO | 1x1"),
                PersonalTransportUseProperties.all(),
                new TherapeuticEffectProfile(TherapeuticEffectProfile.HealingKind.NONE,0,1,0,1,1,0,0,1,1,false,new TimedEffect("Inyección estimulante",6,TimeScale.REAL_SECONDS)));
    }

    public static TherapeuticItem yarrow() {
        return new TherapeuticItem(
                "Emplasto de milenrama",
                "Emplasto húmedo elaborado con hojas y flores de milenrama machacadas sobre una base de tela. Se aplica directamente sobre una herida; no recupera PV, pero elimina una reducción activa de PV REGEN.",
                MiscellaneousCategory.HEALING,
                1,
                1,
                0.030,
                0.050,
                new InventoryFootprint(1, 1),
                3.5,
                List.of(
                        "Extraer el envoltorio.",
                        "Abrirlo con una mano.",
                        "Presionar la milenrama sobre la herida.",
                        "Mantenerla hasta estabilizar la regeneración."
                ),
                List.of(
                        "ESTABILIZADOR DE PV REGEN | Elimina una reducción activa",
                        "PERSISTENCIA | No protege frente a reducciones posteriores",
                        "TAMAÑO | 1x1",
                        "STACK MÁXIMO | 3"
                ),
                List.of(),
                new TherapeuticEffectProfile(TherapeuticEffectProfile.HealingKind.YARROW, 0, 1, 0, 1, 1,
                        0, 0, 1, 1, false, null)
        );
    }

    public static TherapeuticItem bogMoss() {
        return new TherapeuticItem(
                "Apósito de musgo de turbera",
                "Almohadilla de musgo de turbera limpio y comprimido que se fija sobre la lesión reciente. Al aplicarse crea una barrera de PV exactamente igual al daño neto del último golpe recibido; mientras quede barrera no puede colocarse otro apósito.",
                MiscellaneousCategory.HEALING,1,1,0.060,0.120,new InventoryFootprint(1,1),4.5,
                List.of("Extraer el apósito.","Abrir la venda corta.","Asentar el musgo sobre la lesión del último impacto.","Fijar la venda alrededor de la herida."),
                List.of("BARRERA DE PV | Igual al último golpe recibido","REAPLICACIÓN | Bloqueada mientras la barrera siga activa","MILENRAMA | Si se aplica durante la barrera, la inhibición de PV REGEN persiste hasta que ésta desaparezca","TAMAÑO | 1x1"),
                List.of(), new TherapeuticEffectProfile(TherapeuticEffectProfile.HealingKind.BOG_MOSS,0,1,0,1,1,0,0,1,1,false,null));
    }

    public static TherapeuticItem willowBark() {
        return new TherapeuticItem(
                "Corteza de sauce",
                "Tira seca de corteza de sauce preparada para masticarse. Cada uso reduce en un tercio el daño neto recibido por VENENO durante media hora de tiempo de videojuego; no altera PA, estabilidad ni regeneraciones.",
                MiscellaneousCategory.STIMULANT,1,1,0.020,0.030,new InventoryFootprint(1,1),1.8,
                List.of("Extraer una tira de corteza.","Romper el extremo más blando.","Introducirlo en la boca y comenzar a masticar."),
                List.of("VENENO | Daño x2/3","DURACIÓN | 30 min de tiempo de videojuego","TAMAÑO | 1x1"),
                PersonalTransportUseProperties.all(),new TherapeuticEffectProfile(TherapeuticEffectProfile.HealingKind.NONE,0,1,0,1,1,0,0,1,1,false,new TimedEffect("Corteza de sauce",30,TimeScale.GAME_MINUTES)));
    }

    public static TherapeuticItem mead() { return mead(2); }

    public static TherapeuticItem mead(int quantity) {
        return new TherapeuticItem(
                "Petaca de hidromiel",
                "Petaca plana y persistente destinada exclusivamente a 240 mL de hidromiel. Su capacidad se divide funcionalmente en dos usos de 120 mL: beber no destruye el recipiente y la petaca permanece en inventario cuando se agota. La estimulación hace que los ataques cuyo coste técnico excede x1 se paguen como x1 y neutraliza la penalización de recuperación de PA causada por la carga; a cambio desaparece la fijación de blanco y aparece un tambaleo perceptivo y postural constante.",
                MiscellaneousCategory.STIMULANT,quantity,2,0.120,0.126,new InventoryFootprint(2,1),2.0,
                List.of("Extraer la petaca.","Retirar el tapón.","Beber 120 mL.","Cerrar y guardar la petaca."),
                List.of("CAPACIDAD | 240 mL","USO | 120 mL · 2 usos máximos","RECIPIENTE | Persistente al agotarse · 0,120 kg vacía","ATAQUES | Multiplicador de PA >x1 pasa a x1","CARGA | Inhibe su penalización de recuperación de PA","LATENCIA PA REGEN | 1,20 s","FIJAR BLANCO | Inhabilitado","TAMBALEO | Constante","DURACIÓN | 30 min de tiempo de videojuego","TAMAÑO | 2x1"),
                PersonalTransportUseProperties.all(),
                new SurvivalConsumptionEffect(Optional.of(FoodType.MEAD),1,0,false,false),
                new TherapeuticEffectProfile(TherapeuticEffectProfile.HealingKind.NONE,0,1,0,1,1,0,0,1,1,false,new TimedEffect("Hidromiel",30,TimeScale.GAME_MINUTES)));
    }

    public static TherapeuticItem lucidityEssence() {
        return new TherapeuticItem(
                "Esencia de lucidez",
                "Ampolla inhalante monodosis. Al quebrarla y aspirar sus vapores, la respuesta autonómica pone en marcha de inmediato PA REGEN hasta completar la reserva y, durante media hora de juego, elimina los fotogramas de invulnerabilidad de MIRAGE frente al adversario concreto con el que se mantiene el combate.",
                MiscellaneousCategory.STIMULANT,1,1,0.010,0.030,new InventoryFootprint(1,1),1.4,
                List.of("Extraer la ampolla.","Quebrar el extremo con el pulgar.","Acercarla a la nariz.","Realizar una inhalación profunda.","Desechar la ampolla vacía."),
                List.of("PA REGEN | Activación inmediata hasta PA completos","MIRAGE | Sin i-frames frente al adversario actual","DURACIÓN | 30 min de tiempo de videojuego","TAMAÑO | 1x1"),
                PersonalTransportUseProperties.all(),new TherapeuticEffectProfile(TherapeuticEffectProfile.HealingKind.NONE,0,1,0,1,1,0,0,1,1,false,new TimedEffect("Esencia de lucidez",30,TimeScale.GAME_MINUTES)));
    }

    public static TherapeuticItem irndFlask() {
        return new TherapeuticItem(
                "Frasco de I-RND",
                "Inhibidor de la Red Neuronal por Defecto en solución completamente transparente e inodora, de sabor ligeramente amargo. El formato de origen se conserva en vidrio ámbar u opaco porque el compuesto es fotosensible: la radiación solar o ultravioleta degrada sus enlaces y lo inactiva con rapidez. Durante la primera media hora fuerza INTELIGENCIA, FE, CARISMA y CLARIVIDENCIA efectivas a 75. Durante la segunda media hora sólo quedan las secuelas fisiológicas; la única restricción común a ambas fases es la imposibilidad de dormir.",
                MiscellaneousCategory.STIMULANT,1,1,0.020,0.030,new InventoryFootprint(1,1),1.4,
                List.of("Extraer el frasco protegido de la luz.","Abrirlo inmediatamente antes del uso.","Administrar una carga.","Cerrar o desechar el recipiente evitando exposición luminosa intensa."),
                List.of("0-30 MIN | INT/FE/CAR/CLA efectivas = 75; no puede dormir","30-60 MIN | DEX <=20; PV REGEN como VIT 1; PA en régimen de tres tercios (5 s) y latencia 1,20 s; no dormir/escalar/nadar/transporte","SECUELAS | No mitigables","TAMAÑO | 1x1"),
                PersonalTransportUseProperties.all(),new TherapeuticEffectProfile(TherapeuticEffectProfile.HealingKind.NONE,0,1,0,1,1,0,0,1,1,false,new TimedEffect("I-RND",60,TimeScale.GAME_MINUTES)));
    }

    public static FoodItem fruit() {
        return new FoodItem("Fruta", "Pieza individual de fruta fresca con pulpa rica en agua. Consumirla reduce un nivel de hambre y otro de sed, y no activa ni permite la bonificación alimentaria.",
                FoodType.FRUIT, 1, 1, 0.250, new InventoryFootprint(2, 2), 2.0, 1, 0);
    }

    public static FoodItem driedGrapes() {
        return new FoodItem("Uva deshidratada", "Bolsa pequeña con dos raciones de uvas secadas para reducir su contenido de agua. Cada uso reduce un nivel de hambre y puede activar o recibir la bonificación por variedad.",
                FoodType.DRIED_GRAPES, 2, 2, 0.060, new InventoryFootprint(1, 1), 1.5, 0, 0);
    }

    public static BeverageItem waterskin() {
        return new BeverageItem("Odre", "Recipiente flexible de cuero cosido con boquilla de cobre y latón y cierre ajustado. Vacío pesa 0,250 kg y admite cinco cargas de agua de 0,400 kg cada una; beber consume una carga y rellenarlo en una fuente recupera las cinco.", 5, 5, 0.250, 0.400,
                new InventoryFootprint(2, 2), 1.8);
    }


    public static AstrolabeItem astrolabe() {
        return new AstrolabeItem();
    }

    public static ReconnaissanceMonocularItem reconnaissanceMonocular() { return new ReconnaissanceMonocularItem(); }

    public static PortableLaboratoryItem portableLaboratory() { return new PortableLaboratoryItem(); }

    public static ReusableRepairToolItem artisanBox() { return RepairToolCatalog.artisanBox(); }

    public static ReusableRepairToolItem toolbox() { return RepairToolCatalog.toolbox(); }

    public static ResinJarItem resinJar() { return new ResinJarItem(3); }

    public static CoolantBottleItem coolantBottle() { return new CoolantBottleItem(5); }

    public static UtilityObjectItem whetstone() {
        return new UtilityObjectItem("Piedra de afilar", "Piedra abrasiva rectangular utilizada con agua o aceite sobre el filo. Un uso restaura por completo la contundencia de todos los perfiles de un arma que posea al menos un valor cortante superior a cero.",
                100, 100, 0.400, 0.0, UseResourceKind.DURABILITY, new InventoryFootprint(1, 1),
                new UseAnimation(WhetstonePolicy.SHARPEN_DURATION_SECONDS,
                        List.of("Desenvainar el arma.", "Humedecer la piedra con agua o aceite.", "Pasar el filo sobre la superficie abrasiva.", "Comprobar el filo y guardar la piedra.")),
                List.of(UtilityAction.SHARPEN));
    }

    public static UtilityObjectItem mercuryStone() {
        return new UtilityObjectItem("Piedra de Mercurio", domain.combat.coating.MercuryCoatingService.NARRATIVE_DESCRIPTION,
                100, 100, 1.200, 0.0, UseResourceKind.DURABILITY, new InventoryFootprint(1, 1),
                new UseAnimation(8.0, List.of("Sujetar el objetivo ofensivo.", "Frotar la piedra hasta dejar una película plateada uniforme.", "Guardar la piedra.")),
                List.of(UtilityAction.RUB));
    }

    public static UtilityObjectItem amadou() {
        return new UtilityObjectItem("Amadou", "Lámina seca y fibrosa obtenida de la trama interior de un hongo yesquero y preparada como yesca. Recibe la chispa del pedernal, conserva la brasa e inicia la combustión de un material inflamable o de un cerrojo preparado.",
                10, 10, 0.0, 0.010, UseResourceKind.QUANTITY, new InventoryFootprint(1, 1),
                new UseAnimation(IgnitionPolicy.GENERATE_SPARK_DURATION_SECONDS,
                        List.of("Separar una porción de amadou.", "Prepararla para recibir la chispa.")),
                List.of(UtilityAction.GENERATE_SPARK, UtilityAction.IGNITE_LOCK));
    }

    public static UtilityObjectItem flint() {
        return new UtilityObjectItem("Pedernal", "Fragmento duro de sílex empleado para producir chispas al golpearlo contra una pieza metálica. Requiere amadou para conservar la brasa y ejecutar las acciones de generar fuego o incendiar un cerrojo.",
                50, 50, 0.250, 0.0, UseResourceKind.DURABILITY, new InventoryFootprint(1, 1),
                new UseAnimation(IgnitionPolicy.GENERATE_SPARK_DURATION_SECONDS,
                        List.of("Sujetar el pedernal frente al amadou.", "Golpearlo contra la pieza metálica hasta producir una chispa.")),
                List.of(UtilityAction.GENERATE_SPARK, UtilityAction.IGNITE_LOCK));
    }

    public static UtilityObjectItem pebble() {
        return new UtilityObjectItem("Guijarro", "Conjunto de cinco piedras pequeñas, redondeadas por el desgaste del agua. Cada uso lanza una unidad; el impacto, el ruido y cualquier otra consecuencia dependen del entorno y del objetivo.",
                5, 5, 0.0, 0.080, UseResourceKind.QUANTITY, new InventoryFootprint(1, 1),
                new UseAnimation(0.8, List.of("Extraer un guijarro.", "Armar el brazo y lanzarlo.")),
                List.of(UtilityAction.THROW), Optional.of(ThrowProfile.improvised(0.080, true)),
                PersonalTransportUseProperties.all());
    }



    public static domain.inventory.item.misc.RawPotatoItem rawPotato() {
        return new domain.inventory.item.misc.RawPotatoItem();
    }

    public static domain.inventory.item.misc.ImprovisedFuelConverterItem improvisedFuelConverter() {
        return new domain.inventory.item.misc.ImprovisedFuelConverterItem();
    }
}
