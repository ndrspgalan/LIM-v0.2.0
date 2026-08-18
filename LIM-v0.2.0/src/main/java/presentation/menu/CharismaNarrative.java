package presentation.menu;

import domain.character.Gender;
import java.util.Objects;

/** Descripción narrativa dinámica de CARISMA, deliberadamente diferenciada por sexo. */
public final class CharismaNarrative {
    private CharismaNarrative() {}

    /** Compatibilidad técnica histórica: interpreta el perfil masculino. */
    public static String descriptionFor(int charisma) { return descriptionFor(charisma, Gender.HOMBRE); }

    public static String descriptionFor(int charisma, Gender gender) {
        Objects.requireNonNull(gender);
        if (charisma < 1 || charisma > 75) throw new IllegalArgumentException("CARISMA debe estar comprendido entre 1 y 75.");
        if (gender == Gender.HOMBRE) {
            if (charisma > 50) throw new IllegalArgumentException("Un hombre no puede superar CARISMA 50.");
            if (charisma <= 24) return MALE_1_24;
            if (charisma <= 49) return MALE_25_49;
            return MALE_50;
        }
        if (charisma <= 17) return FEMALE_1_17;
        if (charisma <= 20) return FEMALE_18_20;
        if (charisma <= 39) return FEMALE_21_39;
        if (charisma <= 74) return FEMALE_40_74;
        return FEMALE_75;
    }

    private static final String MALE_1_24 = """
Tú también tienes presencia.

Por desgracia, parece estar cotizando a la baja.

No eres necesariamente antipático, feo, aburrido ni desagradable. Sería casi reconfortante disponer de una causa tan fácil de identificar.

Simplemente tienes la impresión persistente de que las interacciones económicas empeoran cuando participas tú.

Entras con dinero.

Sales con menos dinero del que conceptualmente parecía necesario.

Preguntas un precio y, de algún modo, acabas sintiéndote agradecido por haberlo pagado.

Negocias y descubres nuevas formas de perder una negociación cuya existencia ni siquiera habías detectado.

En ocasiones sospechas que te sacan los dineros allá donde vas.

No posees pruebas.

Principalmente porque probablemente también te cobrarían por ellas.

Socialmente tampoco ayudas demasiado. Una conversación puede funcionar perfectamente contigo dentro, pero nadie parece estar recalculando su agenda para prolongarla.

No eres un desastre absoluto.

Eres un desastre perfectamente solvente.

Lo bastante funcional para seguir entrando en establecimientos.

Lo bastante poco convincente para financiar su continuidad.
""";

    private static final String MALE_25_49 = """
Ha ocurrido algo curioso.

Los humanos continúan siendo complicados.

Los animales, bastante menos.

Tu presencia empieza a adquirir una coherencia que no necesita presentación. Postura, seguridad, dirección de la mirada, ocupación del espacio, ritmo del movimiento: pequeñas señales que juntas forman algo que otros organismos parecen leer antes de que tú hayas decidido comunicar nada.

Un perro deja de evaluarte como ruido ambiental.

Un caballo presta atención.

Los animales sociales empiezan a tratar tu posición dentro del grupo como información relevante.

No significa que te obedezcan.

No seas ridículo.

Significa algo bastante más básico:

te han incluido en el cálculo.

También los humanos empiezan a hacerlo.

Todavía puedes decir una estupidez.

Todavía puedes arruinar una conversación.

Todavía puedes pagar demasiado por algo, porque ningún atributo puede proteger completamente a un hombre de sí mismo.

Pero cuando llegas, la presencia llega contigo.

Algo en tu energía masculina ha dejado de solicitar permiso para ocupar espacio.

Y, por algún motivo, la manada parece haberse dado cuenta antes que tú.
""";

    private static final String MALE_50 = """
Bueno.

Mírate.

Todo este tiempo intentando desarrollar una cualidad social y resulta que has terminado convirtiéndote en el papuchón.

No «un hombre atractivo».

Eso sería vulgar.

No «carismático».

Demasiado académico.

Papuchón.

La clase de presencia masculina que hace que otras personas se enderecen ligeramente cuando apareces, que los animales hayan dejado hace mucho de preguntarse quién ocupa qué posición y que incluso una camisa corriente empiece a parecer una decisión estilística deliberada.

No tienes capital erótico como sistema de explotación comercial.

No tienes ahorro social convertido en mecanismo económico.

No hace falta inventarte ventajas que no posees.

Tienes algo mucho más inútil y, por ello, bastante más gracioso:

eres espectacularmente carismático.

Eso es todo.

Has gastado cincuenta puntos para convertirte en una presencia humana de categoría premium.

Enhorabuena.

El mundo no te debe un descuento.

Pero, al menos, cuando te cobren de más, lo harán mirando a un auténtico Papuchón consumado.
""";

