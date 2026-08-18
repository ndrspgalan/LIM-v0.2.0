package domain.combat;

import domain.character.sheet.Attribute;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;

import java.util.Objects;

/** Única fuente de verdad para el desbloqueo de técnicas de combate. */
public final class CombatTechniqueUnlockPolicy {
    public static final int DEFLECTION_DEXTERITY_REQUIREMENT = 20;
    public static final int FEINT_DEXTERITY_REQUIREMENT = 35;
    public static final int STAGGERING_STRIKE_STRENGTH_REQUIREMENT = 30;

    public boolean isUnlocked(CombatTechnique technique, CharacterSheet sheet) {
        Objects.requireNonNull(technique, "La técnica no puede ser nula.");
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        return switch (technique) {
            case FEINT -> sheet.valueOf(Attribute.DESTREZA) >= FEINT_DEXTERITY_REQUIREMENT;
            case DEFLECTION -> sheet.valueOf(Attribute.DESTREZA) >= DEFLECTION_DEXTERITY_REQUIREMENT;
            case STAGGERING_STRIKE -> sheet.valueOf(Attribute.FUERZA) >= STAGGERING_STRIKE_STRENGTH_REQUIREMENT;
        };
    }

    public boolean isUnlocked(CombatTechnique technique, CharacterSheet sheet, EquipmentState equipment) {
        Objects.requireNonNull(technique, "La técnica no puede ser nula.");
        Objects.requireNonNull(sheet, "La hoja no puede ser nula.");
        Objects.requireNonNull(equipment, "El equipamiento no puede ser nulo.");
        return switch (technique) {
            case FEINT -> equipment.effectiveAttributeValue(Attribute.DESTREZA, sheet) >= FEINT_DEXTERITY_REQUIREMENT;
            case DEFLECTION -> equipment.effectiveAttributeValue(Attribute.DESTREZA, sheet)
                    >= DEFLECTION_DEXTERITY_REQUIREMENT;
            case STAGGERING_STRIKE -> equipment.effectiveAttributeValue(Attribute.FUERZA, sheet)
                    >= STAGGERING_STRIKE_STRENGTH_REQUIREMENT;
        };
    }

    /** Curva común de control por DESTREZA para DESVIAR, Mirror Parry y arrojadizas incapacitantes. */
    public double controlStunDurationSeconds(int dexterity) {
        int bounded = Math.max(DEFLECTION_DEXTERITY_REQUIREMENT, Math.min(70, dexterity));
        return 2.0 + (bounded - DEFLECTION_DEXTERITY_REQUIREMENT) * (0.7 / 50.0);
    }

    public double deflectionStunDurationSeconds(int dexterity) {
        return controlStunDurationSeconds(dexterity);
    }

    public boolean canFeint() { return false; }
    public boolean canFeint(int dexterity) { return dexterity >= FEINT_DEXTERITY_REQUIREMENT; }

    public boolean canDeflect(int dexterity) {
        return dexterity >= DEFLECTION_DEXTERITY_REQUIREMENT;
    }

    public boolean canStaggerStrike(int strength) {
        return strength >= STAGGERING_STRIKE_STRENGTH_REQUIREMENT;
    }
}
