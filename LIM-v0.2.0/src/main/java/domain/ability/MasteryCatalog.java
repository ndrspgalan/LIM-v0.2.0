package domain.ability;

import domain.character.CharacterClass;
import domain.bestiarium.physical_plane.ferae.FeraeBranch;
import domain.bestiarium.physical_plane.ferae.FeraeCatalog;
import domain.bestiarium.physical_plane.ferae.FeraeSpecies;
import domain.character.sheet.Attribute;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/** Única fuente de verdad para todas las familias de maestrías del mundo. */
public final class MasteryCatalog {
    private static final Map<MasteryId, Mastery> CANONICAL = buildCanonical();

    private MasteryCatalog() {}

    public static Map<MasteryId, Mastery> canonical() { return CANONICAL; }

    public static Mastery require(MasteryId id) {
        Mastery mastery = CANONICAL.get(id);
        if (mastery == null) throw new IllegalArgumentException("Maestría no catalogada: " + id);
        return mastery;
    }

    private static Map<MasteryId, Mastery> buildCanonical() {
        EnumMap<MasteryId, Mastery> result = new EnumMap<>(MasteryId.class);

        register(result, pair(MasteryId.PULSION, "PULSIÓN", MasteryNarratives.PULSION, CharacterClass.INDOMITO,
                variant("RECICLAJE DE PULSIÓN", false, MasteryType.PASSIVE, Attribute.AGUANTE, 35, 50, MasteryNarratives.RECICLAJE_DE_PULSION,
                        "Pasiva. Desde AGUANTE 35 reduce linealmente el coste de FINTAR desde 5 PA hasta 3,5 PA en AGUANTE 50 y aumenta el salto horizontal desde el 100 % hasta el 150 % de la altura. No aumenta daño contundente ni distancia de finta."),
                variant("AURA DE PULSIÓN", true, MasteryType.PASSIVE, Attribute.AGUANTE, 35, 50, MasteryNarratives.AURA_DE_PULSION,
                        "Pasiva desbloqueada en AGUANTE 50. Sustituye exclusivamente el multiplicador CONTUNDENTE del ataque CARGADO de x1,30 por x1,35; su gasto de PA permanece x1,30 y no altera ataques con salto ni proyectiles.")));
        register(result, pair(MasteryId.EXPLOSION_CINETICA, "REFINAMIENTO DE ENERGÍA MALDITA", MasteryNarratives.EXPLOSION_CINETICA, CharacterClass.INDOMITO,
                variant("EXPLOSIÓN CINÉTICA", false, MasteryType.SUSTAINED, Attribute.AGUANTE, 15, 20, MasteryNarratives.EXPLOSION_CINETICA_VARIANT,
                        "Sostenida y mutuamente excluyente con ENDURECIMIENTO POTENCIAL. Se desbloquea en AGUANTE 20 para hombres y 15 para mujeres. Cuando PA llega exactamente a 0, descarga en un radio de 2,5 veces la altura: inflige daño bruto CONTUNDENTE igual al AGUANTE del usuario y aplica retroceso/stagger mediante StaggerPolicy después de descontar la ESTABILIDAD FÍSICA del objetivo."),
                variant("ENDURECIMIENTO POTENCIAL", true, MasteryType.SUSTAINED, Attribute.AGUANTE, 30, 30, MasteryNarratives.ENDURECIMIENTO_POTENCIAL,
                        "Sostenida y mutuamente excluyente con EXPLOSIÓN CINÉTICA. Se desbloquea en AGUANTE 40 para hombres y 30 para mujeres. Cuando PA llega exactamente a 0 durante un impacto cuerpo a cuerpo, refleja al atacante el daño físico real y el stagger real que habría recibido el usuario.")));
        register(result, pair(MasteryId.LIBERACION_HELICOIDAL, "LIBERACIÓN HELICOIDAL", MasteryNarratives.LIBERACION_HELICOIDAL, CharacterClass.INDOMITO,
                variant("LIBERACIÓN HELICOIDAL", false, MasteryType.PASSIVE, Attribute.AGUANTE, 50, 60, MasteryNarratives.LIBERACION_HELICOIDAL_VARIANT,
                        "Pasiva. Reduce el tiempo de recuperación completa de PA según carga: ≤1/3 = 0,5 s; >1/3–≤2/3 = 1 s; >2/3–<3/3 = 1,5 s; ≥3/3 = 3 s. Alcanzar o superar la capacidad máxima inmoviliza igualmente al personaje y no detiene PA REGEN."),
                variant("OPTIMIZACIÓN HELICOIDAL", true, MasteryType.PASSIVE, Attribute.AGUANTE, 50, 60, MasteryNarratives.OPTIMIZACION_HELICOIDAL,
                        "Pasiva. Expande LIBERACIÓN HELICOIDAL otorgando inmunidad a cualquier inhibición de PA REGEN. No elimina modificadores de velocidad o latencia que no sean una inhibición y no evita la inmovilización por alcanzar o superar la capacidad máxima de carga.")));
        register(result, pair(MasteryId.ANULACION, "ANULACIÓN", MasteryNarratives.ANULACION, CharacterClass.INDOMITO,
                variant("ANULACIÓN INCIDENTAL", false, MasteryType.PASSIVE, Attribute.AGUANTE, 6, 6, MasteryNarratives.ANULACION_INCIDENTAL,
                        "Pasiva desde AGUANTE 6. Un impacto con arma cuerpo a cuerpo, a distancia, de fuego o arrojadiza inhibe hasta el fin del encuentro hostil todos los efectos del abalorio que el adversario HOSTIL con menos AGUANTE llevaba equipado en el momento de la aplicación. No se extiende automáticamente a abalorios equipados después."),
                variant("ANULACIÓN FUNDACIONAL", true, MasteryType.PASSIVE, Attribute.AGUANTE, 6, 6, MasteryNarratives.ANULACION_FUNDACIONAL,
                        "Pasiva desde AGUANTE 6. Campo radial de 2,5 veces la altura: todo adversario HOSTIL dentro del campo con menos AGUANTE pierde hasta el fin del encuentro los efectos del abalorio que llevaba equipado al quedar afectado. No inhibe maestrías, Marcas Rúnicas ni abalorios equipados posteriormente.")));

        register(result, new TransmutationMastery(MasteryId.TRANSMUTACION, "TRANSMUTACIÓN", MasteryNarratives.TRANSMUTACION, CharacterClass.MAESTRO, List.of(
                new TransmutationNode(TransmutationNodeId.OVERCLOCK, "OVERCLOCK", MasteryType.SUSTAINED,
                        Attribute.CLARIVIDENCIA, 11, null, false,
                        "Las heridas abiertas que no pueden cerrarse por aproximación dependen del tejido de granulación, la proliferación fibroblástica, la angiogénesis, el depósito de matriz extracelular y la contracción mediada por miofibroblastos. OVERCLOCK fuerza de forma consciente esa respuesta reparativa hacia una fibrogénesis reactiva extrema: prioriza el taponamiento y la continuidad tisular por encima de la economía metabólica, reproduciendo una cicatrización por segunda intención acelerada a costa de sustratos y agua.",
                        "Mientras se sostiene, multiplica PV REGEN x4 y multiplica x2 el ritmo de HAMBRE y SED. REGENERACIÓN THETA hereda el multiplicador x4 de PV REGEN."),
                new TransmutationNode(TransmutationNodeId.OVERDRIVE, "OVERDRIVE", MasteryType.SUSTAINED,
                        Attribute.CLARIVIDENCIA, 22, TransmutationNodeId.OVERCLOCK, false,
                        "Cuando la demanda mecánica supera la disponibilidad inmediata de ATP, el organismo recurre a reservas de alta rotación, glucogenólisis, glucólisis anaerobia y catabolismo para sostener durante unos instantes una tarea que ya no puede financiar de manera ordinaria. OVERDRIVE lleva esa prioridad de supervivencia al extremo: permite que el trabajo continúe consumiendo directamente integridad somática cuando la reserva de PA ya no alcanza, intercambiando continuidad funcional presente por daño corporal real.",
                        "Cubre solo la siguiente acción inmediata: cada PA ausente consume 1 PV actual. Cada pulso sostenido se evalúa como una nueva acción inmediata. Puede causar la muerte salvo guardarraíl explícito de 1 PV y no interrumpe por sí mismo PV REGEN."),
                new TransmutationNode(TransmutationNodeId.METAMORPHOSIS, "METAMORPHOSIS", MasteryType.SUSTAINED,
                        Attribute.CLARIVIDENCIA, 33, TransmutationNodeId.OVERDRIVE, false,
                        "La toxicidad depende tanto de la molécula recibida como de la forma en que el organismo la reconoce, distribuye y biotransforma. METAMORPHOSIS impone una reinterpretación sostenida de la agresión antes de su resolución tisular: las rutas que normalmente procesarían una carga tóxica y las que responden a una impronta de Maldición intercambian su destino fisiológico. No neutraliza la agresión; cambia qué sistema del cuerpo debe afrontarla.",
                        "Maestría sostenida. Antes de resolver resistencias e inmunidades, transforma daño de MALDICIÓN en VENENO y daño de VENENO en MALDICIÓN, conservando exactamente su magnitud."),
                new TransmutationNode(TransmutationNodeId.MIRAGE, "MIRAGE", MasteryType.SUSTAINED,
                        Attribute.CLARIVIDENCIA, 66, TransmutationNodeId.METAMORPHOSIS, false,
                        "La percepción no reconstruye el mundo de manera pasiva: integra señales sensoriales incompletas con predicciones, continuidad temporal y expectativas sobre dónde debería encontrarse un cuerpo. MIRAGE explota conscientemente esa ventana de integración, desacoplando durante instantes la continuidad motora real del usuario de la predicción perceptiva que el observador intenta mantener. El cuerpo no desaparece; el modelo perceptivo del adversario deja momentáneamente de converger con él.",
                        "Otorga fotogramas de invulnerabilidad. Todo adversario que tenga fijado al usuario pierde temporalmente esa fijación y la recupera al concluir el desfase. El desfase paraliza momentáneamente PV REGEN del usuario."),
                new TransmutationNode(TransmutationNodeId.MIRRORS_EDGE, "MIRROR'S EDGE", MasteryType.PASSIVE,
                        Attribute.CLARIVIDENCIA, 75, TransmutationNodeId.MIRAGE, false,
                        "La percepción sostenida del Intersticio deja de ser observación unilateral cuando el sistema nervioso aprende a tratar sus estímulos como parte estable del mismo marco causal que gobierna el cuerpo. MIRROR'S EDGE representa ese punto sin retorno: aquello que antes podía advertirse sin compartir plenamente su plano de interacción pasa a responder al observador con la misma reciprocidad con la que éste responde a ello.",
                        "Pasiva e irreversible desde CLARIVIDENCIA 75. El usuario inflige daño normal a seres del INTERSTICIO y los seres del INTERSTICIO infligen daño normal al usuario; desaparece la asimetría de interacción en ambos sentidos.")
        )));

        register(result, structured(MasteryId.HOMEOSTASIS_TERMICA, "HOMEOSTASIS TÉRMICA", MasteryNarratives.HOMEOSTASIS_TERMICA, MasteryStructure.BINARY, CharacterClass.ESPECIALISTA,
                stage("REGULACIÓN CALÓRICA SUPERIOR", MasteryType.PASSIVE, Attribute.DESTREZA, 20,
                        MasteryNarratives.REGULACION_CALORICA_SUPERIOR,
                        "Duplica la duración de los estados SACIATED e HYDRATED, sin penalización ni consumo de PA."),
                stage("ADAPTACIÓN TÉRMICA", MasteryType.SUSTAINED, Attribute.DESTREZA, 20,
                        MasteryNarratives.ADAPTACION_TERMICA,
                        "Cuando el build-up de FRÍO ESCARCHANTE ya está activo, paraliza su progresión acumulada sin revertirla y consume 1 PA por segundo real.")));

        register(result, structured(MasteryId.EMPATIA_ANIMAL, "EMPATÍA ANIMAL", MasteryNarratives.EMPATIA_ANIMAL,
                MasteryStructure.CONCATENATED, CharacterClass.INTELECTUAL, animalEmpathyStages()));

        register(result, structured(MasteryId.INCITAR, "INCITAR", MasteryNarratives.INCITAR, MasteryStructure.DUAL, CharacterClass.LUCHADOR,
                stage("PROVOCAR", MasteryType.ACTIVE, Attribute.FUERZA, 25, MasteryNarratives.PROVOCAR,
                        "Sólo hombre→hombre. En encuentro hostil, si FUERZA del usuario > FUERZA del adversario, la penalización persiste durante todo el encuentro aunque el objetivo ataque a otra persona: latencia PA REGEN 1,20 s y recuperación completa 5 s, o 3 s con LIBERACIÓN HELICOIDAL. Si decide atacar al provocador, no puede fijarlo como blanco."),
                stage("GRITO DE GUERRA", MasteryType.ACTIVE, Attribute.FUERZA, 50, MasteryNarratives.GRITO_DE_GUERRA,
                        "Sólo masculino. Exige PA actuales = PA TOTALES. Prepara una única oportunidad: el siguiente golpe cuerpo a cuerpo que conecte contra un hombre consume 0 PA. Si conecta contra una mujer, la oportunidad se consume igualmente pero el ataque paga su coste normal."),
                stage("CAPITALIZAR", MasteryType.PASSIVE, Attribute.CARISMA, 18, MasteryNarratives.CAPITALIZAR,
                        "Sólo mujer→hombre y sólo al comprar. Requiere CARISMA superior al vendedor; la ventaja se limita a +10. Reduce precio según categoría: primera necesidad 1–4 %, interés social 1–10 %, uso privativo 15–30 %."),
                stage("RENTABILIZAR", MasteryType.PASSIVE, Attribute.CARISMA, 21, MasteryNarratives.RENTABILIZAR,
                        "Sólo mujer→hombre y sólo al vender. Incrementa el beneficio según TIPO DE RELACIÓN y categoría económica: AMISTOSA +100/+66,2/+100 %; FIABLE +100/+100/+49,9 %; INDIFERENTE +8,2/+52,4/+0,4 %; DESCONFIADA +0,2/+12,3/+0 %; ANTIPÁTICA +0/+1,3/+0 % para primera necesidad/interés social/uso privativo.")));

        register(result, structured(MasteryId.SANAR, "SANAR", MasteryNarratives.SANAR, MasteryStructure.TRIAD, CharacterClass.APODERADO,
                stage("DRENAR", MasteryType.PASSIVE, Attribute.FE, 32, MasteryNarratives.DRENAR, "Sólo se activa si el usuario causa personalmente la muerte mediante un ataque cuerpo a cuerpo. Restaura PV iguales a los PV TOTALES de la víctima; si sus PV REGEN estaban inhibidos al morir, sólo restaura un valor igual a su VITALIDAD."),
                stage("RESTAURAR", MasteryType.ACTIVE, Attribute.FE, 40, MasteryNarratives.RESTAURAR, "No puede aplicarse a un receptor con 0 PV ni al propio usuario. El PV máximo alcanzable por RESTAURAR es min(PV TOTALES del receptor, PA TOTALES del usuario), y progresa al ritmo del PA REGEN actual del receptor."),
                stage("CUSTODIA", MasteryType.PASSIVE, Attribute.FE, 60, MasteryNarratives.CUSTODIA, "Pasiva. Campo invisible radial de 2,5 veces la altura. Sólo incluye relaciones FIABLES, AMISTOSAS o ROMÁNTICAS dentro del radio; suma sus PV REGEN en un resultado global compartido y dicho PV REGEN no puede ser inhibido. No repele, bloquea ni desplaza a nadie.")));

        register(result, structured(MasteryId.REGENERACION_THETA, "REGENERACIÓN THETA", MasteryNarratives.REGENERACION_THETA, MasteryStructure.UNITARY, CharacterClass.APODERADO,
                stage("REGENERACIÓN THETA", MasteryType.ACTIVE, Attribute.FE, 13, "Requiere PA completos y postura estática semiconsciente. Regenera PV hasta el máximo al mismo ritmo que tarda la barra de PA en regenerarse; se interrumpe al moverse, atacar, recibir daño o actuar.")));

        register(result, structured(MasteryId.TRAYECTORIA_CONVERGENTE, "TRAYECTORIA CONVERGENTE", MasteryNarratives.TRAYECTORIA_CONVERGENTE, MasteryStructure.UNITARY, CharacterClass.ESPECIALISTA,
                stage("TRAYECTORIA CONVERGENTE", MasteryType.PASSIVE, Attribute.DESTREZA, 20, "En combos ligeros de 3 ataques o más, el último ataque sustituye el x1,11 ordinario por x1,40 de daño bruto CONTUNDENTE. En DESARMADO conserva el Flow mientras no empiece PA REGEN ni se complete otro combo, pero todos los ataques del Flow permanecen en x1,40; OVERDRIVE no lo interrumpe.")));

        register(result, structured(MasteryId.INVISIBILIDAD, "INVISIBILIDAD", MasteryNarratives.INVISIBILIDAD, MasteryStructure.UNITARY, CharacterClass.ESPECIALISTA,
                stage("INVISIBILIDAD", MasteryType.SUSTAINED, Attribute.DESTREZA, 70, "Vuelve visualmente indetectable el cuerpo desnudo y consume 1 PA por segundo real. El usuario invisible no puede ser FIJADO COMO BLANCO; tampoco puede fijarse como blanco a un adversario invisible. DESARMADO, arrojadizas pequeñas, consumibles de acceso rápido y otras maestrías preservan el estado; desenvainar/equipar un arma o equipar armadura lo rompe. No oculta sonido, olor, huellas, sangre, partículas ni interacción física.")));

        register(result, structured(MasteryId.ESPIRITU_INFATIGABLE, "ESPÍRITU INFATIGABLE", MasteryNarratives.ESPIRITU_INFATIGABLE, MasteryStructure.UNITARY, CharacterClass.APODERADO,
                stage("ESPÍRITU INFATIGABLE", MasteryType.PASSIVE, Attribute.FE, 3, "Fuera de un encuentro hostil, todo gasto de PA es nulo, incluidos locomoción, acciones físicas y pulsos de maestrías sostenidas; dentro de combate se restaura el coste normal.")));

        register(result, new EvolutiveMastery(MasteryId.TRIBOGENESIS, "TRIBOGÉNESIS", Attribute.ADAPTABILIDAD, MasteryNarratives.TRIBOGENESIS,
                "Maestría evolutiva pasiva común a todas las clases. Desde ADAPTABILIDAD 76, la fricción del cuerpo contra BRONCE, ACERO o COMPUESTO ELECTROMECÁNICO durante un ataque DESARMADO añade entre 1 y 45 puntos de QUEMADURA y permite usos ambientales de ignición o rotura térmica."));
        register(result, new EvolutiveMastery(MasteryId.ELECTROGENESIS, "ELECTROGÉNESIS", Attribute.VITALIDAD, MasteryNarratives.ELECTROGENESIS,
                "Maestría evolutiva pasiva común a todas las clases. Desde VITALIDAD 76, todo contacto DESARMADO inflige entre 1 y 45 puntos eléctricos; el ATURDIMIENTO equivale a 0,01 s por punto eléctrico neto; hereda campos radiales activos y no puede evitarse mediante i-frames de MIRAGE."));

        return Map.copyOf(result);
    }

