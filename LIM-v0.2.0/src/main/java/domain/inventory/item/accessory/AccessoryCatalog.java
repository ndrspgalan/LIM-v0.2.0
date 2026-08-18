package domain.inventory.item.accessory;

import domain.inventory.item.AccessoryEffect;
import domain.inventory.item.AccessoryEffectType;
import domain.inventory.item.AccessoryItem;
import domain.inventory.item.misc.AstrolabeItem;
import domain.inventory.item.ItemProperty;
import domain.inventory.item.ItemPropertyId;

import domain.character.sheet.Attribute;
import domain.inventory.InventoryFootprint;

import java.util.List;

/** Catálogo canónico . La clasificación comercial no forma parte del dominio mecánico. */
public final class AccessoryCatalog {
    private static final String HUNTER_NARRATIVE = "Un cazador debe cazar. Un cazador debe ser tanto depredador como presa. "
            + "Un cazador sabe que, si no fuera por el miedo, la muerte pasaría desapercibida. Por eso, solo un cazador "
            + "sabe que las formas más altas de evolución solo pueden ser alcanzadas por los más osados de los corazones.";

    private AccessoryCatalog() {}

    public static List<AccessoryItem> all() {
        java.util.ArrayList<AccessoryItem> all=new java.util.ArrayList<>(List.of(
                sketchBook(), lunarLantern(), portableLantern(), kiaraLocket(), kenanBracelet(), kiaraNotebook(),
                ratTail(), crowFeather(), pigHoof(), horseHair(), armadilloShell(), deerAntler(), bullEar(), snakeSkin(),
                boarTusk(), lynxEye(), eagleClaws(), wolfSkull(), lionMane(), bearPaw(), rhinocerosHorn(),
                loadedDice(), handmadeDoll(), pocketMirror(), ceremonialComb(), copperAmulet(), promiseRing(),
                emptyReliquary(), engravedPendant(), masterSplinter(), devoteeAshes(), intellectualWax()
        ));
        all.addAll(OccupationalNarrativeAccessoryCatalog.all());
        all.addAll(ArtifactAccessoryCatalog.all());
        return List.copyOf(all);
    }

    private static final String THOUGHT_OF_THOUGHT_NARRATIVE =
            "Y así, nuestra moderna concepción del mundo y nuestra moderna concepción de nosotros mismos es, desde entonces, "
                    + "el producto de la invención de un mundo sobre papel.";

