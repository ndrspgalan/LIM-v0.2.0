package domain.bestiarium.interstice.transcended;

/**
 *  — catálogo canónico de fuerzas TRANSCENDED.
 *
 * No son valores morales, facciones ni probabilidades. Cada ley es una fuerza incorpórea del
 * Intersticio cuya influencia inclina oportunidades del plano físico hacia uno de dos polos.
 * En 0.5 no manifiesta influencia alguna sobre Kenan.
 */
public enum TranscendedLaw {
    SCARCITY_ABUNDANCE(
            "Escasez", "Abundancia",
            "Inclina la disponibilidad material de oportunidades: hacia la escasez, lo útil tiende a faltar, llegar tarde o hallarse agotado; hacia la abundancia, aparecen excedentes, recursos y capacidad ociosa. Ningún polo constituye una recompensa moral: la abundancia también concentra intereses, población y conflicto; la escasez también puede preservar lugares de esas presiones."),
    APPROPRIATION_RECIPROCITY(
            "Apropiación", "Reciprocidad",
            "Inclina la causalidad de los intercambios. La apropiación convierte relaciones y oportunidades en adquisiciones cuyo retorno termina en Kenan; la reciprocidad prolonga el intercambio en obligaciones, memoria mutua y retornos entre personas. No juzga justicia ni altruismo: una reciprocidad puede sostener solidaridad o vendetta, y una apropiación puede explotar o cortar una dependencia."),
    DEPENDENCE_AUTONOMY(
            "Dependencia", "Autonomía",
            "Inclina la forma en que la continuidad de Kenan queda sostenida. La dependencia hace que capacidades, accesos y soluciones residan crecientemente en otros; la autonomía hace que residan en medios propios. Ningún extremo es superior: depender puede abrir integración y protección; bastarse puede cerrar vínculos y oportunidades que sólo existen cuando alguien resulta necesario para otro."),
    CONSERVATION_TRANSFORMATION(
            "Conservación", "Transformación",
            "Inclina la persistencia de estados del mundo. La conservación favorece que estructuras, lugares, usos e instituciones continúen reconocibles; la transformación favorece que sean sustituidos, reutilizados o conduzcan a configuraciones nuevas. No equivale a tradición frente a progreso: conservar puede mantener una ruina y transformar puede destruir una solución funcional."),
    COMPETITION_COOPERATION(
            "Competencia", "Cooperación",
            "Inclina cómo se organizan actores que persiguen recursos u objetivos concurrentes. La competencia abre trayectorias donde las partes intentan imponerse, excluirse o superarse; la cooperación abre aquellas donde coordinan costes, riesgos o beneficios. Ambas pueden estabilizar o destruir comunidades según el contexto."),
    CONCENTRATION_DISTRIBUTION(
            "Concentración", "Distribución",
            "Inclina dónde terminan poder, recursos, conocimiento y capacidad. La concentración refuerza nodos que ya reúnen medios; la distribución dispersa esos medios entre más actores o lugares. Ningún polo es intrínsecamente benigno: concentrar puede contener una amenaza y distribuirla puede multiplicarla, del mismo modo que repartir capacidad puede impedir un monopolio."),
    CONTINGENCY_DETERMINATION(
            "Contingencia", "Determinación",
            "Inclina cuántas posibilidades permanecen abiertas después de actuar. La contingencia conserva reversibilidad, alternativas y decisiones todavía no fijadas; la determinación convierte posibilidades en compromisos, revelaciones o consecuencias difíciles de deshacer. No es azar frente a destino: describe cuánto de la trayectoria permanece disponible para futuras bifurcaciones." );

    private final String poleZeroLabel;
    private final String poleOneLabel;
    private final String canonicalNarrative;

    TranscendedLaw(String poleZeroLabel,String poleOneLabel,String canonicalNarrative){
        this.poleZeroLabel=poleZeroLabel;
        this.poleOneLabel=poleOneLabel;
        this.canonicalNarrative=canonicalNarrative;
    }
    public String poleZeroLabel(){return poleZeroLabel;}
    public String poleOneLabel(){return poleOneLabel;}
    public String canonicalNarrative(){return canonicalNarrative;}
}
