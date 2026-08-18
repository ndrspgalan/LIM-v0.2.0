package presentation.menu;

import domain.character.Gender;
import java.util.Objects;

/** Descripción narrativa dinámica de DESTREZA según softcaps y sexo. */
public final class DexterityNarrative {
    private DexterityNarrative() {}

    /** Compatibilidad técnica histórica: interpreta el perfil masculino. */
    public static String descriptionFor(int dexterity) {
        return descriptionFor(dexterity, Gender.HOMBRE);
    }

    public static String descriptionFor(int dexterity, Gender gender) {
        Objects.requireNonNull(gender);
        if (dexterity < 1 || dexterity > 75) {
            throw new IllegalArgumentException("DESTREZA debe estar comprendida entre 1 y 75.");
        }
        if (gender == Gender.HOMBRE && dexterity > 70) {
            throw new IllegalArgumentException("Un hombre sin afinidad femenina no puede superar DESTREZA 70.");
        }
        if (dexterity <= 19) return TIER_1_19;
        if (dexterity <= 69) return TIER_20_69;
        if (dexterity <= 74) return TIER_70_74;
        return TIER_75;
    }

    private static final String TIER_1_19 = """
Tus extremidades mantienen una relación cordial.

No particularmente estrecha.

La mano sabe lo que quiere hacer. El ojo dispone de información razonablemente buena. El pie ha recibido instrucciones. El problema aparece cuando todos intentan participar en la misma acción.

Entonces comienza la democracia.

El cuerpo debate.

Rectifica.

Introduce una pequeña corrección.

Descubre que la corrección necesitaba otra corrección.

Y finalmente completa un movimiento aproximadamente parecido al que habías imaginado, aunque con suficientes modificaciones como para reclamar autoría propia.

No eres torpe.

La palabra torpe presupone cierta constancia.

Tú eres mucho más interesante: puedes ejecutar una acción perfectamente y, cinco segundos después, ofrecer una reconstrucción experimental de cómo habría salido si nadie hubiese practicado antes.

Por ahora, la precisión sigue siendo un accidente con buena prensa.
""";

    private static final String TIER_20_69 = """
Empieza a resultar difícil atribuirlo a la suerte.

Una vez puede ser casualidad.

Diez veces, una buena racha.

Cien veces obligan a revisar la hipótesis.

Tus manos llegan donde pretendías. Los apoyos aparecen antes de necesitarlos. El cuerpo corrige durante el movimiento en vez de esperar a equivocarse y presentar después una disculpa biomecánica.

La distancia deja de ser simplemente espacio: empieza a dividirse en trayectorias.

El equilibrio deja de ser una posición: se convierte en algo que puedes perder y recuperar deliberadamente.

Y tus movimientos empiezan a contener esa cualidad profundamente irritante que poseen las cosas difíciles cuando alguien las hace parecer obvias.

Todavía existe margen.

Muchísimo.

Pero ya no estamos documentando una sucesión de casualidades.

Estamos ante un accidente estadístico con preocupante capacidad para reproducirse.
""";

    private static final String TIER_70_74 = """
No.

Repetid la prueba.

El instrumental debe de estar mal.

A estas alturas, la coordinación ordinaria ya no explica satisfactoriamente lo observado. El movimiento empieza antes de que resulte evidente que era necesario. Una corrección aparece dentro de otra corrección. El cuerpo atraviesa espacios estrechos como si hubiese negociado previamente sus dimensiones.

Y lo más absurdo es que ya no había ninguna razón sensata para seguir mejorando.

Hace mucho que obtuviste de la coordinación todo cuanto una persona razonable necesitaría para desenvolverse por el mundo.

Pero continuaste.

Por supuesto que continuaste.

Ahora los errores son tan pequeños que el error más probable parece estar en quien los está midiendo.

La estadística solicita una segunda muestra.

La biomecánica solicita tus datos.

Y la realidad, por primera vez, parece estar calculando tu margen de error antes de intentar tocarte.
""";

    private static final String TIER_75 = """
Ya no corriges.

No porque seas incapaz de equivocarte.

Porque cada movimiento contiene desde su inicio las correcciones que antes necesitaban aparecer después.

No sobra distancia.

No sobra tensión.

No sobra tiempo.

Entre intención y ejecución apenas queda algo que pueda eliminarse sin dejar de ser humano.

Durante años entrenaste al cuerpo para obedecer con mayor precisión.

Después aprendiste que la precisión no consistía en obligarlo a seguir una trayectoria perfecta, sino en permitir que millones de pequeñas decisiones mecánicas convergieran hacia una sola acción.

Has alcanzado el límite humano de la destreza.

Desde fuera parece facilidad.

Tú sabes cuánto trabajo hizo falta para que finalmente no pareciera trabajo.
""";
}
