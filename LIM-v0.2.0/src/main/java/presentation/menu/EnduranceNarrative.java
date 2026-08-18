package presentation.menu;

import domain.character.Gender;
import java.util.Objects;

/** Narrativa dinámica de AGUANTE alineada con softcaps por sexo (). */
public final class EnduranceNarrative {
    private EnduranceNarrative() {}

    /** Forma compacta: perfil masculino. */
    public static String descriptionFor(int endurance) { return descriptionFor(endurance, Gender.HOMBRE); }

    public static String descriptionFor(int endurance, Gender gender) {
        Objects.requireNonNull(gender);
        if (endurance < 1 || endurance > 75) throw new IllegalArgumentException("AGUANTE debe estar comprendido entre 1 y 75.");
        if (gender == Gender.MUJER) {
            if (endurance > 30) throw new IllegalArgumentException("Una mujer sin afinidad masculina no puede superar AGUANTE 30.");
            if (endurance <= 14) return TIER_1;
            if (endurance <= 29) return TIER_2;
            return TIER_3;
        }
        if (endurance <= 19) return TIER_1;
        if (endurance <= 39) return TIER_2;
        if (endurance <= 74) return TIER_3;
        return TIER_75;
    }

    private static final String TIER_1 = """
Tu organismo padece una enfermedad gravísima.

Se llama esfuerzo.

Los síntomas son devastadores: respiración acelerada, calor, piernas pesadas y una súbita convicción de que continuar durante treinta segundos más constituiría una violación manifiesta de los derechos humanos.

No debes alarmarte.

Millones de personas sobreviven a ella cada día.

Tu cuerpo simplemente todavía no lo sabe.

En cuanto disminuyen las reservas inmediatas, empieza el espectáculo. Los músculos protestan. Los pulmones presentan una reclamación formal. La voluntad recibe informes contradictorios de todos los departamentos y alguien propone detener la actividad «por seguridad».

Qué considerada es la fisiología cuando se trata de ahorrar energía.

Lo peor es que todavía interpretas cada una de esas señales como una orden.

Estás cansado.

Enhorabuena.

Eso significa que por fin has empezado.
""";

    private static final String TIER_2 = """
Has descubierto algo bastante comprometedor sobre el cansancio:

miente.

No siempre.

No completamente.

Pero muchísimo más de lo que parecía.

Tu organismo continúa enviando las mismas comunicaciones urgentes. Solicita descansos. Recomienda reducir el ritmo. Advierte de que las reservas están descendiendo y sugiere, con extraordinaria insistencia, que quizá éste sea un momento excelente para sentarse.

La diferencia es que ahora conoces al remitente.

Has trabajado después del primer aviso.

Y del segundo.

Y del tercero.

Sorprendentemente, sigues vivo.

Empiezas a distinguir agotamiento real de incomodidad, necesidad fisiológica de preferencia metabólica y límite físico de esa antiquísima estrategia corporal consistente en hacerte sentir miserable hasta que dejes de gastar recursos.

El cuerpo continúa teniendo derecho a presentar quejas.

Tú has dejado de aprobarlas automáticamente.
""";

    private static final String TIER_3 = """
El cansancio ha perdido la mayoría absoluta.

Continúa presente. Continúa votando. Continúa pronunciando discursos larguísimos acerca de glucógeno, temperatura, hidratación, dolor y lo sensato que sería detenerse.

Pero ya no gobierna.

Has sometido tantas veces al organismo a la necesidad de reorganizar sus recursos que la economía del esfuerzo empieza a parecerte menos una emergencia y más un procedimiento.

Sabes cuándo conservar.

Sabes cuándo gastar.

Sabes qué sensaciones anuncian un límite y cuáles sólo anuncian que el cuerpo preferiría no descubrir dónde está.

Ésa es una diferencia que no puede aprenderse descansando.

La resistencia no consiste en ser incapaz de cansarse.

Eso sería una anomalía fisiológica bastante poco interesante.

Consiste en poder mirar al cansancio, escuchar atentamente todo cuanto tiene que decir y responder:

«Solicitud denegada.»
""";

    private static final String TIER_75 = """
El cuerpo ha agotado sus argumentos.

Probó la fatiga.

Probó el dolor.

Probó el ardor.

Probó la respiración desesperada, la pesadez, la incomodidad y esa maravillosa sensación de que cada segundo adicional debería necesitar autorización médica.

Funcionaron durante años.

Después dejaron de hacerlo.

No porque hayas dejado de sentir.

No porque el organismo haya dejado de necesitar energía.

No porque hayas abolido la fisiología mediante fuerza de voluntad.

Has aprendido algo bastante más difícil: a distinguir con precisión aquello que el cuerpo no puede hacer de aquello que simplemente no quiere seguir haciendo.

Has alcanzado el límite humano del aguante.

El cansancio seguirá llegando.

El dolor seguirá hablando.

El cuerpo seguirá intentando convencerte.

Pero ya no existe dentro de ti ninguna parte que confunda su voz con una orden.
""";
}
