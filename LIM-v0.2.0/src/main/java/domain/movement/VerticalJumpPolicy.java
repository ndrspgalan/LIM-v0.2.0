package domain.movement;

import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import java.util.Objects;

/**  — altura vertical canónica como fracción de la altura corporal según AGUANTE y género. */
public final class VerticalJumpPolicy {
    public static final double FIRST_SOFTCAP_HEIGHT_FRACTION = 0.18;
    public static final double MALE_SECOND_SOFTCAP_HEIGHT_FRACTION = 0.40;
    public static final double FEMALE_SECOND_SOFTCAP_HEIGHT_FRACTION = 0.35;

    public double heightFraction(Gender gender, int endurance) {
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        if (endurance < CharacterSheet.MINIMUM_ATTRIBUTE_VALUE || endurance > CharacterSheet.structuralMaximum(Attribute.AGUANTE))
            throw new IllegalArgumentException("AGUANTE fuera del rango canónico.");
        int first = gender == Gender.HOMBRE ? 20 : 15;
        int second = gender == Gender.HOMBRE ? 40 : 30;
        double maximum = gender == Gender.HOMBRE ? MALE_SECOND_SOFTCAP_HEIGHT_FRACTION : FEMALE_SECOND_SOFTCAP_HEIGHT_FRACTION;
        if (endurance <= first) return FIRST_SOFTCAP_HEIGHT_FRACTION;
        if (endurance >= second) return maximum;
        double progress = (endurance - first) / (double) (second - first);
        return FIRST_SOFTCAP_HEIGHT_FRACTION + progress * (maximum - FIRST_SOFTCAP_HEIGHT_FRACTION);
    }

    public double heightMeters(Gender gender, CharacterSheet sheet, double actorHeightMeters) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        if (!Double.isFinite(actorHeightMeters) || actorHeightMeters <= 0) throw new IllegalArgumentException("Altura corporal inválida.");
        return actorHeightMeters * heightFraction(gender, sheet.valueOf(Attribute.AGUANTE));
    }
}
