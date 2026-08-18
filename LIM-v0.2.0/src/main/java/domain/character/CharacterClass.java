package domain.character;

/**
 * Sello espiritual innato del individuo.
 *
 * La clase precede a profesión, hábitos y decisiones: no describe lo que una persona hace, sino la
 * dirección fundamental desde la que su espíritu tiende a materializarse en el cuerpo y en la vida.
 * La biografía decide cuánto llega a expresarse esa afinidad; nunca crea la clase retrospectivamente.
 */
public enum CharacterClass {
    LUCHADOR(
            "Luchador",
            "Espíritu de Fuerza",
            "Naturaleza espiritual masculina cuya vía privilegiada de materialización es la capacidad de imponer el propio organismo sobre la materia. La lucha es sólo una manifestación posible: cargar, construir, proteger, trabajar, resistir una oposición física o transformar el entorno mediante fuerza pueden expresar el mismo sello. FUERZA es su afinidad atributiva; una vida que no la ejercite puede ocultar esa predisposición, pero no convertir al individuo en otra clase."
    ),
    INTELECTUAL(
            "Intelectual",
            "Espíritu de Intelecto",
            "Naturaleza espiritual masculina orientada a convertir experiencia en representación, relación y modelo. No obliga a estudiar ni garantiza educación: predispone a extraer estructura de aquello que se observa y a resolver mediante comprensión antes que mediante mera repetición. INTELIGENCIA es su afinidad atributiva; oportunidad, formación y oficio determinan hasta dónde llega a expresarse."
    ),
    INDOMITO(
            "Indómito",
            "Espíritu de Continuidad",
            "Naturaleza espiritual masculina que conserva dirección propia frente a aquello que normalmente obliga a detenerse, ceder o desviarse. La adversidad no crea al Indómito: revela y desarrolla una disposición que ya estaba presente. AGUANTE es su afinidad atributiva y expresa somáticamente esa continuidad, ya sea en trabajo, marcha, combate, privación, disciplina o cualquier existencia sostenida bajo carga."
    ),
    ESPECIALISTA(
            "Especialista",
            "Espíritu de Precisión",
            "Naturaleza espiritual femenina cuya expresión privilegiada consiste en reducir la distancia entre intención y ejecución. No equivale a poseer un oficio técnico: puede manifestarse en manipulación, coordinación, ritmo, puntería, artesanía o cualquier actividad donde pequeños errores separan resultados cualitativamente distintos. DESTREZA es su afinidad atributiva y la práctica decide cuánto de ese potencial llega a hacerse visible."
    ),
    APODERADO(
            "Apoderado",
            "Espíritu de Convicción",
            "Naturaleza espiritual femenina capaz de mantener una dirección interior suficientemente estable como para actuar aun cuando la información, el entorno o los demás no ofrecen certeza completa. No significa religiosidad, obediencia ni ausencia de duda. FE es su afinidad atributiva: representa la capacidad de consolidar convicciones operativas y sostenerlas hasta que la experiencia obligue realmente a revisarlas."
    ),
    HERALDO(
            "Heraldo",
            "Espíritu de Proyección",
            "Naturaleza espiritual femenina que encuentra su vía privilegiada de expresión en el espacio interpersonal: ser reconocida, transmitir dirección, sostener vínculos y modificar la organización espontánea de quienes comparten una situación. No obliga a ser extrovertida, hermosa ni líder. CARISMA es su afinidad atributiva; la vida social puede desarrollarla, deformarla o dejarla casi sin ejercitar."
    ),
    MAESTRO(
            "Maestro",
            "Espíritu de Perspectiva",
            "Naturaleza espiritual posible tanto en hombre como en mujer cuya afinidad consiste en integrar marcos de experiencia que para otros permanecen separados. No equivale a profesión docente ni a inteligencia académica. CLARIVIDENCIA es su afinidad atributiva y expresa la posibilidad de percibir relaciones, presencias y estructuras que exigen cambiar el punto desde el que se observa, incluso cuando la biografía nunca llegue a desarrollar plenamente esa capacidad."
    );

    private final String label;
    private final String spiritualSeal;
    private final String narrativeDescription;

    CharacterClass(String label, String spiritualSeal, String narrativeDescription) {
        this.label = label;
        this.spiritualSeal = spiritualSeal;
        this.narrativeDescription = narrativeDescription;
    }

    public String label() { return label; }
    public String spiritualSeal() { return spiritualSeal; }
    public String narrativeDescription() { return narrativeDescription; }
}
