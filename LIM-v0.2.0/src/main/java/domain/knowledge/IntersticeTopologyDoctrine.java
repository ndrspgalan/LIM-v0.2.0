package domain.knowledge;

/**
 *  internal canon.
 * The Interstice is not "the spirit world". The Veil is not a plane.
 */
public final class IntersticeTopologyDoctrine {
    private IntersticeTopologyDoctrine(){}

    public static CanonVisibility visibility(){ return CanonVisibility.INTERNAL_CANON; }

    public static String interstice(){
        return """
                El Intersticio es la trama de realidad que media entre aquello que un observador considera
                continuidad ordinaria y otras configuraciones posibles de esa misma realidad. No debe
                reducirse a un mundo de espíritus ni a una geografía sobrenatural paralela.
                """.strip();
    }

    public static String veil(){
        return """
                El Velo designa hendiduras des-veladas del Intersticio. Atravesar una no implica viajar
                necesariamente a un supuesto mundo espiritual. Dependiendo de las condiciones, puede
                equivaler a desplazarse por líneas telúricas —teletransporte desde la perspectiva del
                observador— o a modificar el filtro mediante el cual se proyecta la realidad, haciendo
                que aquello que se creía conocido se presente bajo otra configuración perceptiva y causal.
                """.strip();
    }

    public static String telluricLines(){
        return """
                Las líneas telúricas actúan como continuidades privilegiadas del Intersticio. Un Velo
                compatible puede convertir distancia ordinaria en un problema topológico distinto sin
                exigir que el viajero abandone la realidad por un reino de muertos o espíritus.
                """.strip();
    }
}