    /** la ficha visible de EMPATÍA ANIMAL deriva del catálogo canónico de Ferae. */
    private static MasteryStage[] animalEmpathyStages() {
        return FeraeCatalog.all().stream().map(species -> {
            Attribute attribute = species.branch() == FeraeBranch.CARISMA ? Attribute.CARISMA : Attribute.INTELIGENCIA;
            String mechanics;
            if (species.branch() == FeraeBranch.CARISMA) {
                mechanics = "Al alcanzar CARISMA " + species.empathyAttributeRequirement()
                        + ", " + species.label() + " puede establecer un vínculo de compañero de viaje cuando EMPATÍA ANIMAL está disponible.";
            } else {
                String trophy = species.trophy().map(t -> t.label()).orElse("trofeo canónico");
                mechanics = "Con " + trophy + " y INTELIGENCIA " + species.empathyAttributeRequirement()
                        + ", " + species.label() + " evoluciona de " + species.naturalRelationship().label().toUpperCase(java.util.Locale.ROOT)
                        + " a FIABLE. " + (species.isInitiallyHostile() ? "El vínculo exige además [CAZADOR DE CAZADORES]." : "Puede establecer vínculo al alcanzar FIABLE.");
            }
            return stage(species.label().toUpperCase(java.util.Locale.ROOT), MasteryType.PASSIVE,
                    attribute, species.empathyAttributeRequirement(), mechanics);
        }).toArray(MasteryStage[]::new);
    }

