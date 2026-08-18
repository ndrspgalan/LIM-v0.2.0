package domain.movement;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;

import java.util.Objects;

/** Única fuente de verdad para los requisitos duros de las técnicas de exploración. */
public final class ExplorationTechniqueUnlockPolicy {
    public static final int RIDE_DEXTERITY_REQUIREMENT = 20;
    public static final int RIDE_CHARISMA_REQUIREMENT = 25;
    public static final int CLIMB_STRENGTH_REQUIREMENT = 20;
    public static final int CLIMB_DEXTERITY_REQUIREMENT = 20;
    public static final int SWIM_STRENGTH_REQUIREMENT = 15;
    public static final int SWIM_DEXTERITY_REQUIREMENT = 15;

    public boolean isUnlocked(ExplorationTechnique technique, CharacterSheet sheet) {
        Objects.requireNonNull(technique, "La técnica no puede ser nula.");
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        return switch (technique) {
            case RIDE -> sheet.valueOf(Attribute.DESTREZA) >= RIDE_DEXTERITY_REQUIREMENT
                    && sheet.valueOf(Attribute.CARISMA) >= RIDE_CHARISMA_REQUIREMENT;
            case CLIMB -> sheet.valueOf(Attribute.FUERZA) >= CLIMB_STRENGTH_REQUIREMENT
                    && sheet.valueOf(Attribute.DESTREZA) >= CLIMB_DEXTERITY_REQUIREMENT;
            case SWIM -> sheet.valueOf(Attribute.FUERZA) >= SWIM_STRENGTH_REQUIREMENT
                    && sheet.valueOf(Attribute.DESTREZA) >= SWIM_DEXTERITY_REQUIREMENT;
        };
    }
    public boolean isUnlocked(ExplorationTechnique technique, CharacterSheet sheet, EquipmentState equipment) {
        Objects.requireNonNull(technique, "La técnica no puede ser nula.");
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        int dexterity = equipment.effectiveAttributeValue(Attribute.DESTREZA, sheet);
        int strength = equipment.effectiveAttributeValue(Attribute.FUERZA, sheet);
        int charisma = equipment.effectiveAttributeValue(Attribute.CARISMA, sheet);
        return switch (technique) {
            case RIDE -> dexterity >= RIDE_DEXTERITY_REQUIREMENT
                    && charisma >= RIDE_CHARISMA_REQUIREMENT;
            case CLIMB -> strength >= CLIMB_STRENGTH_REQUIREMENT
                    && dexterity >= CLIMB_DEXTERITY_REQUIREMENT;
            case SWIM -> strength >= SWIM_STRENGTH_REQUIREMENT
                    && dexterity >= SWIM_DEXTERITY_REQUIREMENT;
        };
    }

}
