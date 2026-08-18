package presentation.menu;

import domain.character.Gender;

/** Narrativa fisiológica de ADAPTABILIDAD segregada por sexo hasta el umbral extraordinario. */
public final class AdaptabilityNarrative {
    private AdaptabilityNarrative() {}

    public static String descriptionFor(int adaptability, Gender gender) {
        if (adaptability < 1 || adaptability > 120) throw new IllegalArgumentException("ADAPTABILIDAD debe estar comprendida entre 1 y 120.");
        if (gender == null) throw new IllegalArgumentException("El sexo no puede ser nulo.");
        if (adaptability == 75) return HUMAN_LIMIT;
        if (adaptability > 75) return VitalityNarrative.CONFIGURATIO_ORIGINALIS;
        if (gender == Gender.HOMBRE) return adaptability <= 11 ? MALE_1_11 : MALE_12_74;
        return adaptability <= 11 ? FEMALE_1_11 : FEMALE_12_74;
    }

    /** Compatibilidad de lectura histórica; las pantallas canónicas deben resolver el sexo. */
    public static String descriptionFor(int adaptability) { return descriptionFor(adaptability, Gender.HOMBRE); }

    private static final String MALE_1_11 = """
La primera vez siempre cuesta más.

Tu organismo todavía responde a una adversidad como a un problema que acaba de conocer. Una agresión mecánica obliga a distribuir una carga antes de que supere la capacidad local del tejido. El frío modifica perfusión y producción de calor. Una sustancia tóxica debe absorberse, distribuirse, metabolizarse y eliminarse. Una quemadura obliga a contener lesión térmica e inflamación.

Cada sistema sabe aproximadamente qué hacer. Lo que todavía no sabe es cuánto va a costarle. Por eso la primera respuesta suele ser cara: más activación simpática, más alteración homeostática, más inflamación y mayor desviación de recursos hacia la emergencia.

No confundas sobrevivir con estar adaptado. Sobrevivir significa haber encontrado una solución. Adaptarse empieza cuando la próxima vez necesitas pagar menos por ella.
""";

    private static final String MALE_12_74 = """
Una biomáquina difícil de desorganizar.

Ahora empiezan a aparecer preferencias. Tu arquitectura masculina dispone, por término medio, de mayor masa magra, mayor sección musculoesquelética y una piel más gruesa. Una perforación, un corte o una transferencia brusca de impulso encuentran progresivamente una estructura cuya capacidad para repartir una agresión física crece con especial eficacia.

No eres más difícil de herir porque sientas menos. Dolor y daño nunca fueron la misma variable. Eres más difícil de desorganizar mecánicamente.

Otras adversidades exigen soluciones diferentes. Frente al frío, producción térmica, perfusión y masa corporal negocian el gradiente. Frente al calor, la disipación debe impedir que aquello que produces termine dañándote. Frente a xenobióticos, agua corporal, tejido adiposo, flujo hepático, enzimas y eliminación renal deciden qué concentración termina alcanzando cada órgano. Ninguno de esos sistemas ofrece una ventaja universal por ser hombre: aprenden, pero no todos al mismo ritmo.

La electricidad constituye otra clase de problema. Puedes acostumbrarte a esperar una descarga; tus tejidos no adquieren por ello permiso para conducirla de forma inocua.

ADAPTABILIDAD no consiste en volverte inmune a aquello que sobrevives. Consiste en conseguir que cada nueva exposición necesite alterar menos tu estado basal para obligarte a responder.
""";

    private static final String FEMALE_1_11 = """
Nunca existe exactamente el mismo estado basal.

Una adversidad puede repetirse. Tu estado interno no tiene obligación alguna de hacerlo. Temperatura basal, perfusión periférica, balance hídrico, sensibilidad nociceptiva, señalización endocrina y actividad inmunitaria pueden variar manteniendo, pese a ello, una fisiología perfectamente funcional.

Por eso una misma agresión no siempre llega a la misma configuración fisiológica. El frío puede encontrar una distribución térmica distinta. Una sustancia puede repartirse sobre proporciones diferentes de agua y tejido adiposo. Una respuesta inflamatoria puede operar bajo otra señalización hormonal.

Al principio eso no constituye una ventaja. Es trabajo adicional: tu organismo debe aprender simultáneamente qué está ocurriendo fuera y desde qué estado interno tiene que responder.
""";

    private static final String FEMALE_12_74 = """
Homeostasis sobre una variable móvil.

Aquí aquello que parecía una complicación empieza a revelar su utilidad. La estabilidad femenina nunca necesitó significar inmovilidad. Los sistemas endocrino, térmico, vascular e inmunitario mantienen continuidad mientras cambian algunas de las condiciones internas desde las que trabajan.

Frente a perforación, corte o impacto, tu estructura desarrolla resistencia sin disponer de la misma ventaja media masculina de masa magra, espesor cutáneo y arquitectura musculoesquelética. Después de la lesión aparece otra historia: reparación e inflamación no siguen exactamente la misma cinética y la señalización estrogénica puede favorecer diversos procesos de reparación tisular.

Frente a xenobióticos tampoco existe una ventaja sencilla. Proporción adiposa, volumen de distribución acuosa, actividad enzimática y eliminación pueden hacer que una sustancia desaparezca antes, permanezca más tiempo o alcance una concentración mayor dependiendo de su química.

La vigilancia inmunológica, en cambio, tiende a responder con mayor intensidad. Cromosomas sexuales y señalización hormonal modifican componentes de la inmunidad innata y adaptativa. Esa reactividad también cobra su precio: cuando la discriminación entre lo propio y lo extraño pierde precisión, la misma intensidad defensiva puede dirigirse contra el organismo que debía proteger. Por eso una agresión que compromete la continuidad fisiológica encuentra una respuesta especialmente intensa, mientras que el Frenesí conserva un margen de adaptación mucho menor.

La termorregulación también aprende sin convertir sus diferencias sexuales en una superioridad universal. Y la electricidad sigue sin ofrecer una vía acumulativa de adaptación: puedes anticiparla; no puedes enseñar a tus tejidos a volver inocua su conducción.

La adversidad no necesita encontrarte siempre en el mismo estado para que aprendas a resolverla. Ésa es precisamente la especialidad de una homeostasis móvil.
""";

    private static final String HUMAN_LIMIT = """
just SiO2 maxxing dude
""";
}
