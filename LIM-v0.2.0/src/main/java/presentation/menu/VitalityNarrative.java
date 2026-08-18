package presentation.menu;

/** Descripción narrativa dinámica de VITALIDAD. */
public final class VitalityNarrative {
    private VitalityNarrative() {}

    public static String descriptionFor(int vitality) {
        if (vitality < 1 || vitality > 120) throw new IllegalArgumentException("VITALIDAD debe estar comprendida entre 1 y 120.");
        if (vitality < 75) return ORDINARY;
        if (vitality == 75) return HUMAN_LIMIT;
        return CONFIGURATIO_ORIGINALIS;
    }

    private static final String ORDINARY = """
Mira, no se me ocurría nada. Tú sube VITALIDAD y tardarán más en matarte y ésta tardará menos en regenerarse mientras te escondes en una esquina tras usar una inyección estimulante.
""";

    private static final String HUMAN_LIMIT = """
just SiO2 maxxing dude
""";

    static final String CONFIGURATIO_ORIGINALIS = """
CONFIGURATIO ORIGINALIS

Existe un punto en el que reparar deja de significar regresar al estado anterior.

La biomáquina comienza a seleccionar soluciones que no pertenecen a ninguna etapa conocida de su desarrollo. Algunas reorganizaciones reducen pérdidas. Otras modifican cómo se distribuye la carga. Otras alteran la relación entre tejido, energía y continuidad de una forma que parece demasiado coherente para ser improvisación.

Al principio puede confundirse con una adaptación extraordinariamente eficiente.

Después empiezan a repetirse patrones.

Estructuras distintas convergen hacia soluciones semejantes.

La materia parece reconocer configuraciones que nadie le enseñó durante esta vida.

No estás evolucionando hacia algo nuevo.

Estás retirando lentamente aquello que impedía que una configuración anterior volviera a expresarse.

Tu corazón todavía recuerda.
""";
}
