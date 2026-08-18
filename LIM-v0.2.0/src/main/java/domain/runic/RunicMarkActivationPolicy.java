package domain.runic;

import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;
import domain.persona.PersonaProfile;

import java.util.Objects;

/** única fuente para resolver la marca activa ordinaria o la elegida tras [VOLUNTAD MAYOR]. */
public final class RunicMarkActivationPolicy {
    private RunicMarkActivationPolicy() {}

    public static boolean isActive(RunicMarkId id, CharacterSheet sheet, EquipmentState equipment, PersonaProfile persona) {
        Objects.requireNonNull(id); Objects.requireNonNull(sheet);
        if (persona != null && persona.allRunicMarksUnlocked()) {
            return persona.equippedRunicMark().filter(id::equals).isPresent();
        }
        return equipment != null && equipment.hasAwakenedRunicMark(id, sheet);
    }
}
