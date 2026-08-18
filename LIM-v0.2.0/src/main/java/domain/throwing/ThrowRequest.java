package domain.throwing;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;

import java.util.Objects;

/** Variables propias de una ejecución concreta de lanzamiento. */
public record ThrowRequest(int strength, int dexterity, double characterHeightMeters, double releaseAngleDegrees) {
    public ThrowRequest {
        if (strength < CharacterSheet.MINIMUM_ATTRIBUTE_VALUE || strength > CharacterSheet.structuralMaximum(Attribute.FUERZA)) {
            throw new IllegalArgumentException("FUERZA debe estar entre 1 y 75.");
        }
        if (dexterity < CharacterSheet.MINIMUM_ATTRIBUTE_VALUE || dexterity > CharacterSheet.structuralMaximum(Attribute.DESTREZA)) {
            throw new IllegalArgumentException("DESTREZA debe estar entre 1 y 75.");
        }
        if (!Double.isFinite(characterHeightMeters) || characterHeightMeters <= 0) {
            throw new IllegalArgumentException("La altura corporal debe ser positiva y finita.");
        }
        if (!Double.isFinite(releaseAngleDegrees) || releaseAngleDegrees < -90 || releaseAngleDegrees > 90) {
            throw new IllegalArgumentException("El ángulo de lanzamiento debe estar entre -90 y 90 grados.");
        }
    }

    public static ThrowRequest from(CharacterSheet sheet, double characterHeightMeters, double releaseAngleDegrees) {
        Objects.requireNonNull(sheet, "La hoja de personaje no puede ser nula.");
        return new ThrowRequest(
                sheet.valueOf(Attribute.FUERZA),
                sheet.valueOf(Attribute.DESTREZA),
                characterHeightMeters,
                releaseAngleDegrees
        );
    }
    public static ThrowRequest from(CharacterSheet sheet, EquipmentState equipment,
                                    double characterHeightMeters, double releaseAngleDegrees) {
        Objects.requireNonNull(sheet, "La hoja de personaje no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        return new ThrowRequest(
                equipment.effectiveAttributeValue(Attribute.FUERZA, sheet),
                equipment.effectiveAttributeValue(Attribute.DESTREZA, sheet),
                characterHeightMeters,
                releaseAngleDegrees
        );
    }

}
