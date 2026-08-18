package domain.knowledge;

import domain.character.CharacterClass;

/** Internal meaning of Kenan and Kiara's canonical starting classes. */
public final class ProtagonistSpiritDoctrine {
    private ProtagonistSpiritDoctrine(){}

    public static CharacterClass kenan(){ return CharacterClass.INDOMITO; }
    public static CharacterClass kiara(){ return CharacterClass.HERALDO; }

    public static String truth(){
        return """
                Las clases iniciales describen una orientación previa a las decisiones del jugador.

                Kenan fue escogido como protagonista por su Espíritu Indómito: no porque sea un salvador ni
                porque la trama deba escalar a una guerra planetaria entre bien y mal, sino porque conserva
                dirección propia dentro de un mundo capaz de deformar las condiciones bajo las que otros
                terminan eligiendo. Su historia es una entre muchas. Llegar a ser el Portador de Sueños
                significa cargar con ideas de esperanza para un mundo mejor cuando esas ideas aún no tienen
                un lugar preparado; no cumplir una profecía de salvación.

                Kiara posee Espíritu Heráldico. Su compatibilidad con Kenan no deriva de necesitar una pareja
                para el héroe, sino de una disposición capaz de reconocer, sostener y dar continuidad a una
                dirección que otro se empeña en abrir. Indómito y Heraldo expresan por tanto orientaciones
                vitales canónicas antes de convertirse en herramientas mecánicas.
                """.strip();
    }
}
