package presentation.menu;

import domain.character.Gender;
import java.util.Objects;

/** Narrativa dinámica de FUERZA alineada con softcaps por sexo (). */
public final class StrengthNarrative {
    private StrengthNarrative() {}

    /** Forma compacta: perfil masculino. */
    public static String descriptionFor(int strength) { return descriptionFor(strength, Gender.HOMBRE); }

    public static String descriptionFor(int strength, Gender gender) {
        Objects.requireNonNull(gender);
        if (strength < 1 || strength > 75) throw new IllegalArgumentException("FUERZA debe estar comprendida entre 1 y 75.");
        if (gender == Gender.MUJER) {
            if (strength > 30) throw new IllegalArgumentException("Una mujer sin afinidad masculina no puede superar FUERZA 30.");
            if (strength <= 20) return TIER_1;
            if (strength <= 29) return TIER_2;
            return TIER_3;
        }
        if (strength <= 24) return TIER_1;
        if (strength <= 49) return TIER_2;
        if (strength <= 74) return TIER_3;
        return TIER_75;
    }

    private static final String TIER_1 = """
Tu relación con la fuerza física es, por el momento, principalmente burocrática.

Sabes que existe.

Has visto a otras personas utilizarla.

Incluso puedes señalar aproximadamente dónde debería encontrarse en tu cuerpo.

El problema empieza cuando intentas hacer algo con ella.

Una puerta pesada todavía puede convertir una entrada dramática en una negociación. Una carga incómoda adquiere inmediatamente propiedades arquitectónicas. Y cuando algo se resiste a moverse, tu estrategia consiste en cambiar el ángulo, recolocar los pies y volver a intentarlo con una expresión facial progresivamente más ofensiva.

La materia permanece impasible.

No es personal.

Una piedra tampoco se apartaría si le gritases.

Tu cuerpo, sin embargo, empieza a comprender una verdad bastante menos halagadora: querer ejercer fuerza y ser capaz de producirla son acontecimientos completamente distintos.

Por ahora, cuando el mundo te dice «no», suele tener argumentos mejores.
""";

    private static final String TIER_2 = """
Algo desagradable le está ocurriendo a los objetos.

Empiezan a moverse cuando tú quieres.

No todos. No siempre. Y algunos todavía conservan suficiente dignidad estructural como para obligarte a trabajar.

Pero la antigua relación unilateral se ha terminado.

Tu cuerpo ya no necesita convertir cada esfuerzo serio en una representación teatral. El suelo recibe mejor tus apoyos. Los golpes empiezan a desplazar aquello que alcanzan. El peso deja de ser una propiedad abstracta escrita en una ficha y empieza a convertirse en una cantidad de trabajo que sabes administrar.

Hasta las personas responden de otra manera cuando las fuerzas dejan de estar equilibradas.

No porque tus músculos hayan adquirido poderes diplomáticos.

Simplemente existe un punto en el que un cuerpo humano comprende, con admirable rapidez y sin necesidad de cálculos, que el otro puede moverlo.

Has dejado de pedir permiso a la materia.

Ahora empezáis a negociar.

Y por primera vez, ella también está haciendo concesiones.
""";

    private static final String TIER_3 = """
La negociación ha terminado.

Cuando aplicas fuerza, algo ocurre.

Una masa cambia de posición. Un cuerpo pierde equilibrio. Una pendiente deja de ser una pared maleducada y vuelve a convertirse en geometría. Un arma pesada deja de condicionar el movimiento y empieza a prolongarlo.

No hay misterio.

No has descubierto una energía secreta.

No has trascendido las leyes de la mecánica.

Te has vuelto extraordinariamente competente utilizándolas.

Eso resulta bastante más preocupante.

Porque la fuerza verdaderamente desarrollada tiene muy poco que ver con parecer fuerte. No necesita volumen innecesario, amenazas, aspavientos ni esa entrañable costumbre masculina de tensar músculos durante una discusión que no los requiere.

La fuerza madura es silenciosa.

Se coloca correctamente.

Transfiere el peso.

Aplica trabajo.

Y termina.

A estas alturas, cuando algo sigue sin moverse, ya no te preguntas si eres suficientemente fuerte.

Empiezas a preguntarte si debería permanecer donde está.
""";

    private static final String TIER_75 = """
Aquí termina la fuerza humana.

No porque hayas alcanzado una cifra arbitraria.

Porque ya no queda adaptación humana convencional capaz de aumentar significativamente lo que tu cuerpo puede imponer mecánicamente sobre el mundo.

Tus apoyos transmiten toda la fuerza que pueden transmitir. Tus cadenas musculares trabajan como una unidad. El peso, la aceleración y la resistencia han dejado de ser obstáculos separados: son variables de una misma acción.

Durante mucho tiempo, la materia decidió qué podías hacer.

Después aprendiste a discutir con ella.

Más tarde aprendiste a vencerla.

Ahora posees fuerza suficiente para que esa pregunta haya dejado de ser interesante.

Puedes moverlo.

La cuestión que queda es bastante más difícil:

¿por qué ibas a hacerlo?
""";
}
