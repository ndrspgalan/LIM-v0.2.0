package domain.metaprogression;
import domain.character.CharacterClass;
public enum MemorarDocument {
 BESTIARIUM_DE_LA_CAZADORA(CharacterClass.ESPECIALISTA,"Bestiarium de la Cazadora"), BITACORA_DE_LA_ESTRAPERLISTA(CharacterClass.APODERADO,"Bitácora de la Estraperlista"),
 CANONICUM_DEL_ASPIRANTE(CharacterClass.INDOMITO,"Canonicum del Aspirante"), CUADERNO_DE_LA_DIBUJANTE(CharacterClass.HERALDO,"Cuaderno de la Dibujante"),
 GRIMORIO_DEL_MAESTRO(CharacterClass.MAESTRO,"Grimorio del Maestro"), LIBRO_CONTABLE_DEL_INTELECTUAL(CharacterClass.INTELECTUAL,"Libro Contable del Intelectual"),
 PANOPLIA_DEL_MAESTRE(CharacterClass.LUCHADOR,"Panoplia del Maestre");
 private final CharacterClass clazz; private final String label; MemorarDocument(CharacterClass clazz,String label){this.clazz=clazz;this.label=label;} public CharacterClass characterClass(){return clazz;} public String label(){return label;}
}
