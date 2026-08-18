package domain.runic;

import domain.ability.NullificationPolicy;
import domain.character.sheet.CharacterSheet;
import domain.inventory.equipment.EquipmentState;

import java.util.Objects;
import java.util.Set;

/** Fuente única para activar una Marca Rúnica ordinaria o el set excepcional del Doppelgänger. */
public final class RunicMarkActivityPolicy {
    private RunicMarkActivityPolicy() {}

    public static boolean active(RunicMarkId id, CharacterSheet sheet, EquipmentState equipment,
                                 Set<RunicMarkId> exceptionalActiveMarks,
                                 NullificationPolicy.SuppressionState suppression) {
        Objects.requireNonNull(id); Objects.requireNonNull(sheet);
        if (suppression != null && !NullificationPolicy.runicMarkUsable(suppression)) return false;
        if (exceptionalActiveMarks != null && exceptionalActiveMarks.contains(id)) return true;
        return equipment != null && equipment.hasAwakenedRunicMark(id, sheet);
    }
}
