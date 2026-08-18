package domain.character.progression;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import java.util.Objects;

/** Autoridad GOLD del máximo absoluto por tipo de actor. */
public final class AttributeActorCapPolicy {
    private AttributeActorCapPolicy() {}
    public static int absoluteMaximum(AttributeActorScope scope, Attribute attribute) {
        Objects.requireNonNull(scope); Objects.requireNonNull(attribute);
        if ((attribute == Attribute.VITALIDAD || attribute == Attribute.ADAPTABILIDAD) && scope.allowsExtendedVitalityAdaptability())
            return CharacterSheet.ABSOLUTE_MAXIMUM_ATTRIBUTE_VALUE;
        return CharacterSheet.ORDINARY_MAXIMUM_ATTRIBUTE_VALUE;
    }
    public static void requireValid(AttributeActorScope scope, CharacterSheet sheet) {
        Objects.requireNonNull(sheet);
        for (Attribute attribute : Attribute.values()) {
            int max=absoluteMaximum(scope,attribute);
            if(sheet.valueOf(attribute)>max) throw new IllegalArgumentException(scope+" no puede tener "+attribute.label()+"="+sheet.valueOf(attribute)+"; máximo "+max+".");
        }
    }
}
