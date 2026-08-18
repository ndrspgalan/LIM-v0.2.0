package domain.movement;

import domain.character.Gender;
import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import java.util.Objects;

/**
 *  — autoridad física del gasto locomotor de PA.
 * Correr y nadar con brazadas consumen un porcentaje de los PA TOTALES por segundo:
 * 20% hasta el primer softcap de AGUANTE y progresión lineal hasta 10% en el segundo.
 * Escalar y bucear consumen 1 PA/s fijo. Caminar, trotar, caminar agachado, gatear y nadar normal no consumen PA.
 */
public final class LocomotionStaminaPolicy {
    public static final double FIRST_SOFTCAP_TOTAL_STAMINA_PER_SECOND = 0.20;
    public static final double SECOND_SOFTCAP_TOTAL_STAMINA_PER_SECOND = 0.10;

    public double exertionFractionPerSecond(Gender gender, int endurance) {
        Objects.requireNonNull(gender, "El género no puede ser nulo.");
        if (endurance < CharacterSheet.MINIMUM_ATTRIBUTE_VALUE || endurance > CharacterSheet.structuralMaximum(Attribute.AGUANTE)) {
            throw new IllegalArgumentException("AGUANTE fuera del rango canónico.");
        }
        int first = gender == Gender.HOMBRE ? 20 : 15;
        int second = gender == Gender.HOMBRE ? 40 : 30;
        if (endurance <= first) return FIRST_SOFTCAP_TOTAL_STAMINA_PER_SECOND;
        if (endurance >= second) return SECOND_SOFTCAP_TOTAL_STAMINA_PER_SECOND;
        double progress = (endurance - first) / (double) (second - first);
        return FIRST_SOFTCAP_TOTAL_STAMINA_PER_SECOND
                + progress * (SECOND_SOFTCAP_TOTAL_STAMINA_PER_SECOND - FIRST_SOFTCAP_TOTAL_STAMINA_PER_SECOND);
    }

    public double exertionCostPerSecond(Gender gender, CharacterSheet sheet, double maximumStamina) {
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        if (!Double.isFinite(maximumStamina) || maximumStamina <= 0) throw new IllegalArgumentException("PA TOTALES inválidos.");
        return maximumStamina * exertionFractionPerSecond(gender, sheet.valueOf(Attribute.AGUANTE));
    }

    public double runningCostPerSecond(Gender gender, CharacterSheet sheet, double maximumStamina) {
        return exertionCostPerSecond(gender, sheet, maximumStamina);
    }

    public double climbingCostPerSecond(Gender gender, CharacterSheet sheet, double maximumStamina) { return 1.0; }

    public double fastSwimmingCostPerSecond(Gender gender, CharacterSheet sheet, double maximumStamina) {
        return runningCostPerSecond(gender, sheet, maximumStamina);
    }

    public double divingCostPerSecond() { return 1.0; }

    /** Salto horizontal: misma progresión porcentual que correr, aplicada al segundo canónico de maniobra. */
    public double horizontalJumpCost(Gender gender, CharacterSheet sheet, double maximumStamina) {
        return exertionCostPerSecond(gender, sheet, maximumStamina);
    }

    /** Salto vertical: coste fijo e independiente del pool total. */
    public double verticalJumpCost() { return 1.0; }

    public double passiveLocomotionCostPerSecond(LocomotionMode mode) {
        Objects.requireNonNull(mode, "El modo no puede ser nulo.");
        return switch (mode) {
            case WALKING, TROTTING, CROUCH_WALKING, CRAWLING, SWIMMING -> 0.0;
            case CLIMBING, DIVING -> 1.0;
            case RUNNING, FAST_SWIMMING -> throw new IllegalArgumentException("RUNNING/FAST_SWIMMING requieren actor y PA TOTALES.");
        };
    }
}
