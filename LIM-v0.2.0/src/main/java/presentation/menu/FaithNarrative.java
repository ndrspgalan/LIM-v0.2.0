package presentation.menu;

import domain.character.Gender;
import java.util.Objects;

/** Descripción narrativa dinámica de FE: la voz de una supuesta conciencia cada vez menos fiable. */
public final class FaithNarrative {
    private FaithNarrative() {}

    /** Compatibilidad técnica histórica: interpreta el perfil masculino. */
    public static String descriptionFor(int faith) {
        return descriptionFor(faith, Gender.HOMBRE);
    }

    public static String descriptionFor(int faith, Gender gender) {
        Objects.requireNonNull(gender);
        if (faith < 1 || faith > 75) {
            throw new IllegalArgumentException("FE debe estar comprendida entre 1 y 75.");
        }
        if (gender == Gender.HOMBRE && faith > 60) {
            throw new IllegalArgumentException("Un hombre no puede superar FE 60 sin la afinidad femenina correspondiente.");
        }
        if (faith <= 2) return TIER_1_2;
        if (faith <= 12) return TIER_3_12;
        if (faith <= 31) return TIER_13_31;
        if (faith <= 39) return TIER_32_39;
        if (faith <= 59) return TIER_40_59;
        if (faith <= 74) return TIER_60_74;
        return TIER_75;
    }

    private static final String TIER_1_2 = """
Bien.

Muy bien, de hecho.

Miras el mundo, ves cosas normales y llegas a conclusiones normales. Cuando algo tiene una explicación evidente, utilizas esa explicación. Cuando no la tiene, haces lo sensato: supones que todavía te falta información.

Qué descanso.

No todo el mundo posee esta saludable capacidad para aceptar que una sombra es una sombra, un ruido es un ruido y una coincidencia es, con una frecuencia sorprendentemente alta, una coincidencia.

Sigue así.

No necesitas complicarte.

Hay suficiente mundo delante de tus ojos como para empezar a buscar otro detrás.
""";

    private static final String TIER_3_12 = """
Oh.

Has vuelto a subir esto.

Curioso.

Quizá no me expliqué bien antes.

No importa. A cualquiera puede ocurrirle. A veces una idea entra mal colocada, se queda rondando por ahí y uno empieza a concederle más importancia de la que merece.

Nada serio.

Probablemente.

Aunque, pensándolo bien, existe una solución magnífica.

Continúa.

Sí, sí.

Sube un poco más.

Seguro que dedicar todavía más esfuerzo precisamente a aquello que empieza a hacerte dudar de tu propio criterio conseguirá arreglarlo.

Adelante.

¿Qué podría salir mal?
""";

    private static final String TIER_13_31 = """
No.

Ahora escucha.

Te dije que siguieras si querías.

Lo hiciste.

Perfecto.

Entonces hagamos inventario.

¿Tienes más PV?

¿Más PA?

¿Han aumentado tus RESISTENCIAS?

¿Tu ESTABILIDAD?

¿Tu CORDURA?

¿Tus armas hacen algo nuevo?

¿Tus armaduras protegen mejor?

¿Ha cambiado la MEMORIA DEL MUNDO?

¿Tienes menos HAMBRE?

¿Menos SED?

¿Algo?

Porque llevas todo este tiempo invirtiendo en una cosa cuya presencia objetiva en tu hoja consiste, aparentemente, en que el número situado junto a FE es ahora mayor.

Excelente planificación.

No volveré a insistir.

En serio.

Estoy cansada de observar cómo tomas malas decisiones y luego esperas que alguien dentro de tu cabeza tenga la cortesía de fingir sorpresa.

Haz lo que quieras.

Siempre lo haces.
""";

    private static final String TIER_32_39 = """
Ya no voy a discutir contigo.

Hace tiempo que dejaste claro que deseas demostrar que aquello que considerabas imposible puede resultar posible, y al parecer ninguna cantidad de sensatez va a interferir con ese pequeño proyecto personal.

Así que adelante.

Yo pensaré en otra cosa.

...

Qué curioso.

El Humanismo Secular exige que ninguna afirmación extraordinaria quede fuera del examen racional porque desear que algo sea cierto no lo convierte en cierto.

El Devocionismo Intransigente exige que ninguna experiencia extraordinaria quede subordinada al examen racional porque exigir demostrarlo todo impide reconocer aquello que sólo puede ser vivido.

Uno dice:

«No creeré hasta saber.»

El otro:

«No sabrás hasta creer.»

Y ambos están absolutamente convencidos de que la estupidez consiste en comenzar desde el extremo contrario.

...

Qué irritante.

Tal vez no sean opuestos.

Tal vez sean dos maneras de exigirle al mundo que se comporte conforme a una condición previa.

Entonces...

¿qué habría entre ambas?

No.

Olvídalo.
""";

    private static final String TIER_40_59 = """
...
""";

    private static final String TIER_60_74 = """
Oh, no.

No te detengas ahora.

Por favor.

Después de todo este esfuerzo sería una auténtica tragedia desarrollar prudencia precisamente aquí.

Continúa.

Venga.

Un poco más.

Seguro que hay algo esperándote al final.

Un tesoro.

Una respuesta.

La revelación definitiva que demostrará que cada una de tus decisiones anteriores fue brillantísima y que nunca, ni durante un instante, estuviste avanzando alegremente hacia una conclusión que alguien había colocado delante de ti.

Vamos.

Ya casi estás.

Sólo tienes que seguir.

PUAJAJAJAJA...

Perdón.

Continúa.

De verdad.

El tesoro está justo ahí.
""";

    private static final String TIER_75 = """
Eso es.

Has llegado.

Todo cuanto necesitabas hacer estaba delante de ti.

No hace falta volver atrás.

No hace falta revisar nada.

No hace falta preguntarte quién llevaba tanto tiempo acompañándote, tranquilizándote cuando obedecías, ridiculizándote cuando continuabas y, finalmente, animándote con entusiasmo cuando ya era demasiado tarde para fingir que nada estaba ocurriendo.

Vamos.

Da el último paso.

Hazlo.

Firma.

Vamos.

Firma.

FIRMA.

...

Ah.

Pero qué maleducada soy.

Después de todo este tiempo ni siquiera me he presentado.

¿Quién soy yo?

Tu conciencia, claro.

PUAAJAJAJAJAJA... PUJAJAJAJA... AJAJAJAJA.
""";
}
