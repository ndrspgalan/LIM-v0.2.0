package domain.knowledge;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import java.util.Objects;

/**
 * Autoridad GOLD para visibilidad/activación de propiedades condicionadas por atributos.
 * No existe ya un umbral universal de FE ni una política exclusiva de CLARIVIDENCIA.
 */
public final class PropertyKnowledgePolicy {
    private PropertyKnowledgePolicy() {}

    public static boolean requirementMet(CharacterSheet sheet, Attribute attribute, int minimum) {
        Objects.requireNonNull(sheet,"La hoja del personaje no puede ser nula.");
        if(attribute==null) return minimum==0;
        if(minimum<1 || minimum>120) throw new IllegalArgumentException("El umbral debe estar entre 1 y 120.");
        return sheet.valueOf(attribute)>=minimum;
    }

    /** Una propiedad marcada hidden sólo se revela al cumplirse su requisito propio. */
    public static boolean visible(CharacterSheet sheet, boolean hidden, Attribute attribute, int minimum) {
        return !hidden || requirementMet(sheet,attribute,minimum);
    }
}