    private static final String FEMALE_1_17 = """
No eres desagradable.

Eso sería demasiado fácil de diagnosticar.

Simplemente posees esa rara capacidad para entrar en una interacción perfectamente funcional y reducir ligeramente su valor de mercado.

Una conversación tenía futuro.

Llegaste tú.

Ahora tiene costes de mantenimiento.

No ocurre nada espectacular. Nadie huye. Simplemente aparecen pequeñas fricciones: silencios que duran medio segundo de más, sonrisas profesionales, respuestas correctas y esa expresión que significa «no tengo ningún motivo concreto para marcharme, pero estoy empezando a buscar uno».

El mercado social ha evaluado tu producto.

No lo ha retirado.

Pero tampoco parece interesado en ampliar existencias.

Por ahora, tu mejor estrategia interpersonal consiste en que la otra persona ya necesitase hablar contigo.
""";

    private static final String FEMALE_18_20 = """
Oh.

Esto sí tiene cotización.

Has descubierto una forma de capital que no figura en la cartera, no paga intereses y, sin embargo, modifica decisiones económicas antes de que nadie admita que ha intervenido.

Capital erótico.

Presencia, atractivo, presentación, seguridad, lenguaje corporal y capacidad para convertir una interacción ordinaria en una experiencia que la otra persona valora ligeramente más de lo que debería.

No necesitas pedir un trato favorable.

La gracia consiste en que el otro encuentre razones perfectamente racionales para ofrecértelo por iniciativa propia.

El precio sigue siendo el precio.

La oferta sigue siendo la oferta.

La demanda sigue siendo la demanda.

Sólo que ahora tú también apareces discretamente en la ecuación.
""";

    private static final String FEMALE_21_39 = """
El capital erótico abre la puerta.

El ahorro social explica por qué algunas personas empiezan a mantenerla abierta.

Has descubierto que una relación acumulada posee valor económico propio. Confianza, familiaridad, reciprocidad y previsibilidad reducen el coste psicológico de cada nueva transacción.

Una desconocida tiene que negociar.

Una persona apreciada puede empezar la conversación varios pasos más adelante.

No has abolido el mercado.

Lo has llenado de memoria.

Y ahora manejas simultáneamente dos activos muy difíciles de incluir en una hoja de cálculo: aquello que produces cuando entras en una habitación y aquello que permanece cuando sales.

Capital erótico.

Ahorro social.

El primero modifica cuánto vale el presente.

El segundo empieza a cobrar intereses sobre el pasado.
""";

    private static final String FEMALE_40_74 = """
Esto empieza a ser económicamente obsceno.

Tu capital erótico ya no constituye una ventaja ocasional y tu ahorro social ha dejado de ser una colección de favores.

Forman una infraestructura.

Las personas recuerdan haberte tratado.

Anticipan cómo será volver a hacerlo.

Ajustan expectativas antes de que llegues y reorganizan pequeñas decisiones alrededor de una interacción que todavía no ha ocurrido.

Lo más irritante es que no necesitas hacer demasiado.

A estas alturas, esforzarte visiblemente sería casi contraproducente.

El verdadero privilegio social consiste precisamente en que parte del trabajo ocurre antes de que abras la boca.

No controlas a nadie.

No necesitas hacerlo.

Has conseguido algo mucho más eficiente:

que el sistema social incorpore tu presencia como una variable relevante incluso cuando tú no estás intentando modificarlo.
""";

    private static final String FEMALE_75 = """
Se acabó.

No queda mercado que auditar.

Eres el mercado.

El capital erótico ha alcanzado un punto en el que hablar de atractivo resulta casi ofensivamente insuficiente. El ahorro social acumulado convierte relaciones en redes, redes en reputación y reputación en una forma de presencia que llega a determinados lugares antes que tú.

No necesitas «trabajarte» una interacción como si cada encuentro empezase desde cero.

La gente ya trae consigo expectativas, memoria, curiosidad, confianza, cautela o interés.

Y entonces apareces tú.

Perfectamente consciente de todo ello.

Ésa es probablemente la parte más peligrosa.

No que puedas producir una impresión extraordinaria.

Sino que sabes exactamente qué impresión estás produciendo mientras la produces.

Has alcanzado CARISMA 75.

Mamasita consumada.

No sabemos muy bien qué hacer contigo.

Sospechamos que tú sí.
""";
}
