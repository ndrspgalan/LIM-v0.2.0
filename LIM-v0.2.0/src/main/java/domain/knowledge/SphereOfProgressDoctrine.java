package domain.knowledge;

/** Full internal doctrine of the Institución de la Esfera del Progreso. */
public final class SphereOfProgressDoctrine {
    private SphereOfProgressDoctrine(){}

    public static CanonVisibility visibility(){ return CanonVisibility.INTERNAL_CANON; }

    public static String truth(){
        return """
                La Institución de la Esfera del Progreso articula el Humanismo Secular dominante de Valerian.
                Su credibilidad nace de éxitos materiales reales: captación electroatmosférica, medicina
                frecuencial, saneamiento, longevidad, locomoción electromagnética, materiales V881 y una
                calidad de vida que convierte antiguos milagros en mantenimiento ordinario.

                La institución transforma ese éxito en una cosmología cerrada. El antiguo Santo, el Padre
                de Todos, criaturas, planos, hazañas heroicas y testimonios incompatibles con su marco se
                degradan a superstición, alegoría o ignorancia premoderna. La población no necesita fingir
                esta convicción: dispone de pruebas cotidianas suficientes para creer sinceramente que la
                humanidad ha superado aquello que antes llamaba sagrado.

                El resultado práctico es una devoción intransigente a un progreso circular: estudiar,
                producir, mejorar, prolongar la vida, acumular competencia, consumir los frutos del avance
                y volver a comenzar. El movimiento se confunde con dirección. La Esfera no necesita prometer
                un paraíso final; convierte la continuidad del propio proceso en su justificación.
                """.strip();
    }
}
