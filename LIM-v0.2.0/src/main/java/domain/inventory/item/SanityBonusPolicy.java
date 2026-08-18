package domain.inventory.item;

import domain.character.sheet.CharacterSheet;

import java.util.Objects;

/** Resuelve la bonificación efectiva de cordura de un abalorio equipado. */
@FunctionalInterface
public interface SanityBonusPolicy {
    double resolve(CharacterSheet sheet, AccessoryContext context);

    static SanityBonusPolicy conditionalFixed(
            domain.character.sheet.Attribute attribute,
            int minimum,
            double bonus
    ) {
        Objects.requireNonNull(attribute, "El atributo condicional no puede ser nulo.");
        if (minimum < 1 || minimum > 120) {
            throw new IllegalArgumentException("El umbral condicional debe estar entre 1 y 120.");
        }
        return (sheet, context) -> sheet.valueOf(attribute) >= minimum ? bonus : 0.0;
    }

    static SanityBonusPolicy intersticeCold() {
        return (sheet, context) -> {
            Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
            Objects.requireNonNull(context, "El contexto del abalorio no puede ser nulo.");
            return context.inInterstice() ? 3.0 : 0.0;
        };
    }

    static SanityBonusPolicy solarWarmth() {
        return (sheet, context) -> {
            Objects.requireNonNull(sheet, "La hoja del personaje no puede ser nula.");
            Objects.requireNonNull(context, "El contexto del abalorio no puede ser nulo.");
            return context.dayPhase() == DayPhase.NIGHT ? 3.0 : 0.0;
        };
    }
}