    private static PairMastery pair(MasteryId id, String name, String narrativeDescription, CharacterClass resonance, MasteryVariant original, MasteryVariant refined) {
        return new PairMastery(id, name, narrativeDescription, resonance, original, refined);
    }
    private static StructuredMastery structured(MasteryId id, String name, String narrativeDescription, MasteryStructure structure, CharacterClass resonance, MasteryStage... stages) {
        return new StructuredMastery(id, name, narrativeDescription, structure, resonance, List.of(stages));
    }
    private static MasteryVariant variant(String name, boolean refined, MasteryType type, Attribute attribute, int start, int threshold, String narrativeDescription, String mechanicalDescription) {
        return new MasteryVariant(name, refined, type, attribute, start, threshold, narrativeDescription, mechanicalDescription);
    }
    private static MasteryStage stage(String name, MasteryType nature, Attribute attribute, int threshold, String mechanicalDescription) {
        return MasteryStage.of(name, nature, attribute, threshold, mechanicalDescription);
    }
    private static MasteryStage stage(String name, MasteryType nature, Attribute attribute, int threshold, String narrativeDescription, String mechanicalDescription) {
        return MasteryStage.of(name, nature, attribute, threshold, narrativeDescription, mechanicalDescription);
    }
    private static void register(EnumMap<MasteryId, Mastery> map, Mastery mastery) {
        if (map.put(mastery.id(), mastery) != null) throw new IllegalStateException("Maestría duplicada: " + mastery.id());
    }
}