    public static AccessoryItem sketchBook() {
        return accessory("CUADERNO DEL DIBUJANTE",
                "Es el cuaderno que Kiara utiliza desde niña. Comenzó a garabatearlo mientras permanecía en el Taller, "
                        + "dibujando a su padre y los artilugios con los que trabajaba cuando este no podía atenderla por las "
                        + "exigencias del oficio. Con el paso de los años, Kiara siguió llevándolo consigo como mensajera del "
                        + "Reino de Valerian, recorriendo el Continente de Las Tierras Lapsas para entregar nuevas del Reino y "
                        + "cumplir encargos de los nobles locales. El cuaderno termina convirtiéndose en un registro gráfico de "
                        + "sus viajes, observaciones y fenómenos naturales.",
                0.200, 3, 2,
                List.of("+2 CORDURA"),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.THOUGHT_OF_THOUGHT, "PENSAMIENTO DE PENSAMIENTO",
                        THOUGHT_OF_THOUGHT_NARRATIVE, "+2 CORDURA")),
                List.of(AccessoryEffect.always("PENSAMIENTO DE PENSAMIENTO", AccessoryEffectType.SANITY_BONUS, 2)));
    }

    /** Antiguo Farolillo portátil: conserva su identidad lunar y el Áncora Encarnada. */
    public static AccessoryItem lunarLantern() {
        return accessory("FAROLILLO LUNAR",
                "Farol compacto de ingeniería refinada que se equipa como abalorio y comienza a funcionar por sí solo. "
                        + "Su carcasa protege el foco y proyecta de forma continua un halo azul cian de dos metros sin ocupar las manos. "
                        + "La construcción lo convierte en una referencia portátil y estable durante la navegación por espacios oscuros.",
                0.300, 2, 1,
                List.of(),
                List.of(
                        ItemProperty.hidden(ItemPropertyId.EMBODIED_ANCHOR, "ÁNCORA ENCARNADA",
                                "Ancla al portador frente a la discontinuidad del Velo.", Attribute.CLARIVIDENCIA, 33,
                                "PERMITE NAVEGAR POR LAS HENDIDURAS DEL VELO")
                ),
                List.of(
                        AccessoryEffect.hidden("ÁNCORA ENCARNADA", AccessoryEffectType.VEIL_RIFT_NAVIGATION,
                                Attribute.CLARIVIDENCIA, 33, 1)
                ));
    }

    public static AccessoryItem portableLantern() {
        return accessory("FAROLILLO PORTÁTIL",
                "Pequeño farol de queroseno diseñado para equiparse como abalorio. Su depósito alimenta una llama protegida por una "
                        + "carcasa metálica y una pantalla que proyecta automáticamente un halo anaranjado cálido de dos metros sin ocupar las manos. "
                        + "El conjunto está pensado para mantener una iluminación cercana y estable mientras el portador se desplaza.",
                0.300, 2, 1,
                List.of("NOCHE: +3 CORDURA"),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.SOLAR_WARMTH, "CALOR SOLAR",
                        "La luz cálida conserva una referencia doméstica durante la noche.", "NOCHE: +3 CORDURA")),
                List.of(AccessoryEffect.always("CALOR SOLAR", AccessoryEffectType.NIGHT_SANITY_BONUS, 3)));
    }

    public static AccessoryItem kiaraLocket() {
        return accessory("GUARDAPELO DE KIARA", "Un regalo de Kiara. Contiene un mechón de su castaño cabello.",
                0.015, 1, 1,
                List.of("PV REGEN ×1,5", "+1 CORDURA"),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.UNCONDITIONAL_LOVE, "AMOR INCONDICIONAL",
                                "El vínculo afectivo persiste en aquello que se conserva.", "PV REGEN ×1,5"),
                        ItemProperty.hidden(ItemPropertyId.AFTER_ALL_THIS_TIME, "DESPUÉS DE TODO ESTE TIEMPO...",
                                "El recuerdo sigue presente para quien es capaz de percibirlo.", Attribute.FE, 23, "+1 CORDURA")
                ),
                List.of(
                        AccessoryEffect.always("AMOR INCONDICIONAL", AccessoryEffectType.HEALTH_REGENERATION_MULTIPLIER, 1.5),
                        AccessoryEffect.hidden("DESPUÉS DE TODO ESTE TIEMPO...", AccessoryEffectType.SANITY_BONUS,
                                Attribute.FE, 23, 1)
                ));
    }

    public static AccessoryItem kenanBracelet() {
        return accessory("PULSERA DE KENAN", "Un regalo de Kenan. Confeccionada con lavanda.",
                0.010, 1, 1,
                List.of("PV REGEN ×1,5"),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.UNCONDITIONAL_LOVE, "AMOR INCONDICIONAL",
                                "El vínculo afectivo persiste en aquello que se conserva.", "PV REGEN ×1,5"),
                        ItemProperty.hiddenWithHiddenRequirement(ItemPropertyId.QUEEN_WIFE_LOVE, "MI REINA, MI ESPOSA, MI AMOR",
                                "El vínculo con Kenan permanece como una certeza íntima frente a la adversidad.",
                                Attribute.FE, 23, "INMUNIDAD | Inhibición de PV REGEN")
                ),
                List.of(
                        AccessoryEffect.always("AMOR INCONDICIONAL", AccessoryEffectType.HEALTH_REGENERATION_MULTIPLIER, 1.5),
                        AccessoryEffect.hidden("MI REINA, MI ESPOSA, MI AMOR", AccessoryEffectType.HEALTH_REGEN_INHIBITION_IMMUNITY,
                                Attribute.FE, 23, 1)
                ),
                domain.runic.EffectImmunitySet.of(domain.runic.EffectImmunity.HEALTH_REGEN_PENALTIES));
    }

    public static AccessoryItem kiaraNotebook() {
        return accessory("CUADERNO DE KIARA",
                "Cuaderno de Kiara, deteriorado por el uso, con páginas arrancadas, huellas de haber sido pisoteado y daños "
                        + "provocados por las inclemencias. En sus páginas está escrita la historia de Kenan, el último Guerrero de Ébano.",
                0.200, 3, 2,
                List.of("+2 CORDURA"),
                List.of(
                        ItemProperty.alwaysActive(ItemPropertyId.THOUGHT_OF_THOUGHT, "PENSAMIENTO DE PENSAMIENTO",
                                THOUGHT_OF_THOUGHT_NARRATIVE, "+2 CORDURA"),
                        ItemProperty.hiddenWithHiddenRequirement(ItemPropertyId.GROW_OLD_TOGETHER, "¿ENVEJECEMOS JUNTOS?",
                                "La historia compartida persiste a pesar del deterioro.", Attribute.FE, 23,
                                "INMUNIDAD | Inhibición de PV REGEN")
                ),
                List.of(
                        AccessoryEffect.always("PENSAMIENTO DE PENSAMIENTO", AccessoryEffectType.SANITY_BONUS, 2),
                        AccessoryEffect.hidden("¿ENVEJECEMOS JUNTOS?", AccessoryEffectType.HEALTH_REGEN_INHIBITION_IMMUNITY,
                                Attribute.FE, 23, 1)
                ),
                domain.runic.EffectImmunitySet.of(domain.runic.EffectImmunity.HEALTH_REGEN_PENALTIES));
    }

    public static AccessoryItem ratTail() { return trophy("COLA DE RATA", "Cola larga y flexible, desecada con sal y humo para su conservación. Ligera y poco voluminosa.", 0.020, 1, 1, 1); }
    public static AccessoryItem crowFeather() { return trophy("PLUMA DE CUERVO", "Pluma negra de gran tamaño, seleccionada por su brillo y resistencia. Montada en pequeñas virolas de hilo encerado.", 0.005, 1, 1, 2); }
    public static AccessoryItem pigHoof() { return trophy("PEZUÑA DE CERDO", "Pezuña limpia y curada, conservada como prueba de una captura completa.", 0.090, 1, 1, 3); }
    public static AccessoryItem horseHair() { return trophy("CERDA DE CABALLO", "Mechón de cerdas gruesas y resistentes, anudado para impedir su dispersión.", 0.015, 1, 1, 4); }
    public static AccessoryItem armadilloShell() { return trophy("CAPARAZÓN DE ARMADILLO", "Fragmento articulado de caparazón, limpio y estabilizado para conservar su geometría.", 0.180, 2, 1, 5); }
    public static AccessoryItem deerAntler() { return trophy("CORNAMENTA DE CIERVO", "Parte de cornamenta seleccionada por su tamaño y simetría. Lijada y tratada para evitar grietas.", 0.300, 2, 2, 6); }
    public static AccessoryItem bullEar() { return trophy("OREJA DE TORO", "Oreja curtida y conservada, marcada por el grosor propio de un animal adulto.", 0.120, 2, 1, 7); }
    public static AccessoryItem snakeSkin() { return trophy("PIEL DE SERPIENTE", "Sección continua de piel curtida que conserva el dibujo de las escamas.", 0.060, 2, 1, 8); }
    public static AccessoryItem boarTusk() { return trophy("COLMILLO DE JABALÍ", "Colmillo superior curvado, limpio y pulido. La punta se refuerza para mayor resistencia.", 0.070, 2, 1, 9); }
    public static AccessoryItem lynxEye() { return trophy("OJO DE LINCE", "Ojo conservado en resina y alcohol, protegido en un pequeño estuche de metal sellado.", 0.020, 1, 1, 10); }
    public static AccessoryItem eagleClaws() { return trophy("GARRAS DE ÁGUILA", "Conjunto de garras delanteras completas, curadas y endurecidas en aceite y humo. Cada garra se refuerza con una férula.", 0.060, 2, 1, 11); }
    public static AccessoryItem wolfSkull() { return trophy("CRÁNEO DE LOBO", "Cráneo completo, blanqueado y pulido. Conserva los colmillos en perfecto estado. Requiere largo tratamiento.", 0.480, 2, 2, 12); }
    public static AccessoryItem lionMane() { return trophy("CRIN DE LEÓN", "Mechón espeso de crin, lavado, curado y trenzado para conservar su volumen.", 0.160, 2, 2, 13); }
    public static AccessoryItem bearPaw() { return trophy("ZARPA DE OSO", "Zarpa frontal completa, con piel curtida, garras endurecidas y soporte de cuero para su transporte.", 0.220, 2, 2, 14); }
    public static AccessoryItem rhinocerosHorn() { return trophy("CUERNO DE RINOCERONTE", "Sección de cuerno de gran densidad, estabilizada para evitar fisuras y pérdida de masa.", 0.650, 3, 2, 15); }

    private static AccessoryItem trophy(String name, String description, double weight, int v, int h, int charisma) {
        return accessory(name, HUNTER_NARRATIVE + " " + description, weight, v, h,
                List.of("+" + charisma + " CARISMA"),
                List.of(ItemProperty.alwaysActive(ItemPropertyId.HUNTING_TROPHY, "TROFEO DE CAZA", HUNTER_NARRATIVE, "+" + charisma + " CARISMA")),
                List.of(AccessoryEffect.attribute("TROFEO DE CAZA", Attribute.CARISMA, charisma)));
    }

    public static List<AccessoryItem> inertAccessories() {
        return List.of(loadedDice(), handmadeDoll(), pocketMirror(), ceremonialComb(), copperAmulet(),
                promiseRing(), emptyReliquary(), engravedPendant());
    }

    public static AccessoryItem loadedDice() { return inert("DADOS TRUCADOS", "Dados de peso descentrado con una de sus caras sutilmente más favorable. Difíciles de detectar a simple vista.", 0.030, 1, 1); }
    public static AccessoryItem handmadeDoll() { return inert("MUÑECA ARTESANAL", "Figura de trapo y relleno blando, confeccionada con esmero. Se dice que jamás deja a su dueño solo.", 0.080, 1, 2); }
    public static AccessoryItem pocketMirror() { return inert("ESPEJO DE BOLSILLO", "Pequeño espejo pulido y montado en marco metálico. Se cree que mirar en él antes de un viaje aleja los malos presagios.", 0.070, 1, 1); }
    public static AccessoryItem ceremonialComb() { return inert("PEINE CEREMONIAL", "Peine de madera fina o hueso tallado. Se dice que peinarse con él antes de un acontecimiento favorece el éxito social.", 0.050, 1, 1); }
    public static AccessoryItem copperAmulet() { return inert("AMULETO DE COBRE", "Pequeño disco de cobre grabado con símbolos populares de protección. Su eficacia jamás ha sido probada.", 0.020, 1, 1); }
    public static AccessoryItem promiseRing() { return inert("ANILLO DE PROMESA", "Anillo simple con inscripción interior. Se dice que quien rompe un juramento mientras lo lleva paga un alto precio.", 0.010, 1, 1); }
    public static AccessoryItem emptyReliquary() { return inert("RELICARIO VACÍO", "Pequeño relicario metálico sin reliquia. Muchos creen que su simple presencia atrae la buena fortuna.", 0.030, 1, 1); }
    public static AccessoryItem engravedPendant() { return inert("COLGANTE GRABADO", "Colgante de metal con grabado elaborado. Sus símbolos han dado origen a muchas historias sobre supuesta protección.", 0.020, 1, 1); }
    public static AccessoryItem masterSplinter() { return inert("ASTILLA CON LA QUE CLAVARON A UN MAESTRO", "Pequeña astilla de madera oscurecida, conservada en un envoltorio o relicario sencillo. El vendedor asegura que perteneció al instrumento con el que fue clavado un maestro, aunque su procedencia resulta prácticamente imposible de demostrar.", 0.040, 1, 1); }
    public static AccessoryItem devoteeAshes() { return inert("CENIZAS DE UN DEVOTO QUE SE ARROJÓ A LA HOGUERA", "Pequeño recipiente sellado que contiene cenizas atribuidas a un devoto que se arrojó voluntariamente a una hoguera. Se afirma que conservan el fervor de su sacrificio, aunque no existe medio ordinario de distinguirlas de cualquier otra ceniza.", 0.180, 1, 1); }
    public static AccessoryItem intellectualWax() { return inert("CERA DE UN INTELECTUAL", "Fragmento de cera endurecida que, según quien lo vende, procede del escritorio o la vela de un Intelectual célebre. Algunos compradores creen que conserva algo de su lucidez, aunque la pieza carece de cualquier rasgo que permita identificar a su supuesto propietario.", 0.090, 1, 1); }

    private static AccessoryItem inert(String name, String description, double weight, int vertical, int horizontal) {
        return accessory(name, description, weight, vertical, horizontal, List.of(), List.of(), List.of());
    }


    /**  — artefacto V881 migrado desde MISC. */
    public static AccessoryItem astrolabe() { return new AstrolabeItem(); }

    private static AccessoryItem accessory(String name, String description, double weight, int vertical, int horizontal,
                                           List<String> statistics, List<ItemProperty> properties,
                                           List<AccessoryEffect> effects) {
        return accessory(name, description, weight, vertical, horizontal, statistics, properties, effects,
                domain.runic.EffectImmunitySet.none());
    }

    private static AccessoryItem accessory(String name, String description, double weight, int vertical, int horizontal,
                                           List<String> statistics, List<ItemProperty> properties,
                                           List<AccessoryEffect> effects, domain.runic.EffectImmunitySet immunities) {
        return new AccessoryItem(name, description, weight, new InventoryFootprint(vertical, horizontal),
                statistics, properties, effects, immunities);
    }
}
