package domain.knowledge;

/**
 * Internal political doctrine. Describes canonical intent and effects, not a real-world operational recipe.
 */
public final class ControlledSocialChaosDoctrine {
    private ControlledSocialChaosDoctrine(){}

    public static CanonVisibility visibility(){ return CanonVisibility.INTERNAL_CANON; }

    public static String truth(){
        return """
                La ausencia de digitalización masiva priva a las élites de un microcontrol continuo sobre
                cada conversación, deseo, desplazamiento y relación. V881 compensa esa limitación mediante
                control macrocausal: no necesita decidir por cada individuo cuando puede modificar las
                condiciones estadísticas bajo las que miles de individuos deciden.

                El Caos Social Controlado altera presión económica, seguridad, acceso territorial,
                movimientos de población, mercados clandestinos y condiciones ambientales o sanitarias.
                Entre sus manifestaciones canónicas figuran crisis sanitarias localizadas, alteraciones
                deliberadas de agua, alimentos o vegetación mediante capacidades frecuenciales/ionizantes,
                incendios inducidos por perturbaciones electrostáticas, tormentas estáticas, tolerancia o
                estímulo selectivo de contrabando, drogas y prostitución coercitiva, degradación de rutas
                y creación de focos de violencia capaces de desplazar población o capital.

                El objetivo no es arruinar indiscriminadamente la calidad de vida. V881 necesita una
                población técnicamente competente. El mecanismo busca orientar trayectorias sin asumir
                el coste de vigilar cada mente: una persona sigue sintiendo que elige, aunque el conjunto
                de opciones materialmente viables haya sido previamente deformado.

                Esta doctrina existe en el canon como capacidad y práctica política. Su descripción de
                dominio no especifica parámetros, frecuencias ni procedimientos reproducibles.
                """.strip();
    }
}
