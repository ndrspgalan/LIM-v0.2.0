package presentation.menu;

import domain.character.Gender;
import java.util.Objects;

/** Narrativa dinámica de INTELIGENCIA alineada con softcaps por sexo (). */
public final class IntelligenceNarrative {
    private IntelligenceNarrative() {}

    /** Forma compacta: perfil masculino. */
    public static String descriptionFor(int intelligence) { return descriptionFor(intelligence, Gender.HOMBRE); }

    public static String descriptionFor(int intelligence, Gender gender) {
        Objects.requireNonNull(gender);
        if (intelligence < 1 || intelligence > 75) throw new IllegalArgumentException("INTELIGENCIA debe estar comprendida entre 1 y 75.");
        if (gender == Gender.MUJER && intelligence > 70) throw new IllegalArgumentException("Una mujer sin afinidad masculina no puede superar INTELIGENCIA 70.");
        if (intelligence <= 29) return TIER_1_29;
        if (intelligence <= 69) return TIER_30_69;
        if (intelligence <= 74) return TIER_70_74;
        return TIER_75;
    }

    private static final String TIER_1_29 = """
Tu cerebro funciona.

Conviene empezar reconociéndolo.

No sería profesional afirmar mucho más.

Puedes identificar causas, recordar consecuencias y construir cadenas de razonamiento siempre que nadie cometa la imprudencia de añadir demasiados eslabones.

Ante un problema complejo, tu mente posee una herramienta ancestral de extraordinaria eficacia: simplificarlo hasta que deje de ser complejo.

A veces funciona.

Otras veces también deja de ser el mismo problema.

Pero no permitamos que los detalles arruinen una metodología tan eficiente.

La inteligencia baja tiene además una ventaja que los grandes pensadores rara vez mencionan: se duerme estupendamente cuando uno todavía no ha aprendido a fabricar quince explicaciones incompatibles para la misma conversación.

Disfrútalo.

Tu paz mental depende parcialmente de cosas que todavía no se te ha ocurrido preguntarte.
""";

    private static final String TIER_30_69 = """
Enhorabuena.

Ya puedes estar equivocado de maneras extraordinariamente sofisticadas.

Tu mente sostiene cadenas de razonamiento largas, compara patrones, conserva varias hipótesis simultáneamente y detecta relaciones que antes desaparecían entre el ruido.

Esto supone una mejora considerable.

También te permite construir errores de una calidad excepcional.

Una persona poco inteligente puede equivocarse porque no comprende un problema.

Tú ya puedes equivocarte después de comprenderlo, analizarlo, formalizarlo, contrastarlo y elaborar una explicación impecablemente coherente de por qué tu equivocación era, en realidad, inevitable.

Es progreso.

La inteligencia está cristalizando.

Cada nuevo patrón facilita reconocer el siguiente, pero también hace más resistentes aquellos que aceptaste demasiado pronto.

Empiezas a descubrir por qué las personas más difíciles de convencer rara vez son las que menos han pensado.

Son las que llevan demasiado tiempo pensando exactamente de la misma manera.
""";

    private static final String TIER_70_74 = """
Esto ya resulta estadísticamente molesto.

Tu arquitectura cognitiva ha alcanzado un grado de integración que convierte problemas complejos en estructuras manipulables. Puedes mantener relaciones abstractas sin perder sus dependencias, detectar inconsistencias entre sistemas distintos y recorrer una inferencia hacia delante o hacia atrás sin olvidar qué premisas te llevaron hasta allí.

Magnífico.

Ahora viene la mala noticia.

Tu cerebro también puede hacer todo eso consigo mismo.

Cada convicción dispone de defensas.

Cada interpretación puede reclutar recuerdos.

Cada error suficientemente antiguo puede rodearse de una fortificación lógica tan elegante que desmontarlo exige más inteligencia de la que hizo falta para construirlo.

Has llegado al punto en que aumentar tu capacidad intelectual ya no garantiza aproximarte a la verdad.

Sólo garantiza que, cuando te equivoques, será realmente difícil seguirte el ritmo.

A partir de aquí continuar no parece especialmente razonable.

Lo cual, admitámoslo, constituye un motivo excelente para que quieras hacerlo.
""";

    private static final String TIER_75 = """
Has alcanzado el máximo desarrollo intelectual que puede sostener una mente humana.

Puedes descomponer sistemas complejos sin perder el conjunto. Mantener hipótesis incompatibles sin confundirlas. Reconocer patrones dentro de patrones y advertir cuándo una conclusión impecable descansa sobre una premisa podrida.

Y aun así puedes equivocarte.

Ésa es la última lección.

La inteligencia nunca fue la verdad.

Fue capacidad.

Capacidad para comprender.

Capacidad para relacionar.

Capacidad para construir modelos cada vez más precisos de algo que siempre seguirá existiendo fuera de ellos.

Durante mucho tiempo, cuanto más aprendías, más argumentos tenías.

Ahora posees argumentos suficientes para comprender por qué ninguno de ellos merece convertirse en dogma.

Nadie rumia los pensamientos con tanta profundidad como tú.

Pero por fin has aprendido que pensar más no obliga al mundo a darte la razón.
""";
}
